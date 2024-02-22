Hazelcast demo application
==========================

This is demo appliction for "Java Profilers" training course.

Demo include 3+1 Hazelcast cluster and perform simple map/reduce operation.

Run demo
--------

 * `mvn -P run test` to start cluster
 * `mvn -P run_n_load test` to start cluster and load script
 * `mvn -P stop test` stop demo process
 * `mvn clean` would also clean demo processes

Performance logs are under `var/loadgen/logs/console.out`