import React, { useState } from 'react';
import './CourseGroupNode.css';
import Node from './Node';
/* eslint-disable dot-notation */
/* eslint-disable react/jsx-no-bind */
function CourseGroupNode({ courseNodes, returnData, years }) {
  const [numReq, setNumReq] = useState(courseNodes['numReq']);
  const [courseNodeHis, setCourseNodeHis] = useState(courseNodes['courses']);
  function handleChildData(year, course) {
    // eslint-disable-next-line prefer-destructuring
    const courseCode = course['courseCode'];
    setNumReq(numReq - 1);
    const newCourseNodes = [];
    for (let i = 0; i < courseNodeHis.length; i += 1) {
      const cn = courseNodeHis[i];
      if (!(cn['courseCode'] === courseCode)) {
        newCourseNodes.push(cn);
      }
    }
    setCourseNodeHis(newCourseNodes);
    returnData(year, course);
  }
  if (numReq > 0 && courseNodeHis.length > 0) {
    return (
      <div className="courseGroupNode">
        <div className="grp-title title">
          <p>
            {numReq}
            &nbsp; is required for this course group
          </p>
        </div>
        <div className="courses">
          {courseNodeHis.map((courseNode) => (
            courseNode['courseCode'].includes('elective')
              ? <Node key={courseNode['id']} courseNode={courseNode} sendParentData={handleChildData} years={years} />
              : <Node key={courseNode['id']} courseNode={courseNode} sendParentData={handleChildData} years={years} />
          ))}
        </div>
      </div>
    );
  }
  return (<div />);
}

export default CourseGroupNode;
