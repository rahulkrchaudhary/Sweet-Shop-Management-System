import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import { sweetService } from '../services/api';
import SweetCard from '../components/SweetCard';
import SweetForm from '../components/SweetForm';
import RestockModal from '../components/RestockModal';
import Cart from '../components/Cart';
import './SweetsPage.css';

const SweetsPage = () => {
  const [sweets, setSweets] = useState([]);
  const [filteredSweets, setFilteredSweets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('');
  const [priceRange, setPriceRange] = useState({ min: '', max: '' });
  const [categories, setCategories] = useState([]);
  
  // Form states
  const [showSweetForm, setShowSweetForm] = useState(false);
  const [editingSweet, setEditingSweet] = useState(undefined);
  const [showRestockModal, setShowRestockModal] = useState(false);
  const [restockingSweet, setRestockingSweet] = useState(undefined);
  const [formLoading, setFormLoading] = useState(false);
  
  const { user, logout, isAuthenticated } = useAuth();
  const { getTotalItems } = useCart();
  const navigate = useNavigate();

  const isAdmin = user?.role === 'ADMIN';

  // Load sweets on component mount
  useEffect(() => {
    loadSweets();
  }, []);

  // Extract unique categories when sweets change
  useEffect(() => {
    const uniqueCategories = Array.from(new Set(sweets.map(sweet => sweet.category.name)));
    setCategories(uniqueCategories);
  }, [sweets]);

  // Apply filters when any filter changes
  useEffect(() => {
    applyFilters();
  }, [sweets, searchQuery, selectedCategory, priceRange]);

  const loadSweets = async () => {
    try {
      setLoading(true);
      const data = await sweetService.getAllSweets();
      setSweets(data);
      setFilteredSweets(data);
    } catch (err) {
      setError('Failed to load sweets');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const applyFilters = () => {
    let filtered = [...sweets];

    // Search filter
    if (searchQuery.trim()) {
      filtered = filtered.filter(sweet =>
        sweet.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        sweet.category.name.toLowerCase().includes(searchQuery.toLowerCase())
      );
    }

    // Category filter
    if (selectedCategory) {
      filtered = filtered.filter(sweet => sweet.category.name === selectedCategory);
    }

    // Price range filter
    if (priceRange.min !== '') {
      filtered = filtered.filter(sweet => sweet.price >= parseFloat(priceRange.min));
    }
    if (priceRange.max !== '') {
      filtered = filtered.filter(sweet => sweet.price <= parseFloat(priceRange.max));
    }

    setFilteredSweets(filtered);
  };

  const handleSearch = async () => {
    try {
      setLoading(true);
      const filters = {
        name: searchQuery.trim() || undefined,
        category: selectedCategory || undefined,
        minPrice: priceRange.min ? parseFloat(priceRange.min) : undefined,
        maxPrice: priceRange.max ? parseFloat(priceRange.max) : undefined,
      };
      
      const data = await sweetService.searchSweets(filters);
      setFilteredSweets(data);
    } catch (err) {
      setError('Search failed');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const clearFilters = () => {
    setSearchQuery('');
    setSelectedCategory('');
    setPriceRange({ min: '', max: '' });
    setFilteredSweets(sweets);
  };

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  // Sweet management functions
  const handleAddSweet = () => {
    setEditingSweet(undefined);
    setShowSweetForm(true);
  };

  const handleEditSweet = (sweet) => {
    setEditingSweet(sweet);
    setShowSweetForm(true);
  };

  const handleDeleteSweet = async (sweet) => {
    if (!isAdmin) {
      alert('Only admins can delete sweets');
      return;
    }

    if (window.confirm(`Are you sure you want to delete "${sweet.name}"?`)) {
      try {
        await sweetService.deleteSweet(sweet.id);
        setSweets(prev => prev.filter(s => s.id !== sweet.id));
        alert('Sweet deleted successfully!');
      } catch (err) {
        if (err.response?.status === 403) {
          alert('You do not have permission to delete sweets. Admin access required.');
        } else {
          alert('Failed to delete sweet: ' + (err.response?.data?.message || err.message));
        }
      }
    }
  };

  const handleRestockSweet = (sweet) => {
    if (!isAdmin) {
      alert('Only admins can restock sweets');
      return;
    }
    setRestockingSweet(sweet);
    setShowRestockModal(true);
  };

  const handleSweetFormSubmit = async (formData) => {
    setFormLoading(true);
    try {
      let updatedSweet;
      
      if (editingSweet) {
        // Update existing sweet
        updatedSweet = await sweetService.updateSweet(editingSweet.id, formData);
        setSweets(prev => prev.map(s => s.id === editingSweet.id ? updatedSweet : s));
        alert('Sweet updated successfully!');
      } else {
        // Add new sweet
        updatedSweet = await sweetService.addSweet(formData);
        setSweets(prev => [...prev, updatedSweet]);
        alert('Sweet added successfully!');
      }
      
      setShowSweetForm(false);
      setEditingSweet(undefined);
    } catch (err) {
      alert('Failed to save sweet: ' + (err.response?.data?.message || err.message));
    } finally {
      setFormLoading(false);
    }
  };

  const handleRestockSubmit = async (sweetId, quantity) => {
    setFormLoading(true);
    try {
      const updatedSweet = await sweetService.restockSweet(sweetId, quantity);
      setSweets(prev => prev.map(s => s.id === sweetId ? updatedSweet : s));
      setShowRestockModal(false);
      setRestockingSweet(undefined);
      alert('Sweet restocked successfully!');
    } catch (err) {
      alert('Failed to restock sweet: ' + (err.response?.data?.message || err.message));
    } finally {
      setFormLoading(false);
    }
  };

  const handleFormCancel = () => {
    setShowSweetForm(false);
    setEditingSweet(undefined);
    setShowRestockModal(false);
    setRestockingSweet(undefined);
  };

  if (loading) {
    return (
      <div className="sweets-page">
        <div className="loading-container">
          <div className="loading-spinner"></div>
          <p>Loading delicious sweets...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="sweets-page">
        <div className="error-container">
          <p className="error-message">{error}</p>
          <button onClick={loadSweets} className="retry-btn">
            Try Again
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="sweets-page">
      {/* Header */}
      <div className="sweets-header">
        <div className="header-content">
          <h1 className="page-title">🍭 Sweet Shop</h1>
          <div className="header-actions">
            {isAuthenticated && (
              <button onClick={handleAddSweet} className="add-sweet-btn">
                + Add Sweet
              </button>
            )}
            <div className="user-info">
              <span>Welcome, {user?.name || user?.email}!</span>
              {isAdmin && <span className="admin-badge">Admin</span>}
              <button onClick={handleLogout} className="logout-btn">
                Logout
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* Cart notification */}
      {getTotalItems() > 0 && (
        <div className="cart-notification">
          <span>🛒 {getTotalItems()} items in cart</span>
        </div>
      )}

      <div className="sweets-content">
        {/* Sidebar with filters */}
        <div className="sidebar">
          <div className="filter-section">
            <h3>Categories</h3>
            <div className="category-filters">
              <label className="category-option">
                <input
                  type="radio"
                  name="category"
                  value=""
                  checked={selectedCategory === ''}
                  onChange={(e) => setSelectedCategory(e.target.value)}
                />
                All Categories
              </label>
              {categories.map(category => (
                <label key={category} className="category-option">
                  <input
                    type="radio"
                    name="category"
                    value={category}
                    checked={selectedCategory === category}
                    onChange={(e) => setSelectedCategory(e.target.value)}
                  />
                  {category}
                </label>
              ))}
            </div>
          </div>

          <div className="filter-section">
            <h3>Price Range</h3>
            <div className="price-filters">
              <input
                type="number"
                placeholder="Min Price"
                value={priceRange.min}
                onChange={(e) => setPriceRange(prev => ({ ...prev, min: e.target.value }))}
                className="price-input"
              />
              <input
                type="number"
                placeholder="Max Price"
                value={priceRange.max}
                onChange={(e) => setPriceRange(prev => ({ ...prev, max: e.target.value }))}
                className="price-input"
              />
            </div>
          </div>

          <button onClick={clearFilters} className="clear-filters-btn">
            Clear All Filters
          </button>
        </div>

        {/* Main content */}
        <div className="main-content">
          {/* Search bar */}
          <div className="search-section">
            <div className="search-bar">
              <input
                type="text"
                placeholder="Search sweets by name or category..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="search-input"
                onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
              />
              <button onClick={handleSearch} className="search-btn">
                🔍
              </button>
            </div>
          </div>

          {/* Cart section - appears below search when items are present */}
          {getTotalItems() > 0 && (
            <div className="cart-section">
              <Cart inline={true} />
            </div>
          )}

          {/* Results */}
          <div className="results-section">
            <div className="results-header">
              <h2>Available Sweets ({filteredSweets.length})</h2>
            </div>

            {filteredSweets.length === 0 ? (
              <div className="no-results">
                <p>No sweets found matching your criteria.</p>
                <button onClick={clearFilters} className="clear-filters-btn">
                  Clear Filters
                </button>
              </div>
            ) : (
              <div className="sweets-grid">
                {filteredSweets.map(sweet => (
                  <SweetCard 
                    key={sweet.id} 
                    sweet={sweet}
                    onEdit={isAuthenticated ? handleEditSweet : undefined}
                    onAdd={isAuthenticated ? handleAddSweet : undefined}
                    onDelete={isAdmin ? handleDeleteSweet : undefined}
                    onRestock={isAdmin ? handleRestockSweet : undefined}
                    onUpdate={loadSweets}
                  />
                ))}
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Sweet Form Modal */}
      {showSweetForm && (
        <SweetForm
          sweet={editingSweet}
          onSubmit={handleSweetFormSubmit}
          onCancel={handleFormCancel}
          isLoading={formLoading}
        />
      )}

      {/* Restock Modal */}
      {showRestockModal && restockingSweet && (
        <RestockModal
          sweet={restockingSweet}
          onRestock={handleRestockSubmit}
          onCancel={handleFormCancel}
          isLoading={formLoading}
        />
      )}
    </div>
  );
};

export default SweetsPage;