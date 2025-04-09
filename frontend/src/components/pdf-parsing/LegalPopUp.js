import React from 'react';
import './UploadTranscript.css';

function LegalPopUp({ onConfirm, onCancel }) {
  return (
    <div className="legal-popup-container">
      <div className="legal-popup-card">
        <h2>Legal Notice</h2>
        <p>
          By uploading your transcript, you agree to share your academic
          information with us. We do not store your transcript or any personally
          identifiable information (PII). Only your program, grades, GPA, and co-op
          details will be stored securely for analysis.
        </p>
        <div className="legal-popup-buttons">
          {/* eslint-disable-next-line react/button-has-type */}
          <button className="agree" onClick={onConfirm}>I Agree</button>
          {/* eslint-disable-next-line react/button-has-type */}
          <button className="cancel" onClick={onCancel}>Cancel</button>
        </div>
      </div>
    </div>
  );
}

export default LegalPopUp;
