import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

// Add the following code to enable CORS
api.defaults.headers.common["Access-Control-Allow-Origin"] = "*";
api.defaults.headers.common["Access-Control-Allow-Methods"] = "GET, POST, OPTIONS";
api.defaults.headers.common["Access-Control-Allow-Headers"] = "Origin, Content-Type, Accept";

export default api;