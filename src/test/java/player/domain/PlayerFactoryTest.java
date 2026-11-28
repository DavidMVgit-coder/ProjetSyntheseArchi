package player.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PlayerFactoryTest {

    private Player player(String pseudonym, String email) {
        return PlayerFactory.create(new Pseudonym(pseudonym), new Email(email), new PasswordHash("hash"));
    }

    @Test
    void generatesTheSameIdForTheSameEmail() {
        assertEquals(player("Aria", "aria@mail.com").getId(), player("Aria", "aria@mail.com").getId());
    }

    @Test
    void ignoresEmailCaseWhenGeneratingTheId() {
        assertEquals(player("Aria", "Aria@Mail.com").getId(), player("Aria", "aria@mail.com").getId());
    }

    @Test
    void generatesDifferentIdsForDifferentEmails() {
        assertNotEquals(player("Aria", "aria@mail.com").getId(), player("Bram", "bram@mail.com").getId());
    }
}
