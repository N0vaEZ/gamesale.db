# Relatório de qualidade da P2

Atualização: 17/11/2026. Fonte de referência da revisão estática: `P2-95004ee08769`. Os hashes reais estão em docs/evidencias/resultados-manuais-17-11.md. Referência atual do código corrigido: commit 108fee9. A revisão estática foi repetida após essa correção: P2-0a9be0739877, quatro verificações aprovadas; o relatório anterior foi preservado em verificacao-fontes-etapa06.json.

## Implementação

Lembretes locais vinculados aos jogos da wishlist, com formulário, validação, permissão/canal, IDs exclusivos, agendamento aproximado, histórico, cancelamento e abertura do jogo pela notificação. O Room e a integração Firebase existentes foram preservados.

## Ambientes e origem dos resultados

- Revisão estática e tentativas iniciais: Linux, com download do Gradle bloqueado. Os logs permanecem como histórico.
- Compilação e testes unitários: Windows, Gradle 9.6.0, executados por novaes. ReminderValidationTest roda na JVM do computador, não no Android.
- Verificações manuais: emulador Medium Phone, API 37.0, Android 17, conforme informação do executor. Teste em aparelho físico não foi confirmado.

## Resultados registrados

| Registro | Resultado | Evidência em docs/evidencias/ |
| --- | --- | --- |
| EX01 e EX02 | Tentativas iniciais de teste/build bloqueadas no download do Gradle no Linux | Logs iniciais |
| EX03 e EX04 | Falha de CRLF do wrapper, correção e reteste de sintaxe | gradle-build.log e wrapper-sintaxe.log |
| EX05 | Build da etapa 4 reprovou por callback na tela errada | EX05_CT18_compilacao_falha.png |
| EX06 | Build aprovado após correção de navegação | EX06_CT18_compilacao_corrigida.png |
| EX07 | Cinco testes unitários aprovados; zero falhas e ignorados | EX07_CT17_testes_junit.png |
| EX08 | Build aprovado no Windows após a etapa 5 | EX08_CT18_compilacao_windows.png |
| EX09 | Entrega relatada após Clear all: solicitado 09:51, observado 09:52 | relatos-testes-emulador.md |
| EX10 | Cancelamento confirmado; roteiro completo de CT04 ainda sem todos os registros | relatos-testes-emulador.md |
| EX11 | Abertura do jogo correto confirmada; variantes de CT11 ainda sem todos os registros | relatos-testes-emulador.md |

Execuções adicionais EX12-EX26 e capturas originais estão em docs/evidencias/resultados-manuais-17-11.md. Há validações de campos/horário, bloqueio de notificações, envio às 10:53 em segundo plano, envio às 10:57 em modo avião, abertura offline e regressão da busca/preços/wishlist confirmada pelo executor. A planilha mantém os limites das variantes não verificadas e todas as execuções anteriores.

Capturas do histórico comprovam estado e minuto de envio ao sistema; o recebimento, o uso em segundo plano e os gestos de toque são confirmados por relato. O modo avião é visível, mas o desligamento do Wi-Fi não foi confirmado separadamente. Não foi fornecido vídeo da tela branca ou do reteste. Testes unitários não comprovam entrega Android.

## Defeitos e retestes

**DF01 - wrapper Linux:** CRLF causava erro no Bash. A etapa 5 normalizou finais de linha e permissão de execução. O reteste de sintaxe passou; o código do gradlew.bat não foi alterado.

**DF02 - navegação:** onReminderClick estava na chamada de GameListScreen, que não possui esse parâmetro, e faltava em WishlistScreen. EX05 registra a falha de compilação. O callback foi movido para WishlistScreen; EX06 comprova a compilação após a correção, incorporada antes do commit da etapa 4.

Riscos de inspeção da P1 permanecem no diagnóstico e não equivalem a defeitos reproduzidos em dispositivo.

**DF03 - retorno repetido:** o executor encontrou tela branca ao tocar rapidamente em Voltar. A hipótese técnica era a remoção repetida de destinos, incluindo a raiz, por chamadas sem guarda a popBackStack. O patch 7 adicionou proteção de entrada ativa, lifecycle RESUMED e tela anterior. EX24 registra o defeito por relato; EX25 comprova compilação; EX26 registra reteste aprovado por relato. Correção: 108fee9, com push comprovado. Quantidade de repetições não informada.

## Cobertura e limites

- Planejamento ampliado para 20 casos e três técnicas; CT20 documenta o defeito novo.
- CT17: cinco métodos JUnit aprovados na JVM; não representam cinco casos manuais.
- CT18: builds Windows aprovados antes e após a correção 7. CT19: sintaxe do wrapper Linux aprovada anteriormente.
- CT05: entrega em segundo plano aprovada no minuto 10:53; atraso anterior mantido como outra ocorrência sem causa confirmada.
- CT12: busca, consulta de preços e cache offline aprovados por relato explícito.
- CT20: falha inicial e reteste aprovado por relato; captura da build e do commit disponíveis.
- CT03/CT06: vazios e passado rejeitados sem registros; faltam apenas espaços e minuto atual na UI.
- CT02: bloqueio global e posterior agendamento observados; diálogo inicial de recusa e exigência de novo toque após reativação não confirmados individualmente.
- CT04/CT07/CT08/CT11/CT13: comportamentos básicos observados; consultar na planilha os passos ainda pendentes. Bloqueado por falta de registro não equivale a falha do app.
- Nenhum teste externo, em aparelho físico ou lint foi confirmado.

## Pendências da entrega

1. Completar os roteiros parciais sem transformar comportamentos não observados em resultados aprovados. Oito casos manuais completos ainda não estão comprovados.
2. Registrar janela de observação do cancelamento, reabertura/persistência, variantes de abertura pela notificação, espaços/minuto atual, canal bloqueado, rotação e notas. Regressão de sincronização remota permanece não verificada.
3. Guardar HTML/XML do JUnit e, quando possível, captura/vídeo de recebimento/toque/cancelamento. Identificar SHA do APK instalado.
4. Executar lint, disponibilizar o APK debug gerado no Windows e realizar avaliação por pessoa de outro projeto, com roteiro e exploração registrados.
5. Completar nomes/matrículas, URL digitada do repositório, contribuições e identificação do avaliador externo. Os sete commits e pushes foram confirmados; main não foi modificada pelo assistente.

O Firebase mantém autenticação anônima e coleção compartilhada, conforme o escopo da demonstração. Análise das regras e sucesso da wishlist local não equivalem a teste remoto de regras/sincronização. Nenhuma publicação Firebase foi realizada nesta atualização.
