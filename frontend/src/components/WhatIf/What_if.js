import React, { useEffect, useState } from 'react';
import './What_if.css';
import Node from './Node';
import CourseGroupNode from './CourseGroupNode';

/* eslint-disable dot-notation */
/* eslint-disable no-use-before-define */
const password = 'f109918d-a036-4eba-ba75-bfd36b2dca34';

function courseGroupParse(json, data) {
  let i;
  let v;
  let cData = data;
  const [courseHistory, courseDict, courseGroupHistory] = [cData['courseHistory'], cData['courseDict'], cData['courseGroupHistory']];
  const jsonKeys = Object.keys(json);
  for (v = 0; v < jsonKeys.length; v += 1) {
    const vthGrp = json[jsonKeys[v]];
    if (Object.keys(vthGrp).length === 4) {
      const cgKeys = Object.keys(courseGroupHistory);
      for (i = 0; i < cgKeys.length; i += 1) {
        const ithEntry = courseGroupHistory[cgKeys[i]];
        if (ithEntry['courseGroupId'] === vthGrp['courseGroupId']) {
          // console.log('repeat course group');
          return { courseHistory, courseDict, courseGroupHistory };
        }
      }
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
function What_if() {
  const [html, setHTML] = useState({ __html: {} });
  const [d, setData] = useState('HCOMPSCICO');
  const url = 'http://localhost:8080/api/degree?';
  const handleClick = async () => {
    try {
      const response = await (await fetch(url + new URLSearchParams({
        degreeName: d
      }), {
        method: 'GET',
        headers: {
          Authorization: `Basic ${btoa(`user:${password}`)}`
        }
      })).json();
      console.log(response);
      setHTML(response);
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
    <>
      <input name="degreeName" onChange={(e) => setData(e.target.value)} />
      {/* eslint-disable-next-line jsx-a11y/control-has-associated-label,react/button-has-type */}
      <button onClick={() => handleClick()}>submit</button>
      <div className="what-if">
        {courseGroupHistory.map((courseGroup) => (
          <CourseGroupNode courseNodes={courseGroup} />
        ))}
        <div>
          {Object.keys(courseDict).map((years) => (
            <div key={years} className="years">
              year :
              {years}
              {(courseDict[years]).map((course) => (
                course['courseCode'].includes('elective')
                  ? <Node key={course['courseCode']} courseCode={course['courseCode']} />
                  : <Node key={course['courseCode'].concat(years)} courseCode={course['courseCode']} />
              ))}
            </div>
          ))}
        </div>
      </div>
      <p>{html['name']}</p>
    </>
  );
}

// eslint-disable-next-line camelcase
export default What_if;
