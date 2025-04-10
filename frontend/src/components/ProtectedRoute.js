import React, { useEffect, useState } from 'react';
import { useAuth0 } from '@auth0/auth0-react';

function ProtectedRoute({ component: Component, requiredRoles }) {
  const {
    user, isAuthenticated, isLoading, loginWithRedirect
  } = useAuth0();
  const [hasRole, setHasRole] = useState(false);
  const [isCheckingRoles, setIsCheckingRoles] = useState(true);

  useEffect(() => {
    const checkUserRole = async () => {
      if (isAuthenticated && user) {
        try {
          const roles = user['https://degreeflow.netlify.app/roles'] || []; // Get roles from the JWT claim
          const hasRequiredRole = requiredRoles.some((role) => roles.includes(role));
          setHasRole(hasRequiredRole);
        } catch (error) {
          console.error('Error checking roles:', error);
        }
      }
      setIsCheckingRoles(false);
    };

    if (!isLoading && !isAuthenticated) {
      loginWithRedirect({ redirectUri: `${window.location.origin}/dashboard` });
    } else if (isAuthenticated) {
      checkUserRole();
    }
  }, [isAuthenticated, isLoading, user, requiredRoles, loginWithRedirect]);

  if (isLoading || isCheckingRoles) return <div>Loading...</div>; // Display loading spinner

  if (!isAuthenticated) return null; // Prevent rendering until authentication is confirmed

  if (hasRole) {
    return <Component />;
  }
  return <div>Access Denied: You do not have the required role.</div>;
}

export default ProtectedRoute;
