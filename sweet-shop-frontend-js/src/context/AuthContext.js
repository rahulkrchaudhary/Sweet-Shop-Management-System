import React, { createContext, useContext, useState, useEffect } from 'react';
import { authService } from '../services/api';

const AuthContext = createContext(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // Check if user is already logged in
    const savedToken = localStorage.getItem('token');
    const savedUser = localStorage.getItem('user');
    
    if (savedToken && savedUser) {
      setToken(savedToken);
      setUser(JSON.parse(savedUser));
    }
    setIsLoading(false);
  }, []);

  const login = async (loginRequest) => {
    try {
      const response = await authService.login(loginRequest);
      const userData = {
        id: 0, // Backend doesn't return user details in AuthResponse
        name: '', // You might want to fetch user details separately
        email: loginRequest.email,
        role: response.role
      };
      
      localStorage.setItem('token', response.jwt);
      localStorage.setItem('user', JSON.stringify(userData));
      
      setToken(response.jwt);
      setUser(userData);
    } catch (error) {
      throw error;
    }
  };

  const register = async (registerRequest) => {
    try {
      const response = await authService.register(registerRequest);
      const userData = {
        id: 0,
        name: registerRequest.name,
        email: registerRequest.email,
        role: response.role
      };
      
      localStorage.setItem('token', response.jwt);
      localStorage.setItem('user', JSON.stringify(userData));
      
      setToken(response.jwt);
      setUser(userData);
    } catch (error) {
      throw error;
    }
  };

  const logout = () => {
    authService.logout();
    setToken(null);
    setUser(null);
  };

  const value = {
    user,
    token,
    login,
    register,
    logout,
    isAuthenticated: !!token,
    isLoading
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};