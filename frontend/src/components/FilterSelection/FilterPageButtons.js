import React from 'react';
import './FilterButtonSection.css';
import { useNavigate } from 'react-router-dom';

function ActionButton({ children, onClick }) {
  return (
    <button
      type="button"
      className="action-button"
      onClick={onClick}
    >
      {children}
    </button>
  );
}

function ButtonSection() {
  const navigate = useNavigate();

  const handleBackClick = () => {
    console.log('Back button clicked');
    navigate('/');
  };

  const handleApplyClick = () => {
    console.log('Apply Filters button clicked');
    // Dispatch event to trigger course search
    const applyFiltersEvent = new CustomEvent('applyFilters');
    document.dispatchEvent(applyFiltersEvent);
  };

  return (
    <div className="button-container">
      <div className="filter-page-buttons">
        <ActionButton onClick={handleBackClick}>
          Main Page
        </ActionButton>
        <ActionButton onClick={handleApplyClick}>
          Apply Filters
        </ActionButton>
      </div>
    </div>
  );
}

export default ButtonSection;
