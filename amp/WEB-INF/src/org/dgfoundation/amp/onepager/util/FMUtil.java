package org.dgfoundation.amp.onepager.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.AmpWebSession;
import org.dgfoundation.amp.onepager.components.AmpFMConfigurable;

public final class FMUtil {
	private static Logger logger = Logger.getLogger(FMUtil.class);
	private static HashMap<String,Boolean> fmVisible=new HashMap<String,Boolean>();
	private static HashMap<String,Boolean> fmEnabled=new HashMap<String,Boolean>();
	
	public static final boolean isFmEnabled(Component c) {
		getFmPath(c);
		//logger.info(getFmPath(c));
		AmpFMConfigurable fmc = (AmpFMConfigurable) c;
		Boolean show=fmEnabled.get(fmc.getFMName());
		if(show==null) return true;
		return show;
	}
	
	public static final boolean isFmVisible(Component c) {
		AmpFMConfigurable fmc = (AmpFMConfigurable) c;
		Boolean show=fmVisible.get(fmc.getFMName());
		if(show==null) return true;
		return show;
	}
	
	public static void switchFmEnabled(Component c) {
		AmpFMConfigurable fmc = (AmpFMConfigurable) c;
		Boolean current=isFmEnabled(c);
		setFmEnabled(c, !current);
	}
	
	public static void switchFmVisible(Component c) {
		AmpFMConfigurable fmc = (AmpFMConfigurable) c;
		Boolean current=isFmVisible(c);
		setFmVisible(c, !current);
	}
	
	public static final void setFmEnabled(Component c, Boolean value) {
		AmpFMConfigurable fmc = (AmpFMConfigurable) c;
		fmEnabled.put(fmc.getFMName(), value);
	}
	
	public static final void setFmVisible(Component c, Boolean value) {
		AmpFMConfigurable fmc = (AmpFMConfigurable) c;
		fmVisible.put(fmc.getFMName(), value);
	}
	

	public static boolean isFmMode(Session session) {
		AmpAuthWebSession ampSession=(AmpAuthWebSession) session;
		return ampSession.isFmMode();
	}
	
	/**
	 * Returns the tooltip for this field, if any, stored in the FM database.
	 * @param fmc
	 * @return the tooltip string, or null if not available
	 */
	public static final String getTooltip(AmpFMConfigurable fmc) {
		return "This tooltip for "+fmc.getFMName()+" is supposed to be fetched from the FM database";
	}
	
	public static String getFmPath(Component c) {
		Component visitor = c;
		String ret = "";
		LinkedList<AmpFMTypes> mmm = new LinkedList<AmpFMTypes>();
		while (visitor != null) {
			if (visitor instanceof AmpFMConfigurable){
				AmpFMConfigurable fmc = (AmpFMConfigurable) visitor;
				String typeName;
				switch (fmc.getFMType()) {
				case FEATURE:
					typeName = "feature";
					break;
				case FIELD:
					typeName = "field";
					break;
				case MODULE:
					typeName = "module";
					break;
				default:
					typeName = "n/a";
					break;
				}
				ret = "[" + typeName + ": " + fmc.getFMName() + "] " + ret;
				mmm.addFirst(fmc.getFMType());
			}
			visitor = visitor.getParent();
		}
		ret = "[module: OnePager] "+ ret;
		mmm.addFirst(AmpFMTypes.MODULE);
		
		
		//Check that path is compatible with FM structure:
		//		(module)*{(feature) | [(feature)(field)]}?
		boolean pathOk = true;
		while (!mmm.isEmpty() && mmm.getFirst() == AmpFMTypes.MODULE)
			mmm.removeFirst();
		if (mmm.size() > 2)
			pathOk = false;
		else{
			if (!mmm.isEmpty() && mmm.getFirst() != AmpFMTypes.FEATURE)
				pathOk = false;
			else
				if (mmm.size() == 2 && mmm.getLast() != AmpFMTypes.FIELD)
					pathOk = false;
		}
		
		if (!pathOk){
			logger.error(">>>");
			logger.error(">>> Current FM path is wrong: " + ret);
			logger.error(">>>");
		}
		return ret;
	}
}
