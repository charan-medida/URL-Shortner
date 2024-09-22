import React from "react";
import { useLocation, useNavigate } from 'react-router-dom';
import './App.css';

const SmallUrl = () => {
    const location = useLocation();
    const { shortUrl } = location.state || {}; 
    const navigate = useNavigate();
    const handleCopy = () => {
        navigator.clipboard.writeText(shortUrl)
        navigate('/url');
    };

    return (
        <div className='container'>
            <div className='App'>
                <h1 style={{ color: "blue" }}>Short URL</h1>
                <form className='form' >
                    <h1>Your shortened URL</h1>
                    <div className='temp'>
                        <input
                            type="text"
                            value={shortUrl}
                            readOnly
                        />
                        <button type="button" onClick={handleCopy}>Copy URL</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default SmallUrl;
