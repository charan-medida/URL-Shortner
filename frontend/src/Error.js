
import React from "react";
import { useNavigate } from 'react-router-dom';

const Error = () => {
    const navigate = useNavigate();
    return (
    
    
        <div className='App'>
            <img src="./images/error.png" alt="" /><br/><br/>
        
            <div className='form' >
            
                
            
        
                <div >
                    
                    <center><button type="submit" onClick={()=>{navigate("/")}}>Go Back</button></center>
        
                </div>
            </div>
        
        </div>
    
    );
};

export default Error;
