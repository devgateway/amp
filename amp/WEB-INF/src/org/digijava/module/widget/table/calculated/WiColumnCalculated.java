package org.digijava.module.widget.table.calculated;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.widget.dbentity.AmpDaValue;
import org.digijava.module.widget.table.WiCell;
import org.digijava.module.widget.table.WiColumn;
import org.hibernate.Session;

/**
 * Not used/implemented yet cos now reqs yet.
 * @author Irakli Kobiashvili
 *
 */
public class WiColumnCalculated extends WiColumn{

	@Override
	public void saveData(Session dbSession) throws DgException {
	}

	@Override
	public WiCell createCell() {
		return null;
	}

	@Override
	public WiCell createCell(AmpDaValue value) {
		return null;
	}


}
