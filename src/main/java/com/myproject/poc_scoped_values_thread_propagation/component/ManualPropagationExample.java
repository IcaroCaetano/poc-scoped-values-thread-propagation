package com.myproject.poc_scoped_values_thread_propagation.component;

import com.myproject.poc_scoped_values_thread_propagation.context.ContextLogger;
import com.myproject.poc_scoped_values_thread_propagation.context.RequestContext;
import com.myproject.poc_scoped_values_thread_propagation.context.ScopedRequestContext;

import java.util.concurrent.CompletableFuture;

public class ManualPropagationExample {

    public void execute() {

        ContextLogger.info("ManualPropagationExamples started");

        // Obtém o contexto atual que está bindado no ScopedValue da execução atual.
        // Aqui estamos "capturando" o contexto da thread/escopo atual para reutilizar depois.
        //
        //
        // Sem isso, a task async perderia o contexto.
        RequestContext context = ScopedRequestContext.get();

        ContextLogger.info("ManualPropagationExamples started2");

        // Cria uma tarefa assíncrona usando CompletableFuture. Isso cria um NOVO async boundary.
        //
        // Normalmente o ScopedValue NÃO é propagado automaticamente para cá.
        //
        // O código abaixo será executado em outra thread, geralmente do ForkJoinPool.commonPool.
        CompletableFuture.runAsync(() -> {

                    ScopedValue.where(
                            ScopedRequestContext.CONTEXT,
                            // temporariamente ao ScopedValue
                            context

                    ).run(() -> {

                        // Executa o bloco dentro do novo binding contextual.
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

        // Executa novamente no escopo original.
        //
        // O contexto ainda existe aqui porque:
        // - continuamos dentro do execution scope pai
        // - o ScopedValue continua bindado
        ContextLogger.info("ManualPropagationExamples Ended");
    }
}