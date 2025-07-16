# Sistema de Gestão de Reservas de Hotel

## Índice

1. [Introdução](#introdução)
2. [Funcionalidades Implementadas](#funcionalidades-implementadas)
3. [Estrutura do Projeto](#estrutura-do-projeto)
4. [Tecnologias Utilizadas](#tecnologias-utilizadas)
5. [Testes Realizados](#testes-realizados)
6. [Conclusão](#conclusão)

---

## Introdução

Este trabalho consiste na implementação em Java da primeira parte de um sistema de back-end para a gestão de hotéis. O objetivo é permitir a gestão eficiente de informações relacionadas com o hotel, incluindo contas de utilizadores, quartos, manutenções, reservas, ocorrências e avaliações.

O sistema está estruturado em várias áreas principais:
- **Gestão de Contas e Autenticação**
- **Gestão de Quartos**
- **Gestão de Manutenções**
- **Gestão de Reservas**
- **Gestão de Ocorrências e Avaliações**

O programa pode ser testado utilizando os seguintes comandos:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass=com.thehotel.Main
mvn test
```

---

## Funcionalidades Implementadas

### Gestao de Contas e Autenticação:

- [x] Use Case 1:
  - Consultar informações gerais
- [x] Use Case 2:
    - Criar conta de Hóspede
- [x] Use Case 3:
  - Criar conta de Staff
- [ ] Use Case 4:
  - Login
- [ ] Use Case 5:
  - Recuperaração de password
- [x] Use Case 6:
    - Consultar dados pessoais
- [x] Use Case 7:
  - Consultar dados pessoais de um utilizador
- [x] Use Case 8:
  - Alterar dados pessoais
- [x] Use case 9:
  - Alterar dados de outro utilizador

### Gestão de Quartos: 

- [x] Use Case 10:
  - Registar quarto
- [x] Use Case 11:
  - Editar quarto
- [x] Use Case 12:
  - Remover quarto

### Gestão de Manutenções:

- [x] Use Case 13:
  - Registar manutenção a realizar
- [x] Use Case 14:
  - Registar manutenção finalizada
- [x] Use Case 15:
  - Cancelar manutenção
- [x] Use Case 16:
  - Consultar lista de manutenções

### Gestão de Reservas:

- [x] Use Case 17:
  - Solicitar sugestão de reserva
- [x] Use Case 18:
  - Efetuar reserva
- [ ] Use Case 19:
  - Confirmar reserva 
- [x] Use Case 20:
  - Cancelar reserva
- [x] Use Case 21:
  - Check-in de reserva
- [x] Use case 22:
  - Check-out de reserva

### Gestão de Ocorrências e Avaliações

- [ ] Use Case 23:
  - Registar ocorrências 
- [ ] Use Case 24:
  - Consultar ocorrências por hóspede 
- [ ] Use Case 25: 
  - Consultar ocorrências 
- [ ] Use Case 26:
  - Avaliar estadia
- [ ] Use Case 27:
  - Consultar listagem de avaliações de estadias

---
## Estrutura do Projeto

As classes _Manager, Employee e Guest_ têm os seus respetivos métodos, que permitem que cada tipo de utilizador aceda apenas às operações que lhe são permitidas, garantindo a restrição de acessos não autorizados. Estes utilizadores invocam os métodos adequados nos respectivos serviços (services) para realizar as operações, assegurando que as permissões de acesso sejam respeitadas em cada caso.

O projeto segue a seguinte estrutura:
```plaintext

├── pom.xml
├── Readme.md
├── src
│   ├── main
│   │   └── java
│   │       └── com
│   │           └── thehotel
│   │               ├── HotelServer.java
│   │               ├── Main.java
│   │               ├── model
│   │               │   ├── Employee.java
│   │               │   ├── Guest.java
│   │               │   ├── Maintenance.java
│   │               │   ├── Manager.java
│   │               │   ├── Reservation.java
│   │               │   ├── ReservationSuggestion.java
│   │               │   ├── Room.java
│   │               │   ├── RoomRequest.java
│   │               │   └── User.java
│   │               └── services
│   │                   ├── AccountService.java
│   │                   ├── HotelService.java
│   │                   ├── MaintenanceService.java
│   │                   ├── ReservationService.java
│   │                   └── RoomService.java
│   └── test
│       └── java
│           └── com
│               └── thehotel
│                   ├── AccountServiceTest.java
│                   ├── AppTest.java
│                   ├── HotelServiceTest.java
│                   ├── MaintenanceServiceTest.java
│                   ├── ReservationServiceTest.java
│                   └── RoomServiceTest.java
└── target
├── classes
│   └── com
│       └── thehotel
│           ├── HotelServer.class
│           ├── Main.class
│           ├── model
│           │   ├── Employee.class
│           │   ├── Guest.class
│           │   ├── Maintenance.class
│           │   ├── Manager.class
│           │   ├── Reservation.class
│           │   ├── ReservationSuggestion.class
│           │   ├── Room.class
│           │   ├── RoomRequest.class
│           │   └── User.class
│           └── services
│               ├── AccountService.class
│               ├── HotelService.class
│               ├── MaintenanceService.class
│               ├── ReservationService.class
│               └── RoomService.class
├── generated-sources
│   └── annotations
└── maven-status
└── maven-compiler-plugin
└── compile
└── default-compile
├── createdFiles.lst
└── inputFiles.lst
```


### **Modelos Implementados**

- **User (Classe Pai)**  
  A classe `User` serve como base para todos os tipos de utilizadores do sistema, contendo os atributos comuns a todos os utilizadores autenticados. Esta classe inclui informações pessoais, dados de contacto, e o status do utilizador no sistema.

  Atributos principais:
  - `id`: Identificador único do utilizador.
  - `email`: Endereço de email do utilizador, utilizado para login.
  - `password`: Palavra-passe do utilizador para autenticação.
  - `fullName`: Nome completo do utilizador.
  - `nif`: Número de Identificação Fiscal do utilizador.
  - `birthDate`: Data de nascimento do utilizador.
  - `phone`: Número de telefone de contacto do utilizador.
  - `country`: País de residência do utilizador.
  - `address`: Morada do utilizador.
  - `zipCode`: Código postal da morada do utilizador.
  - `role`: Função do utilizador (hóspede, funcionário, gestor).
  - `status`: Estado atual da conta
  - `hotelServer`: Referência ao servidor do hotel associado ao utilizador, para operações relacionadas com o hotel.


- **Manager (Gestor)**  
  A classe `Manager` herda da classe `User` e representa um utilizador com permissões de gestão no sistema. O gestor é responsável pela supervisão e administração do hotel, sendo capaz de controlar as operações e recursos essenciais.

  Atributos adicionais:
  - `iban`: Número de identificação bancária do gestor, utilizado para associar o gestor à sua conta bancária.
  - `citizenCard`: Número do cartão de cidadão do gestor, utilizado para a identificação oficial.


- **Employee (Empregado)**  
  A classe `Employee` herda da classe `User` e representa os empregados do hotel, responsáveis por executar tarefas operacionais essenciais, como o atendimento ao cliente e a manutenção dos quartos. Os empregados podem ocupar diversas funções, dependendo das necessidades do hotel.

  Atributos adicionais:
  - `iban`: Número de identificação bancária do empregado, utilizado para associar o empregado à sua conta bancária.
  - `citizenCard`: Número do cartão de cidadão do empregado, utilizado para a identificação oficial.


- **Guest (Hóspede)**  
  A classe `Guest` herda de `User` e representa os hóspedes que fazem reservas no hotel. A classe contém informações específicas dos hóspedes, como preferências e histórico de reservas.  


- **Room (Quarto)**  
  A classe `Room` representa os quartos do hotel, armazenando informações detalhadas sobre cada um deles. Cada quarto tem um conjunto de características que determinam o seu tipo, capacidade e preço, além do seu estado atual.

  Atributos principais:
  - `id`: Identificador único do quarto.
  - `maxGuests`: Número máximo de hóspedes que o quarto pode acomodar.
  - `numBeds`: Número de camas disponíveis no quarto.
  - `viewType`: Tipo de vista disponível a partir do quarto (ex: vista para o mar, vista para a cidade).
  - `hasKitchen`: Indica se o quarto tem uma cozinha (true/false).
  - `hasBalcony`: Indica se o quarto tem uma varanda (true/false).
  - `numWC`: Número de casas de banho no quarto.
  - `pricePerNight`: Preço por noite para o quarto.
  - `status`: Estado atual do quarto (ex: disponível, reservado, em manutenção).
  - `registrationDate`: Data em que o quarto foi registado no sistema.
  - `idUserRegistration`: Identificador do utilizador responsável pelo registo do quarto.
  - `needsMaintenance`: Indica se o quarto necessita de manutenção (true/false).


- **Reservation (Reserva)**  
  A classe `Reservation` representa as reservas feitas pelos hóspedes no hotel. Ela contém todas as informações relativas ao quarto reservado, os períodos de estadia e o estado atual da reserva.

  Atributos principais:
  - `id`: Identificador único da reserva.
  - `idGuest`: Identificador do hóspede que fez a reserva.
  - `idReservSuggestion`: Identificador da sugestão de reserva associada, caso aplicável.
  - `checkInDate`: Data de check-in da reserva.
  - `checkOutDate`: Data de check-out da reserva.
  - `totalPrice`: Preço total da estadia, calculado com base nas noites reservadas e nas características do quarto.
  - `status`: Estado da reserva (ex: confirmada, cancelada, pendente).


- **ReservationSuggestion (Sugestão de Reserva)**  
  A classe `ReservationSuggestion` gera sugestões automáticas de reserva com base na disponibilidade dos quartos e nas preferências dos hóspedes. Estas sugestões são criadas para otimizar a alocação de quartos, considerando o número de hóspedes e as datas de check-in e check-out.

  Atributos principais:
  - `id`: Identificador único da sugestão de reserva.
  - `totalGuest`: Número total de hóspedes na sugestão de reserva.
  - `totalRooms`: Número total de quartos sugeridos.
  - `requestRooms`: Lista de pedidos de quartos feitos pelo hóspede, que pode incluir preferências específicas (ex: tipo de quarto, número de camas).
  - `sugestionRooms`: Lista de quartos sugeridos, de acordo com a disponibilidade e as preferências do hóspede.
  - `checkInDate`: Data de check-in sugerida.
  - `checkOutDate`: Data de check-out sugerida.
  - `totalPrice`: Preço total calculado para a sugestão de reserva, com base no número de quartos e no período de estadia.
  - `used`: Um valor booleano que indica se a sugestão de reserva foi associada a uma reserva confirmada.


- **RoomRequest (Pedido de Quarto)**  
  A classe `RoomRequest` representa os pedidos específicos feitos pelos hóspedes ao reservar um quarto, incluindo preferências sobre o tipo de quarto, características desejadas e requisitos especiais para a estadia. Estes pedidos podem ser usados para personalizar a experiência do hóspede, garantindo que as suas necessidades sejam atendidas.

  Atributos principais:
  - `numGuests`: Número de hóspedes para o quarto solicitado.
  - `numBeds`: Número de camas desejadas no quarto.
  - `viewType`: Tipo de vista solicitada para o quarto (ex: vista para o mar, vista para a cidade).
  - `hasKitchen`: Indica se o hóspede prefere um quarto com cozinha (booleano).
  - `hasBalcony`: Indica se o hóspede prefere um quarto com varanda (booleano).
  - `numWC`: Número de casas de banho desejadas no quarto.


- **Maintenance (Manutenção)**  
  A classe `Maintenance` representa as manutenções realizadas nos quartos do hotel. Cada manutenção é registada com detalhes sobre o quarto em questão, a descrição do problema, e os utilizadores envolvidos em diferentes fases do processo (registo, conclusão e cancelamento).

  Atributos principais:
  - `id`: Identificador único da manutenção.
  - `roomId`: Identificador do quarto associado à manutenção.
  - `description`: Descrição do problema ou tarefa de manutenção a ser realizada.
  - `dateRegister`: Data em que a manutenção foi registada.
  - `idUserRegister`: Identificador do utilizador que registou a manutenção.
  - `idUserConclusion`: Identificador do utilizador que concluiu a manutenção.
  - `idUserCancellation`: Identificador do utilizador que cancelou a manutenção, se aplicável.
  - `dateConclusion`: Data de conclusão da manutenção.
  - `dateCancellation`: Data de cancelamento da manutenção, se aplicável.
  - `status`: Estado atual da manutenção (ex: em andamento, concluída, cancelada).


## **Serviços Implementados**

As classes de serviço contêm a lógica de negócios e operações que permitem a gestão e a execução das funcionalidades do sistema.

- **AccountService (Serviço de Contas)**  
  A classe `AccountService` é responsável por gerir todas as operações relacionadas com as contas de utilizadores, incluindo a criação, atualização, eliminação e autenticação de utilizadores.  
  Funções principais:
  - **`createGuestAccount()`**: Cria uma conta de hóspede.
    - Parâmetros: `fullName`, `nif`, `email`, `birthDate`, `address`, `zipCode`, `country`, `phone`, `hotelServer`.
    - Verifica se os dados fornecidos são válidos e se o email e o NIF são únicos. Cria e regista a conta de hóspede.

  - **`createStaffAccount()`**: Cria uma conta de funcionário (gestor ou empregado).
    - Parâmetros: `user`, `role`, `fullName`, `nif`, `email`, `birthDate`, `address`, `zipCode`, `country`, `phone`, `iban`, `citizenCard`, `hotelServer`.
    - Verifica se o utilizador tem permissões de gestor para criar contas de staff. Cria e regista a conta de funcionário com base no papel especificado (`manager` ou `employee`).

  - **`getAllUsers()`**: Obtém todos os utilizadores do sistema, mas apenas se o utilizador for um gestor.
    - Parâmetros: `requester`.
    - Retorna a lista de todos os utilizadores.

  - **`getUserById()`**: Obtém um utilizador a partir de um ID.
    - Parâmetros: `id`.
    - Retorna o utilizador associado ao ID fornecido.

  - **`addUser()`**: Adiciona um utilizador à lista de utilizadores.
    - Parâmetros: `user`.
    - Adiciona um novo utilizador à lista de utilizadores do sistema.

  - **`validateFieldsUser()`**: Valida os campos de um utilizador, verificando se estão no formato correto.
    - Parâmetros: `fullName`, `nif`, `email`, `birthDate`, `address`, `zipCode`, `country`, `phone`.
    - Retorna `true` se todos os campos forem válidos; caso contrário, retorna `false`.

  - **`isUniqueEmail()`**: Verifica se o email fornecido é único no sistema.
    - Parâmetros: `newEmail`.
    - Retorna `true` se o email não estiver associado a outra conta, caso contrário, retorna `false`.

  - **`isUniqueNif()`**: Verifica se o NIF fornecido é único no sistema.
    - Parâmetros: `newNif`.
    - Retorna `true` se o NIF não estiver associado a outra conta, caso contrário, retorna `false`.

  - **`listGuests()`**: Lista todos os hóspedes. Apenas acessível a gestores.
    - Parâmetros: `userReq`.
    - Retorna a lista de hóspedes (utilizadores com papel de `guest`).

  - **`listEmployees()`**: Lista todos os funcionários. Apenas acessível a gestores.
    - Parâmetros: `requester`.
    - Retorna a lista de funcionários (utilizadores com papel de `employee`).

  - **`getPersonalData()`**: Obtém os dados pessoais de um utilizador específico.
    - Parâmetros: `user`, `userId`.
    - Verifica permissões de acesso e retorna as informações pessoais do utilizador com o ID fornecido.

  - **`changePersonalData()`**: Permite alterar os dados pessoais de um utilizador.
    - Parâmetros: `user`, `nif`, `email`, `fullName`, `birthDate`, `address`, `zipCode`, `country`, `phone`.
    - Altera os dados pessoais do utilizador conforme os parâmetros fornecidos, verificando se os dados são válidos.

  - **`changeUserPersonalData()`**: Permite que um gestor altere os dados pessoais de outro utilizador.
    - Parâmetros: `user`, `userId`, `nif`, `email`, `fullName`, `birthDate`, `address`, `zipCode`, `country`, `phone`.
    - Verifica permissões de acesso e altera os dados do utilizador com o ID fornecido.


- **HotelService (Serviço do Hotel)**  
  O `HotelService` gere operações gerais relacionadas com o hotel, como consultar informações sobre o hotel e listar os quartos disponíveis.  
  Funções principais:
  - **`getGeneralInfo()`**: Obtém informações gerais sobre o hotel.
    - Retorna informações detalhadas sobre o hotel, incluindo o nome, endereço, classificação, descrição, comodidades, horários de check-in e check-out, política de cancelamento e contatos.


- **ReservationService (Serviço de Reservas)**
O `ReservationService` é responsável por gerir todas as operações relacionadas com reservas e sugestões de reserva, incluindo a criação, cancelamento, confirmação e gestão de dados de reservas.

 **Funções principais:**

- **`requestReservationSuggestion()`**:  
  Gera uma sugestão de reserva com base nas necessidades do utilizador.
  - **Parâmetros**: `user`, `totalGuests`, `totalRooms`, `roomRequests`, `checkInDate`, `checkOutDate`.
  - Calcula uma sugestão de reserva considerando a disponibilidade e as preferências.

- **`createReservation()`**:  
  Cria uma nova reserva com base nas informações fornecidas.
  - **Parâmetros**: `user`, `reservationRequest`.
  - Valida os dados e cria a reserva no sistema.

- **`confirmReservation()`**:  
  Confirma uma reserva pendente.
  - **Parâmetros**: `user`, `reservationId`.
  - Marca a reserva como confirmada.

- **`cancelReservation()`**:  
  Cancela uma reserva existente.
  - **Parâmetros**: `user`, `reservationId`.
  - Atualiza o estado da reserva para cancelada.

- **`getAllReservations()`**:  
  Obtém a lista de todas as reservas no sistema.
  - **Parâmetros**: `user`.
  - Retorna todas as reservas, filtradas por permissões do utilizador.

- **`getReservationById()`**:  
  Obtém os detalhes de uma reserva específica.
  - **Parâmetros**: `user`, `reservationId`.
  - Verifica permissões e retorna os detalhes da reserva.

- **`validateReservationDates()`**:  
  Valida as datas de uma reserva.
  - **Parâmetros**: `checkInDate`, `checkOutDate`.
  - Garante que as datas são válidas e consistentes.

- **`filterReservationsByDate()`**:  
  Filtra as reservas com base num período temporal.
  - **Parâmetros**: `user`, `startDate`, `endDate`.
  - Retorna todas as reservas dentro do período indicado.

- **MaintenanceService (Serviço de Manutenção)**

O `MaintenanceService` é responsável por gerir todas as operações relacionadas com a manutenção dos quartos do hotel, incluindo registo, consulta, conclusão e cancelamento de manutenções.

Funções principais:

- **`generateMaintenanceId()`**: Gera um ID único para uma nova manutenção.
  - **Parâmetros**: Nenhum.
  - Gera e retorna um ID único que pode ser utilizado para identificar uma manutenção específica.

- **`getAllMaintenances()`**: Obtém todas as manutenções registadas no sistema.
  - **Parâmetros**: `user`.
  - Retorna uma lista de todas as manutenções registadas no sistema, acessível apenas a utilizadores com permissões adequadas.

- **`getMaintenanceById()`**: Obtém os detalhes de uma manutenção específica pelo ID.
  - **Parâmetros**: `user`, `maintenanceId`.
  - Recupera os detalhes de uma manutenção específica, identificado pelo `maintenanceId`. Apenas utilizadores autorizados podem acessar estas informações.

- **`registerMaintenance()`**: Regista uma nova manutenção para um quarto específico.
  - **Parâmetros**: `user`, `roomId`, `description`, `roomService`.
  - Regista uma manutenção associada a um quarto específico. Inclui uma descrição detalhada do problema a ser resolvido.

- **`registerMaintenanceCompletion()`**: Marca uma manutenção como concluída.
  - **Parâmetros**: `user`, `maintenanceId`, `conclusionDate`, `roomService`.
  - Marca uma manutenção como concluída, associando a data de conclusão e outros detalhes pertinentes.

- **`cancelMaintenance()`**: Cancela uma manutenção registada.
  - **Parâmetros**: `user`, `maintenanceId`, `roomService`.
  - Cancela uma manutenção previamente registada no sistema, tornando-a inacessível para futuras ações.

- **`getMaintenanceByStatus()`**: Obtém as manutenções filtradas pelo seu estado (pendente, concluída ou cancelada).
  - **Parâmetros**: `user`, `status`.
  - Recupera uma lista de manutenções filtradas pelo estado especificado (por exemplo, "pendente", "concluída", "cancelada").

- **`getMaintenanceRoom()`**: Obtém as manutenções associadas a um quarto específico.
  - **Parâmetros**: `roomId`.
  - Retorna as manutenções associadas a um quarto específico, permitindo um controle detalhado da manutenção de cada quarto.


- **RoomService(Serviço de Quartos)**

O `RoomService` é responsável por gerir as operações associadas aos quartos do hotel, incluindo a criação, consulta, edição, remoção e atualização do estado de manutenção dos quartos.

Funções principais:

- **`generateRoomId()`**: Gera um ID único para um novo quarto.
  - **Parâmetros**: Nenhum.
  - Gera e retorna um ID único para identificar um quarto.

- **`getAllRooms()`**: Obtém todos os quartos registados no sistema.
  - **Parâmetros**: `user` (utilizador que solicita a lista de quartos).
  - Retorna uma lista de todos os quartos. O acesso é restrito a utilizadores com permissões adequadas.

- **`getRoomById()`**: Obtém os detalhes de um quarto específico pelo ID.
  - **Parâmetros**: `user`, `roomId`.
  - Recupera os detalhes de um quarto específico, acessível apenas a utilizadores autorizados.

- **`registerRoom()`**: Regista um novo quarto no sistema.
  - **Parâmetros**: `user`, `maxGuests`, `numBeds`, `viewType`, `hasKitchen`, `hasBalcony`, `numWC`, `pricePerNight`.
  - Regista um quarto com os dados fornecidos. Apenas gestores têm permissão para registar novos quartos.

- **`editRoom()`**: Edita as características de um quarto existente.
  - **Parâmetros**: `user`, `roomId`, `maxGuests`, `numBeds`, `viewType`, `hasKitchen`, `hasBalcony`, `numWC`, `pricePerNight`.
  - Edita os detalhes de um quarto já registado. O acesso é restrito a gestores.

- **`removeRoom()`**: Remove um quarto do sistema.
  - **Parâmetros**: `user`, `roomId`.
  - Remove um quarto do sistema. Só gestores têm permissão para remover quartos. Um quarto não pode ser removido se estiver ocupado ou inativo.

- **`updateRoomMaintenanceStatus()`**: Atualiza o estado de manutenção de um quarto.
  - **Parâmetros**: `user`, `roomId`, `needsMaintenance`.
  - Atualiza o estado de manutenção de um quarto (necessita ou não de manutenção). Apenas gestores ou funcionários podem atualizar esse estado.

- **`validateRoomData()`**: Valida os dados fornecidos para um quarto.
  - **Parâmetros**: `maxGuests`, `numBeds`, `viewType`, `numWC`, `pricePerNight`.
  - Valida os dados fornecidos para garantir que são válidos, como número de hóspedes, tipo de vista, preço por noite, etc.


---
## Tecnologias Utilizadas

Este projeto foi desenvolvido em Java, utilizando o Maven para a gestão de dependências e automação do processo de construção do projeto. O Git foi utilizado para controlo das versões, permitindo o acompanhamento das alterações com commits regulares e pushes.

---

## Testes Realizados

Foram realizados testes completos a todos os métodos do sistema, garantindo que todas as funcionalidades foram devidamente verificadas. Os testes abrangeram cenários de sucesso, como a criação de utilizadores, a gestão de reservas, a validação de dados e a gestão de quartos. Também foram incluídos testes para verificar a funcionalidade de sugestões, acesso aos dados pessoais, e alteração de informações. Para além disso, foram testados cenários com parâmetros inválidos, como dados incorretos, duplicação de NIF ou email, e acessos não autorizados, garantindo que o sistema lida corretamente com entradas inválidas e respeita as permissões de acesso para diferentes tipos de utilizadores. Em suma, todos os métodos do sistema foram exaustivamente testados para garantir a sua robustez e fiabilidade.

---
## Conclusão

O desenvolvimento deste sistema de gestão de reservas de hotel em Java foi um desafio significativo, mas também uma excelente oportunidade para aplicar conceitos de programação orientada a objetos, estruturas de dados e design de sistemas. O sistema implementado permite a gestão eficiente de diversos aspectos operacionais de um hotel, como a criação de contas de utilizadores, a gestão de quartos e manutenções, o processamento de reservas, e a gestão de ocorrências e avaliações.

Durante o desenvolvimento, foram alcançados diversos marcos importantes, incluindo a criação de funcionalidades essenciais como a gestão de contas de hóspedes e funcionários, o registo e edição de quartos, a realização e cancelamento de reservas, e a gestão de manutenções.

O sistema foi testado de forma abrangente, utilizando testes unitários para garantir que todas as funcionalidades principais estão a funcionar corretamente.

Em suma, o sistema desenvolvido demonstra a viabilidade de uma solução integrada para a gestão de reservas de um hotel, sendo flexível o suficiente para permitir a implementação de melhorias e a expansão de funcionalidades no futuro. A conclusão deste trabalho representa um passo importante na construção de uma plataforma robusta para gestão hoteleira, com potencial para ser integrada a sistemas externos e otimizada para diversas necessidades operacionais.
