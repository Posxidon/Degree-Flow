import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function GenerateSchedule() {
  const navigate = useNavigate();
  const [schedule, setSchedule] = useState([]);
  const [loading, setLoading] = useState(true);

  // Use a test user ID that exists in your json_schedule table.
  // Update this value to match a valid record in your database.
  const userId = 'testUser';

  useEffect(() => {
    async function fetchSchedule() {
      try {
        const response = await fetch(`http://localhost:8080/api/schedules/${userId}`);
        if (response.ok) {
          // The endpoint returns a JSON string (schedule_data) from the database.
          const scheduleJsonString = await response.json();
          // Parse the JSON string into an array or object.
          const scheduleData = JSON.parse(scheduleJsonString);
          setSchedule(scheduleData);
        } else {
          console.error('Schedule not found');
        }
      } catch (error) {
        console.error('Error fetching schedule:', error);
      } finally {
        setLoading(false);
      }
    }
    fetchSchedule();
  }, [userId]);

  if (loading) return <p>Loading schedule...</p>;

  return (
    <div style={{
      margin: '20px auto', width: '90%', maxWidth: '800px', padding: '20px'
    }}
    >
      <h2 style={{ textAlign: 'center', color: '#85013c' }}>Your Schedule</h2>
      {schedule && schedule.length > 0 ? (
        schedule.map((course) => (
          <div
            key={course.code}
            style={{
              border: '1px solid #85013c', borderRadius: '6px', padding: '10px', marginBottom: '15px'
            }}
          >
            <div style={{ fontSize: '18px', fontWeight: 'bold', color: '#85013c' }}>
              {course.code}
              :
              {course.name}
            </div>
            <div style={{ fontSize: '14px', color: '#333' }}>
              <p>
                <strong>Instructor:</strong>
                {' '}
                {course.instructor}
              </p>
              <p>
                <strong>Room:</strong>
                {' '}
                {course.room}
              </p>
            </div>
          </div>
        ))
      ) : (
        <p>No courses scheduled.</p>
      )}
      <button
        type="button"
        style={{
          width: '100%', padding: '12px 20px', fontSize: '16px', color: '#fff', backgroundColor: '#85013c', border: 'none', borderRadius: '4px', cursor: 'pointer', marginTop: '20px'
        }}
        onClick={() => navigate('/dashboard')}
      >
        Back to Dashboard
      </button>
    </div>
  );
}

export default GenerateSchedule;
