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

import org.junit.jupiter.api.Test;
import pro.tremblay.core.price.RandomPriceRepository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;

class PriceServerTest {

    @Test
    void test() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8000/price/IBM"))
            .header("Accept", "text/plain")
            .header("Authorization", "Basic password")
            .GET()
            .build();

        RandomPriceRepository repository = mock(RandomPriceRepository.class);
        expect(repository.getPrice("IBM")).andReturn(new Amount(50));
        replay(repository);

        try (PriceServer _ = new PriceServer(repository); HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertThat(response.body()).isEqualTo("50.00");
        }
    }

}
