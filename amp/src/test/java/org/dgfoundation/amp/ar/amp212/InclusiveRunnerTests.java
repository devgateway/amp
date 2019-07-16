package org.dgfoundation.amp.ar.amp212;

import java.util.function.LongFunction;

import org.dgfoundation.amp.algo.timing.InclusiveTimer;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.junit.Ignore;
import org.junit.Test;


/**
 * 
 * testcases for the inclusive runner
 * @author Constantin Dolghier
 *
 */
public class InclusiveRunnerTests extends AmpTestCase {
    
    /**
     * function which formats numbers by rounding upto 50ms
     */
    public final static LongFunction<String> TESTCASES_FORMATTER = z -> String.format("%d ms", Math.round(z / 50.0) * 50);
    
    /**
     * busy wait (busy waiting is more stable then waiting, so easier to testcases timing code)
     * @param millies
     */
    public final static void delay(long millies) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < millies);
    }
    
    @Test
    public void testSingle() {
        InclusiveTimer timer = new InclusiveTimer("single bench");
        timer.run("root", () -> {
            delay(150);
        });
        assertEquals("{name: <single bench>, totalTime: 150 ms, subNodes: [{name: <root>, totalTime: 150 ms}]}", timer.getCurrentState().asFastString(TESTCASES_FORMATTER));
    }
    
    @Test
    public void testChain() {
        InclusiveTimer timer = new InclusiveTimer("chain bench");
        timer.run("root", () -> {
            delay(800);
            timer.run("subroot", () -> {
                delay(100);
                timer.run("subsubroot", () -> {
                    delay(800);
                });
            });
        });
        assertEquals("{name: <chain bench>, totalTime: 1700 ms, subNodes: [{name: <root>, totalTime: 1700 ms, subNodes: [{name: <subroot>, totalTime: 900 ms, subNodes: [{name: <subsubroot>, totalTime: 800 ms}]}]}]}", timer.getCurrentState().asFastString(TESTCASES_FORMATTER));
    }
    
    @Test
    public void testBinaryTree() {
        InclusiveTimer timer = new InclusiveTimer("binary bench");
        timer.run("0", () -> delay(200));
        timer.run("1", () -> {
            timer.run("11", () -> delay(100));
            timer.run("12", () -> delay(50));
        });
        timer.run("2", () -> {
            timer.run("21", () -> delay(200));
            timer.run("22", () -> delay(250));
        });
        assertEquals("{name: <binary bench>, totalTime: 800 ms, subNodes: [{name: <0>, totalTime: 200 ms}, {name: <1>, totalTime: 150 ms, subNodes: [{name: <11>, totalTime: 100 ms}, {name: <12>, totalTime: 50 ms}]}, {name: <2>, totalTime: 450 ms, subNodes: [{name: <21>, totalTime: 200 ms}, {name: <22>, totalTime: 250 ms}]}]}", timer.getCurrentState().asFastString(TESTCASES_FORMATTER));
        assertEquals(
"{name: <binary bench>, totalTime: 800 ms, subNodes: [\n" + 
"      {name: <0>, totalTime: 200 ms}, \n" +  
"      {name: <1>, totalTime: 150 ms, subNodes: [\n" + 
"         {name: <11>, totalTime: 100 ms}, \n" + 
"         {name: <12>, totalTime: 50 ms}]}, \n" + 
"      {name: <2>, totalTime: 450 ms, subNodes: [\n" + 
"         {name: <21>, totalTime: 200 ms}, \n" + 
"         {name: <22>, totalTime: 250 ms}]}]}", timer.getCurrentState().asUserString(3, TESTCASES_FORMATTER));
    }
    
    @Test
    @Ignore
    public void testThreads() throws InterruptedException {
        InclusiveTimer timer = new InclusiveTimer("threaded bench");
        Thread thr1 = new Thread(() -> timer.run("thread 1", () -> {
                delay(100);
                timer.run("11", () -> delay(250));
                timer.run("12", () -> delay(50));
            }));
        thr1.start();
        Thread thr2 = new Thread(() -> timer.run("thread 2", () -> delay(150)));
        delay(30); // give thread 1 enough time to start
        thr2.start();
        timer.run("main thread", () -> {
            delay(50);
            timer.run("m1", () -> delay(50));
            timer.run("m2", () -> delay(100));
        });
        thr1.join();
        thr2.join();
        assertEquals("{name: <threaded bench>, totalTime: 750 ms, subNodes: [{name: <thread 1>, totalTime: 400 ms, subNodes: [{name: <11>, totalTime: 250 ms}, {name: <12>, totalTime: 50 ms}]}, {name: <thread 2>, totalTime: 150 ms}, {name: <main thread>, totalTime: 200 ms, subNodes: [{name: <m1>, totalTime: 50 ms}, {name: <m2>, totalTime: 100 ms}]}]}", timer.getCurrentState().asFastString(TESTCASES_FORMATTER));
    }
    
    @Test
    public void testCrashedTask() {
        InclusiveTimer timer = new InclusiveTimer("test crash");
        shouldFail(() -> 
            timer.run("root", () -> {
                delay(150);
                timer.run("subtask", () -> {
                    delay(50);
                    throw new RuntimeException("simulating a crash");
                });
            }));
        assertEquals("{name: <test crash>, totalTime: 200 ms, subNodes: [{name: <root>, totalTime: 200 ms, subNodes: [{name: <subtask>, totalTime: 50 ms}]}]}", 
            timer.getCurrentState().asFastString(TESTCASES_FORMATTER));
    }
    
    @Test
    public void testMetaToString() {
        InclusiveTimer timer = new InclusiveTimer("meta test");
        timer.run("meta setter", () -> {
            timer.getCurrentNode().putMeta("meta1", "value1");
            timer.getCurrentNode().putMeta("meta2", "value2");
        });
        assertEquals("{name: <meta test>, totalTime: 0 ms, subNodes: [{name: <meta setter>, totalTime: 0 ms, meta1=value1, meta2=value2}]}", timer.getCurrentState().asFastString(TESTCASES_FORMATTER));
    }
    
    @Test
    public void testMetaInclusive() {
        InclusiveTimer timer = new InclusiveTimer("meta included");
        timer.run("meta meta", () -> {
            timer.putMetaInNode("some", "someValue");
            timer.run("undermeta", () -> timer.putMetaInNode("other", "otherValue"));
        });
        assertEquals("{name: <meta included>, totalTime: 0 ms, subNodes: [{name: <meta meta>, totalTime: 0 ms, some=someValue, subNodes: [{name: <undermeta>, totalTime: 0 ms, other=otherValue}]}]}", timer.getCurrentState().asFastString(TESTCASES_FORMATTER));
    }
    
    @Test
    public void testMetaAddingUnsupported() {
        InclusiveTimer timer = new InclusiveTimer("random name");
        shouldFail(() -> timer.getCurrentNode());
        timer.run("something", () -> {
            shouldFail(() -> timer.putMetaInNode("name", "some name"));
            shouldFail(() -> timer.putMetaInNode("totalTime", "some name"));
            timer.putMetaInNode("aha", "ahem");
        });
        assertEquals("{name: <random name>, totalTime: 0 ms, subNodes: [{name: <something>, totalTime: 0 ms, aha=ahem}]}", timer.getCurrentState().asFastString(TESTCASES_FORMATTER));
    }
}
