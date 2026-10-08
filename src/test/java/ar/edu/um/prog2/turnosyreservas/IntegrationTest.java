package ar.edu.um.prog2.turnosyreservas;

import ar.edu.um.prog2.turnosyreservas.config.AsyncSyncConfiguration;
import ar.edu.um.prog2.turnosyreservas.config.EmbeddedKafka;
import ar.edu.um.prog2.turnosyreservas.config.EmbeddedSQL;
import ar.edu.um.prog2.turnosyreservas.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { TurnosYreservasApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
