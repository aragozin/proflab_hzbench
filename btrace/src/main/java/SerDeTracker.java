// This is BTrace 1 script, will not run with BTrace2

import static org.openjdk.btrace.core.BTraceUtils.classOf;
import static org.openjdk.btrace.core.BTraceUtils.name;
import static org.openjdk.btrace.core.BTraceUtils.println;

import org.openjdk.btrace.core.annotations.BTrace;
import org.openjdk.btrace.core.annotations.Duration;
import org.openjdk.btrace.core.annotations.Kind;
import org.openjdk.btrace.core.annotations.Location;
import org.openjdk.btrace.core.annotations.OnMethod;
import org.openjdk.btrace.core.annotations.Return;
import org.openjdk.btrace.core.types.AnyType;

@BTrace
public class SerDeTracker {

    @OnMethod(clazz="+com.hazelcast.internal.serialization.impl.StreamSerializerAdapter", method="/write.*/", location=@Location(value=Kind.RETURN))
    public static void leaveWrite(AnyType output, AnyType param, @Duration long time) {
        if (time > 10000) {
            println("writeObject: " + name(classOf(param)) + " - " + time);
        }
    }

    @OnMethod(clazz="+com.hazelcast.internal.serialization.impl.StreamSerializerAdapter", method="/read.*/", location=@Location(value=Kind.RETURN))
    public static void leaveRead(AnyType objectDataOutput, @Return Object param, @Duration long time) {
        if (time > 10000) {
            println(" readObject: " + name(classOf(param)) + " - " + time);
        }
    }
}
