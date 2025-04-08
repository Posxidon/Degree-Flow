import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function GenerateSchedule() {
  const navigate = useNavigate();

  // Sample schedule JSON inserted directly as a variable
  const sampleSchedule = {
    "1": [
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Introduction to career readiness and job search; self-assessment, career planning and decision-making, networking, industry research, cover letter and resume writing, interviewing skills and work place professionalism.\nCross-list(s): IBEHS 1EE0\nPrerequisite(s): Registration in (list Eng programs)\nNot open to students in their final level.",
        "unit": 0,
        "name": "ENGINEER1EE0",
        "id": 80,
        "years": 1,
        "courseCode": "ENGINEER1EE0"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Introduction to safety guidelines at McMaster University, acceptable safety conduct and positive safety attitudes and practices in laboratories and Workplace Hazardous Materials Information System (WHMIS).\nThis course is evaluated on a Complete/Fail basis.\nWeb modules\nAntirequisite(s): ART 1HS0, ENGINEER 1A00, ENG TECH 1A00, NURSING 1A00, SCIENCE 1A00\nThis requirement must be completed prior to the start of the first lab. Students who fail the quiz must reattempt it and will not be permitted in any course with a lab component or any Level II ART course until the requirement has been successfully completed.",
        "unit": 0,
        "name": "WHMIS1A00",
        "id": 79,
        "years": 1,
        "courseCode": "WHMIS1A00"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "elective",
        "unit": 0,
        "name": "Electives1",
        "id": 78,
        "years": 1,
        "courseCode": "Electives1"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "elective",
        "unit": 0,
        "name": "Electives0",
        "id": 77,
        "years": 1,
        "courseCode": "Electives0"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Vector spaces given by solutions to linear systems. Linear independence, dimension. Determinants. Eigenvalues, eigenvectors and diagonalisation. Complex numbers.\nThree lectures, one tutorial, one lab; one term\nPrerequisite(s): One of Grade 12 Calculus and Vectors U, MATH 1F03, or credit or registration in MATH 1ZA3\nAntirequisite(s): MATH 1B03, 1ZZ5",
        "unit": 3,
        "name": "MATH1ZC3",
        "id": 76,
        "years": 1,
        "courseCode": "MATH1ZC3"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Techniques of integration, applications of definite integrals, differential equations, polar coordinates, parametrized curves. Sequences, infinite series, power series. Partial derivatives.\nThree lectures, one tutorial, one lab; one term\nPrerequisite(s): MATH 1ZA3\nAntirequisite(s): ARTSSCI 1D06 A/B, MATH 1AA3, 1LT3, 1N03, 1NN3, 1XX3, 1ZZ5\nNot open to students with credit or registration in ISCI 1A24 A/B.",
        "unit": 3,
        "name": "MATH1ZB3",
        "id": 75,
        "years": 1,
        "courseCode": "MATH1ZB3"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Functions: limits, continuity, derivatives, optimization, curve sketching. Antiderivative, definite integral, techniques of integration, with applications.\nThree lectures, one tutorial, one lab; one term\nPrerequisite(s): Registration in a program in Engineering\nAntirequisite(s): ARTSSCI 1D06 A/B, MATH 1A03, 1LS3, 1N03, 1NN3, 1X03, 1Z04\nNot open to students with credit or registration in ISCI 1A24 A/B.",
        "unit": 3,
        "name": "MATH1ZA3",
        "id": 74,
        "years": 1,
        "courseCode": "MATH1ZA3"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Introduction to different aspects of design: Identifying user needs, goals and desires and translating them into software, and structuring and communicating the structure of software to improve reliability, readability and adaptability. Topics include web languages and protocols, types and design patterns.\nTwo lectures, two labs (two hours each); second term; may be offered also in the first term\nPrerequisite(s): COMPSCI 1JC3 and registration in Computer Science 1, or COMPSCI 1JC3 with a result of at least B; COMPSCI 1MD3 or MATH 1MP3\nAntirequisite(s): COMPSCI 1XA3",
        "unit": 3,
        "name": "COMPSCI1XD3",
        "id": 72,
        "years": 1,
        "courseCode": "COMPSCI1XD3"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Acquiring familiarity with professional software development settings via practical experience with interaction with UNIX-like systems, programming in C, with documentation, testing, benchmarking, profiling and debugging; shell interaction and programming, pipes and filters; revision control.\nTwo lectures, two labs (two hours each); second term; may be offered also in the first term\nPrerequisite(s): One of the following:\n  • COMPSCI 1MD3 and registration in Computer Science 1\n  • One of COMPSCI 1MD3 or MATH 1MP3 with a result of at least B\n  • One of ENGINEER 1D04 or 1P13 A/B, or IBEHS 1P10 A/B with a result of at least B, and registration in Level II or above of an Engineering program other than Software Engineering, Mechatronics Engineering, Electrical Engineering, and Computer Engineering\nAntirequisite(s): COMPENG 2SH4, COMPSCI 1XA3, 2XA3, 2S03, MECHTRON 2MP3, SFWRENG 2MP3, 2S03, 2XA3, 2XC3",
        "unit": 3,
        "name": "COMPSCI1XC3",
        "id": 71,
        "years": 1,
        "courseCode": "COMPSCI1XC3"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Introduction to fundamental programming concepts: values and types, expressions and evaluation, control flow constructs and exceptions, recursion, input/output and file processing.\nThree lectures, one tutorial (one hour); first term; may be offered also in the second term (Computer Science students need to take this course in the first term)\nPrerequisite(s): One of MATH 1K03, 1LS3, Grade 12 Advanced Functions and Introductory Calculus U, Grade 12 Calculus and Vectors, or registration or credit in ARTSSCI 1D06\nAntirequisite(s): ENGINEER 1D04, 1P13 A/B, IBEHS 1P10 A/B, MATH 1MP3, PHYSICS 2G03",
        "unit": 3,
        "name": "COMPSCI1MD3",
        "id": 70,
        "years": 1,
        "courseCode": "COMPSCI1MD3"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Exploration of thinking that is inspired, supported, and enabled by computing. Survey of the salient ideas, methods, and technologies in the major areas of computing including basic data types, logic, operating systems, computer networking, web computing, information security, digital media, software development, and problem solving techniques. Introduction to the fundamentals of functional programming.\nThree lectures, one tutorial (two hours), first term; may be offered also in the second term (Computer Science students need to take this course in the first term)\nPrerequisite(s): One of MATH 1K03, Grade 12 Advanced Functions and Introductory Calculus U, Grade 12 Calculus and Vectors, or registration in Computer Science 1",
        "unit": 3,
        "name": "COMPSCI1JC3",
        "id": 69,
        "years": 1,
        "courseCode": "COMPSCI1JC3"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Sets, functions, relations, trees and graphs; counting principles, modular arithmetic, discrete probabilities; induction and recursion, recurrence relations.\nThree lectures, one tutorial (two hours), second term; may be offered also in the first term\nPrerequisite(s): One of the following:\n  • Registration in Computer Science 1 and one of MATH 1B03, 1ZC3\n  • One of MATH 1B03, 1ZC3 with a result of at least B\nAntirequisite(s): COMPSCI 1FC3, 2DM3, SFWRENG 2DM3, 2E03, 2F03",
        "unit": 3,
        "name": "COMPSCI1DM3",
        "id": 68,
        "years": 1,
        "courseCode": "COMPSCI1DM3"
      }
    ],
    "2": [
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Implementation of computational solutions to practical problems that combine algorithmic design and analysis with software design principles, through an experiential approach in simulated workplace environments. Communication skills: Technical documentation and presentation.\nTwo lectures, one lab (three hours), second term\nPrerequisite(s): COMPSCI 1XC3, 2C03, 2ME3\nAntirequisite(s): COMPSCI 2XB3, SFWRENG 2XB3",
        "unit": 3,
        "name": "COMPSCI2XC3",
        "id": 88,
        "years": 2,
        "courseCode": "COMPSCI2XC3"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Models of concurrency: process algebras, Petri nets, temporal logics and model checking; concurrency as software structuring principle: processes, threads, synchronization mechanisms, resource management and sharing; deadlock, safety and liveness; design, verification and testing of concurrent systems.\nThree lectures, one tutorial (two hours); second term; may be offered also in the first term\nPrerequisite(s): COMPSCI 2C03, 2LC3 or 2DM3, 2ME3\nCo-requisite(s): COMPSCI 2AC3\nAntirequisite(s): COMPSCI 3SD3, SFWRENG 3BB4",
        "unit": 3,
        "name": "COMPSCI2SD3",
        "id": 87,
        "years": 2,
        "courseCode": "COMPSCI2SD3"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Classes and inheritance, class invariants, interface specifications; object-oriented design patterns; exception handling; tools for interface documentation, testing, program analysis; requirements documentation; quality attributes; development models.\nThree lectures one tutorial (two hours); first term; may be offered also in the second term\nPrerequisite(s): \n  • COMPSCI 1XC3 or 1XD3, and registration in any Computer Science program (see Department Note 3 in the course listing) or in the Minor in Computer Science\nCo-requisite(s): COMPSCI 2LC3\nAntirequisite(s): SFWRENG 2AA4, SFWRENG 3K04, MECHTRON 3K04",
        "unit": 3,
        "name": "COMPSCI2ME3",
        "id": 86,
        "years": 2,
        "courseCode": "COMPSCI2ME3"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Introduction to logic and proof techniques for practical reasoning: propositional logic, predicate logic, structural induction; rigorous proofs in discrete mathematics and programming.\nThree lectures, one tutorial (two hours); first term; may be offered also in the second term\nPrerequisite(s): COMPSCI 1DM3, 1JC3; one of COMPSCI 1MD3, 1XC3, 1XD3, MATH 1MP3 and registration in Mathematics and Computer Science or, in any Computer Science program (see Department Note 3 in the course listing) or in the Minor in Computer Science.\nAntirequisite(s): COMPSCI 2DM3, SFWRENG 2DM3",
        "unit": 3,
        "name": "COMPSCI2LC3",
        "id": 85,
        "years": 2,
        "courseCode": "COMPSCI2LC3"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Introduction to logic gates, computer arithmetic, instruction-set architecture, assembly programming, translation of high-level languages into assembly. Computer system organization: datapath and control, pipelining, memory hierarchies, I/O systems; measures of performance.\nThree lectures, one tutorial, (one hour); first term; may be offered also in the second term\nPrerequisite(s): One of the following:\n  • COMPSCI 1XC3 and 1DM3 and registration in any Computer Science program (see Department Note 3 in the course listing) or in the Minor in Computer Science.\nAntirequisite(s): COMPENG 3DR4, 4DM4, SFWRENG 2GA3, 3GA3",
        "unit": 3,
        "name": "COMPSCI2GA3",
        "id": 84,
        "years": 2,
        "courseCode": "COMPSCI2GA3"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Data modelling, integrity constraints, principles and design of relational databases, relational algebra, SQL, query processing, transactions, concurrency control, recovery, security and data storage.\nThree lectures, one tutorial (one hour); second term; may be offered also in the first term\nPrerequisite(s): COMPSCI 2LC3 or COMPSCI 2DM3\nAntirequisite(s): COMPSCI 3DB3, 4DB3, SFWRENG 3DB3, 3H03, 4M03, 4DB3",
        "unit": 3,
        "name": "COMPSCI2DB3",
        "id": 83,
        "years": 2,
        "courseCode": "COMPSCI2DB3"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Basic data structures: stacks, queues, hash tables, and binary trees; searching and sorting; graph representations and algorithms, including minimum spanning trees, traversals, shortest paths; introduction to algorithmic design strategies; correctness and performance analysis.\nThree lectures, one tutorial (one hour); first term; may be offered also in the first term\nPrerequisite(s): \n  • COMPSCI 1DM3 or 2DM3; COMPSCI 1XC3 or 1XD3 or 1MD3 or MATH 1MP3, and registration in any Computer Science program (see Department Note 3 in the course listing) or in the Minor in Computer Science.\nAntirequisite(s): SFWRENG 2C03, COMPENG 3SM4",
        "unit": 3,
        "name": "COMPSCI2C03",
        "id": 82,
        "years": 2,
        "courseCode": "COMPSCI2C03"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Finite state machines, regular languages, regular expressions, applications of regular languages, grammars, context-free languages, models of computation, computability and decidability.\nThree lectures, one tutorial (two hours); second term; may be offered also in the first term\nPrerequisite(s): COMPSCI 2LC3, 2C03\nAntirequisite(s): COMPSCI 2FA3, 2MJ3, SFWRENG 2FA3",
        "unit": 3,
        "name": "COMPSCI2AC3",
        "id": 81,
        "years": 2,
        "courseCode": "COMPSCI2AC3"
      }
    ],
    "3": [
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "elective",
        "unit": 0,
        "name": "Electives3",
        "id": 101,
        "years": 3,
        "courseCode": "Electives3"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "elective",
        "unit": 0,
        "name": "Electives2",
        "id": 100,
        "years": 3,
        "courseCode": "Electives2"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "elective",
        "unit": 0,
        "name": "Computer Science Electives1",
        "id": 99,
        "years": 3,
        "courseCode": "Computer Science Electives1"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "elective",
        "unit": 0,
        "name": "Computer Science Electives0",
        "id": 98,
        "years": 3,
        "courseCode": "Computer Science Electives0"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Introduction to probability, data analysis, statistical inference, regression, correlation and analysis of variance.\nThree lectures; one term\nPrerequisite(s): Registration in Level II or above of a program in Engineering above Level I Computer Science, Computer Engineering, Electrical Engineering, Mechanical Engineering, Mechatronics or Software Engineering\nAntirequisite(s): STATS 3J04",
        "unit": 3,
        "name": "STATS3Y03",
        "id": 97,
        "years": 3,
        "courseCode": "STATS3Y03"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Lexical analysis, syntax analysis, type checking; syntax-directed translation, attribute grammars; compiler structure; implications of computer architecture; mapping of programming language concepts; code generation and optimization.\nThree lectures, one lab (two hours); second term\nPrerequisite(s): COMPSCI 2C03, and COMPSCI 2GA3, and COMPSCI 2AC3 or 2FA3, and COMPSCI 3MI3\nAntirequisites: COMPSCI 4TB3, SFWRENG 4TB3",
        "unit": 3,
        "name": "COMPSCI3TB3",
        "id": 95,
        "years": 3,
        "courseCode": "COMPSCI3TB3"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Processes and threads, synchronization and communication; scheduling, memory management; file systems; resource protection; structure of operating systems.\nTwo lectures, one tutorial, two labs (one hour each); first term\nPrerequisite(s): COMPSCI 2SD3 or 3SD3, COMPSCI 2C03, and COMPSCI 2GA3\nAntirequisite(s): SFWRENG 3SH3",
        "unit": 3,
        "name": "COMPSCI3SH3",
        "id": 94,
        "years": 3,
        "courseCode": "COMPSCI3SH3"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Physical networks, TCP/IP protocols, switching methods, network layering and components, network services. Information security, computer and network security threats, defence mechanisms, encryption.\nThree lectures, one tutorial (one hour); second term\nPrerequisite(s): Credit or registration in COMPSCI 3SH3\nAntirequisite(s): COMPSCI 3CN3, 3C03, COMPSCI 4C03, SFWRENG 4C03, COMPENG 4DK4, 4DN4",
        "unit": 3,
        "name": "COMPSCI3N03",
        "id": 93,
        "years": 3,
        "courseCode": "COMPSCI3N03"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Principles of definition of and reasoning about programming languages and domain-specific languages; use of semantics for interpretation and in program analyses for correctness, security and efficiency.\nThree lectures; one tutorial (one hour); first term\nPrerequisite(s): COMPSCI 2C03, and COMPSCI 2LC3 or 2DM3, and COMPSCI 2AC3 or 2FA3, and COMPSCI 2ME3",
        "unit": 3,
        "name": "COMPSCI3MI3",
        "id": 92,
        "years": 3,
        "courseCode": "COMPSCI3MI3"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Basic computability models; the Church-Turing thesis, complexity classes; P versus NP; NP-completeness, reduction techniques; algorithmic design strategies; flows, distributed algorithms, advanced techniques such as randomization.\nThree lectures, one tutorial (one hour), second term\nPrerequisite(s): COMPSCI 2C03 or SFWRENG 2C03, COMPSCI 2AC3 or 2FA3 or SFWRENG 2FA3",
        "unit": 3,
        "name": "COMPSCI3AC3",
        "id": 91,
        "years": 3,
        "courseCode": "COMPSCI3AC3"
      }
    ],
    "4": [
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "elective",
        "unit": 0,
        "name": "Electives7",
        "id": 110,
        "years": 4,
        "courseCode": "Electives7"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "elective",
        "unit": 0,
        "name": "Electives6",
        "id": 109,
        "years": 4,
        "courseCode": "Electives6"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "elective",
        "unit": 0,
        "name": "Electives5",
        "id": 108,
        "years": 4,
        "courseCode": "Electives5"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "elective",
        "unit": 0,
        "name": "Electives4",
        "id": 107,
        "years": 4,
        "courseCode": "Electives4"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "elective",
        "unit": 0,
        "name": "Computer Science Electives3",
        "id": 106,
        "years": 4,
        "courseCode": "Computer Science Electives3"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "elective",
        "unit": 0,
        "name": "Computer Science Electives2",
        "id": 105,
        "years": 4,
        "courseCode": "Computer Science Electives2"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Students, in teams of two to four students, undertake a substantial project in an area of computer science by performing each step of the software life cycle. The lecture component presents an introduction to software management and project management.\nLecture component in term 1, weekly tutorials; two terms\nPrerequisite(s): Registration in Level IV of an Honours Computer Science program, of an Honours Computer Science as a Second Degree (B.A.Sc.) program, or of the Combined Honours in Arts & Science and Computer Science program",
        "unit": 6,
        "name": "COMPSCI4ZP6B",
        "id": 102,
        "years": 4,
        "courseCode": "COMPSCI4ZP6B"
      },
      {
        "prereqs": [],
        "antireqs": [],
        "desc": "Minimum of 12 weeks of full-time employment in a professional environment.\nFirst or second term\nPrerequisite(s): Registration in a Co-op program in the Faculty of Engineering and ENGINEER 1EE0 and permission from the Engineering Co-Op and Career Services.\nNot open to students in their final level.",
        "unit": 0,
        "name": "ENGINEER2EC0",
        "id": 111,
        "years": 2,
        "courseCode": "ENGINEER2EC0"
      }
    ]
  };

  // We'll store the schedule object here
  const [scheduleByYear, setScheduleByYear] = useState({});
  const [selectedYear, setSelectedYear] = useState('1');

  // Instead of fetching from the backend, we load the sample schedule directly
  useEffect(() => {
    setScheduleByYear(sampleSchedule);
  }, []);

  // The courses for the selected year
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
        <p style={{ textAlign: 'center', marginBottom: '20px', fontSize: '16px', color: '#333' }}>
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
                <strong>{course.courseCode}</strong>: <strong>{course.name}</strong>
              </div>
              <div style={courseDetailsStyle}>
                {/* Dummy placeholders for course instructor and timings */}
                <p>
                  <strong>Instructor:</strong> TBA
                </p>
                <p>
                  <strong>Timings:</strong> TBA
                </p>
                <p>
                  <strong>Unit:</strong> {course.unit}
                </p>
                <p>
                  <strong>Description:</strong> {course.desc}
                </p>
                <p>
                  <strong>Years:</strong> {course.years}
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
