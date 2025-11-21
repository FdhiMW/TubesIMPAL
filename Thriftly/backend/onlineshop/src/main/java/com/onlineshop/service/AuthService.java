package com.onlineshop.service;

import com.onlineshop.dto.AuthDtos;
import com.onlineshop.model.User;
import com.onlineshop.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthDtos.UserResponse register(AuthDtos.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email sudah terdaftar");
        }

        User user = new User();
        user.setNamaLengkap(request.getNamaLengkap());
        user.setEmail(request.getEmail());
        user.setPasswordHash(request.getPassword());   // TODO: ganti jadi hash
        user.setNomorTelepon(null);
        user.setTanggalDaftar(LocalDateTime.now());
        user.setPeranPengguna("customer");
        user.setAktif(true);

        User saved = userRepository.save(user);
        return toUserResponse(saved);
    }

    public AuthDtos.UserResponse login(AuthDtos.LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email atau password salah"));

        if (!user.getPasswordHash().equals(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email atau password salah");
        }

        return toUserResponse(user);
    }

    private AuthDtos.UserResponse toUserResponse(User user) {
        AuthDtos.UserResponse resp = new AuthDtos.UserResponse();
        resp.setIdUser(user.getIdUser());
        resp.setNamaLengkap(user.getNamaLengkap());
        resp.setEmail(user.getEmail());
        resp.setPeranPengguna(user.getPeranPengguna());
        return resp;
    }
}
