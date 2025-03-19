import React from 'react';
import Node from './Node';
/* eslint-disable dot-notation */
const password = 'ca5731bb-7cae-4cac-8fbc-7091df883b47';
function getJson(url) {
  return fetch(url, {
    method: 'GET',
    headers: {
      Authorization: `Basic ${btoa(`user:${password}`)}`
    }
  }).then((response) => response.json())
    .catch((error) => {
      console.error(error);
    });
}
function courseParse(json) {
  const keys = Object.keys(json);
  let i = 0;
  if (keys.length === 4) {
    let element = (
      <ul>
        <il>
          {json.id}
        </il>
        <il>
          {json.courseCode}
        </il>
      </ul>
    );
    if (Object.keys(json['prereqs']).length > 0) {
      for (i; i < Object.keys(json['prereqs']).length; i += 1) {
        const key = Object.keys(json['prereqs'])[i];
        // eslint-disable-next-line no-use-before-define
        element += treeTraverse(json['prereqs'][key]);
      }
    }
    return element;
  }
  return null;
}

function treeTraverse(json) {
  let i = 0;
  for (i = 0; i < Object.keys(json).length; i += 1) {
    const key = Object.keys(json)[i];
    console.log(key);
    if (key === 'reqCourses') {
      // eslint-disable-next-line prefer-destructuring
      const name = json['name'];

      let element = (
        // eslint-disable-next-line react/jsx-no-useless-fragment
        <>
          <ul>
            <il>
              {name}
            </il>
          </ul>
        </>
      );
      for (i = 0; i < Object.keys(json[key]).length; i += 1) {
        const elem = json[key][Object.keys(json[key])[i]];
        element += treeTraverse(elem);
      }
      return element;
    } if (key === 'courseGroup') {
      const courses = json[key];
      const keys = Object.keys(courses);
      if (keys.length > 1) {
        let element = (
          // eslint-disable-next-line react/jsx-no-useless-fragment
          <>
            <p>
              {json['numreq']}
            </p>
          </>
        );
        for (i = 0; i < keys.length; i += 1) {
          const k = keys[i];
          element += courseParse(courses[k]);
        }
        return (
          <ul>
            {element}
          </ul>
        );
      } if (keys.length === 1) {
        return courseParse(courses[keys[0]]);
      }
    }
  }
  return null;
}
// eslint-disable-next-line camelcase
function What_if() {
  const json = getJson('http://localhost:8080/api/degree');
  return json.then((result) => {
    console.log(result);
    const r = treeTraverse(result);
    console.log(r);
    return r;
  });
}
// eslint-disable-next-line camelcase
export default What_if;
