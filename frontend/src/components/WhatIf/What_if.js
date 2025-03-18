import React from 'react';
import Node from './Node';


function getJson(url) {
  return fetch(url, { method: 'GET' }).then((response) => response.json())
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
    if (Object.keys(json.get('prereqs')).length > 0) {
      for (i; i < Object.keys(json.get('prereqs')).length; i += 1) {
        const key = Object.keys(json.get('prereqs'))[i];
        element += treeTraverse(json.get('prereqs').get(key));
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
      const name = json.get('name');
      let element = (
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
    } else if (key === 'courseGroup') {
      const courses = json[key];
      const keys = Object.keys(courses);
      if (keys.length > 1) {
        let element = (
          <>
            <p>
              {json.get('numreq')}
            </p>
          </>
        );
        for (i = 0; i < keys.length; i += 1) {
          const k = keys[i];
          element += courseParse(courses[k]);
        }
        return (
          <>
            <ul>
              {element}
            </ul>
          </>
        );
      } else if (keys.length === 1) {
        return courseParse(courses[keys[0]]);
      }
    }
  }
  return null;
}
function What_if() {
  const json = getJson('http://localhost:8080/api/degree');
  return treeTraverse(json);
}
export default What_if;
