import React from 'react';
import './Footer.css';
import mesLogo from './Logo_Name_White.png'; // or wherever your MES logo is

function Footer() {
  return (
    <footer className="footer">
      {/* Left Column */}
      <div className="footer-col footer-left">
        <img src={mesLogo} alt="MES Logo" className="footer-logo" />
      </div>

      {/* Center Column */}
      <div className="footer-col footer-center">
        <a href="/contact">CONTACT US</a>
        <br />
        <a href="/privacy">PRIVACY POLICY</a>
        {/* If you want T&C as well: */}
        {/* <a href="/terms">TERMS & CONDITIONS</a> */}
      </div>

      {/* Right Column */}
      <div className="footer-col footer-right">
        1280 Main Street West Hamilton, Ontario L8S 4L8
      </div>
    </footer>
  );
}

export default Footer;
