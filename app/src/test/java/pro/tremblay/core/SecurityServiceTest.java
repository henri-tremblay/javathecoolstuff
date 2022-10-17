package pro.tremblay.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityServiceTest {

    private final SecurityService securityService = new SecurityService(Paths.get("../listing_short.csv"));

    @BeforeEach
    @AfterEach
    void after() {
        securityService.clear();
    }

    @Test
    void allSecurities() {
        assertThat(securityService.allSecurities()).containsExactlyInAnyOrderElementsOf(SecuritiesForTest.SECURITIES);
    }

    @Test
    void findForTicker() {
        assertThat(securityService.findForTicker("IBM")).isEqualTo(SecuritiesForTest.IBM);
    }
}
