import React, { useState } from 'react';
import './RestockModal.css';

const RestockModal = ({ sweet, onRestock, onCancel, isLoading }) => {
  const [additionalQuantity, setAdditionalQuantity] = useState(10);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (additionalQuantity <= 0) {
      alert('Additional quantity must be greater than 0');
      return;
    }
    onRestock(sweet.id, additionalQuantity);
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
            <label>Additional Stock to Add:</label>
            <input
              type="number"
              value={additionalQuantity}
              onChange={(e) => setAdditionalQuantity(parseInt(e.target.value) || 0)}
              required
              disabled={isLoading}
              min="1"
              placeholder="Enter quantity to add"
            />
            <small>New total will be: {sweet.quantity + additionalQuantity} units</small>
          </div>
          
          <div className="quantity-helpers">
            <button 
              type="button" 
              onClick={() => setAdditionalQuantity(10)}
              disabled={isLoading}
              className="helper-btn"
            >
              Add 10
            </button>
            <button 
              type="button" 
              onClick={() => setAdditionalQuantity(50)}
              disabled={isLoading}
              className="helper-btn"
            >
              Add 50
            </button>
            <button 
              type="button" 
              onClick={() => setAdditionalQuantity(100)}
              disabled={isLoading}
              className="helper-btn"
            >
              Add 100
            </button>
          </div>
          
          <div className="form-actions">
            <button type="button" onClick={onCancel} className="cancel-btn" disabled={isLoading}>
              Cancel
            </button>
            <button type="submit" className="restock-btn" disabled={isLoading}>
              {isLoading ? 'Restocking...' : `Add ${additionalQuantity} Units`}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default RestockModal;