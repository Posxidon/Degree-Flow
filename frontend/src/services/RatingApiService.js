const RatingApiService = {
  // Base URL for the API
  baseUrl: 'http://localhost:8080/api/ratings',

  async getRatingSummary(courseCode) {
    try {
      const response = await fetch(`${this.baseUrl}/summary/${courseCode}`);
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

  async submitRating(email, courseCode, stars) {
    try {
      const response = await fetch(this.baseUrl, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
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

  async getStudentRating(email, courseCode) {
    try {
      const response = await fetch(`${this.baseUrl}/student/${email}/course/${courseCode}`);
      if (response.status === 404) {
        // Student hasn't rated this course yet
        return null;
      }

      if (!response.ok) {
        throw new Error(`Error fetching student rating: ${response.statusText}`);
      }

      return response.json();
    } catch (error) {
      console.error('Failed to fetch student rating:', error);
      return null;
    }
  }
};

export default RatingApiService;
