import React, { useState } from 'react';
import './Node.css';
import icon from './info.png';

/* eslint-disable dot-notation */
/* eslint-disable max-len */
/* eslint-disable jsx-a11y/click-events-have-key-events,jsx-a11y/no-noninteractive-element-interactions,react/button-has-type */
function Node({ courseNode, sendParentData, years }) {
  const [isShown, setIsShown] = useState(false);
  const [showDropdown, setShowDropdown] = useState(false);
  // when user chooses a year, notifies parent
  function returnData(year) {
    sendParentData(year, courseNode);
  }
  return (
    <div
      className="courseNode"
    >
      {/* toggles course description */}
      {/* eslint-disable-next-line jsx-a11y/control-has-associated-label,react/button-has-type */}
      <button
        className="info-btn"
        onMouseDown={() => setIsShown(!isShown)}
      >
        <img src={icon} alt="course information" className="info-icon" />
      </button>
      {courseNode['courseCode']}
      <div>
        {/* toggles dropdown */}
        <button
          onClick={
          function () {
            setShowDropdown(!showDropdown);
          }
        }
          className="add-year"
        >
          Add to year
        </button>
        {/* dropdown menu that shows which year you can move the course to */}
        {showDropdown && (
          <div className="year-options">
            {/* only show years that are greater than the year this course is offered in */}
            {/* aka only move this course to upper years */}
            {years.map((yearK) => (
              parseInt(courseNode['years'], 10) <= parseInt(yearK, 10)
              && (
              <button
                onClick={
                function () {
                  setShowDropdown(!showDropdown);
                  returnData(yearK);
                }
              }
                className="year-option"
              >
                {yearK}
              </button>
              )
            ))}
          </div>
        )}
      </div>
      {/* shows course description */}
      {isShown && (
        <div className="desc-window">
          {courseNode['desc']}
        </div>
      )}
    </div>
  );
}
export default Node;
