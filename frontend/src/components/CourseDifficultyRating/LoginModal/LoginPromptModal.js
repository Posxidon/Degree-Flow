import React, { useRef, useEffect } from 'react';
import './LoginPromptModal.css';

function LoginPromptModal({ onClose }) {
  const closeButtonRef = useRef(null);

  // Focus trap and keyboard navigation
  useEffect(() => {
    // Focus the close button when modal opens
    if (closeButtonRef.current) {
      closeButtonRef.current.focus();
    }

    // Handle ESC key to close modal
    const handleKeyDown = (e) => {
      if (e.key === 'Escape') {
        onClose();
      }
    };

    document.addEventListener('keydown', handleKeyDown);
    document.body.style.overflow = 'hidden'; // Prevent scrolling

    return () => {
      document.removeEventListener('keydown', handleKeyDown);
      document.body.style.overflow = 'auto'; // Re-enable scrolling
    };
  }, [onClose]);

  const handleOverlayClick = (e) => {
    // Only close if clicking the overlay itself, not content
    if (e.target === e.currentTarget) {
      onClose();
    }
  };

  // Handle login button click - this would redirect to login page
  const handleLoginClick = () => {
    onClose();
    console.log('Redirect to login page');
  };

  return (
    <div
      className="rating-login-prompt-backdrop"
      onClick={handleOverlayClick}
      aria-hidden="true"
    >
      <div
        className="rating-login-prompt-content"
        role="dialog"
        aria-modal="true"
        aria-labelledby="login-prompt-title"
      >
        <button
          ref={closeButtonRef}
          type="button"
          className="rating-login-prompt-close-btn"
          onClick={onClose}
          aria-label="Close login prompt"
        >
          ×
        </button>

        <h3 id="login-prompt-title" className="rating-login-prompt-title">
          Login Required
        </h3>

        <p className="rating-login-prompt-message">
          You need to be logged in to rate courses. Please log in with your email to continue.
        </p>

        <button
          type="button"
          className="rating-login-prompt-button"
          onClick={handleLoginClick}
        >
          Login
        </button>
      </div>
    </div>
  );
}

export default LoginPromptModal;
