package org.digijava.kernel.ampapi.endpoints.gpi;

import org.apache.commons.lang.math.NumberUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpGPINiAidOnBudget;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;
import org.hibernate.Query;

/**
 * 
 * @author gerald
 *
 */
public class GPIDataService {
	public static JsonBean saveAidOnBudget(JsonBean data) {
		JsonBean result = null;
		Long id;
		AmpGPINiAidOnBudget aidOnBudget = null;
		if(data.getString(GPIEPConstants.FIELD_ID) != null && NumberUtils.isNumber(data.getString(GPIEPConstants.FIELD_ID))){
			id = Long.parseLong(data.getString(GPIEPConstants.FIELD_ID));
			aidOnBudget = findAidOnBudgetById(id);
		}else{
			aidOnBudget = new AmpGPINiAidOnBudget();
		}
		Long currencyId = Long.parseLong(data.getString(GPIEPConstants.FIELD_CURRENCY_ID));
		Long donorId =  Long.parseLong(data.getString(GPIEPConstants.FIELD_DONOR_ID));
		
		//aidOnBudget.setCurrencyId(currencyId);
		return result;
	}
	public static AmpGPINiAidOnBudget findAidOnBudgetById(Long id){
		Session dbSession = PersistenceManager.getSession();
        String queryString = "select gpi from "
                + AmpGPINiAidOnBudget.class.getName() + " gpi where id=:id";
        Query qry = dbSession.createQuery(queryString);
        qry.setLong("id", id);
        if (qry.list().size()==1)
            return (AmpGPINiAidOnBudget) qry.list().get(0);
        else
            return null;
	}
}
