import java.util.Scanner;

public class Soal2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int peripheralAmount = 0;

        // Input jumlah komputer
        System.out.print("Masukkan jumlah komputer: ");
        int computerAmount = sc.nextInt();

        // Input jumlah peripheral
        if (computerAmount > 2) {
            System.out.print("Masukkan jumlah peripheral: ");
            peripheralAmount = sc.nextInt();
        }

        // Input apakah di luar jam kerja
        System.out.print("Apakah service di luar jam kerja? (true/false): ");
        boolean outsideBusinessHours = sc.nextBoolean();

        // Input apakah customer mau antar-jemput sendiri
        System.out.print("Apakah customer mau antar-jemput sendiri? (true/false): ");
        boolean selfService = sc.nextBoolean();

        double baseFee = 0;
        double additionalFee = 0;

        // DO CASE logic
        if (computerAmount == 1 || computerAmount == 2) {
            baseFee = 50;
            additionalFee = 0;
        } else if (computerAmount >= 3 && computerAmount <= 10) {
            baseFee = 100;
            additionalFee = 10 * peripheralAmount;
        } else if (computerAmount > 10) {
            baseFee = 500;
            additionalFee = 10 * peripheralAmount;
        }

        double totalBaseFee = baseFee + additionalFee;
        
        // Jika di luar jam kerja, biaya dasar jadi 2x lipat
        if (outsideBusinessHours) {
            totalBaseFee *= 2;
        }

        // Jika customer antar-jemput sendiri, biaya jadi setengah
        if (selfService) {
            totalBaseFee /= 2;
        }

        // Output hasil
        System.out.println("Biaya servis total: $" + totalBaseFee);

        sc.close();
    }
}

