💈 Barber Shop Manager

Um sistema desktop desenvolvido em Java (JavaFX + SQLite) para gerenciamento de uma barbearia, com funcionalidades de cadastro de clientes, agendamento, controle de serviços, relatórios financeiros e gerenciamento de usuários.

🚀 Funcionalidades
🔐 Autenticação

Login com diferentes níveis de acesso:

Admin: gerencia usuários, serviços e acessa relatórios.

Usuário comum (barbeiro): registra atendimentos e consulta histórico de clientes.

Usuário admin criado automaticamente (login: admin, senha: 123).

👤 Clientes

Cadastro de clientes com nome, telefone e data de nascimento.

Histórico de serviços realizados vinculado ao cliente.


✂️ Serviços

Cadastro de tipos de serviço (nome, preço e comissão).

Edição e exclusão de serviços.

Registro de serviços realizados para clientes, associando ao barbeiro logado.


📅 Agendamento

Agendamento de clientes com data e hora.

Listagem, edição e exclusão de agendamentos.


📊 Relatórios

Relatório de atendimentos com:

Total de clientes atendidos.

Faturamento total.

Valor de comissão do barbeiro.

Filtros automáticos:

Hoje

Últimos 7 dias

Últimos 30 dias

Período personalizado via DatePicker.


🛠️ Tecnologias Utilizadas

Java 17+

JavaFX (interface gráfica)

SQLite (banco de dados local)

DAO Pattern (persistência organizada)

Maven (gerenciamento de dependências)


📂 Estrutura do Projeto

📦 barber-shop-manager
 ┣ 📂 src/main/java/org/example/barber
 ┃ ┣ 📂 controllers      # Controllers JavaFX
 ┃ ┣ 📂 DAO              # Classes de acesso ao banco (DAO)
 ┃ ┣ 📂 entities         # Entidades (Cliente, Serviço, Usuário, etc.)
 ┃ ┣ Application.java    # Classe principal
 ┣ 📂 src/main/resources
 ┃ ┣ 📂 fxml             # Telas JavaFX (FXML)
 ┃ ┣ 📂 css              # Arquivos de estilo
 ┃ ┣ database.db         # Banco SQLite


⚙️ Como Rodar

Clonar o repositório

git clone https://github.com/seu-usuario/barber-shop-manager.git

Abrir no IntelliJ/Eclipse/VS Code
Certifique-se de ter o Java 17+ instalado.

Rodar a aplicação
Execute a classe:

Application.java


🔑 Usuário Inicial

Login: admin
Senha: 123

📌 Melhorias Futuras

Exportar relatórios em PDF ou CSV.

Sistema de permissões mais detalhado.

Interface mais responsiva e moderna.

Backup automático do banco de dados.

🧑‍💻 Autor

Projeto desenvolvido por Danilo Nascimento de Andrade ✨
📧 Contato: daniloageu@gmail.com
🔗 [![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/danilo-nascimentodev)
