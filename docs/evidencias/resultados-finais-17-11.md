# Consolidação de testes, lint e APK — 17/11/2026

Executor dos testes Android: Victor Novaes Machado (novaes). Emulador Medium Phone/API 37.0/Android 17 no Windows. Fonte de referência: commit 108fee9, documentação 674e34f. A identificação do APK está em apk-identificacao.json. Os testes de interface foram realizados antes do recebimento do APK; a correspondência exata entre o APK testado anteriormente e o enviado não foi comprovada criptograficamente.

## Execuções acrescentadas

### EX27 — CT21 — Aprovado

lintDebug: BUILD SUCCESSFUL em 37 s. HTML: 22 avisos, 1 sugestão; nenhum erro reportado.

Evidência: `docs/evidencias/EX27_CT21_lint_resumo.png`.

Limites: Relatório HTML analisado. Avisos opcionais mantidos; sem atualização de dependências ou supressão de checks. Copiar HTML original para docs/evidencias/lint-results-debug.html.

### EX28 — CT10 — Aprovado

Após rotação para horizontal, título, mensagem e horário preservados. Cancelar seletor manteve horário e não criou lembrete, conforme confirmação.

Evidência: `docs/evidencias/EX28_CT10_rotacao.png`.

Limites: Captura mostra orientação horizontal/horário 11:43. Igualdade com estado anterior e cancelamento confirmados por relato; sem captura anterior.

### EX29 — CT14 — Aprovado

Salvou Comprar em promoção, reabriu; alterou para Aguardar desconto e reabriu, conforme confirmação ao roteiro. Captura mostra nota final em SILENT HILL 2.

Evidência: `docs/evidencias/EX29_CT14_nota_pessoal.png`.

Limites: Aprovação por relato do executor; captura da nota final não demonstra isoladamente as duas reaberturas.

### EX30 — CT03 — Aprovado

Título e mensagem com somente espaços rejeitados; nenhum lembrete criado, confirmado explicitamente. Campos vazios já rejeitados em EX12.

Evidência: `docs/evidencias/EX30_CT03_espacos_titulo.png`.

Limites: Segunda captura EX30_CT03_espacos_mensagem.png. Espaços são invisíveis; entrada e ausência de registros confirmadas pelo executor. Conclusão acumulada do caso; EX12 parcial preservado.

### EX31 — CT06 — Aprovado

Relógio e formulário em 11:48; Escolha uma data e hora no futuro. Nenhum registro adicionado. Passado já rejeitado em EX13; futuros aceitos em EX16/EX33.

Evidência: `docs/evidencias/EX31_CT06_minuto_atual.png`.

Limites: Seletor zera segundos: minuto atual não é igualdade exata do relógio. Igualdade now e limite +1ms comprovados apenas na regra pura em EX07. Captura anterior com 04:47 versus 11:47 é passado, não minuto atual.

### EX32 — CT01 — Aprovado

Botão Testar notificação agora publicou Teste do GameSaleDB. Toque abriu app; nenhum registro no histórico, conforme relato.

Evidência: `docs/evidencias/EX32_CT01_notificacao_imediata.png`.

Limites: Notificação imediata verifica canal/permissão, não agendamento. Captura revela Wi-Fi ativo em modo avião; execuções anteriores nesse modo não comprovam ausência de internet.

### EX33 — CT08 — Aprovado

Wi-Fi desligado e modo avião ativo. #17 SILENT HILL 2 preservou dados ao reabrir. Solicitado 11:55, enviado e recebido 11:57; atraso aproximado de 2 min.

Evidência: `docs/evidencias/EX33_CT08_notificacao.png`.

Limites: Formulário, rede, Agendado e Enviado ao sistema em EX33_CT08_*.png. Reabertura/preservação confirmadas explicitamente. Texto Teste CT01 é conteúdo do lembrete; caso executado é CT08.

### EX34 — CT18 — Aprovado

APK recebido: 25875079 bytes; ZIP sem erro de CRC, Manifest/recursos e 12 DEX presentes. SHA-256: 1433419a052154047fca6eb00a2e3e7ff42b9259818938bcced08b67899276da.

Evidência: `docs/evidencias/apk-identificacao.json`.

Limites: Inspeção real no ambiente do assistente. Não é execução Android, nem validação da assinatura, nem comprovação de build reproduzível.

### EX35 — CT18 — Aprovado

Executor confirmou que o APK enviado instala corretamente e abriu no emulador após orientação para usar o ícone na lista de aplicativos.

Evidência: `docs/evidencias/resultados-finais-17-11.md`.

Limites: Aprovação por relato. Não foi fornecido vídeo/log de instalação nem executado o APK pelo assistente.

## Situação consolidada

Nove casos manuais do plano tiveram seus comportamentos concluídos ao longo das execuções: CT01, CT03, CT04, CT05, CT06, CT08, CT10, CT12 e CT14. CT20 acrescenta o reteste da navegação. Algumas conclusões combinam captura e relato; a planilha descreve a origem. CT03/CT06 usam as execuções anteriores mais as novas, sem apagar registros parciais.

CT04 foi concluído em EX36, preservando os registros parciais anteriores. CT02, CT07, CT09, CT11, CT13, CT15 e CT16 têm variantes pendentes ou não executadas. Não confundir os casos concluídos com cobertura total do plano.

A avaliação externa foi adiada por solicitação do usuário e continua não executada. Isso deixa uma pendência acadêmica. Integrantes e repositório foram informados; contribuições estão em docs/p2/07-integrantes-e-contribuicoes.md. A contribuição de Robson não foi descrita e as variantes acima permanecem pendentes.

## Revisão da evidência de rede

A captura da notificação imediata às 11:50 mostra Wi-Fi conectado com modo avião. As execuções anteriores EX18/EX19 em modo avião não demonstram, por si, ausência de internet. Preservá-las como históricas, sem ampliar sua conclusão. EX33 foi repetido com Wi-Fi desligado, conforme captura, e comprovou a entrega local sem internet.

## Lint

O lint executado pelo usuário concluiu com sucesso. O HTML fornecido registra 22 avisos e uma sugestão: 10 atualizações de ferramentas/dependências; 8 recursos não utilizados; 2 sugestões KTX; 1 organização no catálogo TOML; 1 chamada vazia a super.onCleared; 1 sugestão de mutableLongStateOf. Nenhum erro reportado. Checks desativados no relatório não foram executados; ausência de erro não é prova de ausência de defeitos.

Manter versões já testadas para esta entrega. Não aplicar automaticamente SharedPreferences.edit sem preservar a verificação de commit() e sua semântica. Nenhum aviso foi ocultado para apresentar resultado limpo. Relatório completo: docs/evidencias/lint-results-debug.html, a copiar do resultado real do Windows; o arquivo foi analisado pelo assistente.

## APK

Arquivo recebido e inspecionado, com integridade ZIP aprovada. Instalação e abertura foram confirmadas pelo usuário; demais fluxos nesse exato binário não foram reexecutados. APK debug destinado à demonstração. Distribuição pública e assinatura de release não foram realizadas.

## EX36 — CT04 — Aprovado

Data: 17/11/2026. Executor: Victor Novaes Machado. Ambiente: emulador Medium Phone/API 37.0/Android 17 no Windows, conforme acompanhamento anterior. Referência documental: 3a7d014; código da navegação: 108fee9.

O lembrete #18 de Stardew Valley, solicitado para 13:15, aparece Agendado na captura com relógio em 13:07 e Cancelado na captura de 13:09. Victor confirmou que, após fechar e reabrir o app, o registro continuou Cancelado e nenhuma notificação desse lembrete apareceu até 13:20. A captura final da tela inicial, às 13:21, encerra o acompanhamento.

Evidências: EX36_CT04_agendado.png, EX36_CT04_cancelado.png, EX36_CT04_acompanhamento.png e EX36_CT04_fim_acompanhamento.png. A persistência após reabertura e a ausência de notificação são confirmadas por relato: as capturas da tela inicial, isoladamente, não demonstram ausência de notificações. Os horários são os minutos exibidos; não representam medição de segundos nem o instante exato da ação de cancelar. A aprovação é restrita à janela observada e não comprova ausência de entrega indefinidamente.

O commit 3a7d014, push e árvore limpa foram confirmados na captura commit_3a7d014_push.png. Esta atualização acrescenta documentação, sem novo código do aplicativo.
