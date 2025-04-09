import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth0 } from '@auth0/auth0-react';
import './Header.css';
import mesLogo from './Logo_Name_White.png';

function Header() {
  const navigate = useNavigate();
  const { loginWithRedirect, logout, isAuthenticated } = useAuth0();

  return (
    <div className="custom-header">
      <div className="secondary-bar">
        <img src={mesLogo} alt="MES Logo" className="mes-logo" />
        {/* eslint-disable-next-line max-len */}
        {/* eslint-disable-next-line jsx-a11y/click-events-have-key-events,jsx-a11y/no-noninteractive-element-interactions */}
        <h1
          className="app-title"
          onClick={() => navigate('/dashboard')}
          style={{ cursor: 'pointer' }}
        >
          DegreeFlow
        </h1>

        {isAuthenticated ? (
          <button
            type="button"
            className="login-button"
            onClick={() => logout({ returnTo: window.location.origin })}
          >
            Logout
          </button>
        ) : (
          <button
            type="button"
            className="login-button"
            onClick={() => loginWithRedirect({
              authorizationParams: {
                redirect_uri: `${window.location.origin}/dashboard`,
                audience: 'https://degreeflow-backend/api',
                scope: 'read:data write:data'
              }
            })}
          >
            Login
          </button>
        )}
      </div>

      <div className="action-bar">
        <button type="button" onClick={() => navigate('/dashboard')}>Home</button>
        <button type="button" onClick={() => navigate('/FilterSelection')}>Search Filtered Courses</button>
        <button type="button" onClick={() => navigate('/generate-schedule')}>Generate Schedule</button>
        <button type="button" onClick={() => navigate('/seat-alert')}>Seat Alert</button>
        <button type="button" onClick={() => navigate('/what-if')}>What if</button>
      </div>
    </div>
  );
}

export default Header;
