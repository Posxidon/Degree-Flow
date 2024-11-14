import React from 'react';
import './ButtonSection.css';
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

function UploadTranscript() {
  const handleUpload = () => {
    // Future file upload functionality
    console.log('Upload button clicked');
  };

  return (
    <div className="upload-container">
      <h3 className="upload-title">Upload Transcript</h3>
      <button
        type="button"
        className="upload-box"
        onClick={handleUpload}
      >
        <div className="upload-content">
          <svg
            className="upload-icon"
            fill="none"
            strokeWidth="1.2"
            width="150"
            height="150"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
          </svg>
          <p className="upload-text">
            To update current academic progress, upload your current unofficial academic transcript
          </p>
        </div>
      </button>
    </div>
  );
}

function ButtonSection() {
  const navigate = useNavigate(); // Initialize useNavigate

  const handleSearchClick = () => {
    console.log('Search button clicked');
    navigate('/FilterSelection'); // Navigate to Filter Page
  };

  const handleGenerateClick = () => {
    console.log('Generate button clicked');
  };

  return (
    <div className="button-container">
      <div className="left-buttons">
        <ActionButton onClick={handleSearchClick}>
          Search Filtered Courses
        </ActionButton>
        <ActionButton onClick={handleGenerateClick}>
          Generate New Schedules
        </ActionButton>
      </div>
      <div className="right-upload">
        <UploadTranscript />
      </div>
    </div>
  );
}

export default ButtonSection;
