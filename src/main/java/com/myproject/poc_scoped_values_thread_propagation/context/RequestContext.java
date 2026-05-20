package com.myproject.poc_scoped_values_thread_propagation.context;

public record RequestContext(
        String userId,
        String correlationId
) {}