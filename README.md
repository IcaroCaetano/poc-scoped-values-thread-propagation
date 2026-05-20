# poc-scoped-vlues-thread-propagation

The goal of this POC is to study the Scoped Values feature of Java 25, validating whether the context can be propagated across different Threads.

## 1. Feature covered

### 1.1 CompletableFuture.runAsync 
With this feature, it is possible to execute an asynchronous block in another thread without blocking the current thread.

- By default, it uses a global thread pool shared by the JVM `ForkJoinPool.commonPool()`
- Another point is that `.runAsync` executes a `Runnable`, meaning it executes a statement that has no return value.

In summary:
  - creates an asynchronous task
  - sends it to a thread pool
  - immediately returns a `CompletableFuture<Void>`

### 1.2 try (var scope = StructuredTaskScope.open()) {

Create a concurrency scope. With this feature, it's possible to control the lifecycle of an instruction. 
The goal is to manage concurrent tasks in a structured and secure way.

In summary:
- creates a scope of competition
- allows you to start child tasks within it.
- wait for all to finish with `join()`
- It automatically closes the scope when exiting the try block.
