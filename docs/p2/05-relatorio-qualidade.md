# Relatório de qualidade da P2

Atualização: 17/11/2026. Fonte de referência da revisão estática: `P2-95004ee08769`. Os registros identificam as etapas aplicadas; os hashes dos commits ainda devem ser associados às evidências.

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

As quatro capturas de compilação e JUnit foram fornecidas pelo executor e preservadas sem edição. Entrega, cancelamento e abertura têm confirmação textual, mas não captura/vídeo do emulador. Testes unitários não comprovam entrega de notificações no Android.

## Defeitos e retestes

**DF01 - wrapper Linux:** CRLF causava erro no Bash. A etapa 5 normalizou finais de linha e permissão de execução. O reteste de sintaxe passou; o código do gradlew.bat não foi alterado.

**DF02 - navegação:** onReminderClick estava na chamada de GameListScreen, que não possui esse parâmetro, e faltava em WishlistScreen. EX05 registra a falha de compilação. O callback foi movido para WishlistScreen; EX06 comprova a compilação após a correção, incorporada antes do commit da etapa 4.

Riscos de inspeção da P1 permanecem no diagnóstico e não equivalem a defeitos reproduzidos em dispositivo.

## Cobertura e limites

- Planejamento: 19 casos e três técnicas de projeto de testes.
- CT17: cinco métodos JUnit executados e aprovados. Representam um caso automatizado do plano, não cinco casos manuais Android.
- CT18: compilação aprovada no Windows, com falha anterior e reteste preservados.
- CT19: suporte do wrapper Linux aprovado após correção.
- CT05: entrega aprovada por relato e horários informados; captura/vídeo pendente.
- CT04 e CT11: comportamentos básicos confirmados por relato. A conclusão dos casos completos está bloqueada por falta dos registros restantes. Isso não indica defeito do aplicativo.
- Outros 13 casos manuais: não executados ou sem resultado informado. Nenhuma aprovação foi atribuída a eles.
- Nenhuma execução externa, teste em aparelho físico ou execução de lint foi confirmada.

## Pendências da entrega

1. Completar e registrar pelo menos oito casos manuais Android, incluindo entradas inválidas, permissões, persistência, offline e regressão da P1. Concluir as variantes de CT04 e CT11.
2. Capturar entrega, cancelamento e abertura no emulador. Guardar HTML/XML do JUnit e a versão testada.
3. Executar lint e registrar resultado. Disponibilizar o APK debug gerado no Windows ao avaliador externo; o arquivo APK não foi anexado ao pacote.
4. Realizar avaliação por pessoa de outro projeto e registrar exploração, problemas e sugestões.
5. Completar nomes/matrículas, URL do repositório, contribuições e hashes dos commits. Etapas 1 a 5 registradas/enviadas conforme confirmação do executor; etapa 6 em revisão.

O Firebase mantém autenticação anônima e coleção compartilhada, conforme o escopo da demonstração. A análise do texto das regras não equivale a teste remoto. Nenhuma publicação de regras foi realizada nesta atualização.

## Conclusão

Build no Windows e cinco testes unitários comprovados por capturas. Entrega, cancelamento e abertura confirmados pelo executor no emulador, com os limites descritos. A entrega acadêmica ainda depende de ampliação dos testes, evidências do dispositivo e avaliação externa.
