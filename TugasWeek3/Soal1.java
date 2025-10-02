import java.util.Scanner;

public class Soal1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input tiga sisi
        System.out.print("Masukkan sisi a: ");
        double a = sc.nextDouble();
        System.out.print("Masukkan sisi b: ");
        double b = sc.nextDouble();
        System.out.print("Masukkan sisi c: ");
        double c = sc.nextDouble();

        // Periksa segitiga valid atau tidak
        if (a <= 0 || b <= 0 || c <= 0) {
            System.out.println("Bukan segitiga (ada sisi <= 0)");
            return;
        }

        // Cari sisi terbesar
        double max = Math.max(a, Math.max(b, c));
        double sum = a + b + c - max; // jumlah 2 sisi lainnya

        if (max >= sum) {
            System.out.println("Bukan segitiga (melanggar aturan segitiga)");
            return;
        }

        // Tentukan jenis segitiga
        if (a == b && b == c) {
            System.out.println("Segitiga Sama Sisi (Equilateral)");
        } else if (a == b || b == c || a == c) {
            System.out.println("Segitiga Sama Kaki (Isosceles)");
        } else if (Math.abs(max*max - (sum*sum - 2*a*b)) < 1e-9) {
            // Alternatif cara cek: a^2 + b^2 == c^2 (Pythagoras)
            // Lebih aman cek manual
            if (isRightTriangle(a, b, c)) {
                System.out.println("Segitiga Siku-Siku (Right Triangle)");
            } else {
                System.out.println("Segitiga Bebas (Scalene)");
            }
        } else if (isRightTriangle(a, b, c)) {
            System.out.println("Segitiga Siku-Siku (Right Triangle)");
        } else {
            System.out.println("Segitiga Bebas (Scalene)");
        }

        sc.close();
    }

    // Method bantu untuk cek segitiga siku-siku
    static boolean isRightTriangle(double a, double b, double c) {
        double[] arr = {a, b, c};
        java.util.Arrays.sort(arr);
        double x = arr[0], y = arr[1], z = arr[2]; // z terbesar
        return Math.abs(z*z - (x*x + y*y)) < 1e-9;
    }
}
