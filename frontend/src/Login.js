import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './App.css';
import axiosInstance from './interceptor';


export function Login() {
    const [formData, setFormData] = useState({
    username: '',
    password: '',
    });
    const [formErrors, setFormErrors] = useState({
    username: '',
    password: '',
    error: ''
    });

    const navigate = useNavigate();

    const updateFormData = (field, value) => {
    setFormData(prevData => ({
        ...prevData,
        [field]: value
    }));
    };

    const validateForm = () => {
    let valid = true;
    const newFormErrors = { ...formErrors };

    if (formData.username.trim() === '') {
        newFormErrors.username = 'Username is required';
        valid = false;
    }

    if (formData.password.trim() === '') {
        newFormErrors.password = 'Password is required';
        valid = false;
    }

    setFormErrors(newFormErrors);
    return valid;
    };

    const handleSubmit = async () => {

        if (validateForm()) {
            setFormErrors({ ...formErrors, error: '' }); 
            try {
                const response = await axiosInstance.post('/login', {
                    username: formData.username,
                    password: formData.password
                });
    
                const token = response.data;
                console.log(token);
                if (token) {
                    localStorage.setItem('token', token); 
                    navigate('/url');
                } else {
                    setFormErrors({ ...formErrors, error: 'Invalid Credentials' });
                }
            } catch (err) {
                console.error(err);
                setFormErrors({ ...formErrors, error: 'Invalid Credentials' });
            }
        }
    };
    
    const handleGuest = async () => {

        localStorage.setItem('guest',"guest");
        navigate('/url');
    };

    return (
    <div className="AppT">
        <img className="fullscreen-image" src="./images/task.jpg" alt='' />
        <div className="formCheck">
        <h2>Login Here</h2>
        <input
            type="text"
            id="username"
            value={formData.username}
            onChange={(e) => updateFormData('username', e.target.value)}
            placeholder="Enter username"
        />
        <span style={{ color: 'red' }}>{formErrors.username}</span><br />
        <input
            id="password"
            type="password"
            value={formData.password}
            onChange={(e) => updateFormData('password', e.target.value)}
            placeholder="Enter Password"
        />
        <span style={{ color: 'red' }}>{formErrors.password}</span><br /><br />
        
        
        
        
        <Link  onClick={(e) => { 
                                e.preventDefault(); 
                                handleSubmit();
                                                
                            }}  style={{color:"orange"}}> Login </Link><br/>
        <Link to='/register'>Don't have an account? Click register</Link><br />
        <Link onClick={(e) => {
            e.preventDefault();
            handleGuest();
        }}>Try as guest</Link><br/>
        {formErrors.error && (
            <div className="error-message" style={{ color: 'red', marginTop: '10px' }}>
            {formErrors.error}
            </div>
        )}
        </div>
    </div>
    );
}
export default Login;