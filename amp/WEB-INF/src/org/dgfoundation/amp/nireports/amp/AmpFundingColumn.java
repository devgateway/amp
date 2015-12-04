package org.dgfoundation.amp.nireports.amp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;

/**
 * the {@link NiReportColumn} which fetches the funding
 * 
 * TODO: this is a stub for now
 * @author Dolghier Constantin
 *
 */
public class AmpFundingColumn extends NiReportColumn<CategAmountCell> {

	protected AmpFundingColumn() {
		super("Funding", null, null);
	}

	@Override
	public List<CategAmountCell> fetchColumn(NiReportsEngine engine) {
		return new ArrayList<>();
	}

}
