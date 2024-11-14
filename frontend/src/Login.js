import React from 'react';
import logo from './Logo_Name_Red.png';
import './Login.css';

function Login() {
  return (
    <div className="Login">
      <form className="login-div">
        <img src={logo} className="logo" alt="logo" />
        <h1 className="title">DegreeFlow</h1>
        <p>
          MacID
        </p>
        <input className="input-fld" id="macID" />
        <p>
          Password
        </p>
        <input className="input-fld" type="password" id="pw" />
      </form>
    </div>
  );
}

export default Login;
