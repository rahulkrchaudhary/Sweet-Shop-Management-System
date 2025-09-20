# Sweet Shop Frontend

A React TypeScript frontend application for the Sweet Shop Management System.

## Features

1. **Authentication System**
   - User login and registration
   - JWT token-based authentication
   - Protected routes

2. **Home Page**
   - Welcome page with login/registration buttons
   - User-friendly interface

3. **Sweet Cards**
   - Display sweet details (name, category, price, stock)
   - Add to cart functionality
   - Beautiful placeholder images

4. **Sweets Listing Page**
   - Browse all available sweets
   - Category filter on the left sidebar
   - Search functionality
   - Price range filter (min/max price)

5. **Shopping Cart**
   - Shows selected sweets at the top of the page
   - Quantity management
   - Total price calculation
   - Buy functionality
   - Auto-hide when empty

6. **Responsive Design**
   - Mobile-friendly interface
   - Clean and modern UI

## Getting Started

### Prerequisites

- Node.js (v14 or higher)
- npm or yarn
- Backend API running on http://localhost:9090

### Installation

1. Install dependencies:
   ```bash
   npm install
   ```

2. Start the development server:
   ```bash
   npm start
   ```

3. Open http://localhost:3000 in your browser

### Backend Integration

The frontend is configured to work with your Spring Boot backend running on:
- Base URL: http://localhost:9090
- Authentication: /api/auth/login, /api/auth/register
- Sweets: /api/sweets, /api/sweets/search

### Project Structure

```
src/
├── components/          # Reusable UI components
│   ├── Auth.css
│   ├── Cart.css
│   ├── Cart.tsx
│   ├── LoginForm.tsx
│   ├── RegisterForm.tsx
│   ├── SweetCard.css
│   └── SweetCard.tsx
├── context/            # React context providers
│   ├── AuthContext.tsx
│   └── CartContext.tsx
├── pages/              # Page components
│   ├── Home.css
│   ├── Home.tsx
│   ├── SweetsPage.css
│   └── SweetsPage.tsx
├── services/           # API services
│   └── api.ts
├── types/              # TypeScript type definitions
│   └── index.ts
├── App.css
├── App.tsx
├── index.css
└── index.tsx
```

## Usage

1. **Home Page**: Start here to login or register
2. **Authentication**: Create an account or login with existing credentials
3. **Browse Sweets**: Navigate to the sweets page to view all available items
4. **Filter & Search**: Use the sidebar filters and search bar to find specific sweets
5. **Add to Cart**: Click "Add to Cart" on any sweet card
6. **Manage Cart**: View and modify your cart at the top of the page
7. **Purchase**: Click "Buy Now" to complete your order

## API Integration

The app integrates with your backend API endpoints:

- **POST /api/auth/register** - User registration
- **POST /api/auth/login** - User login
- **GET /api/sweets** - Get all sweets
- **GET /api/sweets/search** - Search sweets with filters

## Technologies Used

- React 18
- TypeScript
- React Router DOM
- Axios for API calls
- CSS3 with Flexbox/Grid
- Context API for state management

## Available Scripts

- `npm start` - Start development server
- `npm run build` - Build for production
- `npm test` - Run tests
- `npm run eject` - Eject from Create React App

## Notes

- The app uses localStorage to persist authentication tokens
- Cart state is managed in memory and resets on page refresh
- Placeholder images are used since the backend doesn't include image URLs
- The app is configured to proxy API requests to localhost:9090