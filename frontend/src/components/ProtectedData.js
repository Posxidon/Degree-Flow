import React, { useEffect, useState } from 'react';
import { useAuth0 } from '@auth0/auth0-react';

function ProtectedData() {
  const { getAccessTokenSilently, isAuthenticated } = useAuth0();
  const [data, setData] = useState(null);
  useEffect(() => {
    if (!isAuthenticated) return;// If the user isn't authenticated, stop
    // Fetching protected data from your API after getting the token
    const fetchData = async () => {
      try {
        const token = await getAccessTokenSilently({
          authorizationParams: {
            audience: 'https://degreeflow-backend/api',
            scope: 'read:data write:data'
          }
        });
        const response = await fetch('http://localhost:8080/api/protected', {
          headers: { Authorization: `Bearer ${token}` }
        });
        if (!response.ok) throw new Error('API call failed');
        const result = await response.json();
        setData(result);// Store the data received from the API
      } catch (error) {
        console.error('API call error:', error);
      }
    };
    fetchData();// Call the fetchData function to get the data
    // eslint-disable-next-line max-len
  }, [isAuthenticated, getAccessTokenSilently]);// Depend on isAuthenticated and getAccessTokenSilently
  return (
    <div>
      <h2>Protected API Data</h2>
      {data ? <pre>{JSON.stringify(data, null, 2)}</pre> : <p>Loading...</p>}
    </div>
  );
}
export default ProtectedData;
