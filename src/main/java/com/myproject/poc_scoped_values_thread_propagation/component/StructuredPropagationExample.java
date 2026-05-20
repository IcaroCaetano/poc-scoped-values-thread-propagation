package com.myproject.poc_scoped_values_thread_propagation.component;

import com.myproject.poc_scoped_values_thread_propagation.context.ContextLogger;

import java.util.concurrent.StructuredTaskScope;

public class StructuredPropagationExample {

    public void execute() throws Exception {

        ContextLogger.info("StructuredPropagationExample: Parent task started");

        try (var scope = StructuredTaskScope.open()) {

            scope.fork(() -> {

                ContextLogger.info("Child task A");

                return null;
            });


            scope.fork(() -> {

                ContextLogger.info("Child task B");

                return null;
            });

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