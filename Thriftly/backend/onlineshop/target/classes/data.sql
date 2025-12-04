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

INSERT IGNORE INTO pm_pesanan
(
    id_user,
    kode_pesanan,
    tanggal_pesanan,
    status_pesanan,
    metode_pembayaran,
    total_barang,
    ongkos_kirim,
    total_pembayaran,
    nama_penerima,
    nomor_telepon,
    alamat_lengkap,
    kode_pos,
    kurir,
    nomor_resi,
    tanggal_dikirim,
    tanggal_diterima,
    sudah_dikonfirmasi
)
VALUES
-- 1 MENUNGGU_PEMBAYARAN
(2, 'INV-2025-0001', NOW() - INTERVAL 10 DAY, 'MENUNGGU_PEMBAYARAN',
 'Transfer Bank', 2, 15000, 215000,
 'Budi Thrift', '081200000002', 'Jl. Budi No. 1, Bandung', '40111',
 'JNE', NULL, NULL, NULL, 0),

-- 2 DIKEMAS
(3, 'INV-2025-0002', NOW() - INTERVAL 9 DAY, 'DIKEMAS',
 'Transfer Bank', 1, 15000, 95000,
 'Siti Thrift', '081200000003', 'Jl. Siti No. 5, Bandung', '40112',
 'JNE', NULL, NULL, NULL, 0),

-- 3 DALAM_PERJALANAN
(2, 'INV-2025-0003', NOW() - INTERVAL 8 DAY, 'DALAM_PERJALANAN',
 'COD', 3, 20000, 375000,
 'Budi Thrift', '081200000002', 'Jl. Mawar No. 10, Cimahi', '40511',
 'SiCepat', 'SC123456789', NOW() - INTERVAL 7 DAY, NULL, 0),

-- 4 SELESAI
(3, 'INV-2025-0004', NOW() - INTERVAL 7 DAY, 'SELESAI',
 'Transfer Bank', 1, 15000, 120000,
 'Siti Thrift', '081200000003', 'Jl. Melati No. 7, Bandung', '40115',
 'JNE', 'JN123456789', NOW() - INTERVAL 6 DAY, NOW() - INTERVAL 5 DAY, 1),

-- 5 DIKEMAS
(2, 'INV-2025-0005', NOW() - INTERVAL 6 DAY, 'DIKEMAS',
 'Transfer Bank', 4, 25000, 450000,
 'Budi Thrift', '081200000002', 'Jl. Kamboja No. 20, Bandung', '40118',
 'J&T', NULL, NULL, NULL, 0),

-- 6 DALAM_PERJALANAN
(3, 'INV-2025-0006', NOW() - INTERVAL 5 DAY, 'DALAM_PERJALANAN',
 'COD', 2, 20000, 280000,
 'Siti Thrift', '081200000003', 'Jl. Anggrek No. 3, Cimahi', '40512',
 'SiCepat', 'SC987654321', NOW() - INTERVAL 4 DAY, NULL, 0),

-- 7 SELESAI
(2, 'INV-2025-0007', NOW() - INTERVAL 4 DAY, 'SELESAI',
 'Transfer Bank', 3, 20000, 330000,
 'Budi Thrift', '081200000002', 'Jl. Kenanga No. 4, Bandung', '40119',
 'JNE', 'JN777888999', NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 2 DAY, 1),

-- 8 MENUNGGU_PEMBAYARAN
(3, 'INV-2025-0008', NOW() - INTERVAL 3 DAY, 'MENUNGGU_PEMBAYARAN',
 'Transfer Bank', 1, 15000, 85000,
 'Siti Thrift', '081200000003', 'Jl. Dahlia No. 9, Bandung', '40121',
 'J&T', NULL, NULL, NULL, 0),

-- 9 DIKEMAS
(2, 'INV-2025-0009', NOW() - INTERVAL 2 DAY, 'DIKEMAS',
 'Transfer Bank', 2, 15000, 210000,
 'Budi Thrift', '081200000002', 'Jl. Teratai No. 2, Bandung', '40122',
 'JNE', NULL, NULL, NULL, 0),

-- 10 SELESAI
(3, 'INV-2025-0010', NOW() - INTERVAL 1 DAY, 'SELESAI',
 'COD', 5, 30000, 600000,
 'Siti Thrift', '081200000003', 'Jl. Cendana No. 11, Cimahi', '40513',
 'SiCepat', 'SC555666777', NOW() - INTERVAL 1 DAY, NOW(), 1);

INSERT IGNORE INTO pm_pesanan_item
(id_pesanan, id_produk, nama_produk, harga_satuan, jumlah, subtotal)
VALUES
-- Pesanan 1
(1, 1, 'Kaos Oversize Vintage',      100000, 1, 100000),
(1, 2, 'Kemeja Flanel Kotak',        100000, 1, 100000),

-- Pesanan 2
(2, 3, 'Jaket Denim Vintage',        80000,  1, 80000),

-- Pesanan 3
(3, 1, 'Kaos Oversize Vintage',      100000, 1, 100000),
(3, 4, 'Celana Jeans Slim Fit',      120000, 2, 240000),

-- Pesanan 4
(4, 5, 'Dress Floral Wanita',        105000, 1, 105000),

-- Pesanan 5
(5, 6, 'Kaos Graphic Streetwear',    100000, 2, 200000),
(5, 7, 'Kemeja Putih Formal',        115000, 2, 230000),

-- Pesanan 6
(6, 2, 'Kemeja Flanel Kotak',        100000, 1, 100000),
(6, 4, 'Celana Jeans Slim Fit',      120000, 1, 120000),

-- Pesanan 7
(7, 1, 'Kaos Oversize Vintage',      100000, 2, 200000),
(7, 5, 'Dress Floral Wanita',        105000, 1, 105000),

-- Pesanan 8
(8, 8, 'Dress Korea A-Line',         70000,  1, 70000),

-- Pesanan 9
(9, 9,  'Celana Chino Cream',        95000,  1, 95000),
(9, 10, 'Jacket Hoodie Hitam',       95000,  1, 95000),

-- Pesanan 10
(10, 3, 'Jaket Denim Vintage',       80000,  2, 160000),
(10, 4, 'Celana Jeans Slim Fit',     120000, 2, 240000),
(10, 6, 'Kaos Graphic Streetwear',   100000, 1, 100000);

