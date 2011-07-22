package org.digijava.module.visualization.form;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.EditActivityForm.ActivityContactInfo;
import org.digijava.module.visualization.helper.DashboardFilter;
import org.digijava.module.visualization.util.Constants;

public class VisualizationForm extends ActionForm {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	
	private DashboardFilter filter = new DashboardFilter();
	private SummaryInformation summaryInformation;
	private RanksInformation ranksInformation;
	private ExportData exportData;
	private Map<AmpActivityVersion, BigDecimal> itemProjectsList;
	
	public Map<AmpActivityVersion, BigDecimal> getItemProjectsList() {
		return itemProjectsList;
	}

	public void setItemProjectsList(
			Map<AmpActivityVersion, BigDecimal> itemProjectsList) {
		this.itemProjectsList = itemProjectsList;
	}

	public void setFilter(DashboardFilter filter) {
		this.filter = filter;
	}

	public DashboardFilter getFilter() {
		return filter;
	}
	

	public void setSummaryInformation(SummaryInformation summaryInformation) {
		this.summaryInformation = summaryInformation;
	}

	public SummaryInformation getSummaryInformation() {
		if(this.summaryInformation==null){
			this.summaryInformation=new SummaryInformation();
		}
		return this.summaryInformation;
	}

	public class SummaryInformation {
		private BigDecimal totalCommitments; 
		private BigDecimal totalDisbursements; 
		private Integer numberOfProjects; 
		private Integer numberOfSectors; 
		private Integer numberOfRegions; 
		private Integer numberOfDonors; 
		private BigDecimal averageProjectSize;

		public BigDecimal getTotalCommitments() {
			return totalCommitments;
		}
		public void setTotalCommitments(BigDecimal totalCommitments) {
			this.totalCommitments = totalCommitments;
		}
		public BigDecimal getTotalDisbursements() {
			return totalDisbursements;
		}
		public void setTotalDisbursements(BigDecimal totalDisbursements) {
			this.totalDisbursements = totalDisbursements;
		}
		public Integer getNumberOfProjects() {
			return numberOfProjects;
		}
		public void setNumberOfProjects(Integer numberOfProjects) {
			this.numberOfProjects = numberOfProjects;
		}
		public Integer getNumberOfSectors() {
			return numberOfSectors;
		}
		public void setNumberOfSectors(Integer numberOfSectors) {
			this.numberOfSectors = numberOfSectors;
		}
		public Integer getNumberOfRegions() {
			return numberOfRegions;
		}
		public void setNumberOfRegions(Integer numberOfRegions) {
			this.numberOfRegions = numberOfRegions;
		}
		public Integer getNumberOfDonors() {
			return numberOfDonors;
		}
		public void setNumberOfDonors(Integer numberOfDonors) {
			this.numberOfDonors = numberOfDonors;
		}
		public BigDecimal getAverageProjectSize() {
			return averageProjectSize;
		}
		public void setAverageProjectSize(BigDecimal averageProjectSize) {
			this.averageProjectSize = averageProjectSize;
		} 

	}
	
	public void setRanksInformation(RanksInformation ranksInformation) {
		this.ranksInformation = ranksInformation;
	}

	public RanksInformation getRanksInformation() {
		if(this.ranksInformation==null){
			this.ranksInformation=new RanksInformation();
		}
		return this.ranksInformation;
	}

	public class RanksInformation {
		private Map<AmpOrganisation, BigDecimal> topDonors;
		private Map<AmpOrganisation, BigDecimal> fullDonors;
		private Map<AmpActivityVersion, BigDecimal> topProjects;
		private Map<AmpActivityVersion, BigDecimal> fullProjects;
		private Map<AmpCategoryValueLocations, BigDecimal> topRegions;
		private Map<AmpCategoryValueLocations, BigDecimal> fullRegions;
		private Map<AmpSector, BigDecimal> topSectors;
		private Map<AmpSector, BigDecimal> fullSectors;
		
		public Map<AmpOrganisation, BigDecimal> getTopDonors() {
			return topDonors;
		}
		public void setTopDonors(Map<AmpOrganisation, BigDecimal> topDonors) {
			this.topDonors = topDonors;
		}
		public Map<AmpOrganisation, BigDecimal> getFullDonors() {
			return fullDonors;
		}
		public void setFullDonors(Map<AmpOrganisation, BigDecimal> fullDonors) {
			this.fullDonors = fullDonors;
			if (fullDonors!=null) {
				this.topDonors = getTop(fullDonors);
			} else {
				this.topDonors = null;
			}
		}
		public Map<AmpActivityVersion, BigDecimal> getTopProjects() {
			return topProjects;
		}
		public void setTopProjects(Map<AmpActivityVersion, BigDecimal> topProjects) {
			this.topProjects = topProjects;
		}
		public Map<AmpActivityVersion, BigDecimal> getFullProjects() {
			return fullProjects;
		}
		public void setFullProjects(Map<AmpActivityVersion, BigDecimal> fullProjects) {
			this.fullProjects = fullProjects;
			if (fullProjects!=null) {
				this.topProjects = getTop(fullProjects);
			} else {
				this.topProjects = null;
			}
		}
		public Map<AmpCategoryValueLocations, BigDecimal> getTopRegions() {
			return topRegions;
		}
		public void setTopRegions(Map<AmpCategoryValueLocations, BigDecimal> topRegions) {
			this.topRegions = topRegions;
		}
		public Map<AmpCategoryValueLocations, BigDecimal> getFullRegions() {
			return fullRegions;
		}
		public void setFullRegions(
				Map<AmpCategoryValueLocations, BigDecimal> fullRegions) {
			this.fullRegions = fullRegions;
			if (fullRegions!=null) {
				this.topRegions = getTop(fullRegions);
			} else {
				this.topRegions = null;
			}
		}
		public Map<AmpSector, BigDecimal> getTopSectors() {
			return topSectors;
		}
		public void setTopSectors(Map<AmpSector, BigDecimal> topSectors) {
			this.topSectors = topSectors;
		}
		public Map<AmpSector, BigDecimal> getFullSectors() {
			return fullSectors;
		}
		public void setFullSectors(Map<AmpSector, BigDecimal> fullSectors) {
			this.fullSectors = fullSectors;
			if (fullSectors!=null) {
				this.topSectors = getTop(fullSectors);
			} else {
				this.topSectors = null;
			}
		}
		
		private Map getTop (Map map){
			int top = Constants.GlobalSettings.TOP_LIMIT;
		    List list = new LinkedList(map.entrySet());
			Map result = new LinkedHashMap();
		    int counter = 0;
		    for (Iterator it = list.iterator(); it.hasNext();) {
		        Map.Entry entry = (Map.Entry)it.next();
		        result.put(entry.getKey(), entry.getValue());
		        counter++;
		        if (counter>=top) {
					break;
				}
		    }
		    return result;
		}
	}

	public void setExportData(ExportData exportData) {
		this.exportData = exportData;
	}

	public ExportData getExportData() {
		if(this.exportData==null){
			this.exportData=new ExportData();
		}
		return this.exportData;
	}

	public class ExportData {
		private BufferedImage fundingGraph;
		private BufferedImage aidPredictabilityGraph;
		private BufferedImage aidTypeGraph;
		private BufferedImage financingInstGraph;
		private BufferedImage donorGraph;
		private BufferedImage sectorGraph;
		private BufferedImage regionGraph;
		private BufferedImage ODAGrowthGraph;
		private String fundingTableData;
		private String aidPredicTableData;
		private String aidTypeTableData;
		private String financingInstTableData;
		private String donorTableData;
		private String sectorTableData;
		private String regionTableData;
		private int fundingOpt = 3;
		private int aidPredicOpt = 3;
		private int aidTypeOpt = 3;
		private int financingInstOpt = 3;
		private int donorOpt = 3;
		private int sectorOpt = 3;
		private int regionOpt = 3;
		private int summaryOpt = 1;	
		private int typeOpt = 0;
		private int ODAGrowthOpt = 1;
		
		public BufferedImage getAidPredictabilityGraph() {
			return aidPredictabilityGraph;
		}

		public void setAidPredictabilityGraph(BufferedImage aidPredictabilityGraph) {
			this.aidPredictabilityGraph = aidPredictabilityGraph;
		}

		public BufferedImage getFundingGraph() {
			return fundingGraph;
		}

		public void setFundingGraph(BufferedImage fundingGraph) {
			this.fundingGraph = fundingGraph;
		}

		public BufferedImage getAidTypeGraph() {
			return aidTypeGraph;
		}

		public void setAidTypeGraph(BufferedImage aidTypeGraph) {
			this.aidTypeGraph = aidTypeGraph;
		}

		public BufferedImage getFinancingInstGraph() {
			return financingInstGraph;
		}

		public void setFinancingInstGraph(BufferedImage financingInstGraph) {
			this.financingInstGraph = financingInstGraph;
		}

		public BufferedImage getDonorGraph() {
			return donorGraph;
		}

		public void setDonorGraph(BufferedImage donorGraph) {
			this.donorGraph = donorGraph;
		}

		public BufferedImage getSectorGraph() {
			return sectorGraph;
		}

		public void setSectorGraph(BufferedImage sectorGraph) {
			this.sectorGraph = sectorGraph;
		}

		public BufferedImage getRegionGraph() {
			return regionGraph;
		}

		public void setRegionGraph(BufferedImage regionGraph) {
			this.regionGraph = regionGraph;
		}

		public BufferedImage getODAGrowthGraph() {
			return ODAGrowthGraph;
		}

		public void setODAGrowthGraph(BufferedImage oDAGrowthGraph) {
			ODAGrowthGraph = oDAGrowthGraph;
		}

		public String getFundingTableData() {
			return fundingTableData;
		}

		public void setFundingTableData(String fundingTableData) {
			this.fundingTableData = fundingTableData;
		}

		public String getAidPredicTableData() {
			return aidPredicTableData;
		}

		public void setAidPredicTableData(String aidPredicTableData) {
			this.aidPredicTableData = aidPredicTableData;
		}

		public String getAidTypeTableData() {
			return aidTypeTableData;
		}

		public void setAidTypeTableData(String aidTypeTableData) {
			this.aidTypeTableData = aidTypeTableData;
		}

		public String getFinancingInstTableData() {
			return financingInstTableData;
		}

		public void setFinancingInstTableData(String financingInstTableData) {
			this.financingInstTableData = financingInstTableData;
		}

		public String getDonorTableData() {
			return donorTableData;
		}

		public void setDonorTableData(String donorTableData) {
			this.donorTableData = donorTableData;
		}

		public String getSectorTableData() {
			return sectorTableData;
		}

		public void setSectorTableData(String sectorTableData) {
			this.sectorTableData = sectorTableData;
		}

		public String getRegionTableData() {
			return regionTableData;
		}

		public void setRegionTableData(String regionTableData) {
			this.regionTableData = regionTableData;
		}

		public int getFundingOpt() {
			return fundingOpt;
		}

		public void setFundingOpt(int fundingOpt) {
			this.fundingOpt = fundingOpt;
		}

		public int getAidPredicOpt() {
			return aidPredicOpt;
		}

		public void setAidPredicOpt(int aidPredicOpt) {
			this.aidPredicOpt = aidPredicOpt;
		}

		public int getAidTypeOpt() {
			return aidTypeOpt;
		}

		public void setAidTypeOpt(int aidTypeOpt) {
			this.aidTypeOpt = aidTypeOpt;
		}

		public int getFinancingInstOpt() {
			return financingInstOpt;
		}

		public void setFinancingInstOpt(int financingInstOpt) {
			this.financingInstOpt = financingInstOpt;
		}

		public int getDonorOpt() {
			return donorOpt;
		}

		public void setDonorOpt(int donorOpt) {
			this.donorOpt = donorOpt;
		}

		public int getSectorOpt() {
			return sectorOpt;
		}

		public void setSectorOpt(int sectorOpt) {
			this.sectorOpt = sectorOpt;
		}

		public int getRegionOpt() {
			return regionOpt;
		}

		public void setRegionOpt(int regionOpt) {
			this.regionOpt = regionOpt;
		}

		public int getSummaryOpt() {
			return summaryOpt;
		}

		public void setSummaryOpt(int summaryOpt) {
			this.summaryOpt = summaryOpt;
		}

		public int getTypeOpt() {
			return typeOpt;
		}

		public void setTypeOpt(int typeOpt) {
			this.typeOpt = typeOpt;
		}

		public int getODAGrowthOpt() {
			return ODAGrowthOpt;
		}

		public void setODAGrowthOpt(int oDAGrowthOpt) {
			ODAGrowthOpt = oDAGrowthOpt;
		} 
	
		
	}
}
