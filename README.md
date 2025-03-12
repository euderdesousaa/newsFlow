# NewsFlow 🌎📰

O **NewsFlow** é uma aplicação que fornece notícias personalizadas de acordo com a localização do usuário. Utiliza duas APIs de notícias (*TheNewsApi* e *AbstractApi*), além de um sistema de autenticação que libera funcionalidades extras, como favoritar notícias. No futuro, um sistema de *Machine Learning* será integrado para aprimorar os filtros de recomendação.

## 🚀 Funcionalidades

- 🔍 **Busca de Notícias**: Integração com *TheNewsApi* e *AbstractApi*.
- 🌍 **Filtragem por Localização**: Utilização do *GeoIP* para exibir notícias relevantes com base no IP do usuário.
- 📝 **CRUD de Notícias**: Consulta, listagem e gerenciamento de notícias obtidas das APIs.
- 🔐 **Autenticação e Registro**: Implementação de *OAuth2* para controle de acesso.
- ⭐ **Favoritos**: Usuários autenticados podem salvar suas notícias favoritas.
- 🤖 **Futuro**: Integração de *Machine Learning* para melhorar as recomendações de notícias.

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.4**
- **MongoDB**
- **OAuth2**
- **Lombok**
- **GeoIP** (Geolocalização por IP)
- **TheNewsApi** e **AbstractApi** (Fontes de notícias)

## 📡 Endpoints da API

### 📢 Notícias
- `GET /api/v1/news/{categories}` - Buscar notícias por categoria (*general, science, sports, business, health, entertainment, tech, politics, food, travel*).
- `GET /api/v1/news/top-news` - Buscar as melhores notícias.
- `GET /api/v1/news/search` - Pesquisar qualquer notícia via título.
- `GET /api/v1/news/another` - Buscar outras notícias.

### 🔐 Autenticação
- `POST /api/v1/auth/signup` - Criar uma conta.
- `POST /api/v1/auth/login` - Realizar login.
- `PUT /api/v1/modify/{username}` - Modificar dados do usuário.

## 📦 Como Executar o Projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/euderdesousaa/NewsFlow.git
   ```
2. Configure as variáveis de ambiente para as APIs e banco de dados.
3. Execute a aplicação:
   ```bash
   mvn spring-boot:run
   ```
4. Acesse a aplicação no navegador: `http://localhost:8080`

## 📌 Roadmap Futuro

- [ ] Implementação de Machine Learning para recomendações inteligentes 📊
- [ ] Criação de uma interface para melhorar a experiência do usuário 🖥️
- [ ] Implementação de notificações 📢

## 🤝 Contribuição
Fique à vontade para contribuir! Basta abrir uma *issue* ou enviar um *pull request* com melhorias.

---

💡 *NewsFlow: Mantendo você sempre informado, de forma personalizada!*

