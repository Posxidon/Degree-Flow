import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './Header.css';
import mesLogo from './Logo_Name_White.png';

function Header() {
  const navigate = useNavigate();
  const currentPath = useLocation().pathname;

  return (
    <div className="custom-header">
      <div className="secondary-bar">
        <img src={mesLogo} alt="MES Logo" className="mes-logo" />
        {/* eslint-disable-next-line max-len */}
        {/* eslint-disable-next-line jsx-a11y/click-events-have-key-events,jsx-a11y/no-noninteractive-element-interactions */}
        <h1 className="app-title" onClick={() => navigate('/dashboard')} style={{ cursor: 'pointer' }}>
          DegreeFlow
        </h1>
        <button
          type="button"
          className="login-button"
          onClick={() => navigate('/login')}
        >
          Login
        </button>
      </div>

      <div className="action-bar">
        {/* HOME Button → Now points to /dashboard */}
        <button
          type="button"
          onClick={() => navigate('/dashboard')}
        >
          Home
        </button>

        {/* Upload Transcript (optional route, update as needed) */}
        <button
          type="button"
          onClick={() => navigate('/upload-transcript')}
        >
          Upload Transcript
        </button>

        <button
          type="button"
          onClick={() => navigate('/FilterSelection')}
        >
          Search Filtered Courses
        </button>

        <button
          type="button"
          className={currentPath === '/generate-schedule' ? 'active' : ''}
          onClick={() => navigate('/generate-schedule')}
        >
          Seat Alert
        </button>

        <button
          type="button"
          className={currentPath === '/generate-schedule' ? 'active' : ''}
          onClick={() => navigate('/generate-schedule')}

        >
          Generate Schedule
        </button>

        <button
          type="button"
          onClick={() => navigate('/what-if')}
        >
          What if scenario
        </button>
      </div>
    </div>
  );
}

export default Header;
