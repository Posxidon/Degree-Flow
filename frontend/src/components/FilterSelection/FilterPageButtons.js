import React from 'react';
import './FilterButtonSection.css';

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
  const handleApplyClick = () => {
    console.log('Apply Filters button clicked');
    // Dispatch event to trigger course search
    const applyFiltersEvent = new CustomEvent('applyFilters');
    document.dispatchEvent(applyFiltersEvent);
  };

  return (
    <div className="button-container">
      <div className="filter-page-buttons">
        <ActionButton onClick={handleApplyClick}>
          Apply Filters
        </ActionButton>
      </div>
    </div>
  );
}

export default ButtonSection;
