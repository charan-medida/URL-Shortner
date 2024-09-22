import React, { useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from 'react-router-dom';
import './App.css';
import axiosInstance from './interceptor';

const Url = () => {
    const [originalUrl, setOriginalUrl] = useState('');
    const [shortUrl, setShortUrl] = useState('');
    const [fetchData, setFetchData] = useState([]);
    const navigate = useNavigate();
    const location = useLocation();
    const isAuthenticated = location.state?.isAuthenticated
    const handleSubmit = async (e) => {
        e.preventDefault();

        try {

            const response = await axiosInstance.post('/shorten', originalUrl, {
                headers: {
                    'Content-Type': 'text/plain',
                    'Is-Authenticated': isAuthenticated.toString()
                }
            });

            if (isAuthenticated) {
                fetchUpdatedData();
            }

            const shortUrl = `http://localhost:8081/${response.data}`;
            setShortUrl(shortUrl);

            navigate('/smallurl', { state: { shortUrl } });

        } catch (error) {
            console.error('Error shortening URL:', error);
        }
    };

    const fetchUpdatedData = async () => {

        try {
            const response = await axiosInstance.get('/retrieve');
            setFetchData(response.data);
        } catch (err) {
            console.error('Error in fetching data:', err.response ? err.response.data : err.message);
        }
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };
    
    const handleLogin = () => {
        localStorage.removeItem('guest');
        navigate('/login');
    };

    useEffect(() => {

        if (isAuthenticated) {
            fetchUpdatedData();
        }
    }, [isAuthenticated, navigate]);
    return (
        <div className='container'>
            <div className='App'>
                <h1 style={{ color: "blue" }}>Short URL</h1>
                <form className='form' onSubmit={handleSubmit}>
                    <h1>Paste the URL to be shortened</h1>
                    <div className='temp'>
                        <input
                            type="text"
                            value={originalUrl}
                            onChange={(e) => setOriginalUrl(e.target.value)}
                            placeholder="Enter the link here"
                            required
                        />
                        <button type="submit">Shorten URL</button>
                    </div>
                </form>
                {shortUrl && (
                    <p>
                        {shortUrl}
                    </p>
                )}
                {!isAuthenticated && (
                    <div>
                        <Link onClick={(e) => {
                            e.preventDefault();
                            handleLogin();
                        }} >login</Link>
                    </div>
                )}
                {isAuthenticated && (
                    <div className="table-container">
                        <table className="table">
                            <thead>
                                <tr>
                                    <th className="td">Original URL</th>
                                    <th className="td">Short URL</th>
                                </tr>
                            </thead>
                            <tbody>
                                {Array.isArray(fetchData) && fetchData.length > 0 ? (
                                    fetchData.map((item) => (
                                        <tr key={item.Id}>
                                            <td className="td">{item.originalUrl}</td>
                                            <td className="td">{item.shortUrl}</td>
                                        </tr>
                                    ))
                                ) : (
                                    <tr>
                                        <td colSpan="2">No entries found</td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>
            {isAuthenticated && (
                <div>
                    <Link className="button-container" onClick={(e) => {
                        e.preventDefault();
                        handleLogout();
                    }} style={{ color: "red" }}>log out</Link>
                </div>
            )}

        </div>

    );
};

export default Url;
