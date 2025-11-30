
import axios from 'axios';

// The product-catalog service is running on port 8082
const API_URL = 'http://localhost:8082';

const productApi = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export default productApi;
