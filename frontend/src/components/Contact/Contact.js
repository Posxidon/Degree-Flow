/* import React, { useRef, useState } from 'react';
import './Contact.css';
// import { HiOutlineMail, HiOutlineArrowSmRight } from 'react-icons/hi';
import emailjs from '@emailjs/browser';

function Contact() {
  const form = useRef();
  const [statusMessage, setStatusMessage] = useState('');
  const [statusType, setStatusType] = useState(''); // "success" or "error"

  const sendEmail = (e) => {
    e.preventDefault();

    emailjs.sendForm(
      process.env.REACT_APP_EMAILJS_SERVICE_ID,
      process.env.REACT_APP_EMAILJS_TEMPLATE_ID,
      form.current,
      process.env.REACT_APP_EMAILJS_PUBLIC_KEY
    ).then(
      () => {
        setStatusMessage('Message sent successfully!');
        setStatusType('success');
        e.target.reset();
      },
      (error) => {
        setStatusMessage('Failed to send message. Please try again.');
        setStatusType('error');
        console.error(error);
      }
    );
  };

  return (
    <section className="contact section" id="contact">
      <h2 className="section__title">Get in Touch</h2>
      <span className="section__subtitle">We&#39;re here to help</span>

      <div className="contact__container container grid">
        <div className="contact__content">
          <h3 className="contact__title">Send us an email</h3>
          <div className="contact__info">
            <div className="contact__card">
              <className="contact__card-icon" />
              <h3 className="contact__card-title">DegreeFlow Email</h3>
              <span className="contact__card-data">degreeflow@gmail.com</span>
              <a href="mailto:degreeflow@gmail.com" className="contact__button">
                Write to us
                {' '}
                <className="contact__button-icon" />
              </a>
            </div>
          </div>
        </div>

        <div className="contact__content">
          <h3 className="contact__title">Or use the contact form</h3>

          <form ref={form} onSubmit={sendEmail} className="contact__form">
            <div className="contact__form-div">
              {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */ /*} 
              <label className="contact__form-tag">Name</label>
              <input
                type="text"
                name="name"
                className="contact__form-input"
                placeholder="Enter your name"
                required
              />
            </div>

            <div className="contact__form-div">
              {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */ /*}
              <label className="contact__form-tag">Email</label>
              <input
                type="email"
                name="email"
                className="contact__form-input"
                placeholder="Enter your email"
                required
              />
            </div>

            <div className="contact__form-div contact__form-area">
              {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */ /*}
              <label className="contact__form-tag">Message</label>
              <textarea
                name="reason"
                cols="30"
                rows="10"
                className="contact__form-input"
                placeholder="Let us know how we can help..."
                required
              />
            </div>

            {/* eslint-disable-next-line react/button-has-type */ /*}
            <button className="send-msg-btn">
              <span className="btn-inner">
                <span className="btn-text">Send Message</span>
                <svg
                  className="button__icon"
                  xmlns="http://www.w3.org/2000/svg"
                  width="20"
                  height="20"
                  viewBox="0 0 24 24"
                  fill="none"
                >
                  <path
                    d="M14.2199 21.9352C13.0399 21.9352 11.3699 21.1052 10.0499 17.1352L9.32988 14.9752L7.16988 14.2552C3.20988 12.9352 2.37988 11.2652 2.37988 10.0852C2.37988 8.91525 3.20988 7.23525 7.16988 5.90525L15.6599 3.07525C17.7799 2.36525 19.5499 2.57525 20.6399 3.65525C21.7299 4.73525 21.9399 6.51525 21.2299 8.63525L18.3999 17.1252C17.0699 21.1052 15.3999 21.9352 14.2199 21.9352Z"
                    fill="var(--container-color)"
                  />
                  <path
                    d="M10.11 14.7052C9.92005 14.7052 9.73005 14.6352 9.58005 14.4852C9.29005 14.1952 9.29005 13.7152 9.58005 13.4252L13.16 9.83518C13.45 9.54518 13.93 9.54518 14.22 9.83518C14.51 10.1252 14.51 10.6052 14.22 10.8952L10.64 14.4852C10.5 14.6352 10.3 14.7052 10.11 14.7052Z"
                    fill="var(--container-color)"
                  />
                </svg>
              </span>
            </button>
          </form>

          {statusMessage && (
            <p className={`form-status ${statusType}`}>
              {statusMessage}
            </p>
          )}
        </div>
      </div>
    </section>
  );
} 

export default Contact; */
