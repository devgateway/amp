package org.digijava.module.aim.ar.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ReportContextData;
import org.dgfoundation.amp.ar.dbentity.AmpFilterData;
import org.dgfoundation.amp.ar.dbentity.FilterDataSetInterface;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.ReportsFilterPickerForm;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FilterUtil {

    private static Logger logger = Logger.getLogger(FilterUtil.class);

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
        return buildFilterFromSource(source, null);
    }

    public static AmpARFilter buildFilterFromSource(FilterDataSetInterface source, TeamMember tm) {
        AmpARFilter arf = new AmpARFilter(tm);
        arf.fillWithDefaultsSettings();
        
        if (source instanceof AmpReports && source != null) {
            arf.fillWithDefaultsFilter(((AmpReports) source).getId());
        } else {
            arf.fillWithDefaultsFilter(null);
        }
        
        FilterUtil.populateFilter(source, arf);
        arf.postprocess();
        return arf;
    }

    public static Long[] getObjectsIds (Collection<? extends Identifiable> col) {
        if ( col == null )
            return null;
        
        Long [] ret = new Long[col.size()];
        Iterator<? extends Identifiable> iter   = col.iterator();
        for (int i=0; iter.hasNext(); i++) {
            Object id   = iter.next().getIdentifier();
            try {
                ret[i]      = Long.parseLong( id.toString() );
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
        
//      if (filter == null)
//      {
//          filter = new AmpARFilter();
//          filter.fillWithDefaultsFilter(ampReportId);
//          filter.fillWithDefaultsSettings();
//      }
        
        // for each sector we have also to add the subsectors
        form.setSelectedSectors( FilterUtil.getObjectsIds(filter.getSelectedSectors()) );
        form.setSelectedSecondarySectors( FilterUtil.getObjectsIds(filter.getSelectedSecondarySectors()) );
        form.setSelectedTertiarySectors( FilterUtil.getObjectsIds(filter.getSelectedTertiarySectors()));
        form.setSelectedQuaternarySectors(FilterUtil.getObjectsIds(filter.getSelectedQuaternarySectors()));
        form.setSelectedQuinarySectors(FilterUtil.getObjectsIds(filter.getSelectedQuinarySectors()));

        form.setSelectedNatPlanObj( FilterUtil.getObjectsIds(filter.getSelectedNatPlanObj()) );
        form.setSelectedPrimaryPrograms( FilterUtil.getObjectsIds(filter.getSelectedPrimaryPrograms()) );
        form.setSelectedSecondaryPrograms( FilterUtil.getObjectsIds(filter.getSelectedSecondaryPrograms()) );
        form.setSelectedTertiaryPrograms(FilterUtil.getObjectsIds(filter.getSelectedTertiaryPrograms()));
        
        form.setRegionSelected( FilterUtil.getObjectsIds( filter.getLocationSelected() ) );
        
        form.setText( filter.getText() );
        form.setIndexString( filter.getIndexText() );
        form.setSearchMode(filter.getSearchMode());
        
        form.setFromYear( filter.getYearFrom()!=null ? filter.getYearFrom().longValue() : null );
        form.setToYear( filter.getYearTo()!=null ? filter.getYearTo().longValue() : null );
        form.setFromMonth( filter.getFromMonth() );
        form.setToMonth( filter.getToMonth() );
        
        form.setFromDate(convertArFilterToUiDate(filter.getFromDate()));
        form.setToDate(convertArFilterToUiDate(filter.getToDate()));
        form.getDynamicDateFilter().setCurrentPeriod(filter.getDynDateFilterCurrentPeriod());
        form.getDynamicDateFilter().setAmount(filter.getDynDateFilterAmount());
        form.getDynamicDateFilter().setOperator(filter.getDynDateFilterOperator());
        form.getDynamicDateFilter().setxPeriod(filter.getDynDateFilterXPeriod());
        
        form.setToActivityStartDate(convertArFilterToUiDate(filter.getToActivityStartDate()));
        form.setFromActivityStartDate(convertArFilterToUiDate(filter.getFromActivityStartDate()));
        form.getDynamicActivityStartFilter().setCurrentPeriod(filter.getDynActivityStartFilterCurrentPeriod());
        form.getDynamicActivityStartFilter().setAmount(filter.getDynActivityStartFilterAmount());
        form.getDynamicActivityStartFilter().setOperator(filter.getDynActivityStartFilterOperator());
        form.getDynamicActivityStartFilter().setxPeriod(filter.getDynActivityStartFilterXPeriod());

        form.setToIssueDate(convertArFilterToUiDate(filter.getToIssueDate()));
        form.setFromIssueDate(convertArFilterToUiDate(filter.getFromIssueDate()));
        form.getDynamicIssueFilter().setCurrentPeriod(filter.getDynIssueFilterCurrentPeriod());
        form.getDynamicIssueFilter().setAmount(filter.getDynIssueFilterAmount());
        form.getDynamicIssueFilter().setOperator(filter.getDynIssueFilterOperator());
        form.getDynamicIssueFilter().setxPeriod(filter.getDynIssueFilterXPeriod());
        
        form.setToActivityActualCompletionDate(convertArFilterToUiDate(filter.getToActivityActualCompletionDate()));
        form.setFromActivityActualCompletionDate(convertArFilterToUiDate(filter.getFromActivityActualCompletionDate()));
        form.getDynamicActivityActualCompletionFilter().setCurrentPeriod(filter.getDynActivityActualCompletionFilterCurrentPeriod());
        form.getDynamicActivityActualCompletionFilter().setAmount(filter.getDynActivityActualCompletionFilterAmount());
        form.getDynamicActivityActualCompletionFilter().setOperator(filter.getDynActivityActualCompletionFilterOperator());
        form.getDynamicActivityActualCompletionFilter().setxPeriod(filter.getDynActivityActualCompletionFilterXPeriod());

        form.setToActualApprovalDate(convertArFilterToUiDate(filter.getToActualApprovalDate()));
        form.setFromActualApprovalDate(convertArFilterToUiDate(filter.getFromActualApprovalDate()));

        form.setToProposedCompletionDate(convertArFilterToUiDate(filter.getToProposedCompletionDate()));
        form.setFromProposedCompletionDate(convertArFilterToUiDate(filter.getFromProposedCompletionDate()));

        form.setToPledgeDetailStartDate(convertArFilterToUiDate(filter.getToPledgeDetailStartDate()));
        form.setFromPledgeDetailStartDate(convertArFilterToUiDate(filter.getFromPledgeDetailStartDate()));
        form.setToPledgeDetailEndDate(convertArFilterToUiDate(filter.getToPledgeDetailEndDate()));
        form.setFromPledgeDetailEndDate(convertArFilterToUiDate(filter.getFromPledgeDetailEndDate()));

        form.setToActivityFinalContractingDate(convertArFilterToUiDate(filter.getToActivityFinalContractingDate()));
        form.setFromActivityFinalContractingDate(convertArFilterToUiDate(filter.getFromActivityFinalContractingDate()));
        form.getDynamicActivityFinalContractingFilter().setCurrentPeriod(filter.getDynActivityFinalContractingFilterCurrentPeriod());
        form.getDynamicActivityFinalContractingFilter().setAmount(filter.getDynActivityFinalContractingFilterAmount());
        form.getDynamicActivityFinalContractingFilter().setOperator(filter.getDynActivityFinalContractingFilterOperator());
        form.getDynamicActivityFinalContractingFilter().setxPeriod(filter.getDynActivityFinalContractingFilterXPeriod());
    
        form.setFromActualApprovalDate(convertArFilterToUiDate(filter.getFromActualApprovalDate()));
        form.setToActualApprovalDate(convertArFilterToUiDate(filter.getToActualApprovalDate()));
        form.getDynamicActualApprovalFilter().setCurrentPeriod(filter.getDynActualApprovalFilterCurrentPeriod());
        form.getDynamicActualApprovalFilter().setAmount(filter.getDynActualApprovalFilterAmount());
        form.getDynamicActualApprovalFilter().setOperator(filter.getDynActualApprovalFilterOperator());
        form.getDynamicActualApprovalFilter().setxPeriod(filter.getDynActualApprovalFilterXPeriod());
        form.setToProposedApprovalDate(convertArFilterToUiDate(filter.getToProposedApprovalDate()));
        form.setFromProposedApprovalDate(convertArFilterToUiDate(filter.getFromProposedApprovalDate()));
        form.setFromProposedStartDate(convertArFilterToUiDate(filter.getFromProposedStartDate()));
        form.setToProposedStartDate(convertArFilterToUiDate(filter.getToProposedStartDate()));
        form.getDynamicProposedApprovalFilter().setCurrentPeriod(filter.getDynProposedApprovalFilterCurrentPeriod());
        form.getDynamicProposedApprovalFilter().setAmount(filter.getDynProposedApprovalFilterAmount());
        form.getDynamicProposedApprovalFilter().setOperator(filter.getDynProposedApprovalFilterOperator());
        form.getDynamicProposedApprovalFilter().setxPeriod(filter.getDynProposedApprovalFilterXPeriod());

        form.setToEffectiveFundingDate(convertArFilterToUiDate(filter.getToEffectiveFundingDate()));
        form.setFromEffectiveFundingDate(convertArFilterToUiDate(filter.getFromEffectiveFundingDate()));
        form.getDynamicEffectiveFundingFilter().setCurrentPeriod(filter.getDynEffectiveFundingFilterCurrentPeriod());
        form.getDynamicEffectiveFundingFilter().setAmount(filter.getDynEffectiveFundingFilterAmount());
        form.getDynamicEffectiveFundingFilter().setOperator(filter.getDynEffectiveFundingFilterOperator());
        form.getDynamicEffectiveFundingFilter().setxPeriod(filter.getDynEffectiveFundingFilterXPeriod());

        form.setToFundingClosingDate(convertArFilterToUiDate(filter.getToFundingClosingDate()));
        form.setFromFundingClosingDate(convertArFilterToUiDate(filter.getFromFundingClosingDate()));
        form.getDynamicFundingClosingFilter().setCurrentPeriod(filter.getDynFundingClosingFilterCurrentPeriod());
        form.getDynamicFundingClosingFilter().setAmount(filter.getDynFundingClosingFilterAmount());
        form.getDynamicFundingClosingFilter().setOperator(filter.getDynFundingClosingFilterOperator());
        form.getDynamicFundingClosingFilter().setxPeriod(filter.getDynFundingClosingFilterXPeriod());
        
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
        
        Collection<String> appStatuses      = filter.getApprovalStatusSelected();
        if ( appStatuses!=null && appStatuses.size()>0  ) {
            form.setApprovalStatusSelected( appStatuses.toArray() );
        }
        
        form.setSelectedStatuses( FilterUtil.getObjectsIds(filter.getStatuses()) ) ;
        
        form.setSelectedExpenditureClasses(FilterUtil.getObjectsIds(filter.getExpenditureClass()));
        form.setSelectedPerformanceAlertLevels(FilterUtil.getObjectsIds(filter.getPerformanceAlertLevel()));
        if (filter.getPerformanceAlertType() != null) {
            form.setSelectedPerformanceAlertTypes(filter.getPerformanceAlertType().toArray());
        }
        form.setSelectedWorkspaces( FilterUtil.getObjectsIds(filter.getNonPrivateWorkspaces()) ) ;

        form.setSelectedProjectCategory( FilterUtil.getObjectsIds(filter.getProjectCategory()) );
        
        form.setSelectedFinancingInstruments( FilterUtil.getObjectsIds(filter.getFinancingInstruments()) );
        
        form.setSelectedFundingStatus( FilterUtil.getObjectsIds(filter.getFundingStatus()) );
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
        form.setSelectedBeneficiaryAgencyTypes(FilterUtil.getObjectsIds(filter.getBeneficiaryAgencyTypes()));
        form.setSelectedBeneficiaryAgencyGroups(FilterUtil.getObjectsIds(filter.getBeneficiaryAgencyGroups()));
        form.setSelectedContractingAgencyTypes(FilterUtil.getObjectsIds(filter.getContractingAgencyTypes()));
        form.setSelectedContractingAgencyGroups(FilterUtil.getObjectsIds(filter.getContractingAgencyGroups()));
        form.setSelectedExecutingAgencyTypes(FilterUtil.getObjectsIds(filter.getExecutingAgencyTypes()));
        form.setSelectedExecutingAgencyGroups(FilterUtil.getObjectsIds(filter.getExecutingAgencyGroups()));
        form.setSelectedImplementingAgencyTypes(FilterUtil.getObjectsIds(filter.getImplementingAgencyTypes()));
        form.setSelectedImplementingAgencyGroups(FilterUtil.getObjectsIds(filter.getImplementingAgencyGroups()));
        form.setSelectedResponsibleOrgTypes(FilterUtil.getObjectsIds(filter.getResponsibleAgencyTypes()));
        form.setSelectedResponsibleOrgGroups(FilterUtil.getObjectsIds(filter.getResponsibleAgencyGroups()));

        form.setSelectedBudgets(FilterUtil.getObjectsIds(filter.getBudget()));
        
        form.setJustSearch( filter.isJustSearch() );
        form.setRenderStartYear( filter.getRenderStartYear()!=null&&filter.getRenderStartYear()>0 ? filter.getRenderStartYear() : -1 );
        form.setRenderEndYear(filter.getRenderEndYear()!=null&& filter.getRenderEndYear()>0 ? filter.getRenderEndYear() : -1 );
        
        form.setSelectedBeneficiaryAgency( FilterUtil.getObjectsIds(filter.getBeneficiaryAgency()) );
        form.setSelectedDonnorAgency( FilterUtil.getObjectsIds(filter.getDonnorgAgency()) );
        form.setSelectedImplementingAgency( FilterUtil.getObjectsIds(filter.getImplementingAgency()) );
        form.setSelectedExecutingAgency( FilterUtil.getObjectsIds(filter.getExecutingAgency()) );
        form.setSelectedresponsibleorg(FilterUtil.getObjectsIds(filter.getResponsibleorg()) );
        form.setSelectedComponentFundingOrg(FilterUtil.getObjectsIds(filter.getComponentFunding()) );
        form.setSelectedComponentSecondResponsibleOrg(FilterUtil.getObjectsIds(filter.getComponentSecondResponsible()) );
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

        form.setComputedYear(filter.getComputedYear() != null ? filter.getComputedYear() : -1);
        
        form.getUndefinedOptions().addAll(filter.getUndefinedOptions());
        
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
        Set<AmpSector> selectedQuaternarySectors = arf.getSelectedQuaternarySectors();
        Set<AmpSector> selectedQuinarySectors = arf.getSelectedQuinarySectors();
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
        if (selectedQuaternarySectors != null && selectedQuaternarySectors.size() > 0) {
            arf.setQuaternarySectors(SectorUtil.getSectorDescendents(selectedQuaternarySectors));
            arf.setQuaternarySectorsAndAncestors(
                    makeUnion(arf.getQuaternarySectors(), SectorUtil.getAmpParentSectors(selectedQuaternarySectors)));
        } else {
            arf.setQuaternarySectors(null);
            arf.setSelectedQuaternarySectors(null);
            arf.setQuaternarySectorsAndAncestors(null);
        }
        if (selectedQuinarySectors != null && selectedQuinarySectors.size() > 0) {
            arf.setQuinarySectors(SectorUtil.getSectorDescendents(selectedQuinarySectors));
            arf.setQuinarySectorsAndAncestors(
                    makeUnion(arf.getQuinarySectors(), SectorUtil.getAmpParentSectors(selectedQuinarySectors)));
        } else {
            arf.setQuinarySectors(null);
            arf.setSelectedQuinarySectors(null);
            arf.setQuinarySectorsAndAncestors(null);
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
        Set<AmpTheme> selectedTertiaryPrograms = arf.getSelectedTertiaryPrograms();
        
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
    
        if (selectedTertiaryPrograms != null && selectedTertiaryPrograms.size() > 0) {
            arf.setTertiaryPrograms(new ArrayList<>(selectedTertiaryPrograms));
            arf.setRelatedTertiaryProgs(new HashSet<>());
            ProgramUtil.collectFilteringInformation(selectedTertiaryPrograms, arf.getTertiaryPrograms(),
                    arf.getRelatedTertiaryProgs());
        } else {
            arf.setTertiaryPrograms(null);
            arf.setSelectedTertiaryPrograms(null);
            arf.setRelatedTertiaryProgs(null);
        }
    }

    public static String convertUiToArFilterDate(String date) {
        String arPattern = AmpARFilter.SDF_IN_FORMAT_STRING;
        String uiPattern = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT);
        return convertDateFormat(date, uiPattern, arPattern);
    }

    private static String convertArFilterToUiDate(String date) {
        String arPattern = AmpARFilter.SDF_IN_FORMAT_STRING;
        String uiPattern = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT);
        return convertDateFormat(date, arPattern, uiPattern);
    }

    private static String convertDateFormat(String date, String parsePattern, String formatPattern) {
        if (StringUtils.isBlank(date)) {
            return date;
        } else {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(formatPattern);
                SimpleDateFormat parser = new SimpleDateFormat(parsePattern);
                return formatter.format(parser.parse(date));
            } catch (ParseException e) {
                logger.error(String.format("Date formatted incorrectly %s, expected format is %s.", date, parsePattern), e);
                return null;
            }
        }
    }
}

