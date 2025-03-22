import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Header.css';
import mesLogo from './Logo_Name_White.png';

function Header() {
  const navigate = useNavigate();

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
          onClick={() => navigate('/')}
        >
          Home
        </button>
        <button
          type="button"
          onClick={() => navigate('/upload-transcript')}
        >
          Upload Transcript
        </button>

        {/* Existing menu buttons */}
        <button
          type="button"
          onClick={() => navigate('/FilterSelection')}
        >
          Search Filtered Courses
        </button>
        <button
          type="button"
          onClick={() => navigate('/')}
        >
          Generate New Schedules
        </button>
        <button
          type="button"
          onClick={() => navigate('/seat-alert')}
        >
          Seat Alert
        </button>

      </div>
    </div>
  );
}

export default Header;
