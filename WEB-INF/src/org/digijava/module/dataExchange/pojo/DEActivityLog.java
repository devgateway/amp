/**
 * 
 */
package org.digijava.module.dataExchange.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.digijava.module.dataExchange.jaxb.CodeValueType;
import org.digijava.module.dataExchange.util.DataExchangeConstants;

/**
 * @author dan
 *
 */
public class DEActivityLog extends DELog{
	private final String logType = DataExchangeConstants.ACTIVITY_LOG;
	
	public DEActivityLog() {
		// TODO Auto-generated constructor stub
		this.setItems(new ArrayList());
	}

	public DEActivityLog(String description, String entityName) {
		super(description, entityName);
		this.setItems(new ArrayList());
	}

	public DEActivityLog(String entityName) {
		super();
		this.setItems(new ArrayList());
	}

	
	public String getLogType() {
		return logType;
	}

	public DEActivityLog(CodeValueType cvt) {
		super(cvt);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<DELog> getListItemLog(String s){
		ArrayList<DELog> result = new ArrayList<DELog>();
		for (Iterator it = this.getItems().iterator(); it.hasNext();) {
			DELog deLog = (DELog) it.next();
			if(s.equals(deLog.getLogType()))
				result.add(deLog);
		}
		return result;
	}
	
	public String buildLog(String s){
		String r = "";
		
		return r;
	}
	
	@Override
	public String display() {
		// TODO Auto-generated method stub
		String result = "";
		HashMap hm = new HashMap<String, ArrayList<DELog> >();
		generateHashMap(hm);
		if(this.getItems()!=null && this.getItems().size()>0){

			String aux = null;
			 
			aux = displayLog( (ArrayList<DELog>)hm.get(DataExchangeConstants.MISSING_SECTOR));
			if(aux !=null ){
				result += DataExchangeConstants.MISSING_SECTOR + "<br/>" + aux;
			}
			
			aux = displayLog( (ArrayList<DELog>)hm.get(DataExchangeConstants.MISSING_PROGRAM));
			if(aux !=null ){
				result += DataExchangeConstants.MISSING_PROGRAM + "<br/>" + aux;
			}
			
			aux = displayLog( (ArrayList<DELog>)hm.get(DataExchangeConstants.MISSING_ORGANIZATION));
			if(aux !=null ){
				result += DataExchangeConstants.MISSING_ORGANIZATION + "<br/>" + aux;
			}
			
			aux = displayLog( (ArrayList<DELog>)hm.get(DataExchangeConstants.MISSING_STATUS));
			if(aux !=null ){
				result += DataExchangeConstants.MISSING_STATUS + "<br/>" + aux;
			}
			
			aux = displayLog( (ArrayList<DELog>)hm.get(DataExchangeConstants.MISSING_TYPE_OF_ASSISTANCE));
			if(aux !=null ){
				result += DataExchangeConstants.MISSING_TYPE_OF_ASSISTANCE + "<br/>" + aux;
			}
			
			aux = displayLog( (ArrayList<DELog>)hm.get(DataExchangeConstants.MISSING_IMPLEMENTATION_LOCATION));
			if(aux !=null ){
				result += DataExchangeConstants.MISSING_IMPLEMENTATION_LOCATION + "<br/>" + aux;
			}
			
			aux = displayLog( (ArrayList<DELog>)hm.get(DataExchangeConstants.MISSING_IMPLEMENTATION_LEVEL));
			if(aux !=null ){
				result += DataExchangeConstants.MISSING_IMPLEMENTATION_LEVEL + "<br/>" + aux;
			}
			
			aux = displayLog( (ArrayList<DELog>)hm.get(DataExchangeConstants.MISSING_FINANCING_INSTRUMENT));
			if(aux !=null ){
				result += DataExchangeConstants.MISSING_FINANCING_INSTRUMENT + "<br/>" + aux;
			}
			
			aux = displayLog( (ArrayList<DELog>)hm.get(DataExchangeConstants.MISSING_MTEF));
			if(aux !=null ){
				result += DataExchangeConstants.MISSING_MTEF + "<br/>" + aux;
			}
						
			return result;
		}
		else return null;
	}
	
	private String displayLog(ArrayList<DELog> list) {
		// TODO Auto-generated method stub
		String s = null;
		if(list!=null)
			for (Iterator it = list.iterator(); it.hasNext();) {
				DELog deLog = (DELog) it.next();
				s+=deLog.display()+"<br/>";
			}
		return s;
	}

	private void generateHashMap(HashMap<String, ArrayList<DELog> > hm){
		if(this.getItems()!=null && this.getItems().size()>0){
			for (Iterator it = this.getItems().iterator(); it.hasNext();){
				DELog deLog = (DELog) it.next();
				hm.put(deLog.getLogType(), new ArrayList<DELog>());
			}
		}
	}
	
	private String createLog(DELog deLog) {
		// TODO Auto-generated method stub
		return null;
	}


}
