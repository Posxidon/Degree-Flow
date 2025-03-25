import React from 'react';
import './CourseGroupNode.css';
import Node from './Node';
/* eslint-disable dot-notation */

function CourseGroupNode({ courseNodes }) {
  return (
    <div className="courseGroupNode">
      <div className="grp-title title">
        <p>
          {courseNodes['numReq']}
          &nbsp; is required for this course group
        </p>
      </div>
      <div className="courses">
        {courseNodes['courses'].map((courseNode) => (
          courseNode['courseCode'].includes('elective')
            ? <Node key={courseNode['id']} courseNode={courseNode} />
            : <Node key={courseNode['id']} courseNode={courseNode} />
        ))}
      </div>
    </div>
  );
}

export default CourseGroupNode;
