# API - Construtor de Formulários
Esta API é responsável por gerenciar formulários e autenticação de usuários. Foi desenvolvida utilizando Java Spring Boot, OAuth2, JWT para autenticação e envio de emails.

## Tecnologias Utilizadas
- Java Spring Boot
- Spring Security (com suporte a JWT e OAuth2)
- Hibernate/JPA
- BCrypt para criptografia de senhas
- Token JWT para autenticação stateless
- OAuth2
- EmailService para envio de emails
- Banco de Dados (PostgreSQL)

## Rotas da API
### Autenticação (Auth Controller)

#### Endpoint principal = /auth

| Método | Endpoint  | Descrição                            | Corpo da requisição |
|--------|-----------|--------------------------------------|---------------------| 
| POST   | /login    | Realiza login e retorna o token JWT. | { "email": "user@mail.com", "password": "senha" } |
| POST   | /register | Cria um novo usuário na api.         | { "email": "user@mail.com", "password": "senha" } |

### Fluxo de autenticação

1. Registro de usuários:
    - Endpoint: /auth/register
    - Usuários recebem um email de verificação.
   
2. Login:
   - Endpoint: /auth/login
   - Usuário fornece email e senha válidos.
   - API retorna um token JWT válido para fazer futuras requisições.
   
3. Acesso aos Recursos Protegidos:
    - Todas as rotas em /api/forms requerem um token JWT válido no header:
      - `Authorization: Bearer <token_jwt>`
      
4. Login OAuth2(ainda está em fase de testes)
    - Opcionalmente, usuários podem utilizar provedores OAuth2 que também irão retornar tokens jwt para eles.

### Formulários (Form Controller)

#### Endpoint principal = /api/forms

| Método | Endpoint         | Descrição                                                                              | Corpo da requisição                                                 |
|--------|------------------|----------------------------------------------------------------------------------------|---------------------------------------------------------------------| 
| POST   | /register | Registra um usuário temporário para responder formulários.                             | { "email": "user@mail.com", "password": "senha" }                   |
| POST   | / | Cria um novo formulário.                                                               | { "title": "Form Title", "description": "...", "questions": [...] } |
| POST   | /{idPublic}/answers | Responde um formulário publicado.                                                      | [{"questionId": ..., "response": ...}]                              |
| GET    | /my-forms | Retorna todos os formulários do usuário autenticado.                                   | .                                                                   |
| GET    | /my-forms/public | Retorna todos os formulários públicos do usuário autenticado.                          | .                                                                   |
| GET    | /{id} | Retorna um formulários específico do usuário autenticado.                              | id                                                                  |
| GET    | /public/{formHasLoginType}/{idPublic} | Retorna um formulário público baseado em seu login (público/privado/com senha).        | formHasLoginType, idPublic, password opcional                       |
| GET    | /{id}/answers | Retorna todas as respostas de um formulário (somente para os criadores do formulário). | id                                                                  |
| PUT    | /{id} | Atualiza um formulário específico.                                                     | { "title": "Form Title", "description": "...", "questions": [...] } |
| PATCH  | /{id}/publish | Publica um formulário e gera um link público para outros usuários responderem.         | id                                                                  |
| PATCH  | /default-settings/{standard} | Configura as permissões padrões dos formulários.                                       | { "password": "senha", "sendEmail": true }                          |
| DELETE | /{id} | Deleta um formulário específico.                                                       | id                                                                  |

---

### Configuração e Execução

1. Clone o repositório

```
git clone https://github.com/EricSouzaDosSantos/fullDev-Community-api.git
cd fullDev-Community-api
```

2. substitua o application.properties com suas credenciais.
    - altere o valor das variáveis de ambiente da sua máquina.
    - caso precise você pode entrar em contato comigo pelo Linkedin, para que eu possa te ajudar com isso.
```
spring.application.name=formulario

##database settings
spring.datasource.url=${URL_POSTGRES_FULLDEV}
spring.datasource.username=${USERNAME_POSTGRES_FULLDEV}
spring.datasource.password=${PASSWORD_POSTGRES_FULLDEV}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update

##jwt token settings
#api.security.token.secret=${TOKEN_SECRET}
api.security.token.secret=${JWT_SECRET:fulldev-community}

##email sending settings
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${EMAIL_FULLDEV}
spring.mail.password=${PASSWORD_EMAIL_FULLDEV}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

##login with facebook
spring.security.oauth2.client.registration.facebook.client-id=${CLIENT_ID_FACEBOOK}
spring.security.oauth2.client.registration.facebook.client-secret=${CLIENT_SECRET_FACEBOOK}
spring.security.oauth2.client.registration.facebook.scope=public_profile,email
spring.security.oauth2.client.registration.facebook.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.facebook.client-name=Facebook

spring.security.oauth2.client.provider.facebook.authorization-uri=https://www.facebook.com/v12.0/dialog/oauth
spring.security.oauth2.client.provider.facebook.token-uri=https://graph.facebook.com/v12.0/oauth/access_token
spring.security.oauth2.client.provider.facebook.user-info-uri=https://graph.facebook.com/v12.0/me?fields=id,name,email


##login with Google
spring.security.oauth2.client.registration.google.client-id=${CLIENT_ID_GOOGLE}
spring.security.oauth2.client.registration.google.client-secret=${CLIENT_SECRET_GOOGLE}

```

---

3. Execute o seu projeto
    - caso tenha mvn:

   ```mvn spring-boot:run```

    - caso não possua o mvn, você pode executar o projeto no botão de run da sua IDE.

4. Testar as rotas
    - Você pode usar ferramentas como o Postman ou Insomnia para testar os endpoints da api(recomendo que utilize o postman).

---

## Colaboradores

<div>
  <a href="https://github.com/EricSouzaDosSantos">
    <img src="https://github.com/EricSouzaDosSantos.png" width="60px"/>
  </a>
  <a href="https://github.com/gabrieldiassantiago">
    <img src="https://github.com/gabrieldiassantiago.png" width="60px"/>
  </a>
  <a href="https://github.com/Carlosaugusto222">
    <img src="https://github.com/Carlosaugusto222.png" width="60px"/>
  </a>
  <a href="https://github.com/DevGustavoGantois">
    <img src="https://github.com/DevGustavoGantois.png" width="60px"/>
  </a>
</div>

---

## Contribuição

Este projeto foi desenvolvido para o hackaton da fullDev, mas sinta-se à vontade para contribuir com sugestões, correções de bugs ou até mesmo novas funcionalidades. Todas as contribuições são bem-vindas!

---
