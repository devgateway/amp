package org.digijava.module.widget.table.filteredColumn;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.widget.dbentity.AmpDaColumn;
import org.digijava.module.widget.dbentity.AmpDaColumnFilter;
import org.digijava.module.widget.dbentity.AmpDaValueFiltered;
import org.digijava.module.widget.table.WiCell;
import org.digijava.module.widget.table.WiCellStandard;

public class WiCellFiltered extends WiCellStandard {

	private Long filterItemId;
	private String value = "&nbsp;";
	private Long selectedFilterId;
	private FilterItemProvider itemProvider;

	
	public WiCellFiltered() {
		setCellTypeId(WiCell.FILTER_CELL);
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	public void setFilterItemId(Long filterItemId) {
		this.filterItemId = filterItemId;
	}

	public Long getFilterItemId() {
		return filterItemId;
	}

	@Override
	public String tagContent() {
		return getValue();
	}

	public void setSelectedFilterId(Long selectedFilterId) {
		this.selectedFilterId = selectedFilterId;
	}

	public Long getSelectedFilterId() {
		return selectedFilterId;
	}

	public FilterItemProvider getItemProvider() {
		return itemProvider;
	}

	public void setItemProvider(FilterItemProvider itemProvider) {
		this.itemProvider = itemProvider;
	}

	@Override
	public void saveData(Session dbSession, AmpDaColumn dbColumn) throws DgException {
		try {
			AmpDaColumnFilter dbColumnFilter = (AmpDaColumnFilter)dbColumn;
			AmpDaValueFiltered dbValue = null;
			if (this.getId() == null || this.getId().longValue() <= 0) {
				dbValue = new AmpDaValueFiltered();
			} else {
				dbValue = (AmpDaValueFiltered) dbSession.load(AmpDaValueFiltered.class,dbColumnFilter.getId());
			}
			dbValue.setPk(this.getPk());
			dbValue.setValue(this.getValue());
			dbValue.setColumn(dbColumnFilter);
			WiColumnFilterSubColumn columnFilter = (WiColumnFilterSubColumn) this.getColumn();
			dbValue.setFilterItemId(columnFilter.getFilterItemId());
			dbSession.saveOrUpdate(dbValue);
		} catch (HibernateException e) {
			throw new DgException("cannot save cell, ID=" + getId()+ ", columnID=" + getColumn().getId(), e);
		}
	}

}
