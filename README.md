PRICE COMPARATOR
================

A **Spring Boot 3 / Java 21** micro‑service that imports daily price & discount CSVs, stores them in a relational database and exposes a REST API for:

* browsing the product catalog and current prices
* inspecting price history (min / avg) and full timelines
* fetching best‑value recommendations & current discounts
* subscribing to e‑mail price alerts

Goal: help consumers track price evolution and choose the best moment to buy.

--------------------------------------------------------------------
1  Requirements
--------------------------------------------------------------------
| Tool | Version |
|------|---------|
| Java | **21 LTS** |
| Docker / Compose | 24 / v2 (optional) |
| Gradle 8 + / Maven 3.9 + | if you build locally |
| PostgreSQL 15 + | only if you *don’t* use Compose |

--------------------------------------------------------------------
2  Quick start (Docker Compose)
--------------------------------------------------------------------
```bash
git clone https://github.com/<org>/price-comparator.git
cd price-comparator
docker compose build            # build image
docker compose up               # app + db + MailHog
```

* API root   `http://localhost:8080/api`
* Swagger UI   `http://localhost:8080/swagger-ui.html`
* MailHog UI   `http://localhost:8025`

Stop & clean:

```bash
Ctrl‑C
docker compose down -v
```

--------------------------------------------------------------------
3  Building from source
--------------------------------------------------------------------
```bash
# Gradle
./gradlew clean build

# Maven
mvn clean package
```
Runnable JAR in `build/libs/` (Gradle) or `target/` (Maven).  
Override props via *application.yml*, env‑vars or `--spring.config.additional-location`.

--------------------------------------------------------------------
4  Project structure
--------------------------------------------------------------------
```
│
├── .gitignore
├── Dockerfile                                      # Application containerization
├── docker-compose.yml                              # Service definitions (DB, mail, etc.)
├── build.gradle (or pom.xml)                       # Build tool + dependencies
├── README.md                                       # Description, architecture, usage, endpoints
├── docs/
│   ├── api-spec.yaml                               # OpenAPI / Swagger definition
│   ├── architecture.md                             # Component diagram & flow explanations
│   └── database-schema.sql                         # For relational DBs, if used
│
└── src/
    ├── main/
    │   ├── java/eu/accesa/pricecomparator/
    │   │   ├── PriceComparatorApplication.java     # Spring Boot entry point
    │   │   │
    │   │   ├── config/                             # Cross-cutting configuration
    │   │   │   ├── CsvConfig.java                  # CSV paths and formats
    │   │   │   ├── SchedulingConfig.java           # Periodic jobs (import, alerts)
    │   │   │   └── OpenApiConfig.java              # Springdoc / Swagger setup
    │   │   │
    │   │   ├── exception/                          # Global error handling
    │   │   │   ├── ApiError.java                   # DTO for error responses
    │   │   │   ├── GlobalExceptionHandler.java     # @ControllerAdvice class
    │   │   │   ├── NotFoundException.java
    │   │   │   └── ValidationException.java
    │   │   │
    │   │   ├── common/                             # Reusable utilities
    │   │   │   └── util/
    │   │   │       ├── CsvParser.java              # Generic parser using OpenCSV
    │   │   │       ├── UnitConversionUtil.java     # kg↔g, l↔ml, pcs↔pcs
    │   │   │       └── DateUtils.java              # e.g. "last 24h" calculation
    │   │   │
    │   │   ├── catalog/                            # Catalog & Pricing domain
    │   │   │   ├── controller/
    │   │   │   │   └── PriceController.java        # /api/prices, /api/prices/history
    │   │   │   ├── service/
    │   │   │   │   ├── PriceService.java           # CSV import + business logic
    │   │   │   │   └── PriceHistoryService.java    # trends, history
    │   │   │   ├── repository/
    │   │   │   │   └── PriceRepository.java        # Spring Data JPA / JDBC Template
    │   │   │   ├── model/
    │   │   │   │   └── PriceRecord.java            # @Entity / internal DTO
    │   │   │   └── dto/
    │   │   │       └── PriceDto.java               # Exposed client schema
    │   │   │
    │   │   ├── discount/                           # Discounts domain
    │   │   │   ├── controller/
    │   │   │   │   └── DiscountController.java     # /api/discounts, /api/discounts/new
    │   │   │   ├── service/
    │   │   │   │   ├── DiscountService.java        # top-percent, new-since-24h logic
    │   │   │   │   └── DiscountImportService.java  # Import discount CSVs
    │   │   │   ├── repository/
    │   │   │   │   └── DiscountRepository.java
    │   │   │   ├── model/
    │   │   │   │   └── Discount.java
    │   │   │   └── dto/
    │   │   │       └── DiscountDto.java
    │   │   │
    │   │   ├── analytics/                          # Recommendations & Reporting
    │   │   │   ├── controller/
    │   │   │   │   └── AnalyticsController.java    # /api/analytics/…
    │   │   │   ├── service/
    │   │   │   │   ├── TrendService.java           # Generates TrendPointDto[]
    │   │   │   │   └── RecommendationService.java  # best-unit-price, substitutions
    │   │   │   ├── dto/
    │   │   │   │   ├── TrendPointDto.java          # { date, unitPrice }
    │   │   │   │   └── RecommendationDto.java      # Product suggestions
    │   │   │   └── util/
    │   │   │       └── UnitPriceCalculator.java
    │   │   │
    │   │   └── alert/                              # Price alerts domain
    │   │       ├── controller/
    │   │       │   └── AlertController.java        # CRUD + manual alert check
    │   │       ├── service/
    │   │       │   └── AlertService.java           # Scheduling + checkAlerts()
    │   │       ├── repository/
    │   │       │   └── AlertRepository.java
    │   │       ├── model/
    │   │       │   └── PriceAlert.java             # targetPrice, userId, active
    │   │       └── dto/
    │   │           └── AlertRequestDto.java        # { productId, targetPrice, … }
    │   │
    │   └── resources/
    │       ├── application.yml                     # CSV dirs, cron expressions, DB URL
    │       ├── logback-spring.xml                  # Logging configuration
    │       └── data/                               # Sample CSVs for manual testing
    │
    └── test/
        ├── java/eu/accesa/pricecomparator/
        │   ├── catalog/                            # PriceServiceTest, PriceControllerTest
        │   ├── discount/                           # DiscountServiceTest
        │   ├── analytics/                          # TrendServiceTest
        │   ├── alert/                              # AlertServiceTest
        │   └── common/                             # CsvParserTest, UnitConversionUtilTest
        └── resources/
            └── test-data/                          # Test CSVs for fixtures
```

--------------------------------------------------------------------
5  API overview
--------------------------------------------------------------------
| Verb | Path | Purpose |
|------|------|---------|
| **POST** | `/api/prices/import?date=YYYY-MM-DD` | import price CSV |
| **GET** | `/api/prices/history/min` | daily minimum (unit‑price) |
| **GET** | `/api/prices/history/avg` | daily average (unit‑price) |
| **GET** | `/api/analytics/prices/history` | full timeline (all points) |
| **GET** | `/api/discounts/best?limit=10` | top % discounts |
| **GET** | `/api/discounts/new?hours=24` | newly added discounts |
| **POST** | `/api/alerts` | create price alert |
| **POST** | `/api/alerts/check` | trigger alert scan manually |

Full spec in **`docs/api-spec.yaml`** or live at `/v3/api-docs`.

--------------------------------------------------------------------
6  Scheduled jobs (defaults)
--------------------------------------------------------------------
| Job | CRON | Description |
|-----|------|-------------|
| `importPricesJob` | `0 0 2 * * ?` | import previous‑day price CSV |
| `importDiscountsJob` | `0 30 2 * * ?` | import daily discounts CSV |
| `checkAlertsJob` | `0 */15 * * * ?` | notify users when alert met |

(Override via *application.yml*.)

--------------------------------------------------------------------
7  Demo in 30 seconds
--------------------------------------------------------------------
```bash
# import two price files
curl -X POST "http://localhost:8080/api/prices/import?date=2025-05-01"
curl -X POST "http://localhost:8080/api/prices/import?date=2025-05-08"

# aggregated trends
curl "http://localhost:8080/api/prices/history/min?productId=P001&from=2025-05-01&to=2025-05-08"
curl "http://localhost:8080/api/prices/history/avg?productId=P001&from=2025-05-01&to=2025-05-08"

# full timeline
curl "http://localhost:8080/api/analytics/prices/history?productId=P001&from=2025-05-01&to=2025-05-08"
```

--------------------------------------------------------------------
8  Testing
--------------------------------------------------------------------
```bash
./gradlew clean test jacocoTestReport    # or: mvn test
```
Fixtures in `src/test/resources/test-data/`.  
Coverage HTML under `build/reports/jacoco/`.

--------------------------------------------------------------------
9  Contributing
--------------------------------------------------------------------
1. Fork → feature branch → PR  
2. Use **conventional commits** (`feat:`, `fix:`, `chore:` …)  
3. Add/extend unit tests & update OpenAPI spec  
4. Ensure `./gradlew test` passes

--------------------------------------------------------------------
10  License
--------------------------------------------------------------------
Released under the **MIT License** – see `LICENSE`.
