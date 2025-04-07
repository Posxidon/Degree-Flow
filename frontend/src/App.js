import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import './App.css';
import Header from './Header';
import Footer from './Footer';
import HomePage from './HomePage';
import UnitTracker from './components/UnitTracker/UnitTracker';
import ProtectedData from './components/ProtectedData';
import ProtectedRoute from './components/ProtectedRoute';
import SeatAlertPage from './pages/SeatAlertPage';
import WhatIf from './components/WhatIf/WhatIf';

function App() {
  return (
    <>
      <Header />
      <main className="main-content">
        <Routes>
          {/* Public Route */}
          <Route path="/" element={<HomePage />} />
          {/* Protected Routes */}
          <Route
            path="/dashboard"
            element={(
              <ProtectedRoute
                component={UnitTracker}
                requiredRoles={['users', 'admin']}
              />
            )}
          />
          <Route
            path="/protected-data"
            element={(
              <ProtectedRoute
                component={ProtectedData}
                requiredRoles={['users', 'admin']}
              />
            )}
          />
          <Route
            path="/seat-alert"
            element={(
              <ProtectedRoute
                component={SeatAlertPage}
                requiredRoles={['users', 'admin']}
              />
            )}
          />
          <Route
            path="/what-if"
            element={(
              <ProtectedRoute
                component={WhatIf}
                requiredRoles={['users', 'admin']}
              />
            )}
          />
          {/* <Route */}
          {/*  path="/course-ratings" */}
          {/*  element={( */}
          {/*    <ProtectedRoute */}
          {/*      component={CourseRatingsPage} */}
          {/*      requiredRoles={['users', 'admin']} */}
          {/*    /> */}
          {/*                )} */}
          {/* /> */}
          {/* Fallback Route */}
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </main>
      <Footer />
    </>
  );
}
export default App;
