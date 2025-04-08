import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function GenerateSchedule() {
  const navigate = useNavigate();

  // We'll store the parsed JSON object here
  const [scheduleByYear, setScheduleByYear] = useState({});
  const [selectedYear, setSelectedYear] = useState('1');
  const [loading, setLoading] = useState(true);

  // Use a valid user ID that exists in your json_schedule table (e.g. "1")
  const userId = '1';

  useEffect(() => {
    async function fetchSchedule() {
      try {
        const response = await fetch(`http://localhost:8080/api/schedules/${userId}`);
        if (response.ok) {
          // The endpoint returns a JSON string
          const scheduleJsonString = await response.json();
          // Convert the JSON string to an object
          const parsedData = JSON.parse(scheduleJsonString);
          setScheduleByYear(parsedData);
        } else {
          console.error('Schedule not found or error occurred');
        }
      } catch (error) {
        console.error('Error fetching schedule:', error);
      } finally {
        setLoading(false);
      }
    }
    fetchSchedule();
  }, [userId]);

  if (loading) {
    return <p>Loading schedule...</p>;
  }

  // The user selected "1", "2", "3", or "4". We'll show that year's array of courses
  const coursesForYear = scheduleByYear[selectedYear] || [];

  const containerStyle = {
    margin: '20px auto',
    width: '90%',
    maxWidth: '800px',
    border: '1px solid #ccc',
    borderRadius: '8px',
    padding: '20px',
    backgroundColor: '#fff'
  };

  const headerStyle = {
    fontSize: '24px',
    fontWeight: 'bold',
    color: '#85013c',
    textAlign: 'center',
    marginBottom: '20px'
  };

  const selectStyle = {
    width: '100%',
    padding: '10px',
    fontSize: '16px',
    borderRadius: '4px',
    border: '1px solid #ccc',
    marginBottom: '20px'
  };

  const courseBlockStyle = {
    border: '1px solid #85013c',
    borderRadius: '6px',
    padding: '10px',
    marginBottom: '15px'
  };

  const courseTitleStyle = {
    fontSize: '18px',
    fontWeight: 'bold',
    color: '#85013c',
    marginBottom: '5px'
  };

  const courseDetailsStyle = {
    fontSize: '14px',
    color: '#333'
  };

  const backButtonStyle = {
    width: '100%',
    padding: '12px 20px',
    fontSize: '16px',
    color: '#fff',
    backgroundColor: '#85013c',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
    marginTop: '20px',
    textAlign: 'center'
  };

  return (
    <div style={containerStyle}>
      <h2 style={headerStyle}>Generate Schedule</h2>
      <p style={{
        textAlign: 'center', marginBottom: '20px', fontSize: '16px', color: '#333'
      }}
      >
        Your transcript was parsed successfully and your schedule is ready.
      </p>

      {/* Year dropdown */}
      <select
        style={selectStyle}
        value={selectedYear}
        onChange={(e) => setSelectedYear(e.target.value)}
      >
        <option value="1">Year 1</option>
        <option value="2">Year 2</option>
        <option value="3">Year 3</option>
        <option value="4">Year 4</option>
      </select>

      {/* Display courses for the selected year */}
      {coursesForYear.map((course) => (
        <div key={course.id} style={courseBlockStyle}>
          <div style={courseTitleStyle}>
            <span>{course.courseCode}</span>
            <span>{': '}</span>
            <span>{course.name}</span>
          </div>
          <div style={courseDetailsStyle}>
            <p>
              <strong>Unit:</strong>
              {' '}
              {course.unit}
            </p>
            <p>
              <strong>Description:</strong>
              {' '}
              {course.desc}
            </p>
            <p>
              <strong>Years:</strong>
              {' '}
              {course.years}
            </p>
            {/* Additional fields like prereqs, antireqs can be displayed if desired */}
          </div>
        </div>
      ))}

      <button
        type="button"
        style={backButtonStyle}
        onClick={() => navigate('/dashboard')}
      >
        Back to Dashboard
      </button>
    </div>
  );
}

export default GenerateSchedule;
