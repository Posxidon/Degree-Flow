import React from 'react';
/* =========================================================================================
This section is to define routers for the page, and create an page for fitler selection.'
 */
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import FilterSelection from './components/FilterSelection/FilterSelection';
/* ========================================================================================= */
import logo from './Logo_Name_Red.png';
import footer from './Logo_Name_White.png';
import ButtonSection from './components/buttonSection/ButtonSection';
import UserNav from './components/userNav/UserNav';
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

        {/* section contains all the features of the first loaded page, these are changeable */}
        <main className="main-content">
          <Routes>
            <Route path="/" element={<ButtonSection />} />
            {/* this creates the "/" path, which is the first loaded page  */}
            <Route path="/FilterSelection" element={<FilterSelection />} />
            {/* this creates "/filterPage" path, which will be followed when button is clicked */}
          </Routes>
        </main>

        <footer className="footer">
          <img src={footer} alt="footer" className="footer-img" />
          <div className="footer-text">
            <div className="inline-text">1280 Main St West.</div>
            <div className="inline-text">Hamtilton, Ontario L8S 4L8.</div>
            <div className="inline-text">(905) 525-9140</div>
          </div>
          <div className="inline-text">© 2024 McMaster Engineering Society</div>
        </footer>
      </div>
    </Router>
  );
}

export default App;
