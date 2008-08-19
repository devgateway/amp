package org.digijava.module.widget.table.filteredColumn;

import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.widget.dbentity.AmpDaColumnFilter;
import org.digijava.module.widget.table.WiCell;
import org.digijava.module.widget.table.WiColumnStandard;

public class WiColumnFilterSubColumn extends WiColumnStandard {

	private Long filterItemId;

	@Override
	public void saveData(Session dbSession) throws DgException {
		try {
			AmpDaColumnFilter dbColumn = (AmpDaColumnFilter) dbSession.load(AmpDaColumnFilter.class, this.getId());
			List<WiCell> myCells = this.getAllCells();
			for (WiCell cell : myCells) {
				cell.saveData(dbSession, dbColumn);
			}
		} catch (HibernateException e) {
			throw new DgException("cannot save column, ID="+getId(),e);
		}
	}

	public void setFilterItemId(Long filterItemId) {
		this.filterItemId = filterItemId;
	}

	public Long getFilterItemId() {
		return filterItemId;
	}
}
