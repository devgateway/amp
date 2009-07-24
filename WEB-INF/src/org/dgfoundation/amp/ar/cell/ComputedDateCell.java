package org.dgfoundation.amp.ar.cell;

import org.dgfoundation.amp.ar.workers.ComputedDateColWorker;

public class ComputedDateCell extends TextCell {

	public Class getWorker() {
		return ComputedDateColWorker.class;
	}

}
