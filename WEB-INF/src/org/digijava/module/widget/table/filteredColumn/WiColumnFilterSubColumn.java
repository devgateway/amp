package org.digijava.module.widget.table.filteredColumn;

import java.util.List;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.widget.dbentity.AmpDaColumnFilter;
import org.digijava.module.widget.dbentity.AmpDaValue;
import org.digijava.module.widget.dbentity.AmpDaValueFiltered;
import org.digijava.module.widget.table.WiCell;
import org.digijava.module.widget.table.WiColumnStandard;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class WiColumnFilterSubColumn extends WiColumnStandard {

	private Long filterItemId;

        @Override
        public void saveData(Session dbSession) throws DgException {
            try {
                AmpDaColumnFilter dbColumn = (AmpDaColumnFilter) dbSession.load(AmpDaColumnFilter.class, this.getId());
                List<WiCell> myCells = this.getAllCells();
                List<WiCell> trashedCells = this.getAllTrashedCells();
                for (WiCell cell : myCells) {
                    cell.saveData(dbSession, dbColumn);
                }
                for (WiCell trashedCell : trashedCells) {
                	if (trashedCell.getId() != null && trashedCell.getId() >0){
                		trashedCell.removeData(dbSession, dbColumn);
                	}
                }
                this.clearTrashedCells();
            } catch (HibernateException e) {
                throw new DgException("cannot save column, ID=" + getId(), e);
            }
        }

	public void setFilterItemId(Long filterItemId) {
		this.filterItemId = filterItemId;
	}

	public Long getFilterItemId() {
		return filterItemId;
	}
    @Override
	public WiCell createCell() {
		WiCellFiltered cell = new WiCellFiltered();
		cell.setColumn(this);
		return cell;
	}

	@Override
	public WiCell createCell(AmpDaValue value) {
		WiCellFiltered cell = (WiCellFiltered)this.createCell();
		cell.setId(value.getId());
		cell.setPk(value.getPk());
		cell.setValue(value.getValue());
		cell.setFilterItemId(((AmpDaValueFiltered) value).getFilterItemId());
		return cell;
	}
}
