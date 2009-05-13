package org.digijava.module.aim.ar.impexp.impl;

import org.digijava.module.aim.ar.impexp.ExpTransformer;
import org.digijava.module.aim.ar.impexp.ExpTransformerFactory;
import org.digijava.module.aim.ar.impexp.data.ColumnLikeType;

public class ColumnLikeExpTransformerFactory implements ExpTransformerFactory<Object, ColumnLikeType>{

	private ColumnLikeExpTransformer transformer	= null;
	
	@Override
	public ExpTransformer<Object, ColumnLikeType> generateExpTransformer() {
		if ( this.transformer == null )
			this.transformer		= new ColumnLikeExpTransformer();
		return this.transformer;
	}

}
