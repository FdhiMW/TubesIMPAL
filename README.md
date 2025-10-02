# Tubes IMPAL - PSPEC

## Soal 1 — Determine Triangle

### Problem Statement
Menentukan jenis segitiga berdasarkan tiga sisi `a`, `b`, `c` yang dapat berupa bilangan bulat atau pecahan.

### Inputs
- Tiga buah bilangan `a`, `b`, `c` (integer atau float).

### Outputs
- Jenis segitiga yang terbentuk, yaitu:
  - Tidak ada segitiga
  - Sama Sisi (Equilateral)
  - Sama Kaki (Isosceles)
  - Siku-Siku (Right Triangle)
  - Bebas/Scalene

### Processing
1. Jika ada sisi ≤ 0 → Tidak ada segitiga.  
2. Jika sisi terbesar ≥ jumlah dua sisi lainnya → Tidak ada segitiga.  
3. Jika `a = b = c` → Segitiga Sama Sisi.  
4. Jika dua sisi sama → Segitiga Sama Kaki.  
5. Jika `(sisi terbesar)² = (sisi1)² + (sisi2)²` → Segitiga Siku-Siku.  
6. Jika tidak memenuhi kriteria di atas namun tetap membentuk segitiga → Segitiga Bebas.  

### Constraints
- Untuk bilangan pecahan: gunakan ketelitian 0.01 (1%).  
  - Selisih ≤ 1% → dianggap sama.  
  - Selisih > 1% → dianggap berbeda.  

---

## Soal 2 — Menghitung Biaya Service

### Problem Statement
Menghitung biaya service berdasarkan jumlah komputer dan kondisi tambahan.

### Inputs
- `computer-amount` (jumlah komputer).
- `peripheral` (jumlah peripheral, jika ada).
- `service-time` (waktu pelayanan).
- `customer-choice` (drop-off & pick-up atau tidak).

### Outputs
- Total biaya service (`total-fee`).

### Processing
1. Tentukan `base-fee` dan `additional-fee` berdasarkan jumlah komputer:
   - 1–2 komputer → base-fee = $50, additional = 0.
   - 3–10 komputer → base-fee = $100, additional = $10 per peripheral.
   - >10 komputer → base-fee = $500, additional = $10 per peripheral.
2. Jika service-time di luar jam kerja → base-fee dilipatgandakan.  
3. Jika customer drop-off & pick-up → total base-fee dikurangi 50%.  
4. Hitung `total-fee = base-fee + additional-fee`.  

### Constraints
- Jumlah komputer > 0.  
- Peripheral bisa 0 atau lebih.  

---

## Soal 3 — ACCOUNTS

### Problem Statement
Mengecek validitas account-number untuk menghasilkan invoice atau error message.

### Inputs
- `account-number` (dari customer).  
- `ACCOUNTS` (database berisi daftar akun dan status-code).  

### Outputs
- Invoice berisi `account-number` dan `amount-of-sale`, **atau**  
- Error message jika account tidak valid.  

### Processing
1. Cek apakah `account-number` ada dalam **ACCOUNTS**.  
2. Jika ada dan `status-code = "valid"` → cetak invoice.  
3. Jika tidak ditemukan atau status-code ≠ "valid"` → tampilkan error message.  

### Constraints
- Account-number harus unik dalam **ACCOUNTS**.  
- Status-code hanya boleh `"valid"` atau `"invalid"`.  

---

## Struktur Proyek (Java)

