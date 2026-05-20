package com.myproject.poc_scoped_values_thread_propagation;

import com.myproject.poc_scoped_values_thread_propagation.component.ManualPropagationExample;
import com.myproject.poc_scoped_values_thread_propagation.component.StructuredPropagationExample;
import com.myproject.poc_scoped_values_thread_propagation.component.ThreadSwitchingExperiment;
import com.myproject.poc_scoped_values_thread_propagation.context.RequestContext;
import com.myproject.poc_scoped_values_thread_propagation.context.ScopedRequestContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class PocScopedValuesThreadPropagationApplication {

	public static void main(String[] args) {
		var context = new RequestContext(
				"icaro",
				UUID.randomUUID().toString()
		);

		ScopedValue.where(
				ScopedRequestContext.CONTEXT,
				context
		).run(() -> {

			try {

				new StructuredPropagationExample().execute();

				new ManualPropagationExample().execute();

				// new ThreadSwitchingExperiment().execute();

				//new BrokenPropagationExample().execute();

			} catch (Exception e) {

				e.printStackTrace();
			}
		});

        /*
            [userId=icaro] [correlationId=f7eb5a38-e415-4fcf-9cf9-c87dce9a9d32] [thread=Thread[#3,main,5,main]] StructuredPropagationExample: Parent task started
			[userId=icaro] [correlationId=f7eb5a38-e415-4fcf-9cf9-c87dce9a9d32] [thread=VirtualThread[#30]/runnable@ForkJoinPool-1-worker-1] Child task A
			[userId=icaro] [correlationId=f7eb5a38-e415-4fcf-9cf9-c87dce9a9d32] [thread=VirtualThread[#32]/runnable@ForkJoinPool-1-worker-4] Child task B
			[userId=icaro] [correlationId=f7eb5a38-e415-4fcf-9cf9-c87dce9a9d32] [thread=Thread[#3,main,5,main]] StructuredPropagationExample: Parent task completed
			[userId=icaro] [correlationId=f7eb5a38-e415-4fcf-9cf9-c87dce9a9d32] [thread=Thread[#3,main,5,main]] ManualPropagationExamples started
			[userId=icaro] [correlationId=f7eb5a38-e415-4fcf-9cf9-c87dce9a9d32] [thread=Thread[#3,main,5,main]] ManualPropagationExamples started2
			[userId=icaro] [correlationId=f7eb5a38-e415-4fcf-9cf9-c87dce9a9d32] [thread=Thread[#36,ForkJoinPool.commonPool-worker-1,5,InnocuousForkJoinWorkerThreadGroup]] Async propagated task
			[userId=icaro] [correlationId=f7eb5a38-e415-4fcf-9cf9-c87dce9a9d32] [thread=Thread[#3,main,5,main]] ManualPropagationExamples Ended
         */
	}

}
