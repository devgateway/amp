package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.behaviours.TextualTokenBehaviour;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.output.nicells.NiTextCell;
/**
 * empty text cells, by the default text cell behaviour, are just ""
 * for Project Implementation Delay, the requirement was to explicitely put '0' 
 * for empty cells. 
 * This behaviour extends TextualTokenBehaviour by overriding empty cells 
 * to be '0'.
 * 
 * @author acartaleanu
 *
 */
public class PidTextualTokenBehaviour extends TextualTokenBehaviour {
    
    public final static PidTextualTokenBehaviour instance = new PidTextualTokenBehaviour(); 
    PidTextualTokenBehaviour(){
        super();
    }

    @Override
    public NiOutCell getEmptyCell(ReportSpecification spec) {
        return new NiTextCell("0", -1, null);
    }

}
