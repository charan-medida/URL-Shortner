import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './App.css';

export function App() {
    const navigate = useNavigate();

    useEffect(() => {
        if (localStorage.getItem('token') !== null) {
        navigate('/url'); 
        } else {
        navigate('/login');
        }
    }, [navigate]);

    return (
        <div>
        {}
        </div>
    );
}

export default App;