package org.digijava.module.aim.ar.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Session;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.dbentity.AmpFilterData;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.SectorUtil;

public class FilterUtil {
	public static void populateFilter(AmpReports report, AmpARFilter filter) {
		if ( report.getFilterDataSet()!=null && report.getFilterDataSet().size()>0 ) {
			Iterator<AmpFilterData> iter	= report.getFilterDataSet().iterator();
			while ( iter.hasNext() ) {
				try {
					iter.next().populateField(filter);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void prepare(HttpServletRequest request, AmpARFilter arf) throws Exception {
		HttpSession httpSession = request.getSession();

		request.setAttribute("apply", "apply");
		//AmpARFilter arf = (AmpARFilter) httpSession.getAttribute(ArConstants.REPORTS_FILTER);
		
		arf.readRequestData(request);

		// for each sector we have also to add the subsectors

		if (arf.getSelectedSectors() != null && arf.getSelectedSectors().size() > 0) {

			arf.setSectors(SectorUtil.getSectorDescendents( arf.getSelectedSectors() ));
		} else {
			arf.setSectors(null);
		}

		if (arf.getSelectedSecondarySectors() != null && arf.getSelectedSecondarySectors().size() > 0) {
			arf.setSecondarySectors(SectorUtil.getSectorDescendents( arf.getSelectedSecondarySectors() ));
		} else {
			arf.setSecondarySectors(null);
		}


		if ( arf.getSelectedNatPlanObj() != null && arf.getSelectedNatPlanObj().size() > 0) {
			arf.setNationalPlanningObjectives(new ArrayList( arf.getSelectedNatPlanObj() ));
		} else {
			arf.setSelectedNatPlanObj(null);
			arf.setNationalPlanningObjectives(null);
		}
		
		Set selectedPrimaryPrograms	= arf.getSelectedPrimaryPrograms();
		
		if (selectedPrimaryPrograms != null && selectedPrimaryPrograms.size() > 0) {
			arf.setPrimaryPrograms(new ArrayList(selectedPrimaryPrograms));
		} else {
			arf.setPrimaryPrograms(null);
		}

		Set selectedSecondaryPrograms	= arf.getSelectedSecondaryPrograms();
		if (selectedSecondaryPrograms != null && selectedSecondaryPrograms.size() > 0) {
			arf.setSecondaryPrograms(new ArrayList(selectedSecondaryPrograms));
		} else {
			arf.setSecondaryPrograms(null);
		}
		
		

		String name = "- " + arf.getCurrency().getCurrencyName();
		httpSession.setAttribute(ArConstants.SELECTED_CURRENCY, name);

		String customDecimalSymbol	= String.valueOf((FormatHelper.getDecimalFormat().getDecimalFormatSymbols().getDecimalSeparator()));
		Integer customDecimalPlaces	= FormatHelper.getDecimalFormat().getMaximumFractionDigits();
		String customGroupCharacter	= String.valueOf(FormatHelper.getDecimalFormat().getDecimalFormatSymbols().getGroupingSeparator());
		Boolean customUseGrouping	= FormatHelper.getDecimalFormat().isGroupingUsed();
		Integer customGroupSize		= FormatHelper.getDecimalFormat().getGroupingSize();
		String customDecimalSymbolTxt 	= "";
		String customGroupCharacterTxt	= "";;

		DecimalFormat custom = new DecimalFormat();
		DecimalFormatSymbols ds = new DecimalFormatSymbols();
		ds.setDecimalSeparator((!"CUSTOM".equalsIgnoreCase(customDecimalSymbol) ? customDecimalSymbol.charAt(0) : customDecimalSymbolTxt.charAt(0)));
		ds.setGroupingSeparator((!"CUSTOM".equalsIgnoreCase(customGroupCharacter) ? customGroupCharacter.charAt(0) : customGroupCharacterTxt
						.charAt(0)));
		custom.setMaximumFractionDigits((customDecimalPlaces != -1) ?customDecimalPlaces : 99);
		custom.setGroupingUsed(customUseGrouping);
		custom.setGroupingSize(customGroupSize);
		custom.setDecimalFormatSymbols(ds);
		arf.setCurrentFormat(custom);
			
		httpSession.setAttribute(ArConstants.REPORTS_FILTER, arf);
	}
}
