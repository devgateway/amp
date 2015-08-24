package org.digijava.module.aim.ar.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ReportContextData;
import org.dgfoundation.amp.ar.dbentity.AmpFilterData;
import org.dgfoundation.amp.ar.dbentity.FilterDataSetInterface;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.ReportsFilterPickerForm;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;

public class FilterUtil {
	
	/**
	 * pumps data from the form to the filter
	 * @param report
	 * @param filter
	 */
	public static void populateFilter(FilterDataSetInterface<? extends AmpFilterData> report, AmpARFilter filter) {
		if ( report.getFilterDataSet()!=null && report.getFilterDataSet().size()>0 ) {
			for(AmpFilterData item:report.getFilterDataSet()){
				try {
					item.populateField(filter);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * @param filterForm
	 * @return
	 */
	public static AmpARFilter getOrCreateFilter(Long forcedAmpReportId, FilterDataSetInterface dataSource)
	{		
		AmpARFilter arf = ReportContextData.getFromRequest().getFilter();

		if (arf == null)
		{			
			ReportContextData.getFromRequest().setSerializedFilter(buildFilter(dataSource, forcedAmpReportId));
			ReportContextData.getFromRequest().setFilter(buildFilter(dataSource, forcedAmpReportId));
		}
		
		return ReportContextData.getFromRequest().getFilter();
	}
	
	/**
	 * <b>the birthplace of filters in AMP</b>
	 * builds an AmpARFilter instance from scratch off a source or just initializes it, if source is null
	 * @param source: the source to copy the data off or null if just init needed
	 * @return
	 */
	public static AmpARFilter buildFilter(FilterDataSetInterface source, Long forcedAmpReportId)
	{
		AmpARFilter arf = new AmpARFilter();
		arf.readRequestData(TLSUtils.getRequest(), AmpARFilter.FILTER_SECTION_ALL, forcedAmpReportId);
		arf.rememberDefaultValues();
		if (source != null)
			FilterUtil.populateFilter(source, arf);
		/* The prepare function needs to have the filter (af) already populated */
		arf.postprocess();
		return arf;
	}
	
	public static AmpARFilter buildFilterFromSource(FilterDataSetInterface source) {
		AmpARFilter arf = new AmpARFilter();
		arf.fillWithDefaultsSettings();
		arf.fillWithDefaultsFilter(null);
		FilterUtil.populateFilter(source, arf);
		arf.postprocess();
		return arf;
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
	
	/**
	 * copies the values of a collection into an array. Returns an empty array if the collection is null
	 * @param col
	 * @return
	 */
	public static Object[] collectValues(Collection<?> col) {
		if (col == null)
			return null;
		
		Object[] res = new Object[col.size()];
		int i = 0;
		for(Object obj:col) {
			res[i] = obj;
			i++;
		}
		return res;
	}
	
	/**
	 * pumps data from the filter to the form
	 * @param form
	 * @param filter
	 * @param ampReportId - the ampReportId to be used on the newly-generated ampReportId, if filter is null. Can be null if filter != null
	 * @return the used filter (#filter if not null, the generated default value else)
	 */
	public static AmpARFilter populateForm(ReportsFilterPickerForm form, AmpARFilter filter, Long ampReportId) 
	{
		
//		if (filter == null)
//		{
//			filter = new AmpARFilter();
//			filter.fillWithDefaultsFilter(ampReportId);
//			filter.fillWithDefaultsSettings();
//		}
		
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
		
		form.setFromDate(filter.getFromDate());
		form.setToDate(filter.getToDate());
		form.getDynamicDateFilter().setCurrentPeriod(filter.getDynDateFilterCurrentPeriod());
		form.getDynamicDateFilter().setAmount(filter.getDynDateFilterAmount());
		form.getDynamicDateFilter().setOperator(filter.getDynDateFilterOperator());
		form.getDynamicDateFilter().setxPeriod(filter.getDynDateFilterXPeriod());
		
		form.setToActivityStartDate(filter.getToActivityStartDate());
		form.setFromActivityStartDate(filter.getFromActivityStartDate());
		form.getDynamicActivityStartFilter().setCurrentPeriod(filter.getDynActivityStartFilterCurrentPeriod());
		form.getDynamicActivityStartFilter().setAmount(filter.getDynActivityStartFilterAmount());
		form.getDynamicActivityStartFilter().setOperator(filter.getDynActivityStartFilterOperator());
		form.getDynamicActivityStartFilter().setxPeriod(filter.getDynActivityStartFilterXPeriod());
		
		form.setToActivityActualCompletionDate(filter.getToActivityActualCompletionDate() );
		form.setFromActivityActualCompletionDate(filter.getFromActivityActualCompletionDate());
		form.getDynamicActivityActualCompletionFilter().setCurrentPeriod(filter.getDynActivityActualCompletionFilterCurrentPeriod());
		form.getDynamicActivityActualCompletionFilter().setAmount(filter.getDynActivityActualCompletionFilterAmount());
		form.getDynamicActivityActualCompletionFilter().setOperator(filter.getDynActivityActualCompletionFilterOperator());
		form.getDynamicActivityActualCompletionFilter().setxPeriod(filter.getDynActivityActualCompletionFilterXPeriod());

		form.setToActivityFinalContractingDate(filter.getToActivityFinalContractingDate());
		form.setFromActivityFinalContractingDate(filter.getFromActivityFinalContractingDate());
		form.getDynamicActivityFinalContractingFilter().setCurrentPeriod(filter.getDynActivityFinalContractingFilterCurrentPeriod());
		form.getDynamicActivityFinalContractingFilter().setAmount(filter.getDynActivityFinalContractingFilterAmount());
		form.getDynamicActivityFinalContractingFilter().setOperator(filter.getDynActivityFinalContractingFilterOperator());
		form.getDynamicActivityFinalContractingFilter().setxPeriod(filter.getDynActivityFinalContractingFilterXPeriod());
		
		form.setToProposedApprovalDate(filter.getToProposedApprovalDate());
		form.setFromProposedApprovalDate(filter.getFromProposedApprovalDate());
		form.getDynamicProposedApprovalFilter().setCurrentPeriod(filter.getDynProposedApprovalFilterCurrentPeriod());
		form.getDynamicProposedApprovalFilter().setAmount(filter.getDynProposedApprovalFilterAmount());
		form.getDynamicProposedApprovalFilter().setOperator(filter.getDynProposedApprovalFilterOperator());
		form.getDynamicProposedApprovalFilter().setxPeriod(filter.getDynProposedApprovalFilterXPeriod());
		
		form.setCurrency(filter.getUsedCurrency().getAmpCurrencyId());
		form.setDefaultCurrency(AmpARFilter.getDefaultCurrency().getAmpCurrencyId());
		form.setCalendar(filter.getCalendarType().getAmpFiscalCalId());
		form.setSelectedActivityPledgesSettings(filter.getSelectedActivityPledgesSettings().toString());
		
		if ( filter.getLineMinRank() != null && filter.getLineMinRank().size() > 0) {
	 		int i = 0;
	 	 	Object[] temp   = new Object[filter.getLineMinRank().size()];
	 	 	for ( Integer intVal:filter.getLineMinRank() ) {
	 	 		temp[i++]       = intVal.toString();
	 	 	}
	 	 	form.setLineMinRanks(temp);
	 	}

	 	if ( filter.getLocationSelected() != null )
	 		form.setRegionSelected ( FilterUtil.getObjectsIds(filter.getLocationSelected()) );
		
		Collection<String> appStatuses		= filter.getApprovalStatusSelected();
		if ( appStatuses!=null && appStatuses.size()>0  ) {
			form.setApprovalStatusSelected( appStatuses.toArray() );
		}
		
		form.setSelectedStatuses( FilterUtil.getObjectsIds(filter.getStatuses()) ) ;

		form.setSelectedWorkspaces( FilterUtil.getObjectsIds(filter.getWorkspaces()) ) ;

		form.setSelectedProjectCategory( FilterUtil.getObjectsIds(filter.getProjectCategory()) );
		
		form.setSelectedFinancingInstruments( FilterUtil.getObjectsIds(filter.getFinancingInstruments()) );
		form.setSelectedAidModalities(FilterUtil.getObjectsIds(filter.getAidModalities()));

		form.setSelectedTypeOfAssistance( FilterUtil.getObjectsIds(filter.getTypeOfAssistance()) );

		form.setSelectedModeOfPayment( FilterUtil.getObjectsIds(filter.getModeOfPayment()) );
		
		form.setSelectedProjectImplUnit(FilterUtil.getObjectsIds(filter.getProjectImplementingUnits()));

		form.setPageSize( filter.getPageSize() );

		form.setSelectedRisks( FilterUtil.getObjectsIds(filter.getRisks()) ); 
		form.setGovernmentApprovalProcedures( filter.getGovernmentApprovalProcedures() );
		form.setJointCriteria( filter.getJointCriteria() );

		form.setSelectedDonorTypes( FilterUtil.getObjectsIds(filter.getDonorTypes()) );
		form.setSelectedDonorGroups( FilterUtil.getObjectsIds(filter.getDonorGroups()) );
		form.setSelectedContractingAgencyGroups( FilterUtil.getObjectsIds(filter.getContractingAgencyGroups()) );

		form.setSelectedBudgets(FilterUtil.getObjectsIds(filter.getBudget()));
		
		form.setJustSearch( filter.isJustSearch() );
		form.setRenderStartYear( filter.getRenderStartYear()!=null&&filter.getRenderStartYear()>0 ? filter.getRenderStartYear() : -1 );
		form.setRenderEndYear(filter.getRenderEndYear()!=null&& filter.getRenderEndYear()>0 ? filter.getRenderEndYear() : -1 );
		
		form.setSelectedBeneficiaryAgency( FilterUtil.getObjectsIds(filter.getBeneficiaryAgency()) );
		form.setSelectedDonnorAgency( FilterUtil.getObjectsIds(filter.getDonnorgAgency()) );
		form.setSelectedImplementingAgency( FilterUtil.getObjectsIds(filter.getImplementingAgency()) );
		form.setSelectedExecutingAgency( FilterUtil.getObjectsIds(filter.getExecutingAgency()) );
		form.setSelectedresponsibleorg(FilterUtil.getObjectsIds(filter.getResponsibleorg()) );
		form.setSelectedContractingAgency( FilterUtil.getObjectsIds(filter.getContractingAgency()) );
		
		form.setAmountinthousands(filter.computeEffectiveAmountInThousand());			
		
		if ( filter.getGroupingseparator() != null ) {
			form.setCustomGroupCharacter("CUSTOM");
			form.setCustomGroupCharacterTxt(filter.getGroupingseparator().toString() );
		}
		if ( filter.getDecimalseparator() != null ) {
			form.setCustomDecimalSymbol("CUSTOM");
			form.setCustomDecimalSymbolTxt(filter.getDecimalseparator().toString() );
		}
		if (filter.getMaximumFractionDigits() != null  ) {
			if ( filter.getMaximumFractionDigits() > 5 ) {
				form.setCustomDecimalPlaces( -2 );
				form.setCustomDecimalPlacesTxt(filter.getMaximumFractionDigits());
			}
			else 
				form.setCustomDecimalPlaces(filter.getMaximumFractionDigits());
		}
		if ( filter.getCustomusegroupings() != null ){
			form.setCustomUseGrouping(filter.getCustomusegroupings());
		}
		if ( filter.getGroupingsize() != null ){
			form.setCustomGroupSize(filter.getGroupingsize());
		}
		form.setSelectedHumanitarianAid(collectValues(filter.getHumanitarianAid()));
		form.setSelectedDisasterResponse(collectValues(filter.getDisasterResponse()));
		
		return filter;
	}
	
	/**
	 * make union of two sets
	 * @param a
	 * @param b
	 * @return
	 */
	public static Set<AmpSector> makeUnion(Set<AmpSector> a, Set<AmpSector> b)
	{
		HashSet<AmpSector> res = new HashSet<AmpSector>(a);
		res.addAll(b);
		return res;
	}
	
	/**
	 * add subsectors for each selected sector
	 * @param arf
	 */
	public static void postprocessFilterSectors(AmpARFilter arf)
	{
		Set<AmpSector> selectedSectors = arf.getSelectedSectors();
		Set<AmpSector> selectedSecondarySectors = arf.getSelectedSecondarySectors();
		Set<AmpSector> selectedTertiarySectors = arf.getSelectedTertiarySectors();
		Set<AmpSector> selectedTagSectors = arf.getSelectedTagSectors();
		
		// for each sector we have also to add the subsectors
		if (selectedSectors != null && selectedSectors.size() > 0) {
			arf.setSectors(SectorUtil.getSectorDescendents(selectedSectors));
			arf.setSectorsAndAncestors(makeUnion(arf.getSectors(), SectorUtil.getAmpParentSectors(selectedSectors)));
		} else {
			arf.setSectors(null);
			arf.setSelectedSectors(null);
			arf.setSectorsAndAncestors(null);
		}

		if (selectedSecondarySectors != null && selectedSecondarySectors.size() > 0) {
			arf.setSecondarySectors(SectorUtil.getSectorDescendents(selectedSecondarySectors));
			arf.setSecondarySectorsAndAncestors(makeUnion(arf.getSecondarySectors(), SectorUtil.getAmpParentSectors(selectedSecondarySectors)));
		} else {
			arf.setSecondarySectors(null);
			arf.setSelectedSecondarySectors(null);
			arf.setSecondarySectorsAndAncestors(null);
		}
        if (selectedTertiarySectors != null && selectedTertiarySectors.size() > 0) {
            arf.setTertiarySectors(SectorUtil.getSectorDescendents(selectedTertiarySectors));
            arf.setTertiarySectorsAndAncestors(makeUnion(arf.getTertiarySectors(), SectorUtil.getAmpParentSectors(selectedTertiarySectors)));
        } else {
            arf.setTertiarySectors(null);
            arf.setSelectedTertiarySectors(null);
            arf.setTertiarySectorsAndAncestors(null);
        }
        	
    	if (selectedTagSectors != null && selectedTagSectors.size() > 0) {
			arf.setTagSectors(SectorUtil.getSectorDescendents(selectedTagSectors));
			arf.setTagSectorsAndAncestors(makeUnion(arf.getTagSectors(), SectorUtil.getAmpParentSectors(selectedTagSectors)));
		} else {
			arf.setTagSectors(null);
			arf.setSelectedTagSectors(null);
			arf.setTagSectorsAndAncestors(null);
		}
	}
	
	public static void postprocessFilterPrograms(AmpARFilter arf)
	{
		Set<AmpTheme> selectedNatPlanObj = arf.getSelectedNatPlanObj();
		Set<AmpTheme> selectedPrimaryPrograms = arf.getSelectedPrimaryPrograms();
		Set<AmpTheme> selectedSecondaryPrograms = arf.getSelectedSecondaryPrograms();
		
		if (selectedNatPlanObj != null && selectedNatPlanObj.size() > 0) {
			arf.setNationalPlanningObjectives( new ArrayList<AmpTheme>(selectedNatPlanObj) );			
			arf.setRelatedNatPlanObjs(new HashSet<AmpTheme>() );
			ProgramUtil.collectFilteringInformation(selectedNatPlanObj, arf.getNationalPlanningObjectives(), arf.getRelatedNatPlanObjs() );			
		} else {
			arf.setSelectedNatPlanObj(null);
			arf.setNationalPlanningObjectives(null);
			arf.setRelatedNatPlanObjs(null);
		}

		if (selectedPrimaryPrograms != null && selectedPrimaryPrograms.size() > 0) {
			arf.setPrimaryPrograms(new ArrayList<AmpTheme>(selectedPrimaryPrograms));			
			arf.setRelatedPrimaryProgs(new HashSet<AmpTheme>() );
			ProgramUtil.collectFilteringInformation(selectedPrimaryPrograms, arf.getPrimaryPrograms(), arf.getRelatedPrimaryProgs() );
			
		} else {
			arf.setPrimaryPrograms(null);
			arf.setSelectedPrimaryPrograms(null);
			arf.setRelatedPrimaryProgs(null);
		}

		if (selectedSecondaryPrograms != null && selectedSecondaryPrograms.size() > 0) {
			arf.setSecondaryPrograms(new ArrayList<AmpTheme>(selectedSecondaryPrograms));			
			arf.setRelatedSecondaryProgs( new HashSet<AmpTheme>() );
			ProgramUtil.collectFilteringInformation(selectedSecondaryPrograms, arf.getSecondaryPrograms(), arf.getRelatedSecondaryProgs() );		
		} else {
			arf.setSecondaryPrograms(null);
			arf.setSelectedSecondaryPrograms(null);
			arf.setRelatedSecondaryProgs(null);
		}
	}
}

