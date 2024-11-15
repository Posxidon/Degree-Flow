import React from 'react';

function LoginButton({ onClick }) {
  return (
    <button
      type="button"
      className="login-button"
      onClick={onClick}
      aria-label="Login"
    >
      login
    </button>
  );
}

export default LoginButton;
