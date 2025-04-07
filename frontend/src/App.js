import React from 'react';
import {
  Routes, Route, useLocation, Navigate
} from 'react-router-dom'; // No need to import Router here
import './App.css';

import Header from './Header';
import MinimalHeader from './MinimalHeader';
import Footer from './Footer';

import HomePage from './HomePage'; // the landing page
import FilterSelection from './components/FilterSelection/FilterSelection';
import FilterPageButtons from './components/FilterSelection/FilterPageButtons';
import FilterOptions from './components/FilterSelection/FilterOptions';
import UnitTrackerSection from './components/UnitTracker/UnitTrackerSection';
import YearlySchedule from './components/YearlySchedule/YearlySchedule';
import SeatAlertPage from './pages/SeatAlertPage';
import GenerateSchedule from './pages/GenerateSchedule'; // New page
import ProtectedRoute from './components/ProtectedRoute'; // Import the ProtectedRoute

function App() {
  const location = useLocation();
  const isLanding = location.pathname === '/';

  return (
    <div className="App">
      {/* Header based on route */}
      {isLanding ? <MinimalHeader /> : <Header />}

      <main className="main-content">
        <Routes>
          {/* Landing Page is Open to All */}
          <Route path="/" element={<HomePage />} />

          {/* Protected Routes (only accessible with valid roles) */}
          <Route
            path="/dashboard"
            element={(
              <ProtectedRoute
                component={(
                  <>
                    <div className="left-content">
                      <UnitTrackerSection />
                    </div>
                    <div className="center-panel">
                      <YearlySchedule />
                    </div>
                  </>
                                )}
                requiredRoles={['users', 'admin']}
              />
                        )}
          />

          <Route
            path="/FilterSelection"
            element={(
              <ProtectedRoute
                component={(
                  <>
                    <FilterPageButtons />
                    <FilterSelection />
                    <FilterOptions />
                  </>
                                )}
                requiredRoles={['users', 'admin']}
              />
                        )}
          />

          <Route
            path="/seat-alert"
            element={
              <ProtectedRoute component={<SeatAlertPage />} requiredRoles={['users', 'admin']} />
                        }
          />

          <Route
            path="/generate-schedule"
            element={
              <ProtectedRoute component={<GenerateSchedule />} requiredRoles={['users', 'admin']} />
                        }
          />

          {/* Catch-all route for unknown paths */}
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </main>

      {/* Footer is always shown */}
      <Footer />
    </div>
  );
}

export default App;
