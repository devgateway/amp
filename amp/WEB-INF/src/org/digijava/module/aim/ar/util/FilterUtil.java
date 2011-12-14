package org.digijava.module.aim.ar.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.dbentity.AmpFilterData;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.ReportsFilterPickerForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.ProgramUtil;
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
			
			arf.setSectorsAndAncestors( new HashSet<AmpSector>() );
			arf.getSectorsAndAncestors().addAll( arf.getSectors() );
			arf.getSectorsAndAncestors().addAll( SectorUtil.getAmpParentSectors( arf.getSelectedSectors() ) );
		} else {
			arf.setSectors(null);
			arf.setSectorsAndAncestors(null);
		}

		if (arf.getSelectedSecondarySectors() != null && arf.getSelectedSecondarySectors().size() > 0) {
			arf.setSecondarySectors(SectorUtil.getSectorDescendents( arf.getSelectedSecondarySectors() ));
			
			arf.setSecondarySectorsAndAncestors( new HashSet<AmpSector>() );
			arf.getSecondarySectorsAndAncestors().addAll( arf.getSecondarySectors() );
			arf.getSecondarySectorsAndAncestors().addAll( SectorUtil.getAmpParentSectors(arf.getSelectedSecondarySectors()) );
		} else {
			arf.setSecondarySectors(null);
			arf.setSecondarySectorsAndAncestors(null);
		}
        if (arf.getSelectedTertiarySectors() != null && arf.getSelectedTertiarySectors().size() > 0) {
			arf.setTertiarySectors(SectorUtil.getSectorDescendents( arf.getSelectedTertiarySectors() ));

			arf.setTertiarySectorsAndAncestors( new HashSet<AmpSector>() );
			arf.getTertiarySectorsAndAncestors().addAll( arf.getTertiarySectors() );
			arf.getTertiarySectorsAndAncestors().addAll( SectorUtil.getAmpParentSectors(arf.getSelectedTertiarySectors()) );
		} else {
			arf.setTertiarySectors(null);
			arf.setTertiarySectorsAndAncestors(null);
		}

		if ( arf.getSelectedNatPlanObj() != null && arf.getSelectedNatPlanObj().size() > 0) {
			arf.setNationalPlanningObjectives(new ArrayList( arf.getSelectedNatPlanObj() ));
			
			arf.setRelatedNatPlanObjs(new HashSet<AmpTheme>() );
			ProgramUtil.collectFilteringInformation(arf.getSelectedNatPlanObj(), arf.getNationalPlanningObjectives(), arf.getRelatedNatPlanObjs() );
			
		} else {
			arf.setSelectedNatPlanObj(null);
			arf.setNationalPlanningObjectives(null);
			arf.setRelatedNatPlanObjs(null);
		}
		
		Set selectedPrimaryPrograms	= arf.getSelectedPrimaryPrograms();
		
		if (selectedPrimaryPrograms != null && selectedPrimaryPrograms.size() > 0) {
			arf.setPrimaryPrograms(new ArrayList(selectedPrimaryPrograms));
			
			arf.setRelatedPrimaryProgs(new HashSet<AmpTheme>() );
			ProgramUtil.collectFilteringInformation(selectedPrimaryPrograms, arf.getPrimaryPrograms(), arf.getRelatedPrimaryProgs() );
			
		} else {
			arf.setPrimaryPrograms(null);
			arf.setSelectedPrimaryPrograms(null);
			arf.setRelatedPrimaryProgs(null);
		}

		Set selectedSecondaryPrograms	= arf.getSelectedSecondaryPrograms();
		if (selectedSecondaryPrograms != null && selectedSecondaryPrograms.size() > 0) {
			arf.setSecondaryPrograms(new ArrayList(selectedSecondaryPrograms));
			
			arf.setRelatedSecondaryProgs( new HashSet<AmpTheme>() );
			ProgramUtil.collectFilteringInformation(selectedSecondaryPrograms, arf.getSecondaryPrograms(), arf.getRelatedSecondaryProgs() );
			
		} else {
			arf.setSecondaryPrograms(null);
			arf.setSelectedSecondaryPrograms(null);
			arf.setRelatedSecondaryProgs(null);
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
        form.setSelectedTertiarySectors( FilterUtil.getObjectsIds(filter.getSelectedTertiarySectors()));
	
		form.setSelectedNatPlanObj( FilterUtil.getObjectsIds(filter.getSelectedNatPlanObj()) );
		form.setSelectedPrimaryPrograms( FilterUtil.getObjectsIds(filter.getSelectedPrimaryPrograms()) );
		form.setSelectedSecondaryPrograms( FilterUtil.getObjectsIds(filter.getSelectedSecondaryPrograms()) );
		
		form.setRegionSelected( FilterUtil.getObjectsIds( filter.getLocationSelected() ) );
		
		form.setText( filter.getText() );
		form.setIndexString( filter.getIndexText() );
		form.setSearchMode(filter.getSearchMode());
		
		form.setFromYear( filter.getYearFrom()!=null ? filter.getYearFrom().longValue() : null );
		form.setToYear( filter.getYearTo()!=null ? filter.getYearTo().longValue() : null );
		form.setFromMonth( filter.getFromMonth() );
		form.setToMonth( filter.getToMonth() );
		form.setFromDate( filter.getFromDate() );
		form.setToDate( filter.getToDate() );

		if (filter.getCurrency() != null)
			form.setCurrency( filter.getCurrency().getAmpCurrencyId() );
		
		if ( filter.getLineMinRank() != null && filter.getLineMinRank().size() > 0) {
	 		int i = 0;
	 	 	Object[] temp   = new Object[filter.getLineMinRank().size()];
	 	 	for ( Integer intVal:filter.getLineMinRank() ) {
	 	 		temp[i++]       = intVal.toString();
	 	 	}
	 	 	form.setLineMinRanks(temp);
	 	}
	 	if ( filter.getPlanMinRank() != null && filter.getPlanMinRank().size() > 0) {
	 	 	int i = 0;
	 	 	Object[] temp   = new Object[filter.getPlanMinRank().size()];
	 	 	for ( Integer intVal:filter.getPlanMinRank() ) {
	 	 		temp[i++]       = intVal.toString();
	 	 	}
	 	 	form.setPlanMinRanks(temp);
	 	}
	 	if ( filter.getLocationSelected() != null )
	 		form.setRegionSelected ( FilterUtil.getObjectsIds(filter.getLocationSelected()) );
		
		Collection<String> appStatuses		= filter.getApprovalStatusSelected();
		if ( appStatuses!=null && appStatuses.size()>0  ) {
			form.setApprovalStatusSelected( appStatuses.toArray() );
		}
		
		form.setSelectedStatuses( FilterUtil.getObjectsIds(filter.getStatuses()) ) ;

		form.setSelectedProjectCategory( FilterUtil.getObjectsIds(filter.getProjectCategory()) );
		
		form.setSelectedFinancingInstruments( FilterUtil.getObjectsIds(filter.getFinancingInstruments()) ); 

		form.setSelectedTypeOfAssistance( FilterUtil.getObjectsIds(filter.getTypeOfAssistance()) );

		form.setSelectedModeOfPayment( FilterUtil.getObjectsIds(filter.getModeOfPayment()) );
		
		form.setSelectedProjectImplUnit(FilterUtil.getObjectsIds(filter.getProjectImplementingUnits()));

		form.setPageSize( filter.getPageSize() );

		form.setSelectedRisks( FilterUtil.getObjectsIds(filter.getRisks()) ); 
		form.setGovernmentApprovalProcedures( filter.getGovernmentApprovalProcedures() );
		form.setJointCriteria( filter.getJointCriteria() );

		form.setSelectedDonorTypes( FilterUtil.getObjectsIds(filter.getDonorTypes()) );
		form.setSelectedDonorGroups( FilterUtil.getObjectsIds(filter.getDonorGroups()) );

		if (filter.getBudget() != null) {
	 		form.setSelectedBudgets( new Object[1] );
	 	 	form.getSelectedBudgets()[0]    = filter.getBudget()?"1":"0" ;
	 	}
		
		form.setJustSearch( filter.isJustSearch() );
		form.setRenderStartYear( filter.getRenderStartYear()!=null&&filter.getRenderStartYear()>0 ? filter.getRenderStartYear() : -1 );
		form.setRenderEndYear(filter.getRenderEndYear()!=null&& filter.getRenderEndYear()>0 ? filter.getRenderEndYear() : -1 );
		
		form.setSelectedBeneficiaryAgency( FilterUtil.getObjectsIds(filter.getBeneficiaryAgency()) );
		form.setSelectedDonnorAgency( FilterUtil.getObjectsIds(filter.getDonnorgAgency()) );
		form.setSelectedImplementingAgency( FilterUtil.getObjectsIds(filter.getImplementingAgency()) );
		form.setSelectedExecutingAgency( FilterUtil.getObjectsIds(filter.getExecutingAgency()) );
		form.setAmountinthousands(filter.getAmountinthousand());
	}
	
}
