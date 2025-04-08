// GenerateSchedule.js

import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth0 } from '@auth0/auth0-react';
import './GenerateSchedule.css';

function GenerateSchedule() {
  const navigate = useNavigate();
  const { getAccessTokenSilently } = useAuth0();
  const [selectedYear, setSelectedYear] = useState('1');
  const [loading, setLoading] = useState(false);
  const [coursesByYear, setCoursesByYear] = useState({});
  const [error, setError] = useState('');
  const [courses, setCourses] = useState([]);
  const url = 'http://localhost:8080/api/schedules/getSchedule?';
  const fetchSchedule = async () => {
    console.log('requesting');
    setLoading(true);
    try {
      const token = await getAccessTokenSilently({
        audience: 'https://degreeflow-backend/api',
        scope: 'read:data write:data'
      });
      const response = await (await fetch(url + new URLSearchParams({
        userID: token
      }), {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${token}`
        }
      })).json();
      setCoursesByYear(response);
      setLoading(false);
      console.log(response);
      console.log('success');
    } catch (err) {
      console.log(err.message);
      setError('Failed to get degree data, please try again.');
    }
  };

  useEffect(() => {
    fetchSchedule();
  }, []);

  const handleYearChange = (e) => {
    setSelectedYear(e);
  };

  useEffect(() => {
    setCourses(coursesByYear[selectedYear]);
  }, [coursesByYear, selectedYear]);

  return (
    <div className="schedule-container">
      <h2 className="schedule-header">Generate Schedule</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <p className="schedule-message">Your transcript was parsed successfully and your schedule is ready.</p>

      <select className="schedule-select" value={selectedYear} onChange={handleYearChange}>
        <option value="1">Year 1</option>
        <option value="2">Year 2</option>
        <option value="3">Year 3</option>
        <option value="4">Year 4</option>
      </select>

      {Array.isArray(courses) && courses.length > 0 ? (
        courses.map((course) => (
          <div key={course.id} className="course-block">
            <div className="course-title">
              {course.courseCode}
              :
              {' '}
              {course.name}
            </div>
            <div className="course-details">
              <p>
                <strong>Description:</strong>
                {' '}
                {course.desc}
              </p>
              <p>
                <strong>Prereqs:</strong>
                {' '}
                {course.prereqs?.join(', ') || 'None'}
              </p>
              <p>
                <strong>Antireqs:</strong>
                {' '}
                {course.antireqs?.join(', ') || 'None'}
              </p>
              <p>
                <strong>Units:</strong>
                {' '}
                {course.unit}
              </p>
            </div>
          </div>
        ))
      ) : (
        <p className="schedule-message">No courses available for this year.</p>
      )}

      <button type="button" className="back-button" onClick={() => navigate('/dashboard')}>
        Back to Dashboard
      </button>
    </div>
  );
}

export default GenerateSchedule;
