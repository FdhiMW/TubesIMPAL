package com.onlineshop.dto;

public class AuthDtos {

    public static class RegisterRequest {
        private String namaLengkap;
        private String email;
        private String password;

        public String getNamaLengkap() { return namaLengkap; }
        public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class UserResponse {
        private Long idUser;
        private String namaLengkap;
        private String email;
        private String peranPengguna;

        public Long getIdUser() { return idUser; }
        public void setIdUser(Long idUser) { this.idUser = idUser; }

        public String getNamaLengkap() { return namaLengkap; }
        public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPeranPengguna() { return peranPengguna; }
        public void setPeranPengguna(String peranPengguna) { this.peranPengguna = peranPengguna; }
    }
}
