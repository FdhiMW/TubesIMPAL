package com.onlineshop.repository;

import com.onlineshop.model.KeranjangItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeranjangRepository extends JpaRepository<KeranjangItem, Long> {

    List<KeranjangItem> findByUserIdUser(Long idUser);

    Optional<KeranjangItem> findByUserIdUserAndProdukIdProduk(Long idUser, Long idProduk);

    void deleteByUserIdUser(Long idUser);
}
