package pro.tremblay.core;

import javax.annotation.Nonnull;

public interface PriceService {

    /**
     * Returns the current price for a security.
     *
     * @param security security for which we want a price
     * @throws IllegalArgumentException if no price is found at this date
     * @return the price of the security
     */
    @Nonnull
    Amount getPrice(@Nonnull Security security);

}
