import axios from 'axios';


const getToken = JSON.parse(sessionStorage.getItem("token"))?.jwt;
const callAPI = axios.create({
    baseURL: 'http://localhost:8080/',
    headers: {
        Authorization: `Bearer ${getToken}`,
        "Content-type": 'application/json'
    }
})
export default callAPI;
