import React, { useState } from 'react';
import axios from 'axios';
import LegalPopUp from './LegalPopUp';
import TranscriptTable from './TranscriptTable';
import './UploadTranscript.css';

function UploadTranscript() {
  const [isPopUpVisible, setPopUpVisible] = useState(false);
  const [file, setFile] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [transcriptData, setTranscriptData] = useState(null);
  const [errorMessage, setErrorMessage] = useState('');

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleUploadClick = () => {
    setPopUpVisible(true);
  };

  const handleConfirm = () => {
    setPopUpVisible(false);
  };

  const handleCancel = () => {
    setPopUpVisible(false);
  };

  const handleSubmit = async () => {
    if (!file) {
      setErrorMessage('Please select a file before submitting.');
      return;
    }

    setErrorMessage('');
    setIsLoading(true);
    const formData = new FormData();
    formData.append('transcript', file);
    // For demonstration, we include a fixed studentId.
    formData.append('studentId', 'student123');

    try {
      const response = await axios.post('http://localhost:8080/api/transcripts/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      // Optionally, fetch the transcript data back from backend.
      // For now, we simply display the success message.
      setTranscriptData([{
        course: 'Dummy Course', term: 'Dummy Term', grade: 'A', gpa: '4.0'
      }]);
      // In your real implementation, you might fetch the parsed transcript details.
    } catch (error) {
      console.error('Error uploading transcript:', error);
      setErrorMessage('Error uploading transcript. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="transcript-upload">
      {isPopUpVisible && <LegalPopUp onConfirm={handleConfirm} onCancel={handleCancel} />}
      <div className="upload-section">
        {/* eslint-disable-next-line react/button-has-type */}
        <button onClick={handleUploadClick}>Upload Transcript</button>
      </div>
      <div className="file-upload-section">
        <input type="file" accept="application/pdf" onChange={handleFileChange} />
        {/* eslint-disable-next-line react/button-has-type */}
        <button onClick={handleSubmit}>Submit</button>
      </div>
      {isLoading && <p>Uploading and parsing transcript...</p>}
      {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
      <div className="table-container">
        <TranscriptTable data={transcriptData || []} />
      </div>
    </div>
  );
}

export default UploadTranscript;
