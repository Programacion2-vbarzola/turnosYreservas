package ar.edu.um.prog2.turnosyreservas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Turnos Yreservas.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Liquibase liquibase = new Liquibase();

    private final CatalogService catalogService = new CatalogService();

    // jhipster-needle-application-properties-property

    public Liquibase getLiquibase() {
        return liquibase;
    }

    public CatalogService getCatalogService() {
        return catalogService;
    }

    // jhipster-needle-application-properties-property-getter

    public static class Liquibase {

        private Boolean asyncStart = true;

        public Boolean getAsyncStart() {
            return asyncStart;
        }

        public void setAsyncStart(Boolean asyncStart) {
            this.asyncStart = asyncStart;
        }
    }

    public static class CatalogService {

        private String baseUrl = "http://localhost:8081";
        private String serviceJwt = "";

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getServiceJwt() {
            return serviceJwt;
        }

        public void setServiceJwt(String serviceJwt) {
            this.serviceJwt = serviceJwt;
        }
    }
    // jhipster-needle-application-properties-property-class
}
