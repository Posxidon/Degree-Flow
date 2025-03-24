import React, { useState } from 'react';
import './Node.css';

/* eslint-disable dot-notation */
function Node({ courseCode }) {
  // const [isShown, setIsShown] = useState(false);
  return (
    <div
      className="courseNode"
      // onMouseEnter={() => setIsShown(true)}
      // onMouseLeave={() => setIsShown(false)}
    >
      {courseCode}
    </div>
  );
}
export default Node;
