package org.example.shop.controller;

import org.example.shop.model.entity.Voucher;
import org.example.shop.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voucher")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @PostMapping
    public ResponseEntity<Voucher> insert(@RequestBody Voucher voucher){
        return ResponseEntity.status(HttpStatus.CREATED).body(voucherService.insert(voucher));
    }

    @GetMapping
    public ResponseEntity<List<Voucher>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(voucherService.select());
    }


}
