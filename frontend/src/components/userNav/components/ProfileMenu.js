import React from 'react';

function MenuIcon() {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      height="24px"
      viewBox="0 -960 960 960"
      width="24px"
      fill="#5f6368"
      aria-hidden="true"
    >
      <path d="M120-240v-80h720v80H120Zm0-200v-80h720v80H120Zm0-200v-80h720v80H120Z" />
    </svg>
  );
}

function ProfileMenu({ currentTerm, showMenu, onToggleMenu }) {
  return (
    <div className="profile-menu-container">
      <button
        type="button"
        className="nav-button menu-button"
        onClick={onToggleMenu}
      >
        <MenuIcon />
      </button>

      {showMenu && (
        <div className="profile-menu">
          <div className="profile-header">
            <span className="current-term">{currentTerm}</span>
            <span className="user-name">Current User</span>
          </div>
          <div className="menu-divider" />
          <button
            type="button"
            className="menu-item"
            onClick={() => console.log('View Profile clicked')}
          >
            View Profile
          </button>
          <button
            type="button"
            className="menu-item"
            onClick={() => console.log('Academic History clicked')}
          >
            Academic History
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
  );
}

export default ProfileMenu;
