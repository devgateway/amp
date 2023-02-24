package org.dgfoundation.amp.ar.cell;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.workers.TrnTextColWorker;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;


public class TrnTextCell extends TextCell{
    
    protected static Logger logger = Logger.getLogger(TrnTextCell.class);

    public TrnTextCell() {
        super();
    }

    public TrnTextCell(Long id) {
        super(id);
    }
    
    public Class getWorker() {
        return TrnTextColWorker.class;
    }       
    
    //For AMP-16666 value is already translated so the superclass toString() is ok
//  @Override
//  public String toString() {
//      
//      //return this.getTrasnlatedValue(parent.getReportMetadata().getSiteId(), parent.getReportMetadata().getLocale()) ;
//      return this.getValue() != null ? this.getValue().toString() : "";
//  }
}
