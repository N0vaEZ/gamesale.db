# Revisão por etapas e execução local

Nenhum commit ou publicação remota foi realizado pelo assistente. Os patches são mudanças locais propostas. Use o histórico real do seu projeto, revise o diff e só então faça cada commit. Não atribua datas ou testes que não ocorreram.

## Etapas propostas

| Patch | Conteúdo | Mensagem sugerida |
| --- | --- | --- |
| 01-diagnostico.patch | Arquitetura, requisitos, riscos e escopo | docs: registra arquitetura e requisitos da P2 |
| 02-persistencia.patch | Modelo, regras de validação e histórico em SharedPreferences | feat: persiste lembretes locais vinculados aos jogos |
| 03-notificacoes.patch | AlarmManager, canal, receiver, permissão no Manifest e ícone | feat: agenda e publica lembretes locais |
| 04-telas-navegacao.patch | Formulário, wishlist, cancelamento e abertura do jogo | feat: integra lembretes a wishlist e navegacao |
| 05-wrapper-linux.patch | Correção CRLF, execução e atributo LF do wrapper | fix: normaliza o wrapper Gradle para Linux |
| 06-testes-documentacao.patch | Cinco testes JUnit, planilha, plano, relatório e verificações | test: adiciona validacoes e documenta a P2 |

As etapas são cumulativas. Etapas 02 e 03 acrescentam a base, sem acesso pela interface; etapa 04 integra o fluxo visível. As compilações foram confirmadas no Windows. A falha de navegação foi corrigida antes do commit da etapa 4. A branch em uso é feat/p2-doc; etapas 1 a 5 registradas/enviadas conforme confirmação do executor. Conferir os hashes no histórico real.

## Aplicar no repositório existente

1. Faça backup local e confira `git status`. Se houver mudanças próprias, salve-as em um commit/branch antes de aplicar. Não usar `git reset --hard`.
2. Crie uma branch de trabalho, por exemplo `git switch -c feat/p2-lembretes`.
3. Na raiz do projeto, use `git apply --check CAMINHO/etapas/01-diagnostico.patch`. Se estiver limpo, aplique com `git apply CAMINHO/etapas/01-diagnostico.patch`.
4. Revise os arquivos e `git diff`. Adicione apenas os arquivos da etapa com `git add CAMINHOS_DOS_ARQUIVOS`, confira `git diff --cached` e faça o commit com a mensagem sugerida.
5. Repita na ordem para 02 a 06. Se algum patch não aplicar, pare e compare sua versão com o ZIP base, sem forçar substituição de mudanças próprias. Confira finais de linha no Windows se necessário.
6. Compile/teste e complete as evidências antes de publicar. Push e publicação no Firebase dependem de sua decisão; não foram executados aqui.

O pacote também contém `projeto/gamesale.db/`, com a versão integrada para comparação. Não sobrescreva sua pasta `.git`. Aplique os patches OU compare/copie arquivos; não faça as duas coisas na mesma cópia.

## Configuração local

- Manter applicationId e namespace com.example.gamesaledb.
- Manter o seu app/google-services.json original; ele não foi alterado. A cópia redistribuível do projeto omite esse arquivo e local.properties. Para uma pasta nova, recupere-os do seu projeto original. Nenhuma chave está nos patches.
- Em local.properties, configurar sdk.dir para seu SDK e ITAD_API_KEY com sua chave. Não enviar esse arquivo ao GitHub.
- O projeto original solicita JDK 25 em gradle/gradle-daemon-jvm.properties e Gradle 9.6.0 no wrapper. AGP 9.4.0, SDK 37 e dependências foram preservados; usar ferramentas compatíveis já instaladas no Android Studio. sourceCompatibility 11 não é a versão do JDK usada para executar Gradle.
- Sync Project with Gradle Files; aceitar instalação de SDK/dependências somente no seu ambiente de desenvolvimento.

## Verificações reproduzíveis

Linux/macOS, na raiz:

```bash
python3 tools/check_p2_sources.py
./gradlew :app:testDebugUnitTest --tests "com.example.gamesaledb.reminders.ReminderValidationTest"
./gradlew :app:assembleDebug
./gradlew :app:lintDebug
```

No Windows, usar `gradlew.bat` nos comandos Gradle. O verificador Python contém verificação Bash e é voltado a Linux; no Windows, use Git Bash ou execute essa parte no Linux. Os testes JUnit podem ser executados pelo botão Run em ReminderValidationTest no Android Studio.

Comandos no Windows:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "com.example.gamesaledb.reminders.ReminderValidationTest"
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:lintDebug
```

Build debug e cinco testes JUnit confirmados no Windows por capturas. Lint ainda não confirmado. O APK fica em app/build/outputs/apk/debug/; copiar o arquivo gerado nessa máquina para a entrega ao avaliador. Nenhum APK foi anexado ao pacote de documentação.

Depois de instalar, execute CT01 a CT16 e registre ambiente, versão, horário, resultado e evidência. Consulte docs/p2/03-plano-testes.md e docs/testes/GameSaleDB-Testes-P2.xlsx. Para a avaliação externa e a entrega final, ainda faltam testes reais e evidências do dispositivo.
