package com.myproject.poc_scoped_values_thread_propagation.component;

import com.myproject.poc_scoped_values_thread_propagation.context.ContextLogger;

public class ThreadSwitchingExperiment {

    public void execute() throws Exception{

        ContextLogger.info("ThreadSwitchingExperiment: Before thread switch");

        // Cria e inicia uma NOVA Virtual Thread. Mas nao cria uma child task estruturada.
        //
        // Apenas cria uma nova thread independente.
        Thread virtualThread = Thread.startVirtualThread(() -> {

            ContextLogger.info("Inside new virtual thread");

        });

        virtualThread.join();

        ContextLogger.info("ThreadSwitchingExperiment: After thread switch");
    }
}