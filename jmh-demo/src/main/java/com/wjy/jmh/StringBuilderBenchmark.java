package com.wjy.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

@BenchmarkMode(Mode.Throughput)
@Fork(2)
@Threads(4)
@Warmup(iterations = 3)
@Measurement(iterations = 10, time = 5, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
/***
 * 安装插件https://github.com/artyushov/idea-jmh-plugin基于注解，类似junit一样运行
 */
public class StringBuilderBenchmark {

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(StringBuilderBenchmark.class.getSimpleName())
                .mode(Mode.Throughput)
                .forks(2)
                .threads(4)
                .warmupIterations(3)
                .measurementIterations(10)
                .measurementTime(TimeValue.seconds(5))
                .timeUnit(TimeUnit.MILLISECONDS)
                .build();
        new Runner(options).run();
    }

    @Setup
    public void before() {
    }

    @TearDown
    public void after() {
    }

    @Benchmark
    public void testStringAdd() {
        String s = "";
        for (int i = 0; i < 10; i++) {
            s += i;
        }
        print(s);
    }

    @Benchmark
    public void testStringBuilderAdd() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(i);
        }
        print(sb.toString());
    }

    private void print(String s) {
    }

}
