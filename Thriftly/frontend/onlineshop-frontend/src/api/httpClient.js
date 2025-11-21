import axios from 'axios'

const http = axios.create({
  baseURL: 'http://localhost:8080/api', // backend Spring Boot
})

export default http
