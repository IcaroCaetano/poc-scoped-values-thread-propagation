package com.myproject.poc_scoped_values_thread_propagation.component;

import com.myproject.poc_scoped_values_thread_propagation.context.ContextLogger;
import com.myproject.poc_scoped_values_thread_propagation.context.RequestContext;
import com.myproject.poc_scoped_values_thread_propagation.context.ScopedRequestContext;

import java.util.concurrent.CompletableFuture;

public class ManualPropagationExample {

    public void execute() {

        ContextLogger.info("ManualPropagationExamples started");

        RequestContext context = ScopedRequestContext.get();

        ContextLogger.info("ManualPropagationExamples started2");

        CompletableFuture.runAsync(() -> {

                    ScopedValue.where(
                            ScopedRequestContext.CONTEXT,
                            context

                    ).run(() -> {
                        ContextLogger.info("Async propagated task");
                    });

                    // Quando o .run() termina o binding contextual é removido automaticamente.
                    //
                    // Isso evita:
                    // - vazamento de contexto
                    // - contaminação entre tasks
                    // - cleanup manual

                })

                // Espera a execução async terminar. Sem o join() o método poderia finalizar antes
                //  da task assíncrona completar.
                .join();

        ContextLogger.info("ManualPropagationExamples Ended");
    }
}