import axios from "axios";


// JSON 콘텐츠를 위한 기본 URL과 헤더를 사용하여 axios 인스턴스 생성합니다.
const api = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

// multipart/form-data 콘텐츠를 위한 기본 URL과 헤더를 사용하여 axios 인스턴스 생성합니다.
const multipartApi = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "multipart/form-data",
  },
});


// CORS를 활성화하기 위한 다음 코드 추가
// API 요청에 대한 공통 헤더 설정
api.defaults.headers.common["Access-Control-Allow-Origin"] = "*";
api.defaults.headers.common["Access-Control-Allow-Methods"] = "GET, POST, OPTIONS,DELETE, PATCH, PUT";
api.defaults.headers.common["Access-Control-Allow-Headers"] = "Origin, Content-Type, Accept";

// multipart API 요청에 대한 공통 헤더 설정
multipartApi.defaults.headers.common["Access-Control-Allow-Origin"] = "*";
multipartApi.defaults.headers.common["Access-Control-Allow-Methods"] = "GET, POST, OPTIONS,DELETE, PATCH, PUT";
multipartApi.defaults.headers.common["Access-Control-Allow-Headers"] = "Origin, Content-Type, Accept";

export { api, multipartApi };