package com.myproject.poc_scoped_values_thread_propagation.component;

import com.myproject.poc_scoped_values_thread_propagation.context.ContextLogger;

import java.util.concurrent.StructuredTaskScope;

public class StructuredPropagationExample {

    public void execute() throws Exception {

        ContextLogger.info("StructuredPropagationExample: Parent task started");

        // Isso representa um escopo estrutura de concorrência. O execution context atual
        // (incluindo ScopedValues) será herdado pelas subtasks.
        //
        // O try-with-resources garante:
        // - fechamento automático
        // - cleanup do escopo
        // - finalização estruturada
        try (var scope = StructuredTaskScope.open()) {

            // Cria uma subtask concorrente.
            // - cria uma task filha
            // - normalmente em uma Virtual Thread
            // - associada ao MESMO execution scope
            scope.fork(() -> {

                ContextLogger.info("Child task A");

                return null;
            });

            // Cria outra subtask concorrente.
            //
            // Esta task pode executar:
            // - em paralelo
            // - em outra Virtual Thread
            // - em outro carrier thread
            //
            // Mesmo assim o contexto continuará disponível.
            scope.fork(() -> {

                ContextLogger.info("Child task B");

                return null;
            });

            // Aguarda TODAS as subtasks terminarem.
            // Sem isso o metodo poderia continuar antes das child tasks completarem.
            //
            // join() sincroniza
            scope.join();
        }

        ContextLogger.info("StructuredPropagationExample: Parent task completed");
    }


    /*
        Saida:
        [userId=icaro] [correlationId=c8bc8198-47f0-46c0-8fc0-c081eeae3b1a] [thread=Thread[#3,main,5,main]] StructuredPropagationExample: Parent task started
        [userId=icaro] [correlationId=c8bc8198-47f0-46c0-8fc0-c081eeae3b1a] [thread=VirtualThread[#32]/runnable@ForkJoinPool-1-worker-1] Child task A
        [userId=icaro] [correlationId=c8bc8198-47f0-46c0-8fc0-c081eeae3b1a] [thread=VirtualThread[#34]/runnable@ForkJoinPool-1-worker-3] Child task B
        [userId=icaro] [correlationId=c8bc8198-47f0-46c0-8fc0-c081eeae3b1a] [thread=Thread[#3,main,5,main]] StructuredPropagationExample: Parent task completed
     */
}