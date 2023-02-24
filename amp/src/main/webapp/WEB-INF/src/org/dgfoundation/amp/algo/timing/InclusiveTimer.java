package org.dgfoundation.amp.algo.timing;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Stack;

import org.dgfoundation.amp.algo.ExceptionRunnable;
import org.dgfoundation.amp.algo.VivificatingMap;

/**
 * reentrant, multiple-threads-safe timer which can track how much a task and its subtasks took by modelling the runtime through a tree
 * This class is thread-safe
 * @author Dolghier Constantin
 *
 */
public class InclusiveTimer {
    
    protected final String name;
    protected final long startTime;

    /**
     * a task (e.g. fetching/calculating lots of data items) can have its subtasks run on different threads. The tasks run in these threads are logically subtasks of the parent task
     */
    protected final VivificatingMap<Thread, Stack<RunTree>> treeLeaves = new VivificatingMap<>(new IdentityHashMap<Thread, Stack<RunTree>>(), () -> new Stack<RunTree>());
    protected final List<RunTree> retiredRoots = new ArrayList<>(); 
    
    public InclusiveTimer(String name) {
        this.name = name;
        this.startTime = System.currentTimeMillis();
    }
    
    /**
     * runs a task oblivious to the multithreaded capabilities of the timer. It is a simple forwarder to {@link #run(String, TimedTask, RunNode)} which uses the task which lastly run in this runner/thread as a parent <br />
     * This approximation is good enough even for multithreaded code, as long as threads are not reused for unrelated tasks
     * @param name
     * @param runnable
     */
    public synchronized void run(String name, ExceptionRunnable<?> runnable) {
        final RunTree parentTask = getCurrentStack().isEmpty() ? null : getCurrentStack().peek();
        run(name, selfTask -> runnable.run(), parentTask);
    }
    
    /**
     * runs a task and lets it know the runtime id of the code, so that the code can know its id and pass it on down the call stack
     * @param name
     * @param runnable
     * @param parentTask
     */
    public synchronized void run(String name, TimedTask<?> runnable, RunNode parentTask) {
        long start = System.currentTimeMillis();
        final RunTree runTree = new RunTree(name, (RunTree) parentTask);
        getCurrentStack().push(runTree);
        try {
            runnable.run(runTree);
        }
        catch(Exception e) {
            if (e instanceof RuntimeException)
                throw (RuntimeException) e;
            throw new RuntimeException(e);
        }
        finally {
            long end = System.currentTimeMillis();
            runTree.addTime(end - start);
            getCurrentStack().pop();
            if (getCurrentStack().isEmpty())
                retiredRoots.add(runTree);
        }
    }
    
    public synchronized RunNode getCurrentState() {
//      if (treeLeaves.size() == 1) {
//          Stack<RunTree> res = treeLeaves.values().iterator().next();
//          if (!res.isEmpty())
//              return res.get(0);
//      }
        RunTree res = new RunTree(this.name, null);
        for(RunTree rt:retiredRoots) {
            res.addSubtree(rt);
            res.addTime(rt.getTotalTime());
        }
        for(Stack<RunTree> stack:treeLeaves.values())
            if (!stack.isEmpty()) {
                RunTree z = stack.get(0);
                res.addSubtree(z);
                res.addTime(z.getTotalTime());
            }
        return res;
    }
    
    public long getWallclockTime() {
        return System.currentTimeMillis() - startTime;
    }
    
    protected Stack<RunTree> getCurrentStack() {
        return treeLeaves.getOrCreate(Thread.currentThread());
    }
    
    /**
     * will throw an exception if currently not running anything
     * @return
     */
    public RunNode getCurrentNode() {
        Stack<RunTree> stack = getCurrentStack();
        if (stack == null || stack.isEmpty())
            throw new RuntimeException("not inside an InclusiveTimer benchmark!");
        return stack.peek();
    }
    
    /**
     * syntactic sugar for {@link #getCurrentNode()}.{@link RunNode#putMeta(String, Object)}. Please see these function for documentation
     * @param key
     * @param value
     */
    public void putMetaInNode(String key, Object value) {
        getCurrentNode().putMeta(key, value);
    }
}
