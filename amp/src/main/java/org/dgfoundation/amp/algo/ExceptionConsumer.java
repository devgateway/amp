package org.dgfoundation.amp.algo;

import java.util.function.Consumer;

/**
 * a consumer which is allowed to throw exceptions
 * @author DolghierConstantin
 *
 * @param <T>
 */
public interface ExceptionConsumer<T> extends Consumer<T> {
    public void consume(T t) throws Exception;
        
    @Override
    public default void accept(T t) {
        try {consume(t);}
        catch(Exception e) {throw AlgoUtils.translateException(e);}
    }
    
    public static<T> ExceptionConsumer<T> of(ExceptionConsumer<T> c) {
        return c;
    }
}
