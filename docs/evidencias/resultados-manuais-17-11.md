# Resultados manuais de 17/11/2026

Executor: novaes. Ambiente informado: Windows / Android Studio; emulador Medium Phone, API 37.0, Android 17. Não houve execução do emulador pelo assistente. Resultados são observações do executor, com capturas originais quando disponíveis. As datas/horários abaixo reproduzem a tela do emulador; fuso não confirmado.

Versões: as execuções anteriores ao patch 7 referem-se ao conteúdo das etapas 1 a 6, commit db0ec98. A correção de navegação foi compilada e confirmada no emulador antes do commit 108fee9. A regressão foi confirmada após essa correção; não foi fornecido log de instalação ou SHA do APK.

## Resultados e alcance

### EX12 — CT03 — Bloqueado

Título vazio: Preencha o título. Mensagem vazia: Preencha a mensagem. Nenhum lembrete adicionado, conforme relato.

Evidência: `docs/evidencias/EX12_CT03_titulo_vazio.png`.

Limites: Capturas dos dois campos em EX12_CT03_*.png. Parte executada conforme esperado; apenas espaços e horário futuro nas pré-condições não confirmados. Bloqueio de conclusão do roteiro, não falha do app.

### EX13 — CT06 — Bloqueado

Horário passado com campos preenchidos rejeitado: Escolha uma data e hora no futuro. Nenhum registro criado.

Evidência: `docs/evidencias/EX13_CT06_horario_passado.png`.

Limites: Parte manual de passado aprovada; minuto atual ainda sem execução confirmada. Limites now/now+1 testados na JVM em EX07; não substituem UI.

### EX14 — CT04 — Bloqueado

Lembrete #9 SILENT HILL 2, solicitado para 10:46, aparece Cancelado.

Evidência: `docs/evidencias/EX14_CT04_cancelado.png`.

Limites: Cancelamento confirmado. Reabertura e término da janela de observação sem entrega não informados explicitamente; não declarar ausência futura ilimitada.

### EX15 — CT05 — Bloqueado

IDs #10 (10:44) e #11 (10:45) ainda Agendados na captura. Depois chegaram juntos ao abrir o app, conforme relato, sem hora real informada.

Evidência: `docs/evidencias/EX15_CT05_aguardando.png`.

Limites: Entrega ocorreu; atraso não quantificável. Relação causal com abertura não demonstrada. Nova execução independente em EX16.

### EX16 — CT05 — Aprovado

IDs #12 e #13 solicitados para 10:53 e enviados ao sistema às 10:53. Usuário confirmou recebimento com app em segundo plano sem reabri-lo.

Evidência: `docs/evidencias/EX16_CT05_segundo_plano.png`.

Limites: Captura do histórico comprova envio ao sistema no mesmo minuto; recebimento/estado em segundo plano confirmados por relato. Precisão de segundos não medida.

### EX17 — CT07 — Bloqueado

IDs #12 e #13 distintos foram enviados ao sistema às 10:53, sem um substituir o outro.

Evidência: `docs/evidencias/EX16_CT05_segundo_plano.png`.

Limites: Independência de IDs observada. Variante de cancelar apenas um do par e confirmar entrega do outro ainda não executada; não confundir com cancelamento separado do #9.

### EX18 — CT08 — Bloqueado

Modo avião visível. IDs #14 e #15 solicitados para 10:57 e enviados às 10:57; usuário confirmou entrega após o roteiro sem internet.

Evidência: `docs/evidencias/EX18_CT08_modo_aviao.png`.

Limites: Entrega offline confirmada por relato e captura. Wi-Fi desligado e reabertura com conferência de todos os campos não foram explicitamente confirmados; não concluir roteiro completo de persistência.

### EX19 — CT11 — Bloqueado

Após teste em modo avião, usuário confirmou que tocar na notificação abriu o jogo correto. Captura posterior mostra detalhes offline de Counter-Strike 2.

Evidência: `docs/evidencias/EX19_CT11_detalhes_offline.png`.

Limites: Abertura offline aprovada no comportamento relatado. Captura não demonstra o gesto de toque. Activity fria e app aberto em outro jogo ainda sem identificação individual.

### EX20 — CT02 — Bloqueado

Com notificações desativadas nas configurações, app pediu ativação e não criou lembrete, conforme captura e relato.

Evidência: `docs/evidencias/EX20_CT02_notificacoes_desativadas.png`.

Limites: Bloqueio global verificado. Negação do diálogo POST_NOTIFICATIONS de primeiro acesso e bloqueio exclusivo do canal não testados nesta execução.

### EX21 — CT02 — Bloqueado

Após reativação, #16 aparece Agendado para 11:05.

Evidência: `docs/evidencias/EX21_CT02_agendado_apos_reativacao.png`.

Limites: Agendamento após reativação confirmado. Ausência de agendamento automático antes de novo clique não foi confirmada explicitamente; não concluir essa variante.

### EX22 — CT12 — Aprovado

Usuário confirmou pesquisa, consulta de preços e consulta de preços salvos sem internet funcionando.

Evidência: `docs/evidencias/resultados-manuais-17-11.md`.

Limites: Aprovação por relato explícito: 1,2,3 funcionaram. Jogos usados e capturas específicas da busca/cache não informados. Revisão da navegação já compilada/testada; nova repetição da regressão após commit não foi exigida.

### EX23 — CT13 — Bloqueado

Usuário confirmou adicionar e remover jogo da wishlist funcionando.

Evidência: `docs/evidencias/resultados-manuais-17-11.md`.

Limites: Fluxo básico aprovado por relato. Operações offline, estados PENDING/SYNCED e conferência remota no Firestore não confirmados; não declarar sincronização verificada.

### EX24 — CT20 — Reprovado

Usuário relatou tela branca ao tocar rapidamente na seta de voltar. Fonte tinha popBackStack sem guarda de tela ativa/transição/raiz.

Evidência: `docs/evidencias/resultados-manuais-17-11.md`.

Limites: Defeito reproduzido pelo executor; sem vídeo/captura da tela branca. A captura de detalhes não comprova esse defeito.

### EX25 — CT18 — Aprovado

Patch 7 aplicado e assembleDebug: BUILD SUCCESSFUL em 3 s; 40 tarefas, 10 executadas e 30 atualizadas.

Evidência: `docs/evidencias/EX25_CT18_build_correcao.png`.

Limites: Build real no Windows; não é teste instrumentado. Execução atualizada via Run foi orientada; executor confirmou reteste em EX26.

### EX26 — CT20 — Aprovado

Após correção e orientação para executar app atualizado, usuário confirmou retorno normal e cliques rápidos sem tela branca.

Evidência: `docs/evidencias/resultados-manuais-17-11.md`.

Limites: Reteste aprovado por relato no mesmo emulador. Quantidade de repetições e vídeo não fornecidos. Commit/push 108fee9 comprovados por captura.

## Registros textuais do executor

- Após validação de título/mensagem/horário: “não adicionam lembretes a lista”.
- Entrega às 10:53: “esses dois chegaram no horario correto”; confirmação de segundo plano: “sim, estava em segundo plano”.
- Teste sem internet: “foi”; abertura pela notificação: “abriu”.
- Notificações desativadas: “ele nao criou e pediu para eu ativar as notificações”.
- Reteste da navegação após build e orientação: “foi”.
- Regressão: “1,2,3 funcionaram”, em resposta a pesquisa/preços, adicionar/remover wishlist e consultar preços em cache offline.

## Como interpretar resultados parciais

Bloqueado nesta atualização significa que falta registrar uma parte do roteiro original; o texto informa qual comportamento já foi observado. Não equivale a defeito do app nem apaga resultados anteriores. Nenhum caso foi dividido artificialmente para aumentar o número de aprovações. CT20 foi acrescentado porque corresponde ao novo defeito de navegação descoberto durante a execução.

O atraso dos IDs #10/#11 foi preservado sem causa atribuída ou duração inventada. A entrega dos IDs #12/#13 às 10:53 é outra execução; não elimina a ocorrência anterior. Enviado ao sistema não significa lido nem ouvido.

## Histórico Git comprovado por capturas

| Etapa | Commit |
| --- | --- |
| 1 — diagnóstico | c145eb0 |
| 2 — persistência | 752734b |
| 3 — notificações | 05207bf |
| 4 — telas e navegação | bbf04da |
| 5 — wrapper | 7b53982 |
| 6 — testes e documentação | db0ec98 |
| 7 — correção do retorno | 108fee9 |

Branch feat/p2-doc. Capturas historico_commits_01_a_06.png e commit_108fee9_confirmacao.png comprovam histórico, push e árvore limpa no momento mostrado. Nenhum merge na main ou deploy Firebase foi realizado pelo assistente.
