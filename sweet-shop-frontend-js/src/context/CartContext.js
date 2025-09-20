import React, { createContext, useContext, useState } from 'react';

const CartContext = createContext(undefined);

export const useCart = () => {
  const context = useContext(CartContext);
  if (context === undefined) {
    throw new Error('useCart must be used within a CartProvider');
  }
  return context;
};

export const CartProvider = ({ children }) => {
  const [cartItems, setCartItems] = useState([]);
  const [isCartVisible, setIsCartVisible] = useState(false);

  const addToCart = (sweet, quantity = 1) => {
    setCartItems(prevItems => {
      const existingItem = prevItems.find(item => item.sweet.id === sweet.id);
      
      if (existingItem) {
        return prevItems.map(item =>
          item.sweet.id === sweet.id
            ? { ...item, quantity: item.quantity + quantity }
            : item
        );
      } else {
        return [...prevItems, { sweet, quantity }];
      }
    });
    
    // Show cart when item is added
    if (cartItems.length === 0) {
      setIsCartVisible(true);
    }
  };

  const removeFromCart = (sweetId) => {
    setCartItems(prevItems => {
      const newItems = prevItems.filter(item => item.sweet.id !== sweetId);
      // Hide cart if no items left
      if (newItems.length === 0) {
        setIsCartVisible(false);
      }
      return newItems;
    });
  };

  const updateQuantity = (sweetId, quantity) => {
    if (quantity <= 0) {
      removeFromCart(sweetId);
      return;
    }
    
    setCartItems(prevItems =>
      prevItems.map(item =>
        item.sweet.id === sweetId
          ? { ...item, quantity }
          : item
      )
    );
  };

  const clearCart = () => {
    setCartItems([]);
    setIsCartVisible(false);
  };

  const getTotalPrice = () => {
    return cartItems.reduce((total, item) => total + (item.sweet.price * item.quantity), 0);
  };

  const getTotalItems = () => {
    return cartItems.reduce((total, item) => total + item.quantity, 0);
  };

  const showCart = () => {
    if (cartItems.length > 0) {
      setIsCartVisible(true);
    }
  };

  const hideCart = () => {
    setIsCartVisible(false);
  };

  const value = {
    cartItems,
    addToCart,
    removeFromCart,
    updateQuantity,
    clearCart,
    getTotalPrice,
    getTotalItems,
    isCartVisible,
    showCart,
    hideCart
  };

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
};