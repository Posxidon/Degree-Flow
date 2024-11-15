import React from 'react';
import './FilterAppearance.css'; // Import the CSS for styling

function Filter() {
  const expandFilter = (filterid, icon) => {
    const filterDiv = document.getElementById(filterid);
    let options = [];
    const iconChange = icon.querySelector('.icon-change');

    if (filterid === 'department') {
      options = ['Anthropology',
        'Biochemistry and Biomedical Sciences',
        'Biology',
        'Chemical Engineering',
        'Chemistry & Chemical Biology',
        'Civil Engineering',
        'Classics',
        'Communication Studies and Media Arts',
        'Computing and Software',
        'Economics',
        'Electrical and Computer Engineering',
        'Engineering Physics',
        'English and Cultural Studies',
        'French',
        'Geography and Environmental Studies',
        'Health, Aging and Society',
        'History',
        'Indigenous Studies',
        'Kinesiology',
        'Labour Studies',
        'Linguistics and Languages',
        'Materials Science and Engineering',
        'Mathematics & Statistics',
        'Mechanical Engineering',
        'Music',
        'Philosophy',
        'Physics & Astronomy',
        'Political Science',
        'Psychology, Neuroscience & Behaviour',
        'Religious Studies',
        'Social Psychology',
        'Social Work',
        'Sociology',
        'Theatre and Film Studies',
        'Visual Arts'];
    } else if (filterid === 'terms') {
      options = ['Fall',
        'Winter',
        'Spring',
        'Summer'];
    } else if (filterid === 'difficulty') {
      options = ['Easy',
        'Medium',
        'Hard'];
    } else if (filterid === 'rating') {
      options = ['★',
        '★★',
        '★★★',
        '★★★★',
        '★★★★★'];
    }

    if (filterDiv.style.display === 'none') {
      filterDiv.style.display = 'grid'; // if display is hidden, set to block which will display
      iconChange.textContent = '−';
      if (!filterDiv.innerHTML) {
        options.forEach((option) => { // loops through all the options and creates new div element
          const optionDiv = document.createElement('div');

          const checkbox = document.createElement('span'); // span: used for check box
          checkbox.className = 'custom-checkbox';
          checkbox.id = option;
          checkbox.textContent = '';
          console.log('Checkbox created for:', option);

          checkbox.addEventListener('click', () => {
            if (checkbox.textContent === '✔') {
              checkbox.textContent = '✖';
              checkbox.classList.remove('checkbox-checkmark'); // Remove green style
              checkbox.classList.add('checkbox-x'); // Add red style
            } else if (checkbox.textContent === '') {
              checkbox.textContent = '✔';
              checkbox.classList.remove('checkbox-x');
              checkbox.classList.add('checkbox-checkmark');
            } else {
              checkbox.textContent = '';
            }
          });

          const optionlabel = document.createElement('label');
          optionlabel.textContent = option;
          optionlabel.style.marginLeft = '10px';
          optionDiv.appendChild(checkbox);
          optionDiv.appendChild(optionlabel);
          filterDiv.appendChild(optionDiv);
        });
      }
    } else {
      filterDiv.style.display = 'none';
      iconChange.textContent = '+';
    }
  };

  return (
    <div id="filters" className="filter">
      <h2>Filters</h2>
      <div className="filter-selection">
        <button type="button" className="filter-title" onClick={(e) => expandFilter('department', e.currentTarget)}>
          DEPARTMENT
          <span className="icon-change">+</span>
        </button>
        <div id="department" className="filter-subtitles" style={{ display: 'none' }} />

        <button type="button" className="filter-title" onClick={(e) => expandFilter('terms', e.currentTarget)}>
          TERMS
          <span className="icon-change">+</span>
        </button>
        <div id="terms" className="filter-subtitles" style={{ display: 'none' }} />

        <button type="button" className="filter-title" onClick={(e) => expandFilter('difficulty', e.currentTarget)}>
          DIFFICULTY
          <span className="icon-change">+</span>
        </button>
        <div id="difficulty" className="filter-subtitles" style={{ display: 'none' }} />

        <button type="button" className="filter-title" onClick={(e) => expandFilter('rating', e.currentTarget)}>
          RATING
          <span className="icon-change">+</span>
        </button>
        <div id="rating" className="filter-subtitles" style={{ display: 'none' }} />

      </div>
    </div>
  );
}
export default Filter;
