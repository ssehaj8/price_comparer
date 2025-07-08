package com.project.price_comparer.controller;

import com.project.price_comparer.model.PriceResult;
import com.project.price_comparer.service.PriceCompareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/compare")
public class PriceController {

    @Autowired
    private PriceCompareService service;

    // Existing POST endpoint (keep this for other tools)
    @PostMapping
    public ResponseEntity<List<PriceResult>> comparePrices(@RequestBody Map<String, String> request) {
        String country = request.get("country");
        String query = request.get("query");
        return ResponseEntity.ok(service.compare(country, query));
    }

    @GetMapping
    public ResponseEntity<List<PriceResult>> comparePricesGet(@RequestParam String country, @RequestParam String query) {
        return ResponseEntity.ok(service.compare(country, query));
    }
}
