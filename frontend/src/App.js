import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import './App.css';
import Header from './Header';
import Footer from './Footer';
import HomePage from './HomePage';
import UnitTracker from './components/UnitTracker/UnitTracker';
import ProtectedData from './components/ProtectedData';
import ProtectedRoute from './components/ProtectedRoute';
import FilterWrapper from './components/FilterSelection/FilterWrapper';
import UnitTrackerSection from './components/UnitTracker/UnitTrackerSection';
import YearlySchedule from './components/YearlySchedule/YearlySchedule';
import SeatAlertPage from './pages/SeatAlertPage';
import GenerateSchedule from './pages/GenerateSchedule';
import UploadTranscript from './components/pdf-parsing/UploadTranscript';
import WhatIf from './components/WhatIf/WhatIf';

// Wrapper component for degree-progress
function DegreeProgressWrapper() {
  return (
    <>
      <div className="left-content">
        <UnitTrackerSection />
      </div>
      <div className="center-panel">
        <YearlySchedule />
      </div>
    </>
  );
}

function App() {
  return (
    <div className="App">
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
            path="/generate-schedule"
            element={(
              <ProtectedRoute
                component={GenerateSchedule}
                requiredRoles={['users', 'admin']}
              />
                        )}
          />

          <Route
            path="/FilterSelection"
            element={(
              <ProtectedRoute
                component={FilterWrapper}
                requiredRoles={['users', 'admin']}
              />
                        )}
          />

          <Route
            path="/degree-progress"
            element={(
              <ProtectedRoute
                component={DegreeProgressWrapper}
                requiredRoles={['users', 'admin']}
              />
                        )}
          />

          <Route
            path="/upload-transcript"
            element={(
              <ProtectedRoute
                component={UploadTranscript}
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

          {/* Fallback Route */}
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </main>
      <Footer />
    </div>
  );
}

export default App;
