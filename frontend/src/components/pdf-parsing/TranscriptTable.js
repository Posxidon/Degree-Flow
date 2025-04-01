import React from 'react';
import './UploadTranscript.css';

function TranscriptTable({ data }) {
  if (!data || data.length === 0) {
    return <p>No transcript data available.</p>;
  }

  return (
    <div className="table-container">
      <h2>Parsed Transcript</h2>
      <table>
        <thead>
          <tr>
            <th>Course</th>
            <th>Term</th>
            <th>Grade</th>
            <th>GPA</th>
          </tr>
        </thead>
        <tbody>
          {data.map((record) => {
            const {
              course = '', term = '', grade = '', gpa = ''
            } = record;
            const key = `${course}-${term}-${grade}`;
            return (
              <tr key={key}>
                <td>{course}</td>
                <td>{term}</td>
                <td>{grade}</td>
                <td>{gpa}</td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}

export default TranscriptTable;
