# 🛡️ Gatekeeper API - Sistema de Gestão de Acessos a Módulos

> **Projeto Final - Disciplina de WEB II** > **Instituto Federal Goiano - Campus Urutaí**

O **Sistema de Gestão de Acessos a Módulos** é um ecossistema Back-end robusto desenvolvido para a empresa fictícia *LogiTech Distribuidora*. O sistema atua como uma plataforma centralizada para a intermediação, concessão e auditoria de acessos temporários a módulos corporativos críticos, mitigando riscos de segurança e garantindo conformidade operacional.

## 🎯 Principais Funcionalidades

- **Controle de Acesso Baseado em Perfis (RBAC):** Hierarquia estrita de privilégios (`ROLE_ADMIN` > `ROLE_GESTOR` > `ROLE_COMUM`).
- **Gestão de Solicitações e Pareceres:** Fluxo completo de pedido de acesso temporário a módulos, com aprovação/rejeição formal por gestores responsáveis.
- **Auditoria Imutável (Append-Only):** Histórico de acessos blindado contra alterações manuais via API.
- **Proteção contra IDOR:** Validações de contexto de segurança nativas para impedir acessos indevidos a dados de terceiros.
- **Notificações:** Alertas automatizados sobre a mudança de status das solicitações.

## 💻 Tecnologias e Padrões Arquiteturais

A aplicação foi construída utilizando uma **Arquitetura em Camadas** (Controller, Service, Repository, Model) e adota os seguintes padrões de projeto e tecnologias:

- **Linguagem:** Java 17+
- **Framework Principal:** Spring Boot (Spring Web, Spring Data JPA)
- **Segurança:** Spring Security com filtro *stateless* e tokens **JWT (JSON Web Token)**
- **Mapeamento e Transferência:** MapStruct, DTOs (Data Transfer Objects).
- **Design Patterns:** Value Objects (VO) para validação estrutural de e-mail.
- **Documentação:** OpenAPI 3 / Swagger UI
- **Performance:** Estratégia de caching abstraída com *Caffeine* para recursos de alta leitura.
