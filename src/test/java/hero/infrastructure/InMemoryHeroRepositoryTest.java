package hero.infrastructure;

import hero.domain.Characteristic;
import hero.domain.Hero;
import hero.domain.HeroClass;
import hero.domain.HeroFactory;
import hero.domain.HeroName;
import hero.domain.Species;
import hero.domain.exception.HeroAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryHeroRepositoryTest {

    private static final int CONCURRENT_CREATION_COUNT = 8;
    private static final int WAIT_TIMEOUT_SECONDS = 5;

    @Test
    void returnsEmptyForAnUnknownHero() {
        InMemoryHeroRepository repository = new InMemoryHeroRepository();

        assertTrue(repository.fetchById(UUID.randomUUID()).isEmpty());
    }

    @Test
    void findsACreatedHeroById() {
        InMemoryHeroRepository repository = new InMemoryHeroRepository();
        Hero hero = hero("Aria");

        repository.create(hero);

        assertSame(hero, repository.fetchById(hero.getHeroId()).orElseThrow());
    }

    @Test
    void rejectsASecondHeroWithTheSameNameWithoutSavingIt() {
        InMemoryHeroRepository repository = new InMemoryHeroRepository();
        Hero existing = hero("Aria");
        Hero attempted = hero("Aria");
        repository.create(existing);

        assertThrows(HeroAlreadyExistsException.class, () -> repository.create(attempted));

        assertTrue(repository.fetchById(attempted.getHeroId()).isEmpty());
    }

    @Test
    @Timeout(15)
    void acceptsOnlyOneOfSeveralConcurrentCreationsWithTheSameName() throws Exception {
        InMemoryHeroRepository repository = new InMemoryHeroRepository();
        CyclicBarrier start = new CyclicBarrier(CONCURRENT_CREATION_COUNT);
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_CREATION_COUNT);
        List<Future<?>> results = new ArrayList<>();

        try {
            for (int i = 0; i < CONCURRENT_CREATION_COUNT; i++) {
                Hero hero = hero("Aria");
                results.add(executor.submit(() -> {
                    start.await(WAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    repository.create(hero);
                    return null;
                }));
            }

            int successes = 0;
            for (Future<?> result : results) {
                try {
                    result.get(WAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    successes++;
                } catch (ExecutionException exception) {
                    assertInstanceOf(HeroAlreadyExistsException.class, exception.getCause());
                }
            }
            assertEquals(1, successes);
        } finally {
            executor.shutdownNow();
        }
    }

    private static Hero hero(String name) {
        Map<Characteristic, Integer> values = new EnumMap<>(Characteristic.class);
        for (Characteristic characteristic : Characteristic.values()) {
            values.put(characteristic, 10);
        }
        return HeroFactory.createHeroLevel1(new HeroName(name), Species.HUMAN, HeroClass.WARRIOR, values);
    }
}
