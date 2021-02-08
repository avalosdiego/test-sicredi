
# Setup
Clone https://github.com/avalosdiego/test-sicredi.git <br>
Projeto na pasta sicredi.

# Run
open cmd <br>
cd {pasta projeto} <br>
mvn clean install <br>
java -jar target/sicredi-0.0.1-SNAPSHOT.jar <br>
Obs: Maven deve estar instalado ou também pode ser buildado pelo eclipse ou IDEs de escolha, no
meu caso utilizei as ferramentas do eclipse com maven embedded.


# Dependências
  spring-boot-starter <br>
  spring-boot-starter-data-rest <br>
  spring-boot-devtools <br>
  spring-boot-starter-data-jpa <br>
  spring-boot-starter-validation <br>
  spring-boot-starter-activemq <br>
  com.h2database : h2 <br>
  io.springfox > springfox-boot-starter > 3.0.0

# Profiles
Default > application.properties <br>
Test > application-test.properties

# Banco H2
A persistência dos dados é feita em banco de dados em memória.

# Documentation Swagger
URL: http://localhost:8080/swagger-ui/?urls.primaryName=Vers%C3%A3o%20API%202 <br>
Foram realizadas duas versões da API, V1 e V2.

# Métodos da API
GET 		/pautas/v2 			listar <br>
POST		/pautas/v2 			cadastrar <br>
GET 		/pautas/v2/{id} 		detalhar <br>
PUT 		/pautas/v2/{id} 		atualizar <br>
DELETE 	/pautas/v2/{id} 			remover <br>
PUT 		/pautas/v2/{id}/abrirSessao 	abrirSessao <br>
POST 		/pautas/v2/{id}/votar 		votar

# Mensageria ActiveMQ
Assim que uma sessão é fechada pelo scheduler, uma mensagem é enviada para o ActiveMQ. <br>
Mensagem publicada: {"pautaId": "1","numeroVotos": "0","numeroVotosSim": "0","numeroVotosNao": "0"}

# Mais recurso utlizados
Habilitado cache para as consultas do controller, não havia necessidade nas especificações do test, 
mas adicionei como funcionalidade, o cache está funcionando na listagem de pautas (v2/pautas) para não realizar
consultas desnecessárias no banco.
Também foi habilitada paginação da consulta da listagem de pautas.

# Funcionamento do scheduling
Está configurado diretamente no código (ScheduledTasks) para rodar verificar de 5 em 5 segundos, ele verifica as pautas
que estão com status OPENED e que devem ser fechadas, ou seja, dtFechamento anterior a data atual, setando o status da 
pauta para CLOSE, contabilizando os votos da sessão e enviado a mensagem para o ActiveMQ.

# Model, Dto, Form
Classes model para persistência dos dados. <br>
Dtos para o retorno da API, enviando assim apenas os dados que devem ser apresentados e não a base de dados <br>
Form para a entrada de dados na API.

# RestControllerAdvice
Handler de erros padronizado.

# Cenário
1) Cadastrar uma pauta {POST /pautas/v2} <br>
2) Abrir sessão {PUT /pautas/v2/{id}/abrirSessao} <br>
	- Valida se pauta existe
3) Votar em uma sessão {POST /pautas/v2/{id}/votar} <br>
	- Valida se sessão foi iniciada; <br>
	- Valida se sessão já foi encerrada pela dtFechamento <br>
	- Valida se usuário já votou na sessão <br>
	- Valida se usuário existe e está apto a votar (https://user-info.herokuapp.com/users/{cpf})
4) Scheduler fecha sessão aberta e contabiliza votos, enviando mensagem para o ActiveMQ

# TODO
- Testes de performance <br>
- Testas toda a API, devido ao tempo não implementei todos os testes que eu gostaria. <br>
