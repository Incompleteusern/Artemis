/*
 * Copyright © Wynntils 2022.
 * This file is released under AGPLv3. See LICENSE for full license details.
 */
package com.wynntils.core.net;

import com.wynntils.core.components.Managers;
import java.io.InputStream;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class ApiResponse extends NetResult {
    public ApiResponse(HttpRequest request) {
        super(request);
    }

    @Override
    protected CompletableFuture<InputStream> getInputStreamFuture() {
        CompletableFuture<InputStream> future = Managers.Net.HTTP_CLIENT
                .sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                .thenApply(HttpResponse::body);

        return future;
    }
}
