import React, { useState } from 'react';
import './CourseGroupNode.css';
import Node from './Node';
/* eslint-disable dot-notation */
/* eslint-disable react/jsx-no-bind */
/* eslint-disable prefer-destructuring */
function CourseGroupNode({ courseNodes, returnData, years }) {
  const [numReq, setNumReq] = useState(courseNodes['numReq']);
  const [courseNodeHis, setCourseNodeHis] = useState(courseNodes['courses']);
  // updates self based on child actions and passes params along to parent
  function handleChildData(year, course) {
    const courseCode = course['courseCode'];
    setNumReq(numReq - 1);
    const newCourseNodes = [];
    // remove child from self
    for (let i = 0; i < courseNodeHis.length; i += 1) {
      const cn = courseNodeHis[i];
      if (!(cn['courseCode'] === courseCode)) {
        newCourseNodes.push(cn);
      }
    }
    setCourseNodeHis(newCourseNodes);
    returnData(year, course);
  }
  // if self has child / requirements left then generate courses
  if (numReq > 0 && courseNodeHis.length > 0) {
    return (
      <div className="courseGroupNode">
        <div className="grp-title title">
          <p>
            {numReq}
            &nbsp; is required for this course group
          </p>
        </div>
        {/* generate courses */}
        <div className="courses">
          {courseNodeHis.map((courseNode) => (
            <Node key={courseNode['id']} courseNode={courseNode} sendParentData={handleChildData} years={years} />
          ))}
        </div>
      </div>
    );
  }
  return (<div />);
}

export default CourseGroupNode;
