const CourseApiService = {
  // Base URL now points to your backend proxy
  baseUrl: `https://degreeflow-api-dnfnaqhababxdjg8.canadacentral-01.azurewebsites.net/api/courses`,

  async getCoursesBySubjectAndLevel(subjectCode, level, getAccessTokenSilently) {
    try {
      const url = `${this.baseUrl}/by-subject-level?subjectCode=${subjectCode}&level=${level}`;
      const token = await getAccessTokenSilently({
        audience: 'https://degreeflow-backend/api',
        scope: 'read:data write:data'
      });

      const response = await fetch(url, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      if (!response.ok) {
        throw new Error(`API request failed with status ${response.status}`);
      }

      const data = await response.json();
      // Extract the courses array from the response
      return data.courses || [];
    } catch (error) {
      console.error('Error fetching courses:', error);
      return [];
    }
  },

  getSubjectCodes() {
    // Directly return the array of subject codes
    return ['COMPSCI', 'MATH', 'ANTHROP', 'ARABIC', 'ART', 'ARTHIST', 'ARTSSCI', 'ASTRON', 'AUTOTECH',
      'BIOCHEM', 'BIOLOGY', 'BIOMEDDC', 'BIOPHYS', 'BIOSAFE', 'BIOTECH', 'CAYUGA', 'CHALLENG', 'CHEM',
      'CHEMBIO', 'CHEMBME', 'CHEMENG', 'CHINESE', 'CIVBME', 'CIVDEM', 'CIVENG', 'CIVTECH', 'CMST', 'CMTYENGA',
      'COLLAB', 'COMMERCE', 'COMPENG', 'DATASCI', 'EARTHSC', 'ECON', 'ELECBME', 'ELECENG', 'ENGINEER',
      'ENGLISH', 'ENGNMGT', 'ENGPHYS', 'ENGSOCTY', 'ENGTECH', 'ENRTECH', 'ENVIRSC', 'ENVSOCTY', 'EPHYSBME',
      'EXPLORE', 'FARSI', 'FRENCH', 'GENDRST', 'GENTECH', 'GERMAN', 'GKROMST', 'GLOBALZN', 'GREEK', 'HEBREW',
      'HISTORY', 'HLTHAGE', 'HTHSCI', 'HUMAN', 'HUMBEHV', 'IARTS', 'IBEHS', 'IBH', 'INDIGST', 'INNOVATE',
      'INSPIRE', 'INTENG', 'IRH', 'ISCI', 'ITALIAN', 'JAPANESE', 'KINESIOL', 'KOREAN', 'LABRST', 'LATAM',
      'LATIN', 'LIFESCI', 'LINGUIST', 'MANTECH', 'MATLS', 'MATLSBME', 'MECHBME', 'MECHENG', 'MECHTRON',
      'MEDIAART', 'MEDPHYS', 'MEDRADSC', 'MELD', 'MIDWIF', 'MOHAWK', 'MOLBIOL', 'MUSIC', 'MUSICCOG', 'NEUROSCI',
      'NEXUS', 'NURSING', 'OJIBWE', 'PEACJUST', 'PHARMAC', 'PHILOS', 'PHYSICS', 'PNB', 'POLSCI', 'PROCTECH',
      'PSYCH', 'RUSSIAN', 'SANSKRIT', 'SCAR', 'SCICOMM', 'SCIENCE', 'SEP', 'SFGNTECH', 'SFWRBME', 'SFWRENG',
      'SFWRTECH', 'SMRTTECH', 'SOCIOL', 'SOCPSY', 'SOCSCI', 'SOCWORK', 'SPANISH', 'STATS', 'STEP', 'SUSTAIN',
      'THTRFLM', 'TRONBME', 'VSR', 'WHMIS', 'WORKLABR'];
  },

  getCourseLevels() {
    // Directly return the array of course levels
    return ['1', '2', '3', '4'];
  }
};

export default CourseApiService;
