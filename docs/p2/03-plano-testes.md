# Plano e casos de teste da P2

## Escopo e requisitos

RF01: criar e listar lembrete local com ID único e vínculo com jogo da wishlist.
RF02: publicar notificação local no momento permitido pelo sistema e oferecer teste imediato distinto.
RF03: validar campos/horário e tratar permissão e canal indisponíveis.
RF04: cancelar lembrete antes do disparo e preservar histórico.
RF05: persistir campos/vínculos localmente, suportar reabertura/offline e documentar limites de reboot.
RF06: toque na notificação abre o jogo vinculado com Activity fria ou já aberta.
RG01/02/03: preservar busca/cache, wishlist/sincronização e notas da P1.
RT01: suporte do wrapper e compilação do projeto.

## Estratégia e ambiente

Executar primeiro CT17 e CT18 no Android Studio. Usar dispositivo/emulador API 33+ para permissão em runtime; repetir o fluxo principal em API 26 e, se disponível, API 24 para as guardas da minSdk original. Registrar modelo/API, build, fuso, estado do canal/permissão, rede, bateria e modo de abertura do app.

Não foram informados nomes/matrículas completos, URL exata do repositório ou parceiro externo. Preencher esses dados antes da entrega. Não inventar autoria/contribuições. Usar dados fictícios e ocultar chaves nas capturas.

Condição de aprovação: resultado obtido coincide com o esperado, com versão e evidência. Em alarme inexato, registrar horário solicitado e observado; não exigir precisão de segundos. Definir e registrar janela de observação. Falta de entrega dentro dela exige investigação de canal, permissão, Doze e fabricante antes de concluir causa.

Atualização de 17/11/2026: build aprovado no Windows e cinco testes de ReminderValidationTest executados, sem falhas ou ignorados. Os testes unitários rodam na JVM do computador. Verificações manuais no emulador Medium Phone, API 37.0, Android 17, conforme informação do executor.

CT05 aprovado por relato: solicitado 09:51 e entregue 09:52 após Clear all. Cancelamento (CT04) e abertura do jogo (CT11) confirmados no comportamento básico, com os demais passos/variantes ainda pendentes para conclusão dos casos completos. Os outros 13 casos manuais não têm execução confirmada. Tentativas iniciais bloqueadas, falha de compilação e retestes permanecem na planilha. Ver docs/p2/05-relatorio-qualidade.md.

## Técnicas

### T01 - Particionamento de equivalência

Entradas válidas/inválidas e acesso permitido/bloqueado produzem comportamentos diferentes.

Regras: Campos preenchidos/vazios/só espaços; permissão/canal ativos ou bloqueados; horário futuro ou passado.

Casos: CT01, CT02, CT03, CT05, CT06, CT09, CT12, CT17

### T02 - Transição de estados

Verifica agendamento, cancelamento e processamento sem perder registros ou afetar outro ID.

Regras: Formulário > SCHEDULED > PUBLISHED, CANCELLED, BLOCKED ou FAILED; cancelado não publica; reabrir preserva vínculo.

Casos: CT04, CT05, CT07, CT08, CT09, CT10, CT11, CT12, CT13, CT14, CT15, CT16, CT18, CT19

### T03 - Análise de valor limite

Valida a fronteira estrita quando > agora com relógio fixo para não depender de tempo real.

Regras: now-1 e now rejeitados; now+1 aceito na regra pura. Na UI, seletor em minutos.

Casos: CT06, CT17

## Casos reproduzíveis

### CT01 - Conferir canal com teste imediato

Requisito: RF02. Técnica: T01. Prioridade: Alta.

Pré-condições: App instalado; notificações e canal habilitados.

Dados: Sem dados pessoais.

Passos: 1. Abrir wishlist > Lembretes. 2. Tocar Testar notificação agora. 3. Observar bandeja. 4. Tocar a notificação.

Esperado: Notificação de teste enviada; toque abre app. Não criar lembrete e não usar como prova do agendador.

### CT02 - Negar permissão e tentar agendar

Requisito: RF03. Técnica: T01. Prioridade: Alta.

Pré-condições: Android 13+; permissão não concedida; jogo salvo.

Dados: Título e mensagem válidos; agora + 10 min.

Passos: 1. Criar lembrete no jogo. 2. Pedir permissão e negar. 3. Tocar Agendar. 4. Conferir histórico. 5. Abrir configurações.

Esperado: Orientação clara; nenhuma confirmação de sucesso ou novo lembrete. Configurações acessíveis. Concessão posterior exige novo toque em Agendar.

### CT03 - Rejeitar campos vazios e apenas espaços

Requisito: RF03. Técnica: T01. Prioridade: Alta.

Pré-condições: Jogo salvo; notificações habilitadas.

Dados: Título vazio, mensagem vazia, espaços; horário futuro.

Passos: 1. Deixar título vazio e agendar. 2. Repetir com mensagem vazia. 3. Repetir com apenas espaços. 4. Comparar histórico antes/depois.

Esperado: Cada entrada inválida produz mensagem de validação; nenhuma adiciona registro ou alarme.

### CT04 - Cancelar antes do disparo

Requisito: RF04. Técnica: T02. Prioridade: Alta.

Pré-condições: Um lembrete agendado para pelo menos 10 min no futuro.

Dados: Lembrete A.

Passos: 1. Abrir histórico. 2. Cancelar A e confirmar. 3. Reabrir o app. 4. Aguardar além do horário com margem para alarmes inexatos.

Esperado: A fica Cancelado e permanece no histórico; não publica. Registrar janela de observação; cancelamento não prova ausência futura ilimitada.

### CT05 - Entregar lembrete futuro com app em segundo plano

Requisito: RF01/RF02. Técnica: T01/T02. Prioridade: Alta.

Pré-condições: Jogo salvo; canal e permissão ativos; não forçar parada.

Dados: Título Conferir Hades; mensagem Ver ofertas; horário + 5 min.

Passos: 1. Agendar. 2. Anotar hora escolhida, ID e ambiente. 3. Ir para tela inicial. 4. Aguardar entrega com margem de atraso. 5. Registrar horário real e captura.

Esperado: Não publicar antes do horário; notificação posterior sujeita ao atraso do sistema. Registrar hora observada. Teste imediato não substitui esta execução.

### CT06 - Rejeitar passado/agora e aceitar limite futuro

Requisito: RF03. Técnica: T01/T03. Prioridade: Alta.

Pré-condições: Relógio conhecido; jogo salvo; notificações habilitadas.

Dados: now-60000, now-1, now, now+1 na regra pura; passado/futuro em minutos na UI.

Passos: 1. Selecionar minuto passado na UI e tentar agendar. 2. Selecionar minuto atual quando já iniciado e repetir. 3. Selecionar minuto futuro. 4. Executar teste de limites puro CT17.

Esperado: Passado e instante atual rejeitados sem novo registro. Futuro válido aceito. UI usa minutos; now+1 ms é verificado no teste puro.

### CT07 - Criar dois lembretes e cancelar apenas um

Requisito: RF01/RF04. Técnica: T02. Prioridade: Alta.

Pré-condições: Um ou dois jogos na wishlist; notificações habilitadas.

Dados: A e B com títulos diferentes; horários futuros distintos.

Passos: 1. Criar A e B. 2. Comparar IDs. 3. Cancelar A. 4. Observar B no horário com margem do sistema.

Esperado: IDs distintos; A cancelado; B continua agendado e publica independentemente.

### CT08 - Reabrir offline e entregar sem internet

Requisito: RF05. Técnica: T02. Prioridade: Alta.

Pré-condições: Lembrete futuro criado; não reiniciar celular nem forçar parada.

Dados: Jogo já salvo localmente; modo avião.

Passos: 1. Agendar com notificações habilitadas. 2. Ativar modo avião. 3. Sair e reabrir app. 4. Conferir título/mensagem/horário/ID. 5. Ir para tela inicial e observar entrega.

Esperado: Lista preservada; publicação local funciona sem internet. Anotar atraso observado. Preços podem depender do cache.

### CT09 - Bloquear canal depois de agendar

Requisito: RF03. Técnica: T01/T02. Prioridade: Alta.

Pré-condições: Android 8+; lembrete agendado; inicialmente permitido.

Dados: Canal Lembretes de jogos.

Passos: 1. Agendar. 2. Bloquear canal nas configurações. 3. Voltar e conferir indicador. 4. Aguardar processamento do alarme. 5. Atualizar lista.

Esperado: Nenhuma notificação publicada; ao processar, status Bloqueado. Reativar não recria alarme automaticamente.

### CT10 - Rotacionar durante preenchimento

Requisito: RF05. Técnica: T02. Prioridade: Alta.

Pré-condições: Formulário aberto para jogo salvo.

Dados: Título/mensagem personalizados; horário futuro.

Passos: 1. Preencher. 2. Escolher horário e confirmar. 3. Rotacionar dispositivo. 4. Conferir campos. 5. Abrir seletores e cancelar.

Esperado: Campos e horário confirmados preservados. Cancelar seletores não substitui horário anterior nem agenda.

### CT11 - Tocar notificação com app fechado e aberto

Requisito: RF06. Técnica: T02. Prioridade: Alta.

Pré-condições: Notificação real publicada para jogo conhecido; dados do lembrete persistidos.

Dados: Jogo A; repetir com app aberto em jogo B e com modo avião.

Passos: 1. Tocar notificação com Activity não aberta. 2. Repetir com app aberto em B e outra notificação para A. 3. Repetir sem rede.

Esperado: Abrir detalhes de A nos dois estados; exibir dados locais e preços em cache quando disponíveis. Nenhuma troca do vínculo para B.

### CT12 - Pesquisar jogos e recuperar preços offline

Requisito: RG01. Técnica: T01/T02. Prioridade: Alta.

Pré-condições: Chave ITAD configurada; internet inicialmente disponível.

Dados: Hades e Cyberpunk 2077.

Passos: 1. Buscar. 2. Abrir detalhes e preços. 3. Voltar. 4. Ativar modo avião. 5. Abrir jogo em cache.

Esperado: Busca e detalhes continuam disponíveis conforme API; preços salvos aparecem offline com indicação de cache.

### CT13 - Adicionar/remover offline e sincronizar

Requisito: RG02. Técnica: T02. Prioridade: Alta.

Pré-condições: Firebase do responsável funcionando; acesso autorizado; dados fictícios.

Dados: Um jogo de teste; registrar estado remoto antes.

Passos: 1. Adicionar offline. 2. Reabrir ainda offline. 3. Reconectar e observar PENDING/SYNCED. 4. Repetir remoção offline. 5. Conferir documento remoto.

Esperado: Persistência local e sincronização mantidas; tombstone removido após confirmação. Registrar falhas preexistentes separadamente; não declarar segurança das regras por este teste.

### CT14 - Salvar e recuperar nota pessoal

Requisito: RG03. Técnica: T02. Prioridade: Alta.

Pré-condições: Jogo acessível; não apagar dados do aplicativo.

Dados: Nota fictícia Comprar em promoção.

Passos: 1. Salvar nota. 2. Fechar/reabrir app. 3. Abrir mesmo jogo. 4. Alterar nota e repetir.

Esperado: Nota vinculada ao jogo recuperada e atualização preservada.

### CT15 - Remover jogo com lembrete ativo

Requisito: RF05/RF06. Técnica: T02. Prioridade: Alta.

Pré-condições: Lembrete futuro salvo para jogo A.

Dados: Jogo A.

Passos: 1. Remover A da wishlist. 2. Abrir histórico pelo botão Lembretes. 3. Conferir A. 4. Aguardar disparo e tocar ou cancelar explicitamente.

Esperado: Histórico e vínculo preservados; remoção não cancela implicitamente; notificação abre A pelos dados persistidos. Política aparece na tela.

### CT16 - Reiniciar celular e recriar agendamento

Requisito: RF05. Técnica: T02. Prioridade: Média.

Pré-condições: Lembrete futuro existente; dispositivo de teste.

Dados: A antes do reboot; B recriado depois.

Passos: 1. Reiniciar celular. 2. Abrir histórico. 3. Ler aviso de limitação. 4. Cancelar A. 5. Criar B para horário futuro e observar.

Esperado: Lista permanece, mas alarme de A não é restaurado. SCHEDULED não comprova alarme ativo. B precisa ser criado de novo; confirmar entrega separadamente.

### CT17 - Executar validações puras de lembrete

Requisito: RF03. Técnica: T01/T03. Prioridade: Alta.

Pré-condições: JDK/Gradle/SDK e dependências disponíveis.

Dados: ReminderValidationTest: 5 métodos; relógio fixo.

Passos: 1. Executar ./gradlew :app:testDebugUnitTest --tests "com.example.gamesaledb.reminders.ReminderValidationTest". 2. Guardar relatório JUnit.

Esperado: Cinco testes passam: futuro, campos em branco, passado/agora, +1 ms e gameId obrigatório. Não substitui testes Android.

### CT18 - Gerar APK da versão P2

Requisito: RT01. Técnica: T02. Prioridade: Alta.

Pré-condições: JDK 25 conforme configuração original; Android SDK 37 e dependências disponíveis; local.properties próprio.

Dados: Fonte P2 desta entrega.

Passos: 1. Sincronizar Gradle. 2. Executar ./gradlew :app:assembleDebug. 3. Guardar log e instalar APK em dispositivo de teste.

Esperado: Build concluído e APK instalável em app/build/outputs/apk/debug. Não usar APK antigo incluído no ZIP como evidência.

### CT19 - Interpretar gradlew no shell Linux

Requisito: RT01. Técnica: T02. Prioridade: Média.

Pré-condições: Shell Bash disponível.

Dados: ZIP original com CRLF; fonte P2 com LF.

Passos: 1. Executar bash gradlew no original e registrar falha. 2. Corrigir apenas finais de linha e permissão. 3. Executar bash -n gradlew. 4. Executar wrapper e observar se inicia download.

Esperado: Após correção, shell aceita script e wrapper chega à inicialização/download. Falha de rede no download é bloqueio distinto da sintaxe.

## Registro de execução

Não sobrescrever execuções anteriores. Usar EX01, EX02 etc.; preencher data, executor, dispositivo/SO, versão, esperado, obtido, situação, evidência e defeito associado. Para cada correção, abrir nova execução do mesmo CT e executar regressões relacionadas.

Guardar evidência como docs/evidencias/EXxx_CTxx_descricao.png ou .mp4; não criar arquivos de evidência fictícios. Horário escolhido e observado devem estar visíveis/anotados para CT05. Capturar cancelamento e IDs para CT04/CT07.

## Validação externa e exploração

Pendentes. Escolher pessoa de outro projeto e registrar nome, data, dispositivo/API e APK/versão. Entregar os passos CT01 a CT16 e permitir exploração por 15 minutos com objetivo de encontrar falhas de agendamento/cancelamento, navegação e clareza da permissão. Registrar ações, observações, resultado de cada caso e sugestões priorizadas. Não atribuir testes externos ao assistente.

## Riscos prioritários

Persistência e AlarmManager não são atômicos; permissões podem mudar depois de agendar; Doze atrasa eventos; reboot/force-stop não restauram alarmes. Defeitos antigos de sincronização devem ser registrados como tais, sem atribuí-los automaticamente à P2. A auditoria de segurança das regras publicadas não foi realizada.
