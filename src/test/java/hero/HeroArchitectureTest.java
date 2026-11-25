package hero;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "hero", importOptions = ImportOption.DoNotIncludeTests.class)
class HeroArchitectureTest {

    @ArchTest
    static final ArchRule domainDependsOnNothingOutsideItself =
            noClasses().that().resideInAPackage("hero.domain..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "hero.application..", "hero.infrastructure..", "hero.api..", "api..",
                            "jakarta..", "com.fasterxml..");

    @ArchTest
    static final ArchRule applicationDoesNotKnowInfrastructureOrApi =
            noClasses().that().resideInAPackage("hero.application..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "hero.infrastructure..", "hero.api..", "api..",
                            "jakarta..", "com.fasterxml..");

    @ArchTest
    static final ArchRule heroApiOnlyTalksToTheApplicationLayer =
            noClasses().that().resideInAPackage("hero.api..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "hero.domain..", "hero.infrastructure..");

    @ArchTest
    static final ArchRule heroContextIsIndependentFromOtherContexts =
            noClasses().that().resideInAPackage("hero..")
                    .should().dependOnClassesThat().resideInAnyPackage("player..", "lobby..", "item..");
}
