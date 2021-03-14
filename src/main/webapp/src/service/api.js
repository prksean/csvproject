import Axios from 'axios';

/**
 * Base instance for axios
 * @author Sihyun Park
 */
export const api = Axios.create({
    baseURL: "http://localhost:8090",
    headers: {
        "Content-type": "application/json"
    }
});