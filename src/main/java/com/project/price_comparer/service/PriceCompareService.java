package com.project.price_comparer.service;

import com.project.price_comparer.model.PriceResult;
import com.project.price_comparer.util.WebScraper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriceCompareService {
    public List<PriceResult> compare(String country, String query) {
        List<PriceResult> results = new ArrayList<>();
        if (country == null || query == null) return results;

        switch (country.toUpperCase()) {
            case "IN" -> {
                results.addAll(WebScraper.scrapeAmazonIN(query));
                results.addAll(WebScraper.scrapeFlipkart(query));
                results.addAll(WebScraper.scrapeCroma(query));
                results.addAll(WebScraper.scrapeTataCliq(query));
                results.addAll(WebScraper.scrapeRelianceDigital(query));
                results.addAll(WebScraper.scrapeVijaySales(query));
            }
            case "US" -> {
                results.addAll(WebScraper.scrapeAmazonUS(query));
                results.addAll(WebScraper.scrapeBestBuy(query));
                results.addAll(WebScraper.scrapeWalmartUS(query));
                results.addAll(WebScraper.scrapeTarget(query));
                results.addAll(WebScraper.scrapeNewegg(query));
                results.addAll(WebScraper.scrapeBHPhoto(query));
            }
            case "UK" -> {
                results.addAll(WebScraper.scrapeAmazonUK(query));
                results.addAll(WebScraper.scrapeArgos(query));
                results.addAll(WebScraper.scrapeCurrys(query));
                results.addAll(WebScraper.scrapeVeryUK(query));
                results.addAll(WebScraper.scrapeEbuyer(query));
            }
            case "CA" -> {
                results.addAll(WebScraper.scrapeAmazonCA(query));
                results.addAll(WebScraper.scrapeWalmartCA(query));
                results.addAll(WebScraper.scrapeBestBuyCA(query));
                results.addAll(WebScraper.scrapeCanadaComputers(query));
            }
            case "AU" -> {
                results.addAll(WebScraper.scrapeAmazonAU(query));
                results.addAll(WebScraper.scrapeJBHiFi(query));
                results.addAll(WebScraper.scrapeHarveyNorman(query));
            }
            case "DE" -> {
                results.addAll(WebScraper.scrapeAmazonDE(query));
                results.addAll(WebScraper.scrapeMediaMarkt(query));
                results.addAll(WebScraper.scrapeSaturn(query));
            }
            default -> System.out.println("Country not supported: " + country);
        }

        return results.stream()
                .filter(p -> p.getPrice() != null && p.getPrice().matches("\\d+(\\.\\d+)?"))
                .sorted(Comparator.comparingDouble(p -> Double.parseDouble(p.getPrice())))
                .collect(Collectors.toList());
    }

}
