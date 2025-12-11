// src/api/api.js
import axios from 'axios'

// Instance axios utama untuk seluruh request ke backend
const api = axios.create({
  baseURL: 'http://localhost:8081/api', // sesuaikan port backend-mu
  // kalau butuh, bisa tambahkan header default di sini
  // headers: { 'Content-Type': 'application/json' },
})

export default api
