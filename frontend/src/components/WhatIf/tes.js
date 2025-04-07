import React, { useState, useEffect, useRef } from 'react';
import { useAuth0 } from '@auth0/auth0-react';

const styles = {
  container: {
    margin: '20px auto',
    width: '400px',
    border: '1px solid #ccc',
    borderRadius: '8px',
    padding: '20px',
    textAlign: 'center',
    backgroundColor: '#fff',
    position: 'relative'
  },
  inputWrapper: {
    position: 'relative',
    marginBottom: '16px'
  },
  header: {
    marginBottom: '15px',
    fontSize: '20px',
    fontWeight: 'bold'
  },
  label: {
    display: 'block',
    textAlign: 'left',
    marginBottom: '5px',
    fontWeight: 'bold'
  },
  input: {
    width: '100%',
    padding: '8px',
    marginBottom: '10px',
    fontSize: '16px'
  },
  dropdown: {
    position: 'absolute',
    top: '100%',
    left: 0,
    width: '100%',
    maxWidth: '350px',
    backgroundColor: '#fff',
    border: '1px solid #ccc',
    borderRadius: '8px',
    boxShadow: '0 4px 10px rgba(0, 0, 0, 0.1)',
    maxHeight: '180px',
    overflowY: 'auto',
    zIndex: 1000,
    marginTop: '4px'
  },
  dropdownItem: {
    display: 'block',
    width: '100%',
    textAlign: 'left',
    padding: '10px',
    fontSize: '14px',
    border: 'none',
    background: 'white',
    cursor: 'pointer',
    borderBottom: '1px solid #f1f1f1',
    transition: 'background 0.2s'
  },
  button: {
    width: '100%',
    padding: '12px',
    fontSize: '16px',
    color: '#fff',
    backgroundColor: '#7a003c',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer'
  },
  errorBox: {
    marginTop: '10px',
    backgroundColor: '#ffe6e6',
    color: 'red',
    padding: '10px',
    borderRadius: '4px',
    fontWeight: 'bold'
  },
  successBox: {
    marginTop: '10px',
    backgroundColor: '#e6ffe6',
    color: 'green',
    padding: '10px',
    borderRadius: '4px',
    fontWeight: 'bold'
  }
};
function SeatAlertPage() {
  const { getAccessTokenSilently } = useAuth0();
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
    try {
      const token = await getAccessTokenSilently({
        audience: 'https://degreeflow-backend/api',
        scope: 'read:data write:data'
      });
      const url = `http://localhost:8080/api/courses/wildcard?subjectCode=${subject}&catalogPattern=${pattern}`;
      const resp = await fetch(url, {
        headers: { Authorization: `Bearer ${token}` }
      });
      if (!resp.ok) return [];
      const data = await resp.json();
      return data;
    } catch (err) {
      console.error('Error fetching courses:', err);
      return [];
    }
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
        const allResults = await Promise.all(
          patterns.map((p) => fetchSinglePattern(subject, p))
        );
        results = allResults.flat();
      }
      if (results.length > 0) {
        setSuggestions(results);
        setShowDropdown(true);
      } else {
        setSuggestions([]);
        setShowDropdown(false);
      }
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
      const token = await getAccessTokenSilently({
        audience: 'https://degreeflow-backend/api',
        scope: 'read:data write:data'
      });
      const resp = await fetch('http://localhost:8080/api/seat-alerts/subscribe', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`
        },
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
    <div style={styles.container} ref={containerRef}>
      <h2 style={styles.header}>Seat Alert Subscription</h2>
      <div style={styles.inputWrapper}>
        {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
        <label style={styles.label} htmlFor="courseCodeInput">
          Course Code
        </label>
        <input
          id="courseCodeInput"
          style={styles.input}
          type="text"
          placeholder="e.g. COMPSCI 3CR3"
          value={courseCode}
          onChange={handleCourseCodeChange}
          onFocus={() => {
            if (suggestions.length > 0) setShowDropdown(true);
          }}
        />
        {showDropdown && suggestions.length > 0 && (
          <div style={styles.dropdown}>
            {suggestions.map((course) => (
              <button
                type="button"
                key={course.id}
                style={styles.dropdownItem}
                onClick={() => handleSuggestionClick(course)}
                onMouseEnter={(e) => {
                  e.currentTarget.style.backgroundColor = '#f0f0f0';
                }}
                onMouseLeave={(e) => {
                  e.currentTarget.style.backgroundColor = '#fff';
                }}
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
      <label style={styles.label} htmlFor="emailInput">
        Email
  </label>      <input        id="emailInput"        style={styles.input}        type="email"        placeholder="Enter Your Email"        value={email}        onChange={(e) => setEmail(e.target.value)}      />      {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}      <label style={styles.label} htmlFor="termSelect">        Term      </label>      <select        id="termSelect"        style={styles.input}        value={term}        onChange={(e) => setTerm(e.target.value)}      >        <option value="Fall-2024">Fall 2024</option>        <option value="Winter-2025">Winter 2025</option>        <option value="Spring/Summer-2025">Spring/Summer 2025</option>      </select>      <button        type="button"        style={styles.button}        onClick={handleSubscribe}        disabled={loading}      >        {loading ? 'Subscribing...' : 'Subscribe'}      </button>      {errorMessage && <div style={styles.errorBox}>{errorMessage}</div>}      {successMessage && <div style={styles.successBox}>{successMessage}</div>}    </div>  );}export default SeatAlertPage;