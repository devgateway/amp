/**
 * 
 */
package org.dgfoundation.amp.ar.cell;

import org.dgfoundation.amp.ar.MetaInfo;

/**
 * @author Alex
 *
 */
public interface IMetaCell {
    
    public MetaInfo<? extends Comparable> retrieveMetaData(String key) ;
    public <T extends Comparable<? super T>> void putMetaData (String key, T value) ;
    
    public boolean hasMetaData(String key) ;

}
