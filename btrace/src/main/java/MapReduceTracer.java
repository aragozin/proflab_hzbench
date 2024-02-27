// This is BTrace 1 script, will not run with BTrace2

import org.openjdk.btrace.core.annotations.*;
import org.openjdk.btrace.core.Profiler;
import static org.openjdk.btrace.core.BTraceUtils.*;

import org.openjdk.btrace.core.BTraceUtils;

@BTrace
public class MapReduceTracer {

    @TLS
    static long getStartNS = 0;

    @Property
    static Profiler profiler = Profiling.newProfiler();

    @OnMethod(
        clazz="com.hazelcast.mapreduce.impl.task.JobSupervisor",
        method="getReducerAddressByKey"
        )
    public static void entryJobS(@ProbeMethodName(fqn=false) String probeMethod) {
        Profiling.recordEntry(profiler, probeMethod);
    }

    @OnMethod(
        clazz="com.hazelcast.mapreduce.impl.task.JobSupervisor",
        method="getReducerAddressByKey",
        location=@Location(value=Kind.RETURN)
    )
    public static void exitJobS(@ProbeMethodName(fqn=false) String probeMethod, @Duration long duration) {
        Profiling.recordExit(profiler, probeMethod, duration);
    }


    @OnMethod(clazz = "/com\\.hazelcast\\.mapreduce\\.impl\\.task\\..*/", method = "/.*/",
            location = @Location(value = Kind.CALL, clazz = "/java\\.util\\.concurrent\\..*ConcurrentMap/", method = "/get/", where=Where.BEFORE))
    public static void enterMapGet(@TargetMethodOrField() String targetMethod) {
        getStartNS = BTraceUtils.timeNanos();
        Profiling.recordEntry(profiler, targetMethod);
    }

    @OnMethod(clazz = "/com\\.hazelcast\\.mapreduce\\.impl\\.task\\..*/", method = "/.*/",
            location = @Location(value = Kind.CALL, clazz = "/java\\.util\\.concurrent\\..*ConcurrentMap/", method = "/get/", where=Where.AFTER))
    public static void leaveMapGet(@TargetMethodOrField() String targetMethod) {
        Profiling.recordExit(profiler, targetMethod, BTraceUtils.timeNanos() - getStartNS);
    }

    @OnMethod(
        clazz="com.hazelcast.mapreduce.impl.task.MapCombineTask",
        method="processPartitionMapping"
        )
    public static void entryCombine(@ProbeMethodName(fqn=false) String probeMethod) {
        Profiling.recordEntry(profiler, probeMethod);
    }

    @OnMethod(
        clazz="com.hazelcast.mapreduce.impl.task.MapCombineTask",
        method="processPartitionMapping",
        location=@Location(value=Kind.RETURN)
    )
    public static void exitCombine(@ProbeMethodName(fqn=false) String probeMethod, @Duration long duration) {
        Profiling.recordExit(profiler, probeMethod, duration);
    }

    @OnTimer(10000)
    public static void timer() {
        Profiling.printSnapshot("Hazelcast tracing", profiler, "%s %s");
    }
}

