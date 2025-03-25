import React from 'react';
import './Header.css';
import mesLogo from './Logo_Name_White.png';

function MinimalHeader() {
  return (
    <div className="custom-header">
      <div className="secondary-bar">
        <img src={mesLogo} alt="MES Logo" className="mes-logo" />
        <h1 className="app-title">DegreeFlow</h1>
        <button type="button" className="login-button">
          Login
        </button>
      </div>
    </div>
  );
}

export default MinimalHeader;
