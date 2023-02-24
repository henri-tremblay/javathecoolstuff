package pro.tremblay.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FakePriceServiceTest {

    PriceService priceService = new FakePriceService();

    @Test
    void getPrice_alwaysTheSamePriceForASecurity() {
        Amount a1 = priceService.getPrice(SecuritiesForTest.GOOGL);
        Amount a2 = priceService.getPrice(SecuritiesForTest.GOOGL);
        assertSame(a1, a2);
    }

    @Test
    void getPrice_randomPrices() {
        Amount a1 = priceService.getPrice(SecuritiesForTest.GOOGL);
        Amount a2 = priceService.getPrice(SecuritiesForTest.IBM);
        assertNotEquals(a1, a2);
    }


}
