package com.onlineshop.controller;

import com.onlineshop.dto.KeranjangDtos;
import com.onlineshop.service.KeranjangService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keranjang")
@CrossOrigin(origins = "*")
public class KeranjangController {

    private final KeranjangService keranjangService;

    public KeranjangController(KeranjangService keranjangService) {
        this.keranjangService = keranjangService;
    }

    // GET /api/keranjang?idUser=1
    @GetMapping
    public List<KeranjangDtos.CartItemResponse> getKeranjang(
            @RequestParam("idUser") Long idUser
    ) {
        return keranjangService.getKeranjangByUser(idUser);
    }

    // POST /api/keranjang
    @PostMapping
    public KeranjangDtos.CartItemResponse addToKeranjang(
            @RequestBody KeranjangDtos.AddCartRequest request
    ) {
        return keranjangService.addToKeranjang(request);
    }

    // PATCH /api/keranjang/{id}/qty
    @PatchMapping("/{id}/qty")
    public KeranjangDtos.CartItemResponse updateJumlah(
            @PathVariable("id") Long idKeranjang,
            @RequestBody KeranjangDtos.UpdateCartRequest request
    ) {
        return keranjangService.updateJumlah(idKeranjang, request.getJumlah());
    }

    // DELETE /api/keranjang/{id}
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable("id") Long idKeranjang) {
        keranjangService.deleteItem(idKeranjang);
    }

    // DELETE /api/keranjang/user/{idUser}
    @DeleteMapping("/user/{idUser}")
    public void clearKeranjang(@PathVariable("idUser") Long idUser) {
        keranjangService.clearKeranjangByUser(idUser);
    }
}
