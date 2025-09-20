# Sweet Shop Management System

A full-stack web application for managing a sweet shop with features for customers and administrators. Built with Spring Boot backend and React frontend.

![Sweet Shop Banner](https://via.placeholder.com/800x200/FF6B6B/FFFFFF?text=Sweet+Shop+Management+System)

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [API Endpoints](#api-endpoints)
- [Screenshots](#screenshots)
- [My AI Usage](#my-ai-usage)

## 🎯 Overview

The Sweet Shop Management System is a modern web application that allows to browse and purchase sweets while providing administrators with powerful inventory management tools. The system features secure authentication, real-time inventory tracking, and a responsive user interface.

## ✨ Features

- **User Registration & Authentication** -Secure account creation and login
- **Sweet Catalog Browsing** - View all available sweets with details
- **Advanced Search & Filtering** - Find sweets by name, category, and price range
- **Shopping Cart Management** - Add items, modify quantities, and checkout
- **Real-time Stock Updates** - See current availability and low stock warnings
- **Responsive Design** - Works seamlessly on desktop, tablet, and mobile
- **Inventory Management** - Add, edit, and delete sweet products
- **Stock Restocking** - Easily update inventory quantities
- **User Management** - View and manage customer accounts
- **Sales Analytics** - Track purchases and inventory movements
- **Admin Dashboard** - Comprehensive overview of shop operations
- **JWT Authentication** - Secure token-based authentication
- **Role-based Access Control** - Separate permissions for customers and admins
- **CORS Configuration** - Secure cross-origin resource sharing
- **Password Encryption** - BCrypt hashing for secure password storage

## 🛠 Technology Stack

### Backend
- **Java 17** - Core programming language
- **Spring Boot 3.5.5** - Main framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **MySQL 8** - Primary database
- **JWT (JSON Web Tokens)** - Stateless authentication
- **Maven** - Dependency management
- **Lombok** - Boilerplate code reduction
- **JUnit 5** - Testing framework

### Frontend
- **React 18** - User interface library
- **JavaScript (ES6+)** - Programming language
- **React Router DOM** - Client-side routing
- **Axios** - HTTP client for API calls
- **CSS3 with Flexbox/Grid** - Styling and layout
- **Context API** - State management
- **Local Storage** - Client-side data persistence

### Development Tools
- **VS Code** - Primary IDE
- **IntelliJ IDEA** 
- **Git** - Version control
- **GitHub** - Repository hosting
- **Postman** - API testing

## 📁 Project Structure

```
Sweet-Shop-Management-System/
├── Sweet-Shop-Management-System-API/          # Backend Spring Boot Application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/SweetShopManagementSystem/
│   │   │   │   ├── controller/                # REST Controllers
│   │   │   │   ├── service/                   # Business Logic Services
│   │   │   │   ├── repository/                # Data Access Layer
│   │   │   │   ├── model/                     # Entity Classes
│   │   │   │   ├── dto/                       # Data Transfer Objects
│   │   │   │   ├── security_configuration/    # Security & JWT Config
│   │   │   │   └── exception/                 # Custom Exceptions
│   │   │   └── resources/
│   │   │       └── application.properties     # Configuration
│   │   └── test/                              # Unit & Integration Tests
│   ├── target/                                # Compiled Classes
│   ├── pom.xml                                # Maven Dependencies
│   └── .gitignore
│
├── sweet-shop-frontend-js/                    # Frontend React Application
│   ├── public/
│   │   ├── index.html                         # Main HTML Template
│   │   └── manifest.json                      # PWA Manifest
│   ├── src/
│   │   ├── components/                        # Reusable UI Components
│   │   │   ├── LoginForm.js
│   │   │   ├── RegisterForm.js
│   │   │   ├── SweetCard.js                   # Product Display
│   │   │   ├── SweetForm.js                   # Add/Edit Products
│   │   │   ├── Cart.js                        # Shopping Cart
│   │   │   └── RestockModal.js                # Admin Inventory
│   │   ├── pages/                             # Page Components
│   │   │   ├── Home.js                        # Landing Page
│   │   │   └── SweetsPage.js                  # Main Shop Interface
│   │   ├── context/                           # State Management
│   │   │   ├── AuthContext.js                 # User Authentication
│   │   │   └── CartContext.js                 # Shopping Cart State
│   │   ├── services/
│   │   │   └── api.js                         # API Integration
│   │   └── *.css                              # Component Styles
│   ├── package.json                           # Dependencies
│   ├── .env                                   # Environment Variables
│   └── .gitignore
│
├── .gitignore                                 # Global Git Ignore
└── README.md                                  # This file
```

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- **Java Development Kit (JDK) 17+**
- **Node.js 14+ and npm**
- **MySQL 8.0+**
- **Git**
- **Maven 3.6+** (or use included Maven wrapper)

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/rahulkrchaudhary/Sweet-Shop-Management-System.git
cd Sweet-Shop-Management-System
```

### 2. Database Setup

1. **Install MySQL** and create a new database:
```sql
CREATE DATABASE database_name;
```

2. **Create a MySQL user** (optional but recommended):
```sql
CREATE USER 'sweetshop'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON database_name.* TO 'sweetshop'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Backend Setup

1. **Navigate to the backend directory:**
```bash
cd Sweet-Shop-Management-System-API
```

2. **Create environment configuration:**
Create a `.env` file in the backend root directory:
```properties
DATASOURCE_URL=database_url
DATASOURCE_USER=database_user
DATASOURCE_PASSWORD=your_password
FRONTED_URL=http://localhost:3000
```

3. **Build and run the backend:**
```bash
# Using Maven wrapper (recommended)
./mvnw clean install
./mvnw spring-boot:run

# Or using installed Maven
mvn clean install
mvn spring-boot:run
```

4. **Verify backend is running:**
- Open http://localhost:9090
- The backend should be accessible and create database tables automatically

### 4. Frontend Setup

1. **Navigate to the frontend directory:**
```bash
cd ../sweet-shop-frontend-js
```

2. **Create environment configuration:**
Create a `.env` file in the frontend root directory:
```properties
REACT_APP_BACKEND_API_URL=http://localhost:9090/api
```

3. **Install dependencies and start the frontend:**
```bash
npm install
npm start
```

4. **Access the application:**
- Open http://localhost:3000 in your browser
- The React application should load and connect to the backend

### 5. Initial Data Setup

1. **Create an admin user** by registering through the frontend
2. **Manually update the user role** in the database:
```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'your-admin-email@example.com';
```

3. **Login as admin** and start adding sweet products to the inventory

## 📡 API Endpoints

### Authentication Endpoints
```
POST /api/auth/register    # User registration
POST /api/auth/login       # User login
```

### Sweet Management Endpoints
```
GET    /api/sweets                    # Get all sweets
GET    /api/sweets/search             # Search sweets with filters
POST   /api/sweets                    # Add new sweet (Admin only)
PUT    /api/sweets/{id}               # Update sweet (Admin only)
DELETE /api/sweets/{id}               # Delete sweet (Admin only)
POST   /api/sweets/{id}/purchase      # Purchase sweet (decreases quantity)
POST   /api/sweets/{id}/restock       # Restock sweet (Admin only)
```

### Request/Response Examples

**User Registration:**
```json
POST /api/auth/register
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```

**Add Sweet (Admin):**
```json
POST /api/sweets
{
  "name": "Chocolate Barfi",
  "category": "Traditional",
  "price": 250.0,
  "quantity": 100
}
```

**Purchase Sweet:**
```json
POST /api/sweets/1/purchase
{
  "quantity": 2
}
```

## 📸 Screenshots

### Home Page
![Home Page]
<img width="1920" height="1046" alt="Screenshot (323)" src="https://github.com/user-attachments/assets/ff8b76c9-948f-4927-883d-a6882635a942" />

*The landing page where users can login or register for new accounts*

### Sweet Catalog
![Sweet Catalog]
<img width="1920" height="1060" alt="Screenshot (325)" src="https://github.com/user-attachments/assets/3843ad0b-4b39-472c-9cb1-a9625ba8afa6" />

*Main shopping interface with search, filters, and product cards*

### Shopping Cart
![Shopping Cart]
<img width="1920" height="1044" alt="Screenshot (326)" src="https://github.com/user-attachments/assets/d1854bea-329d-4da6-81be-fb12849def6b" />

*Shopping cart with quantity management and checkout functionality*

### Admin Dashboard
![Admin Dashboard]
<img width="1920" height="1060" alt="Screenshot (327)" src="https://github.com/user-attachments/assets/96304c0a-cf9d-42a4-be32-8c28c776ab25" />

*Administrative interface for managing products and inventory*

### Vodeo Link
[Project Demo](https://drive.google.com/file/d/167vqyRTu-d18gvKlGVViSQqcCWFI9OBu/view?usp=sharing)

## 🤖 My AI Usage

Throughout the development, I extensively utilized various AI tools to enhance productivity, solve problems, and improve code quality. Here's a detailed breakdown of how AI impacted my development workflow:

### AI Tools Used

1. **ChatGPT (OpenAI)** - Primary AI assistant for planning and problem-solving
2. **GitHub Copilot** - Intelligent code completion and generation
3. **Claude (Anthropic)** - Code review and architectural decisions

### How I Used AI

#### 🎯 **Project Planning & Analysis (ChatGPT)**
- **Requirement Understanding**: I used ChatGPT to break down and analyze the project requirements, ensuring I understood all functional and non-functional needs
- **Feature Listing**: Created comprehensive feature lists for both customer and admin functionalities
- **API Design**: Collaborated with ChatGPT to design RESTful API endpoints with proper HTTP methods and status codes
- **Database Design**: Discussed entity relationships and created the optimal database schema for the sweet shop domain

#### 🏗 **System Architecture (ChatGPT)**
- **Low-Level Design**: Used ChatGPT to create detailed component diagrams and class structures
- **Security Architecture**: Designed JWT-based authentication and authorization flows
- **Integration Patterns**: Established best practices for frontend-backend communication

#### 🧪 **Testing Strategy (ChatGPT)**
- **Test Planning**: Generated comprehensive test cases for all major functionalities
- **Unit Test Structure**: Created initial boilerplate code for JUnit test classes
- **Integration Testing**: Designed test scenarios for API endpoints and user workflows

#### 💻 **Frontend Development (GitHub Copilot)**
- **React Application Structure**: Copilot assisted in creating the initial project structure and component hierarchy
- **Component Development**: Generated boilerplate code for React components including:
  - `SweetCard.js` - Product display components with interactive features
  - `SweetForm.js` - Forms for adding and editing products
  - `Cart.js` - Shopping cart with complex state management
  - `RestockModal.js` - Admin inventory management interface etc.
- **CSS Styling**: Copilot helped create responsive CSS with modern design patterns
- **State Management**: Assisted in implementing Context API for authentication and cart state
- **API Integration**: Generated Axios service functions for backend communication

#### 🐛 **Debugging & Problem Solving (ChatGPT & Copilot)**
- **Error Resolution**: Both tools helped identify and fix compilation errors, runtime exceptions, and logical bugs
- **Code Optimization**: Suggested performance improvements and cleaner code patterns
- **Best Practices**: Ensured adherence to Spring Boot and React best practices

#### **Used Copilot to create this README.md file. I then customized and refined it to better explain the project’s structure, features, and usage.**
 

### Impact on Development Workflow

#### ⚡ **Productivity Gains**
- **Speed**: AI tools reduced development time by approximately 40-50%
- **Code Quality**: Consistent coding standards and fewer bugs through AI suggestions
- **Learning**: Discovered new libraries, patterns, and best practices through AI recommendations

#### 🎓 **Knowledge Enhancement**
- **Spring Boot Mastery**: Learned advanced Spring Security configurations and JWT implementation
- **React Patterns**: Improved understanding of modern React hooks and context patterns
- **API Design**: Better grasp of RESTful principles and HTTP status code usage

#### 🤝 **Collaboration Benefits**
- **Code Reviews**: AI tools acted as an additional pair of eyes, catching potential issues
- **Documentation**: Helped generate comprehensive comments and documentation
- **Testing**: Created thorough test suites that might have been overlooked

### Reflection on AI Impact

The integration of AI tools into my development workflow was transformative. Rather than replacing human creativity and problem-solving, AI served as an intelligent pair programming partner that:

1. **Accelerated Initial Development**: Rapid prototyping and boilerplate generation
2. **Enhanced Code Quality**: Consistent patterns and fewer syntax errors
3. **Improved Learning**: Exposure to new techniques and best practices
4. **Reduced Mental Load**: Handled repetitive tasks, allowing focus on architecture and business logic
5. **Better Documentation**: AI helped maintain comprehensive documentation throughout development

The key to successful AI usage was maintaining a balance - using AI for acceleration and learning while ensuring I understood every piece of generated code and made thoughtful architectural decisions. This approach resulted in a robust, well-tested, and maintainable application that demonstrates both technical proficiency and effective AI collaboration.
