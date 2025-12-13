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
                />
              </div>
              <div class="field-group">
                <label>Provinsi</label>
                <input
                  v-model="form.provinsi"
                  type="text"
                  placeholder="Jawa Barat"
                  class="input-pill"
                />
              </div>
              <div class="field-group">
                <label>Kode Pos</label>
                <input
                  v-model="form.kodePos"
                  type="text"
                  placeholder="40257"
                  class="input-pill"
                />
              </div>
            </div>

            <div class="field-group">
              <label>Layanan Pengiriman</label>
              <div class="input-pill shipping-pill">
                {{ selectedShipment.label }}
              </div>
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
                  <span v-if="paymentMethod === method.value" class="radio-dot"></span>
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
            <button class="btn-secondary" @click="goBack">
              Kembali
            </button>
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
      shipmentOptions: [
        { code: 'NEXT_DAY', label: 'Next Day – Rp 35.000', biaya: 35000 },
      ],
      selectedShipmentCode: 'NEXT_DAY',
      paymentMethods: [
        { value: 'TRANSFER_BANK', label: 'Transfer Bank' },
        { value: 'E_WALLET', label: 'E-Wallet' },
        { value: 'COD', label: 'COD' },
      ],
      paymentMethod: 'TRANSFER_BANK',
    }
  },
  computed: {
    selectedShipment() {
      return (
        this.shipmentOptions.find(
          s => s.code === this.selectedShipmentCode,
        ) || this.shipmentOptions[0]
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
      // kalau tidak ada id produk, balik ke home
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
        alert('Gagal memuat produk, kembali ke beranda')
        this.$router.push('/')
      }
    },
    prefillFromUser() {
      const rawUser = localStorage.getItem('user')
      if (!rawUser) return
      try {
        const user = JSON.parse(rawUser)
        if (user.namaLengkap) {
          this.form.namaPenerima = user.namaLengkap
        }
      } catch (e) {
        // abaikan
      }
    },
    formatRupiah(v) {
      if (v === null || v === undefined) return ''
      return new Intl.NumberFormat('id-ID').format(v)
    },
    goBack() {
      this.$router.back()
    },
    async submitOrder() {
      if (!this.produk) return

      const rawUser = localStorage.getItem('user')
      if (!rawUser) {
        alert('Silakan login terlebih dahulu.')
        this.$router.push('/login')
        return
      }

      const user = JSON.parse(rawUser)

      // validasi sederhana
      if (
        !this.form.namaPenerima ||
        !this.form.telepon ||
        !this.form.alamatLengkap ||
        !this.form.kota ||
        !this.form.provinsi ||
        !this.form.kodePos
      ) {
        alert('Lengkapi alamat pengiriman terlebih dahulu.')
        return
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
          layananPengiriman: this.selectedShipment.label,
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

        alert('Pesanan berhasil dibuat. Kode pesanan: ' + kode)
        this.$router.push('/') // nanti bisa diarahkan ke halaman lacak pesanan
      } catch (err) {
        console.error('Gagal membuat pesanan', err)
        alert(
          err.response?.data?.message ||
            'Terjadi kesalahan saat menyimpan pesanan',
        )
      } finally {
        this.loading = false
      }
    },
  },
}
</script>

<style scoped>
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

/* BODY */
.checkout-main {
  padding: 28px 40px 40px;
}

.checkout-card {
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(15, 23, 42, 0.06);
  padding: 28px 32px 24px;
}

.checkout-grid {
  display: grid;
  grid-template-columns: 1.2fr 1fr;
  gap: 40px;
}

.checkout-column {
  font-size: 14px;
  color: #111827;
}

.section-title {
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 6px;
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

.field-group label {
  font-size: 13px;
  margin-bottom: 4px;
  color: #111827;
}

.field-row {
  display: flex;
  gap: 14px;
}

.input-pill {
  border-radius: 999px;
  border: 1px solid #d1d5db;
  padding: 10px 14px;
  font-size: 13px;
  outline: none;
}

.input-pill::placeholder {
  color: #9ca3af;
}

.shipping-pill {
  background: #ffffff;
}

/* PAYMENT */
.payment-methods {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 18px;
}

.payment-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 16px;
  border-radius: 999px;
  background: #e5e7eb;
  cursor: pointer;
}

.payment-item--active {
  background: linear-gradient(90deg, #ef4444, #f97316);
  color: #ffffff;
}

.radio-circle {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  border: 2px solid #9ca3af;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #ffffff;
}

.payment-item--active .radio-circle {
  border-color: #ffffff;
}

.radio-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: #ef4444;
}

.payment-item--active .radio-dot {
  background: #ffffff;
}

.payment-label {
  font-weight: 600;
  font-size: 14px;
}

.card-section {
  margin-top: 10px;
}

.small-field .input-pill {
  width: 100%;
}

/* FOOTER */
.checkout-footer {
  border-top: 1px solid #e5e7eb;
  margin-top: 20px;
  padding-top: 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.total-text {
  font-size: 16px;
  font-weight: 600;
}

.footer-buttons {
  display: flex;
  gap: 12px;
}

.btn-secondary,
.btn-primary {
  border-radius: 999px;
  padding: 10px 26px;
  font-size: 14px;
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

/* RESPONSIVE SEDIKIT */
@media (max-width: 900px) {
  .checkout-grid {
    grid-template-columns: 1fr;
  }

  .header-center {
    padding: 0 16px;
  }

  .checkout-main {
    padding: 20px;
  }
}
</style>
