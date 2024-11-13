import React from 'react';
import './YearlySchedule.css';

function YearlySchedule() {
  const courses = {
    year1: {
      fall: { title: 'Fall Semester', courseCode: 'COMPSCI 1MD3', courseName: '' },
      winter: { title: 'Winter Semester', courseCode: 'COMPSCI 1JC3', courseName: '' }
    },
    year2: {
      fall: { title: 'Fall Semester', courseCode: 'COMPSCI 2C03', courseName: '' },
      winter: { title: 'Winter Semester', courseCode: 'COMPSCI 2AC3', courseName: '' }
    },
    year3: {
      fall: { title: 'Fall Semester', courseCode: 'COMPSCI 3LA3', courseName: '' },
      winter: { title: 'Winter Semester', courseCode: 'COMPSCI 3GA3', courseName: '' }
    },
    year4: {
      fall: { title: 'Fall Semester', courseCode: 'COMPSCI 4HC3', courseName: '' },
      winter: { title: 'Winter Semester', courseCode: 'COMPSCI 4TB3', courseName: '' }
    }
  };

  return (
    <div className="yearly-schedule-container">
      {Object.keys(courses).map((year, index) => (
        <div className="year-column" key={year}>
          <div className="year-title">
            <span>Year </span>
            <span>{index + 1}</span>
          </div>

          {/* Fall Semester */}
          <div className="semester-box">
            <div className="semester-title">{courses[year].fall.title}</div>
            {/* Add 4 white boxes with course name and course code inside the same box */}
            <div className="white-box">
              <div className="course-code">{courses[year].fall.courseCode}</div>
              <div className="course-name">{courses[year].fall.courseName}</div>
            </div>
            {Array(4).fill().map((_, i) => (
              <div className="white-box" key={`fall-box-${year}-${i + 1}`} />
            ))}
          </div>

          {/* Winter Semester */}
          <div className="semester-box">
            <div className="semester-title">{courses[year].winter.title}</div>
            {/* Add 4 white boxes with course name and course code inside the same box */}
            <div className="white-box">
              <div className="course-code">{courses[year].winter.courseCode}</div>
              <div className="course-name">{courses[year].winter.courseName}</div>
            </div>
            {Array(4).fill().map((_, i) => (
              <div className="white-box" key={`winter-box-${year}-${i + 1}`} />
            ))}
          </div>
        </div>
      ))}
    </div>
  );
}

export default YearlySchedule;
