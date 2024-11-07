import React from 'react';
import logo from './Logo_Name_Red.png';
import menu from './51b3808ae01037d3b2519b24860dbeb6.png';
import footer from './mcmaster-footer-banner.png';
import './App.css';

function App() {
  return (
    <div className="App">
      <header className="header">
        <img src={logo} className="App-logo" alt="logo" />
        <h1 className="title">DegreeFlow</h1>
        <div className="user-info-div">
          <div className="inline-text">login</div>
          <div className="inline-text">2024 fall</div>
          <img src={menu} className="menu-icon" alt="menu" />
        </div>
      </header>
      <footer className="footer">
        <img src={footer} alt="footer" className="footer-img" />
        <div className="footer-text">
          <div className="inline-text">1280 Main St West.</div>
          <div className="inline-text">Hamtilton, Ontario L8S 4L8.</div>
          <div className="inline-text">(905) 525-9140</div>
        </div>
        <div className="inline-text">@McMaster University</div>
      </footer>
    </div>
  );
}

export default App;
