import React, { useState, useEffect, useRef } from 'react';
import './SeatAlertPage.css';

function SeatAlertPage() {
  const [courseCode, setCourseCode] = useState('');
  const [email, setEmail] = useState('');
  const [term, setTerm] = useState('Winter-2025');
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  const [suggestions, setSuggestions] = useState([]);
  const [showDropdown, setShowDropdown] = useState(false);

  const containerRef = useRef(null);

  useEffect(() => {
    const handleOutsideClick = (e) => {
      if (containerRef.current && !containerRef.current.contains(e.target)) {
        setShowDropdown(false);
      }
    };
    document.addEventListener('mousedown', handleOutsideClick);
    return () => document.removeEventListener('mousedown', handleOutsideClick);
  }, []);

  const fetchSinglePattern = async (subject, pattern) => {
    const url = `http://localhost:8080/api/courses/wildcard?subjectCode=${subject}&catalogPattern=${pattern}`;
    const resp = await fetch(url);
    if (!resp.ok) return [];
    const data = await resp.json();
    return data;
  };

  const handleCourseCodeChange = async (e) => {
    setSuccessMessage('');
    const value = e.target.value.toUpperCase();
    setCourseCode(value);

    if (value.length < 3) {
      setSuggestions([]);
      setShowDropdown(false);
      return;
    }

    const [subject, numPart = ''] = value.split(' ');
    if (!subject) {
      setSuggestions([]);
      setShowDropdown(false);
      return;
    }

    try {
      let results = [];
      const firstChar = numPart.charAt(0);

      if (/^[1-4]$/.test(firstChar)) {
        const pattern = `${firstChar}***`;
        const data = await fetchSinglePattern(subject, pattern);

        if (numPart.length > 1) {
          const remainder = numPart.substring(1).toUpperCase();
          results = data.filter((course) => course.catalogNumber.toUpperCase().includes(remainder));
        } else {
          results = data;
        }
      } else {
        const patterns = ['1***', '2***', '3***', '4***'];
        const allPromises = patterns.map((p) => fetchSinglePattern(subject, p));
        const allResults = await Promise.all(allPromises);
        results = allResults.flat();
      }

      setSuggestions(results);
      setShowDropdown(results.length > 0);
    } catch (err) {
      console.error('Autocomplete error:', err);
      setSuggestions([]);
      setShowDropdown(false);
    }
  };

  const handleSuggestionClick = (course) => {
    setCourseCode(`${course.subjectCode} ${course.catalogNumber}`);
    setShowDropdown(false);
    setSuggestions([]);
  };

  const handleSubscribe = async () => {
    setErrorMessage('');
    setSuccessMessage('');

    if (!courseCode || !email) {
      setErrorMessage('⚠ Please provide both course code and email.');
      return;
    }

    setLoading(true);
    try {
      const resp = await fetch('http://localhost:8080/api/seat-alerts/subscribe', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ courseCode, email, term })
      });
      if (!resp.ok) {
        throw new Error('Course not offered in this term.');
      }
      setSuccessMessage(`Subscribed successfully for ${courseCode} in ${term}!`);
    } catch (err) {
      setErrorMessage(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="seat-alert-container" ref={containerRef}>
      <h2 className="seat-alert-header">Seat Alert Subscription</h2>

      <div className="input-wrapper">
        {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
        <label className="seat-alert-label" htmlFor="courseCodeInput">
          Course Code
        </label>
        <input
          id="courseCodeInput"
          className="seat-alert-input"
          type="text"
          placeholder="e.g. COMPSCI 3CR3"
          value={courseCode}
          onChange={handleCourseCodeChange}
          onFocus={() => {
            if (suggestions.length > 0) setShowDropdown(true);
          }}
        />
        {showDropdown && suggestions.length > 0 && (
          <div className="seat-alert-dropdown">
            {suggestions.map((course) => (
              <button
                type="button"
                key={course.id}
                className="dropdown-item"
                onClick={() => handleSuggestionClick(course)}
              >
                {`${course.subjectCode} ${course.catalogNumber}${
                  course.title ? ` — ${course.title}` : ''
                }`}
              </button>
            ))}
          </div>
        )}
      </div>

      {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
      <label className="seat-alert-label" htmlFor="emailInput">
        Email
      </label>
      <input
        id="emailInput"
        className="seat-alert-input"
        type="email"
        placeholder="Enter Your Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />

      {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
      <label className="seat-alert-label" htmlFor="termSelect">
        Term
      </label>
      <select
        id="termSelect"
        className="seat-alert-select"
        value={term}
        onChange={(e) => setTerm(e.target.value)}
      >
        <option value="Fall-2024">Fall 2024</option>
        <option value="Winter-2025">Winter 2025</option>
        <option value="Spring/Summer-2025">Spring/Summer 2025</option>
      </select>

      <button
        type="button"
        className="seat-alert-button"
        onClick={handleSubscribe}
        disabled={loading}
      >
        {loading ? 'Subscribing...' : 'Subscribe'}
      </button>

      {errorMessage && <div className="error-box">{errorMessage}</div>}
      {successMessage && <div className="success-box">{successMessage}</div>}
    </div>
  );
}

export default SeatAlertPage;
