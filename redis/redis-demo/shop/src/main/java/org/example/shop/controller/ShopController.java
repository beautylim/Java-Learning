package org.example.shop.controller;

import org.example.shop.model.entity.Shop;
import org.example.shop.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('shop:get')")
    public ResponseEntity<Shop> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(shopService.findShopById(id));
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyAuthority('shop:edit')")
    public ResponseEntity<Shop>  update(@PathVariable Long id, @RequestBody Shop shop){
        return ResponseEntity.status(HttpStatus.OK).body(shopService.updateShop(id, shop));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Shop>> search(@RequestParam Double x,
                                             @RequestParam Double y,
                                             @RequestParam(defaultValue = "1") int pageNo,
                                             @RequestParam int distance){

        return ResponseEntity.status(HttpStatus.OK).body(shopService.searchByGEO(x, y, pageNo, distance));

    }
}
