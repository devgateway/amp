/**
 * CategAmountCell.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.dgfoundation.amp.ar.cell;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.Categorizable;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.workers.CategAmountColWorker;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since May 30, 2006 AmountCell that also holds metadata
 */
public class CategAmountCell extends AmountCell implements Categorizable {

	protected Set metaData;

	@Override
	public Cell merge(Cell c) {
		AmountCell ret=(AmountCell) super.merge(c);
		CategAmountCell realRet=new CategAmountCell(ret.getOwnerId());
		realRet.getMergedCells().addAll(ret.getMergedCells());
		CategAmountCell categ=(CategAmountCell) c;
		realRet.getMetaData().addAll(categ.getMetaData());
		return realRet;
	}	
	@Override
    public void merge(Cell c1, Cell c2) {
		super.merge(c1, c2);
		CategAmountCell categ1=(CategAmountCell) c1;
		CategAmountCell categ2=(CategAmountCell) c2;
		categ1.getMetaData().addAll(categ2.getMetaData());
	}
	
	/**
	 * this item is a customized show only for cummulative amounts
	 */
	protected boolean cummulativeShow;
	
	/**
	 * @return Returns the cummulativeShow.
	 */
	public boolean isCummulativeShow() {
		return cummulativeShow;
	}

	public Cell newInstance() {
		return new CategAmountCell();
	}
	
	/**
	 * @param cummulativeShow The cummulativeShow to set.
	 */
	public void setCummulativeShow(boolean cummulativeShow) {
		this.cummulativeShow = cummulativeShow;
	}

	/**
	 * @return Returns the metaData.
	 */
	public Set getMetaData() {
		return metaData;
	}

	/**
	 * @param metaData
	 *            The metaData to set.
	 */
	public void setMetaData(Set metaData) {
		this.metaData = metaData;
	}

	public String getMetaValueString(String category) {
		MetaInfo mi = MetaInfo.getMetaInfo(metaData,category);
		if (mi == null)
			return null;
		return  mi.getValue().toString();
	}

	
	public boolean existsMetaString(String category) {
		MetaInfo mi = MetaInfo.getMetaInfo(metaData,category);
		if(mi!=null) return true;
		return false;
		
	}


	public CategAmountCell() {
		super();
		metaData = new HashSet();
	}

	public Class getWorker() {
		return CategAmountColWorker.class;
	}

	/**
	 * @param ownerId
	 * @param name
	 * @param value
	 */
	public CategAmountCell(Long id) {
		super(id);
		metaData = new HashSet();
	}

	public void setValue(Object o) {
		this.amount = ((Double) o).doubleValue();
	}

	public Object getValue() {
		return new Double(amount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.Viewable#getViewArray()
	 */
	protected MetaInfo[] getViewArray() {
		// TODO Auto-generated method stub
		return null;
	}

	
public void applyMetaFilter(String columnName,Cell metaCell,CategAmountCell ret) {
	
	if(metaCell.getColumn().getName().equals(columnName) ) {
		//we need to get the percentage, it is stored in the MetaText of related to the owner of the current cell
		CellColumn c=(CellColumn) metaCell.getColumn();
		Cell temp	= c.getByOwnerAndValue(this.getOwnerId(),metaCell.getValue());
		if ( temp instanceof MetaTextCell) {
			MetaTextCell relatedHierarchyCell=(MetaTextCell) temp;
			if(relatedHierarchyCell!=null) { 
			MetaInfo percentMeta=MetaInfo.getMetaInfo(relatedHierarchyCell.getMetaData(),ArConstants.PERCENTAGE);
			if(percentMeta!=null) {
				Double percentage=(Double) percentMeta.getValue() ;
				ret.setPercentage(percentage.doubleValue(), relatedHierarchyCell);			
			}
			}
		}
	}
	
}
	
	
public Cell filter(Cell metaCell,Set ids) {
    	CategAmountCell ret = (CategAmountCell) super.filter(metaCell,ids);    
		if(ret==null) return null;
		
		if ( metaCell.getColumn().getName().equals(ArConstants.COLUMN_CAPITAL_EXPENDITRURE) ) {
			if(!metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.COLUMN_CAPITAL_EXPENDITRURE)))
				return null;
		}
		
		if(metaCell.getColumn().getName().equals(ArConstants.DONOR)) 
		
		if((!metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.DONOR))) && (!ret.existsMetaString(ArConstants.COSTING_GRAND_TOTAL)))
		return null;
		
		if(metaCell.getColumn().getName().equals(ArConstants.DONOR_GROUP)) 
			if(!metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.DONOR_GROUP)))
		return null;
		
			
		if(metaCell.getColumn().getName().equals(ArConstants.DONOR_TYPE_COL)) 
			if(!metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.DONOR_TYPE_COL)))
		return null;
	
		if(metaCell.getColumn().getName().equals(ArConstants.REGION) &&
				this.getNearestReportData().getReportMetadata().getType()==ArConstants.REGIONAL_TYPE){
				String retRegionName	= ret.getMetaValueString(ArConstants.REGION);
				retRegionName			= (retRegionName!=null)?retRegionName.trim():retRegionName;
				if( !metaCell.getValue().toString().equals( retRegionName ) )
					return null;
		}
		
		if(metaCell.getColumn().getName().equals(ArConstants.DISTRICT) &&
				this.getNearestReportData().getReportMetadata().getType()==ArConstants.REGIONAL_TYPE) {
			
				String retDistrictName	= ret.getMetaValueString(ArConstants.DISTRICT);
				retDistrictName			= (retDistrictName!=null)?retDistrictName.trim():retDistrictName;
				if( !metaCell.getValue().toString().equals( retDistrictName ) )
					return null;
		}
		
		if(metaCell.getColumn().getName().equals(ArConstants.ZONE) &&
				this.getNearestReportData().getReportMetadata().getType()==ArConstants.REGIONAL_TYPE) {
				String retZoneName	= ret.getMetaValueString(ArConstants.ZONE);
				retZoneName			= (retZoneName!=null)?retZoneName.trim():retZoneName;
				if(!metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.ZONE)))
					return null;
		}

		if(metaCell.getColumn().getName().equals(ArConstants.TERMS_OF_ASSISTANCE)) 
				if(!metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.TERMS_OF_ASSISTANCE)))
		return null;
		
		if(metaCell.getColumn().getName().equals(ArConstants.FINANCING_INSTRUMENT)) 
			if(!metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.FINANCING_INSTRUMENT)))
		return null;
	
		if(metaCell.getColumn().getName().equals(ArConstants.FUNDING_STATUS) && ret.getMetaValueString(ArConstants.FUNDING_STATUS)!=null)
			if(!metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.FUNDING_STATUS)))
		return null;
	
		if(metaCell.getColumn().getName().equals(ArConstants.MODE_OF_PAYMENT) && ret.getMetaValueString(ArConstants.MODE_OF_PAYMENT)!=null) 
			if(!metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.MODE_OF_PAYMENT)))
		return null;

		if(metaCell.getColumn().getName().equals(ArConstants.COMPONENT)) 
			if(!metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.COMPONENT)))
		return null;
		
		
		//apply metatext filters
		if(metaCell instanceof MetaTextCell) {
			//apply metatext filters for column Sector
//		 applyMetaFilter("Sector", ArConstants.SECTOR_PERCENTAGE, metaCell, ret);
//		 applyMetaFilter("Executing Agency", ArConstants.EXECUTING_AGENCY_PERCENTAGE, metaCell, ret);
//		 applyMetaFilter("Sub-Sector", ArConstants.SECTOR_PERCENTAGE, metaCell, ret);
//		 applyMetaFilter("Region", ArConstants.LOCATION_PERCENTAGE, metaCell, ret);
//		 applyMetaFilter("Componente", ArConstants.COMPONENTE_PERCENTAGE, metaCell, ret);
//		 applyMetaFilter("National Planning Objectives", ArConstants.NPO_PERCENTAGE, metaCell, ret);
//		 applyMetaFilter("Primary Program", ArConstants.PROGRAM_PERCENTAGE, metaCell, ret);
//		 applyMetaFilter("Secondary Program", ArConstants.PROGRAM_PERCENTAGE, metaCell, ret);
			
			for (Iterator iterator = this.getNearestReportData().getReportMetadata().getHierarchies().iterator(); iterator.hasNext();) {
				AmpReportHierarchy col = (AmpReportHierarchy) iterator.next();
				if(col.getColumn().getCellType().contains(MetaTextCell.class.getSimpleName()))
					//NEVER apply this for regional reports with regional metaCell:
					if(metaCell.getColumn().getName().equals(ArConstants.REGION) && this.getNearestReportData().getReportMetadata().getType()==ArConstants.REGIONAL_TYPE)
						continue;
					//column is needed to get the tokenExpression on computed fields
					ret.setColumn(this.getColumn());
					applyMetaFilter(col.getColumn().getColumnName(), metaCell, ret);
			}
			
			 

		}
		
		//if(ret.getMergedCells().size()>0) 
			//logger.info(ret.getMergedCells());
		return ret;
	}	/*
		 * (non-Javadoc)
		 * 
		 * @see org.dgfoundation.amp.ar.Categorizable#hasMeta(org.dgfoundation.amp.ar.MetaInfo)
		 */
	public boolean hasMetaInfo(MetaInfo m) {
		MetaInfo internal = MetaInfo.getMetaInfo(metaData,m.getCategory());
		if (internal == null)
			return false;
		if (internal.equals(m))
			return true;
		else
			return false;
	}
	
	
	private  boolean render;
	public void  setRenderizable(boolean prender) {
	    // TODO Auto-generated method stub
	     render=prender;
	}
	public boolean isRenderizable() {
	    // TODO Auto-generated method stub
	    return render;
	}
}
