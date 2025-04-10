import React, { useState } from 'react';
import axios from 'axios';
import { useAuth0 } from '@auth0/auth0-react';
import LegalPopUp from './LegalPopUp';
import TranscriptTable from './TranscriptTable';
import './UploadTranscript.css';

function UploadTranscript() {
  const [isPopUpVisible, setPopUpVisible] = useState(false);
  const [isLegalAgreed, setLegalAgreed] = useState(false);
  const [file, setFile] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [transcriptData, setTranscriptData] = useState(null);
  const [errorMessage, setErrorMessage] = useState('');
  const [programSummary, setProgramSummary] = useState(null);

  const { getAccessTokenSilently, isAuthenticated, user } = useAuth0();

  const handleUploadClick = () => setPopUpVisible(true);
  const handleConfirm = () => {
    setLegalAgreed(true);
    setPopUpVisible(false);
  };
  const handleCancel = () => setPopUpVisible(false);
  const handleFileChange = (e) => setFile(e.target.files[0]);

  const handleSubmit = async () => {
    if (!isLegalAgreed) {
      setErrorMessage('You must agree to the legal notice before uploading.');
      return;
    }

    if (!file) {
      setErrorMessage('Please select a file before submitting.');
      return;
    }

    if (!isAuthenticated) {
      setErrorMessage('You must be logged in to upload a transcript.');
      return;
    }

    setErrorMessage('');
    setIsLoading(true);

    try {
      const token = await getAccessTokenSilently({
        audience: 'https://degreeflow-backend/api',
        scope: 'read:data write:data'
      });

      const formData = new FormData();
      formData.append('transcript', file);
      formData.append('studentId', user.sub);

      const response = await axios.post('https://degreeflow-api-dnfnaqhababxdjg8.canadacentral-01.azurewebsites.net/api/transcripts/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${token}`
        }
      });

      setTranscriptData(response.data);
      // eslint-disable-next-line no-use-before-define
      calculateSummary(response.data);
    } catch (error) {
      console.error('Error uploading transcript:', error);
      setErrorMessage('Error uploading transcript. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  const calculateSummary = (data) => {
    if (!data || data.length === 0) return;

    const termToUnits = new Map();
    data.forEach((entry) => {
      const term = entry.term?.trim();
      const units = parseFloat(entry.units);
      if (term && units > 0 && !termToUnits.has(term)) {
        termToUnits.set(term, units);
      }
    });

    const totalCompleted = Array.from(termToUnits.values()).reduce((sum, units) => sum + units, 0);

    const fullProgram = [...data].reverse().find((entry) => entry.program)?.program || 'Unknown Program';
    const cleanedProgram = fullProgram.split('Course')[0].trim();
    const programLower = cleanedProgram.toLowerCase();

    let required = 120;
    if (programLower.includes('software')) required = 122;
    else if (programLower.includes('civil') || programLower.includes('mechanical')) required = 130;

    const remaining = Math.max(0, required - totalCompleted);

    setProgramSummary({
      program: cleanedProgram,
      completedUnits: totalCompleted,
      requiredUnits: required,
      remainingUnits: remaining
    });
  };

  return (
    <div className="transcript-upload">
      {isPopUpVisible && <LegalPopUp onConfirm={handleConfirm} onCancel={handleCancel} />}

      <h3 className="instruction-note">
        Ready to visualize your degree?
        <br />
        <strong>Begin by clicking &#34;Upload Transcript&#34; and follow the steps!</strong>
      </h3>

      <div className="upload-section">
        {/* eslint-disable-next-line react/button-has-type */}
        <button onClick={handleUploadClick}>Upload Transcript</button>
      </div>

      {isLegalAgreed && (
        <div className="file-upload-section">
          <input type="file" accept="application/pdf" onChange={handleFileChange} />
          {/* eslint-disable-next-line react/button-has-type */}
          <button onClick={handleSubmit}>Submit</button>
        </div>
      )}

      {isLoading && <p>Uploading and parsing transcript...</p>}
      {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}

      {programSummary && (
        <div className="program-next-gpa-wrapper">
          <div className="program-summary-box">
            <h2>Program Summary</h2>
            <p>
              <strong>Program:</strong>
              {' '}
              {programSummary.program}
            </p>
            <p>
              <strong>Completed Units:</strong>
              {' '}
              {programSummary.completedUnits}
            </p>
            <p>
              <strong>Required Units:</strong>
              {' '}
              {programSummary.requiredUnits}
            </p>
            <p>
              <strong>Remaining Units:</strong>
              {' '}
              {programSummary.remainingUnits}
            </p>
          </div>

          <div className="next-steps-box">
            <h3>How to Reach Your Goal</h3>
            <p>
              You still need
              {' '}
              <strong>{programSummary.remainingUnits}</strong>
              {' '}
              units to graduate.
            </p>
            <ul>
              <li>
                Head to
                {' '}
                <strong>What If</strong>
                {' '}
                to explore future term planning.
              </li>
              <li>
                Use
                {' '}
                <strong>Search Filtered Courses</strong>
                {' '}
                to find courses by year, department, difficulty rating, and more.
              </li>
              <li>
                Visit
                {' '}
                <strong>Generate Schedule</strong>
                {' '}
                to view your potential semester layout.
              </li>
              <li>
                Check
                {' '}
                <strong>Seat Alert</strong>
                {' '}
                to get notified when seats open up in full courses.
              </li>
            </ul>
            <p>You’re almost there—keep going!</p>
          </div>

          <TranscriptTable data={transcriptData || []} />
        </div>
      )}
    </div>
  );
}

export default UploadTranscript;
