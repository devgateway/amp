/**
 * 
 */
package org.dgfoundation.amp.ar.cell;

import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.workers.NewMetaDateColWorker;

import java.util.HashMap;
import java.util.Set;

/**
 * @author Alex
 *
 */
public class NewMetaDateCell extends DateCell implements IMetaCell {

    private HashMap<String, MetaInfo<? extends Comparable>> metaMap;
    
    public NewMetaDateCell() {
        super();
        metaMap     = new HashMap<String, MetaInfo<? extends Comparable>>();
    }

    /**
     * @param id
     * @param name
     */
    public NewMetaDateCell(Long id) {
        super(id);
        metaMap     = new HashMap<String, MetaInfo<? extends Comparable>>();
    }
    
    public NewMetaDateCell(DateCell dc) {
        this(dc.ownerId);
        this.ethiopianDate  = dc.ethiopianDate;
        this.value          = dc.value;
        this.id             = dc.id;
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
    public Cell filter(Cell metaCell,Set ids) {
        NewMetaDateCell cell    = (NewMetaDateCell) super.filter(metaCell, ids);
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
    

    @Override
    protected String getMyClassName() {
        return DateCell.class.getName();
    }
    
    @Override
    public Class getWorker() {
        return NewMetaDateColWorker.class;
    }

}
