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

    @Test
    void domain_no_depende_de_adapters() {
        ArchRule rule = noClasses()
            .that()
            .resideInAnyPackage("..domain.port..", "..domain.service..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..adapter..");

        rule.check(classes);
    }

    @Test
    void adapters_in_no_depende_de_adapters_out() {
        ArchRule rule = noClasses()
            .that()
            .resideInAPackage("..adapter.in..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..adapter.out..");

        rule.check(classes);
    }

    @Test
    void adapters_out_no_depende_de_adapters_in() {
        ArchRule rule = noClasses()
            .that()
            .resideInAPackage("..adapter.out..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..adapter.in..");

        rule.check(classes);
    }
}
