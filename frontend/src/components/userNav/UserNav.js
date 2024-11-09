import React, { useState, useRef, useEffect } from 'react';
import './UserNav.css';

function UserNav() {
  const [showTermMenu, setShowTermMenu] = useState(false);
  const [currentTerm, setCurrentTerm] = useState('2024 Fall');
  const menuRef = useRef(null);

  useEffect(() => {
    function handleClickOutside(event) {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        setShowTermMenu(false);
      }
    }

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const handleTermChange = (term) => {
    setCurrentTerm(term);
    setShowTermMenu(false);
  };

  const handleLoginClick = () => {
    console.log('Login button clicked');
  };

  return (
    <div className="user-nav">
      <button
        type="button"
        className="login-button"
        onClick={handleLoginClick}
      >
        login
      </button>

      <div className="term-selector" ref={menuRef}>
        <button
          type="button"
          className="nav-button term-button"
          onClick={() => setShowTermMenu(!showTermMenu)}
        >
          {currentTerm}
        </button>

        {showTermMenu && (
          <div className="term-menu">
            <button
              type="button"
              className={`menu-item ${currentTerm === '2024 Fall' ? 'active' : ''}`}
              onClick={() => handleTermChange('2024 Fall')}
            >
              2024 Fall
            </button>
            <button
              type="button"
              className={`menu-item ${currentTerm === 'Winter 2025' ? 'active' : ''}`}
              onClick={() => handleTermChange('Winter 2025')}
            >
              Winter 2025
            </button>
            <div className="menu-divider" />
            <button
              type="button"
              className="menu-item"
              onClick={() => console.log('Start Over clicked')}
            >
              Start Over
            </button>
            <div className="menu-divider" />
            <button
              type="button"
              className="menu-item sign-out"
              onClick={() => console.log('Sign Out clicked')}
            >
              SIGN OUT
            </button>
          </div>
        )}
      </div>

      <button
        type="button"
        className="nav-button menu-button"
        onClick={() => setShowTermMenu(!showTermMenu)}
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          height="24px"
          viewBox="0 -960 960 960"
          width="24px"
          fill="#5f6368"
        >
          <path d="M120-240v-80h720v80H120Zm0-200v-80h720v80H120Zm0-200v-80h720v80H120Z" />
        </svg>
      </button>
    </div>
  );
}

export default UserNav;
