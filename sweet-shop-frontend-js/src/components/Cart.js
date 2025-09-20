import React from 'react';
import { useCart } from '../context/CartContext';
import { sweetService } from '../services/api';
import './Cart.css';

const Cart = ({ inline = false }) => {
  const { 
    cartItems, 
    removeFromCart, 
    updateQuantity, 
    clearCart, 
    getTotalPrice, 
    isCartVisible, 
    hideCart 
  } = useCart();

  if (!isCartVisible || cartItems.length === 0) {
    return null;
  }

  const handleBuy = async () => {
    try {
      // Update quantities in backend for each cart item
      for (const item of cartItems) {
        const currentSweet = item.sweet;
        const newQuantity = currentSweet.quantity - item.quantity;
        
        if (newQuantity < 0) {
          alert(`Not enough stock for ${currentSweet.name}. Available: ${currentSweet.quantity}, Requested: ${item.quantity}`);
          return;
        }
        
        // Update sweet quantity in backend
        await sweetService.updateSweet(currentSweet.id, {
          name: currentSweet.name,
          category: currentSweet.category.name,
          price: currentSweet.price,
          quantity: newQuantity
        });
      }
      
      // Clear cart and show success message
      alert(`Order placed successfully! Total: ₹${getTotalPrice().toFixed(2)}`);
      clearCart();
      
      // Refresh page to show updated quantities
      window.location.reload();
    } catch (error) {
      console.error('Purchase failed:', error);
      alert('Purchase failed. Please try again.');
    }
  };

  const handleQuantityIncrease = (item) => {
    // Check if increasing quantity would exceed available stock
    if (item.quantity >= item.sweet.quantity) {
      alert(`Only ${item.sweet.quantity} units available in stock`);
      return;
    }
    updateQuantity(item.sweet.id, item.quantity + 1);
  };

  const handleQuantityDecrease = (item) => {
    if (item.quantity <= 1) {
      removeFromCart(item.sweet.id);
    } else {
      updateQuantity(item.sweet.id, item.quantity - 1);
    }
  };

  const containerClass = inline ? "cart-container-inline" : "cart-overlay";
  const cartClass = inline ? "cart-inline" : "cart-container";

  return (
    <div className={containerClass}>
      <div className={cartClass}>
        <div className="cart-header">
          <h2>🛒 Shopping Cart ({cartItems.length} items)</h2>
          {!inline && (
            <button className="close-cart-btn" onClick={hideCart}>
              &times;
            </button>
          )}
        </div>
        
        <div className="cart-items">
          {cartItems.map((item) => (
            <div key={item.sweet.id} className="cart-item">
              <div className="item-info">
                <h4>{item.sweet.name}</h4>
                <p className="item-category">{item.sweet.category.name}</p>
                <p className="item-price">₹{item.sweet.price.toFixed(2)} each</p>
                <p className="item-stock">Stock: {item.sweet.quantity}</p>
              </div>
              
              <div className="item-controls">
                <div className="quantity-controls">
                  <button 
                    onClick={() => handleQuantityDecrease(item)}
                    className="quantity-btn"
                  >
                    -
                  </button>
                  <span className="quantity">{item.quantity}</span>
                  <button 
                    onClick={() => handleQuantityIncrease(item)}
                    className="quantity-btn"
                    disabled={item.quantity >= item.sweet.quantity}
                  >
                    +
                  </button>
                </div>
                
                <div className="item-total">
                  ₹{(item.sweet.price * item.quantity).toFixed(2)}
                </div>
                
                <button 
                  onClick={() => removeFromCart(item.sweet.id)}
                  className="remove-btn"
                >
                  Remove
                </button>
              </div>
            </div>
          ))}
        </div>
        
        <div className="cart-footer">
          <div className="cart-total">
            <strong>Total: ₹{getTotalPrice().toFixed(2)}</strong>
          </div>
          
          <div className="cart-actions">
            <button onClick={clearCart} className="clear-cart-btn">
              Clear Cart
            </button>
            <button onClick={handleBuy} className="buy-btn">
              Make Bill
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Cart;