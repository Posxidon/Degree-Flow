import React from 'react';
import './TranscriptTable.css';

function TranscriptTable({ data }) {
  return (
    <table className="transcript-table">
      <thead>
        <tr>
          <th>Course</th>
          <th>Term</th>
          <th>Grade</th>
          <th>GPA (Term Wise)</th>
          <th>Cumulative GPA</th>
        </tr>
      </thead>
      <tbody>
        {data.map((row, index) => (
          // eslint-disable-next-line react/no-array-index-key
          <tr key={index}>
            <td>{row.courseCode}</td>
            <td>{row.term}</td>
            <td>{row.grade}</td>
            <td>{row.gpa}</td>
            <td>{row.totalGpa}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

export default TranscriptTable;
