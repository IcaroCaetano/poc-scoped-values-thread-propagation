package com.myproject.poc_scoped_values_thread_propagation.context;

public final class ContextLogger {

    private ContextLogger() {
    }

    public static void info(String message) {

        var context = ScopedRequestContext.get();

        System.out.printf(
                "[userId=%s] [correlationId=%s] [thread=%s] %s%n",
                context.userId(),
                context.correlationId(),
                Thread.currentThread(),
                message
        );
    }
}