# 🎮 GameSaleDB

GameSaleDB é um aplicativo Android nativo desenvolvido em **Kotlin** com **Jetpack Compose**, criado para consultar jogos e seus preços, manter uma lista de desejos e oferecer suporte a operações offline com sincronização automática.

O projeto foi desenvolvido para a disciplina de **Laboratório de Desenvolvimento de Aplicativos Nativos**.

---

## 📱 Funcionalidades

### 🔎 Busca de jogos

O usuário pode pesquisar jogos pelo nome.

Os dados dos jogos e preços são obtidos através da API do **IsThereAnyDeal (ITAD)**.

Exemplo:

- Cyberpunk 2077
- Hades
- Hollow Knight

Ao selecionar um jogo, o aplicativo apresenta seus detalhes e os preços encontrados em diferentes lojas.

---

### 💰 Consulta de preços

Na tela de detalhes são exibidas as ofertas disponíveis para o jogo selecionado.

Cada oferta apresenta informações como:

- Loja
- Moeda
- Preço

Durante a consulta, o aplicativo também apresenta feedback visual de carregamento.

---

## ❤️ Wishlist

O usuário pode adicionar e remover jogos de sua lista de desejos.

A Wishlist é armazenada localmente utilizando **Room Database**, permitindo que os dados permaneçam disponíveis mesmo após fechar e abrir novamente o aplicativo.

---

## 📝 Notas pessoais

Cada jogo pode possuir uma nota pessoal criada pelo usuário.

Exemplo:

> Comprar quando estiver abaixo de R$ 100.

As notas são armazenadas localmente utilizando Room e permanecem salvas mesmo após o aplicativo ser fechado.

Também é possível alterar uma nota já existente salvando um novo conteúdo para o mesmo jogo.

---

## 📴 Offline First

O GameSaleDB foi desenvolvido utilizando conceitos de **Offline First**.

O aplicativo monitora a conectividade do dispositivo e apresenta o estado atual da conexão:

- 🟢 Online
- 🔴 Offline

Quando o dispositivo está sem internet, operações compatíveis continuam sendo realizadas utilizando o banco de dados local.

Por exemplo, um jogo pode ser adicionado à Wishlist mesmo sem conexão.

Nesse caso, a operação recebe o estado:

`PENDING`

e fica armazenada localmente até que seja possível realizar a sincronização.

---

## 🔄 Sincronização

A Wishlist utiliza sincronização entre o banco de dados local **Room** e o **Cloud Firestore**.

Os estados de sincronização utilizados são:

- `PENDING` — operação aguardando sincronização;
- `SYNCING` — sincronização em andamento;
- `SYNCED` — operação confirmada no Firestore.

Quando a conexão com a internet é restabelecida, o aplicativo detecta automaticamente a mudança e processa as operações pendentes.

Fluxo simplificado:

    Operação offline
          ↓
        Room
          ↓
       PENDING
          ↓
    Internet retorna
          ↓
       SYNCING
          ↓
    Cloud Firestore
          ↓
       SYNCED

A sincronização também trata remoções da Wishlist.

Uma remoção realizada offline é mantida localmente como uma operação pendente até que o Firestore possa ser atualizado.

---

## 💾 Persistência de dados

O aplicativo utiliza **Room Database** para persistência local.

Entre os dados persistidos estão:

- Wishlist;
- Notas pessoais dos jogos;
- Dados utilizados pelo aplicativo para funcionamento local/cache.

Isso permite que informações importantes permaneçam disponíveis após fechar e abrir novamente o aplicativo.

---

## ☁️ Firebase

O **Cloud Firestore** é utilizado como armazenamento remoto para sincronização da Wishlist.

Quando uma alteração é realizada offline, ela é registrada localmente e enviada ao Firestore quando a conexão retorna.

---

## 🛠️ Tecnologias utilizadas

- Kotlin
- Android SDK
- Jetpack Compose
- Material 3
- Navigation Compose
- Room Database
- Kotlin Coroutines
- StateFlow
- Retrofit
- OkHttp
- IsThereAnyDeal API
- Firebase
- Cloud Firestore
- Gradle
- Git / GitHub

---

## 🏗️ Organização do projeto

O projeto está dividido em camadas e pacotes responsáveis por diferentes partes da aplicação.

    com.example.gamesaledb
    │
    ├── data
    │   ├── local
    │   │   ├── DAOs
    │   │   ├── Entities
    │   │   └── GameSaleDatabase
    │   │
    │   ├── remote
    │   │   ├── API
    │   │   ├── DTOs
    │   │   └── Firebase
    │   │
    │   └── repository
    │
    ├── navigation
    │
    ├── ui
    │   ├── game
    │   └── wishlist
    │
    └── util

---

## 🌐 API

O projeto utiliza a API do **IsThereAnyDeal** para consulta de jogos e preços.

Por segurança, a chave da API não deve ser armazenada diretamente no código-fonte nem enviada ao GitHub.

Cada desenvolvedor deve configurar sua própria chave localmente.

Exemplo em `local.properties`:

    ITAD_API_KEY=SUA_CHAVE_AQUI

O arquivo `local.properties` não deve ser versionado pelo Git.

---

## 🔥 Configuração do Firebase

O aplicativo utiliza Firebase Cloud Firestore para a sincronização remota da Wishlist.

Para executar uma cópia própria do projeto, é necessário configurar um projeto Firebase compatível com o aplicativo Android e disponibilizar o arquivo:

    app/google-services.json

Também é necessário criar um banco de dados Cloud Firestore.

---

## ▶️ Como executar

### Requisitos

- Android Studio
- Android SDK configurado
- JDK compatível com o projeto
- Emulador Android ou dispositivo físico
- Chave da API IsThereAnyDeal
- Configuração do Firebase

### Passos

1. Clone o repositório.

2. Abra o projeto no Android Studio.

3. Configure a chave da API no arquivo `local.properties`.

4. Verifique a configuração do Firebase.

5. Execute:

   **Sync Project with Gradle Files**

6. Compile o projeto utilizando:

   **Assemble App**

7. Execute em um emulador ou dispositivo Android.

---

## 🧪 Demonstração do Offline First

Uma forma de testar a sincronização é:

1. Iniciar o aplicativo conectado à internet.
2. Ativar o modo avião.
3. Aguardar o aplicativo indicar estado Offline.
4. Adicionar um jogo à Wishlist.
5. Verificar o estado `Pending synchronization`.
6. Fechar o aplicativo.
7. Abrir novamente ainda offline.
8. Confirmar que o jogo continua armazenado.
9. Desativar o modo avião.
10. Aguardar o aplicativo detectar a conexão.
11. Verificar a sincronização automática.
12. Confirmar o estado `Synchronized`.
13. Verificar o registro correspondente no Cloud Firestore.

O mesmo processo pode ser realizado para testar uma remoção offline.

---

## 🧪 Demonstração de persistência

O projeto possui pelo menos duas funcionalidades com persistência de dados.

### Wishlist

1. Adicionar um jogo à Wishlist.
2. Fechar o aplicativo.
3. Abrir novamente.
4. Confirmar que o jogo continua na Wishlist.

### Notas pessoais

1. Abrir os detalhes de um jogo.
2. Digitar uma nota.
3. Pressionar **Save Note**.
4. Fechar o aplicativo.
5. Abrir novamente.
6. Abrir o mesmo jogo.
7. Confirmar que a nota permanece salva.

---

## 📦 APK

O projeto pode gerar um APK instalável através do Android Studio.

O APK permite instalar e executar o GameSaleDB em um dispositivo Android compatível.

---

## 📚 Objetivo acadêmico

O projeto demonstra conceitos abordados no desenvolvimento de aplicativos Android nativos, incluindo:

- desenvolvimento com Kotlin;
- interfaces declarativas com Jetpack Compose;
- navegação entre telas;
- consumo de API REST;
- persistência local;
- arquitetura Offline First;
- detecção de conectividade;
- sincronização de dados;
- armazenamento remoto;
- tratamento de estados de carregamento e sincronização;
- versionamento com Git.


## P2 - lembretes locais de jogos

Na lista de desejos, toque em **Criar lembrete** no jogo. Preencha título/mensagem, escolha horário futuro e habilite notificações. Toque em **Agendar** após conceder a permissão. O botão **Lembretes** abre o histórico e permite cancelar agendamentos antes do disparo.

Os lembretes são locais e independem de Firestore ou internet. O horário é aproximado. Após reiniciar o celular ou forçar a parada, cancele os registros antigos e crie novamente. Remover um jogo da wishlist não cancela seus lembretes. Tocar na notificação abre os detalhes do jogo vinculado.

- [Diagnóstico e escopo](docs/p2/01-diagnostico.md)
- [Implementação e demonstração](docs/p2/02-implementacao.md)
- [Plano e 21 casos de teste](docs/p2/03-plano-testes.md)
- [Etapas, commits e execução](docs/p2/04-commits-e-execucao.md)
- [Relatório de qualidade e pendências](docs/p2/05-relatorio-qualidade.md)
- [Planilha de testes](docs/testes/GameSaleDB-Testes-P2.xlsx)

Estado da validação em 17/11/2026: build debug aprovado no Windows e cinco testes de ReminderValidationTest executados, sem falhas ou ignorados. Verificações manuais no emulador Medium Phone, API 37.0, Android 17: entrega após Clear all (09:51 solicitado, 09:52 observado), cancelamento e abertura do jogo correto confirmados por relato. Variantes pendentes e distinção entre capturas e relatos estão no relatório de qualidade. A entrega ainda exige mais casos manuais, evidências no dispositivo e avaliação externa.

Correção de navegação da etapa 4 incorporada. Firebase mantido com autenticação anônima e coleção compartilhada, conforme o escopo da demonstração.


### Atualização de testes e navegação — 17/11/2026

A correção de cliques repetidos em Voltar está no commit `108fee9`, com build Windows aprovado e reteste manual confirmado no emulador Medium Phone/API 37/Android 17. Resultados, capturas e limites de cobertura: [registro manual](docs/evidencias/resultados-manuais-17-11.md). A planilha mantém o histórico de falhas e reexecuções. Variantes não executadas, avaliação externa, lint e disponibilização do APK continuam pendentes.

### Consolidação da demonstração

Nove casos manuais concluídos (incluindo CT04), além do reteste CT20; cinco métodos JUnit aprovados e lint revisado com 22 avisos/1 sugestão. [Resultados finais](docs/evidencias/resultados-finais-17-11.md) e [identificação do APK](docs/evidencias/apk-identificacao.json). APK recebido com instalação/abertura confirmadas no emulador; não houve reexecução completa dos fluxos nesse binário pelo assistente. Avaliação externa adiada e demais variantes pendentes. [Roteiro da apresentação](docs/p2/06-demonstracao-e-entrega.md).

### Integrantes e repositório

| Integrante | Matrícula |
| --- | --- |
| Victor Novaes Machado | 202312567 |
| Robson Cruz de Mello Junior | 202323260 |

Repositório: https://github.com/N0vaEZ/gamesale.db — branch `feat/p2-doc`.

[Contribuições informadas](docs/p2/07-integrantes-e-contribuicoes.md). Victor informou ter realizado as atividades orientadas nas conversas da P1 e P2; contribuição de Robson não informada.

CT04 concluído em EX36: lembrete #18 (Stardew Valley), previsto para 13:15, observado como Cancelado às 13:09. Estado preservado após reabrir e ausência de notificação até pelo menos 13:20 confirmados pelo executor; captura final às 13:21. [Registro e limites das evidências](docs/evidencias/resultados-finais-17-11.md).
