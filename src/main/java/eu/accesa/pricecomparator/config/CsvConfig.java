package eu.accesa.pricecomparator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "csv")
public class CsvConfig {

    /** ex: src/main/resources/data (relative) */
    private String directory;

    public String getDirectory() {
        return directory;
    }
    public void setDirectory(String directory) {
        this.directory = directory;
    }
}