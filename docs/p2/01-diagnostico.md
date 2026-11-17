# GameSaleDB - diagnóstico da versão recebida

Base: gamesale.db.zip enviado pelo responsável. Revisão estática, sem execução no Android.

## Arquitetura confirmada

- MainActivity hospeda Compose/Material3; AppNavigation concentra as rotas games, wishlist e apiGameDetails.
- GameViewModel consulta GameRepository; Retrofit/ITAD fornece jogos e preços. GameDao e PriceDao guardam cache em Room.
- GameSaleDatabase está na versão 7, com migrações incrementais 1 até 7, sem fallback destrutivo.
- WishlistViewModel escreve primeiro no Room, mantém PENDING/SYNCING/SYNCED e ADD/DELETE, e envia ao FirebaseWishlistService quando o app detecta conexão.
- DELETE é um tombstone local, ocultado por WishlistDao.getAll(), até confirmação remota.
- GameNoteViewModel mantém notas pessoais por gameId no Room; NetworkMonitor observa a conectividade.
- FirebaseWishlistService grava gameId/title/slug em wishlist/{gameId}. Não há leitura remota nesse serviço.

## Firebase: situação e limites da verificação

O responsável informou que o Firebase voltou a funcionar. O ZIP contém google-services.json, o plugin Google Services e a dependência Firestore. Isso não comprova as regras publicadas nem a autorização do banco. O ZIP não contém firestore.rules ou firebase.json, mas já contém Firebase Authentication: a dependência firebase-auth está declarada e FirebaseWishlistService chama signInAnonymously().await() quando não existe usuário autenticado, antes de adicionar/excluir. Essa versão é mais recente que os arquivos avulsos enviados anteriormente. Nenhuma regra publicada foi lida e nenhuma operação remota foi executada nesta revisão.

Não alterar o projeto Firebase, a coleção, seus documentos ou a estrutura remota para realizar a P2. A segurança permanente continua pendente de auditoria das regras publicadas: não renovar apenas uma data de teste e não liberar acesso irrestrito. A autenticação anônima existente identifica a sessão, mas o caminho wishlist/{gameId} e os campos gravados não identificam o proprietário do documento; não atribuir registros antigos a usuários sem definir sua origem. Antes de uma futura migração, produzir backup e validar acesso autorizado/negado no emulador. Isso exige uma etapa própria e autorização para publicação.

## Problemas/riscos encontrados por leitura

1. Firestore .set(data) substitui o documento: campos que não estão no mapa podem ser perdidos. Não foi encontrado campo adicional no esquema enviado; nenhuma perda foi observada.
2. A sincronização só lê PENDING: uma interrupção após marcar SYNCING pode deixar operação sem nova tentativa. Não reproduzido em dispositivo.
3. syncPendingWishlist pode ser iniciado concorrentemente e confirmar uma operação antiga depois de um novo toggle; faltam serialização/versionamento das operações. Não reproduzido.
4. Atualização de cache apaga e insere em chamadas separadas, sem transação conjunta no repositório. Uma falha intermediária pode deixar cache vazio.
5. Capturas genéricas de Exception podem ocultar erros e cancelamento de corrotinas; mensagens de cache atribuem falhas diversas à ausência de internet.
6. NetworkMonitor registra callback sem descarte; nova criação do grafo pode deixar observadores anteriores registrados.
7. Seleção de jogo é mantida no ViewModel: restauração do processo na tela de detalhes pode não recuperar o jogo.
8. Nota usa remember e feedback otimista, antes da confirmação da gravação; rotação e falha de persistência precisam de testes.
9. O ZIP inclui caches/build e local.properties com configuração local. Não versionar local.properties nem redistribuir sua chave ITAD. Não usar APK antigo como evidência da P2.

Esses achados são riscos de inspeção, não resultados de testes manuais. As correções não relacionadas à integração dos lembretes ficam fora desta mudança para reduzir regressões. Devem ser priorizadas em uma rodada separada.

## Escopo da P2

Necessidade: lembrar o usuário de conferir ofertas de um jogo salvo, em uma data escolhida. Não é alerta automático de queda de preço.

Recurso: AlarmManager com alarme inexato, BroadcastReceiver não exportado, canal e notificação local. Dados pequenos em SharedPreferences, separados do Room e do Firestore, conforme a base didática.

Critérios observáveis:

- CA01: para um jogo da wishlist, título/mensagem preenchidos, notificações habilitadas e horário futuro, criar um registro local e solicitar um alarme com ID exclusivo; a lista permanece após reabrir.
- CA02: ao receber o alarme, publicar a notificação sem rede; ao tocar, abrir os detalhes do jogo vinculado. Rejeitar campos vazios/horário passado e orientar quando notificações estão bloqueadas, sem confirmar agendamento.
- CA03: cancelar um lembrete agendado antes do disparo, preservar histórico e impedir sua publicação futura; outros lembretes permanecem independentes.

Não há mudança de versão do Room, remoção de tabelas, alteração de dependências ou publicação externa prevista.
