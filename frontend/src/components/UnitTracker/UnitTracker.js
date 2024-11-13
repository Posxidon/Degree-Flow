import React from 'react';
import './UnitTracker.css';

function UnitTracker({ label, progress, total }) {
  const percentage = (progress / total) * 100;

  return (
    <div className="unit-tracker">
      <div className="unit-label">
        {label}
        {': '}
        {progress}
        {' / '}
        {total}
        {' units'}
      </div>
      <div className="progress-bar">
        <div
          className="progress-fill"
          style={{ width: `${percentage}%` }}
        />
      </div>
    </div>
  );
}

export default UnitTracker;
