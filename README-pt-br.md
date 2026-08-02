[English version](README.md)

[English version](README.md)

# 🎯 CRM Pipeline

Um aplicativo de desktop profissional de **Gerenciamento de Pipeline de Vendas** construído com **JavaFX**, apresentando um quadro Kanban totalmente interativo com arrastar e soltar, estatísticas em tempo real, armazenamento persistente de dados e uma interface escura elegante.

---

## 📸 Pré-visualização

```
┌─────────────────────────────────────────────────────────────────────┐
│  ◈ CRM Pipeline          [Search...]              [ + New Lead ]    │
├──────────┬──────────────┬───────────────┬──────────┬───────────────┤
│TOTAL     │ PIPELINE     │ CLOSED VALUE  │CONVERSION│ DEALS CLOSED  │
│ 6 leads  │ $128,000     │ $18,000       │  16%     │ 1 deals       │
├──────────┴──────────────┴───────────────┴──────────┴───────────────┤
│                                                                     │
│  ▌New Lead(2) ▌Contacted(2) ▌Proposal Sent(1) ▌Closed(1) ▌Lost(0) │
│  ┌─────────┐  ┌──────────┐  ┌─────────────┐  ┌────────┐           │
│  │^ HIGH   │  │^ HIGH    │  │^ HIGH       │  │- MED   │           │
│  │Maria S. │  │Anna C.   │  │Carlos Lima  │  │Fernanda│           │
│  │TechCorp │  │Logistics │  │FinTech      │  │EduTech │           │
│  │$15,000  │  │$25,000   │  │$50,000      │  │$18,000 │           │
│  └─────────┘  └──────────┘  └─────────────┘  └────────┘           │
└─────────────────────────────────────────────────────────────────────┘
```

---

## ✨ Funcionalidades

- **Quadro Kanban** — Pipeline visual com 5 estágios personalizáveis
- **Arrastar e Soltar** — Mova negócios entre colunas sem esforço
- **Estatísticas em Tempo Real** — Total de leads, valor do pipeline, valor fechado, taxa de conversão
- **Adicionar / Editar Leads** — Formulário modal completo com todos os dados de contato
- **Menu de Botão Direito** — Edite, mova ou exclua leads instantaneamente
- **Busca e Filtro** — Filtre os cartões por nome, empresa ou e-mail em tempo real
- **Sistema de Prioridade** — ALTA / MÉDIA / BAIXA com indicadores de cor
- **Persistência de Dados** — Salva automaticamente em um arquivo JSON local a cada alteração
- **Interface Escura** — Tema escuro moderno e limpo com colunas codificadas por cores

---

## 🗂️ Estágios do Pipeline

| Estágio | Cor | Descrição |
|---|---|---|
| **Novo Lead** | Roxo | Prospect identificado |
| **Contatado** | Azul | Primeiro contato feito |
| **Proposta Enviada** | Laranja | Proposta entregue |
| **Fechado** | Verde | Negócio ganho |
| **Perdido** | Vermelho | Negócio perdido |

---

## 🛠️ Stack Tecnológico

| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | 21 (LTS) | Linguagem principal |
| JavaFX | 21 | Framework de interface |
| Gson | 2.10.1 | Persistência em JSON |
| Maven | 3.9+ | Build e gerenciamento de dependências |

---

## 📁 Estrutura do Projeto

```
crm-pipeline/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── com/crm/
                ├── Main.java                   # Ponto de entrada do app
                ├── model/
                │   └── Lead.java               # Modelo de dados do lead
                ├── service/
                │   └── LeadService.java        # Lógica de negócio + persistência em JSON
                └── view/
                    ├── KanbanBoard.java         # Interface Kanban principal
                    └── LeadFormDialog.java      # Modal de adicionar/editar lead
```

---

## 🚀 Começando

### Pré-requisitos

- [Java JDK 21+](https://adoptium.net)
- [Apache Maven 3.9+](https://maven.apache.org/download.cgi)

### Instalação

```bash
# Clone o repositório
git clone https://github.com/your-username/CRM-Pipeline-Flow.git

# Navegue até o projeto
cd CRM-Pipeline-Flow/crm-pipeline

# Execute o aplicativo
mvn javafx:run
```

> Na primeira execução, o Maven fará o download das dependências do JavaFX e do Gson. Isso pode levar de 1 a 2 minutos.

---

## 📋 Campos do Lead

Cada lead armazena as seguintes informações:

| Campo | Tipo | Descrição |
|---|---|---|
| Nome | Texto | Nome completo (obrigatório) |
| E-mail | Texto | E-mail de contato |
| Telefone | Texto | Número de telefone |
| Empresa | Texto | Nome da empresa |
| Valor | Decimal | Valor do negócio em USD |
| Prioridade | Enum | ALTA / MÉDIA / BAIXA |
| Estágio | Texto | Estágio atual do pipeline |
| Anotações | Texto | Observações internas |
| Criado | Data | Definido automaticamente na criação |

---

## 💾 Armazenamento de Dados

Todos os leads são salvos automaticamente em um arquivo local `crm_data.json` na raiz do projeto. O arquivo é atualizado a cada ação (adicionar, editar, mover, excluir). Na próxima inicialização, os dados são restaurados automaticamente.

---

## ⌨️ Atalhos de Teclado

| Tecla | Ação |
|---|---|
| `Enter` | Salvar formulário (quando o formulário está aberto) |
| `Esc` | Fechar modal |
| `Duplo clique` no cartão | Abrir formulário de edição |
| `Clique direito` no cartão | Menu de contexto |

---

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie sua branch de funcionalidade: `git checkout -b feature/AmazingFeature`
3. Faça commit das suas alterações: `git commit -m 'Add AmazingFeature'`
4. Envie para a branch: `git push origin feature/AmazingFeature`
5. Abra um Pull Request

---

## 📄 Licença

Distribuído sob a Licença MIT. Consulte `LICENSE` para mais informações.

---

<p align="center">Construído com JavaFX</p>
