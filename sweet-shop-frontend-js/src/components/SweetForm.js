import React, { useState, useEffect } from 'react';
import './SweetForm.css';

const SweetForm = ({ sweet, onSubmit, onCancel, isLoading }) => {
  const [formData, setFormData] = useState({
    name: '',
    category: '',
    price: 0,
    quantity: 0
  });

  useEffect(() => {
    if (sweet) {
      setFormData({
        name: sweet.name,
        category: sweet.category.name,
        price: sweet.price,
        quantity: sweet.quantity
      });
    }
  }, [sweet]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === 'price' || name === 'quantity' ? parseFloat(value) || 0 : value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <div className="sweet-form-modal">
      <div className="sweet-form-content">
        <div className="form-header">
          <h2>{sweet ? 'Edit Sweet' : 'Add New Sweet'}</h2>
          <button className="close-btn" onClick={onCancel}>&times;</button>
        </div>
        
        <form onSubmit={handleSubmit} className="sweet-form">
          <div className="form-group">
            <label>Sweet Name:</label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
              disabled={isLoading}
              placeholder="Enter sweet name"
            />
          </div>
          
          <div className="form-group">
            <label>Category:</label>
            <input
              type="text"
              name="category"
              value={formData.category}
              onChange={handleChange}
              required
              disabled={isLoading}
              placeholder="Enter category"
            />
          </div>
          
          <div className="form-row">
            <div className="form-group">
              <label>Price (₹):</label>
              <input
                type="number"
                name="price"
                value={formData.price}
                onChange={handleChange}
                required
                disabled={isLoading}
                min="0"
                step="0.01"
                placeholder="0.00"
              />
            </div>
            
            <div className="form-group">
              <label>Quantity:</label>
              <input
                type="number"
                name="quantity"
                value={formData.quantity}
                onChange={handleChange}
                required
                disabled={isLoading}
                min="0"
                placeholder="0"
              />
            </div>
          </div>
          
          <div className="form-actions">
            <button type="button" onClick={onCancel} className="cancel-btn" disabled={isLoading}>
              Cancel
            </button>
            <button type="submit" className="submit-btn" disabled={isLoading}>
              {isLoading ? 'Saving...' : sweet ? 'Update Sweet' : 'Add Sweet'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default SweetForm;