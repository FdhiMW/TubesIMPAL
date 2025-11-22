package com.onlineshop.controller;

import com.onlineshop.dto.PesananDtos;
import com.onlineshop.service.PesananService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pesanan")
@CrossOrigin(origins = "*")
public class PesananController {

    private final PesananService pesananService;

    public PesananController(PesananService pesananService) {
        this.pesananService = pesananService;
    }

    @PostMapping
    public PesananDtos.PesananResponse createPesanan(
            @RequestBody PesananDtos.CreatePesananRequest request
    ) {
        return pesananService.createPesanan(request);
    }
}
