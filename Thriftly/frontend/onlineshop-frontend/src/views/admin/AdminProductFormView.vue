<template>
  <div class="admin-page">
    <main class="page-content">
      <h1 class="page-title">Tambah Produk</h1>
      <p class="page-subtitle">
        Isi data produk yang akan ditambahkan ke katalog.
      </p>

      <form class="product-form" @submit.prevent="onSubmit">
        <!-- GRID ATAS -->
        <div class="form-grid">
          <div class="form-group">
            <label>Nama Produk</label>
            <input v-model="form.namaProduk" type="text" required />
          </div>

          <div class="form-group">
            <label>Harga (Rp)</label>
            <input
              v-model.number="form.harga"
              type="number"
              min="0"
              step="1000"
              required
            />
          </div>

          <div class="form-group">
            <label>Stok</label>
            <input
              v-model.number="form.stok"
              type="number"
              min="0"
              step="1"
              required
            />
          </div>

          <div class="form-group">
            <label>Kondisi</label>
            <input
              v-model="form.kondisi"
              type="text"
              placeholder="Bekas / Like New / Baru"
            />
          </div>

          <div class="form-group">
            <label>Ukuran</label>
            <input
              v-model="form.ukuran"
              type="text"
              placeholder="S / M / L / All Size"
            />
          </div>

          <div class="form-group">
            <label>Warna</label>
            <input v-model="form.warna" type="text" />
          </div>

          <div class="form-group">
            <label>Merek</label>
            <input v-model="form.merek" type="text" />
          </div>

          <div class="form-group">
            <label>Jenis Kelamin</label>
            <input
              v-model="form.jenisKelamin"
              type="text"
              placeholder="Unisex / Pria / Wanita"
            />
          </div>
        </div>

        <!-- DESKRIPSI -->
        <div class="form-group full-width">
          <label>Deskripsi</label>
          <textarea
            v-model="form.deskripsi"
            rows="3"
            placeholder="Tuliskan deskripsi singkat produk..."
          ></textarea>
        </div>

        <!-- INPUT GAMBAR -->
        <div class="form-group full-width">
          <label>Gambar Produk</label>
          <input type="file" accept="image/*" @change="onFileChange" />
          <small>Pilih file gambar dari komputer (.jpg, .png, dll).</small>
        </div>

        <!-- PREVIEW GAMBAR -->
        <div v-if="imagePreview" class="image-preview-wrapper full-width">
          <p class="preview-label">Preview Gambar:</p>
          <img :src="imagePreview" alt="Preview" class="image-preview" />
        </div>

        <!-- ACTIONS -->
        <div class="form-actions">
          <button
            type="button"
            class="ghost-btn"
            @click="goBack"
            :disabled="loading"
          >
            Batal
          </button>
          <button type="submit" class="primary-btn" :disabled="loading">
            {{ loading ? 'Menyimpan...' : 'Simpan Produk' }}
          </button>
        </div>
      </form>
    </main>
  </div>
</template>

<script>
import { createProduk } from '@/api/produkApi'

export default {
  name: 'AdminProductCreateView',
  data() {
    return {
      loading: false,
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
        // field tambahan supaya cocok dengan kolom DB / entity
        statusProduk: 'aktif', // pm_produk.status_produk (NOT NULL)
        imageUrl: '', // pm_produk.image_url (nama/path file)
      },
      fileGambar: null,
      imagePreview: null,
    }
  },
  methods: {
    onFileChange(e) {
      const file = e.target.files && e.target.files[0]
      this.fileGambar = file || null

      if (file) {
        // simpan nama file ke imageUrl – backend bisa simpan file fisik nanti
        this.form.imageUrl = file.name

        const reader = new FileReader()
        reader.onload = (ev) => {
          this.imagePreview = ev.target.result
        }
        reader.readAsDataURL(file)
      } else {
        this.imagePreview = null
        this.form.imageUrl = ''
      }
    },

    async onSubmit() {
      if (!this.form.namaProduk || this.form.harga == null) {
        alert('Nama produk dan harga wajib diisi.')
        return
      }

      this.loading = true
      try {
        // kirim JSON (bukan FormData) – sesuaikan dengan ProdukDetailDto
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
          statusProduk: this.form.statusProduk, // penting: NOT NULL di DB
          imageUrl: this.form.imageUrl, // boleh kosong, kolom NULL
        }

        await createProduk(payload)

        alert('Produk berhasil disimpan.')
        this.$router.push({ name: 'admin-product-list' })
      } catch (err) {
        console.error('Gagal menyimpan produk', err.response || err)
        alert('Gagal menyimpan produk, cek console.')
      } finally {
        this.loading = false
      }
    },

    goBack() {
      this.$router.back()
    },
  },
}
</script>

<style scoped>
.admin-page {
  min-height: 100vh;
  background: #f5f7fb;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI',
    sans-serif;
  color: #222;
}

.page-content {
  padding: 20px 32px 32px;
}

.page-title {
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 4px;
}

.page-subtitle {
  margin: 0 0 16px;
  font-size: 13px;
  color: #6b7280;
}

.product-form {
  background: #fff;
  border-radius: 20px;
  padding: 20px 22px 24px;
  box-shadow: 0 8px 20px rgba(15, 23, 42, 0.04);
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 14px 18px;
  margin-bottom: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-size: 13px;
}

.form-group.full-width {
  margin-top: 8px;
}

.form-group label {
  font-weight: 500;
  color: #374151;
}

.form-group input,
.form-group textarea {
  border-radius: 999px;
  border: 1px solid #d1d5db;
  padding: 8px 14px;
  font-size: 13px;
  outline: none;
}

.form-group textarea {
  border-radius: 14px;
  resize: vertical;
}

.form-group small {
  font-size: 11px;
  color: #9ca3af;
}

.image-preview-wrapper {
  margin-top: 12px;
}

.preview-label {
  font-size: 12px;
  color: #6b7280;
  margin-bottom: 6px;
}

.image-preview {
  max-width: 260px;
  border-radius: 12px;
  border: 1px solid #e5e7eb;
}

.form-actions {
  margin-top: 18px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* Buttons reuse style dari dashboard */
.ghost-btn {
  border-radius: 999px;
  border: 1px solid #d3d7e6;
  background: #fff;
  padding: 7px 18px;
  font-size: 12px;
  cursor: pointer;
}

.primary-btn {
  border-radius: 999px;
  border: none;
  background: linear-gradient(135deg, #ff5a3c, #ff9f1c);
  padding: 7px 20px;
  color: #fff;
  font-size: 12px;
  cursor: pointer;
}

@media (max-width: 1024px) {
  .form-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}
</style>
