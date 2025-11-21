-- Data dummy untuk tabel pm_user

INSERT IGNORE INTO pm_user (nama_lengkap, email, password_hash, nomor_telepon, tanggal_daftar, peran_pengguna, aktif)
VALUES
  ('Admin Thriftly', 'admin@thriftly.com', 'admin123', '081200000001', NOW(), 'admin', 1),
  ('Budi Thrift', 'budi@thriftly.com', 'budi123', '081200000002', NOW(), 'customer', 1),
  ('Siti Thrift', 'siti@thriftly.com', 'siti123', '081200000003', NOW(), 'customer', 1);
