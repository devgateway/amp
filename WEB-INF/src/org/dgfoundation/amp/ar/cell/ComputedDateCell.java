package org.dgfoundation.amp.ar.cell;

import org.dgfoundation.amp.ar.workers.ComputedDateColWorker;
import org.digijava.module.aim.helper.FormatHelper;

public class ComputedDateCell extends TextCell {

	public Class getWorker() {
		return ComputedDateColWorker.class;
	}

	@Override
	public String toString() {
		String result =super.toString();
		if ("".equalsIgnoreCase(result) || result==null){
			result="0";
		}
		return FormatHelper.formatNumber(Double.parseDouble(result));
	}

	@Override
	public Object getValue() {

		return super.getValue();
	}

	public ComputedDateCell(Long id) {
		super(id);

	}

	public ComputedDateCell() {
		super();
	}
}
