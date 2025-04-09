import React from 'react';
import UnitTracker from './UnitTracker';
import './UnitTracker.css';

function UnitTrackerSection() {
  return (
    <div className="unit-tracker-container">
      <h3 className="unit-tracker-title">Overall Progress</h3>
      <UnitTracker label="Overall Degree Progress" progress={72} total={120} />
      <UnitTracker label="Required Courses" progress={36} total={72} />
      <UnitTracker label="Technical Courses" progress={12} total={18} />
      <UnitTracker label="Elective Courses" progress={24} total={30} />
    </div>
  );
}

export default UnitTrackerSection;
