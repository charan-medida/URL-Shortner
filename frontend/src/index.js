import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import App from './App';
import Error from './Error';
import './index.css';
import Login from './Login';
import ProtectedRoute from "./ProtectedRoute";
import Register from './Register';
import SmallUrl from './SmallUrl';
import Url from './Url';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<App />} />
      <Route path="smallurl" element={<SmallUrl/>}/>
      <Route path="error" element={<Error/>}/>
      <Route path="register" element={<Register/>}/>
      <Route path="login" element={<Login/>}/>
      <Route path="/url" element={<ProtectedRoute><Url /></ProtectedRoute>} />
    </Routes>
  </BrowserRouter>
);
