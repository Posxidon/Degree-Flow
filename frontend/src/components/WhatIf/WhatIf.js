import React, { useEffect, useState } from 'react';
import './What_if.css';
import Node from './Node';
import dropdown from './dropdown.png';
import loading from './loading.gif';
import CourseGroupNode from './CourseGroupNode';

/* eslint-disable dot-notation */
/* eslint-disable no-use-before-define */
/* eslint-disable react/jsx-no-bind */
/* eslint-disable jsx-a11y/control-has-associated-label,react/button-has-type */
const password = 'e509c377-3231-4e35-a50f-67723ab45566';

/**
 * parses course group and all its given courses
 * @param json - json file to be parsed
 * @param data - data object to be updated from its courses and course group
 * @returns {{courseHistory: [], courseDict: {}, courseGroupHistory: []}}
 * - updated course history, course dictionary and course group history
 */
function courseGroupParse(json, data) {
  let i;
  let cData = data;
  const [courseHistory, courseDict, courseGroupHistory] = [cData['courseHistory'], cData['courseDict'], cData['courseGroupHistory']];
  const cgKeys = Object.keys(courseGroupHistory);
  // if the course group is in the course group history then skip it
  for (i = 0; i < cgKeys.length; i += 1) {
    const ithEntry = courseGroupHistory[cgKeys[i]];
    if (ithEntry['courseGroupId'] === data['courseGroupId']) {
      return { courseHistory, courseDict, courseGroupHistory };
    }
  }
  courseGroupHistory.push(json);
  // traverse within its courses and update the data
  const courseKeys = Object.keys(json['courses']);
  for (i = 0; i < courseKeys.length; i += 1) {
    cData = courseParse(json['courses'][courseKeys[i]], cData);
  }
  return cData;
}

/**
 * parses course
 * @param json - json file to be parsed
 * @param data - data object to be updated from the course
 * @returns {{courseHistory: [], courseDict: {}, courseGroupHistory: []}}
 * - updated course history and course dictionary
 */
function courseParse(json, data) {
  const [courseHistory, courseDict, courseGroupHistory] = [data['courseHistory'], data['courseDict'], data['courseGroupHistory']];
  let i = 0;
  // if the course is in the course history then skip it
  for (i = 0; i < courseHistory.length; i += 1) {
    if (json['courseCode'] === courseHistory[i] && !(json['courseCode'] === 'elective')) {
      return { courseHistory, courseDict, courseGroupHistory };
    }
  }
  // update the course dictionary with its corresponding year and update course history
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

/**
 * root call to traverse the json file
 * @param json - json file to be parsed
 * @returns {{courseHistory: *[], courseDict: {}, courseGroupHistory: *[]}}
 * - course history contains all unique courses within the requirements
 * - course dictionary contains a reference of courses and their corresponding levels / years
 * - course group history contains all unique course groups
 */
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

/**
 * generates a paired reference between all degree name and code
 * @param name - list of all degree names
 * @param code - list of all degree codes
 * @returns {{}} - object where the key is the code and value is the name
 */
function degreeTableGen(name, code) {
  const table = {};
  for (let i = 0; i < name.length; i += 1) {
    table[code[i]] = name[i];
  }
  return table;
}

/**
 * generates an empty table representing year schedule
 * @param years - how many years to generate
 * @returns {{}} - empty schedule where key is year and value is empty list
 */
function yearDataGen(years) {
  const table = {};
  for (let i = 0; i < years; i += 1) {
    table[i + 1] = [];
  }
  return table;
}

function WhatIf() {
  const [html, setHTML] = useState({ __html: {} });
  const [degrees, setDegrees] = useState([]);
  const [degreeTable, setDegreeTable] = useState({});
  const [degree, setDegree] = useState('');
  const [inuse, setInuse] = useState(false);
  const [showDegree, setShowDegree] = useState(false);
  const [fetched, setFetched] = useState(true);
  const [fetching, setFetching] = useState(true);
  const [maxYear, setMaxYear] = useState(4);
  const [yearData, setYearData] = useState({});
  const [data, setData] = useState({ courseHistory: [], courseDict: {}, courseGroupHistory: [] });
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const url = 'http://localhost:8080/api/degree/requirement?';
  const degreeUrl = 'http://localhost:8080/api/degree/degreeName?';
  // for updating self using data from node objects
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
  // for requesting degree requirement
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
      console.log('success');
      setError('');
    } catch (err) {
      console.log(err.message);
      setError('Failed to get degree data, please try again.');
    }
  };
  // triggers parsing when degree requirement received
  useEffect(() => {
    console.log('parsing');
    console.log(html);
    try {
      setData(treeTraverse(html));
    } catch (err) {
      console.log(err.message);
      setError('Sorry, we cannot provide information for this degree');
    }
  }, [html]);
  // triggers updating self when parsing complete
  useEffect(() => {
    console.log('on change');
    console.log(data);
    if (Object.keys(data['courseDict']).length > 0) {
      console.log('creating new year data');
      const max = Object.keys(data['courseDict']).reduce((a, b) => Math.max(parseInt(a, 10), parseInt(b, 10)));
      setMaxYear(parseInt(max, 10));
      setYearData(yearDataGen(parseInt(max, 10)));
    }
    setInuse(false);
  }, [data]);
  // for requesting list of undergrad engineering degrees
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
    setError('');
    try {
      const response = await (await fetch(degreeUrl, {
        method: 'GET',
        headers: {
          Authorization: `Basic ${btoa(`user:${password}`)}`
        }
      })).json();
      setDegrees(response);
      console.log('success');
    } catch (err) {
      console.log(err.message);
      setError('Failure to get list of degree, please try again');
    }
  };
  // triggers when degrees are retrieved, generate a reference between degree name and codes
  useEffect(() => {
    console.log('degrees retrieved');
    console.log(degrees);
    if (degrees.length === 2) {
      setDegreeTable(degreeTableGen(degrees['1'], degrees['0']));
    }
  }, [degrees]);
  // triggers reference is generated
  useEffect(() => {
    setFetching(false);
  }, [degreeTable]);
  // triggers when degree is updated
  useEffect(() => {
    console.log(degree);
  }, [degree]);
  // triggers when yearly schedule is updated
  useEffect(() => {
    console.log(yearData);
  }, [yearData]);
  // resets locks when error occurs
  useEffect(() => {
    setInuse(false);
    setFetched(true);
    setSubmitting(false);
  }, [error]);
  const handleSubmit = async () => {
    console.log('posting');
    console.log(JSON.stringify(yearData));
    setSubmitting(true);
    try {
      const resp = await fetch('http://localhost:8080/api/degree/addSchedule', {
        method: 'POST',
        Authorization: `Basic ${btoa(`user:${password}`)}`,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(yearData)
      });
      if (!resp.ok) {
        throw new Error('Course not offered in this term.');
      }
      console.log('posted');
      setSubmitting(false);
    } catch (err) {
      setError('failed to submit please try again');
    }
  };
  return (
    <div className="container">
      {/* error message */}
      {error.length > 0
        && (
        <div className="error-msg">
          {error}
        </div>
        )}
      {/* on toggle dropdown table that selects degree */}
      <div className="input-container">
        {/* calls api and toggles dropdown */}
        <button name="degreeName" onClick={() => handleClickDegree()} className="submission-fld">
          {Object.keys(degreeTable).includes(degree)
            ? degreeTable[degree]
            : 'Select Degree' }
          <img src={dropdown} alt="select courses" className="icon" />
        </button>
        {/* if dropdown is toggled then show list of all degrees */}
        {showDegree && (
          <div className="dropdown">
            {/* maps all degree table code to button and set display name as the name */}
            {(fetching || Object.keys(degreeTable).length === 0)
              ? (<p>loading</p>)
              : (Object.keys(degreeTable).map((k) => (
                <button
                  key={k}
                  className="option"
                  onClick={
                  function () {
                    setShowDegree(false);
                    setDegree(k);
                  }
                }
                >
                  {degreeTable[k]}
                </button>
              )))}
          </div>
        )}
        {/* submission button */}
        {/* calls mosaic api to get degree */}
        {!inuse
          ? <button onClick={() => handleClick()} className="submission-btn">submit</button>
          : <img src={loading} alt="loading" className="loading" />}
      </div>
      {/* container for course groups and schedule */}
      <div className="display-container">
        {/* title / program name */}
        {!inuse
          ? (<p className="degree-name">{html['name']}</p>)
          : <p className="degree-name"> Loading </p> }
        {/* course group component */}
        {/* hides if degree is loading */}
        {!inuse
        && (
        <div className="component-container">
          {/* generate all unqiue course group */}
          {data['courseGroupHistory'].map((courseGroup) => (
            <CourseGroupNode
              courseNodes={courseGroup}
              returnData={handleChildData}
              years={Object.keys(yearData)}
            />
          ))}
        </div>
        )}
        {/* schedule component */}
        {/* hides if it is loading degree or there are no yearly schedule info */}
        {!(inuse || Object.keys(yearData).length === 0)
          && (
          <div className="component-container">
            {/* add a new year to year data */}
            <button onClick={
              function () {
                const newYearData = {};
                for (let i = 0; i < Object.keys(yearData).length; i += 1) {
                  newYearData[Object.keys(yearData)[i]] = yearData[Object.keys(yearData)[i]];
                }
                newYearData[Object.keys(yearData).length + 1] = [];
                setYearData(newYearData);
              }
            }
            >
              add year
            </button>
            {/* generate year schedule */}
            {Object.keys(yearData).length > 0 && Object.keys(yearData).map((years) => (
              <div key={years} className="years">
                {/* add delete year button only if the year is after the minimum year required */}
                {parseInt(years, 10) > maxYear
                && (
                  <button onClick={
                    // delete year function
                    // moves all courses from deleted year to previous year
                    // shift all courses after deleted year one down
                    function () {
                      const newYearData = {};
                      for (let i = 0; i < Object.keys(yearData).length; i += 1) {
                        const y = Object.keys(yearData)[i];
                        if (parseInt(years, 10) > parseInt(y, 10)) {
                          newYearData[y] = yearData[y];
                        } else if (parseInt(years, 10) === parseInt(y, 10)) {
                          const prevY = Object.keys(yearData)[i - 1];
                          newYearData[prevY] = yearData[prevY].concat(yearData[y]);
                        } else {
                          const prevY = Object.keys(yearData)[i - 1];
                          newYearData[prevY] = yearData[y];
                        }
                      }
                      console.log('new year data');
                      console.log(newYearData);
                      setYearData(newYearData);
                    }
                  }
                  >
                    delete year
                  </button>
                )}
                <p>
                  year :&nbsp;
                  {years}
                </p>
                <p>
                  courseload :&nbsp;
                  {Object.keys(yearData[years]).length}
                </p>
                {/* generate all courses in schedule based on year data */}
                {(yearData[years]).map((course) => (
                  <Node key={course['id']} courseNode={course} sendParentData={handleChildData} years={Object.keys(yearData)} />
                ))}
              </div>
            ))}
            {!submitting
              && (
              <button onClick={() => handleSubmit()}>
                submit schedule
              </button>
              )}
          </div>
          )}
      </div>
    </div>
  );
}

export default WhatIf;
