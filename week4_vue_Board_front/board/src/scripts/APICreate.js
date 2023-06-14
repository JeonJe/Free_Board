import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});


const multipartApi = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "multipart/form-data",
  },
});


// Add the following code to enable CORS
api.defaults.headers.common["Access-Control-Allow-Origin"] = "*";
api.defaults.headers.common["Access-Control-Allow-Methods"] = "GET, POST, OPTIONS,DELETE, PATCH, PUT";
api.defaults.headers.common["Access-Control-Allow-Headers"] = "Origin, Content-Type, Accept";

multipartApi.defaults.headers.common["Access-Control-Allow-Origin"] = "*";
multipartApi.defaults.headers.common["Access-Control-Allow-Methods"] = "GET, POST, OPTIONS,DELETE, PATCH, PUT";
multipartApi.defaults.headers.common["Access-Control-Allow-Headers"] = "Origin, Content-Type, Accept";

export { api, multipartApi };