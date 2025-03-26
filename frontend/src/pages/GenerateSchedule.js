import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function GenerateSchedule() {
  const navigate = useNavigate();
  const [selectedTerm, setSelectedTerm] = useState('Fall 2025');
  const [schedule, setSchedule] = useState([]);

  // Define course data for each term (5 courses per term)
  const coursesByTerm = {
    'Fall 2025': [
      {
        code: 'COMPSCI 1MD3',
        name: 'Discrete Math for Comp Sci',
        instructor: 'N/A',
        room: 'BSB 108'
      },
      {
        code: 'COMPSCI 1JC3',
        name: 'Intro to Computational Thinking',
        instructor: 'N/A',
        room: 'KTH 104'
      },
      {
        code: 'COMPSCI 1AD3',
        name: '',
        instructor: 'N/A',
        room: 'TSH 127'
      },
      {
        code: 'COMPSCI 1XC3',
        name: 'Development Basics',
        instructor: 'N/A',
        room: 'BSB 108'
      },
      {
        code: 'COMPSCI 1XD3',
        name: 'Intro to Software Development',
        instructor: 'N/A',
        room: 'KTH 104'
      }
    ],
    'Winter 2025': [
      {
        code: 'MATH 1B03',
        name: 'Linear Algebra',
        instructor: 'N/A',
        room: 'BSB 108'
      },
      {
        code: 'MATH 1ZA3',
        name: 'Engineering Mathematics I',
        instructor: 'N/A',
        room: 'KTH 107'
      },
      {
        code: 'MATH 1ZB3',
        name: 'Engineering Mathematics II-A',
        instructor: 'N/A',
        room: 'TSH 127'
      },
      {
        code: 'ECON 1B03',
        name: 'Introductory Microeconomics',
        instructor: 'N/A',
        room: 'BSB 108'
      },
      {
        code: 'COMPSCI 2BC3',
        name: 'Computer Science Fundamentals',
        instructor: 'N/A',
        room: 'KTH 104'
      }
    ],
    'Fall 2026': [
      {
        code: 'COMPSCI 2AC3',
        name: 'Automata and Computability',
        instructor: 'N/A',
        room: 'MDCL 1005'
      },
      {
        code: 'COMPSCI 2C03',
        name: 'Data Structures and Algorithms',
        instructor: 'N/A',
        room: 'LRWB 1003'
      },
      {
        code: 'COMPSCI 2DB3',
        name: 'Database Systems',
        instructor: 'N/A',
        room: 'BSB 108'
      },
      {
        code: 'COMPSCI 2GA3',
        name: 'Computer Architecture',
        instructor: 'N/A',
        room: 'KTH 104'
      },
      {
        code: 'COMPSCI 2LC3',
        name: 'Logical Reasoning',
        instructor: 'N/A',
        room: 'TSH 127'
      }
    ],
    'Winter 2026': [
      {
        code: 'COMPSCI 2ME3',
        name: 'Intro to software development',
        instructor: 'N/A',
        room: 'BSB 108'
      },
      {
        code: 'COMPSCI 2SD3',
        name: 'Concurrent systems',
        instructor: 'N/A',
        room: 'KTH 104'
      },
      {
        code: 'COMPSCI 2XC3',
        name: 'Algorithms and software design',
        instructor: 'N/A',
        room: 'MDCL 1007'
      },
      {
        code: 'COMPSCI 3SD3',
        name: 'Computer Graphics',
        instructor: 'N/A',
        room: 'LRWB 1003'
      },
      {
        code: 'COMPSCI 4AL3',
        name: 'Applications of machine learning',
        instructor: 'N/A',
        room: 'TSH 108'
      }
    ]
  };

  useEffect(() => {
    setSchedule(coursesByTerm[selectedTerm] || []);
  }, [selectedTerm]);

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

  const messageStyle = {
    textAlign: 'center',
    marginBottom: '20px',
    fontSize: '16px',
    color: '#333'
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
      <p style={messageStyle}>
        Your transcript was parsed successfully and your schedule is ready.
      </p>
      <select
        style={selectStyle}
        value={selectedTerm}
        onChange={(e) => setSelectedTerm(e.target.value)}
      >
        <option value="Fall 2025">Fall 2025</option>
        <option value="Winter 2025">Winter 2025</option>
        <option value="Fall 2026">Fall 2026</option>
        <option value="Winter 2026">Winter 2026</option>
      </select>
      {schedule
       && schedule.length > 0
       && schedule.map((course) => (
         <div key={course.code} style={courseBlockStyle}>
           <div style={courseTitleStyle}>
             <span>{course.code}</span>
             <span>{': '}</span>
             <span>{course.name}</span>
           </div>
           <div style={courseDetailsStyle}>
             <p>
               <strong>Instructor:</strong>
               <span>{course.instructor}</span>
             </p>
             <p>
               <strong>Room:</strong>
               <span>{course.room}</span>
             </p>
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
