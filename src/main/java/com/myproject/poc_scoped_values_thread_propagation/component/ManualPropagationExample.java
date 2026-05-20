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

    /* Saida:
        [userId=icaro] [correlationId=e8bf1d51-382d-4df2-b893-7fdd155c87d4] [thread=Thread[#3,main,5,main]] ManualPropagationExamples started
        [userId=icaro] [correlationId=e8bf1d51-382d-4df2-b893-7fdd155c87d4] [thread=Thread[#3,main,5,main]] ManualPropagationExamples started2
        [userId=icaro] [correlationId=e8bf1d51-382d-4df2-b893-7fdd155c87d4] [thread=Thread[#37,ForkJoinPool.commonPool-worker-1,5,InnocuousForkJoinWorkerThreadGroup]] Async propagated task
        [userId=icaro] [correlationId=e8bf1d51-382d-4df2-b893-7fdd155c87d4] [thread=Thread[#3,main,5,main]] ManualPropagationExamples Ended
     */
}