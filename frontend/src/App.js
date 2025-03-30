import React from 'react';
import {
  BrowserRouter as Router, Routes, Route, useLocation
} from 'react-router-dom';
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
import GenerateSchedule from './pages/GenerateSchedule';
// eslint-disable-next-line camelcase,import/no-named-as-default,import/no-named-as-default-member
import WhatIf from './components/WhatIf/WhatIf';

function MainRoutes() {
  const location = useLocation();
  const isLanding = location.pathname === '/';

  return (
    <>
      {/* Header based on route */}
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
              <>
                <FilterPageButtons />
                <FilterSelection />
                <FilterOptions />
              </>
            )}
          />

          <Route path="/seat-alert" element={<SeatAlertPage />} />
          <Route path="/generate-schedule" element={<GenerateSchedule />} />
          <Route path="/what-if" element={<WhatIf />} />
        </Routes>
      </main>

      {/* Footer is always shown */}
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
