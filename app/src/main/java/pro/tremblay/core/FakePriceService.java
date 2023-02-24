package pro.tremblay.core;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@ThreadSafe
public class FakePriceService implements PriceService {

    private final ConcurrentMap<Security, Amount> prices = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @Nonnull
    @Override
    public Amount getPrice(@Nonnull Security security) {
        return prices.computeIfAbsent(security, s -> Amount.amnt(random.nextDouble() * 100));
    }

}
