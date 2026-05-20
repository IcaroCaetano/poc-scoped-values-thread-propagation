package com.myproject.poc_scoped_values_thread_propagation.context;

public final class ScopedRequestContext {

    private ScopedRequestContext() {}

    public static final ScopedValue<RequestContext> CONTEXT = ScopedValue.newInstance();

    public static RequestContext get() {

        return CONTEXT.get();
    }
}