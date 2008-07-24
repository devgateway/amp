package org.digijava.module.widget.indicatorChart;

import org.digijava.module.widget.Widget;

public class WiIndicatorChart extends Widget{

	public String generateHtml() {
		StringBuffer result = new StringBuffer("<IMG ");
		//TODO add link
		result.append(">");
		return result.toString();
	}

}
