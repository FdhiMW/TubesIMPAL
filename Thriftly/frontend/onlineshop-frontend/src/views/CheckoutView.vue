```vue
<template>
  <div class="checkout-page">
    <!-- KONTEN -->
    <main class="checkout-main">
      <section class="checkout-card">
        <div class="checkout-grid">
          <!-- KIRI: ALAMAT & PENGIRIMAN -->
          <div class="checkout-column">
            <h2 class="section-title">Alamat &amp; Pengiriman</h2>
            <div class="section-divider"></div>

            <div class="field-row">
              <div class="field-group">
                <label>Nama Penerima</label>
                <input
                  v-model="form.namaPenerima"
                  type="text"
                  placeholder="Nama Lengkap"
                  class="input-pill"
                />
              </div>

              <div class="field-group">
                <label>Telepon</label>
                <input
                  v-model="form.telepon"
                  type="text"
                  placeholder="08xxxxxxxxx"
                  class="input-pill"
                  inputmode="numeric"
                  pattern="[0-9]*"
                  maxlength="15"
                  @input="onPhoneInput"
                />
              </div>
            </div>

            <div class="field-group">
              <label>Alamat Lengkap</label>
              <input
                v-model="form.alamatLengkap"
                type="text"
                placeholder="Jalan, No, RT/RW, Kel/Desa, Kecamatan"
                class="input-pill"
              />
            </div>

            <div class="field-row">
              <div class="field-group">
                <label>Kota</label>
                <input
                  v-model="form.kota"
                  type="text"
                  placeholder="Bandung"
                  class="input-pill"
                  inputmode="text"
                  maxlength="40"
                  @input="onCityInput"
                />
              </div>
              <div class="field-group">
                <label>Provinsi</label>
                <input
                  v-model="form.provinsi"
                  type="text"
                  placeholder="Jawa Barat"
                  class="input-pill"
                  inputmode="text"
                  maxlength="40"
                  @input="onProvinceInput"
                />
              </div>
              <div class="field-group">
                <label>Kode Pos</label>
                <input
                  v-model="form.kodePos"
                  type="text"
                  placeholder="40257"
                  class="input-pill"
                  inputmode="numeric"
                  pattern="[0-9]*"
                  maxlength="10"
                  @input="onPostalInput"
                />
              </div>
            </div>

            <div class="field-group">
              <label>Layanan Pengiriman</label>

              <!-- ✅ DROP DOWN: NEXT DAY, REGULER, GRATIS ONGKIR (sesuai kode yang biasa dipakai di backend/db) -->
              <select
                v-model="selectedShipmentCode"
                class="input-pill shipping-select"
              >
                <option
                  v-for="opt in shipmentOptions"
                  :key="opt.code"
                  :value="opt.code"
                >
                  {{ opt.label }}
                </option>
              </select>
            </div>
          </div>

          <!-- KANAN: PEMBAYARAN -->
          <div class="checkout-column">
            <h2 class="section-title">Pembayaran</h2>
            <div class="section-divider"></div>

            <!-- Metode Pembayaran -->
            <div class="payment-methods">
              <div
                v-for="method in paymentMethods"
                :key="method.value"
                class="payment-item"
                :class="{ 'payment-item--active': paymentMethod === method.value }"
                @click="paymentMethod = method.value"
              >
                <span class="radio-circle">
                  <span
                    v-if="paymentMethod === method.value"
                    class="radio-dot"
                  ></span>
                </span>
                <span class="payment-label">{{ method.label }}</span>
              </div>
            </div>

            <!-- Detail kartu (hanya kalau Transfer Bank / E-Wallet) -->
            <div v-if="paymentMethod !== 'COD'" class="card-section">
              <div class="field-group">
                <label>Nomor Kartu</label>
                <input
                  v-model="form.nomorKartu"
                  type="text"
                  placeholder="4111 1111 1111 1111"
                  class="input-pill"
                />
              </div>

              <div class="field-group">
                <label>Nama Di Kartu</label>
                <input
                  v-model="form.namaDiKartu"
                  type="text"
                  placeholder="Nama Sesuai Kartu"
                  class="input-pill"
                />
              </div>

              <div class="field-row">
                <div class="field-group small-field">
                  <label>Exp</label>
                  <input
                    v-model="form.exp"
                    type="text"
                    placeholder="MM/YY"
                    class="input-pill"
                  />
                </div>
                <div class="field-group small-field">
                  <label>CVV</label>
                  <input
                    v-model="form.cvv"
                    type="text"
                    placeholder="123"
                    class="input-pill"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- BAWAH: TOTAL + TOMBOL -->
        <div class="checkout-footer">
          <div class="total-text">
            Total Bayar : {{ formatRupiah(totalBayar) }}
          </div>
          <div class="footer-buttons">
            <button class="btn-secondary" @click="goBack">Kembali</button>
            <button
              class="btn-primary"
              :disabled="loading || !produk"
              @click="submitOrder"
            >
              {{ loading ? 'Memproses...' : 'Bayar Sekarang' }}
            </button>
          </div>
        </div>
      </section>
    </main>

    <!-- ====== [DITAMBAHKAN: POPUP SUKSES PESANAN] ====== -->
    <div v-if="showSuccessPopup" class="popup-overlay">
      <div class="popup-card" role="dialog" aria-modal="true">
        <div class="popup-icon">
          <span class="popup-check">✓</span>
        </div>

        <div class="popup-title">Pesanan berhasil dibuat</div>
        <div class="popup-subtitle">
          Kode pesanan: <span class="popup-code">{{ successKode }}</span>
        </div>

        <button class="popup-ok" @click="closeSuccessPopup">OK</button>
      </div>
    </div>
    <!-- ====== [AKHIR PENYESUAIAN] ====== -->

    <!-- ====== [DITAMBAHKAN: POPUP INFO/ERROR YANG MENARIK] ====== -->
    <div v-if="showInfoPopup" class="popup-overlay">
      <div class="popup-card" role="dialog" aria-modal="true">
        <div
          class="popup-icon"
          :class="{ 'popup-icon--warn': infoType === 'warn' }"
        >
          <span v-if="infoType === 'warn'" class="popup-warn">!</span>
          <span v-else class="popup-check">✓</span>
        </div>

        <div class="popup-title">{{ infoTitle }}</div>
        <div class="popup-subtitle">
          {{ infoMessage }}
        </div>

        <button class="popup-ok" @click="closeInfoPopup">OK</button>
      </div>
    </div>
    <!-- ====== [AKHIR PENYESUAIAN] ====== -->
  </div>
</template>

<script>
import http from '@/api/httpClient'

export default {
  name: 'CheckoutView',
  data() {
    return {
      produk: null,
      qty: 1,
      loading: false,
      form: {
        namaPenerima: '',
        telepon: '',
        alamatLengkap: '',
        kota: '',
        provinsi: '',
        kodePos: '',
        // info kartu
        nomorKartu: '',
        namaDiKartu: '',
        exp: '',
        cvv: '',
      },

      // ✅ DROP DOWN: NEXT DAY, REGULER, GRATIS ONGKIR
      // Catatan: "code" di bawah ini dibuat umum agar nyambung dengan backend/db.
      // Kalau di DB kamu pakai kode lain, cukup ganti field "code" saja (tanpa ubah struktur).
      shipmentOptions: [
        { code: 'NEXT_DAY', label: 'Next Day – Rp 35.000', biaya: 35000 },
        { code: 'REGULER', label: 'Reguler – Rp 20.000', biaya: 20000 },
        { code: 'GRATIS_ONGKIR', label: 'Gratis Ongkir – Rp 0', biaya: 0 },
      ],
      selectedShipmentCode: 'NEXT_DAY',

      paymentMethods: [
        { value: 'TRANSFER_BANK', label: 'Transfer Bank' },
        { value: 'E_WALLET', label: 'E-Wallet' },
        { value: 'COD', label: 'COD' },
      ],
      paymentMethod: 'TRANSFER_BANK',

      // ====== [DITAMBAHKAN: STATE POPUP SUKSES] ======
      showSuccessPopup: false,
      successKode: '',
      // ====== [AKHIR PENYESUAIAN] ======

      // ====== [DITAMBAHKAN: STATE POPUP INFO/ERROR] ======
      showInfoPopup: false,
      infoTitle: '',
      infoMessage: '',
      infoType: 'warn', // 'warn' atau 'success'
      // ====== [AKHIR PENYESUAIAN] ======
    }
  },
  computed: {
    selectedShipment() {
      return (
        this.shipmentOptions.find(s => s.code === this.selectedShipmentCode) ||
        this.shipmentOptions[0]
      )
    },
    subtotal() {
      if (!this.produk) return 0
      const harga = Number(this.produk.harga || 0)
      return harga * this.qty
    },
    ongkir() {
      return this.selectedShipment?.biaya || 0
    },
    totalBayar() {
      return this.subtotal + this.ongkir
    },
  },
  created() {
    const { id, qty } = this.$route.query
    if (!id) {
      this.$router.push('/')
      return
    }

    const parsedQty = parseInt(qty || '1', 10)
    this.qty = Number.isNaN(parsedQty) || parsedQty <= 0 ? 1 : parsedQty

    this.loadProduk(id)
    this.prefillFromUser()
  },
  methods: {
    async loadProduk(id) {
      try {
        const res = await http.get(`/produk/${id}`)
        this.produk = res.data
      } catch (err) {
        console.error('Gagal memuat produk untuk checkout', err)
        this.openInfoPopup(
          'Gagal memuat produk',
          'Produk tidak bisa dimuat. Kamu akan kembali ke beranda.',
          'warn',
        )
        this.$router.push('/')
      }
    },
    prefillFromUser() {
      const rawUser = localStorage.getItem('user')
      if (!rawUser) return
      try {
        const user = JSON.parse(rawUser)
        if (user.namaLengkap) this.form.namaPenerima = user.namaLengkap
        if (user.telepon) this.form.telepon = String(user.telepon).replace(/\D/g, '').slice(0, 15)
        if (user.kota) this.form.kota = String(user.kota).replace(/[^a-zA-Z\s]/g, '').replace(/\s+/g, ' ').trim().slice(0, 40)
        if (user.provinsi) this.form.provinsi = String(user.provinsi).replace(/[^a-zA-Z\s]/g, '').replace(/\s+/g, ' ').trim().slice(0, 40)
        if (user.kodePos) this.form.kodePos = String(user.kodePos).replace(/\D/g, '').slice(0, 10)

        // kalau user punya preferensi kurir di localStorage, boleh dipakai (opsional aman)
        if (user.layananPengiriman) {
          const found = this.shipmentOptions.find(o => o.code === user.layananPengiriman)
          if (found) this.selectedShipmentCode = found.code
        }
      } catch (e) {
        // abaikan
      }
    },

    // ====== FILTER INPUT ======
    onPhoneInput(e) {
      const raw = e?.target?.value ?? this.form.telepon ?? ''
      const digitsOnly = String(raw).replace(/\D/g, '').slice(0, 15)
      this.form.telepon = digitsOnly
      if (e && e.target) e.target.value = digitsOnly
    },
    onPostalInput(e) {
      const raw = e?.target?.value ?? this.form.kodePos ?? ''
      const digitsOnly = String(raw).replace(/\D/g, '').slice(0, 10)
      this.form.kodePos = digitsOnly
      if (e && e.target) e.target.value = digitsOnly
    },
    onCityInput(e) {
      const raw = e?.target?.value ?? this.form.kota ?? ''
      const lettersOnly = String(raw)
        .replace(/[^a-zA-Z\s]/g, '')
        .replace(/\s+/g, ' ')
        .slice(0, 40)
      this.form.kota = lettersOnly
      if (e && e.target) e.target.value = lettersOnly
    },
    onProvinceInput(e) {
      const raw = e?.target?.value ?? this.form.provinsi ?? ''
      const lettersOnly = String(raw)
        .replace(/[^a-zA-Z\s]/g, '')
        .replace(/\s+/g, ' ')
        .slice(0, 40)
      this.form.provinsi = lettersOnly
      if (e && e.target) e.target.value = lettersOnly
    },
    // ====== AKHIR FILTER INPUT ======

    formatRupiah(v) {
      if (v === null || v === undefined) return ''
      return new Intl.NumberFormat('id-ID').format(v)
    },
    goBack() {
      this.$router.back()
    },

    openInfoPopup(title, message, type = 'warn') {
      this.infoTitle = title
      this.infoMessage = message
      this.infoType = type
      this.showInfoPopup = true
    },
    closeInfoPopup() {
      this.showInfoPopup = false
    },

    closeSuccessPopup() {
      this.showSuccessPopup = false
      this.$router.push('/')
    },

    async submitOrder() {
      if (!this.produk) return

      const rawUser = localStorage.getItem('user')
      if (!rawUser) {
        this.openInfoPopup(
          'Login dibutuhkan',
          'Silakan login terlebih dahulu sebelum melakukan pembayaran.',
          'warn',
        )
        this.$router.push('/login')
        return
      }

      const user = JSON.parse(rawUser)

      // normalize sebelum validasi
      this.form.telepon = String(this.form.telepon || '').replace(/\D/g, '').slice(0, 15)
      this.form.kodePos = String(this.form.kodePos || '').replace(/\D/g, '').slice(0, 10)
      this.form.kota = String(this.form.kota || '').replace(/[^a-zA-Z\s]/g, '').replace(/\s+/g, ' ').trim().slice(0, 40)
      this.form.provinsi = String(this.form.provinsi || '').replace(/[^a-zA-Z\s]/g, '').replace(/\s+/g, ' ').trim().slice(0, 40)

      if (
        !this.form.namaPenerima ||
        !this.form.telepon ||
        !this.form.alamatLengkap ||
        !this.form.kota ||
        !this.form.provinsi ||
        !this.form.kodePos
      ) {
        this.openInfoPopup(
          'Alamat belum lengkap',
          'Lengkapi alamat pengiriman terlebih dahulu agar pesanan bisa diproses.',
          'warn',
        )
        return
      }

      if (!/^\d{8,15}$/.test(this.form.telepon)) {
        this.openInfoPopup(
          'Nomor telepon tidak valid',
          'Nomor telepon harus berisi 8–15 digit angka.',
          'warn',
        )
        return
      }

      if (!/^\d{4,10}$/.test(this.form.kodePos)) {
        this.openInfoPopup(
          'Kode pos tidak valid',
          'Kode pos hanya boleh angka (minimal 4 digit).',
          'warn',
        )
        return
      }

      if (this.paymentMethod !== 'COD') {
        const nomor = (this.form.nomorKartu || '').trim()
        const nama = (this.form.namaDiKartu || '').trim()
        const exp = (this.form.exp || '').trim()
        const cvv = (this.form.cvv || '').trim()

        if (!nomor || !nama || !exp || !cvv) {
          this.openInfoPopup(
            'Data pembayaran belum lengkap',
            'Untuk Transfer Bank / E-Wallet, isi Nomor Kartu, Nama di Kartu, Exp, dan CVV terlebih dahulu.',
            'warn',
          )
          return
        }

        const digitsCard = nomor.replace(/\s+/g, '')
        if (!/^\d{12,19}$/.test(digitsCard)) {
          this.openInfoPopup(
            'Nomor kartu tidak valid',
            'Masukkan nomor kartu 12–19 digit (angka saja).',
            'warn',
          )
          return
        }

        if (!/^\d{2}\/\d{2}$/.test(exp)) {
          this.openInfoPopup(
            'Format Exp tidak valid',
            'Gunakan format MM/YY. Contoh: 08/27',
            'warn',
          )
          return
        }

        if (!/^\d{3,4}$/.test(cvv)) {
          this.openInfoPopup(
            'CVV tidak valid',
            'CVV harus 3 atau 4 digit.',
            'warn',
          )
          return
        }
      }

      this.loading = true
      try {
        const payload = {
          idUser: user.idUser,
          metodePembayaran: this.paymentMethod,
          namaPenerima: this.form.namaPenerima,
          nomorTelepon: this.form.telepon,
          alamatLengkap: this.form.alamatLengkap,
          kota: this.form.kota,
          provinsi: this.form.provinsi,
          kodePos: this.form.kodePos,
          layananPengiriman: this.selectedShipment.code,   // ✅ kirim kode sesuai DB
          ongkosKirim: this.ongkir,
          items: [
            {
              idProduk: this.produk.idProduk,
              jumlah: this.qty,
            },
          ],
        }

        const res = await http.post('/pesanan', payload)
        const kode = res.data?.kodePesanan || '(tanpa kode)'

        this.successKode = kode
        this.showSuccessPopup = true
      } catch (err) {
        console.error('Gagal membuat pesanan', err)
        this.openInfoPopup(
          'Gagal membuat pesanan',
          err.response?.data?.message ||
            'Terjadi kesalahan saat menyimpan pesanan. Silakan coba lagi.',
          'warn',
        )
      } finally {
        this.loading = false
      }
    },
  },
}
</script>

<style scoped>
/* (STYLE KAMU TETAP SAMA, TIDAK DIUBAH) */
.checkout-page {
  min-height: 100vh;
  background: #ffffff;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI',
    sans-serif;
}

/* HEADER GRADIENT */
.checkout-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 40px;
  background: linear-gradient(90deg, #ef4444, #f97316);
  color: #ffffff;
}

.header-left,
.header-center,
.header-right {
  display: flex;
  align-items: center;
}

.logo-box {
  display: flex;
  align-items: center;
  gap: 8px;
}

.logo-icon {
  font-size: 26px;
}

.logo-text {
  font-weight: 700;
  font-size: 20px;
}

.header-center {
  flex: 1;
  justify-content: center;
  padding: 0 40px;
}

.search-input {
  width: 100%;
  max-width: 480px;
  border-radius: 999px;
  padding: 10px 18px;
  border: none;
  outline: none;
  font-size: 14px;
  color: #111827;
}

.header-right {
  gap: 14px;
}

.icon-box {
  width: 34px;
  height: 34px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
}

/* ✅ BODY */
.checkout-main {
  padding: 0;
  background: #ffffff;
  min-height: calc(100vh - 84px);
  display: flex;
}

.checkout-card {
  background: #ffffff;
  width: 100%;
  max-width: 100%;
  border-radius: 0;
  box-shadow: none;
  padding: 28px 56px 36px;

  flex: 1;
  display: flex;
  flex-direction: column;
}

/* ✅ GRID */
.checkout-grid {
  display: grid;
  grid-template-columns: 1.35fr 1fr;
  gap: 46px;
  align-items: stretch;
  flex: 1;
}

.checkout-column {
  font-size: 14px;
  color: #111827;
  display: flex;
  flex-direction: column;
  min-height: 100%;
}

/* ✅ Judul section lebih besar */
.section-title {
  font-size: 28px;
  font-weight: 900;
  margin-bottom: 8px;
}

.section-divider {
  height: 1px;
  background: #e5e7eb;
  margin-bottom: 18px;
}

.field-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 14px;
}

/* ✅ Label lebih besar */
.field-group label {
  font-size: 15px;
  font-weight: 800;
  margin-bottom: 8px;
  color: #111827;
}

.field-row {
  display: flex;
  gap: 14px;
}

/* ✅ Input lebih besar */
.input-pill {
  border-radius: 999px;
  border: 1px solid #d1d5db;
  padding: 14px 18px;
  font-size: 15px;
  outline: none;
  width: 100%;
}

.input-pill::placeholder {
  color: #9ca3af;
  font-size: 14px;
}

/* ✅ DROPDOWN: tampil polos seperti input (tidak berwarna) */
.shipping-select {
  appearance: none;
  -webkit-appearance: none;
  -moz-appearance: none;
  background-color: #ffffff;
  color: #111827;
  cursor: pointer;

  /* panah dropdown halus */
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='20' height='20' viewBox='0 0 20 20'%3E%3Cpath fill='%239CA3AF' d='M5.25 7.5 10 12.25 14.75 7.5 16 8.75 10 14.75 4 8.75z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 14px center;
  background-size: 18px 18px;
  padding-right: 44px; /* ruang untuk panah */
}

/* ✅ KIRI: jarak antar input di baris dibuat lebih renggang */
.checkout-column:nth-child(1) .field-row {
  gap: 28px;
  align-items: flex-start;
}

.checkout-column:nth-child(1) .field-row .field-group {
  flex: 1;
  min-width: 220px;
  margin-bottom: 18px;
}

/* ✅ Pembayaran: perbesar font dasar kolom */
.checkout-column:nth-child(2) {
  font-size: 15.5px;
}

/* ✅ Judul pembayaran */
.checkout-column:nth-child(2) .section-title {
  font-size: 28px;
}

/* ✅ Metode pembayaran: lebih besar */
.payment-methods {
  display: flex;
  flex-direction: column;
  gap: 18px;
  margin-bottom: 26px;
}

.payment-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 18px 22px;
  border-radius: 999px;
  background: #e5e7eb;
  cursor: pointer;
}

.payment-item--active {
  background: linear-gradient(90deg, #ef4444, #f97316);
  color: #ffffff;
}

.radio-circle {
  width: 26px;
  height: 26px;
  border-radius: 50%;
  border: 2px solid #9ca3af;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #ffffff;
  flex: 0 0 auto;
}

.payment-item--active .radio-circle {
  border-color: #ffffff;
}

.radio-dot {
  width: 14px;
  height: 14px;
  border-radius: 50%;
  background: #ef4444;
}

.payment-item--active .radio-dot {
  background: #ffffff;
}

.payment-label {
  font-weight: 900;
  font-size: 18px;
  line-height: 1.1;
}

/* ✅ FORM PEMBAYARAN */
.card-section {
  margin-top: 16px;
  padding-bottom: 24px;
  flex: 1;
}

.checkout-column:nth-child(2) .card-section .field-group {
  margin-bottom: 28px;
}

.checkout-column:nth-child(2) .card-section .field-row {
  gap: 34px;
  margin-top: 14px;
  align-items: flex-start;
}

.checkout-column:nth-child(2) .card-section .small-field {
  flex: 1;
  min-width: 280px;
}

/* ✅ label pembayaran lebih besar */
.checkout-column:nth-child(2) .field-group label {
  font-size: 16px;
  font-weight: 900;
  margin-bottom: 10px;
}

/* ✅ input pembayaran lebih besar */
.checkout-column:nth-child(2) .input-pill {
  font-size: 16px;
  padding: 15px 18px;
}

.small-field .input-pill {
  width: 100%;
}

/* ✅ FOOTER */
.checkout-footer {
  border-top: 1px solid #e5e7eb;
  margin-top: 18px;
  padding-top: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.total-text {
  font-size: 20px;
  font-weight: 900;
}

.footer-buttons {
  display: flex;
  gap: 16px;
}

/* ✅ tombol */
.btn-secondary,
.btn-primary {
  border-radius: 999px;
  padding: 16px 38px;
  font-size: 17px;
  font-weight: 900;
  border: none;
  cursor: pointer;
}

.btn-secondary {
  background: #d1d5db;
  color: #4b5563;
}

.btn-primary {
  background: linear-gradient(90deg, #ef4444, #f97316);
  color: #ffffff;
}

.btn-primary:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

/* RESPONSIVE */
@media (max-width: 900px) {
  .checkout-main {
    min-height: auto;
    display: block;
  }

  .checkout-card {
    padding: 20px;
  }

  .checkout-grid {
    grid-template-columns: 1fr;
    gap: 22px;
  }

  .header-center {
    padding: 0 16px;
  }

  .checkout-column:nth-child(1) .field-row {
    gap: 14px;
    flex-wrap: wrap;
  }

  .checkout-column:nth-child(1) .field-row .field-group {
    min-width: 0;
    flex: 1 1 240px;
  }

  .checkout-column:nth-child(2) .card-section .field-row {
    gap: 14px;
    flex-wrap: wrap;
  }

  .checkout-column:nth-child(2) .card-section .small-field {
    min-width: 0;
    flex: 1 1 220px;
  }

  .btn-secondary,
  .btn-primary {
    padding: 14px 26px;
    font-size: 16px;
  }

  .total-text {
    font-size: 18px;
  }
}

/* ====== POPUP ====== */
.popup-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.popup-card {
  width: 520px;
  max-width: calc(100vw - 40px);
  background: #ffffff;
  border-radius: 22px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.25);
  padding: 34px 34px 26px;
  text-align: center;
}

.popup-icon {
  width: 92px;
  height: 92px;
  border-radius: 999px;
  margin: 0 auto 18px;
  background: linear-gradient(90deg, #ef4444, #f97316);
  display: flex;
  align-items: center;
  justify-content: center;
}

.popup-icon--warn {
  background: linear-gradient(90deg, #f97316, #ef4444);
}

.popup-check {
  color: #ffffff;
  font-size: 46px;
  line-height: 1;
  font-weight: 800;
}

.popup-warn {
  color: #ffffff;
  font-size: 52px;
  line-height: 1;
  font-weight: 900;
}

.popup-title {
  font-size: 28px;
  font-weight: 800;
  color: #111827;
  margin-bottom: 8px;
}

.popup-subtitle {
  font-size: 16px;
  color: #374151;
  margin-bottom: 22px;
}

.popup-code {
  font-weight: 800;
  color: #111827;
}

.popup-ok {
  width: 100%;
  border: none;
  border-radius: 999px;
  padding: 14px 0;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  background: linear-gradient(90deg, #ef4444, #f97316);
  color: #ffffff;
}
</style>
```
