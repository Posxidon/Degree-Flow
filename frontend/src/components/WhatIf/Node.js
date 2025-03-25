import React, { useState } from 'react';
import './Node.css';

/* eslint-disable dot-notation */
function Node({ courseNode }) {
  const [isShown, setIsShown] = useState(false);
  return (
    <div
      className="courseNode"
      onMouseEnter={() => setIsShown(true)}
      onMouseLeave={() => setIsShown(false)}
    >
      {courseNode['courseCode']}
      {isShown && (
        <div className="desc-window">
          {courseNode['desc']}
        </div>
      )}
    </div>
  );
}
export default Node;
