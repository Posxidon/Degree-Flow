import React from 'react';
import './Node.css';

function Node({ courseCode }) {
  return (
    <div className="courseNode">
      {courseCode}
    </div>
  );
}
export default Node;
