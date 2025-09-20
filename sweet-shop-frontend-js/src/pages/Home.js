import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import LoginForm from '../components/LoginForm';
import RegisterForm from '../components/RegisterForm';
import './Home.css';

const Home = () => {
  const [showLogin, setShowLogin] = useState(false);
  const [showRegister, setShowRegister] = useState(false);
  const { isAuthenticated, user, logout } = useAuth();
  const navigate = useNavigate();
  console.log(user)

  const handleSwitchToRegister = () => {
    setShowLogin(false);
    setShowRegister(true);
  };

  const handleSwitchToLogin = () => {
    setShowRegister(false);
    setShowLogin(true);
  };

  const closeModals = () => {
    setShowLogin(false);
    setShowRegister(false);
  };

  const handleBrowseSweets = () => {
    navigate('/sweets');
  };

  const handleLogout = () => {
    logout();
  };

  return (
    <div className="home-container">
      <div className="home-header">
        <div className="header-content">
          <h1 className="logo">🍭 Sweet Shop</h1>
          
          <div className="header-actions">
            {isAuthenticated ? (
              <div className="user-section">
                <span className="welcome-text">Welcome, {user?.name || user?.email}!</span>
                <button onClick={handleBrowseSweets} className="browse-btn">
                  Browse Sweets
                </button>
                <button onClick={handleLogout} className="logout-btn">
                  Logout
                </button>
              </div>
            ) : (
              <div className="auth-buttons">
                <button onClick={() => setShowLogin(true)} className="login-btn">
                  Login
                </button>
                <button onClick={() => setShowRegister(true)} className="register-btn">
                  Register
                </button>
              </div>
            )}
          </div>
        </div>
      </div>

      <div className="home-content">
        <div className="hero-section">
          <h2 className="hero-title">Welcome to Sweet Shop</h2>
          <p className="hero-description">
            Discover the finest collection of sweets. 
            From classic Indian mithai to contemporary desserts, we have 
            something special for every sweet tooth.
          </p>
          
          {isAuthenticated ? (
            <button onClick={handleBrowseSweets} className="cta-btn">
              Browse Sweets
            </button>
          ) : (
            <div className="cta-section">
              {/* <p className="cta-text">Join us today to start your sweet journey!</p> */}
              <div className="cta-buttons">
                <button onClick={() => setShowLogin(true)} className="cta-login-btn">
                  Login
                </button>
                <button onClick={() => setShowRegister(true)} className="cta-register-btn">
                  Register
                </button>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Auth Modals */}
      {showLogin && (
        <LoginForm 
          onSwitchToRegister={handleSwitchToRegister}
          onClose={closeModals}
        />
      )}
      {showRegister && (
        <RegisterForm 
          onSwitchToLogin={handleSwitchToLogin}
          onClose={closeModals}
        />
      )}
    </div>
  );
};

export default Home;