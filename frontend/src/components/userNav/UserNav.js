import React, { useState, useRef, useEffect } from 'react';
import LoginButton from './components/LoginButton';
import TermSelector from './components/TermSelector';
import ProfileMenu from './components/ProfileMenu';
import './UserNav.css';

function UserNav() {
  const [showTermMenu, setShowTermMenu] = useState(false);
  const [showProfileMenu, setShowProfileMenu] = useState(false);
  const [currentTerm, setCurrentTerm] = useState('2024 Fall');

  const termSelectorRef = useRef(null);
  const profileMenuRef = useRef(null);

  useEffect(() => {
    function handleClickOutside(event) {
      if (termSelectorRef.current && !termSelectorRef.current.contains(event.target)) {
        setShowTermMenu(false);
      }
      if (profileMenuRef.current && !profileMenuRef.current.contains(event.target)) {
        setShowProfileMenu(false);
      }
    }
    document.addEventListener('mousedown', handleClickOutside);

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  return (
    <div className="user-nav">
      <LoginButton onClick={() => console.log('Login clicked')} />

      <div ref={termSelectorRef}>
        <TermSelector
          currentTerm={currentTerm}
          showMenu={showTermMenu}
          onToggleMenu={() => setShowTermMenu(!showTermMenu)}
          onTermChange={(term) => {
            setCurrentTerm(term);
            setShowTermMenu(false);
          }}
        />
      </div>

      <div ref={profileMenuRef}>
        <ProfileMenu
          currentTerm={currentTerm}
          showMenu={showProfileMenu}
          onToggleMenu={() => setShowProfileMenu(!showProfileMenu)}
        />
      </div>
    </div>
  );
}

export default UserNav;
