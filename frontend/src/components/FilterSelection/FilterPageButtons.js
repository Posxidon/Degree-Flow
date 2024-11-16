import React from 'react';
import './FilterButtonSection.css'; // '../buttonSection/ButtonSection.css';
import { useNavigate } from 'react-router-dom';// this is to use routing function

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
  const navigate = useNavigate(); // Initialize useNavigate
  const handleBackClick = () => {
    console.log('Back button clicked');
    navigate('/'); // Navigate to Filter Page
  };

  const handleApplyClick = () => {
    console.log('Generate button clicked');
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
