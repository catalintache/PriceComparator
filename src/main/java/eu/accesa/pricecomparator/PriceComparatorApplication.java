package eu.accesa.pricecomparator;

import eu.accesa.pricecomparator.config.CsvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties(CsvConfig.class)
@EnableScheduling
@ComponentScan(
		basePackages = "eu.accesa.pricecomparator",
		excludeFilters = @ComponentScan.Filter(
				type = FilterType.REGEX,
				pattern = "eu\\.accesa\\.pricecomparator\\.alert\\..*"   // tot sub-pachetul alert
		) // am incercat sa termin in 2 zile proiectul, dar n-am reusit, am vazut tarziu mailul :D
)
@SpringBootApplication
public class PriceComparatorApplication {
	public static void main(String[] args) {
		SpringApplication.run(PriceComparatorApplication.class, args);
	}
}
