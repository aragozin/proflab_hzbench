// This is BTrace 1 script, will not run with BTrace2

import static org.openjdk.btrace.core.BTraceUtils.classOf;
import static org.openjdk.btrace.core.BTraceUtils.name;
import static org.openjdk.btrace.core.BTraceUtils.println;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.Map;

import org.openjdk.btrace.core.BTraceUtils;
import org.openjdk.btrace.core.BTraceUtils.Aggregations;
import org.openjdk.btrace.core.BTraceUtils.Collections;
import org.openjdk.btrace.core.BTraceUtils.Reflective;
import org.openjdk.btrace.core.aggregation.Aggregation;
import org.openjdk.btrace.core.aggregation.AggregationFunction;
import org.openjdk.btrace.core.aggregation.AggregationKey;
import org.openjdk.btrace.core.annotations.BTrace;
import org.openjdk.btrace.core.annotations.Duration;
import org.openjdk.btrace.core.annotations.Kind;
import org.openjdk.btrace.core.annotations.Location;
import org.openjdk.btrace.core.annotations.OnMethod;
import org.openjdk.btrace.core.annotations.OnTimer;
import org.openjdk.btrace.core.annotations.Return;
import org.openjdk.btrace.core.types.AnyType;

@BTrace
public class SerDeStats {

    private static Aggregation readHisto = Aggregations.newAggregation(AggregationFunction.AVERAGE);
    private static Aggregation writeHisto = Aggregations.newAggregation(AggregationFunction.AVERAGE);

    @OnMethod(clazz="+com.hazelcast.internal.serialization.impl.StreamSerializerAdapter", method="/write.*/", location=@Location(value=Kind.RETURN))
    public static void leaveWrite(AnyType output, AnyType param, @Duration long time) {
        if (param != null) {
            String name = Reflective.name(classOf(param));
            Aggregations.addToAggregation(writeHisto, Aggregations.newAggregationKey(name), time);
        }
    }

    @OnMethod(clazz="+com.hazelcast.internal.serialization.impl.StreamSerializerAdapter", method="/read.*/", location=@Location(value=Kind.RETURN))
    public static void leaveRead(AnyType input, @Return Object param, @Duration long time) {
        if (param != null) {
            String name = Reflective.name(classOf(param));
            Aggregations.addToAggregation(readHisto, Aggregations.newAggregationKey(name), time);
        }
    }

    @OnTimer(5000)
    public static void onTimer() {
        BTraceUtils.printAggregation("Read", readHisto);
        BTraceUtils.printAggregation("Write", writeHisto);
    }
}
