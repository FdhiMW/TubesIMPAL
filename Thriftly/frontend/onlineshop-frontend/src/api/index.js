import axios from 'axios';

function resolveBaseURL() {
  // kalau frontend jalan di 8081 (Vue dev server), arahkan API ke 8080
  const { protocol, hostname, port } = window.location;

  // jika port kosong (prod/https default), fallback ke origin saja
  if (!port) return `${protocol}//${hostname}:8080`;

  // dev: frontend 8081 → backend 8080
  if (port === '8081') return `${protocol}//${hostname}:8080`;

  // kalau kamu memang jalankan frontend di 8080, biarkan origin
  return `${protocol}//${hostname}:${port}`;
}

const api = axios.create({
  baseURL: resolveBaseURL(),
  timeout: 30000,
});

export default api;
