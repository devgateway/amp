/**
 * 
 */
package org.dgfoundation.amp.ar.cell;

import java.util.HashMap;
import java.util.Set;

import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.workers.NewTextColWorker;

/**
 * @author Alex
 *
 */
public class NewTextCell extends TextCell implements IMetaCell {
    private HashMap<String, MetaInfo<? extends Comparable>> metaMap;

    /**
     * 
     */
    public NewTextCell() {
        super();
        metaMap = new HashMap<String, MetaInfo<? extends Comparable>>();
    }

    /**
     * @param id
     */
    public NewTextCell(Long id) {
        super(id);
        metaMap = new HashMap<String, MetaInfo<? extends Comparable>>();
    }
    
    public NewTextCell(TextCell tc) {
        this(tc.ownerId);
        this.value      = tc.value;
        this.id         = tc.id;
    }



    
    @Override
    public Class<? extends NewTextColWorker> getWorker() {
        return NewTextColWorker.class;
    }

    
    @Override
    public MetaInfo<? extends Comparable> retrieveMetaData(String key) {
        return this.metaMap.get(key);
    }

    @Override
    public boolean hasMetaData(String key) {
        return this.metaMap.containsKey(key);
    }



    @Override
    public <T extends Comparable<? super T>> void putMetaData(String key, T value) {
        MetaInfo<T> mi  = new MetaInfo<T>(key, value);
        this.metaMap.put(key, mi);
        
    }
    
    @Override
    protected String getMyClassName() {
        return TextCell.class.getName();
    }
    
    @Override
    public Cell filter(Cell metaCell,Set ids) {
        NewTextCell cell        = (NewTextCell) super.filter(metaCell, ids);
        if ( cell == null )
            return null;
        String columnName       = metaCell.getColumn().getColumnId();
        Object splitterValue    = metaCell.getValue();
        if (splitterValue == null || !(splitterValue instanceof String) || column == null ) {
            return cell;
        }
        columnName  = columnName.trim().toLowerCase();
        MetaInfo<? extends Comparable> mi   = this.retrieveMetaData(columnName);
        /**
         * If mi is null then this cell has no metadata related to the hierarchy by which we a
         * are splitting. In this case we let the cell pass.
         */
        if ( mi == null || splitterValue.equals(mi.getValue()) )
            return cell;
        return null;
    }
    
}
