<template>
  <div class="admin-page" @click="closeAllDropdowns">
    <main class="page-content">
      <h1 class="page-title">Tambah Produk</h1>
      <p class="page-subtitle">Isi data produk yang akan ditambahkan ke katalog.</p>

      <form class="product-form" @submit.prevent="onSubmit">
        <div class="form-grid">
          <div class="form-group">
            <label>Nama Produk</label>
            <input v-model="form.namaProduk" type="text" required />
          </div>

          <!-- ===== KATEGORI (custom combobox) ===== -->
          <div class="form-group">
            <label>Kategori</label>

            <div class="combo" @click.stop>
              <div
                class="combo-input"
                :class="{ open: open.kategori }"
                @click="toggleDropdown('kategori')"
              >
                <input
                  ref="kategoriInput"
                  v-model="kategoriInput"
                  type="text"
                  placeholder="Pilih / ketik kategori baru"
                  @focus="openDropdown('kategori')"
                  @keydown.enter.prevent="pickOrCreateKategori()"
                  @keydown.down.prevent="moveActive('kategori', 1)"
                  @keydown.up.prevent="moveActive('kategori', -1)"
                  @keydown.esc.prevent="closeDropdown('kategori')"
                />
                <span class="chev" :class="{ open: open.kategori }">▾</span>
              </div>

              <transition name="fade-pop">
                <div v-if="open.kategori" class="combo-menu">
                  <div class="combo-hint">
                    <span>Enter untuk pilih / tambah</span>
                    <span class="pill">Kategori</span>
                  </div>

                  <div v-if="filteredKategori.length === 0" class="combo-empty">
                    Tidak ada hasil. Tekan <b>Enter</b> untuk menambah:
                    <b>{{ kategoriInput }}</b>
                  </div>

                  <div
                    v-for="(k, idx) in filteredKategori"
                    :key="k.idKategori"
                    class="combo-item"
                    :class="{ active: idx === activeIndex.kategori }"
                    @mouseenter="activeIndex.kategori = idx"
                    @click="selectKategori(k)"
                  >
                    <span class="item-title">{{ k.namaKategori }}</span>
                    <span class="item-sub">ID: {{ k.idKategori }}</span>
                  </div>
                </div>
              </transition>
            </div>
          </div>

          <div class="form-group">
            <label>Harga (Rp)</label>
            <input v-model.number="form.harga" type="number" min="0" step="1000" required />
          </div>

          <div class="form-group">
            <label>Stok</label>
            <input v-model.number="form.stok" type="number" min="0" step="1" required />
          </div>

          <!-- ===== KONDISI ===== -->
          <div class="form-group">
            <label>Kondisi</label>

            <div class="combo" @click.stop>
              <div
                class="combo-input"
                :class="{ open: open.kondisi }"
                @click="toggleDropdown('kondisi')"
              >
                <input
                  ref="kondisiInput"
                  v-model="form.kondisi"
                  type="text"
                  placeholder="Pilih / ketik kondisi baru"
                  @focus="openDropdown('kondisi')"
                  @keydown.enter.prevent="pickOrCreateSimple('kondisi', 'kondisiOptions', 'kondisi')"
                  @keydown.down.prevent="moveActive('kondisi', 1)"
                  @keydown.up.prevent="moveActive('kondisi', -1)"
                  @keydown.esc.prevent="closeDropdown('kondisi')"
                />
                <span class="chev" :class="{ open: open.kondisi }">▾</span>
              </div>

              <transition name="fade-pop">
                <div v-if="open.kondisi" class="combo-menu">
                  <div class="combo-hint">
                    <span>Enter untuk pilih / tambah</span>
                    <span class="pill">Kondisi</span>
                  </div>

                  <div v-if="filteredKondisi.length === 0" class="combo-empty">
                    Tidak ada hasil. Tekan <b>Enter</b> untuk menambah:
                    <b>{{ form.kondisi }}</b>
                  </div>

                  <div
                    v-for="(k, idx) in filteredKondisi"
                    :key="k"
                    class="combo-item"
                    :class="{ active: idx === activeIndex.kondisi }"
                    @mouseenter="activeIndex.kondisi = idx"
                    @click="selectSimple('kondisi', k)"
                  >
                    <span class="item-title">{{ k }}</span>
                    <span class="item-sub">Kondisi produk</span>
                  </div>
                </div>
              </transition>
            </div>
          </div>

          <!-- ===== UKURAN ===== -->
          <div class="form-group">
            <label>Ukuran</label>

            <div class="combo" @click.stop>
              <div
                class="combo-input"
                :class="{ open: open.ukuran }"
                @click="toggleDropdown('ukuran')"
              >
                <input
                  ref="ukuranInput"
                  v-model="form.ukuran"
                  type="text"
                  placeholder="Pilih / ketik ukuran baru"
                  @focus="openDropdown('ukuran')"
                  @keydown.enter.prevent="pickOrCreateSimple('ukuran', 'ukuranOptions', 'ukuran')"
                  @keydown.down.prevent="moveActive('ukuran', 1)"
                  @keydown.up.prevent="moveActive('ukuran', -1)"
                  @keydown.esc.prevent="closeDropdown('ukuran')"
                />
                <span class="chev" :class="{ open: open.ukuran }">▾</span>
              </div>

              <transition name="fade-pop">
                <div v-if="open.ukuran" class="combo-menu">
                  <div class="combo-hint">
                    <span>Enter untuk pilih / tambah</span>
                    <span class="pill">Ukuran</span>
                  </div>

                  <div v-if="filteredUkuran.length === 0" class="combo-empty">
                    Tidak ada hasil. Tekan <b>Enter</b> untuk menambah:
                    <b>{{ form.ukuran }}</b>
                  </div>

                  <div
                    v-for="(u, idx) in filteredUkuran"
                    :key="u"
                    class="combo-item"
                    :class="{ active: idx === activeIndex.ukuran }"
                    @mouseenter="activeIndex.ukuran = idx"
                    @click="selectSimple('ukuran', u)"
                  >
                    <span class="item-title">{{ u }}</span>
                    <span class="item-sub">Ukuran produk</span>
                  </div>
                </div>
              </transition>
            </div>
          </div>

          <!-- ===== WARNA ===== -->
          <div class="form-group">
            <label>Warna</label>

            <div class="combo" @click.stop>
              <div
                class="combo-input"
                :class="{ open: open.warna }"
                @click="toggleDropdown('warna')"
              >
                <input
                  ref="warnaInput"
                  v-model="form.warna"
                  type="text"
                  placeholder="Pilih / ketik warna baru"
                  @focus="openDropdown('warna')"
                  @keydown.enter.prevent="pickOrCreateSimple('warna', 'warnaOptions', 'warna')"
                  @keydown.down.prevent="moveActive('warna', 1)"
                  @keydown.up.prevent="moveActive('warna', -1)"
                  @keydown.esc.prevent="closeDropdown('warna')"
                />
                <span class="chev" :class="{ open: open.warna }">▾</span>
              </div>

              <transition name="fade-pop">
                <div v-if="open.warna" class="combo-menu">
                  <div class="combo-hint">
                    <span>Enter untuk pilih / tambah</span>
                    <span class="pill">Warna</span>
                  </div>

                  <div v-if="filteredWarna.length === 0" class="combo-empty">
                    Tidak ada hasil. Tekan <b>Enter</b> untuk menambah:
                    <b>{{ form.warna }}</b>
                  </div>

                  <div
                    v-for="(w, idx) in filteredWarna"
                    :key="w"
                    class="combo-item"
                    :class="{ active: idx === activeIndex.warna }"
                    @mouseenter="activeIndex.warna = idx"
                    @click="selectSimple('warna', w)"
                  >
                    <span class="item-title">{{ w }}</span>
                    <span class="item-sub">Warna produk</span>
                  </div>
                </div>
              </transition>
            </div>
          </div>

          <!-- ===== MEREK ===== -->
          <div class="form-group">
            <label>Merek</label>

            <div class="combo" @click.stop>
              <div
                class="combo-input"
                :class="{ open: open.merek }"
                @click="toggleDropdown('merek')"
              >
                <input
                  ref="merekInput"
                  v-model="form.merek"
                  type="text"
                  placeholder="Pilih / ketik merek baru"
                  @focus="openDropdown('merek')"
                  @keydown.enter.prevent="pickOrCreateSimple('merek', 'merekOptions', 'merek')"
                  @keydown.down.prevent="moveActive('merek', 1)"
                  @keydown.up.prevent="moveActive('merek', -1)"
                  @keydown.esc.prevent="closeDropdown('merek')"
                />
                <span class="chev" :class="{ open: open.merek }">▾</span>
              </div>

              <transition name="fade-pop">
                <div v-if="open.merek" class="combo-menu">
                  <div class="combo-hint">
                    <span>Enter untuk pilih / tambah</span>
                    <span class="pill">Merek</span>
                  </div>

                  <div v-if="filteredMerek.length === 0" class="combo-empty">
                    Tidak ada hasil. Tekan <b>Enter</b> untuk menambah:
                    <b>{{ form.merek }}</b>
                  </div>

                  <div
                    v-for="(m, idx) in filteredMerek"
                    :key="m"
                    class="combo-item"
                    :class="{ active: idx === activeIndex.merek }"
                    @mouseenter="activeIndex.merek = idx"
                    @click="selectSimple('merek', m)"
                  >
                    <span class="item-title">{{ m }}</span>
                    <span class="item-sub">Merek produk</span>
                  </div>
                </div>
              </transition>
            </div>
          </div>

          <!-- ===== JENIS KELAMIN ===== -->
          <div class="form-group">
            <label>Jenis Kelamin</label>

            <div class="combo" @click.stop>
              <div class="combo-input" :class="{ open: open.jk }" @click="toggleDropdown('jk')">
                <input
                  ref="jkInput"
                  v-model="form.jenisKelamin"
                  type="text"
                  placeholder="Pilih / ketik (Unisex/Pria/Wanita)"
                  @focus="openDropdown('jk')"
                  @keydown.enter.prevent="pickOrCreateSimple('jk', 'jenisKelaminOptions', 'jenisKelamin')"
                  @keydown.down.prevent="moveActive('jk', 1)"
                  @keydown.up.prevent="moveActive('jk', -1)"
                  @keydown.esc.prevent="closeDropdown('jk')"
                />
                <span class="chev" :class="{ open: open.jk }">▾</span>
              </div>

              <transition name="fade-pop">
                <div v-if="open.jk" class="combo-menu">
                  <div class="combo-hint">
                    <span>Enter untuk pilih / tambah</span>
                    <span class="pill">Gender</span>
                  </div>

                  <div v-if="filteredJK.length === 0" class="combo-empty">
                    Tidak ada hasil. Tekan <b>Enter</b> untuk menambah:
                    <b>{{ form.jenisKelamin }}</b>
                  </div>

                  <div
                    v-for="(j, idx) in filteredJK"
                    :key="j"
                    class="combo-item"
                    :class="{ active: idx === activeIndex.jk }"
                    @mouseenter="activeIndex.jk = idx"
                    @click="selectSimple('jk', j)"
                  >
                    <span class="item-title">{{ j }}</span>
                    <span class="item-sub">Jenis kelamin target</span>
                  </div>
                </div>
              </transition>
            </div>
          </div>
        </div>

        <div class="form-group full-width">
          <label>Deskripsi</label>
          <textarea v-model="form.deskripsi" rows="3" placeholder="Tuliskan deskripsi singkat produk..."></textarea>
        </div>

        <div class="form-group full-width">
          <label>Gambar Produk</label>
          <input type="file" accept="image/*" @change="onFileChange" />
          <small>Pilih file gambar dari komputer (.jpg, .png, dll).</small>
        </div>

        <div v-if="imagePreview" class="image-preview-wrapper full-width">
          <p class="preview-label">Preview Gambar:</p>
          <img :src="imagePreview" alt="Preview" class="image-preview" />
        </div>

        <div class="form-actions">
          <button type="button" class="ghost-btn" @click="goBack" :disabled="loading">
            Batal
          </button>
          <button type="submit" class="primary-btn" :disabled="loading">
            {{ loading ? 'Menyimpan...' : 'Simpan Produk' }}
          </button>
        </div>
      </form>
    </main>

    <transition name="pop">
      <div v-if="popup.show" class="popup-overlay" @click.self="closePopup">
        <div class="popup-card" role="dialog" aria-modal="true">
          <div class="popup-icon-wrap">
            <div class="popup-icon">
              ✓
            </div>
          </div>

          <div class="popup-title">{{ popup.title }}</div>
          <div class="popup-desc" v-if="popup.desc">{{ popup.desc }}</div>

          <button class="popup-btn" @click="closePopup">OK</button>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import { createProduk, getMasterDropdown } from '@/api/produkApi'
import { getKategoriList, createKategori } from '@/api/kategoriApi'

export default {
  name: 'AdminProductCreateView',
  data() {
    return {
      loading: false,
      kategoriInput: '',

      form: {
        namaProduk: '',
        harga: null,
        stok: 0,
        kondisi: '',
        ukuran: '',
        warna: '',
        merek: '',
        jenisKelamin: '',
        deskripsi: '',
        statusProduk: 'aktif',
        imageUrl: '',
        idKategori: '',
      },

      fileGambar: null,
      imagePreview: null,

      kategoriOptions: [],
      kondisiOptions: [],
      ukuranOptions: [],
      warnaOptions: [],
      merekOptions: [],
      jenisKelaminOptions: [],

      open: { kategori: false, kondisi: false, ukuran: false, warna: false, merek: false, jk: false },
      activeIndex: { kategori: 0, kondisi: 0, ukuran: 0, warna: 0, merek: 0, jk: 0 },

      popup: {
        show: false,
        title: 'Produk berhasil disimpan',
        desc: 'Produk sudah ditambahkan ke katalog admin.',
      },
    }
  },

  async created() {
    await this.loadDropdowns()
  },

  computed: {
    filteredKategori() {
      const q = this.norm(this.kategoriInput)
      const list = this.kategoriOptions || []
      if (!q) return list
      return list.filter((k) => this.norm(k.namaKategori).includes(q))
    },
    filteredKondisi() {
      const q = this.norm(this.form.kondisi)
      const list = this.kondisiOptions || []
      if (!q) return list
      return list.filter((x) => this.norm(x).includes(q))
    },
    filteredUkuran() {
      const q = this.norm(this.form.ukuran)
      const list = this.ukuranOptions || []
      if (!q) return list
      return list.filter((x) => this.norm(x).includes(q))
    },
    filteredWarna() {
      const q = this.norm(this.form.warna)
      const list = this.warnaOptions || []
      if (!q) return list
      return list.filter((x) => this.norm(x).includes(q))
    },
    filteredMerek() {
      const q = this.norm(this.form.merek)
      const list = this.merekOptions || []
      if (!q) return list
      return list.filter((x) => this.norm(x).includes(q))
    },
    filteredJK() {
      const q = this.norm(this.form.jenisKelamin)
      const list = this.jenisKelaminOptions || []
      if (!q) return list
      return list.filter((x) => this.norm(x).includes(q))
    },
  },

  methods: {
    norm(s) {
      return (s || '').toString().trim().toLowerCase()
    },

    closeAllDropdowns() {
      Object.keys(this.open).forEach((k) => (this.open[k] = false))
      Object.keys(this.activeIndex).forEach((k) => (this.activeIndex[k] = 0))
    },

    openDropdown(key) {
      this.closeAllDropdowns()
      this.open[key] = true
      this.activeIndex[key] = 0
    },

    closeDropdown(key) {
      this.open[key] = false
      this.activeIndex[key] = 0
    },

    toggleDropdown(key) {
      if (this.open[key]) this.closeDropdown(key)
      else this.openDropdown(key)
    },

    moveActive(key, delta) {
      let max = 0
      if (key === 'kategori') max = this.filteredKategori.length
      if (key === 'kondisi') max = this.filteredKondisi.length
      if (key === 'ukuran') max = this.filteredUkuran.length
      if (key === 'warna') max = this.filteredWarna.length
      if (key === 'merek') max = this.filteredMerek.length
      if (key === 'jk') max = this.filteredJK.length
      if (!max) return

      let next = (this.activeIndex[key] ?? 0) + delta
      if (next < 0) next = max - 1
      if (next >= max) next = 0
      this.activeIndex[key] = next
    },

    async loadDropdowns() {
      const fallbackKondisi = ['Bekas', 'Like New', 'Baru']
      const fallbackUkuran = ['S', 'M', 'L', 'XL', 'All Size']
      const fallbackJK = ['Unisex', 'Pria', 'Wanita']

      try {
        const resKategori = await getKategoriList()
        this.kategoriOptions = Array.isArray(resKategori?.data) ? resKategori.data : []
      } catch (e) {
        this.kategoriOptions = []
      }

      try {
        const resMaster = await getMasterDropdown()
        const master = resMaster?.data || {}

        this.kondisiOptions = master.kondisi?.length ? master.kondisi : fallbackKondisi
        this.ukuranOptions = master.ukuran?.length ? master.ukuran : fallbackUkuran
        this.jenisKelaminOptions = master.jenisKelamin?.length ? master.jenisKelamin : fallbackJK

        this.warnaOptions = master.warna?.length ? master.warna : []
        this.merekOptions = master.merek?.length ? master.merek : []
      } catch (e) {
        this.kondisiOptions = fallbackKondisi
        this.ukuranOptions = fallbackUkuran
        this.jenisKelaminOptions = fallbackJK
        this.warnaOptions = []
        this.merekOptions = []
      }
    },

    selectKategori(k) {
      this.kategoriInput = k.namaKategori
      this.form.idKategori = k.idKategori
      this.closeDropdown('kategori')
    },

    async pickOrCreateKategori() {
      if (this.filteredKategori.length > 0) {
        const idx = this.activeIndex.kategori || 0
        const pick = this.filteredKategori[idx]
        if (pick) {
          this.selectKategori(pick)
          return
        }
      }

      const nama = (this.kategoriInput || '').trim()
      if (!nama) return

      const found = this.kategoriOptions.find((x) => this.norm(x.namaKategori) === this.norm(nama))
      if (found) {
        this.selectKategori(found)
        return
      }

      const res = await createKategori({ namaKategori: nama, deskripsi: `Kategori ${nama}` })
      const created = res?.data
      if (created?.idKategori) {
        this.kategoriOptions.push(created)
        this.selectKategori(created)
      }
    },

    selectSimple(key, value) {
      if (key === 'kondisi') this.form.kondisi = value
      if (key === 'ukuran') this.form.ukuran = value
      if (key === 'warna') this.form.warna = value
      if (key === 'merek') this.form.merek = value
      if (key === 'jk') this.form.jenisKelamin = value
      this.closeDropdown(key)
    },

    pickOrCreateSimple(key, optionKey, fieldName) {
      let list = []
      if (key === 'kondisi') list = this.filteredKondisi
      if (key === 'ukuran') list = this.filteredUkuran
      if (key === 'warna') list = this.filteredWarna
      if (key === 'merek') list = this.filteredMerek
      if (key === 'jk') list = this.filteredJK

      if (list.length > 0) {
        const idx = this.activeIndex[key] || 0
        const pick = list[idx]
        if (pick) {
          this.selectSimple(key, pick)
          return
        }
      }

      const v =
        fieldName === 'kondisi' ? this.form.kondisi :
        fieldName === 'ukuran' ? this.form.ukuran :
        fieldName === 'warna' ? this.form.warna :
        fieldName === 'merek' ? this.form.merek :
        this.form.jenisKelamin

      const val = (v || '').trim()
      if (!val) return

      const arr = this[optionKey]
      const exists = (arr || []).some((x) => this.norm(x) === this.norm(val))
      if (!exists) arr.push(val)

      this.closeDropdown(key)
    },

    onFileChange(e) {
      const file = e.target.files && e.target.files[0]
      this.fileGambar = file || null

      if (file) {
        this.form.imageUrl = file.name
        const reader = new FileReader()
        reader.onload = (ev) => (this.imagePreview = ev.target.result)
        reader.readAsDataURL(file)
      } else {
        this.imagePreview = null
        this.form.imageUrl = ''
      }
    },

    async resolveKategoriId() {
      if (this.form.idKategori) return this.form.idKategori

      const nama = (this.kategoriInput || '').trim()
      if (!nama) throw new Error('Kategori wajib diisi.')

      const found = this.kategoriOptions.find((x) => this.norm(x.namaKategori) === this.norm(nama))
      if (found) return found.idKategori

      const res = await createKategori({ namaKategori: nama, deskripsi: `Kategori ${nama}` })
      const created = res?.data
      if (!created?.idKategori) throw new Error('Gagal membuat kategori baru.')
      this.kategoriOptions.push(created)
      return created.idKategori
    },

    showPopup(title, desc) {
      this.popup.title = title || 'Berhasil'
      this.popup.desc = desc || ''
      this.popup.show = true
    },
    closePopup() {
      this.popup.show = false
    },

    async onSubmit() {
      if (!this.form.namaProduk || this.form.harga == null) {
        this.showPopup('Gagal menyimpan', 'Nama produk dan harga wajib diisi.')
        return
      }

      this.loading = true
      try {
        this.form.idKategori = await this.resolveKategoriId()

        const payload = {
          namaProduk: this.form.namaProduk,
          deskripsi: this.form.deskripsi || '',
          harga: this.form.harga != null ? this.form.harga : 0,
          stok: this.form.stok != null ? this.form.stok : 0,
          kondisi: this.form.kondisi || '',
          ukuran: this.form.ukuran || '',
          warna: this.form.warna || '',
          merek: this.form.merek || '',
          jenisKelamin: this.form.jenisKelamin || '',
          statusProduk: this.form.statusProduk,
          imageUrl: this.form.imageUrl,
          idKategori: this.form.idKategori,
        }

        const formData = new FormData()
        Object.keys(payload).forEach((key) => formData.append(key, payload[key]))
        if (this.fileGambar) formData.append('gambar', this.fileGambar)

        await createProduk(formData)

        this.showPopup('Produk berhasil disimpan', 'Produk sudah ditambahkan ke katalog admin.')

        setTimeout(() => {
          this.$router.push({ name: 'admin-product-list' })
        }, 650)
      } catch (err) {
        console.error('Gagal menyimpan produk', err.response || err)
        this.showPopup('Gagal menyimpan', err?.message || 'Cek console untuk detail error.')
      } finally {
        this.loading = false
      }
    },

    goBack() {
      this.$router.back()
    },

    resolveImageUrl(url) {
      const API = "http://localhost:8080";

      if (!url) return "";
      if (url.startsWith("http")) return url;
      if (url.startsWith("/uploads/")) return API + url;
      if (url.startsWith("/foto-barang/")) return url;

      return API + "/uploads/" + url;
    },
  },
}
</script>

<style scoped>
/* =========================================================
   PAKSA BACKGROUND GLOBAL PUTIH
   ========================================================= */
:global(html),
:global(body),
:global(#app) {
  height: 100%;
  min-height: 100%;
  margin: 0;
  background: #ffffff !important;
}

:global(*),
:global(*::before),
:global(*::after) {
  box-sizing: border-box;
}

/* =========================================================
   FULL SATU LAYAR (Viewport)
   ========================================================= */
.admin-page {
  min-height: 100vh;
  width: 100%;
  background: #ffffff;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  color: #222;

  display: flex;
  flex-direction: column;
}

/* ✅ page-content dipaksa setinggi layar dan fleksibel */
.page-content {
  width: 100%;
  max-width: 100%;
  margin: 0;
  padding: 26px 34px 42px;
  font-size: 16px;

  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* ✅ form/card dibuat mengisi sisa tinggi layar */
.product-form {
  width: 100%;
  background: #fff;
  border-radius: 22px;
  padding: 26px 26px 28px;
  box-shadow: 0 10px 26px rgba(15, 23, 42, 0.06);
  border: 1px solid #eef0f7;

  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 0;
}

/* ✅ tombol dinaikkan + diperbesar */
.form-actions {
  margin-top: 18px;          /* ✅ sebelumnya auto (terlalu bawah). sekarang naik */
  display: flex;
  justify-content: flex-end;
  gap: 14px;
  padding-top: 10px;         /* ✅ lebih rapat */
}

/* =========================================================
   TYPO
   ========================================================= */
.page-title {
  font-size: 28px;
  font-weight: 900;
  margin: 0 0 6px;
}

.page-subtitle {
  margin: 0 0 18px;
  font-size: 15px;
  color: #6b7280;
}

/* =========================================================
   GRID + INPUT
   ========================================================= */
.form-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 18px 22px;
  margin-bottom: 18px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-size: 15px;
  position: relative;
}

.form-group.full-width { margin-top: 10px; }

.form-group label {
  font-weight: 800;
  color: #374151;
  font-size: 14px;
}

.form-group input,
.form-group textarea {
  border-radius: 14px;
  border: 1px solid #d1d5db;
  padding: 12px 14px;
  font-size: 15px;
  outline: none;
  background: #fff;
}

.form-group input:focus,
.form-group textarea:focus {
  border-color: rgba(255, 90, 60, 0.55);
  box-shadow: 0 0 0 5px rgba(255, 90, 60, 0.12);
}

.form-group textarea {
  border-radius: 16px;
  resize: vertical;
  min-height: 120px;
}

.form-group small {
  font-size: 12px;
  color: #9ca3af;
}

/* preview */
.image-preview-wrapper { margin-top: 14px; }
.preview-label { font-size: 13px; color: #6b7280; margin: 0 0 8px; font-weight: 700; }
.image-preview {
  max-width: 420px;
  width: 100%;
  border-radius: 14px;
  border: 1px solid #e5e7eb;
}

/* buttons (✅ diperbesar) */
.ghost-btn {
  border-radius: 999px;
  border: 1px solid #d3d7e6;
  background: #fff;
  padding: 14px 26px;        /* ✅ lebih besar */
  font-size: 15px;           /* ✅ lebih besar */
  font-weight: 800;
  cursor: pointer;
  min-height: 46px;          /* ✅ konsisten tinggi tombol */
}

.primary-btn {
  border-radius: 999px;
  border: none;
  background: linear-gradient(135deg, #ff5a3c, #ff9f1c);
  padding: 14px 28px;        /* ✅ lebih besar */
  color: #fff;
  font-size: 15px;           /* ✅ lebih besar */
  font-weight: 900;
  cursor: pointer;
  min-height: 46px;          /* ✅ konsisten tinggi tombol */
}

/* combobox */
.combo { position: relative; }

.combo-input {
  display: flex;
  align-items: center;
  gap: 10px;
  border-radius: 14px;
  border: 1px solid #d1d5db;
  padding: 0 12px;
  background: #fff;
  transition: box-shadow 0.15s ease, border-color 0.15s ease;
  min-height: 46px;
}

.combo-input.open {
  border-color: rgba(255, 90, 60, 0.55);
  box-shadow: 0 0 0 5px rgba(255, 90, 60, 0.12);
}

.combo-input input {
  border: none !important;
  box-shadow: none !important;
  padding: 12px 6px;
  width: 100%;
  outline: none;
  font-size: 15px;
}

.chev {
  user-select: none;
  font-size: 14px;
  color: #6b7280;
  transition: transform 0.15s ease;
}
.chev.open { transform: rotate(180deg); }

.combo-menu {
  position: absolute;
  z-index: 50;
  top: calc(100% + 10px);
  left: 0;
  right: 0;
  background: #ffffff;
  border: 1px solid rgba(15, 23, 42, 0.10);
  box-shadow: 0 22px 48px rgba(15, 23, 42, 0.14);
  border-radius: 16px;
  overflow: hidden;
  max-height: 320px;
  display: flex;
  flex-direction: column;
}

.combo-hint {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 14px;
  font-size: 12px;
  color: #6b7280;
  background: linear-gradient(135deg, rgba(255, 90, 60, 0.06), rgba(255, 159, 28, 0.06));
  border-bottom: 1px solid rgba(15, 23, 42, 0.06);
}

.pill {
  font-size: 11px;
  padding: 5px 10px;
  border-radius: 999px;
  color: #ff5a3c;
  background: rgba(255, 90, 60, 0.10);
  border: 1px solid rgba(255, 90, 60, 0.18);
  font-weight: 800;
}

.combo-empty {
  padding: 14px;
  font-size: 13px;
  color: #374151;
}

.combo-item {
  padding: 12px 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  transition: background 0.12s ease;
  font-size: 14px;
}
.combo-item:hover { background: rgba(15, 23, 42, 0.04); }
.combo-item.active { background: rgba(255, 90, 60, 0.10); }

.item-title { font-weight: 800; color: #111827; }
.item-sub { display: none !important; }

.fade-pop-enter-active,
.fade-pop-leave-active { transition: opacity 0.12s ease, transform 0.12s ease; }
.fade-pop-enter-from,
.fade-pop-leave-to { opacity: 0; transform: translateY(-6px) scale(0.98); }

/* popup */
.popup-overlay {
  position: fixed;
  inset: 0;
  z-index: 999;
  background: rgba(15, 23, 42, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 18px;
}

.popup-card {
  width: min(520px, 92vw);
  border-radius: 22px;
  background: #fff;
  box-shadow: 0 28px 70px rgba(0, 0, 0, 0.25);
  padding: 26px 22px 18px;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.popup-card::before {
  content: "";
  position: absolute;
  inset: 0;
  background: radial-gradient(900px 260px at 50% -20%, rgba(255, 90, 60, 0.22), transparent 55%);
  pointer-events: none;
}

.popup-icon-wrap {
  display: flex;
  justify-content: center;
  margin-top: 8px;
  margin-bottom: 12px;
  position: relative;
  z-index: 1;
}

.popup-icon {
  width: 92px;
  height: 92px;
  border-radius: 999px;
  display: grid;
  place-items: center;
  font-size: 44px;
  font-weight: 800;
  color: #fff;
  background: linear-gradient(135deg, #ff5a3c, #ff9f1c);
  box-shadow: 0 18px 40px rgba(255, 90, 60, 0.28);
}

.popup-title {
  font-size: 28px;
  font-weight: 800;
  color: #111827;
  line-height: 1.1;
  margin: 8px 0 6px;
  position: relative;
  z-index: 1;
}

.popup-desc {
  font-size: 13px;
  color: #6b7280;
  margin: 0 0 16px;
  position: relative;
  z-index: 1;
}

.popup-btn {
  width: 100%;
  border: none;
  border-radius: 14px;
  padding: 13px 14px;
  font-size: 15px;
  font-weight: 700;
  color: #fff;
  cursor: pointer;
  background: linear-gradient(135deg, #ff9f1c, #ff5a3c);
  box-shadow: 0 16px 34px rgba(255, 90, 60, 0.22);
  position: relative;
  z-index: 1;
}

.pop-enter-active,
.pop-leave-active { transition: opacity 0.14s ease, transform 0.14s ease; }
.pop-enter-from,
.pop-leave-to { opacity: 0; transform: translateY(8px) scale(0.98); }

/* responsive */
@media (max-width: 1200px) {
  .form-grid { grid-template-columns: repeat(2, minmax(0, 1fr)); }
}
@media (max-width: 680px) {
  .page-content { padding: 18px 16px 28px; }
  .product-form { padding: 18px 16px 18px; border-radius: 18px; }
  .form-grid { grid-template-columns: 1fr; }

  /* tetap enak di mobile */
  .form-actions { flex-direction: column; align-items: stretch; gap: 10px; }
  .ghost-btn, .primary-btn { width: 100%; }
}
</style>
