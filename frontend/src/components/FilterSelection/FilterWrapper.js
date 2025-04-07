import React from 'react';
import FilterSelection from './FilterSelection';
import FilterOptions from './FilterOptions';
import './FilterWrapper.css';

function FilterWrapper() {
  return (
    <div className="filter-course-container">
      <div className="filter-box-wrapper">
        <FilterSelection />
      </div>
      <div className="course-list-wrapper">
        <FilterOptions />
      </div>
    </div>
  );
}

export default FilterWrapper;
