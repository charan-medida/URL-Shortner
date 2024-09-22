import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function ProtectedRoute({ children }) {
    const navigate = useNavigate();
    
    useEffect(() => {
        const token = localStorage.getItem("token");

       
            
        if (!token && localStorage.getItem("guest")) {
        
            navigate('/url',{ state: { isAuthenticated: false } });
        } else {
    
        navigate('/url',{ state: { isAuthenticated: true } }); 
        }
    }, [navigate]);


    return localStorage.getItem("token") ||  localStorage.getItem("guest")? children : null;
}

export default ProtectedRoute;
