import React, { useEffect, useState } from 'react';
import Node from './Node';
import './What_if.css';

/* eslint-disable dot-notation */
/* eslint-disable no-use-before-define */
const password = '2ac9b9a0-4aec-4107-85c0-0a5316141982';

function combineLst(d1, d2) {
  return d1.filter((x) => d2.includes(x))
    .concat(d2.filter((x) => !d1.includes(x)))
    .concat(d1.filter((x) => !d2.includes(x)));
}

function updateData(d1, d2) {
  let i;
  const courseDict = {};
  const CH1 = d1['courseHistory'];
  const CH2 = d2['courseHistory'];
  const CD1 = d1['courseDict'];
  const CD2 = d2['courseDict'];
  const CG1 = d1['courseGroupHistory'];
  const CG2 = d2['courseGroupHistory'];

  const courseHistory = combineLst(CH1, CH2);
  const courseGroupHistory = combineLst(CD1, CD2);
  const sameYears = Object.keys(CD1)
    .filter((x) => Object.keys(CD2).includes(x));
  const dYears1 = Object.keys(CD1)
    .filter((x) => !Object.keys(CD2).includes(x));
  const dYears2 = Object.keys(CD2)
    .filter((x) => !Object.keys(CD1).includes(x));
  for (i = 0; i < sameYears.length; i += 1) {
    courseDict[i] = combineLst(CG1[i], CG2[i]);
  }
  for (i = 0; i < dYears1.length; i += 1) {
    courseDict[i] = combineLst(CG1[i], CG2[i]);
  }
  for (i = 0; i < dYears2.length; i += 1) {
    courseDict[i] = combineLst(CG2[i], CG1[i]);
  }
  return { courseHistory, courseDict, courseGroupHistory };
}

function courseGroupParse(json, data) {
  const [courseHistory, courseDict, courseGroupHistory] = [data['courseHistory'], data['courseDict'], data['courseGroupHistory']];
  let i;
  let v;
  let cData = data;
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
  const keys = Object.keys(json);
  let i = 0;
  if (keys.length === 5) {
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
    // console.log(hasYear);
    // console.log(courseDict);
    courseHistory.push(json['courseCode']);
    // if (Object.keys(json['prereqs']).length > 0) {
    //   for (i = 0; i < Object.keys(json['prereqs']).length; i += 1) {
    //     const key = Object.keys(json['prereqs'])[i];
    //     treeTraverse(json['prereqs'][key]);
    //   }
    // }
  }
  return { courseHistory, courseDict, courseGroupHistory };
}

function treeTraverse(json) {
  let i;
  let v;
  let a;
  let reqGrps;
  let cg;
  const iData = { courseHistory: [], courseDict: {}, courseGroupHistory: [] };
  let tData;
  // eslint-disable-next-line prefer-destructuring
  const reqCourses = json['reqCourses'];
  for (i = 0; i < Object.keys(reqCourses).length; i += 1) {
    const k = Object.keys(reqCourses)[i];
    reqGrps = reqCourses[k]['reqGrp'];
    for (v = 0; v < Object.keys(reqGrps).length; v += 1) {
      cg = reqGrps[Object.keys(reqGrps)[v]]['courseGroups'];
      for (a = 0; a < Object.keys(cg).length; a += 1) {
        // console.log(cg[Object.keys(cg)[a]]);
        tData = courseGroupParse(cg[Object.keys(cg)[a]], iData);
      }
    }
  }
  return tData;
}

// eslint-disable-next-line camelcase
function What_if() {
  const [html, setHTML] = useState({ __html: {} });
  const url = 'http://localhost:8080/api/degree';
  useEffect(() => {
    async function createMarkup() {
      const response = await fetch(url, {
        method: 'GET',
        headers: {
          Authorization: `Basic ${btoa(`user:${password}`)}`
        }
      });
      const backendHtmlString = await response.text();
      return { __html: backendHtmlString };
    }
    createMarkup().then(
      (result) => {
        setHTML(result);
      }
    );
  }, []);
  console.log(html['__html']);
  let resp;
  if (typeof html['__html'] === 'string') {
    resp = JSON.parse(html['__html']);
    console.log('response');
    // console.log(resp);
    const data = treeTraverse(resp);
    const [courseHistory, courseDict, courseGroupHistory] = [data['courseHistory'], data['courseDict'], data['courseGroupHistory']];
    console.log(courseHistory);
    console.log(Object.keys(courseDict));
    console.log(courseGroupHistory.length);
    return (
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
    );
  }
  return <div />;
}

// eslint-disable-next-line camelcase
export default What_if;
