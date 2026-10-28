package ar.edu.um.prog2.turnosyreservas.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class HexagonalArchitectureTest {

    static final String BASE_PACKAGE = "ar.edu.um.prog2.turnosyreservas";

    static JavaClasses classes;

    @BeforeAll
    static void importClasses() {
        classes = new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS).importPackages(BASE_PACKAGE);
    }

    // ── Feature-first checks (availability/) ─────────────────────────────────

    @Test
    void availability_domain_no_depende_de_infrastructure() {
        ArchRule rule = noClasses()
            .that()
            .resideInAnyPackage("..availability.domain..", "..availability.application..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..availability.infrastructure..");

        rule.check(classes);
    }

    // ── Feature-first checks (hold/) ─────────────────────────────────────────

    @Test
    void hold_domain_no_depende_de_infrastructure() {
        ArchRule rule = noClasses()
            .that()
            .resideInAnyPackage("..hold.domain..", "..hold.application..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..hold.infrastructure..");

        rule.check(classes);
    }

    // ── Feature-first checks (reservation/) ──────────────────────────────────

    @Test
    void reservation_domain_no_depende_de_infrastructure() {
        ArchRule rule = noClasses()
            .that()
            .resideInAnyPackage("..reservation.domain..", "..reservation.application..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..reservation.infrastructure..");

        rule.check(classes);
    }

    // ── Feature-first checks (user/) ──────────────────────────────────────────

    @Test
    void user_domain_no_depende_de_infrastructure() {
        ArchRule rule = noClasses()
            .that()
            .resideInAnyPackage("..user.domain..", "..user.application..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..user.infrastructure..");

        rule.check(classes);
    }
}
