import React, { useEffect, useState } from 'react';
import './What_if.css';
import Node from './Node';
import CourseGroupNode from './CourseGroupNode';

/* eslint-disable dot-notation */
/* eslint-disable no-use-before-define */
/* eslint-disable react/jsx-no-bind */
/* eslint-disable jsx-a11y/control-has-associated-label,react/button-has-type */
const password = '69e30346-d450-4b4b-9337-ebf9fcdd3178';

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

function degreeTableGen(name, code) {
  const table = {};
  for (let i = 0; i < name.length; i += 1) {
    table[code[i]] = name[i];
  }
  return table;
}
// eslint-disable-next-line camelcase
function WhatIf() {
  const [html, setHTML] = useState({ __html: {} });
  const [degrees, setDegrees] = useState([]);
  const [degreeTable, setDegreeTable] = useState({});
  const [degree, setDegree] = useState('HCOMPSCICO');
  const [inuse, setInuse] = useState(false);
  const [showDegree, setShowDegree] = useState(false);
  const [fetched, setFetched] = useState(true);
  const [fetching, setFetching] = useState(true);
  const [yearData, setYearData] = useState({
    1: [], 2: [], 3: [], 4: []
  });
  const [data, setData] = useState({ courseHistory: [], courseDict: {}, courseGroupHistory: [] });
  const url = 'http://localhost:8080/api/degree/requirement?';
  const degreeUrl = 'http://localhost:8080/api/degree/degreeName?';
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
  const handleClickDegree = async () => {
    console.log('degree status');
    console.log(fetched);
    setShowDegree(!showDegree);
    if (!fetched) {
      console.log('fetched');
      return;
    }
    setFetched(false);
    console.log('requesting Degree');
    try {
      const response = await (await fetch(degreeUrl, {
        method: 'GET',
        headers: {
          Authorization: `Basic ${btoa(`user:${password}`)}`
        }
      })).json();
      console.log('degree response');
      console.log(response);
      setDegrees(response);
      console.log('success');
    } catch (err) {
      console.log(err.message);
    }
  };
  useEffect(() => {
    console.log('degrees retrieved');
    console.log(degrees);
    if (degrees.length === 2) {
      setDegreeTable(degreeTableGen(degrees['1'], degrees['0']));
    }
  }, [degrees]);
  useEffect(() => {
    setFetching(false);
  }, [degreeTable]);
  useEffect(() => {
    console.log(degree);
  }, [degree]);
  return (
    <div className="container">
      <div className="input-container">
        <button name="degreeName" onClick={() => handleClickDegree()} className="submission-fld">
          Select degree
        </button>
        {showDegree && (
          <div className="degree-dropdown">
            {Object.keys(degreeTable).map((k) => (
              <button
                key={k}
                onClick={
                function () {
                  // console.log(k);
                  setDegree(k);
                }
              }
              >
                {degreeTable[k]}
              </button>
            ))}
          </div>
        )}
        {!inuse
          && <button onClick={() => handleClick()} className="submission-btn">submit</button>}
      </div>
      <div className="display-container">
        <p className="degree-name">{html['name']}</p>
        {!inuse
        && (
        <div className="component-container">
          {data['courseGroupHistory'].map((courseGroup) => (
            <CourseGroupNode
              courseNodes={courseGroup}
              returnData={handleChildData}
              years={Object.keys(data['courseDict'])}
            />
          ))}
        </div>
        )}
        <div className="component-container">
          {Object.keys(yearData).length > 0 && Object.keys(yearData).map((years) => (
            <div key={years} className="years">
              <p>
                year :&nbsp;
                {years}
              </p>
              <p>
                courseload :&nbsp;
                {Object.keys(yearData[years]).length}
              </p>
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
