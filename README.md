# 🌍 Global Product Price Comparison Tool

A generic and extensible backend service that fetches **real-time prices of a product** from **multiple e-commerce websites** across **different countries**, ranking them in **ascending order** by price.

> ✅ Works across India 🇮🇳, USA 🇺🇸, and extendable to all major e-commerce regions.
> ✅ Site-specific scraping logic using **Selenium** and **Jsoup** (for static pages).
> ✅ Returns results only for **accurate product matches** with cleaned prices, names, and links.

---

## 🔍 Sample Queries

```json
POST /compare

Body:
{ "country": "US", "query": "iPhone 16 Pro, 128GB" }

{ "country": "IN", "query": "boAt Airdopes 311 Pro" }

GET/ http://localhost:8089/compare?country=in&query=apple%20iphone%2014%20128gb
```  

## 🚀 How It Works
Based on the selected country (IN, US, etc.), the scraper visits relevant websites.

Uses Selenium WebDriver for dynamic websites (e.g., Flipkart, TataCliq, Amazon).

Parses the DOM to extract accurate product name, price, and link.


## Run By Maven
git clone https://github.com/yourusername/global-price-comparer.git

cd global-price-comparer

mvn spring-boot:run


### App runs on: http://localhost:8089/compare


### 🧩 Dependencies

- **Java** 17 or higher
- **Spring Boot** 3.x
- **Maven**
- **Selenium** `4.21.0`
- **WebDriverManager** `5.7.0`
- **ChromeDriver** (compatible with your local Chrome version)
- **Jsoup** `1.21.1`
- **Lombok** `1.18.38`
- **Spring Boot DevTools**
