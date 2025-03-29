import React from 'react';
import { useNavigate } from 'react-router-dom';
import Typewriter from 'react-simple-typewriter';
import './HomePage.css';

function HomePage() {
  const navigate = useNavigate();

  return (
    <div className="homepage">
      <div className="hero">
        <h1>
          <Typewriter
            words={['Personalized Course Guidance', 'Academic Advising at Your Fingertips']}
            loop
            cursor
            typeSpeed={80}
            deleteSpeed={40}
            delaySpeed={1500}
          />
        </h1>
        <p className="subtext">
          Plan, track, and optimize your degree path with confidence.
        </p>
        {/* eslint-disable-next-line react/button-has-type */}
        <button onClick={() => navigate('/dashboard')}>Launch DegreeFlow</button>
      </div>

      <div className="features-section">
        <h2>What is DegreeFlow?</h2>
        <p>
          DegreeFlow is a smart academic planner built for students at McMaster University.
          {/* eslint-disable-next-line max-len */}
          Whether you are pursuing a minor, planning electives, or double-checking graduation eligibility, DegreeFlow helps you visualize your entire academic journey in one place.
        </p>

        <div className="features-grid">
          <div className="feature-box">
            <h3> Visual Progress Tracker</h3>
            <p>
              {/* eslint-disable-next-line max-len */}
              Instantly see how many units you have completed across core, technical, and elective courses.
              Stay on top of what’s left to graduate — no guesswork required.
            </p>
          </div>

          <div className="feature-box">
            <h3> Upload Your Transcript</h3>
            <p>
              {/* eslint-disable-next-line max-len */}
              Just upload your Mosaic transcript and we’ll auto-generate a timeline of your completed courses.
              No manual entry — it&#39;s quick and simple.
            </p>
          </div>

          <div className="feature-box">
            <h3> Conflict-Free Schedule Builder</h3>
            <p>
              Plan your upcoming semester with real-time seat alerts and auto-conflict detection.
              No more switching tabs or second-guessing your timetable.
            </p>
          </div>

          <div className="feature-box">
            <h3> Seat Alerts</h3>
            <p>
              Don’t miss out on full courses. Set alerts and get notified when a seat opens up.
              You’ll be the first to know — and the first to register.
            </p>
          </div>
        </div>
      </div>

      <div className="call-to-action">
        <h2>Ready to take control of your degree?</h2>
        {/* eslint-disable-next-line react/button-has-type */}
        <button onClick={() => navigate('/dashboard')}>Get Started Now</button>
      </div>
    </div>
  );
}

export default HomePage;
