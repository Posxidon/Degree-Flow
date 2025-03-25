import React, { useState } from 'react';
import './FilterAppearance.css';
import CourseApiService from '../../services/CourseApiService';

function Filter() {
  const [expandedSections, setExpandedSections] = useState({
    department: false,
    'course-level': false
  });

  // Get subject codes and course levels directly from the service
  const subjectCodes = CourseApiService.getSubjectCodes();
  const courseLevels = CourseApiService.getCourseLevels().map((level) => ({
    display: `Level ${level}`,
    value: level
  }));

  const [selectedSubject, setSelectedSubject] = useState(null);
  const [selectedLevel, setSelectedLevel] = useState(null);

  // Toggles a filter section open/closed
  const toggleSection = (sectionId) => {
    setExpandedSections((prev) => ({
      ...prev,
      [sectionId]: !prev[sectionId]
    }));
  };

  // Handle checkbox selection for a filter option
  const handleCheckboxSelect = (filterId, value) => {
    let updatedValue = null;
    let filterType = '';

    if (filterId === 'department') {
      // If the same subject is clicked again, deselect it
      updatedValue = selectedSubject === value ? null : value;
      setSelectedSubject(updatedValue);
      filterType = 'subjectCode';
    } else if (filterId === 'course-level') {
      // If the same level is clicked again, deselect it
      updatedValue = selectedLevel === value ? null : value;
      setSelectedLevel(updatedValue);
      filterType = 'level';
    }

    // Dispatch an event to notify other components
    const filterChangeEvent = new CustomEvent('filterChange', {
      detail: {
        type: filterType,
        value: updatedValue
      }
    });
    document.dispatchEvent(filterChangeEvent);
  };

  // Render the filter options for a specific filter
  const renderFilterOptions = (filterId) => {
    if (!expandedSections[filterId]) {
      return null;
    }

    let options = [];
    let selectedValue = null;

    if (filterId === 'department') {
      options = subjectCodes;
      selectedValue = selectedSubject;
    } else if (filterId === 'course-level') {
      options = courseLevels;
      selectedValue = selectedLevel;
    }

    return (
      <div className="filter-subtitles">
        {options.map((option) => {
          const optionValue = typeof option === 'object' ? option.value : option;
          const displayText = typeof option === 'object' ? option.display : option;
          const isSelected = selectedValue === optionValue;

          // Create a unique key that doesn't rely on index because of the eslint thing
          const uniqueKey = `${filterId}-${optionValue}`;

          return (
            <div key={uniqueKey} className="option-div">
              <div className="checkbox-wrapper">
                <span
                  className={`custom-checkbox ${isSelected ? 'checkbox-checkmark' : ''}`}
                  onClick={() => handleCheckboxSelect(filterId, optionValue)}
                  onKeyDown={(e) => {
                    if (e.key === 'Enter' || e.key === ' ') {
                      e.preventDefault();
                      handleCheckboxSelect(filterId, optionValue);
                    }
                  }}
                  role="checkbox"
                  aria-checked={isSelected}
                  tabIndex={0}
                >
                  {isSelected ? '✔' : ''}
                </span>
                <div>{displayText}</div>
              </div>
            </div>
          );
        })}
      </div>
    );
  };

  return (
    <div id="filters" className="filter">
      <h2>Filters</h2>
      <div className="filter-selection">
        <button
          type="button"
          className="filter-title"
          onClick={() => toggleSection('department')}
        >
          SUBJECT CODE
          <span className="icon-change">
            {expandedSections.department ? '−' : '+'}
          </span>
        </button>
        {renderFilterOptions('department')}

        <button
          type="button"
          className="filter-title"
          onClick={() => toggleSection('course-level')}
        >
          COURSE LEVEL
          <span className="icon-change">
            {expandedSections['course-level'] ? '−' : '+'}
          </span>
        </button>
        {renderFilterOptions('course-level')}
      </div>
    </div>
  );
}

export default Filter;
