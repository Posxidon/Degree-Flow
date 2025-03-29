import React, { useState } from 'react';
import './What_if.css';
import Node from './Node';
import CourseGroupNode from './CourseGroupNode';

/* eslint-disable dot-notation */
/* eslint-disable no-use-before-define */
const password = '132be6ff-c823-4666-a34d-a39ad2a02b1c';
function courseGroupParse(json, data) {
  let i;
  let cData = data;
  const [courseHistory, courseDict, courseGroupHistory] = [cData['courseHistory'], cData['courseDict'], cData['courseGroupHistory']];
  const cgKeys = Object.keys(courseGroupHistory);
  for (i = 0; i < cgKeys.length; i += 1) {
    const ithEntry = courseGroupHistory[cgKeys[i]];
    if (ithEntry['courseGroupId'] === data['courseGroupId']) {
      // console.log('repeat course group');
      return { courseHistory, courseDict, courseGroupHistory };
    }
  }
  courseGroupHistory.push(json);
  const courseKeys = Object.keys(json['courses']);
  for (i = 0; i < courseKeys.length; i += 1) {
    cData = courseParse(json['courses'][courseKeys[i]], cData);
  }
  return cData;
}
function courseParse(json, data) {
  const [courseHistory, courseDict, courseGroupHistory] = [data['courseHistory'], data['courseDict'], data['courseGroupHistory']];
  let i = 0;
  for (i = 0; i < courseHistory.length; i += 1) {
    if (json['courseCode'] === courseHistory[i] && !(json['courseCode'] === 'elective')) {
      // console.log('repeat course');
      return { courseHistory, courseDict, courseGroupHistory };
    }
  }
  let hasYear = false;
  const cdKeys = Object.keys(courseDict);
  for (i = 0; i < cdKeys.length; i += 1) {
    if (json['years'].toString() === cdKeys[i]) {
      hasYear = true;
    }
  }
  if (hasYear) {
    courseDict[json['years']].push(json);
  } else {
    courseDict[json['years']] = [(json)];
  }
  courseHistory.push(json['courseCode']);
  return { courseHistory, courseDict, courseGroupHistory };
}

function treeTraverse(json) {
  let i;
  let v;
  let a;
  let reqGrps;
  let cg;
  let tData = { courseHistory: [], courseDict: {}, courseGroupHistory: [] };
  // eslint-disable-next-line prefer-destructuring
  const reqCourses = json['reqCourses'];
  for (i = 0; i < Object.keys(reqCourses).length; i += 1) {
    const k = Object.keys(reqCourses)[i];
    reqGrps = reqCourses[k]['reqGrp'];
    for (v = 0; v < Object.keys(reqGrps).length; v += 1) {
      cg = reqGrps[Object.keys(reqGrps)[v]]['courseGroups'];
      for (a = 0; a < Object.keys(cg).length; a += 1) {
        // console.log(cg[Object.keys(cg)[a]]);
        tData = courseGroupParse(cg[Object.keys(cg)[a]], tData);
      }
    }
  }
  return tData;
}

// eslint-disable-next-line camelcase
function WhatIf() {
  const [html, setHTML] = useState({ __html: {} });
  const [d, setData] = useState('HCOMPSCICO');
  const [inuse, setInuse] = useState(false);
  const url = 'http://localhost:8080/api/degree?';
  const handleClick = async () => {
    setInuse(true);
    try {
      const response = await (await fetch(url + new URLSearchParams({
        degreeName: d
      }), {
        method: 'GET',
        headers: {
          Authorization: `Basic ${btoa(`user:${password}`)}`
        }
      })).json();
      setHTML(response);
      console.log(JSON.stringify(response));
      setInuse(false);
    } catch (err) {
      console.log(err.message);
    }
  };
  console.log(html);
  let data = { courseHistory: [], courseDict: {}, courseGroupHistory: [] };
  let [courseHistory, courseDict, courseGroupHistory] = [data['courseHistory'], data['courseDict'], data['courseGroupHistory']];
  if (Object.keys(html).length > 1) {
    data = treeTraverse(html);
    [courseHistory, courseDict, courseGroupHistory] = [data['courseHistory'], data['courseDict'], data['courseGroupHistory']];
  }
  console.log(courseHistory);
  console.log(Object.keys(courseDict));
  console.log(courseGroupHistory.length);
  return (
    <div className="container">
      <div className="input-container">
        <input name="degreeName" onChange={(e) => setData(e.target.value)} className="submission-fld" />
        {/* eslint-disable-next-line jsx-a11y/control-has-associated-label,react/button-has-type */}
        {!inuse
          // eslint-disable-next-line react/button-has-type
          && <button onClick={() => handleClick()} className="submission-btn">submit</button>}
      </div>
      <div className="display-container">
        <p className="degree-name">{html['name']}</p>
        <div className="component-container">
          {courseGroupHistory.map((courseGroup) => (
            <CourseGroupNode courseNodes={courseGroup} />
          ))}
        </div>
        <div className="component-container">
          {Object.keys(courseDict).map((years) => (
            <div key={years} className="years">
              year :&nbsp;
              {years}
              {(courseDict[years]).map((course) => (
                course['courseCode'].includes('elective')
                  ? <Node key={course['id']} courseNode={course} />
                  : <Node key={course['id']} courseNode={course} />
              ))}
            </div>
          ))}
        </div>
      </div>

    </div>
  );
}

export default WhatIf;
