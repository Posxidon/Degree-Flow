import React, { useEffect, useState } from 'react';
import './What_if.css';
import Node from './Node';
import CourseGroupNode from './CourseGroupNode';

/* eslint-disable dot-notation */
/* eslint-disable no-use-before-define */
/* eslint-disable react/jsx-no-bind */
const password = 'f1f2ec88-ff8e-453e-b564-c11dfc38593f';

function courseGroupParse(json, data) {
  let i;
  let cData = data;
  const [courseHistory, courseDict, courseGroupHistory] = [cData['courseHistory'], cData['courseDict'], cData['courseGroupHistory']];
  const cgKeys = Object.keys(courseGroupHistory);
  for (i = 0; i < cgKeys.length; i += 1) {
    const ithEntry = courseGroupHistory[cgKeys[i]];
    if (ithEntry['courseGroupId'] === data['courseGroupId']) {
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
  if (!(json === null || json === undefined)) {
    if (Object.keys(json).length > 1) {
      // eslint-disable-next-line prefer-destructuring
      const reqCourses = json['reqCourses'];
      for (i = 0; i < Object.keys(reqCourses).length; i += 1) {
        const k = Object.keys(reqCourses)[i];
        reqGrps = reqCourses[k]['reqGrp'];
        for (v = 0; v < Object.keys(reqGrps).length; v += 1) {
          cg = reqGrps[Object.keys(reqGrps)[v]]['courseGroups'];
          for (a = 0; a < Object.keys(cg).length; a += 1) {
            tData = courseGroupParse(cg[Object.keys(cg)[a]], tData);
          }
        }
      }
    }
  }
  return tData;
}

// eslint-disable-next-line camelcase
function WhatIf() {
  const [html, setHTML] = useState({ __html: {} });
  const [degree, setDegree] = useState('HCOMPSCICO');
  const [inuse, setInuse] = useState(false);
  const [yearData, setYearData] = useState({
    1: [], 2: [], 3: [], 4: []
  });
  const [data, setData] = useState({ courseHistory: [], courseDict: {}, courseGroupHistory: [] });
  const url = 'http://localhost:8080/api/degree?';
  function handleChildData(yearNum, course) {
    const newYearData = {};
    for (let i = 0; i < Object.keys(yearData).length; i += 1) {
      const year = yearData[Object.keys(yearData)[i]];
      const newYear = [];
      console.log('year');
      console.log(year);
      for (let j = 0; j < year.length; j += 1) {
        if (!(year[j]['courseCode'] === course['courseCode'])) {
          newYear.push(year[j]);
        }
      }
      newYearData[Object.keys(yearData)[i]] = newYear;
    }
    if (Object.keys(newYearData).includes(yearNum)) {
      newYearData[yearNum].push(course);
    } else {
      newYearData[yearNum] = course;
    }
    setYearData(newYearData);
  }
  const handleClick = async () => {
    console.log('requesting');
    setInuse(true);
    try {
      const response = await (await fetch(url + new URLSearchParams({
        degreeName: degree
      }), {
        method: 'GET',
        headers: {
          Authorization: `Basic ${btoa(`user:${password}`)}`
        }
      })).json();
      setHTML(response);
      setYearData({
        1: [], 2: [], 3: [], 4: []
      });
      console.log('success');
    } catch (err) {
      console.log(err.message);
    }
  };
  useEffect(() => {
    console.log('parsing');
    console.log(html);
    setData(treeTraverse(html));
  }, [html]);
  useEffect(() => {
    console.log('on change');
    console.log(data);
    setInuse(false);
  }, [data]);
  if (inuse) {
    return (
      <p>
        loading
      </p>
    );
  }
  return (
    <div className="container">
      <div className="input-container">
        <input name="degreeName" onChange={(e) => setDegree(e.target.value)} className="submission-fld" />
        {/* eslint-disable-next-line jsx-a11y/control-has-associated-label,react/button-has-type */}
        {!inuse
          // eslint-disable-next-line react/button-has-type
          && <button onClick={() => handleClick()} className="submission-btn">submit</button>}
      </div>
      <div className="display-container">
        <p className="degree-name">{html['name']}</p>
        <div className="component-container">
          {data['courseGroupHistory'].map((courseGroup) => (
            <CourseGroupNode
              courseNodes={courseGroup}
              returnData={handleChildData}
              years={Object.keys(data['courseDict'])}
            />
          ))}
        </div>
        <div className="component-container">
          {Object.keys(yearData).length > 0 && Object.keys(yearData).map((years) => (
            <div key={years} className="years">
              year :&nbsp;
              {years}
              {(yearData[years]).map((course) => (
                course['courseCode'].includes('elective')
                  ? <Node key={course['id']} courseNode={course} sendParentData={handleChildData} years={Object.keys(data['courseDict'])} />
                  : <Node key={course['id']} courseNode={course} sendParentData={handleChildData} years={Object.keys(data['courseDict'])} />
              ))}
            </div>
          ))}
        </div>
      </div>

    </div>
  );
}

export default WhatIf;
