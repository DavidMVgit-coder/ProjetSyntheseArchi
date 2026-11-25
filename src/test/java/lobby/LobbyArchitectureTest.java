package lobby;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "lobby", importOptions = ImportOption.DoNotIncludeTests.class)
class LobbyArchitectureTest {

    @ArchTest
    static final ArchRule domainDependsOnNothingOutsideItself =
            noClasses().that().resideInAPackage("lobby.domain..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "lobby.application..", "lobby.infrastructure..", "lobby.api..", "api..",
                            "jakarta..", "com.fasterxml..");

    @ArchTest
    static final ArchRule applicationDoesNotKnowInfrastructureOrApi =
            noClasses().that().resideInAPackage("lobby.application..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "lobby.infrastructure..", "lobby.api..", "api..",
                            "jakarta..", "com.fasterxml..");

    @ArchTest
    static final ArchRule lobbyApiOnlyTalksToTheApplicationLayer =
            noClasses().that().resideInAPackage("lobby.api..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "lobby.domain..", "lobby.infrastructure..");

    @ArchTest
    static final ArchRule lobbyContextIsIndependentFromOtherContexts =
            noClasses().that().resideInAPackage("lobby..")
                    .should().dependOnClassesThat().resideInAnyPackage("player..", "hero..", "item..");
}
