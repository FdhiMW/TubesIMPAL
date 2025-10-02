import java.util.Scanner;

class Account {
    int accountNumber;
    String status;

    Account(int accountNumber, String status) {
        this.accountNumber = accountNumber;
        this.status = status;
    }
}

public class Soal3 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Database akun (contoh)
        Account[] accounts = {
            new Account(1001, "valid"),
            new Account(1002, "invalid"),
            new Account(1003, "valid")
        };
         // Input
        System.out.print("Masukkan account number: ");
        int inputAcc = sc.nextInt();
        System.out.print("Masukkan biaya servis (amount-of-sale): ");
        double amount = sc.nextDouble();

        boolean found = false;
        // === Precondition 1 ===
        for (Account acc : accounts) {
            if (acc.accountNumber == inputAcc) {
                found = true;
                if (acc.status.equalsIgnoreCase("valid")) {
                    // === Postcondition 1 ===
                    System.out.println("\n=== INVOICE ===");
                    System.out.println("Account Number : " + inputAcc);
                    System.out.printf("Amount of Sale : Rp %.2f\n", amount);
                } else {
                    // === Postcondition 2 ===
                    System.out.println("\nERROR: Status akun tidak valid.");
                }
                break;
            }
        }
        if (!found) {
            // === Postcondition 2 ===
            System.out.println("\nERROR: Account number tidak ditemukan.");
        }

        sc.close();
    }
}
