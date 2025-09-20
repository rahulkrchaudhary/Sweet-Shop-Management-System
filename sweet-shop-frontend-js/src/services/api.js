import axios from 'axios';

// Create axios instance
const api = axios.create({
  baseURL: process.env.REACT_APP_BACKEND_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests if available
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Auth Services
export const authService = {
  login: async (loginRequest) => {
    const response = await api.post('/auth/login', loginRequest);
    return response.data;
  },

  register: async (registerRequest) => {
    const response = await api.post('/auth/register', registerRequest);
    return response.data;
  },

  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },

  getCurrentUser: () => {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  },

  isAuthenticated: () => {
    return !!localStorage.getItem('token');
  }
};

// Sweet Services
export const sweetService = {
  getAllSweets: async () => {
    const response = await api.get('/sweets');
    return response.data;
  },

  searchSweets: async (filters) => {
    const params = new URLSearchParams();
    
    if (filters.name) params.append('name', filters.name);
    if (filters.category) params.append('category', filters.category);
    if (filters.minPrice !== undefined) params.append('minPrice', filters.minPrice.toString());
    if (filters.maxPrice !== undefined) params.append('maxPrice', filters.maxPrice.toString());
    
    const response = await api.get(`/sweets/search?${params.toString()}`);
    return response.data;
  },

  addSweet: async (sweetData) => {
    const response = await api.post('/sweets', sweetData);
    return response.data;
  },

  updateSweet: async (id, sweetData) => {
    const response = await api.put(`/sweets/${id}`, sweetData);
    return response.data;
  },

  deleteSweet: async (id) => {
    await api.delete(`/sweets/${id}`);
  },

  restockSweet: async (id, quantity) => {
    // Get current sweet data first
    const currentSweet = await api.get(`/sweets`);
    const sweet = currentSweet.data.find((s) => s.id === id);
    
    if (!sweet) throw new Error('Sweet not found');
    
    // Update with new quantity
    const updateData = {
      name: sweet.name,
      category: sweet.category.name,
      price: sweet.price,
      quantity: quantity
    };
    
    const response = await api.put(`/sweets/${id}`, updateData);
    return response.data;
  }
};

export default api;