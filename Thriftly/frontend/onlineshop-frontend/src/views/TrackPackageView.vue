<template>
  <div class="page">

    <div class="container">
      <h2 class="title">Lacak Paket</h2>

      <div v-if="loading" class="state">Memuat...</div>
      <div v-else-if="error" class="state error">{{ error }}</div>
      <div v-else-if="items.length === 0" class="state">
        Belum ada pesanan.
      </div>

      <div v-else class="list">
        <div v-for="(it, idx) in items" :key="idx" class="card">
          <div class="thumb">
            <img :src="it.imageUrl || placeholder" alt="produk" @error="onImgError" />
          </div>

          <div class="info">
            <div class="name">{{ it.namaProduk || '-' }}</div>

            <div class="meta">
              <span>Ukuran: <b>{{ it.ukuran || '-' }}</b></span>
              <span class="dot">•</span>
              <span>Jenis: <b>{{ it.jenis || '-' }}</b></span>
              <span class="dot">•</span>
              <span>Harga: <b>{{ formatRupiah(it.harga) }}</b></span>
            </div>

            <div class="timeline">
              <div class="step">
                <span class="bullet" :class="bulletClass(it.statusPesanan, 'DIKEMAS')"></span>
                <span class="label">Dikemas</span>
              </div>
              <div class="line"></div>
              <div class="step">
                <span class="bullet" :class="bulletClass(it.statusPesanan, 'DALAM_PERJALANAN')"></span>
                <span class="label">Dalam Perjalanan</span>
              </div>
              <div class="line"></div>
              <div class="step">
                <span class="bullet" :class="bulletClass(it.statusPesanan, 'SELESAI')"></span>
                <span class="label">Selesai</span>
              </div>

              <div class="actions">
                <!-- ====== [DISESUAIKAN: BUTTON HANYA MUNCUL SAAT DALAM_PERJALANAN / SELESAI] ====== -->
                <button
                  v-if="canShowConfirmBtn(it.statusPesanan)"
                  class="btn"
                  :class="{ done: normalizeStatus(it.statusPesanan) === 'SELESAI' }"
                  :disabled="!canConfirm(it.statusPesanan) || confirmingId === it.idPesanan"
                  @click="confirmReceived(it)"
                  title="Hanya bisa jika status pesanan DALAM_PERJALANAN"
                >
                  {{ confirmingId === it.idPesanan ? 'Memproses...' : 'Pesanan Selesai' }}
                </button>
                <!-- ====== [AKHIR PENYESUAIAN] ====== -->
              </div>
            </div>

            <div class="order-code">
              Kode Pesanan: <b>{{ it.kodePesanan }}</b>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from '../components/layout/Navbar.vue'
import { fetchUserTracking, confirmUserOrderReceived } from '../api/pesananApi'

export default {
  name: 'TrackPackageView',
  components: { Navbar },
  data() {
    return {
      loading: true,
      error: '',
      items: [],
      confirmingId: null,
      placeholder:
        'data:image/svg+xml;charset=UTF-8,' +
        encodeURIComponent(
          `<svg xmlns="http://www.w3.org/2000/svg" width="200" height="200"><rect width="100%" height="100%" fill="#f3f4f6"/><text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" fill="#9ca3af" font-family="Arial" font-size="14">No Image</text></svg>`
        ),
    }
  },
  methods: {
    getUser() {
      const raw = localStorage.getItem('user')
      return raw ? JSON.parse(raw) : null
    },
    async load() {
      this.loading = true
      this.error = ''
      try {
        const user = this.getUser()
        if (!user?.idUser) {
          this.$router.push('/login')
          return
        }
        const res = await fetchUserTracking(user.idUser)
        this.items = Array.isArray(res.data) ? res.data : []
      } catch (e) {
        this.error = e.response?.data?.message || 'Gagal memuat data lacak paket'
      } finally {
        this.loading = false
      }
    },
    normalizeStatus(s) {
      return (s || '').trim().toUpperCase()
    },
    canConfirm(status) {
      return this.normalizeStatus(status) === 'DALAM_PERJALANAN'
    },

    // ====== [DITAMBAHKAN: TOMBOL MUNCUL HANYA SAAT DALAM_PERJALANAN / SELESAI] ======
    canShowConfirmBtn(status) {
      const s = this.normalizeStatus(status)
      return s === 'DALAM_PERJALANAN' || s === 'SELESAI'
    },
    // ====== [AKHIR PENYESUAIAN] ======

    bulletClass(currentStatus, step) {
      const cur = this.normalizeStatus(currentStatus)
      const order = ['DIKEMAS', 'DALAM_PERJALANAN', 'SELESAI']
      const curIdx = order.indexOf(cur)
      const stepIdx = order.indexOf(step)
      if (curIdx === -1) return 'off'
      return stepIdx <= curIdx ? 'on' : 'off'
    },
    formatRupiah(val) {
      const n = Number(val)
      if (Number.isNaN(n)) return 'Rp0'
      return new Intl.NumberFormat('id-ID', {
        style: 'currency',
        currency: 'IDR',
        minimumFractionDigits: 0,
      }).format(n)
    },
    onImgError(e) {
      e.target.src = this.placeholder
    },
    async confirmReceived(item) {
      if (!this.canConfirm(item.statusPesanan)) return
      const user = this.getUser()
      if (!user?.idUser) return this.$router.push('/login')

      try {
        this.confirmingId = item.idPesanan
        await confirmUserOrderReceived(user.idUser, item.idPesanan)
        item.statusPesanan = 'SELESAI'
      } catch (e) {
        alert(e.response?.data?.message || 'Gagal konfirmasi pesanan selesai')
      } finally {
        this.confirmingId = null
      }
    },
  },
  mounted() {
    this.load()
  },
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #ffffff;
}

.container {
  max-width: 1400px;
  width: calc(100% - 64px);
  margin: 0 auto;
  padding: 34px 32px 80px;
}

.title {
  font-size: 32px;
  font-weight: 900;
  margin: 8px 0 24px;
  letter-spacing: -0.02em;
}

.state {
  padding: 20px;
  border-radius: 16px;
  background: #f8fafc;
  color: #334155;
  font-size: 16px;
}

.state.error {
  background: #fee2e2;
  color: #991b1b;
}

.list {
  display: flex;
  flex-direction: column;
  gap: 22px;
}

.card {
  display: flex;
  gap: 22px;
  padding: 22px 0;
  border-bottom: 1px solid #e5e7eb;
}

.thumb {
  width: 190px;
  height: 150px;
  border-radius: 22px;
  overflow: hidden;
  background: linear-gradient(90deg, #ff5a3c, #ff9f1c);
  flex: 0 0 auto;
}

.thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.info {
  flex: 1;
  min-width: 0;
}

.name {
  font-size: 24px;
  font-weight: 900;
  margin-bottom: 10px;
  line-height: 1.15;
}

.meta {
  font-size: 15px;
  color: #475569;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  align-items: center;
}

.meta b { font-weight: 800; }
.dot { color: #cbd5e1; }

/* ✅ FIX: agar "Selesai" selalu sejajar & panjang sama (tidak terdorong tombol) */
.timeline {
  --stepW: 220px;   /* lebar tiap status sama */
  --lineW: 200px;   /* lebar garis sama */

  margin-top: 14px;

  /* 🔥 ubah jadi GRID agar 3 step + 2 line selalu fixed, tombol ada kolom sendiri */
  display: grid;
  grid-template-columns: var(--stepW) var(--lineW) var(--stepW) var(--lineW) var(--stepW) 1fr;
  align-items: center;
  column-gap: 16px;
}

.step {
  width: var(--stepW);
  display: flex;
  align-items: center;
  gap: 10px;
  white-space: nowrap;
}

.bullet {
  width: 14px;
  height: 14px;
  border-radius: 999px;
  display: inline-block;
}

.bullet.on { background: #22c55e; }
.bullet.off { background: #ef4444; }

.label {
  font-size: 14px;
  color: #334155;
  font-weight: 700;
}

.line {
  height: 3px;
  width: var(--lineW);
  background: #e2e8f0;
  border-radius: 999px;
}

/* tombol di kolom terakhir, jadi tidak menggeser posisi "Selesai" */
.actions {
  justify-self: end;
}

.btn {
  border: none;
  background: linear-gradient(90deg, #ef4444, #f97316);
  color: #fff;
  padding: 14px 20px;
  border-radius: 14px;
  font-weight: 900;
  font-size: 15px;
  cursor: pointer;
  min-width: 170px;
}

.btn:disabled { opacity: 0.5; cursor: not-allowed; }
.btn.done { background: #22c55e; }

.order-code {
  margin-top: 12px;
  font-size: 14px;
  color: #64748b;
}

/* RESPONSIVE */
@media (max-width: 1100px) {
  .timeline {
    /* kalau layar mengecil, biar tetap rapi: timeline boleh turun baris */
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 16px;

    --stepW: 190px;
    --lineW: 140px;
  }

  .step { width: var(--stepW); }
  .line { width: var(--lineW); }

  .actions {
    margin-left: auto;
    justify-self: unset;
  }
}

@media (max-width: 900px) {
  .container {
    max-width: 100%;
    width: 100%;
    padding: 22px 18px 60px;
  }

  .title { font-size: 26px; }

  .card {
    gap: 16px;
    padding: 18px 0;
  }

  .thumb {
    width: 140px;
    height: 110px;
    border-radius: 18px;
  }

  .name { font-size: 20px; }
  .meta { font-size: 14px; }
  .label { font-size: 13px; }

  .timeline {
    flex-wrap: wrap;
    --stepW: 170px;
    --lineW: 120px;
  }

  .btn {
    width: 100%;
    min-width: unset;
    font-size: 14px;
    padding: 12px 16px;
  }

  .actions {
    margin-left: 0;
    width: 100%;
  }
}
</style>
