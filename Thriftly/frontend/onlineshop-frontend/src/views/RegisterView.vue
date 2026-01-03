<template>
  <div class="page">
    <div class="content">
      <div class="left">
        <div class="brand">
          <img
            src="@/assets/foto/logo.jpg"
            alt="Thriftly Logo"
            class="logo"
          />
          <div class="brand-name">Thriftly</div>
        </div>
      </div>

      <div class="right">
        <div class="card">
          <h2>Daftar</h2>
          <form @submit.prevent="onSubmit">
            <div class="field">
              <input
                v-model="namaLengkap"
                type="text"
                placeholder="Nama Lengkap"
              />
            </div>
            <div class="field">
              <input
                v-model="email"
                type="email"
                placeholder="Masukkan Email"
              />
            </div>
            <div class="field">
              <input
                v-model="password"
                type="password"
                placeholder="Masukkan Password"
              />
            </div>
            <div class="field">
              <input
                v-model="confirmPassword"
                type="password"
                placeholder="Konfirmasi Password"
              />
            </div>

            <button type="submit" class="btn-daftar" :disabled="loading">
              {{ loading ? 'Memproses...' : 'Daftar' }}
            </button>

            <p class="bottom-text">
              Sudah punya akun?
              <router-link to="/login">Login</router-link>
            </p>

            <p v-if="error" class="error">{{ error }}</p>
          </form>
        </div>
      </div>
    </div>

    <!-- ====== [DITAMBAHKAN: POPUP SUKSES] ====== -->
    <div
      v-if="showSuccessPopup"
      class="popup-overlay"
      @click.self="onSuccessOk"
    >
      <div class="popup-card" role="dialog" aria-modal="true">
        <div class="popup-icon">
          <svg
            width="44"
            height="44"
            viewBox="0 0 24 24"
            fill="none"
            aria-hidden="true"
          >
            <path
              d="M20 6L9 17L4 12"
              stroke="white"
              stroke-width="2.5"
              stroke-linecap="round"
              stroke-linejoin="round"
            />
          </svg>
        </div>

        <div class="popup-title">
          Berhasil registrasi<br />
          akun
        </div>

        <button class="popup-btn" @click="onSuccessOk">
          OK
        </button>
      </div>
    </div>
    <!-- ====== [AKHIR PENYESUAIAN] ====== -->
  </div>
</template>

<script>
import Navbar from '../components/layout/Navbar.vue'
import { register } from '../api/authApi'

export default {
  name: 'RegisterView',
  components: { Navbar },
  data() {
    return {
      namaLengkap: '',
      email: '',
      password: '',
      confirmPassword: '',
      loading: false,
      error: '',

      // ====== [DITAMBAHKAN: STATE POPUP SUKSES] ======
      showSuccessPopup: false,
      // ====== [AKHIR PENYESUAIAN] ======
    }
  },
  methods: {
    // ====== [DITAMBAHKAN: VALIDASI EMAIL] ======
    isValidEmail(email) {
      const v = (email || '').trim()
      return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v)
    },
    // ====== [AKHIR PENYESUAIAN] ======

    // ====== [DITAMBAHKAN: AKSI OK POPUP] ======
    onSuccessOk() {
      this.showSuccessPopup = false

      // ====== [DITAMBAHKAN: HINDARI LOOP NAVIGATION GUARD] ======
      // Pastikan status "login" tidak aktif saat menuju /login
      localStorage.removeItem('user')

      // gunakan replace agar tidak menambah history & hindari error redirect
      this.$router.replace('/login').catch(() => {})
      // ====== [AKHIR PENYESUAIAN] ======
    },
    // ====== [AKHIR PENYESUAIAN] ======

    async onSubmit() {
      this.error = ''

      // ====== [DITAMBAHKAN: VALIDASI WAJIB ISI + FORMAT] ======
      const nama = (this.namaLengkap || '').trim()
      const emailTrim = (this.email || '').trim()
      const pass = this.password || ''
      const confirm = this.confirmPassword || ''

      if (!nama) {
        this.error = 'Nama lengkap wajib diisi'
        return
      }
      if (!emailTrim) {
        this.error = 'Email wajib diisi'
        return
      }
      if (!this.isValidEmail(emailTrim)) {
        this.error = 'Email tidak valid (harus mengandung "@" dan format email)'
        return
      }
      if (!pass) {
        this.error = 'Password wajib diisi'
        return
      }
      if (pass.length < 8) {
        this.error = 'Password minimal 8 karakter'
        return
      }
      if (pass !== confirm) {
        this.error = 'Password dan konfirmasi tidak sama'
        return
      }
      // ====== [AKHIR PENYESUAIAN] ======

      this.loading = true
      try {
        const res = await register(nama, emailTrim, pass)

        // struktur tetap ada: simpan user
        localStorage.setItem('user', JSON.stringify(res.data))

        // ====== [DITAMBAHKAN: HINDARI ROUTER GUARD MENGANGGAP SUDAH LOGIN] ======
        // Karena setelah registrasi kamu ingin balik ke login, bukan dianggap login otomatis
        localStorage.removeItem('user')
        // ====== [AKHIR PENYESUAIAN] ======

        // ====== [DIUBAH: TAMPILKAN POPUP SUKSES, OK -> /login] ======
        this.showSuccessPopup = true
        // ====== [AKHIR PENYESUAIAN] ======
      } catch (e) {
        this.error = e.response?.data?.message || 'Gagal mendaftar, coba lagi'
      } finally {
        this.loading = false
      }
    },
  },
}
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.content {
  flex: 1;
  display: flex;
  padding: 40px 80px;
}

.left,
.right {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.brand {
  text-align: center;
}

.logo {
  height: 150px;
  width: auto;
  object-fit: contain;
}

.brand-name {
  font-size: 48px;
  font-weight: 800;
  margin-top: 16px;
}

.card {
  width: 360px;
  padding: 32px 28px;
  border-radius: 24px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.08);
  background: #ffffff;
}

.card h2 {
  margin-bottom: 20px;
  font-size: 24px;
}

.field {
  margin-bottom: 14px;
}

.field input {
  width: 100%;
  padding: 10px 14px;
  border-radius: 999px;
  border: 1.5px solid #d4d4d4;
  outline: none;
  font-size: 14px;
}

.btn-daftar {
  width: 100%;
  border: none;
  border-radius: 999px;
  padding: 10px 0;
  margin-top: 8px;
  background: linear-gradient(90deg, #ef4444, #f97316);
  color: #fff;
  font-weight: 600;
  cursor: pointer;
}

.bottom-text {
  margin-top: 24px;
  text-align: center;
  font-size: 13px;
}

.error {
  margin-top: 12px;
  color: #ef4444;
  font-size: 13px;
}

/* ====== [DITAMBAHKAN: STYLE POPUP SUKSES] ====== */
.popup-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 18px;
  z-index: 9999;
}

.popup-card {
  width: 560px;
  max-width: 92vw;
  background: #fff;
  border-radius: 26px;
  padding: 40px 28px 26px;
  box-shadow: 0 18px 50px rgba(0, 0, 0, 0.18);
  text-align: center;
}

.popup-icon {
  width: 110px;
  height: 110px;
  margin: 0 auto 18px;
  border-radius: 999px;
  background: linear-gradient(90deg, #f97316, #ef4444);
  display: flex;
  align-items: center;
  justify-content: center;
}

.popup-title {
  font-size: 34px;
  font-weight: 900;
  line-height: 1.15;
  margin-bottom: 22px;
  color: #111;
}

.popup-btn {
  width: 100%;
  border: none;
  border-radius: 16px;
  padding: 16px 0;
  background: linear-gradient(90deg, #f97316, #ef4444);
  color: #fff;
  font-weight: 900;
  font-size: 18px;
  cursor: pointer;
}
/* ====== [AKHIR PENYESUAIAN] ====== */
</style>
