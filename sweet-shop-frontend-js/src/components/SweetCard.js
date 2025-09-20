import React from 'react';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import './SweetCard.css';

const SweetCard = ({ sweet, onEdit, onDelete, onRestock, onAdd }) => {
  const { addToCart } = useCart();
  const { isAuthenticated, user } = useAuth();

  const isAdmin = user?.role === 'ADMIN';

  const handlePurchase = () => {
    if (!isAuthenticated) {
      alert('Please login to add items to cart');
      return;
    }
    
    if (sweet.quantity <= 0) {
      alert('Sorry, this sweet is out of stock!');
      return;
    }
    
    addToCart(sweet, 1);
    // alert(`${sweet.name} added to cart!`);
  };

  return (
    <div className="sweet-card">
      {/* <div className="sweet-image">
        <div className="image-placeholder">
          <span>{sweet.name.charAt(0).toUpperCase()}</span>
        </div>
      </div> */}
      
      <div className="sweet-details">
        <h3 className="sweet-name">{sweet.name}</h3>
        <p className="sweet-category">{sweet.category.name}</p>
        <div className="sweet-price">₹{sweet.price.toFixed(2)}</div>
        <div className="sweet-quantity">
          Stock: {sweet.quantity} units
          {sweet.quantity < 10 && sweet.quantity > 0 && (
            <span className="low-stock"> (Low Stock!)</span>
          )}
          {sweet.quantity === 0 && (
            <span className="out-of-stock"> (Out of Stock!)</span>
          )}
        </div>
        
        <div className="card-actions">
          <button 
            className="purchase-btn"
            onClick={handlePurchase}
            disabled={sweet.quantity === 0}
          >
            {sweet.quantity === 0 ? 'Out of Stock' : 'Add to Cart'}
          </button>
          
          {isAuthenticated && (
            <div className="user-actions">
              <button 
                className="edit-btn"
                onClick={() => onEdit && onEdit(sweet)}
                title="Edit Sweet"
              >
                Edit
              </button>
              {/* <button 
                className="add-btn"
                onClick={() => onAdd && onAdd()}
                title="Add New Sweet"
              >
                ➕ Add New
              </button> */}
            </div>
          )}
          
          {isAdmin && (
            <div className="admin-actions">
              <button 
                className="restock-btn"
                onClick={() => onRestock && onRestock(sweet)}
                title="Restock Sweet"
              >
                Restock
              </button>
              <button 
                className="delete-btn"
                onClick={() => onDelete && onDelete(sweet)}
                title="Delete Sweet"
              >
                Delete
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default SweetCard;