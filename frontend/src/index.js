import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import { Auth0Provider } from '@auth0/auth0-react';
import App from './App';
import './index.css';

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
  <React.StrictMode>
    <BrowserRouter>
      <Auth0Provider
        domain="dev-ga3kbufgkmbbq0sw.us.auth0.com"
        clientId="vqJ6wTK3EEsqMAXCFdGLZfBIwrMRrrZA"
        authorizationParams={{
          redirect_uri: `${window.location.origin}/dashboard`,
          audience: 'https://degreeflow-backend/api',
          scope: 'openid profile email read:data write:data'
        }}
        cacheLocation="localstorage"
        useRefreshTokens
      >
        <App />
      </Auth0Provider>
    </BrowserRouter>
  </React.StrictMode>
);
