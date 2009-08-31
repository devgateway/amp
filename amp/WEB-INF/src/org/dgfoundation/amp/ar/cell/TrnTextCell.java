package org.dgfoundation.amp.ar.cell;

import javax.servlet.http.HttpServletRequest;

import org.dgfoundation.amp.ar.workers.TrnTextColWorker;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;


public class TrnTextCell extends TextCell{

	public TrnTextCell() {
		super();
	}

	public TrnTextCell(Long id) {
		super(id);
	}
	
	public Class getWorker() {
		return TrnTextColWorker.class;
	}
	
	public String getTrasnlatedValue(HttpServletRequest request){
		Site site = RequestUtils.getSite(request);
		Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
		String siteId = site.getId()+"";
		String locale = navigationLanguage.getCode();
		String text;
		try {
			text = TranslatorWorker.translateText((String) super.getValue(),locale,siteId);
			return text;
		} catch (WorkerException e) {
			e.printStackTrace();
		}
		return getValue()!=null?getValue().toString().replaceAll("\\<.*?>",""):"";
	}
	
}
