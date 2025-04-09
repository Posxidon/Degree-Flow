import React from 'react';
import './PrivacyPolicy.css';

function PrivacyPolicy() {
  return (
    <div className="privacy-container">
      <h1>Privacy Policy & Terms of Use</h1>
      <p>
        <strong>Effective Date:</strong>
        {' '}
        8 April 2025
      </p>

      <h2>Privacy Policy</h2>
      <p>
        {/* eslint-disable-next-line max-len */}
        DegreeFlow is a student-led initiative supervised by the McMaster Engineering Society and is not an official academic advising platform of McMaster University. While we strive to provide useful tools for academic planning, all information presented through DegreeFlow should be used as a supplementary resource and not as a replacement for professional academic advising from McMaster University.
      </p>

      <h3>1. Personal Information and Data Collection</h3>
      <p>
        {/* eslint-disable-next-line max-len */}
        We respect your privacy. When you use DegreeFlow, we may collect limited personal information related to your academic history, including your program, enrolled courses, grades, GPA, and co-op details. However, we do not collect or store any personally identifiable information (PII) such as your name, address, phone number, or student number.
      </p>

      <h3>2. Data Use and Protection</h3>
      <p>
        {/* eslint-disable-next-line max-len */}
        Your academic data is stored in an encrypted format and is only used to support features such as degree planning, alerts, and academic visualization tools. We do not share, sell, or rent your information to third parties.
      </p>

      <h3>3. Disclaimer</h3>
      <p>
        {/* eslint-disable-next-line max-len */}
        DegreeFlow is not affiliated with McMaster University’s Registrar&#39;s Office or any official academic body. The platform is developed and maintained by students as a capstone project in 2025. Any information provided by DegreeFlow is for informational and planning purposes only and does not constitute academic advising. Users are encouraged to consult with official McMaster academic advisors for final decisions.
      </p>

      <h3>4. Consent</h3>
      <p>
        {/* eslint-disable-next-line max-len */}
        By using DegreeFlow and uploading your academic documents, you consent to the parsing and storage of non-PII academic data as described above. You acknowledge that this tool is not a substitute for official university resources or advisement.
      </p>

      <h3>5. Data Deletion</h3>
      <p>
        {/* eslint-disable-next-line max-len */}
        You may request the deletion of your data at any time by contacting us at degreeflow@gmail.com. We will process such requests promptly.
      </p>

      <h3>6. Changes to This Policy</h3>
      <p>
        {/* eslint-disable-next-line max-len */}
        We may update this Privacy Policy from time to time. Continued use implies acceptance of the revised terms.
      </p>

      <h2>Terms of Use</h2>

      <h3>1. Acceptable Use</h3>
      <p>
        {/* eslint-disable-next-line max-len */}
        You agree to use the Platform only for lawful academic planning purposes. Any misuse, including uploading inappropriate documents or unauthorized access attempts, may result in permanent bans and notification to authorities.
      </p>

      <h3>2. Intellectual Property</h3>
      <p>
        {/* eslint-disable-next-line max-len */}
        All tools and designs are the property of the DegreeFlow team or its contributors. You may not reproduce or reuse any part without written permission.
      </p>

      <h3>3. Disclaimer of Liability</h3>
      <p>
        {/* eslint-disable-next-line max-len */}
        DegreeFlow is not an official product of McMaster University. We make no guarantees on the accuracy or completeness of information provided. Users must verify with official resources.
      </p>

      <h3>4. Limitation of Liability</h3>
      <p>
        {/* eslint-disable-next-line max-len */}
        The DegreeFlow team and McMaster Engineering Society will not be liable for any damages or academic consequences from use of the platform.
      </p>

      <h3>5. Modifications</h3>
      <p>
        These terms may be updated. Continued use of the platform implies acceptance of changes.
      </p>

      <div className="privacy-download">
        <a
          href="/files/DegreeFlow_PrivacyPolicy_TermsOfUse_Final.pdf"
          target="_blank"
          rel="noopener noreferrer"
        >
          Download PDF Version
        </a>
      </div>
    </div>
  );
}

export default PrivacyPolicy;
