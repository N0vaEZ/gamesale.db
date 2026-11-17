# Demonstração e entrega do GameSaleDB

## Versão e ambiente

Código de referência: 108fee9. Documentação anterior: 674e34f. Windows/Android Studio; Medium Phone/API 37.0/Android 17. APK debug identificado em docs/evidencias/apk-identificacao.json; arquivo distribuído separadamente. Use esse SHA-256 para identificar o binário da demonstração, sem afirmar correspondência reproduzível com fontes não verificada.

## O que explicar

O GameSaleDB mantém busca e preços pela ITAD, cache e wishlist no Room, sincronização existente no Firestore e notas pessoais. A P2 acrescenta lembretes locais associados pelo gameId. A notificação lembra o usuário de conferir ofertas; não monitora automaticamente quedas de preço.

O formulário valida campos, vínculo e horário. ReminderStore guarda JSON em SharedPreferences e gera IDs. ReminderScheduler cria canal, verifica permissão e agenda AlarmManager com setAndAllowWhileIdle. ReminderReceiver processa um evento curto, sem rede, e publica a notificação. O toque passa o ID à MainActivity; a navegação recupera o jogo associado. Cancelar usa o mesmo PendingIntent e mantém histórico.

## Sequência de demonstração

1. Pesquisar jogo, consultar preços e adicionar à wishlist.
2. Abrir Criar lembrete. Mostrar rejeição de título/mensagem vazios e horário passado.
3. Agendar com título e mensagem próprios, para alguns minutos no futuro. Mostrar ID e estado Agendado.
4. Deixar em segundo plano; apresentar as evidências reais de disparo (incluindo 11:55 → 11:57 sem rede), pois o horário é aproximado.
5. Tocar na notificação e abrir o jogo. Usar Testar notificação agora somente para demonstrar canal/permissão, explicando que não prova o agendador.
6. Cancelar outro lembrete antes do disparo e mostrar o histórico. Não afirmar janela sem entrega que não foi observada.
7. Mostrar planilha: técnicas T01 equivalência, T02 estados e T03 valor limite; falha/reteste DF03; relatório JUnit e lint.

## Defeito corrigido

Cliques rápidos em Voltar podiam retirar telas repetidamente e deixar a tela branca. A correção verifica a entrada ativa, lifecycle RESUMED e tela anterior antes do pop. Commit 108fee9. Build passou e usuário confirmou retorno normal e cliques rápidos sem tela branca.

## Limitações a declarar

- Alarmes inexatos podem atrasar e ser agrupados; dois minutos foram observados no teste offline.
- Reinício/forçar parada não restauram os alarmes. Histórico Agendado não comprova alarme ativo após esses eventos.
- Wi-Fi pode continuar ligado em modo avião; a execução offline válida desligou o Wi-Fi explicitamente.
- Sem recorrência, edição, confirmação de leitura ou transação atômica entre preferências e AlarmManager.
- Firebase compartilhado com autenticação anônima mantido para apresentação; regras publicadas não foram auditadas/testadas remotamente nesta etapa.
- Nenhuma avaliação externa foi executada; adiada pelo usuário. Aparelho físico e variantes pendentes não devem ser apresentados como aprovados.

## Arquivos e revisão

README, docs/p2/, docs/testes/GameSaleDB-Testes-P2.xlsx, docs/evidencias/, APK debug separado. Copiar HTML do lint do resultado real do Windows. Guardar HTML/XML do JUnit quando possível. Preencher integrantes, matrículas, contribuições e URL do repositório. Não é necessário colocar build/ ou APK no histórico Git de fontes.

A main não foi modificada pelo assistente. Revisar a branch feat/p2-doc antes de abrir/mesclar PR; preserve commits separados se esse for o histórico desejado. Nenhum deploy Firebase é necessário para os lembretes locais.
