import React from 'react';

function getJson() {
  return fetch('http://localhost:8080/api/degree')
  .then((response) => response.json())
  .catch((error) => {
    console.error(error);
  });
}

function courseParse(json){
  let keys = Object.keys(json);
  if(keys.length===4){
    let element = (json) = (
        <>
        <ul>
          <il>
            {json["id"]}
          </il>
          <il>
            {json["courseCode"]}
          </il>
        </ul>
        </>
    )
    if(Object.keys(json["prereqs"]).length>0){
      for(let key in json["prereqs"]){
        element = element + treeTraverse(json["prereqs"][key]);
      }
    }
    return element;
  }

}

function treeTraverse(json) {
  for (let key in json) {
    if (key === "reqCourses") {
      let name = json["name"];
      let element = (name) = (
          <>
            <ul>
              <il>
                {name}
              </il>
            </ul>
          </>
      );
      for (let elem of json[key]) {
        element = element + treeTraverse(elem);
      }
      return element;
    }else if(key === "courseGroup") {
      let courses = json[key];
      let keys = Object.keys(courses);
      if (keys.length>1){
        let element =(json)=  (
            <>
              <p>
                {json["numreq"]}
              </p>
            </>
        )
        for (key of keys){
          element = element + courseParse(courses[key]);
        }
        return (element) = (
            <>
              <ul>
                {element}
              </ul>
            </>
        );
      }else if(keys.length=== 1){
        return courseParse(courses[keys[0]]);
      }else{
        return null;
      }
    }
  }
}
function What_if(){
  let json = getJson("http://localhost:8080/api/degree");
  return treeTraverse(json);
}
export default What_if;