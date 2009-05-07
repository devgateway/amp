package org.digijava.module.aim.ar.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.dbentity.AmpFilterData;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.form.ReportsFilterPickerForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.Identifiable;
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
		
		String name=null;
		if (arf.getCurrency()!=null){
			name = "- " + arf.getCurrency().getCurrencyName();
		}else{
			name = "- " + Constants.DEFAULT_CURRENCY; 
		}
		
		
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
	
	public static Long[] getObjectsIds (Collection<? extends Identifiable> col) {
		if ( col == null )
			return null;
		
		Long [] ret	= new Long[col.size()];
		Iterator<? extends Identifiable> iter	= col.iterator();
		for (int i=0; iter.hasNext(); i++) {
			Object id	= iter.next().getIdentifier();
			try {
				ret[i]		= Long.parseLong( id.toString() );
			}
			catch(NumberFormatException e) {
				e.printStackTrace();
				return null;
			}
				
		}
		
		return ret;
	}
	
	public static void populateForm ( ReportsFilterPickerForm form, AmpARFilter filter ) {

		// for each sector we have also to add the subsectors
		form.setSelectedSectors( FilterUtil.getObjectsIds(filter.getSelectedSectors()) );
		form.setSelectedSecondarySectors( FilterUtil.getObjectsIds(filter.getSelectedSecondarySectors()) );
	
		form.setSelectedNatPlanObj( FilterUtil.getObjectsIds(filter.getSelectedNatPlanObj()) );
		form.setSelectedPrimaryPrograms( FilterUtil.getObjectsIds(filter.getSelectedPrimaryPrograms()) );
		form.setSelectedSecondaryPrograms( FilterUtil.getObjectsIds(filter.getSelectedSecondaryPrograms()) );
		
		form.setText( filter.getText() );
		form.setIndexString( filter.getIndexText() );
		
		form.setFromYear( filter.getYearFrom()!=null ? filter.getYearFrom().longValue() : null );
		form.setToYear( filter.getYearTo()!=null ? filter.getYearTo().longValue() : null );
		form.setFromMonth( filter.getFromMonth() );
		form.setToMonth( filter.getToMonth() );
		form.setFromDate( filter.getFromDate() );
		form.setToDate( filter.getToDate() );

		if (filter.getCurrency() != null)
			form.setCurrency( filter.getCurrency().getAmpCurrencyId() );
		
		form.setLineMinRank( filter.getLineMinRank() );
		form.setPlanMinRank( filter.getPlanMinRank() );
		if ( filter.getRegionSelected() != null )
			form.setRegionSelected( filter.getRegionSelected().getId() );
		
		Collection<String> appStatuses		= filter.getApprovalStatusSelected();
		if ( appStatuses!=null && appStatuses.size()>0  ) {
			form.setApprovalStatusSelected( appStatuses.toArray() );
		}
		
		form.setSelectedStatuses( FilterUtil.getObjectsIds(filter.getStatuses()) ) ;

		form.setSelectedProjectCategory( FilterUtil.getObjectsIds(filter.getProjectCategory()) );
		
		form.setSelectedFinancingInstruments( FilterUtil.getObjectsIds(filter.getFinancingInstruments()) ); 

		form.setSelectedTypeOfAssistance( FilterUtil.getObjectsIds(filter.getTypeOfAssistance()) );

		form.setPageSize( filter.getPageSize() );

		form.setSelectedRisks( FilterUtil.getObjectsIds(filter.getRisks()) ); 
		form.setGovernmentApprovalProcedures( filter.getGovernmentApprovalProcedures() );
		form.setJointCriteria( filter.getJointCriteria() );

		form.setSelectedDonorTypes( FilterUtil.getObjectsIds(filter.getDonorTypes()) );
		form.setSelectedDonorGroups( FilterUtil.getObjectsIds(filter.getDonorGroups()) );

		if (filter.getBudget() != null)
			form.setSelectedBudget( filter.getBudget()?1:2 );
		
		form.setJustSearch( filter.isJustSearch() );
		form.setRenderStartYear( filter.getRenderStartYear()>0 ? filter.getRenderStartYear() : -1 );
		form.setRenderEndYear( filter.getRenderEndYear()>0 ? filter.getRenderEndYear() : -1 );
		
		form.setSelectedBeneficiaryAgency( FilterUtil.getObjectsIds(filter.getBeneficiaryAgency()) );
		form.setSelectedDonnorAgency( FilterUtil.getObjectsIds(filter.getDonnorgAgency()) );
		form.setSelectedImplementingAgency( FilterUtil.getObjectsIds(filter.getImplementingAgency()) );
		form.setSelectedExecutingAgency( FilterUtil.getObjectsIds(filter.getExecutingAgency()) );
		
	}
	
}
