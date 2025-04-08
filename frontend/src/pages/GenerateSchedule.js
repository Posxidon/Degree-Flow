// GenerateSchedule.js

import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth0 } from '@auth0/auth0-react';
import axios from 'axios';
import './GenerateSchedule.css';

function GenerateSchedule() {
  const navigate = useNavigate();
  const {
    user, isAuthenticated, isLoading
  } = useAuth0();

  const [selectedYear, setSelectedYear] = useState('1');
  const [coursesByYear, setCoursesByYear] = useState({});
  const [error, setError] = useState('');

  const fetchSchedule = async (userId) => {
    try {
      console.log('👤 Logged-in user ID:', userId);

      const res = await axios.get(`http://localhost:8080/api/public/schedules/${encodeURIComponent(userId)}`);
      if (!res.data) throw new Error('No data returned from backend');

      console.log('✅ Raw JSON from backend:', res.data);
      const parsed = JSON.parse(res.data);
      console.log('✅ Parsed schedule by year:', parsed);

      setCoursesByYear(parsed);
    } catch (err) {
      console.error('❌ Error fetching schedule:', err);
      setError('Failed to load schedule. Please try again.');
    }
  };

  useEffect(() => {
    if (!isLoading && isAuthenticated && user?.sub) {
      fetchSchedule(user.sub).catch(console.error);
    }
  }, [isAuthenticated, isLoading, user]);

  const handleYearChange = (e) => {
    const year = e.target.value;
    setSelectedYear(year);
    const courses = coursesByYear[year] || [];
    console.log(`📅 Selected Year: ${year}`);
    courses.forEach((course) => {
      const shortDesc = course.desc?.split('.')[0] || 'No description available';
      console.log(`➡️ ${course.courseCode}: ${shortDesc}`);
    });
  };

  const courses = coursesByYear[selectedYear] || [];

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

      {courses.length > 0 ? (
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
