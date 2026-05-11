# Majikku Stores Management System

![Python](https://img.shields.io/badge/Python-3.9+-3776ab?style=flat-square&logo=python&logoColor=white)
![Flask](https://img.shields.io/badge/Flask-Framework-000000?style=flat-square&logo=flask&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-4479a1?style=flat-square&logo=mysql&logoColor=white)

A full-stack, highly relational web application designed to centralize and manage the daily operations of a large-scale retail environment. Majikku Stores unifies inventory control, transaction processing, human resources administration, and role-based security into a single, intuitive dashboard.

## ✨ Key Features
* **Role-Based Access Control (RBAC):** Secure routing ensures that sensitive modules (like HR administration) are restricted exclusively to authorized personnel.
* **Cryptographic Security:** Employee authentication utilizes SHA-512 hashing (with architectural support mapped for unique salting) to protect system access.
* **Dynamic Inventory Tracking:** Real-time tracking of product stock, pricing, and automated rendering of low-stock thresholds.
* **Administrative Dashboard:** A dedicated portal for managing the staff roster, updating job roles, and tracking permissions across different organizational tiers.
* **Dynamic Data Views:** A universal routing engine (`/view/<table>`) that dynamically queries and renders any authorized MySQL table without requiring custom HTML templates for every view.

## 🏗️ Architecture
The application follows a standard client-server architecture:
* **Backend:** Built with Python and Flask. Flask handles all HTTP routing, form processing, template rendering, and secure session management.
* **Database:** Powered by a complex MySQL relational schema containing over 25 interlinked tables. All database transactions are abstracted into a dedicated `database.py` module to keep route logic clean.
* **Frontend:** Utilizes Jinja2 templating to inject Python data directly into the HTML/CSS user interface.

## 🚀 Getting Started

### Prerequisites
* Python 3.9 or higher
* MySQL Server 8.0+

### Installation
1. **Clone the repository:**
   ```bash
   git clone [https://github.com/yourusername/majikku-stores.git](https://github.com/yourusername/majikku-stores.git)
   cd majikku-stores