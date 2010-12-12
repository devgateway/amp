package org.dgfoundation.amp.onepager.util;

import java.util.HashMap;

import org.apache.wicket.Session;
import org.dgfoundation.amp.onepager.AmpWebSession;
import org.dgfoundation.amp.onepager.components.AmpFMConfigurable;

public final class FMUtil {
	
	private static HashMap<String,Boolean> fmVisible=new HashMap<String,Boolean>();
	private static HashMap<String,Boolean> fmEnabled=new HashMap<String,Boolean>();
	
	public static final boolean isFmEnabled(AmpFMConfigurable fmc) {
		Boolean show=fmEnabled.get(fmc.getFMName());
		if(show==null) return true;
		return show;
	}
	
	public static final boolean isFmVisible(AmpFMConfigurable fmc) {
		Boolean show=fmVisible.get(fmc.getFMName());
		if(show==null) return true;
		return show;
	}
	
	public static void switchFmEnabled(AmpFMConfigurable fmc) {
		Boolean current=isFmEnabled(fmc);
		setFmEnabled(fmc, !current);
	}
	
	public static void switchFmVisible(AmpFMConfigurable fmc) {
		Boolean current=isFmVisible(fmc);
		setFmVisible(fmc, !current);
	}
	
	public static final void setFmEnabled(AmpFMConfigurable fmc,Boolean value) {
		fmEnabled.put(fmc.getFMName(), value);
	}
	
	public static final void setFmVisible(AmpFMConfigurable fmc,Boolean value) {
		fmVisible.put(fmc.getFMName(), value);
	}
	

	public static boolean isFmMode(Session session) {
		AmpWebSession ampSession=(AmpWebSession) session;
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
}
