import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './Header.css';
import mesLogo from './Logo_Name_White.png';

function Header() {
  const navigate = useNavigate();
  const location = useLocation();
  const currentPath = location.pathname;
  console.log('Current path:', currentPath);
  return (
    <div className="custom-header">
      <div className="secondary-bar">
        <img src={mesLogo} alt="MES Logo" className="mes-logo" />
        <h1 className="app-title">DegreeFlow</h1>
        <button
          type="button"
          className="login-button"
          onClick={() => navigate('/login')}
        >
          Login
        </button>
      </div>

      <div className="action-bar">
        {/* HOME Button */}
        <button
          type="button"
          className={currentPath === '/' ? 'active' : ''}
          onClick={() => navigate('/')}
        >
          Home
        </button>
        <button
          type="button"
          className={currentPath === '/upload-transcript' ? 'active' : ''}
          onClick={() => navigate('/upload-transcript')}
        >
          Upload Transcript
        </button>

        {/* Existing menu buttons */}
        <button
          type="button"
          className={currentPath === '/FilterSelection' ? 'active' : ''}
          onClick={() => navigate('/FilterSelection')}
        >
          Search Filtered Courses
        </button>
        <button
          type="button"
          className={currentPath === '/' ? 'active' : ''}
          onClick={() => navigate('/')}
        >
          Generate New Schedules
        </button>
        <button
          type="button"
          className={currentPath === '/seat-alert' ? 'active' : ''}
          onClick={() => navigate('/seat-alert')}
        >
          Seat Alert
        </button>

      </div>
    </div>
  );
}

export default Header;
