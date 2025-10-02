# TubesIMPAL

Soal 1
DETERMINE TRIANGLE
Dari masukan 3 bilangan a, b, c bebas (boleh bulat, boleh pecahan), segitiga apa dapat dibangun?
- Jika ada yang negatif atau 0, tidak ada segitiga dapat dibangun.
- Jika bilangan yang terbesar lebih besar atau sama dengan penjumlahan dua bilangan lainnya yang lebih kecil, tidak ada segitiga dapat dibangun.
- Jika a=b atau b=c atau a=c (namun tidak sama dengan salah satu yang lain maka segitiga SAMA KAKI. (ISOSCELES)
- Jika a=b dan b=c maka segitiga SAMA SISI. (EQUILATERAL)
- Jika kuadrat bilangan terbesar = penjumlahan dari kuadrat dua bilangan lainnya, maka SEGITIGA SIKU-SIKU. (RIGHT TRIANGLE)
- Jika bukan sama kaki, sama sisi atau siku-siku, namun bilangan terbesarnya lebih kecil daripada penjumlahan dua bilangan lainnya, maka SEGITIGA BEBAS.
SOAL
1. Buat code (Pascal/C/PHP) untuk masukan bilangan bulat
2. Buat untuk bilangan pecahan dengan ketelitian 0.01 (1%) .Ketelitian 1% berarti selisih panjang
sisi-sisinya tidak lebih dari 1%. Selisih lebih dari 1% dianggap beda. Selisih 1% atau kurang dianggap
sama.

Soal 2
DO CASE
- CASE computer-amount = 1 OR 2
  set base-fee = $50 and additional-fee = 0
- CASE computer-amount = 3 to 10
  set base-fee = $100 and additional-fee = $10 per peripheral
- CASE computer-amount > 10
  set base-fee = $500 and additional-fee = $10 per peripheral
IF 
- IF service-time is NOT in business hours base-fee is doubled
- IF customer is willing to drop off and pick up total-base-fee is reduced to one-half

Soal 3
- Precondition 1
  Customer arrives with account-number matching an account number in ACCOUNTS, whose status-code is set to “valid.”
-  Postcondition 1
  Invoice is produced containing account-number and amount-of-sale.
- Precondition 2
  Precondition 1 fails for any reason (account-number can’t be found on ACCOUNTS or status-code is not equal to “valid”).
- Postcondition 2
  Error message is produced. 
