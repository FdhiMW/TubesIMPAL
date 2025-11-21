import http from './httpClient'

export function login(email, password) {
  return http.post('/auth/login', { email, password })
}

export function register(namaLengkap, email, password) {
  return http.post('/auth/register', { namaLengkap, email, password })
}
