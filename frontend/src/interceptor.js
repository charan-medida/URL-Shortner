import axios from "axios";

const axiosInstance  = axios.create({
    baseURL: 'https://url-shortner-8jno.onrender.com'
});


axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if(token){
            config.headers['Authorization'] = `Bearer ${token}`
        } else {
            config.headers['Authorization'] = '';
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default axiosInstance;
