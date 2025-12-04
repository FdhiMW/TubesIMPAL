package com.onlineshop.controller;

import com.onlineshop.dto.PesananDtos;
import com.onlineshop.service.PesananService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @GetMapping("/admin")
    public List<PesananDtos.PesananResponse> getPesananAdmin(
            @RequestParam(name = "kode", required = false) String kodePesanan
    ) {
        return pesananService.getPesananForAdmin(kodePesanan);
    }

    @PatchMapping("/admin/{id}/status")
    public PesananDtos.PesananResponse updateStatusPesanan(
            @PathVariable("id") Long idPesanan,
            @RequestBody PesananDtos.UpdateStatusRequest request
    ) {
        return pesananService.updateStatusPesanan(idPesanan, request.getStatusPesanan());
    }
}
