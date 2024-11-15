import React from 'react';

function TermSelector({
  currentTerm, showMenu, onToggleMenu, onTermChange
}) {
  return (
    <div className="term-selector">
      <button
        type="button"
        className="nav-button term-button"
        onClick={onToggleMenu}
      >
        {currentTerm}
      </button>

      {showMenu && (
        <div className="term-menu">
          <button
            type="button"
            className={`menu-item ${currentTerm === '2024 Fall' ? 'active' : ''}`}
            onClick={() => onTermChange('2024 Fall')}
          >
            2024 Fall
          </button>
          <button
            type="button"
            className={`menu-item ${currentTerm === 'Winter 2025' ? 'active' : ''}`}
            onClick={() => onTermChange('Winter 2025')}
          >
            Winter 2025
          </button>
        </div>
      )}
    </div>
  );
}

export default TermSelector;
