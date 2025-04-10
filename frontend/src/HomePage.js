import React, { useEffect } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import Typewriter from 'react-simple-typewriter';
import './HomePage.css';

function HomePage() {
  const {
    loginWithRedirect, isAuthenticated, isLoading, getAccessTokenSilently
  } = useAuth0();

  useEffect(() => {
    const checkUserRole = async () => {
      if (isAuthenticated) {
        try {
          const token = await getAccessTokenSilently({
            audience: 'https://degreeflow-backend/api',
            scope: 'read:data write:data'
          });

          const response = await fetch('https://degreeflow-api-dnfnaqhababxdjg8.canadacentral-01.azurewebsites.net/api/protected', {
            headers: { Authorization: `Bearer ${token}` }
          });

          if (response.ok) {
            const data = await response.json();
            console.log('User data:', data);
          }
        } catch (error) {
          console.error('Error fetching user data:', error);
        }
      }
    };

    checkUserRole();
  }, [isAuthenticated, getAccessTokenSilently]);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return (
    <div className="homepage">
      <div className="hero">
        <h1>
          <Typewriter
            words={[
              'Personalized Course Guidance',
              'Academic Advising at Your Fingertips'
            ]}
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
        {!isAuthenticated && (
        // eslint-disable-next-line react/button-has-type
          <button
            onClick={() => loginWithRedirect({
              redirectUri: `${window.location.origin}/dashboard`, // Redirect to dashboard after login
              audience: 'https://degreeflow-backend/api',
              scope: 'read:data write:data'
            })}
          >
            Launch DegreeFlow
          </button>
        )}
      </div>

      <div className="features-section">
        <h2>What is DegreeFlow?</h2>
        <p>
          DegreeFlow is your intelligent academic co-pilot, purpose-built for McMaster students.
          Whether you are planning a minor, cross-checking electives, or racing toward graduation,
          {/* eslint-disable-next-line max-len */}
          DegreeFlow gives you a complete, personalized overview of your degree journey — all in one place.
        </p>

        <div className="features-grid">
          <div className="feature-box">
            <h3>Visual Progress Tracker</h3>
            <p>
              Instantly view completed units across core, technical, and electives.
              Say goodbye to spreadsheet headaches — know exactly where you stand.
            </p>
          </div>

          <div className="feature-box">
            <h3>Transcript Upload</h3>
            <p>
              Upload your Mosaic transcript and let DegreeFlow auto-map your courses.
              No manual typing, no stress. Just insights.
            </p>
          </div>

          <div className="feature-box">
            <h3>Smart Schedule Builder</h3>
            <p>
              Plan your term with live seat availability and conflict-free course selection.
              Visualize your semester before it starts.
            </p>
          </div>

          <div className="feature-box">
            <h3>Seat Alerts</h3>
            <p>
              Get notified the moment a seat opens in your desired course.
              Act fast, stay ahead, and never miss your chance again.
            </p>
          </div>
        </div>

        <div className="extra-info">
          <h2>Why Choose DegreeFlow?</h2>
          <p>
            {/* eslint-disable-next-line max-len */}
            Traditional tools are clunky, outdated, and often overwhelming. DegreeFlow was built with students in mind — clean, modern, and intuitive. We believe that every student should have the tools they need to succeed, right at their fingertips.
            {/* eslint-disable-next-line max-len */}
            Whether you are in your first year trying to understand your requirements or nearing graduation double-checking your remaining credits, DegreeFlow supports your journey at every step.
          </p>
          <br />
          <p className="disclaimer-note">
            <strong>Note:</strong>
            {/* eslint-disable-next-line max-len */}
            DegreeFlow is a supplementary tool for academic planning. It does not replace official academic advising.
            {/* eslint-disable-next-line max-len */}
            For personalized academic advice, please consult your faculty’s academic advising office:&nbsp;
            <a
              href="https://www.eng.mcmaster.ca/current-students/undergraduate-academic-advising/"
              target="_blank"
              rel="noopener noreferrer"
            >
              Academic Advising – McMaster Engineering
            </a>
            .
          </p>
        </div>
      </div>
    </div>
  );
}

export default HomePage;
