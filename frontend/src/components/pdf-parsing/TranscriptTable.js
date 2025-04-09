import React from 'react';
import './TranscriptTable.css';

function TranscriptTable({ data }) {
  if (!data || data.length === 0) return null;

  const gpa12 = parseFloat(data[0]?.totalGpa || 0);

  const convertTo4Scale = (gpa) => {
    // Placeholder: Replace with server call if needed
    const mapping = {
      12: 4.0,
      11: 3.9,
      10: 3.7,
      9: 3.3,
      8: 3.0,
      7: 2.7,
      6: 2.3,
      5: 2.0,
      4: 1.7,
      3: 1.3,
      2: 1.0,
      1: 0.7,
      0: 0.0
    };
    const rounded = Math.round(gpa);
    return mapping[rounded] || 0.0;
  };

  const gpa4 = convertTo4Scale(gpa12);

  return (
    <>
      <div className="gpa-summary-text">
        <p>
          Your parsed transcript is shown below.
          <br />
          Your overall GPA is
          {' '}
          <strong>{gpa12.toFixed(2)}</strong>
          {' '}
          on McMaster’s 12-point scale,
          which converts to
          {' '}
          <strong>{gpa4.toFixed(2)}</strong>
          {' '}
          on the 4.0 scale.
        </p>
      </div>

      <div className="transcript-summary-wrapper">
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
      </div>
    </>
  );
}

export default TranscriptTable;
