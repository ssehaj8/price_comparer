package com.project.price_comparer.util;

import com.project.price_comparer.model.PriceResult;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class WebScraper {

    private static final int DELAY = 3000;

    private static WebDriver createDriver() {
        ChromeOptions options = new ChromeOptions();
       // options.setHeadless(false); // Important: headless false for Amazon
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--window-size=1920,1200");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36");
        return new ChromeDriver(options);
    }

    private static void sleep() {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException ignored) {}
    }

    private static List<PriceResult> scrape(String url, String seller, String currency, String itemSelector, String titleSelector, String priceSelector, String linkSelector) {
        List<PriceResult> results = new ArrayList<>();
        WebDriver driver = createDriver();
        try {
            driver.get(url);
            sleep();

            List<WebElement> items = driver.findElements(By.cssSelector(itemSelector));
            for (WebElement item : items) {
                try {
                    String title = item.findElement(By.cssSelector(titleSelector)).getText();
                    String price = item.findElement(By.cssSelector(priceSelector)).getText().replaceAll("[^\\d.]", "");
                    String link = item.findElement(By.cssSelector(linkSelector)).getAttribute("href");

                    if (!title.isEmpty() && !price.isEmpty()) {
                        results.add(new PriceResult(title, link, price, currency, seller));
                        if (results.size() >= 5) break;
                    }
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            System.err.println("❌ Failed scraping " + seller + ": " + e.getMessage());
        } finally {
            driver.quit();
        }
        return results;
    }

    // ---------------- INDIA ----------------
    public static List<PriceResult> scrapeAmazonIN(String query) {
        List<PriceResult> results = new ArrayList<>();
        String url = "https://www.amazon.in/s?k=" + URLEncoder.encode(query, StandardCharsets.UTF_8);

        WebDriver driver = createDriver();
        try {
            driver.get(url);

            // Wait for product results to load
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("div.s-result-item[data-component-type='s-search-result']")
            ));

            List<WebElement> items = driver.findElements(By.cssSelector("div.s-result-item[data-component-type='s-search-result']"));

            for (WebElement item : items) {
                try {
                    String title = item.findElement(By.cssSelector("h2 a span")).getText();
                    String price = item.findElement(By.cssSelector("span.a-price > span.a-offscreen")).getText().replaceAll("[^\\d.]", "");
                    String link = item.findElement(By.cssSelector("h2 a")).getAttribute("href");

                    if (!title.isEmpty() && !price.isEmpty()) {
                        results.add(new PriceResult(title, link, price, "INR", "Amazon IN"));
                        if (results.size() >= 5) break;
                    }
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            System.err.println("❌ Failed scraping Amazon IN: " + e.getMessage());
        } finally {
            driver.quit();
        }

        return results;
    }

    public static List<PriceResult> scrapeFlipkart(String query) {
        String url = "https://www.flipkart.com/search?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "Flipkart", "INR",
                "div._1AtVbE",
                "div._4rR01T, a.IRpwTa",
                "div._30jeq3",
                "a._1fQZEK, a.IRpwTa");
    }

    public static List<PriceResult> scrapeCroma(String query) {
        String url = "https://www.croma.com/searchB?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "Croma", "INR",
                "li.product-item",
                "h3.product-title",
                ".amount",
                "a.product-title");
    }

    public static List<PriceResult> scrapeRelianceDigital(String query) {
        String url = "https://www.reliancedigital.in/search?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "Reliance Digital", "INR",
                "li.sp__product",
                ".sp__name",
                ".sp__finalPrice",
                "a.sp__product");
    }

    public static List<PriceResult> scrapeTataCliq(String query) {
        String url = "https://www.tatacliq.com/search/?searchCategory=all&text=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "Tata CLiQ", "INR",
                "div.ProductModule",
                "div.ProductDescription__Name",
                "div.ProductDescription__Price",
                "a.ProductModule");
    }

    public static List<PriceResult> scrapeVijaySales(String query) {
        String url = "https://www.vijaysales.com/search/" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "Vijay Sales", "INR",
                "div.product-box",
                ".product-name",
                ".offer-price",
                "a");
    }

    // ---------------- USA ----------------
    public static List<PriceResult> scrapeAmazonUS(String query) {
        String url = "https://www.amazon.com/s?k=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "Amazon US", "USD",
                "div.s-result-item",
                "h2 span",
                ".a-price .a-offscreen",
                "h2 a");
    }

    public static List<PriceResult> scrapeBestBuy(String query) {
        String url = "https://www.bestbuy.com/site/searchpage.jsp?st=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "BestBuy", "USD",
                "li.sku-item",
                "h4.sku-header a",
                ".priceView-customer-price span",
                "h4.sku-header a");
    }

    public static List<PriceResult> scrapeWalmartUS(String query) {
        String url = "https://www.walmart.com/search?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "Walmart", "USD",
                "div.search-result-gridview-item",
                "a.product-title-link span",
                "span.price-main span.visuallyhidden",
                "a.product-title-link");
    }

    public static List<PriceResult> scrapeTarget(String query) {
        String url = "https://www.target.com/s?searchTerm=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "Target", "USD",
                "li.h-padding-a-none",
                "a[data-test='product-title']",
                "div[data-test='current-price']",
                "a[data-test='product-title']");
    }

    public static List<PriceResult> scrapeNewegg(String query) {
        String url = "https://www.newegg.com/p/pl?d=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "Newegg", "USD",
                "div.item-cell",
                "a.item-title",
                "li.price-current strong",
                "a.item-title");
    }

    public static List<PriceResult> scrapeBHPhoto(String query) {
        String url = "https://www.bhphotovideo.com/c/search?Ntt=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "B&H Photo", "USD",
                "div.itemListing",
                "span.title",
                "span.price_1DPoToKrLP8uWvruGqgtaY",
                "a.itemImg");
    }

    // ---------------- UK ----------------
    public static List<PriceResult> scrapeAmazonUK(String query) {
        String url = "https://www.amazon.co.uk/s?k=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "Amazon UK", "GBP",
                "div.s-result-item",
                "h2 span",
                ".a-price .a-offscreen",
                "h2 a");
    }

    public static List<PriceResult> scrapeArgos(String query) {
        String url = "https://www.argos.co.uk/search/" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "Argos", "GBP",
                "div.ProductCard",
                "div.ProductCard-title",
                ".ProductCard-price",
                "a");
    }

    public static List<PriceResult> scrapeCurrys(String query) {
        String url = "https://www.currys.co.uk/search?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "Currys", "GBP",
                "div.product-tile",
                "h3.product-title",
                ".product-price",
                "a");
    }

    public static List<PriceResult> scrapeVeryUK(String query) {
        String url = "https://www.very.co.uk/e/q/" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "Very", "GBP",
                "li.product",
                "a.productTitle",
                "div.productPrice",
                "a.productTitle");
    }

    public static List<PriceResult> scrapeEbuyer(String query) {
        String url = "https://www.ebuyer.com/search?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "eBuyer", "GBP",
                "div.listing-product",
                "h4",
                ".price",
                "a");
    }

    // ---------------- CANADA ----------------
    public static List<PriceResult> scrapeAmazonCA(String query) {
        String url = "https://www.amazon.ca/s?k=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "Amazon CA", "CAD",
                "div.s-result-item",
                "h2 span",
                ".a-price .a-offscreen",
                "h2 a");
    }

    public static List<PriceResult> scrapeWalmartCA(String query) {
        String url = "https://www.walmart.ca/search?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "Walmart CA", "CAD",
                "div.product-tile-group",
                "span.product-title",
                "span.price-current",
                "a.product-link");
    }

    public static List<PriceResult> scrapeBestBuyCA(String query) {
        String url = "https://www.bestbuy.ca/en-ca/search?search=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "BestBuy CA", "CAD",
                "div.productItem",
                "h4.productItemName",
                "span.productPricing",
                "a");
    }

    public static List<PriceResult> scrapeCanadaComputers(String query) {
        String url = "https://www.canadacomputers.com/search/results_details.php?language=en&keywords=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "Canada Computers", "CAD",
                "div.search-result",
                "span.text-dark",
                "span.cc-price",
                "a");
    }

    // ---------------- AUSTRALIA ----------------
    public static List<PriceResult> scrapeAmazonAU(String query) {
        String url = "https://www.amazon.com.au/s?k=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "Amazon AU", "AUD",
                "div.s-result-item",
                "h2 span",
                ".a-price .a-offscreen",
                "h2 a");
    }

    public static List<PriceResult> scrapeJBHiFi(String query) {
        String url = "https://www.jbhifi.com.au/search?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "JB Hi-Fi", "AUD",
                "div.product-tile",
                "p.product-tile-title",
                ".product-tile-price",
                "a");
    }

    public static List<PriceResult> scrapeHarveyNorman(String query) {
        String url = "https://www.harveynorman.com.au/catalogsearch/result/?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "Harvey Norman", "AUD",
                "li.product-item",
                "a.product-item-link",
                "span.price",
                "a.product-item-link");
    }

    // ---------------- GERMANY ----------------
    public static List<PriceResult> scrapeAmazonDE(String query) {
        String url = "https://www.amazon.de/s?k=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "Amazon DE", "EUR",
                "div.s-result-item",
                "h2 span",
                ".a-price .a-offscreen",
                "h2 a");
    }

    public static List<PriceResult> scrapeMediaMarkt(String query) {
        String url = "https://www.mediamarkt.de/de/search.html?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "MediaMarkt", "EUR",
                "div.product-wrapper",
                "h2.title",
                "div.price",
                "a");
    }

    public static List<PriceResult> scrapeSaturn(String query) {
        String url = "https://www.saturn.de/de/search.html?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return scrape(url, "Saturn", "EUR",
                "div.product-wrapper",
                "h2.title",
                "div.price",
                "a");
    }
}
