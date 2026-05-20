package com.myproject.poc_scoped_values_thread_propagation.component;

import com.myproject.poc_scoped_values_thread_propagation.context.ContextLogger;

import java.util.concurrent.CompletableFuture;

public class BrokenPropagationExample {

    public void execute() {

        ContextLogger.info("BrokenPropagationExample - Parent task started");

        CompletableFuture.runAsync(() -> {

            // provavelmente falhará
            ContextLogger.info("Async Broken task");

        }).join();

        ContextLogger.info("BrokenPropagationExample - Parent task ended");
    }
}