/**
 * GroupColumn.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ComputedAmountCell;
import org.dgfoundation.amp.exprlogic.MathExpressionRepository;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpReportMeasures;
import org.digijava.module.aim.dbentity.AmpReports;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org Column that is built
 *         for grouping other ColumnS. This Column can hold only items of type
 *         Column and is basically a list of ColumnS.
 * @since Jun 22, 2006
 * 
 */
public class GroupColumn extends Column {

	/**
	 * Returns the max of the underlying visible rows of the subcolumns
	 */
    	@Override
	public int getVisibleRows() {
    	 Iterator i=items.iterator();
 	    int ret=0;
 	    while (i.hasNext()) {
			Column element = (Column) i.next();
			int visCol=element.getVisibleRows();
			if(visCol>ret) ret=visCol;
	    }
 	    return ret;
	}
    	
	public int getWidth() {
		int ret=0;
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			ret+=element.getWidth();
		}
		return ret;
	}
	
	public static Column verticalSplitByCategs(CellColumn src,
            List categories, boolean generateTotalCols,AmpReports reportMetadata) {
		
		
		return verticalSplitByCategs(src,categories,null,generateTotalCols, reportMetadata);
	}
	
	/**
	 * Split a column holding CategAmountCellS into several subcolumns based on the categorized amount data and some
	 * categories given as reference. The result will be a categorized column tree of GroupColumnS and CellColumnS as leafs. 
	 * Each category will create another level on the tree while on the same level we will find several GroupColumnS that 
	 * share the same metainfo category but not the same value.
	 * @param src The source CellColumn to be categorized
	 * @param categories the list of categories to be applied to the src column
	 * @param generateTotalCols true when creating TotalAmountColumnS instead of CellColumnS 
	 * @return a GroupColumn that holds the categorized data
	 * @see MetaInfo, TotalAmountColumn, CategAmountCell
	 */
    private static Column verticalSplitByCategs(Column src,
            List categories, Set ids,boolean generateTotalCols,AmpReports reportMetadata) {
        String cat = (String) categories.remove(0);
        if (categories.size() > 0)
            return verticalSplitByCategs(verticalSplitByCateg(src, cat,ids,
                    false,reportMetadata), categories, ids, generateTotalCols,reportMetadata);
        else
            return verticalSplitByCateg(src, cat, ids, generateTotalCols, reportMetadata);
    }

    /**
     * Helper method that only uses one category to create a categorized tree. This method is internally used and should not
     * be invoked by the developer directly.
     * @param src The source column to be categorized
     * @param category the category to categorize the data with
     * @param generateTotalCols true when creating TotalAmountColumnS instead of CellColumnS
     * @return a GroupColumn that holds the categorized Data
     * @see verticalSplitByCategs
     */
    private static Column verticalSplitByCateg(Column src, 
            String category,Set ids, boolean generateTotalCols,AmpReports reportMetadata) {    
    	if(src instanceof CellColumn) 
    		return verticalSplitByCateg((CellColumn)src,category,ids,generateTotalCols,reportMetadata);
    	else {
    		GroupColumn srcG=(GroupColumn) src;
    		GroupColumn dest=null;
   			dest=new GroupColumn(src);
    		Iterator i=srcG.iterator();
    		while (i.hasNext()) {
				Column element = (Column) i.next();
				
				if(category.equals(ArConstants.TERMS_OF_ASSISTANCE) && element.getExpression().equalsIgnoreCase(MathExpressionRepository.TOTAL_COMMITMENTS) ){ 
					continue;
				}
				
				Column splitted=verticalSplitByCateg(element,category,ids,generateTotalCols,reportMetadata);
				
				if(splitted!=null) {
					dest.addColumn(splitted);
					splitted.setContentCategory(category);
				}
				else dest.addColumn(element);
			}
    		return dest;
    	}
    }
    
    /**
     * Helper method that only uses one category to create a categorized tree. This method is internally used and should not
     * be invoked by the developer directly.
     * @param src the CellColumn source
     * @param category category the category to categorize the data with 
     * @param generateTotalCols true when creating TotalAmountColumnS instead of CellColumnS
     * @return a GroupColumn that holds the categorized Data
     */
    private static Column verticalSplitByCateg(CellColumn src,
    	String category, Set ids, boolean generateTotalCols,AmpReports reportMetadata) {
    	
    	HashMap<String,String> yearMapping=new HashMap<String, String>();
    	HashMap<String,String> monthMapping=new HashMap<String, String>();
    	
    	Column ret = new GroupColumn(src);
        Set<MetaInfo> metaSet = new TreeSet<MetaInfo>();
        Iterator i = src.iterator();
       
        HashMap<String,String> computedCategories=new HashMap<String,String>();
        
        AmpARFilter myFilters	= null;
        try{
        	myFilters	= src.getWorker().getGenerator().getFilter();
        }
        catch (NullPointerException e) {
			logger.warn("Could not get filter object when type is: " + category);
		}
       if ( (reportMetadata.getAllowEmptyFundingColumns()!=null && reportMetadata.getAllowEmptyFundingColumns()) && 
    		  ( category.equals(ArConstants.YEAR) || category.equals(ArConstants.QUARTER) 
    		   	|| category.equals(ArConstants.MONTH) ) ) {
    	  ARUtil.insertEmptyColumns(category, src, metaSet, myFilters);
       } 
        
        
        while (i.hasNext()) {
            Categorizable element = (Categorizable) i.next();
            if(!element.isShow()) continue;
            MetaInfo minfo=MetaInfo.getMetaInfo(element.getMetaData(),category);
            if(minfo==null || minfo.getValue()==null) return null;
            	//if the year is not renderizable just not add it to minfo
           
            if (element.isRenderizable()) {
        	    metaSet.add(minfo);
        	    
        	    if (category.equalsIgnoreCase(ArConstants.YEAR)){
                	MetaInfo minfo2=MetaInfo.getMetaInfo(element.getMetaData(),ArConstants.FISCAL_Y);
                	yearMapping.put(minfo.getValue().toString(),minfo2.getValue().toString());
                	
               }
        	    if (category.equalsIgnoreCase(ArConstants.MONTH)){
                	MetaInfo minfo2=MetaInfo.getMetaInfo(element.getMetaData(),ArConstants.FISCAL_M);
                	monthMapping.put(minfo.getValue().toString(),minfo2.getValue().toString());
                	
               }
            }
        }
      
    	if(category.equals(ArConstants.TERMS_OF_ASSISTANCE) 
    			&& ARUtil.containsMeasure(ArConstants.UNDISBURSED_BALANCE,reportMetadata.getMeasures())) {
    	
    		if (metaSet.size()==0)
    			metaSet.add(new MetaInfo<String>(ArConstants.TERMS_OF_ASSISTANCE,"Grant"));
    		}
       
       
       // split by selected measures
       if(category.equals(ArConstants.FUNDING_TYPE)) {
    	   metaSet.clear();
    	   Set<AmpReportMeasures> measures=reportMetadata.getMeasures();
    	   Iterator<AmpReportMeasures> ii = measures.iterator();
    	   while (ii.hasNext()) {
	    		AmpReportMeasures ampReportMeasurement = ii.next();
				AmpMeasures element = ampReportMeasurement.getMeasure();
					boolean splitMeasure=(element.isAllowSplitbyCateg()!=null && element.isAllowSplitbyCateg());
					if (splitMeasure) {
						MetaInfo<FundingTypeSortedString> metaInfo = new MetaInfo<FundingTypeSortedString>(ArConstants.FUNDING_TYPE, new FundingTypeSortedString(element.getMeasureName(), reportMetadata.getMeasureOrder(element.getMeasureName())));
						if (element.getExpression()!=null){
							computedCategories.put(element.getMeasureName(), element.getExpression());
						}
						metaSet.add(metaInfo);
					}	
				}
			}
    	  
    	  
        
        

        // iterate the set and create a subColumn for each of the metainfo
        i = metaSet.iterator();
        while (i.hasNext()) {
            boolean isComputed=false;
        	MetaInfo element = (MetaInfo) i.next();
            CellColumn cc = null;
            if (generateTotalCols){
            	if (computedCategories.containsKey(element.getValue().toString())){
            		isComputed=true;
            		cc=new TotalComputedMeasureColumn(element.getValue().toString());	
            		cc.setExpression(computedCategories.get(element.getValue().toString().toString()));
            	}else{
            		cc = new TotalAmountColumn(element.getValue().toString(),true);
                  	}
            	}
            else{
            	if(category.equalsIgnoreCase(ArConstants.YEAR)){
            		cc = new AmountCellColumn( yearMapping.get(element.getValue().toString()));
            	}else if(category.equalsIgnoreCase(ArConstants.MONTH)){
                		cc = new AmountCellColumn( monthMapping.get(element.getValue().toString()));
                	}
            	else{
            	cc = new AmountCellColumn( element.getValue().toString());
            	}
            }
            ret.getItems().add(cc);
            cc.setParent(ret);
            
            cc.setContentCategory(category);
            //iterate the src column and add the items with same MetaInfo
            Iterator ii=src.iterator();
            while (ii.hasNext()) {
    			Categorizable item = (Categorizable) ii.next();
    			if (isComputed){
    				ComputedAmountCell c1=new ComputedAmountCell();
    				c1.setValuesFromCell((CategAmountCell) item);
    				cc.addCell(c1);
    				
    			}else{
    				//MetaInfo costValues=new MetaInfo(ArConstants.PROPOSED_COST,"true");
    				if(item.hasMetaInfo(element))//|| item.hasMetaInfo(costValues)) 
    					cc.addCell(item);
    			
    			}
    		} 
        }
        
         
        if(ret.getItems().size()==0) {
        	AmountCellColumn acc=new AmountCellColumn(ret);
        	Iterator ii=src.iterator();
        	while (ii.hasNext()) {
				AmountCell element = (AmountCell) ii.next();
				acc.addCell(element);
			}
        	
        	//fixed problem when there is only  TOTAL_COMMITMENTS  or UNDISBURSED_BALANCE selected
        	// ret=acc;
        	 ret.getItems().add(acc);
        	 acc.setParent(ret);
        }
        
        return ret;
        
       
    }

    /**
     * Constructs a GroupColumn from another Column. The resulting GroupColumn will inherit the parent and the name of its 
     * predecessor.
     * @param src the predecessor Column
     */
    public GroupColumn(Column src) {
       	super(src.getParent(),src.getName());
        this.setContentCategory(src.getContentCategory());
    }
    
    /**
     * Constructs a GroupColumn using a given name
     * @param name the name of the new GroupColumn
     */
    public GroupColumn(String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }

    public GroupColumn() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    /**
     * Adds a Column to this GroupColumn. The Parent property of the added Column will be set to this GroupColumn.
     * If the column already exists then the items are appended to the column end so we'll end up with one 
     * unique column per group
     * @param c
     */
    public void addColumn(Column c) {
        if(!items.contains(c)) {items.add(c);        
        c.setParent(this);
        } else {
            Column older=(Column) items.get(items.indexOf(c));
            older.getItems().addAll(c.getItems());
        }
    }
    
    public void addColumn(Integer idx,Column c){
    	if(items.size()<idx.intValue()|| idx.intValue()==0) addColumn(c);else {
    	if(idx.intValue()+1>items.size()) items.add(idx.intValue()-1,c);else
    			items.add(idx.intValue(),c);
    	c.setParent(this);
    	}
    }

    /**
     * Returns a specific column based on its ColumnId. The ColumnId is an arbitrary object that uniquely identifies this
     * column.  
     * @param columnId the columnId to be used to track the Column
     * @return the requested Column or null if not found
     * @see Column
     */
    public Column getColumn(Object columnId) {
        Iterator i = items.iterator();
        while (i.hasNext()) {
            Column element = (Column) i.next();
            if (element.getColumnId().equals(columnId))
                return element;
        }
        return null;
    }

    /**
     * Returns the Column occupying the specific position in the internal list of this GroupColumn 
     * @param idx the index of the requested column
     * @return the requested column
     */
    public Column getColumn(int idx) {
        return (Column) getItem(idx);
    }

    /**
     * Replaces the column identified by columnId with the new specified Column
     * @param columnId the identifier for the column to be replaced
     * @param newColumn the new column
     */
    public void replaceColumn(Object columnId, Column newColumn) {
        int idx = items.indexOf(getColumn(columnId));
        items.remove(idx);
        items.add(idx, newColumn);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.Column#filterCopy(org.dgfoundation.amp.ar.cell.Cell)
     */
    public Column filterCopy(Cell filter, Set ids) {
        GroupColumn dest = new GroupColumn(this.getName());
        dest.setContentCategory(this.getContentCategory());
        Iterator i = items.iterator();
        while (i.hasNext()) {
            Column element = (Column) i.next();
            dest.addColumn(element.filterCopy(filter, ids));
        }
        return dest;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.Column#process()
     */
    public Column postProcess() {
        GroupColumn dest = new GroupColumn(this);
        Iterator i = this.iterator();
        while (i.hasNext()) {
            Column element = (Column) i.next();
            dest.addColumn(element.postProcess());

        }
        return dest;
    }

    
    public List getHorizColumnList() {
    	ArrayList ret=new ArrayList();
    	for(int i=0;i<getColumnSpan();i++) {
    		ret.add(getSubColumns(i));
    	}
    	return ret;
    }
    
	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getSubColumn(int)
	 */
	public List getSubColumns(int depth) {
		ArrayList ret=new ArrayList();
		if(getColumnSpan()<depth) return ret;
		if(depth==0) {
			ret.add(this);
			return ret;
		} else
		if(depth==1) {
			ret.addAll(items);
		} else {
			Iterator i=items.iterator();
			while (i.hasNext()) {
				Column element = (Column) i.next();
				ret.addAll(element.getSubColumns(depth-1));
			}
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getColumnDepth()
	 */
	public int getColumnSpan() {
		Column c=this.getColumn(0);
		if(c==null) return 0; else;
		return 1+c.getColumnSpan();
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getCurrentRowSpan()
	 */
	public int getCurrentRowSpan() {
		rowSpan--;
		spanCount++;
		if(spanCount==this.getColumnSpan()+1) return rowSpan+1; else return 1;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getOwnerIds()
	 */
	public Set getOwnerIds() {
		TreeSet ret=new TreeSet();
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			ret.addAll(element.getOwnerIds());
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getTrailCell()
	 */
	public Cell getTrailCell() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getTrailCells()
	 */
	public List getTrailCells() {
		ArrayList ret=new ArrayList();
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			ret.addAll(element.getTrailCells());
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#newInstance()
	 */
	public Column newInstance() {
		return new GroupColumn(this);
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getColumnDepth()
	 */
	public int getColumnDepth() {
		int ret=0;
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			ret+=element.getColumnDepth();			
		}
		return ret;
	}

	
	public int getRowSpan() {
		return 1+items.size();
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#cellCount()
	 */
	public int getCellCount() {
		int count=0;
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			count+=element.getCellCount();
		}
		return count;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getVisibleCellCount()
	 */
	public int getVisibleCellCount(Long ownerId) {
		int count=0;
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			count+=element.getVisibleCellCount(ownerId);
			}
			return count;
	}

	
	public boolean hasTrailCells() {
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			if(element.hasTrailCells()) return true;
		}
		return false;
	}
	
	@Override
	public boolean removeEmptyChildren(boolean checkFunding) {
		List<Column> myItems	= getItems();
		Iterator<Column> iter	= myItems.iterator();
		boolean allEmpty		= true;
		while ( iter.hasNext() ) {
			Column col			= iter.next();
			if ( checkFunding && !ArConstants.COLUMN_FUNDING.equals(col.name)) {
				continue;
			}
			if ( checkFunding && col.getItems().size() == 1 ) {
				Column child 	= (Column) col.getItems().get(0);
				if ( ArConstants.COLUMN_FUNDING.equals(child.name) )
					iter.remove();
				continue;
			}
			if ( col.removeEmptyChildren(false) ) {
				if (col instanceof GroupColumn)
					iter.remove();
			}
			else
				allEmpty		= false;
		}
		return allEmpty;
	}
}
