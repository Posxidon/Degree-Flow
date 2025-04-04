import React from 'react';
import { Routes, Route, useLocation } from 'react-router-dom';
import './App.css';

import Header from './Header';
import MinimalHeader from './MinimalHeader';
import Footer from './Footer';

import HomePage from './HomePage';
import FilterSelection from './components/FilterSelection/FilterSelection';
import FilterOptions from './components/FilterSelection/FilterOptions';
import UnitTrackerSection from './components/UnitTracker/UnitTrackerSection';
import YearlySchedule from './components/YearlySchedule/YearlySchedule';
import UploadTranscript from './components/pdf-parsing/UploadTranscript';
import SeatAlertPage from './pages/SeatAlertPage';
import GenerateSchedule from './pages/GenerateSchedule';

function MainRoutes() {
  const location = useLocation();
  const isLanding = location.pathname === '/';

  return (
    <>
      {/* Show different header on landing page */}
      {isLanding ? <MinimalHeader /> : <Header />}

      <main className="main-content">
        <Routes>
          <Route path="/" element={<HomePage />} />

          <Route
            path="/dashboard"
            element={(
              <>
                <div className="left-content">
                  <UnitTrackerSection />
                </div>
                <div className="center-panel">
                  <YearlySchedule />
                </div>
              </>
                        )}
          />

          <Route
            path="/FilterSelection"
            element={(
              <div className="filter-course-container">
                <div className="filter-box-wrapper">
                  <FilterSelection />
                </div>
                <div className="course-list-wrapper">
                  <FilterOptions />
                </div>
              </div>
            )}
          />

          <Route path="/seat-alert" element={<SeatAlertPage />} />
          <Route path="/generate-schedule" element={<GenerateSchedule />} />
          <Route path="/upload-transcript" element={<UploadTranscript />} />
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
