/*
 * Copyright 2022-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pro.tremblay.core.transaction;

import net.jcip.annotations.NotThreadSafe;
import org.jspecify.annotations.Nullable;
import pro.tremblay.core.Amount;
import pro.tremblay.core.position.Position;
import pro.tremblay.core.Quantity;
import pro.tremblay.core.security.Security;

import java.time.LocalDate;

/**
 * A transaction that happened on a position.
 */
@NotThreadSafe
public sealed interface Transaction permits Buy, Sell, Deposit, Withdrawal {

    static Transaction buy(LocalDate date, Amount cash, Security security, Quantity quantity) {
        return new Buy(date, cash, security, quantity);
    }

    static Transaction sell(LocalDate date, Amount cash, Security security, Quantity quantity) {
        return new Sell(date, cash, security, quantity);
    }

    static Transaction deposit(LocalDate date, Amount cash) {
        return new Deposit(date, cash);
    }

    static Transaction withdrawal(LocalDate date, Amount cash) {
        return new Withdrawal(date, cash);
    }

    /** Date at which the transaction occurred */
    LocalDate date();
    /** Amount of cash exchanged during the transaction. The amount is always positive, the side of the transaction is determined by its type */
    Amount cash();
    /** Securities bought or sold if securities are involved in the transaction */
    default @Nullable Security security() {
        return null;
    }
    /** Quantity of securities exchanged during the transaction. The quantity is always positive, the side of the transaction is determined by its type */
    default @Nullable Quantity quantity() {
        return null;
    }

    default TransactionType type() {
        return switch(this) {
            case Buy _ -> TransactionType.SELL;
            case Sell _ -> TransactionType.BUY;
            case Deposit _ -> TransactionType.DEPOSIT;
            case Withdrawal _ -> TransactionType.WITHDRAWAL;
        };
    }

    default boolean hasQuantity() {
        return switch(this) {
            case Buy _, Sell _ -> true;
            case Deposit _, Withdrawal _ -> false;
        };
    }

    default void revert(Position position) {
        switch(this) {
            case Buy(var _, var cash, var security, var quantity) -> {
                position.addCash(cash);
                position.addSecurityPosition(security, quantity.negate());
            }
            case Sell(var _, var cash, var security, var quantity) -> {
                position.addCash(cash.negate());
                position.addSecurityPosition(security, quantity);
            }
            case Deposit(var _, var cash) -> position.addCash(cash.negate());
            case Withdrawal(var _, var cash) -> position.addCash(cash);
        }
    }
}

