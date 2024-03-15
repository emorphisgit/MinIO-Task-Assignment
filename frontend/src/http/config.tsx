import axios from "axios";

// const baseUrl = 'http://172.16.1.108:8080/';
const baseUrl = 'http://localhost:8080/';

const api = axios.create({
    baseURL: baseUrl
});

export default api;

export const getFilesUrl = 'minio/files'
export const downloadFilesUrl = 'minio/download'
export const searchFilesUrl = 'minio/search'
