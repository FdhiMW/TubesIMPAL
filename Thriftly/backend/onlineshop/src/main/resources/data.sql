-- Data dummy untuk tabel pm_user

INSERT IGNORE INTO pm_user (nama_lengkap, email, password_hash, nomor_telepon, tanggal_daftar, peran_pengguna, aktif)
VALUES
  ('Admin Thriftly', 'admin@thriftly.com', 'admin123', '081200000001', NOW(), 'admin', 1),
  ('Budi Thrift', 'budi@thriftly.com', 'budi123', '081200000002', NOW(), 'customer', 1),
  ('Siti Thrift', 'siti@thriftly.com', 'siti123', '081200000003', NOW(), 'customer', 1);

INSERT IGNORE INTO pm_kategori (id_kategori, nama_kategori, deskripsi) VALUES
(1, 'Kaos', 'Kaos pria dan wanita berbagai ukuran'),
(2, 'Kemeja', 'Kemeja formal dan casual'),
(3, 'Jaket', 'Jaket hoodie, bomber, dan denim'),
(4, 'Celana', 'Jeans, chino, dan celana training'),
(5, 'Dress', 'Dress wanita casual dan formal');

INSERT IGNORE INTO pm_produk (
    id_kategori, id_penjual, nama_produk, deskripsi, harga, stok, kondisi,
    ukuran, warna, merek, jenis_kelamin, status_produk, tanggal_ditambahkan
) VALUES
-- KAOS
(1, 1, 'Kaos Oversize Hitam Unisex',
 'Kaos oversize warna hitam, bahan cotton premium 24s, kondisi 95%.',
 45000, 10, 'bekas', 'L', 'Hitam', 'H&M', 'Unisex', 'aktif', NOW()),

(1, 1, 'Kaos Vintage 90s Band Nirvana',
 'Kaos vintage edisi 90-an, rare collection. Kondisi 90%.',
 150000, 3, 'bekas', 'M', 'Hitam', 'Gildan', 'Unisex', 'aktif', NOW()),

-- KEMEJA
(2, 1, 'Kemeja Flanel Kotak Merah',
 'Kemeja flanel kotak-kotak, bahan hangat dan nyaman.',
 60000, 5, 'bekas', 'L', 'Merah', 'Uniqlo', 'Pria', 'aktif', NOW()),

(2, 1, 'Kemeja Putih Formal Slim Fit',
 'Kemeja putih slim fit, cocok untuk acara formal, kondisi 97%.',
 70000, 4, 'bekas', 'M', 'Putih', 'Zara', 'Pria', 'aktif', NOW()),

-- JAKET
(3, 1, 'Jaket Denim Vintage Levis',
 'Jaket denim klasik Levis, kondisi 85%, warna biru tua.',
 250000, 2, 'bekas', 'L', 'Biru', 'Levis', 'Unisex', 'aktif', NOW()),

(3, 1, 'Hoodie Hitam Supreme KW Super',
 'Hoodie nyaman dipakai harian, kondisi 90%.',
 90000, 6, 'bekas', 'XL', 'Hitam', 'Supreme', 'Unisex', 'aktif', NOW()),

-- CELANA
(4, 1, 'Celana Jeans Sobek Biru',
 'Jeans ripped warna biru, kondisi 92%.',
 80000, 3, 'bekas', '32', 'Biru', 'Pull&Bear', 'Unisex', 'aktif', NOW()),

(4, 1, 'Celana Chino Cream Slim Fit',
 'Chino warna cream, bahan nyaman fit slim.',
 65000, 8, 'bekas', '30', 'Cream', 'Uniqlo', 'Pria', 'aktif', NOW()),

-- DRESS
(5, 1, 'Dress Floral Wanita',
 'Dress motif bunga, bahan halus dan nyaman dipakai.',
 55000, 5, 'bekas', 'M', 'Biru', 'Mango', 'Wanita', 'aktif', NOW()),

(5, 1, 'Dress Korea Model A-Line',
 'Dress A-Line ala Korea, cantik dan elegan.',
 85000, 2, 'bekas', 'S', 'Pink', 'Hotsun', 'Wanita', 'aktif', NOW());
