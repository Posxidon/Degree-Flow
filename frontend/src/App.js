import React, { useEffect } from 'react';
import {
  Routes, Route, Navigate, useLocation
} from 'react-router-dom';
import './App.css';

import Header from './Header';
import Footer from './Footer';
import HomePage from './HomePage';
import ProtectedData from './components/ProtectedData';
import ProtectedRoute from './components/ProtectedRoute';
import FilterSelection from './components/FilterSelection/FilterSelection';
import FilterOptions from './components/FilterSelection/FilterOptions';
import UnitTrackerSection from './components/UnitTracker/UnitTrackerSection';
import YearlySchedule from './components/YearlySchedule/YearlySchedule';
import SeatAlertPage from './pages/SeatAlertPage';
import GenerateSchedule from './pages/GenerateSchedule';
import UploadTranscript from './components/pdf-parsing/UploadTranscript';

function FilterSelectionWrapper() {
  return (
    <div className="filter-course-container">
      <div className="filter-box-wrapper">
        <FilterSelection />
      </div>
      <div className="course-list-wrapper">
        <FilterOptions />
      </div>
    </div>
  );
}

function DashboardWrapper() {
  return (
    <div className="dashboard-wrapper">
      <div className="left-content">
        <UnitTrackerSection />
      </div>
      <div className="center-panel">
        <YearlySchedule />
      </div>
    </div>
  );
}

function MainRoutes() {
  const location = useLocation();

  useEffect(() => {
    const isLanding = location.pathname === '/';
    document.body.classList.toggle('landing', isLanding);
  }, [location.pathname]);

  return (
    <>
      <Header />
      <main className={location.pathname === '/' ? '' : 'dashboard-main'}>
        <Routes>
          <Route path="/" element={<HomePage />} />

          <Route
            path="/dashboard"
            element={(
              <ProtectedRoute
                component={DashboardWrapper}
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
                component={FilterSelectionWrapper}
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

          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </main>
      <Footer />
    </>
  );
}

function App() {
  return (
    <div className="App">
      <MainRoutes />
    </div>
  );
}

export default App;
