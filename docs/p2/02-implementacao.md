# Implementação e demonstração

## Necessidade e fluxo

Usuário: pessoa que salva jogos e esquece de voltar para comparar ofertas. Evento escolhido: conferir as ofertas de um jogo em uma data decidida pelo usuário. Não há monitoramento automático de preço nem push remoto.

Lista de desejos → Criar lembrete no jogo → conferir título/mensagem → escolher data e hora → permitir notificações, se necessário → Agendar → consultar histórico → voltar à tela inicial do celular → receber notificação → tocar para abrir detalhes do mesmo jogo.

O botão Lembretes, no topo da wishlist, permite acessar todo o histórico, inclusive para jogos removidos da wishlist. Remover o jogo não cancela lembretes já criados: o usuário deve cancelá-los explicitamente. O histórico guarda gameId, título e slug para permitir abrir o jogo mesmo se ele não estiver mais na wishlist/cache.

## Arquivos e responsabilidades

| Arquivo | Responsabilidade |
| --- | --- |
| reminders/GameReminder.kt | Dados, estados e validação pura de campos/horário |
| reminders/ReminderStore.kt | JSON em SharedPreferences, ID crescente, histórico; não apaga dados quando há erro de leitura |
| reminders/ReminderScheduler.kt | Canal, verificação de permissão/canal, AlarmManager, PendingIntent, publicação e cancelamento |
| reminders/ReminderReceiver.kt | Recebimento curto e local; relê o histórico antes de publicar |
| ui/reminders/ReminderViewModel.kt | Operações fora da thread principal e atualização da lista |
| ui/reminders/RemindersScreen.kt | Formulário, permissão contextual, data/hora, feedback, histórico e confirmação de cancelamento |
| MainActivity.kt / AppNavigation.kt | Abertura fria e onNewIntent; recuperar o vínculo local e navegar aos detalhes |
| WishlistScreen.kt | Ação Criar lembrete para cada jogo |
| AndroidManifest.xml / ic_reminder.xml | POST_NOTIFICATIONS, receiver não exportado e ícone monocromático |

## Permissões e falhas

No Android 13+, o botão Permitir notificações solicita POST_NOTIFICATIONS. Conceder permissão não agenda automaticamente: é necessário tocar em Agendar. Há acesso às configurações do app para recusa persistente ou canal bloqueado. O canal só é criado a partir da API 26; as verificações têm guardas para a minSdk 24 original. Não são solicitados alarmes exatos ou permissão de boot.

Antes de gravar/agendar, validar gameId, campos após espaços e quando > agora; confirmar que o jogo continua na wishlist. O agendador confere novamente app/permissão/canal. Bloqueio no momento do disparo produz status BLOCKED. Falha ao registrar um alarme produz FAILED e mensagem de falha. Nenhum erro deve ser apresentado como agendamento bem-sucedido.

Cada alarme é explícito, imutável e identificado pelo ID do lembrete. O receiver carrega os dados persistidos pelo ID e ignora registros cancelados/processados. Cancelamento é serializado com o receiver no processo do app, marca o histórico e remove o alarme. Não há rede no receiver.

## Estados

| Estado | Significado |
| --- | --- |
| SCHEDULED | Registro salvo e solicitação de agendamento feita; não prova que um alarme continua ativo após reboot/force-stop |
| PUBLISHED | notify foi chamado sem exceção; não confirma leitura, som ou visualização pelo usuário |
| CANCELLED | Cancelamento registrado; alarme removido quando ainda pendente |
| BLOCKED | Notificações indisponíveis quando o receiver processou o evento |
| FAILED | Falha ao registrar o alarme ou publicar |

Campos, vínculo e horário usam rememberSaveable; data só é aplicada ao confirmar também a hora. Cancelar os seletores não confirma o formulário. Operações com SharedPreferences do formulário rodam em Dispatchers.IO; o receiver faz apenas trabalho curto local. Um listener atualiza a tela após mudanças do receiver, e ON_RESUME atualiza estado de permissão ao voltar das configurações.

## Limitações conhecidas e explícitas

- setAndAllowWhileIdle é inexato e nunca deve ser demonstrado como garantia de disparo em 30 segundos; Android pode atrasar por Doze/economia de bateria. Registrar horário escolhido e observado.
- Não restaura alarmes após reiniciar o celular. A lista continua salva, mas SCHEDULED não comprova um alarme ativo. Cancelar e criar novamente. Backup/restauração de dados também não restaura alarmes.
- Forçar parada pode impedir entrega. Remover a tela de recentes não é a mesma ação. Restrições do fabricante também afetam entrega.
- Alterar relógio/fuso exige tratamento adicional. Salva instante absoluto; display usa fuso atual.
- Não há edição, recorrência, exclusão do histórico, sincronização remota de lembretes ou confirmação de leitura.
- SharedPreferences e AlarmManager não constituem transação atômica: interrupção entre gravar e agendar pode deixar registro sem alarme; falha entre publicar e salvar estado pode deixar status desatualizado. Adequado à base didática, não a alertas críticos.
- Histórico é pequeno e local; para alto volume, evoluir para Room. A lista não deve armazenar segredos. Conteúdo tem visibilidade privada na tela bloqueada, sujeita às configurações do dispositivo.
- Busca, cache, wishlist, notas, migrações e Firestore foram preservados em código. Preservação de código não substitui regressão em dispositivo.

## Roteiro de demonstração (5 a 8 minutos)

1. Mostrar busca, preços, nota e wishlist existentes.
2. Abrir um jogo da wishlist e criar lembrete alguns minutos no futuro. Conferir mensagem sobre horário aproximado.
3. Criar outro lembrete; cancelar apenas o segundo, confirmar histórico e IDs diferentes.
4. Mostrar campos vazios/horário passado bloqueados e recusa/configurações de notificações.
5. Colocar o app em segundo plano e, se aplicável, ativar modo avião depois de agendar; aguardar observando a margem de atraso. Registrar hora real, ambiente e versão.
6. Tocar na notificação; mostrar que abre o jogo vinculado. Não confundir teste imediato com esse passo.
7. Mostrar testes automatizados, planilha, evidências reais e limitações. Se não foi executado, dizer isso.

## Referências

- Materiais fornecidos: Meu aplicativo em uso real (P2, 6 aulas); Aula 2 da ideia ao primeiro fluxo; O Aplicativo Lembra por Você; instruções textuais das aulas 2 e 3; planilha do professor.
- https://developer.android.com/develop/background-work/services/alarms
- https://developer.android.com/develop/ui/views/notifications/notification-permission
- https://developer.android.com/develop/ui/views/notifications/channels
- https://developer.android.com/reference/android/app/PendingIntent
- https://firebase.google.com/docs/firestore/security/insecure-rules
- https://firebase.google.com/docs/firestore/manage-data/add-data
