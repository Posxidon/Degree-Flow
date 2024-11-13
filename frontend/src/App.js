import React from 'react';
import logo from './Logo_Name_Red.png';
import footer from './Logo_Name_White.png';
import ButtonSection from './components/buttonSection/ButtonSection';
import UserNav from './components/userNav/UserNav';
import UnitTrackerSection from './components/UnitTracker/UnitTrackerSection';
import YearlySchedule from './components/YearlySchedule/YearlySchedule';
import './App.css';

function App() {
  return (
    <div className="App">
      <header className="header">
        <img src={logo} className="App-logo" alt="logo" />
        <h1 className="title">DegreeFlow</h1>
        <UserNav />
      </header>

      <main className="main-content">
        <div className="left-content">
          <ButtonSection />
          <UnitTrackerSection />
        </div>
        <div className="center-panel">
          <YearlySchedule />
        </div>
      </main>

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
  );
}

export default App;
