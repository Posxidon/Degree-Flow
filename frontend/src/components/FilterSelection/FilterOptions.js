import React, { useState } from 'react';
import './FilterOptions.css'; // Import the CSS for styling

function Options() {
  const [expandedCourses, setExpandedCourses] = useState({}); // Store states for each courses

  const courses = [
    {
      id: 'COMPSCI 1MD3',
      course_name: 'Introduction to Computer Science',
      description:
        'An introduction to fundamental concepts in computer science, including algorithms, data structures, and programming principles.',
      semester: ['Fall'],
      Prerequisite: ['None'],
      Antirequisite: ['None']
    },
    {
      id: 'COMPSCI 2C03',
      course_name: 'Data Structures and Algorithms',
      description:
        'Study of data structures such as lists, stacks, queues, trees, and graphs, along with associated algorithms and their analysis.',
      semester: ['Winter'],
      Prerequisite: ['COMPSCI 1MD3'],
      Antirequisite: ['None']
    },
    {
      id: 'ECON 2G03',
      course_name: 'Intermediate Microeconomics',
      description:
        'Analysis of consumer behavior, production, and market structures using mathematical models.',
      semester: ['Winter'],
      Prerequisite: ['ECON 1B03', 'MATH 1A03'],
      Antirequisite: ['None']
    },
    {
      id: 'COMPSCI 2ME3',
      course_name: 'Introduction to Software Development',
      description:
        'Covers principles of software development, including design, implementation, and testing of software systems.',
      semester: ['Fall'],
      Prerequisite: ['COMPSCI 1MD3'],
      Antirequisite: ['None']
    },
    {
      id: 'COMPSCI 3MI3',
      course_name: 'Principles of Programming Languages',
      description:
        'Explores the design and implementation of programming languages, including syntax, semantics, and paradigms.',
      semester: ['Winter'],
      Prerequisite: ['COMPSCI 2ME3'],
      Antirequisite: ['None']
    },
    {
      id: 'COMPSCI 4ZP6',
      course_name: 'Capstone Project',
      description:
        'A team-based project course where students design and implement a substantial software system.',
      semester: ['Fall', 'Winter'],
      Prerequisite: ['COMPSCI 3MI3'],
      Antirequisite: ['None']
    },
    {
      id: 'ECON 3G03',
      course_name: 'Econometrics',
      description:
        'Introduction to statistical methods used in economic research, including regression analysis and hypothesis testing.',
      semester: ['Winter'],
      Prerequisite: ['ECON 2B03'],
      Antirequisite: ['None']
    },
    {
      id: 'ECON 4T03',
      course_name: 'International Trade Theory',
      description:
        'Study of the causes and consequences of international trade and trade policy.',
      semester: ['Fall'],
      Prerequisite: ['ECON 3G03'],
      Antirequisite: ['None']
    },
    {
      id: 'BIOLOGY 2C03',
      course_name: 'Genetics',
      description:
        'Principles of heredity, gene structure and function, and genetic analysis.',
      semester: ['Fall'],
      Prerequisite: ['BIOLOGY 1A03'],
      Antirequisite: ['None']
    },
    {
      id: 'BIOLOGY 3UU3',
      course_name: 'Molecular Biology',
      description:
        'Study of molecular mechanisms in biological processes, including DNA replication, transcription, and translation.',
      semester: ['Winter'],
      Prerequisite: ['BIOLOGY 2C03'],
      Antirequisite: ['None']
    },
    {
      id: 'BIOLOGY 4Z03',
      course_name: 'Advanced Topics in Cell Biology',
      description:
        'In-depth exploration of current research topics in cell biology.',
      semester: ['Fall'],
      Prerequisite: ['BIOLOGY 3UU3'],
      Antirequisite: ['None']
    },
    {
      id: 'PSYCH 2AP3',
      course_name: 'Abnormal Psychology',
      description:
        'Examination of psychological disorders, their diagnosis, and treatment.',
      semester: ['Fall'],
      Prerequisite: ['PSYCH 1X03'],
      Antirequisite: ['None']
    },
    {
      id: 'PSYCH 3CC3',
      course_name: 'Cognitive Neuroscience',
      description:
        'Study of the neural mechanisms underlying cognitive processes.',
      semester: ['Winter'],
      Prerequisite: ['PSYCH 2AP3'],
      Antirequisite: ['None']
    },
    {
      id: 'PSYCH 4D03',
      course_name: 'Advanced Research Methods',
      description:
        'Advanced techniques in psychological research design and analysis.',
      semester: ['Fall'],
      Prerequisite: ['PSYCH 3CC3'],
      Antirequisite: ['None']
    },
    {
      id: 'ECON 4T03',
      course_name: 'International Trade Theory',
      description:
        'Study of the causes and consequences of international trade and trade policy.',
      semester: ['Fall'],
      Prerequisite: ['ECON 3G03'],
      Antirequisite: ['None']
    },
    {
      id: 'PHYSICS 3BB3',
      course_name: 'Quantum Mechanics I',
      description:
        'Introduction to the fundamental principles of quantum mechanics, including wave-particle duality, the Schrödinger equation, and applications to simple systems.',
      semester: ['Winter'],
      Prerequisite: ['PHYSICS 2B03', 'MATH 2X03'],
      Antirequisite: ['None']
    }
  ];

  const toggleCourse = (courseId) => {
    setExpandedCourses((prevState) => ({
      ...prevState,
      [courseId]: !prevState[courseId]
    }));
  };

  return (
    <div className="course">
      <div className="courses-options">
        {courses.map((course) => (
          <div key={course.id} className="course-box">
            <button
              type="button"
              className="course-title"
              onClick={() => toggleCourse(course.id)}
            >
              {`${course.id} - ${course.course_name}`}
              <span className="icon-change2">
                {expandedCourses[course.id] ? '−' : '+'}
              </span>
            </button>
            {expandedCourses[course.id] && (
              <div className="course-details">
                <p>
                  <strong>Description: </strong>
                  {course.description}
                </p>
                <p>
                  <strong>Semester: </strong>
                  {course.semester.join(', ')}
                </p>
                <p>
                  <strong>Prerequisite: </strong>
                  {course.Prerequisite.join(', ')}
                </p>
                <p>
                  <strong>Antirequisite: </strong>
                  {course.Antirequisite.join(', ')}
                </p>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}

export default Options;
