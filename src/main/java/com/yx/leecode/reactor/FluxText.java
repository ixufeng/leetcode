package com.yx.leecode.reactor;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author xufeng
 * Create Date: 2020-04-12 14:49
 **/
public class FluxText {

    private static void generate() {
        Flux.generate(sink -> {
            sink.next("test");
            System.out.println(Thread.currentThread().getName());
            sink.complete();
        }).subscribe(System.out::print);

        Flux.generate(LongAdder::new, (adder, sink) -> {
            adder.increment();
            sink.next("next");
            if (adder.intValue() > 10) {
                sink.complete();
            }
            return adder;

        }).subscribe(System.out::print);
    }

    public static void main(String[] args) throws Exception {
        generate();

//        Thread.currentThread().join();

    }

    public static void test1() {
        Flux.just(1, 2, 3, 4).subscribe(System.out::println);
        Flux.empty().subscribe(System.out::println);
        Flux.range(1, 20).subscribe((t) -> {
            System.out.println(Thread.currentThread().getName());
        });
        System.out.println();
        Flux.interval(Duration.ofSeconds(1)).subscribe((t) -> {
            System.out.println(Thread.currentThread().getName());
        });
    }
}
