import React, { useState } from 'react';
import './RestockModal.css';

const RestockModal = ({ sweet, onRestock, onCancel, isLoading }) => {
  const [quantity, setQuantity] = useState(sweet.quantity);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (quantity < 0) {
      alert('Quantity cannot be negative');
      return;
    }
    onRestock(sweet.id, quantity);
  };

  return (
    <div className="restock-modal">
      <div className="restock-modal-content">
        <div className="modal-header">
          <h2>Restock Sweet</h2>
          <button className="close-btn" onClick={onCancel}>&times;</button>
        </div>
        
        <div className="sweet-info">
          <h3>{sweet.name}</h3>
          <p>Category: {sweet.category.name}</p>
          <p>Current Stock: {sweet.quantity} units</p>
          <p>Price: ₹{sweet.price.toFixed(2)}</p>
        </div>
        
        <form onSubmit={handleSubmit} className="restock-form">
          <div className="form-group">
            <label>New Stock Quantity:</label>
            <input
              type="number"
              value={quantity}
              onChange={(e) => setQuantity(parseInt(e.target.value) || 0)}
              required
              disabled={isLoading}
              min="0"
              placeholder="Enter new quantity"
            />
          </div>
          
          <div className="quantity-helpers">
            <button 
              type="button" 
              onClick={() => setQuantity(prev => prev + 10)}
              disabled={isLoading}
              className="helper-btn"
            >
              +10
            </button>
            <button 
              type="button" 
              onClick={() => setQuantity(prev => prev + 50)}
              disabled={isLoading}
              className="helper-btn"
            >
              +50
            </button>
            <button 
              type="button" 
              onClick={() => setQuantity(prev => prev + 100)}
              disabled={isLoading}
              className="helper-btn"
            >
              +100
            </button>
          </div>
          
          <div className="form-actions">
            <button type="button" onClick={onCancel} className="cancel-btn" disabled={isLoading}>
              Cancel
            </button>
            <button type="submit" className="restock-btn" disabled={isLoading}>
              {isLoading ? 'Restocking...' : 'Update Stock'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default RestockModal;