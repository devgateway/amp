package org.dgfoundation.amp.exprlogic;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.digijava.module.categorymanager.action.CategoryManager;

public class ANDBinaryLogicalToken extends BinaryLogicalToken {
	
	private static Logger logger	= Logger.getLogger(ANDBinaryLogicalToken.class);

	public ANDBinaryLogicalToken(LogicalToken left,
			LogicalToken right, boolean negation) {
		super(left, right, negation);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean evaluate(CategAmountCell c) {
		//logger.warn(this + "Evaluating AND:" + left.getClass() + " & " + right.getClass() + " amount: " + c.amount );
		boolean retLeft	= left.evaluate(c);
		//logger.warn(this + " LEFT evaluations is : " + retLeft );
		if ( retLeft ) {
			boolean retRight	= right.evaluate(c);
			//logger.warn(this + " RIGHT evaluations is : " + retRight );
			ret		= retLeft && retRight;
		}
		else
			ret		= false;
		//logger.warn(this + " RET evaluation is : " + ret );
//		ret=left.evaluate(c) && right.evaluate(c);
		//return super.evaluate(c);
		boolean realRet		= super.evaluate(c);
		//logger.warn(this + " RealRET evaluation is : " + realRet );
		return realRet;
	}
	
}
