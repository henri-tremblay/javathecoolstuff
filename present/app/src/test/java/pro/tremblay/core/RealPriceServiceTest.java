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
package pro.tremblay.core;

//import com.sun.net.httpserver.HttpServer;
//import com.sun.net.httpserver.SimpleFileServer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

//import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
//import static pro.tremblay.core.Assertions.assertThat;

class RealPriceServiceTest {

//    private static HttpServer server;
//    private final PriceService priceService = new RealPriceService("http://localhost:8000");

    @BeforeAll
    static void before(@TempDir Path dir) throws Exception {
//        Files.writeString(dir.resolve(SecuritiesForTest.GOOGL.symbol()), "12.34", StandardOpenOption.CREATE_NEW);
//        server = SimpleFileServer.createFileServer(
//            new InetSocketAddress(8000),
//            dir,
//            SimpleFileServer.OutputLevel.INFO);
//        server.start();
    }

    @AfterAll
    static void after() {
//        server.stop(0);
    }

    @Test
    void getPrice() {
//        Amount price = priceService.getPrice(SecuritiesForTest.GOOGL);
//        assertThat(price).isEqualTo(Amount.amnt("12.34"));
    }

    @Test
    void getPrice_noPrice() {
//        assertThatIllegalArgumentException()
//            .isThrownBy(() -> priceService.getPrice(SecuritiesForTest.IBM))
//            .withMessage("No price found for IBM");
    }
}
