import React from 'react';
import { Routes, Route } from 'react-router-dom';
import './App.css';

// Header has the single logo + 3 menu buttons + seat alert
import Header from './Header';

// Page components
import FilterSelection from './components/FilterSelection/FilterSelection';
import FilterPageButtons from './components/FilterSelection/FilterPageButtons';
import FilterOptions from './components/FilterSelection/FilterOptions';
import UnitTrackerSection from './components/UnitTracker/UnitTrackerSection';
import YearlySchedule from './components/YearlySchedule/YearlySchedule';
// import SeatAlertPage from './pages/SeatAlertPage';

// Separate Footer component
import Footer from './Footer';

function App() {
  return (
    <div className="App">
      <Header />

      {/* All routes within the main-content wrapper */}
      <main className="main-content">
        <Routes>
          {/* Home / Main page */}
          <Route
            path="/"
            element={(
              <>
                <div className="left-content">
                  {/* <ButtonSection /> // remove if not needed */}
                  <UnitTrackerSection />
                </div>
                <div className="center-panel">
                  <YearlySchedule />
                </div>
              </>
                          )}
          />

          {/* Filter Selection page */}
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

          {/* Seat Alert page */}
          {/*
            <Route
              path="/seat-alert"
              element={<SeatAlertPage />}
            />
            */}

          {/* Add more routes as needed */}
        </Routes>
      </main>

      <Footer />
    </div>
  );
}

export default App;
