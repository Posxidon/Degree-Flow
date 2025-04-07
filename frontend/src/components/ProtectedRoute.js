import React, { useEffect, useState } from 'react';
import { useAuth0 } from '@auth0/auth0-react';

function ProtectedRoute({ component: Component, requiredRoles }) {
  const {
    user, isAuthenticated, isLoading, loginWithRedirect
  } = useAuth0();
  const [hasRole, setHasRole] = useState(false);
  useEffect(() => {
    // Check if the user has the required role after login
    const checkUserRole = async () => {
      if (isAuthenticated && user) {
        try {
          const roles = user['http://localhost:3000/roles'] || []; // Get roles from the JWT claim
          // Check if the user has the required role
          const hasRequiredRole = requiredRoles.some((role) => roles.includes(role));
          setHasRole(hasRequiredRole); // Set state for role check
        } catch (error) {
          console.error('Error checking roles:', error);
        }
      }
    };
    // If user is not authenticated, trigger login
    if (!isAuthenticated) {
      loginWithRedirect();// Trigger login, but do NOT await this
    } else {
      checkUserRole();// Check user role if already authenticated
    }
  }, [isAuthenticated, user, requiredRoles, loginWithRedirect]);
  if (isLoading) return <div>Loading...</div>; // Show loading until authentication state is settled
  // If user has the required role, render component, otherwise show access denied
  return hasRole ? <Component /> : <div>Access Denied: You do not have the required role.</div>;
}
export default ProtectedRoute;
