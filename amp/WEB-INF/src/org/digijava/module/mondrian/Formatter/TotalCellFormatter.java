package org.digijava.module.mondrian.Formatter;

import mondrian.olap.CellFormatter;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * 
 * @author Diego Dimunzio
 */

/**
 * A cell formatter modifies the behavior of Cell.getFormattedValue(). The class
 * must implement the mondrian.olap.CellFormatter interface, and is specified
 * like this: <Measure name="name" formatter="com.acme.MyCellFormatter">
 * <CalculatedMemberProperty /> </Measure> For a calculated member that belongs
 * to a cube or virtual cube, you can define a formatter by setting the
 * CELL_FORMATTER property of the member to the name of the formatter class:
 * <CalculatedMember name="name" formatter="com.acme.MyCellFormatter">
 * <CalculatedMemberProperty name="CELL_FORMATTER"
 * value="com.acme.MyCellFormatter" /> </CalculatedMember>
 */

public class TotalCellFormatter implements CellFormatter {

	@Override
	public String formatCell(Object value) {
		return FormatHelper.formatNumber(new Double(value.toString()) / FeaturesUtil.getAmountMultiplier());
	}
}
