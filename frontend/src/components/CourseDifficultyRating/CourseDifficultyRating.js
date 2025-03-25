import React, { useState, useEffect, useRef } from 'react';
import RatingApiService from '../../services/RatingApiService';
import './CourseDifficultyRating.css';

function CourseDifficultyRating({ courseId, email }) {
  const [averageRating, setAverageRating] = useState(0);
  const [totalRatings, setTotalRatings] = useState(0);
  const [difficultyCategory, setDifficultyCategory] = useState('No ratings yet');
  const [userRating, setUserRating] = useState(0);
  const [hoverRating, setHoverRating] = useState(0);
  const [modalOpen, setModalOpen] = useState(false);
  const [submitted, setSubmitted] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  // Refs for focus trapping
  const modalRef = useRef(null);
  const closeButtonRef = useRef(null);

  // Default email if not provided
  const currentEmail = email || 'current-user';

  // Load course ratings on component mount
  useEffect(() => {
    const fetchRatings = async () => {
      try {
        setIsLoading(true);
        // Get the rating summary for this course
        const summary = await RatingApiService.getRatingSummary(courseId);
        setAverageRating(summary.averageRating);
        setTotalRatings(summary.totalRatings);
        setDifficultyCategory(summary.difficultyCategory);

        // Check if the current student has already rated this course
        const existingRating = await RatingApiService.getStudentRating(currentEmail, courseId);
        if (existingRating) {
          setUserRating(existingRating.stars);
        }

        setIsLoading(false);
      } catch (err) {
        setError('Failed to load ratings. Please try again later.');
        setIsLoading(false);
      }
    };

    fetchRatings();
  }, [courseId, currentEmail]);

  // Handle opening/closing the modal
  useEffect(() => {
    // Handle ESC key to close modal
    const handleKeyDown = (e) => {
      if (e.key === 'Escape' && modalOpen && !submitted) {
        setModalOpen(false);
      }
    };

    // When modal opens, focus the close button and add event listener
    if (modalOpen) {
      if (closeButtonRef.current) {
        closeButtonRef.current.focus();
      }
      document.addEventListener('keydown', handleKeyDown);
      // Prevent body from scrolling
      document.body.style.overflow = 'hidden';
    } else {
      // Re-enable scrolling when modal closes
      document.body.style.overflow = 'auto';
    }

    // Clean up
    return () => {
      document.removeEventListener('keydown', handleKeyDown);
      document.body.style.overflow = 'auto';
    };
  }, [modalOpen, submitted]);

  const handleStarClick = (rating) => {
    setUserRating(rating);
  };

  const handleStarHover = (rating) => {
    setHoverRating(rating);
  };

  const handleMouseLeave = () => {
    setHoverRating(0);
  };

  const handleSubmit = async () => {
    if (userRating === 0) return;

    try {
      // Submit the rating to the backend
      await RatingApiService.submitRating(currentEmail, courseId, userRating);

      // Refresh the rating summary
      const summary = await RatingApiService.getRatingSummary(courseId);
      setAverageRating(summary.averageRating);
      setTotalRatings(summary.totalRatings);
      setDifficultyCategory(summary.difficultyCategory);

      setSubmitted(true);
      // The user can manually close the modal when ready
    } catch (err) {
      setError('Failed to submit rating. Please try again later.');
    }
  };

  const handleOverlayClick = (e) => {
    // Only close if clicking the overlay itself, not content
    if (e.target === e.currentTarget && !submitted) {
      setModalOpen(false);
    }
  };

  const handleCloseModal = () => {
    setModalOpen(false);
    setSubmitted(false); // Reset submit state for next time
  };

  // Helper function to get difficulty label
  const getDifficultyLabel = (stars) => {
    switch (stars) {
      case 1: return 'Very Easy';
      case 2: return 'Easy';
      case 3: return 'Medium';
      case 4: return 'Challenging';
      case 5: return 'Very Difficult';
      default: return '';
    }
  };

  // If loading or error
  if (isLoading) {
    return <div>Loading rating data...</div>;
  }

  if (error) {
    return <div className="rating-error">{error}</div>;
  }

  // Render component
  return (
    <section className="course-rating-simple">
      <div className="course-info-row">
        <span className="course-info-label">Course Difficulty Rating:</span>
        <span className="course-info-value">{difficultyCategory}</span>
        <button
          type="button"
          className="rating-button"
          onClick={() => setModalOpen(true)}
        >
          Rating
        </button>
      </div>

      {/* Accessible Modal */}
      {modalOpen && (
        <div
          className="rating-modal-backdrop"
          onClick={handleOverlayClick}
          aria-hidden="true"
        >
          <div
            ref={modalRef}
            className="rating-modal-content"
            role="dialog"
            aria-modal="true"
            aria-labelledby="rating-modal-title"
          >
            <button
              ref={closeButtonRef}
              type="button"
              className="modal-close-btn"
              onClick={handleCloseModal}
              aria-label="Close rating dialog"
            >
              ×
            </button>

            <h3 id="rating-modal-title">Course Difficulty Rating</h3>

            {!submitted ? (
              <div className="rating-selection">
                <p>How difficult is this course?</p>
                <div className="star-rating">
                  {[1, 2, 3, 4, 5].map((star) => (
                    <button
                      key={star}
                      type="button"
                      className={`star-btn ${star <= (hoverRating || userRating) ? 'active' : ''}`}
                      onClick={() => handleStarClick(star)}
                      onMouseEnter={() => handleStarHover(star)}
                      onMouseLeave={handleMouseLeave}
                      aria-label={`Rate as ${star} star${star !== 1 ? 's' : ''}: ${getDifficultyLabel(star)}`}
                    >
                      ★
                    </button>
                  ))}
                  <span className="rating-label">
                    {getDifficultyLabel(hoverRating || userRating)}
                  </span>
                </div>
                <p className="rating-hint">1 star = easiest, 5 stars = hardest</p>
                <button
                  type="button"
                  className="submit-btn"
                  disabled={userRating === 0}
                  onClick={handleSubmit}
                >
                  Submit Rating
                </button>
              </div>
            ) : (
              <p className="thank-you-message" aria-live="polite">Thank you for your rating!</p>
            )}

            <div className="rating-statistics">
              <p className="current-rating">
                Current rating:
                {' '}
                <strong>{averageRating.toFixed(1)}</strong>
                {' '}
                (
                {difficultyCategory}
                )
              </p>
              <p className="total-ratings">
                Based on
                {' '}
                {totalRatings}
                {' '}
                student ratings
              </p>
            </div>
          </div>
        </div>
      )}
    </section>
  );
}

export default CourseDifficultyRating;
