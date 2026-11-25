package player;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = {"player", "api"}, importOptions = ImportOption.DoNotIncludeTests.class)
class PlayerArchitectureTest {

    @ArchTest
    static final ArchRule domainDependsOnNothingOutsideItself =
            noClasses().that().resideInAPackage("player.domain..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "player.application..", "player.infrastructure..", "player.api..", "api..",
                            "jakarta..", "com.fasterxml..", "com.password4j..");

    @ArchTest
    static final ArchRule applicationDoesNotKnowInfrastructureOrApi =
            noClasses().that().resideInAPackage("player.application..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "player.infrastructure..", "player.api..", "api..",
                            "jakarta..", "com.fasterxml..", "com.password4j..");

    @ArchTest
    static final ArchRule playerApiOnlyTalksToTheApplicationLayer =
            noClasses().that().resideInAPackage("player.api..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "player.domain..", "player.infrastructure..");

    @ArchTest
    static final ArchRule playerContextIsIndependentFromOtherContexts =
            noClasses().that().resideInAPackage("player..")
                    .should().dependOnClassesThat().resideInAnyPackage("lobby..", "hero..", "item..");
}
