/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since 31.10.2006
 * Copyright (c) 31.10.2006 Development Gateway Foundation
 */
package org.dgfoundation.amp.ar.view.xls;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;

/**
 * @author mihai
 * @since 31.10.2006
 *
 */
public class TotalCommitmentsAmountCellXLS extends AmountCellXLS {

	/**
	 * @param parent
	 * @param item
	 */
	public TotalCommitmentsAmountCellXLS(Exporter parent, Viewable item) {
		super(parent, item);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param sheet
	 * @param row
	 * @param rowId
	 * @param colId
	 * @param ownerId
	 * @param item
	 */
	public TotalCommitmentsAmountCellXLS(XSSFWorkbook wb ,XSSFSheet sheet, XSSFRow row,
			IntWrapper rowId, IntWrapper colId, Long ownerId, Viewable item) {
		super(wb,sheet, row, rowId, colId, ownerId, item);
		// TODO Auto-generated constructor stub
	}

}
