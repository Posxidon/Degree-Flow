import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

export const uploadTranscript = async (file) => {
  const formData = new FormData();
  formData.append('file', file);

  return axios.post(`${API_BASE_URL}/transcripts/upload`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  });
};

export const fetchTranscriptData = async () => axios.get(`${API_BASE_URL}/transcripts/data`);
