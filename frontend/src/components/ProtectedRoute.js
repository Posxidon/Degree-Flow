import React, { useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth0 } from '@auth0/auth0-react';

function ProtectedRoute({ component: Component, children, requiredRoles }) {
  const {
    user,
    isAuthenticated,
    isLoading,
    loginWithRedirect
  } = useAuth0();

  const [hasRole, setHasRole] = useState(false);

  useEffect(() => {
    const checkUserRole = async () => {
      if (isAuthenticated && user) {
        try {
          const roles = user['http://localhost:3000/roles'] || [];
          const hasRequiredRole = requiredRoles.some((role) => roles.includes(role));
          setHasRole(hasRequiredRole);
        } catch (error) {
          console.error('Error checking user roles:', error);
        }
      }
    };

    if (!isAuthenticated && !isLoading) {
      loginWithRedirect();
    } else {
      checkUserRole();
    }
  }, [isAuthenticated, user, isLoading, requiredRoles, loginWithRedirect]);

  if (isLoading) return <div>Loading...</div>;
  if (!isAuthenticated) return <Navigate to="/" replace />;
  if (!hasRole) return <div>Access Denied: You do not have the required role.</div>;

  return children || <Component />;
}

export default ProtectedRoute;
