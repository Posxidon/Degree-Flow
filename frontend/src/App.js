import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import FilterSelection from './components/FilterSelection/FilterSelection';
import FilterPageButtons from './components/FilterSelection/FilterPageButtons';
import FilterOptions from './components/FilterSelection/FilterOptions';
import logo from './Logo_Name_Red.png';
import footer from './Logo_Name_White.png';
import ButtonSection from './components/buttonSection/ButtonSection';
import UserNav from './components/userNav/UserNav';
import UnitTrackerSection from './components/UnitTracker/UnitTrackerSection';
import YearlySchedule from './components/YearlySchedule/YearlySchedule';
// eslint-disable-next-line camelcase
import What_if from './components/WhatIf/What_if';
import './App.css';

function App() {
  return (
    <Router>
      {/* this component will enables routing */}
      <div className="App">
        <header className="header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="title">DegreeFlow</h1>
          <UserNav />
        </header>

        <Routes>
          {/* section contains all the features of the first loaded page, these are changeable */}
          <Route
            path="/"
            element={
              (
                <main className="main-content">
                  <div className="left-content">
                    <ButtonSection />
                    <UnitTrackerSection />
                  </div>
                  <div className="center-panel">
                    <YearlySchedule />
                  </div>
                </main>
              )
            }
          />
          <Route
            path="/What_if"
            element={
            (
              <div>
                {/* eslint-disable-next-line react/jsx-pascal-case */}
                <What_if />
              </div>
            )
          }
          />
          <Route
            path="/FilterSelection"
            element={
            (
              <div>
                <FilterPageButtons />
                <FilterSelection />
                <FilterOptions />
              </div>
            )
          }
          />
        </Routes>
        <footer className="footer">
          <img src={footer} alt="footer" className="footer-img" />
          <div className="footer-text">
            <div className="inline-text">1280 Main St West.</div>
            <div className="inline-text">Hamilton, Ontario L8S 4L8.</div>
            <div className="inline-text">(905) 525-9140</div>
          </div>
          <div className="inline-text">© 2024 McMaster Engineering Society</div>
        </footer>
      </div>
    </Router>
  );
}

export default App;
