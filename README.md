# 🎯 CRM Pipeline

A professional **Sales Pipeline Management** desktop application built with **JavaFX**, featuring a fully interactive Kanban board with drag-and-drop, real-time statistics, persistent data storage, and a sleek dark UI.

---

## 📸 Preview

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

## ✨ Features

- **Kanban Board** — Visual pipeline with 5 customizable stages
- **Drag & Drop** — Move deals between columns effortlessly
- **Real-time Stats** — Total leads, pipeline value, closed value, conversion rate
- **Add / Edit Leads** — Full modal form with all contact details
- **Right-click Menu** — Edit, move, or delete leads instantly
- **Search & Filter** — Filter cards by name, company or email in real time
- **Priority System** — HIGH / MEDIUM / LOW with color indicators
- **Data Persistence** — Auto-saves to local JSON file on every change
- **Dark UI** — Clean, modern dark theme with color-coded columns

---

## 🗂️ Pipeline Stages

| Stage | Color | Description |
|---|---|---|
| **New Lead** | Purple | Prospect identified |
| **Contacted** | Blue | First contact made |
| **Proposal Sent** | Orange | Proposal delivered |
| **Closed** | Green | Deal won |
| **Lost** | Red | Deal lost |

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 (LTS) | Core language |
| JavaFX | 21 | UI framework |
| Gson | 2.10.1 | JSON persistence |
| Maven | 3.9+ | Build & dependency management |

---

## 📁 Project Structure

```
crm-pipeline/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── com/crm/
                ├── Main.java                   # App entry point
                ├── model/
                │   └── Lead.java               # Lead data model
                ├── service/
                │   └── LeadService.java        # Business logic + JSON persistence
                └── view/
                    ├── KanbanBoard.java         # Main Kanban UI
                    └── LeadFormDialog.java      # Add/Edit lead modal
```

---

## 🚀 Getting Started

### Prerequisites

- [Java JDK 21+](https://adoptium.net)
- [Apache Maven 3.9+](https://maven.apache.org/download.cgi)

### Installation

```bash
# Clone the repository
git clone https://github.com/your-username/CRM-Pipeline-Flow.git

# Navigate into the project
cd CRM-Pipeline-Flow/crm-pipeline

# Run the application
mvn javafx:run
```

> On the first run, Maven will download JavaFX and Gson dependencies. This may take 1-2 minutes.

---

## 📋 Lead Fields

Each lead stores the following information:

| Field | Type | Description |
|---|---|---|
| Name | String | Full name (required) |
| Email | String | Contact email |
| Phone | String | Phone number |
| Company | String | Company name |
| Value | Double | Deal value in USD |
| Priority | Enum | HIGH / MEDIUM / LOW |
| Stage | String | Current pipeline stage |
| Notes | String | Internal observations |
| Created | Date | Auto-set on creation |

---

## 💾 Data Storage

All leads are automatically saved to a local `crm_data.json` file in the project root. The file is updated on every action (add, edit, move, delete). On next launch, data is restored automatically.

---

## ⌨️ Keyboard Shortcuts

| Key | Action |
|---|---|
| `Enter` | Save form (when form is open) |
| `Esc` | Close modal |
| `Double-click` on card | Open edit form |
| `Right-click` on card | Context menu |

---

## 🤝 Contributing

1. Fork the project
2. Create your feature branch: `git checkout -b feature/AmazingFeature`
3. Commit your changes: `git commit -m 'Add AmazingFeature'`
4. Push to the branch: `git push origin feature/AmazingFeature`
5. Open a Pull Request

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

---

<p align="center">Built with JavaFX</p>