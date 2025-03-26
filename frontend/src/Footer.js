import React from 'react';
import './Footer.css';

function Footer() {
  return (
    <footer className="footer">
      <div className="footer-wrapper">
        <div className="footer-links">
          <a href="/contact">Contact Us</a>
          <a href="/privacy">Privacy Policy</a>
        </div>

        <p className="footer-copy">
          ©
          {' '}
          {new Date().getFullYear()}
          {' '}
          DegreeFlow. All rights reserved.
        </p>
      </div>
    </footer>
  );
}

export default Footer;
