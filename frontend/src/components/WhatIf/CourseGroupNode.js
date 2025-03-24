import React from 'react';
import './CourseGroupNode.css';
import Node from './Node';
/* eslint-disable dot-notation */

function CourseGroupNode({ courseNodes }) {
  return (
    <div className="courseGroupNode">
      <p>
        {courseNodes['numReq']}
        is required for this course group
      </p>
      {courseNodes['courses'].map((courseNode) => (
        courseNode['courseCode'].includes('elective')
          ? <Node key={courseNode['courseCode'].concat(courseNode['years'])} courseCode={courseNode['courseCode'].concat(courseNode['years'])} />
          : <Node key={courseNode['courseCode']} courseCode={courseNode['courseCode']} />
      ))}
    </div>
  );
}

export default CourseGroupNode;
