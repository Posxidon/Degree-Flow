const RatingApiService = {
  // Base URL for the API
  baseUrl: 'https://degreeflow-api-dnfnaqhababxdjg8.canadacentral-01.azurewebsites.net/api/ratings',

  async getRatingSummary(courseCode, getAccessTokenSilently) {
    try {
      const token = await getAccessTokenSilently({
        audience: 'https://degreeflow-backend/api',
        scope: 'read:data write:data'
      });
      const response = await fetch(`${this.baseUrl}/summary/${courseCode}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      if (!response.ok) {
        throw new Error(`Error fetching rating summary: ${response.statusText}`);
      }
      return response.json();
    } catch (error) {
      console.error('Failed to fetch rating summary:', error);
      // Return a default summary for error cases
      return {
        courseCode,
        averageRating: 0,
        totalRatings: 0,
        difficultyCategory: 'No ratings yet'
      };
    }
  },

  async submitRating(email, courseCode, stars, getAccessTokenSilently) {
    try {
      const token = await getAccessTokenSilently({
        audience: 'https://degreeflow-backend/api',
        scope: 'read:data write:data'
      });
      const response = await fetch(this.baseUrl, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({
          email,
          courseCode,
          stars
        })
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Error submitting rating: ${errorText}`);
      }

      return response.json();
    } catch (error) {
      console.error('Failed to submit rating:', error);
      throw error;
    }
  },

  async getStudentRating(email, courseCode, getAccessTokenSilently) {
    try {
      const token = await getAccessTokenSilently({
        audience: 'https://degreeflow-backend/api',
        scope: 'read:data write:data'
      });
      const response = await fetch(`${this.baseUrl}/student/${email}/course/${courseCode}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      if (!response.ok) {
        throw new Error(`Error fetching student rating: ${response.statusText}`);
      }

      const data = await response.json();
      // Check if it's our "not found" indicator
      if (data.found === false) {
        return null;
      }

      return data;
    } catch (error) {
      console.error('Failed to fetch student rating:', error);
      return null;
    }
  }
};

export default RatingApiService;
