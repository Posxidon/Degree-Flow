import React, { useState, useEffect } from 'react';
import './FilterOptions.css';
import { useAuth0 } from '@auth0/auth0-react';
import CourseDifficultyRating from '../CourseDifficultyRating/CourseDifficultyRating';
import CourseApiService from '../../services/CourseApiService';

function Options() {
  const [expandedCourses, setExpandedCourses] = useState({});
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(false);
  const [filtersApplied, setFiltersApplied] = useState(false);
  const [selectedFilters, setSelectedFilters] = useState({
    subjectCode: null,
    level: null
  });

  const {
    getAccessTokenSilently,
    isAuthenticated,
    isLoading: authLoading,
    user
  } = useAuth0();

  const [userEmail, setUserEmail] = useState('');

  // Check authentication status and set user email
  useEffect(() => {
    if (!authLoading) {
      if (isAuthenticated && user) {
        // User is logged in, set email from user object
        setUserEmail(user.email || '');
      } else {
        // User is not logged in
        setUserEmail('');
      }
    }
  }, [authLoading, isAuthenticated, user]);

  // Helper function to extract prerequisites from description
  const extractPrerequisites = (description) => {
    if (!description) return ['None'];
    const match = description.match(/Prerequisite\(s\):(.*?)(?=Antirequisite|$)/s);
    if (match && match[1]) {
      return [match[1].trim()];
    }
    return ['None'];
  };

  // Helper function to extract antirequisites from description
  const extractAntirequisites = (description) => {
    if (!description) return ['None'];
    const match = description.match(/Antirequisite\(s\):(.*?)(?=$)/s);
    if (match && match[1]) {
      return [match[1].trim()];
    }
    return ['None'];
  };

  // Helper function to clean description (remove prerequisites and antirequisites)
  const cleanDescription = (description) => {
    if (!description) return 'No description available';

    // Remove prerequisite and antirequisite sections
    let cleanedDesc = description.split(/Prerequisite\(s\):/)[0].trim();

    // Format the description with line breaks where appropriate
    cleanedDesc = cleanedDesc.replace(/\.\s+(?=[A-Z])/g, '.\n\n');

    return cleanedDesc;
  };

  // Define fetch courses function
  const fetchCourses = async () => {
    const { subjectCode, level } = selectedFilters;

    // Only fetch if both filters are selected
    if (subjectCode && level) {
      setLoading(true);
      setCourses([]);

      try {
        const coursesData = await CourseApiService.getCoursesBySubjectAndLevel(
          subjectCode,
          level,
          getAccessTokenSilently
        );

        // Transform API data to our format and filter out courses without titles
        const transformedCourses = coursesData
          .filter((course) => course.title || course.description)
          .map((course) => ({
            id: `${course.subjectCode} ${course.catalogNumber}`,
            course_name: course.title || 'No title available',
            description: cleanDescription(course.longDescription || ''),
            Prerequisite: extractPrerequisites(course.longDescription || ''),
            Antirequisite: extractAntirequisites(course.longDescription || '')
          }));

        setCourses(transformedCourses);
      } catch (error) {
        console.error('Error fetching courses:', error);
      } finally {
        setLoading(false);
      }
    }
  };

  // Listen for filter changes
  useEffect(() => {
    const handleFilterChange = (event) => {
      const newFilters = {
        ...selectedFilters,
        [event.detail.type]: event.detail.value
      };

      setSelectedFilters(newFilters);
    };

    // Listen for apply filter button clicks
    const handleApplyFilters = () => {
      setFiltersApplied(true);
      fetchCourses();
    };

    document.addEventListener('filterChange', handleFilterChange);
    document.addEventListener('applyFilters', handleApplyFilters);

    return () => {
      document.removeEventListener('filterChange', handleFilterChange);
      document.removeEventListener('applyFilters', handleApplyFilters);
    };
  }, [selectedFilters]);

  const toggleCourse = (courseId) => {
    setExpandedCourses((prevState) => ({
      ...prevState,
      [courseId]: !prevState[courseId]
    }));
  };

  return (
    <div className="course-list">
      <div className="courses-options">
        {loading && (
          <div className="loading-indicator">
            Loading courses...
          </div>
        )}

        {!loading && courses.length === 0 && (
          <div className="no-courses-message">
            {!filtersApplied
              ? 'Please apply course filters to generate courses.'
              : (
                <>
                  No courses found. Try:
                  <ul>
                    <li>Checking your filter selections</li>
                    <li>Selecting a different subject code</li>
                    <li>Choosing another course level</li>
                  </ul>
                </>
              )}
          </div>
        )}

        {/* Removed courses count display */}

        {courses.map((course) => (
          <div key={course.id} className="course-box">
            <button
              id={course.id}
              type="button"
              className="course-title"
              onClick={() => toggleCourse(course.id)}
            >
              {`${course.id} - ${course.course_name}`}
              <span className="icon-change2">
                {expandedCourses[course.id] ? '−' : '+'}
              </span>
            </button>
            {expandedCourses[course.id] && (
              <div className="course-details">
                <p>
                  <strong>Description: </strong>
                  {course.description.split('\n\n').map((paragraph, index) => {
                    const uniqueKey = `${course.id}-description-${index}`;
                    return (
                      <React.Fragment key={uniqueKey}>
                        {paragraph}
                        {index < course.description.split('\n\n').length - 1 && (
                        <br />
                        )}
                      </React.Fragment>
                    );
                  })}
                </p>
                <p>
                  <strong>Prerequisite: </strong>
                  {course.Prerequisite.join(', ')}
                </p>
                <p>
                  <strong>Antirequisite: </strong>
                  {course.Antirequisite.join(', ')}
                </p>
                <CourseDifficultyRating courseId={course.id} email={userEmail} />
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}

export default Options;
