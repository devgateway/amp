<%@page import="org.dgfoundation.amp.visibility.data.MeasuresVisibility"%>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<module:display name="ADMIN" parentModule="Messaging System"></module:display> 
<module:display name="ADMINISTRATIVE SECTION"></module:display>
<module:display name="Template Documents Manager" parentModule="ADMINISTRATIVE SECTION"></module:display>
<module:display name="Resource Manager" parentModule="ADMINISTRATIVE SECTION"></module:display>
<module:display name="Activity Approval Process" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Structures Importer" parentModule="ADMINISTRATIVE SECTION"></module:display>
<module:display name="Currency deflator" parentModule="ADMINISTRATIVE SECTION"></module:display>
<module:display name="Dashboards Manager" parentModule="ADMINISTRATIVE SECTION"/>
<module:display name="Activity Levels" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="Add & Edit Activity" parentModule="PARIS INDICATORS"></module:display> 
<module:display name="Admin Home" parentModule="PARIS INDICATORS"></module:display>
<module:display name="Calendar" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Channel Overview" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Components Resume" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Components" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Contact Information" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Contracting" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Cross Cutting Issues" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Document" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Funding" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="Pledges" parentModule="PROJECT MANAGEMENT"/>
<module:display name="/GIS"></module:display>

<module:display  name="/Dashboards"></module:display>
<feature:display name="Top Donors" module="/Dashboards"/>
<field:display name="Show Names As Acronyms" feature="Top Donors"/>
<feature:display name="Top Regions" module="/Dashboards"/>
<feature:display name="Top Sectors" module="/Dashboards"/>
<feature:display name="Top Donor Group" module="/Dashboards"/>
<feature:display name="Aid Predictability" module="/Dashboards"/>
<feature:display name="Funding Type" module="/Dashboards"/>
<feature:display name="Responsible Organizations" module="/Dashboards"/>
<feature:display name="Beneficiary Agencies" module="/Dashboards"/>
<feature:display name="Executing Agencies" module="/Dashboards"/>
<feature:display name="Implementing Agencies" module="/Dashboards"/>
<feature:display name="Peace-building and State-building Goals" module="/Dashboards"/>
<feature:display name="Sector Fragmentation" module="/Dashboards"/>
<feature:display name="Location Fragmentation" module="/Dashboards"/>
<feature:display name="Program Fragmentation" module="/Dashboards"/>
<feature:display name="NDD Dashboard" module="/Dashboards"/>

<module:display name="HELP"></module:display> 
<module:display name="Issues" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="M & E" parentModule="MONITORING AND EVALUATING"></module:display> 
<module:display name="MONITORING AND EVALUATING"></module:display> 
<module:display name="Measures" parentModule="REPORTING"></module:display> 
<module:display name="Messages" parentModule="Messaging System"></module:display> 
<module:display name="Messaging System"></module:display> 
<module:display name="NATIONAL PLAN DASHBOARD"></module:display> 
<module:display name="National Planning Dashboard" parentModule="NATIONAL PLAN DASHBOARD"></module:display> 
<module:display name="PARIS INDICATORS"></module:display> 
<module:display name="Aid Effectiveness Section" parentModule="PROJECT MANAGEMENT"></module:display>
<feature:display name="Aid Effectiveness" module="Aid Effectiveness Section"></feature:display>
<module:display name="PI Reports" parentModule="REPORTING"></module:display>
<module:display name="PROJECT MANAGEMENT"></module:display> 
<module:display name="Organizations" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Paris Indicators" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Portfolio" parentModule="PARIS INDICATORS"></module:display> 
<module:display name="Previews" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Program" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Project ID and Planning" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="Public View Content" parentModule="ADMINISTRATIVE SECTION"></module:display> 
<module:display name="REPORTING"></module:display> 
<module:display name="References" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Report Generator" parentModule="REPORTING"></module:display>



<module:display name="Pledge Reports" parentModule="REPORTING"></module:display>
<feature:display name="Pledges Default Reports" module="Pledge Reports"></feature:display>
<module:display name="Report Types" parentModule="REPORTING"></module:display> 
<module:display name="Scenarios" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="TREND ANALYSIS"></module:display> 
<module:display name="Tab Generator" parentModule="REPORTING"></module:display>
<feature:display name="Enable Saiku icon in Tab Manager" module="Tab Generator"></feature:display>
<module:display name="Trend Analysis and Forecasting" parentModule="TREND ANALYSIS"></module:display>
<module:display name="Tab Generator" parentModule="REPORTING"></module:display>
<module:display name="Trend Analysis and Forecasting" parentModule="TREND ANALYSIS"></module:display>
<module:display name="Global Permission Manager" parentModule="ADMINISTRATIVE SECTION"></module:display>
<module:display name="Budget Manager" parentModule="ADMINISTRATIVE SECTION"></module:display>
<module:display name="Indicator Manager" parentModule="ADMINISTRATIVE SECTION"></module:display>
<module:display name="Structure Types Manager" parentModule="ADMINISTRATIVE SECTION"></module:display>
<module:display name="Aid Effectiveness Indicators Manager" parentModule="ADMINISTRATIVE SECTION"></module:display>
<module:display name="Indicator Layer Manager" parentModule="ADMINISTRATIVE SECTION"></module:display>
<module:display name="Program Mapping Manager" parentModule="ADMINISTRATIVE SECTION"></module:display>
<feature:display module="Contact Information" name="Government Contact Information"></feature:display>
<feature:display module="Contracting" name="Contracting"></feature:display> 
<feature:display module="Funding" name="Commitments"></feature:display> 
<feature:display module="Funding" name="Disbursement Orders"></feature:display> 
<feature:display module="Funding" name="Disbursement"></feature:display> 
<feature:display module="Funding" name="Expenditures"></feature:display> 
<feature:display module="Funding" name="Funding Information"></feature:display> 
<feature:display module="Funding" name="MTEF Projections"></feature:display> 
<feature:display module="Funding" name="Undisbursed Balance"></feature:display> 
<feature:display module="Project ID and Planning" name="Identification"></feature:display> 
<feature:display module="Project ID and Planning" name="Location"></feature:display> 
<feature:display module="Project ID and Planning" name="Planning"></feature:display> 
<feature:display module="Project ID and Planning" name="Sectors"></feature:display>
<feature:display module="Project ID and Planning" name="Budget"></feature:display> 
<feature:display name="About AMP" module="HELP"></feature:display>
<feature:display name="Activity - Component Step" module="Components"></feature:display>
 <feature:display name="Activity Dashboard" module="M & E"></feature:display>

<module:display name="Map configuration" parentModule="ADMINISTRATIVE SECTION"></module:display>
<module:display name="Aid Effectiveness Indicators Manager" parentModule="ADMINISTRATIVE SECTION"></module:display>

<feature:display name="Activity" module="M & E"></feature:display> 

<feature:display name="Beneficiary Agency" module="Organizations"></feature:display> 
<feature:display name="Contracting Agency" module="Organizations"></feature:display> 
<feature:display name="Executing Agency" module="Organizations"></feature:display>
<feature:display name="Implementing Agency" module="Organizations"></feature:display> 
<feature:display name="Regional Group" module="Organizations"></feature:display> 
<feature:display name="Responsible Organization" module="Organizations"></feature:display> 
<feature:display name="Sector Group" module="Organizations"></feature:display> 

<feature:display name="AddMessageButton" module="Messages"></feature:display> 
<feature:display name="Admin - Component Type" module="Components"></feature:display> 
<feature:display name="Admin - Component" module="Components"></feature:display> 
<feature:display name="Admin Help" module="HELP"></feature:display> 
<feature:display name="Admin NPD" module="National Planning Dashboard"></feature:display> 
<feature:display name="Admin Topics Help" module="ADMINISTRATIVE SECTION"></feature:display> 
<feature:display name="Admin" module="M & E"></feature:display> 
<feature:display name="Alert tab" module="Messages"></feature:display> 
<feature:display name="Alert tab" module="Messages"></feature:display> 
<feature:display name="Applied Patches" module="ADMINISTRATIVE SECTION"></feature:display> 
<feature:display name="Approval Tab" module="Messages"></feature:display> 
<feature:display name="Approval Tab" module="Messages"></feature:display> 
<feature:display name="Calendar" module="Calendar"></feature:display> 
<feature:display name="Channel Overview" module="Channel Overview"></feature:display> 
<feature:display name="Component Report" module="Report Types"></feature:display> 
<feature:display name="Components" module="Components"></feature:display> 
<feature:display name="Contracting" module="Contracting"></feature:display> 
<feature:display name="Create Message Form" module="Messages"></feature:display>
<feature:display name="Cross Cutting Issues" module="Cross Cutting Issues"></feature:display> 
<feature:display name="Disbursement Orders" module="Funding"></feature:display> 
<feature:display name="Documents Tab" module="Document"></feature:display> 
<feature:display name="Donor Contact Information" module="Contact Information"></feature:display> 
<feature:display name="Donor Report" module="Report Types"></feature:display> 
<feature:display name="Edit Activity" module="Previews"></feature:display> 
<feature:display name="Enable Scrolling Reports" module="Report Generator"></feature:display> 
<feature:display name="Event Tab" module="Messages"></feature:display> 
<feature:display name="Filter Button" module="Report Generator"></feature:display>
<feature:display name="Save and Open Button"  module="Report Generator"></feature:display>
<feature:display name="Also show pledges checkbox" module="Report and Tab Options"></feature:display>
<feature:display name="Public View Checkbox" module="Report and Tab Options"></feature:display>
<feature:display name="Financial Progress Tab" module="Funding"></feature:display> 
<feature:display name="Funding Information" module="Funding"></feature:display> 
<feature:display name="Government Contact Information" module="Contact Information"></feature:display> 
<feature:display name="Issues" module="Issues"></feature:display>
<feature:display name="Level Links" module="Activity Levels"></feature:display>
<feature:display name="Logframe" module="Previews"></feature:display> 
<feature:display name="Message Manager" module="ADMIN"></feature:display> 
<feature:display name="Message tab" module="Messages"></feature:display> 
<feature:display name="Message tab" module="Messages"></feature:display> 
<feature:display name="My Messages" module="Messages"></feature:display>
<feature:display name="NPD Dashboard" module="National Planning Dashboard"></feature:display> 
<feature:display name="NPD Programs" module="National Planning Dashboard"></feature:display> 
<feature:display name="New Region Manager" module="ADMINISTRATIVE SECTION"></feature:display> 
<feature:display name="PI report 10a" module="PI Reports"></feature:display> 
<feature:display name="PI report 3" module="PI Reports"></feature:display> 
<feature:display name="PI report 4" module="PI Reports"></feature:display> 
<feature:display name="PI report 5a" module="PI Reports"></feature:display> 
<feature:display name="PI report 5b" module="PI Reports"></feature:display> 
<feature:display name="PI report 6" module="PI Reports"></feature:display> 
<feature:display name="PI report 7" module="PI Reports"></feature:display> 
<feature:display name="PI report 9" module="PI Reports"></feature:display> 
<feature:display name="Paris Indicator" module="Add & Edit Activity"></feature:display> 
<feature:display name="Paris Indicators Reports" module="Portfolio"></feature:display> 
<feature:display name="Paris Indicators Targets Manager" module="Admin Home"></feature:display> 
<feature:display name="Paris Indicators" module="Paris Indicators"></feature:display> 
<feature:display name="Physical Progress" module="Components"></feature:display> 
<feature:display name="Portfolio Dashboard" module="M & E"></feature:display> 
<feature:display name="Add Pledge Button" module="Pledges"></feature:display> 
<feature:display name="Pledge Donor Information" module="Pledges"></feature:display> 
<feature:display name="Pledge Sector and Location" module="Pledges"></feature:display>
<feature:display name="Pledge Status" module="Pledges"></feature:display> 
<feature:display name="Pledge Contact 1" module="Pledges"></feature:display> 
<feature:display name="Pledge Contact 2" module="Pledges"></feature:display> 
<feature:display name="Pledge Funding" module="Pledges"></feature:display> 
<feature:display name="Pledge Additional Information" module="Pledges"></feature:display>
<feature:display name="Pledge Attached Files" module="Pledges"></feature:display> 
<feature:display name="Preview Activity" module="Previews"></feature:display> 
<feature:display name="Program" module="Program"></feature:display> 
<feature:display name="Project Coordinator Contact Information" module="Contact Information"></feature:display> 
<feature:display name="Project Fiche" module="Previews"></feature:display> 
<feature:display name="Proposed Project Cost" module="Funding"></feature:display>
<feature:display name="Regional Funding" module="Funding"></feature:display> 
<feature:display name="Regional Report" module="Report Types"></feature:display> 
<feature:display name="Related Documents" module="Document"></feature:display> 
<feature:display name="Reports Contact Information" module="Contact Information"></feature:display> 
<feature:display name="Save Filters from Desktop" module="Report Generator"></feature:display> 
<feature:display name="Sector Ministry Contact Information" module="Contact Information"></feature:display> 
<feature:display name="Target Value" module="PI Reports"></feature:display>

<feature:display name="User Help" module="HELP"></feature:display> 
<feature:display name="Web Resources" module="Document"></feature:display> 
<field:display feature="Disbursement Orders" name="Disbursement Orders Tab"></field:display>
<field:display name="Donor Email" feature="Donor Contact Information"></field:display> 
<field:display name="Donor First Name" feature="Donor Contact Information"></field:display> 
<field:display feature="Edit Activity" name="Edit Activity Button"></field:display> 
<field:display feature="Edit Activity" name="Validate Activity Button"></field:display> 
<field:display feature="Funding Information" name="Type Of Assistance"></field:display> 
<field:display feature="Funding Information" name="Funding Classification Date"></field:display>
<field:display feature="Funding Information" name="Expenditure Class"></field:display>
<field:display feature="Funding Information" name="Effective Funding Date"></field:display>
<field:display feature="Funding Information" name="Funding Closing Date"></field:display>
<field:display feature="Government Contact Information" name="Government Email"></field:display> 
<field:display feature="Government Contact Information" name="Government First Name"></field:display> 
<field:display feature="Identification" name="AMP ID"></field:display> 
<field:display feature="Identification" name="Data Source"></field:display> 
<field:display feature="Identification" name="Description"></field:display> 
<field:display feature="Identification" name="Objective Assumption"></field:display> 
<field:display feature="Identification" name="Objective Comments"></field:display> 
<field:display feature="Identification" name="Objective Objectively Verifiable Indicators"></field:display> 
<field:display feature="Identification" name="Objective Verification"></field:display> 
<field:display feature="Identification" name="Objective"></field:display> 
<field:display feature="Identification" name="Organizations and Project ID"></field:display> 
<field:display feature="Identification" name="Project Title"></field:display>
<field:display feature="Identification" name="Related Pledges"></field:display>
<field:display feature="Identification" name="Only show projects related to pledges"></field:display>
 <field:display feature="Identification" name="Purpose"></field:display> 
<field:display feature="Identification" name="Results"></field:display> 
<field:display feature="Issues" name="Issue Date"></field:display> 
<field:display feature="Issues" name="Measures Taken"></field:display> 
<field:display feature="MTEF Projections" name="MTEFProjections"></field:display> 
<field:display feature="Planning" name="Line Ministry Rank"></field:display> 
<field:display feature="Preview Activity" name="Preview Button"></field:display> 
<field:display feature="Project Coordinator Contact Information" name="Project Coordinator Email"></field:display> 
<field:display feature="Project Coordinator Contact Information" name="Project Coordinator First Name"></field:display> 
<field:display feature="Sector Ministry Contact Information" name="Sector Ministry Contact Email"></field:display> 
<field:display feature="Sector Ministry Contact Information" name="Sector Ministry Contact First Name"></field:display> 
<field:display feature="Sectors" name="Level 1 Sectors List"></field:display> 
<field:display name="A.C. Chapter" feature="Identification"></field:display>
<field:display name="Budget Code Project Id" feature="Identification"></field:display> 
<field:display name="AMP ID" feature="Identification"></field:display> 
<field:display name="Procurement System" feature="Identification"></field:display> 
<field:display name="Reporting System" feature="Identification"></field:display> 
<field:display name="Audit System" feature="Identification"></field:display> 
<field:display name="Institutions" feature="Identification"></field:display> 
<field:display name="Accession Instrument" feature="Identification"></field:display> 
<field:display name="Active Funding Organization" feature="Funding Information"></field:display> 
<field:display name="Activity Approved By" feature="Identification"></field:display> 
<field:display name="Activity Approved On" feature="Identification"></field:display>
<field:display name="Activity Performance" feature="Activity Dashboard"></field:display>
<field:display name="Activity Printer Friendly Button Performance" feature="Activity Dashboard"></field:display>
<field:display name="Activity Printer Friendly Button Risk" feature="Activity Dashboard"></field:display>
<field:display name="Activity Created By" feature="Identification"></field:display>
<field:display name="Activity Created On" feature="Identification"></field:display> 
<field:display name="Activity Updated By" feature="Identification"></field:display>
<field:display name="Activity Updated On" feature="Identification"></field:display>
<field:display name="Activity Without Baseline Button Performance" feature="Activity Dashboard"></field:display>
<field:display name="Actors" feature="Issues"></field:display>
<field:display name="Actual Approval Date" feature="Planning" ></field:display> 
<field:display name="Actual Completion Date" feature="Planning"></field:display>
<field:display name="Actual Disbursement Orders" feature="Disbursement Orders"></field:display> 
<field:display name="Actual Start Date" feature="Planning"></field:display> 
<field:display name="Actual/Planned Commitments" feature="Regional Funding"></field:display> 
<field:display name="Actual/Planned Disbursements" feature="Regional Funding"></field:display> 
<field:display name="Actual/Planned Expenditures" feature="Regional Funding"></field:display> 
<field:display name="Add A New Component Button" feature="Admin - Component Type"></field:display> 
<field:display name="Add Activity Button" feature="Edit Activity"></field:display> 
<field:display name="Add Actors Link" feature="Issues"></field:display> 
<field:display name="Add Commitment Button" feature="Commitments"></field:display> 
<field:display name="Add Components Button" feature="Activity - Component Step"></field:display> 
<field:display name="Add Disbursement Button" feature="Disbursement"></field:display> 
<field:display name="Add Disbursement Order Button" feature="Disbursement Orders"></field:display> 
<field:display name="Add Documents Button" feature="Related Documents"></field:display> 
<field:display name="Add Donor Funding Button" feature="Funding Information"></field:display> 
<field:display name="Add Donor Organization" feature="Funding Information"></field:display> 
<field:display name="Add Expenditure Button" feature="Expenditures"></field:display> 
<field:display name="Add IPA Contract" feature="Contracting"></field:display> 
<field:display name="Add Indicator Button" feature="Activity"></field:display> 
<field:display name="Add Issues Button" feature="Issues"></field:display> 
<field:display name="Add Location" feature="Location"></field:display> 
<field:display name="Add Measures Link" feature="Issues"></field:display> 
<field:display name="Add New Indicator" feature="Admin"></field:display> 
<field:display name="Add Physical Progress Link" feature="Physical Progress"></field:display> 
<field:display name="Add Programs Button - National Plan Objective" feature="Program"></field:display> 
<field:display name="Add Programs Button - Primary Programs" feature="Program"></field:display> 
<field:display name="Add Programs Button - Secondary Programs" feature="Program"></field:display> 
<field:display name="Add Projection" feature ="MTEF Projections"></field:display> 
<field:display name="Add Projection" feature="MTEF Projections"></field:display> 
<field:display name="Add Regional Funding Link" feature="Regional Funding"></field:display> 
<field:display name="Add Regional Fundings" feature="Regional Funding"></field:display> 
<field:display name="Add Scheme Link" feature="Sectors"></field:display> 
<field:display name="Add Sector Level 1 Link" feature="Sectors"></field:display> 
<field:display name="Add Sector Level 2 Link" feature="Sectors"></field:display> 
<field:display name="Add Sector Level 3 Link" feature="Sectors"></field:display> 
<field:display name="Add Sectors Button" feature="Sectors"></field:display> 
<field:display name="Add Web Resource Button" feature="Web Resources"></field:display> 
<field:display name="Adjustment Type Commitment" feature="Commitments"></field:display> 
<field:display name="Adjustment Type Disbursement" feature="Disbursement"></field:display> 
<field:display name="Adjustment Type Disbursement" feature="Funding Information"></field:display> 
<field:display name="Adjustment Type Expenditure" feature="Expenditures"></field:display> 
<field:display name="Adjustment Type of Disbursement Order" feature="Disbursement Orders"></field:display> 
<field:display name="Admin - Component Cancel Button" feature="Admin - Component"></field:display> 
<field:display name="Admin - Component Code" feature="Admin - Component"></field:display> 
<field:display name="Admin - Component Description" feature="Admin - Component"></field:display> 
<field:display name="Admin - Component Save Button" feature="Admin - Component"></field:display> 
<field:display name="Admin - Component Title" feature="Admin - Component"></field:display> 
<field:display name="Admin - Component Type Cancel Button" feature="Admin - Component Type"></field:display> 
<field:display name="Admin - Component Type Close Button" feature="Admin - Component Type"></field:display> 
<field:display name="Admin - Component Type Code" feature="Admin - Component Type"></field:display> 
<field:display name="Admin - Component Type Enable checkbox" feature="Admin - Component Type"></field:display> 
<field:display name="Admin - Component Type Name" feature="Admin - Component Type"></field:display> 
<field:display name="Admin - Component Type Save Button" feature="Admin - Component Type"></field:display> 
<field:display name="Admin - Component Type" feature="Admin - Component"></field:display> 
<field:display name="Admin Description" feature="Admin"></field:display> 
<field:display name="Amount Commitment" feature="Commitments"></field:display> 
<field:display name="Amount Disbursement" feature="Disbursement"></field:display> 
<field:display name="Amount Disbursement" feature="Funding Information"></field:display> 
<field:display name="Amount Expenditure" feature="Expenditures"></field:display> 
<field:display name="Amount of Disbursement Order" feature="Disbursement Orders"></field:display> 
<field:display name="Assumptions" feature="Identification"></field:display> 
<field:display name="Base Value" feature="Activity"></field:display> 
<field:display name="Cancel Button" feature="Admin - Component Type"></field:display> 
<field:display name="Cancel Button" feature="Admin - Component"></field:display> 
<field:display name="Cancel button" feature="Create Message Form"></field:display> 
<field:display name="Channel Overview Tab" feature="Channel Overview"></field:display> 
<field:display name="Classification Expenditure" feature="Funding Information"></field:display> 
<field:display name="Close Button" feature="Admin - Component Type"></field:display> 
<field:display name="Code" feature="Admin - Component Type"></field:display> 
<field:display name="Code" feature="Admin - Component"></field:display> 
<field:display name="Comments Base Value" feature="Activity"></field:display> 
<field:display name="Comments Current Value" feature="Activity"></field:display> 
<field:display name="Comments Revised Target Value" feature="Activity"></field:display> 
<field:display name="Comments Target Value" feature="Activity"></field:display> 
<field:display name="Component Name" feature="Activity - Component Step"></field:display> 
<field:display name="Component description" feature="Activity - Component Step"></field:display> 
<field:display name="Componente" feature="Planning"></field:display> 
<field:display name="Components Actual/Planned Commitments" feature="Activity - Component Step"></field:display> 
<field:display name="Components Actual/Planned Disbursements" feature="Activity - Component Step"></field:display> 
<field:display name="Components Actual/Planned Expenditures" feature="Activity - Component Step"></field:display> 
<field:display name="Components Amount Commitments" feature="Activity - Component Step"></field:display> 
<field:display name="Components Amount Disbursements" feature="Activity - Component Step"></field:display> 
<field:display name="Components Amount Expenditures" feature="Activity - Component Step"></field:display> 
<field:display name="Components Currency Commitments" feature="Activity - Component Step"></field:display> 
<field:display name="Components Currency Disbursements" feature="Activity - Component Step"></field:display> 
<field:display name="Components Currency Expenditures" feature="Activity - Component Step"></field:display> 
<field:display name="Components Date Commitments" feature="Activity - Component Step"></field:display> 
<field:display name="Components Date Disbursements" feature="Activity - Component Step"></field:display> 
<field:display name="Components Date Expenditures" feature="Activity - Component Step"></field:display> 
<field:display name="Components Grand Total Commitments" feature="Activity - Component Step"></field:display> 
<field:display name="Components Grand Total Disbursements" feature="Activity - Component Step"></field:display> 
<field:display name="Components Grand Total Expenditures" feature="Activity - Component Step"></field:display> 
<field:display name="Conditions for Fund Release" feature="Funding Information"></field:display>
<field:display name="Contact Name" feature="Reports Contact Information"></field:display> 
<field:display name="Contract Completion" feature="Contracting"></field:display> 
<field:display name="Contract Description" feature="Contracting"></field:display> 
<field:display name="Contract Donor Disbursements" feature="Contracting"></field:display> 
<field:display name="Contract Execution Rate" feature="Contracting"></field:display> 
<field:display name="Contract Funding Execution Rate" feature="Contracting"></field:display> 
<field:display name="Contract Name" feature="Contracting"></field:display> 
<field:display name="Contract Number" feature="Planning"></field:display> 
<field:display name="Contract Organization" feature="Contracting"></field:display> 
<field:display name="Contract Total Value" feature="Contracting"></field:display> 
<field:display name="Contract Validity Date" feature="Contracting"></field:display> 
<field:display name="Contract of Disbursement Order" feature="Disbursement Orders"></field:display> 
<field:display name="Contract of Disbursement" feature="Disbursement"></field:display> 
<field:display name="Contract type" feature="Contracting"></field:display> 
<field:display name="Contracting Activity Category" feature="Contracting"></field:display> 
<field:display name="Contracting Add Disbursement" feature="Contracting"></field:display> 
<field:display name="Contracting Cancel Saving" feature="Contracting"></field:display> 
<field:display name="Contracting Central Amount" feature="Contracting"></field:display> 
<field:display name="Contracting Contractor Name" feature="Contracting"></field:display> 
<field:display name="Contracting Disbursements Global Currency" feature="Contracting"></field:display> 
<field:display name="Contracting Disbursements" feature="Contracting"></field:display> 
<field:display name="Contracting Funding Disbursements" feature="Contracting"></field:display> 
<field:display name="Contracting IB" feature="Contracting"></field:display> 
<field:display name="Contracting IFIs" feature="Contracting"></field:display> 
<field:display name="Contracting INV" feature="Contracting"></field:display> 
<field:display name="Contracting IPA Activity Category" feature="Contracting"></field:display> 
<field:display name="Contracting IPA Contract Type" feature="Contracting"></field:display> 
<field:display name="Contracting Regional Amount" feature="Contracting"></field:display> 
<field:display name="Contracting Remove Disbursements" feature="Contracting"></field:display> 
<field:display name="Contracting Save Button" feature="Contracting"></field:display> 
<field:display name="Contracting Start of Tendering" feature="Contracting"></field:display> 
<field:display name="Contracting Status" feature="Contracting"></field:display> 
<field:display name="Contracting Tab Status" feature="Contracting"></field:display> 
<field:display name="Contracting Tab" feature="Contracting"></field:display> 
<field:display name="Contracting Total Amount" feature="Contracting"></field:display> 
<field:display name="Contracting Total National Contribution" feature="Contracting"></field:display> 
<field:display name="Contracting Type" feature="Contracting"></field:display> 
<field:display name="Creation date" feature="Admin"></field:display>
<field:display name="Cris Number" feature="Identification"></field:display> 
<field:display name="Cumulative Commitment" feature="Funding Information"></field:display> 
<field:display name="Cumulative Disbursement" feature="Funding Information"></field:display> 
<field:display name="Currency Commitment" feature="Commitments"></field:display> 
<field:display name="Currency Commitments" feature="Regional Funding"></field:display> 
<field:display name="Currency Disbursement" feature="Disbursement"></field:display> 
<field:display name="Currency Disbursement" feature="Funding Information"></field:display> 
<field:display name="Currency Disbursements" feature="Regional Funding"></field:display> 
<field:display name="Currency Expenditure" feature="Expenditures"></field:display> 
<field:display name="Currency Expenditures" feature="Regional Funding"></field:display> 
<field:display name="Currency of Disbursement Order" feature="Disbursement Orders"></field:display> 
<field:display name="Current Value" feature="Activity"></field:display> 
<field:display name="Data Source" feature="Identification"></field:display> 
<field:display name="Data Team Leader" feature="Identification"></field:display> 
<field:display name="Date Base Value" feature="Activity"></field:display> 
<field:display name="Date Commitment" feature="Commitments"></field:display> 
<field:display name="Related Pledge" feature="Commitments"></field:display>
<field:display name="Date Commitments" feature="Regional Funding"></field:display> 
<field:display name="Date Current Value" feature="Activity"></field:display> 
<field:display name="Date Disbursement" feature="Disbursement"></field:display>
<field:display name="Related Pledge" feature="Disbursement"></field:display>
<field:display name="Date Disbursement" feature="Funding Information"></field:display>
<field:display name="Date Disbursements" feature="Regional Funding"></field:display> 
<field:display name="Date Expenditure" feature="Expenditures"></field:display> 
<field:display name="Date Expenditures" feature="Regional Funding"></field:display> 
<field:display name="Date Of Last Activity Change" feature="Identification"></field:display> 
<field:display name="Date Revised Target Value" feature="Activity"></field:display> 
<field:display name="Date Target Value" feature="Activity"></field:display> 
<field:display name="Date of Disbursement Order" feature="Disbursement Orders"></field:display> 
<field:display name="Delegated Cooperation" feature="Funding Information"></field:display> 
<field:display name="Delegated Partner" feature="Funding Information"></field:display> 
<field:display name="Delete Contract" feature="Contracting"></field:display> 
<field:display name="Delete Funding Link - Donor Organization" feature="Funding Information"></field:display> 
<field:display name="Delete Regional Funding Button" feature="Regional Funding"></field:display> 
<field:display name="Description Text Box" feature="Create Message Form"></field:display> 
<field:display name="Description" feature="Admin - Component"></field:display> 
<field:display name="Description" feature="Identification"></field:display> 
<field:display name="Disbursement Order Contract ID" feature="Disbursement Orders"></field:display> 
<field:display name="Disbursement Order Number" feature="Disbursement Orders"></field:display> 
<field:display name="Disbursement Orders Tab" feature="Disbursement Orders"></field:display> 
<field:display name="Disbursements" feature="Contracting"></field:display> 
<field:display name="Document Comment" feature="Related Documents"></field:display> 
<field:display name="Document Date" feature="Related Documents"></field:display> 
<field:display name="Document Description" feature="Related Documents"></field:display> 
<field:display name="Document FileName" feature="Related Documents"></field:display> 
<field:display name="Document Notes" feature="Related Documents"></field:display> 
<field:display name="Document Title" feature="Related Documents"></field:display> 
<field:display name="Document Type" feature="Related Documents"></field:display> 
<field:display name="Donor Agency" feature="Funding Information"></field:display> 
<field:display name="Donor Commitment Date" feature="Funding Information"></field:display>
<field:display name="Loan Ratification Date" feature="Funding Information"></field:display>
<field:display name="Loan Maturity Date" feature="Funding Information"></field:display>
<field:display name="Loan Interest Rate" feature="Funding Information"></field:display>
<field:display name="Loan Grace Period" feature="Funding Information"></field:display>
<field:display name="Donor Email" feature="Donor Contact Information"></field:display> 
<field:display name="Donor Fax Number" feature="Donor Contact Information"></field:display> 
<field:display name="Donor First Name" feature="Donor Contact Information"></field:display> 
<field:display name="Donor Group" feature="Funding Information"></field:display> 
<field:display name="Donor Last Name" feature="Donor Contact Information"></field:display> 
<field:display name="Donor Objective" feature="Funding Information"></field:display> 
<field:display name="Donor Organization" feature="Donor Contact Information"></field:display> 
<field:display name="Donor Phone Number" feature="Donor Contact Information"></field:display> 
<field:display name="Donor Title" feature="Donor Contact Information"></field:display> 
<field:display name="Draft Alert" feature="Alert tab"></field:display> 
<field:display name="Draft Message" feature="Message tab"></field:display> 
<field:display name="Draft" feature="Identification"></field:display> 
<field:display name="Edit Components Link" feature="Activity - Component Step"></field:display> 
<field:display name="Edit Contract" feature="Contracting"></field:display> 
<field:display name="Edit Funding Link - Donor Organization" feature="Funding Information"></field:display> 
<field:display name="Edit Funding Link" feature="Regional Funding"></field:display> 
<field:display name="Enable checkbox" feature="Admin - Component Type"></field:display> 
<field:display name="Environment" feature="Cross Cutting Issues"></field:display> 
<field:display name="Equal Opportunity" feature="Cross Cutting Issues"></field:display> 
<field:display name="Exchange Rate" feature="Funding Information"></field:display> 
<field:display name="External Financing" feature="Admin NPD"></field:display> 
<field:display name="FY" feature="Budget"></field:display> 
<field:display name="Final Date for Contracting" feature="Planning"></field:display> 
<field:display name="Final Date for Disbursements" feature="Planning"></field:display> 
<field:display name="Financial Instrument" feature="Budget"></field:display> 
<field:display name="Financing Instrument" feature="Funding Information"></field:display> 
<field:display name="Funding Organization Id" feature="Funding Information"></field:display> 
<field:display name="Funding Organization Name" feature="Funding Information"></field:display> 
<field:display name="Funding Organization" feature="Funding Information"></field:display> 
<field:display name="Funding Organizations Tab" feature="Funding Information"></field:display>
<field:display name="Funding Status" feature="Funding Information"></field:display>  
<field:display name="Funding start date" feature="Funding Information"></field:display>  
<field:display name="Funding end date" feature="Funding Information"></field:display>  
<field:display name="Government Agreement Number" feature="Identification"></field:display> 
<field:display name="Government Approval Procedures" feature="Budget"></field:display>
<field:display name="Indirect On Budget" feature="Budget"></field:display>  
<field:display name="Government Email" feature="Government Contact Information"></field:display> 
<field:display name="Government Fax Number" feature="Government Contact Information"></field:display> 
<field:display name="Government First Name" feature="Government Contact Information"></field:display> 
<field:display name="Government Last Name" feature="Government Contact Information"></field:display> 
<field:display name="Government Organization" feature="Government Contact Information"></field:display> 
<field:display name="Government Phone Number" feature="Government Contact Information"></field:display> 
<field:display name="Government Title" feature="Government Contact Information"></field:display> 
<field:display name="Grand Total Commitments" feature="Activity - Component Step"></field:display> 
<field:display name="Grand Total Disbursements" feature="Activity - Component Step"></field:display>
<field:display name="Humanitarian Aid" feature="Identification"></field:display>
<field:display name="Disaster Response Marker" feature="Identification"></field:display>
<field:display name="Implementation Level" feature="Location"></field:display>
<field:display name="Implementation Location" feature="Location"></field:display> 
<field:display name="Inbox Alert" feature="Alert tab"></field:display> 
<field:display name="Inbox Message" feature="Message tab"></field:display> 
<field:display name="Indicator Base Value" feature="Activity"></field:display> 
<field:display name="Indicator Current Value" feature="Activity"></field:display> 
<field:display name="Indicator Description" feature="Activity"></field:display> 
<field:display name="Indicator ID" feature="Activity"></field:display> 
<field:display name="Indicator Name" feature="Activity"></field:display> 
<field:display name="Indicator Target Value" feature="Activity"></field:display> 
<field:display name="Indicator Type" feature="Admin"></field:display> 
<field:display name="Indicator code" feature="Admin"></field:display> 
<field:display name="Admin Indicator name" feature="Admin"></field:display> 
<field:display name="Internal Financing" feature="Admin NPD"></field:display> 
<field:display name="Issue Date" feature="Issues"></field:display> 
<field:display name="Issues" feature="Issues"></field:display> 
<field:display name="Joint Criteria" feature="Budget"></field:display> 
<field:display name="Lessons Learned" feature="Identification"></field:display> 
<field:display name="Level 1 Sectors List" feature="Sectors"></field:display> 
<field:display name="Level 2 Sectors List" feature="Sectors"></field:display> 
<field:display name="Level 3 Sectors List" feature="Sectors"></field:display> 
<field:display name="Line Ministry Rank" feature="Planning"></field:display> 
<field:display name="Indirect On Budget" feature="Budget"></field:display>
<field:display name="Link to Disbursement Order ID" feature="Disbursement"></field:display> 
<field:display name="Logframe Category" feature="Activity"></field:display> 
<field:display name="Logframe Preview Button" feature="Logframe" ></field:display> 
<field:display name="Logframe Preview Button" feature="Logframe"></field:display> 
<field:display name="Measures Taken" feature="Issues"></field:display> 
<field:display name="Minorities" feature="Cross Cutting Issues"></field:display> 
<field:display name="Multi Sector Configuration" feature="Sectors"></field:display> 
<field:display name="NPD Clasification" feature="Identification"></field:display> 
<field:display name="NPD Program Description" feature="Program"></field:display> 
<field:display name="Name" feature="Admin - Component Type"></field:display> 
<field:display name="National Planning Objectives" feature="NPD Programs"></field:display>
<field:display name="National Planning Objectives Detail" feature="NPD Programs"></field:display>  
<field:display name="National Plan Objective" feature="Program"></field:display> 
<field:display name="National Planning Objectives" feature="NPD Programs"></field:display> 
<field:display name="Objective Comments" feature="Identification"></field:display> 
<field:display name="Objective" feature="Identification"></field:display> 
<field:display name="Objectively Verifiable Indicators" feature="Identification"></field:display> 
<field:display name="Activity Budget" feature="Budget"></field:display>
<field:display name="Organizations Selector" feature="Funding Information"></field:display> 
<field:display name="Organizations and Project ID" feature="Identification"></field:display> 
<field:display name="Overall Contribution" feature="Planning"></field:display> 
<field:display name="Overall Cost" feature="Planning"></field:display> 
<field:display name="Paris Survey" feature="Paris Indicators"></field:display> 
<field:display name="Percentage" feature="Sectors"></field:display> 
<field:display name="Physical Progress Tab" feature="Physical Progress"></field:display> 
<field:display name="Physical Progress" feature="Physical Progress"></field:display> 
<field:display name="Physical progress description" feature="Physical Progress"></field:display> 
<field:display name="Physical progress title" feature="Physical Progress"></field:display> 
<field:display name="Planned Disbursement Preview" feature="Disbursement"></field:display> 
<field:display name="Primary Program" feature="NPD Programs"></field:display>
<field:display name="Primary Program Detail" feature="NPD Programs"></field:display>
<field:display name="Secondary Program Detail" feature="NPD Programs"></field:display>
<field:display name="Tertiary Program Detail" feature="NPD Programs"></field:display> 
<field:display name="Primary Sector Sub-Sector" feature="Sectors"></field:display> 
<field:display name="Primary Sector Sub-Sub-Sector" feature="Sectors"></field:display> 
<field:display name="Primary Sector" feature="Sectors"></field:display> 
<field:display name="Validate Mandatory Primary Sector" feature="Sectors"></field:display> 
<field:display name="Printer Friendly Button Performance" feature="Portfolio Dashboard"></field:display> 
<field:display name="Printer Friendly Button Risk" feature="Portfolio Dashboard"></field:display> 
<field:display name="Program Background" feature="Admin NPD"></field:display> 
<field:display name="Program Beneficiaries" feature="Admin NPD"></field:display> 
<field:display name="Program Code" feature="Admin NPD"></field:display> 
<field:display name="Program Description" feature="Admin NPD"></field:display> 
<field:display name="Program Environment Considerations" feature="Admin NPD"></field:display> 
<field:display name="Program Lead Agency" feature="Admin NPD"></field:display> 
<field:display name="Program Name" feature="Admin NPD"></field:display> 
<field:display name="Program Objectives" feature="Admin NPD"></field:display> 
<field:display name="Program Outputs" feature="Admin NPD"></field:display> 
<field:display name="Program Target Groups" feature="Admin NPD"></field:display> 
<field:display name="Program Type" feature="Admin NPD"></field:display> 
<field:display name="Project Category" feature="Identification"></field:display> 
<field:display name="Project Code" feature="Budget"></field:display> 
<field:display name="Project Coordinator Email" feature="Project Coordinator Contact Information"></field:display> 
<field:display name="Project Coordinator Fax Number" feature="Project Coordinator Contact Information"></field:display> 
<field:display name="Project Coordinator First Name" feature="Project Coordinator Contact Information"></field:display> 
<field:display name="Project Coordinator Last Name" feature="Project Coordinator Contact Information"></field:display> 
<field:display name="Project Coordinator Organization" feature="Project Coordinator Contact Information"></field:display> 
<field:display name="Project Coordinator Phone Number" feature="Project Coordinator Contact Information"></field:display> 
<field:display name="Project Coordinator Title" feature="Project Coordinator Contact Information"></field:display> 
<field:display name="Project Fiche Button" feature="Project Fiche" ></field:display> 
<field:display name="Project Risk" feature="Activity Dashboard"></field:display>
<field:display name="Project Title" feature="Identification"></field:display>
<field:display name="Projection Amount" feature="MTEF Projections"></field:display> 
<field:display name="Projection Currency Code" feature="MTEF Projections"></field:display> 
<field:display name="Projection Date" feature ="MTEF Projections"></field:display> 
<field:display name="Projection Name" feature="MTEF Projections"></field:display> 
<field:display name="Proposed Approval Date" feature="Planning"></field:display> 
<field:display name="Proposed Completion Date" feature="Planning"></field:display> 
<field:display name="Duration of Project" feature="Planning"></field:display> 
<field:display name="Closing Dates" feature="Planning"></field:display>
<field:display name="Proposed Project Amount" feature="Proposed Project Cost"></field:display>
<field:display name="Revised Project Amount" feature="Proposed Project Cost"></field:display> 
<field:display name="Proposed Start Date" feature="Planning"></field:display> 
<field:display name="Purpose" feature="Identification"></field:display> 
<field:display name="Recievers" feature="Create Message Form"></field:display> 
<field:display name="Region" feature="Location"></field:display> 
<field:display name="Regional Funding Tab" feature="Regional Funding"></field:display> 
<field:display name="Regional Percentage" feature="Location"></field:display> 
<field:display name="Related Activity Dropdown" feature="Create Message Form"></field:display> 
<field:display name="Remove Actors Button" feature="Issues"></field:display> 
<field:display name="Remove Components Button" feature="Activity - Component Step"></field:display> 
<field:display name="Remove Documents Button" feature="Related Documents"></field:display> 
<field:display name="Remove Donor Organization" feature="Funding Information"></field:display> 
<field:display name="Remove Fundings" feature="Regional Funding"></field:display> 
<field:display name="Remove Issues Button" feature="Issues"></field:display> 
<field:display name="Remove Location" feature="Location"></field:display> 
<field:display name="Remove Measures Button" feature="Issues"></field:display> 
<field:display name="Remove Physical Progress Link" feature="Physical Progress"></field:display> 
<field:display name="Remove Program Button - National Plan Objective" feature="Program"></field:display> 
<field:display name="Remove Program Button - Primary Programs" feature="Program"></field:display> 
<field:display name="Remove Program Button - Secondary Programs" feature="Program"></field:display> 
<field:display name="Remove Sectors Button" feature="Sectors"></field:display> 
<field:display name="Results" feature="Identification"></field:display> 
<field:display name="Revised Target Value" feature="Activity"></field:display> 
<field:display name="Risk" feature="Activity"></field:display> 
<field:display name="Same as Proposed Approval Date" feature="Planning"></field:display> 
<field:display name="Same as Proposed Start Date" feature="Planning"></field:display> 
<field:display name="Save Button" feature="Admin - Component Type"></field:display> 
<field:display name="Save Button" feature="Admin - Component"></field:display> 
<field:display name="Save button" feature="Create Message Form"></field:display> 
<field:display name="Secondary Program" feature="NPD Programs"></field:display> 
<field:display name="Secondary Sector Sub-Sector" feature="Sectors"></field:display> 
<field:display name="Secondary Sector Sub-Sub-Sector" feature="Sectors"></field:display> 
<field:display name="Secondary Sector" feature="Sectors"></field:display>
<field:display name="Validate Mandatory Secondary Sector" feature="Sectors"></field:display> 
<field:display name="Sector Ministry Contact Email" feature="Sector Ministry Contact Information"></field:display> 
<field:display name="Sector Ministry Contact Fax Number" feature="Sector Ministry Contact Information"></field:display> 
<field:display name="Sector Ministry Contact First Name" feature="Sector Ministry Contact Information"></field:display> 
<field:display name="Sector Ministry Contact Last Name" feature="Sector Ministry Contact Information"></field:display> 
<field:display name="Sector Ministry Contact Organization" feature="Sector Ministry Contact Information"></field:display> 
<field:display name="Sector Ministry Contact Phone Number" feature="Sector Ministry Contact Information"></field:display> 
<field:display name="Sector Ministry Contact Title" feature="Sector Ministry Contact Information"></field:display> 
<field:display name="Sector Scheme Name" feature="Sectors"></field:display> 
<field:display name="Sector" feature="Sectors"></field:display> 
<field:display name="Sectors" feature="Admin"></field:display> 
<field:display name="Send button" feature="Create Message Form"></field:display> 
<field:display name="Sent Alert" feature="Alert tab"></field:display> 
<field:display name="Sent Message" feature="Message tab"></field:display> 
<field:display name="Set Alert Drop down" feature="Create Message Form"></field:display> 
<field:display name="Signature of Contract" feature="Contracting"></field:display> 
<field:display name="Status" feature="Identification"></field:display> 
<field:display name="Sub Program Level 1" feature="NPD Dashboard"></field:display> 
<field:display name="Sub Program Level 2" feature="NPD Dashboard"></field:display> 
<field:display name="Sub Program Level 3" feature="NPD Dashboard"></field:display> 
<field:display name="Sub Program Level 4" feature="NPD Dashboard"></field:display> 
<field:display name="Sub Program Level 5" feature="NPD Dashboard"></field:display> 
<field:display name="Sub Program Level 6" feature="NPD Dashboard"></field:display> 
<field:display name="Sub Program Level 7" feature="NPD Dashboard"></field:display> 
<field:display name="Sub Program Level 8" feature="NPD Dashboard"></field:display> 
<field:display name="Sub-Program" feature="Budget"></field:display> 
<field:display name="Sub-Vote" feature="Budget"></field:display>
<field:display name="Target Value" feature="Activity"></field:display> 
<field:display name="Template Manager" feature="Message Manager"></field:display> 
<field:display name="Title Text Box" feature="Create Message Form"></field:display> 
<field:display name="Title" feature="Admin - Component"></field:display> 
<field:display name="Total Amount Commitments" feature="Regional Funding"></field:display> 
<field:display name="Total Amount Disbursements" feature="Regional Funding"></field:display> 
<field:display name="Total Amount Expenditures" feature="Regional Funding"></field:display> 
<field:display name="Total Amount" feature="Contracting"></field:display> 
<field:display name="Total Committed" feature="Commitments"></field:display> 
<field:display name="Total Disbursed" feature="Disbursement"></field:display> 
<field:display name="Total Disbursements of Contract" feature="Contracting"></field:display> 
<field:display name="Total Donor Commitments" feature="Regional Funding"></field:display> 
<field:display name="Total Donor Disbursements" feature="Regional Funding"></field:display> 
<field:display name="Total Donor Expenditures" feature="Regional Funding"></field:display> 
<field:display name="Total EC Contribution" feature="Contracting"></field:display> 
<field:display name="Total Expended" feature="Expenditures"></field:display> 
<field:display name="Total Financing Required" feature="Admin NPD"></field:display> 
<field:display name="Total Funding Disbursements of Contract" feature="Contracting"></field:display> 
<field:display name="Total Ordered" feature="Disbursement Orders"></field:display> 
<field:display name="Total Private Contribution" feature="Contracting"></field:display> 
<field:display name="Type Of Assistance" feature="Funding Information"></field:display> 
<field:display name="Type" feature="Admin - Component"></field:display> 
<field:display name="Uncommitted Cumulative Balance" feature="Funding Information"></field:display> 
<field:display name="Undisbursed Cumulative Balance" feature="Funding Information"></field:display> 
<field:display name="Undisbursed Funds" feature="Funding Information"></field:display> 
<field:display name="Unexpended Funds" feature="Funding Information"></field:display> 
<field:display name="Delivery rate" feature="Funding Information"></field:display>
<field:display name="Validate Mandatory Regional Percentage" feature="Location"></field:display> 
<field:display name="Verifications" feature="Identification"></field:display> 
<field:display name="View Schemes Link" feature="Sectors"></field:display> 
<field:display name="Vote" feature="Budget"></field:display>
<field:display name="Validate Mandatory Vote" feature="Budget"></field:display>
<field:display name="Validate Mandatory Sub-Vote" feature="Budget"></field:display>
<field:display name="Validate Mandatory Project Code" feature="Budget"></field:display>
<field:display name="Validate Mandatory Sub-Program" feature="Budget"></field:display>
 
<field:display name="Web Resource Description" feature="Web Resources"></field:display> 
<field:display name="Web Resources Document Type" feature="Web Resources"></field:display> 
<field:display name="Web Resources Notes" feature="Web Resources"></field:display> 
<field:display name="Web Resources Title" feature="Web Resources"></field:display> 
<field:display name="Web Resources Url" feature="Web Resources"></field:display> 
<field:display name="Without Baseline Button Performance" feature="Portfolio Dashboard"></field:display> 
<field:display name="Workspace of Creator" feature="Identification"></field:display> 
<field:display name='Delete Regional Funding Button' feature='Regional Funding'></field:display> 
<module:display name="Content Repository"></module:display>
<feature:display name="My Resources" module="Content Repository"></feature:display> 
<feature:display name="Team Resources" module="Content Repository"></feature:display>
<feature:display name="Public Resources" module="Content Repository"></feature:display>
<feature:display name="Add Resources" module="Content Repository"></feature:display>
<field:display name="Create From Template" feature="Add Resources"></field:display>
<field:display name="Add Web Link" feature="Add Resources"></field:display>
<field:display name="Upload Doc" feature="Add Resources"></field:display>

<feature:display name="Pledges Columns" module="Pledges"></feature:display>
<field:display feature="Pledges Columns" name="Pledges Titles"></field:display>
<field:display feature="Pledges Columns" name="Pledges Donor Group"></field:display>
<field:display feature="Pledges Columns" name="Pledges Aid Modality"></field:display>
<field:display feature="Pledges Columns" name="Pledge Status"></field:display>
<field:display feature="Pledges Countries" name="Pledges Countries"></field:display>
<field:display feature="Pledges Columns" name="Pledges Regions"></field:display>
<field:display feature="Pledges Columns" name="Pledges Zones"></field:display>
<field:display feature="Pledges Columns" name="Pledges Districts"></field:display>
<field:display feature="Pledges Columns" name="Pledges Programs"></field:display>
<field:display feature="Pledges Columns" name="Pledges Secondary Programs"></field:display>
<field:display feature="Pledges Columns" name="Pledges Tertiary Programs"></field:display>
<field:display feature="Pledges Columns" name="Pledges National Plan Objectives"></field:display>
<field:display feature="Pledges Columns" name="Pledges Sectors"></field:display>
<field:display feature="Pledges Columns" name="Pledges Secondary Sectors"></field:display>
<field:display feature="Pledges Columns" name="Pledges Tertiary Sectors"></field:display>
<field:display feature="Pledges Columns" name="Pledges Type Of Assistance"></field:display>
<field:display feature="Pledges Columns" name="Related Projects"></field:display>
<field:display feature="Pledges Columns" name="Point of Contact"></field:display>
<field:display feature="Pledges Columns" name="Alternate Contact"></field:display>
<field:display feature="Pledges Columns" name="Pledges Detail Start Date"></field:display>
<field:display feature="Pledges Columns" name="Pledges Detail End Date"></field:display>
<field:display feature="Pledges Columns" name="Pledges Detail Date Range"></field:display>

<feature:display name="Pledges Names" module="Pledges"></feature:display>
<field:display name="Use Category Manager" feature="Pledges Names"></field:display>
<field:display name="Use Free Text" feature="Pledges Names"></field:display>

<field:display name="Who Authorized Pledge" feature="Pledge Donor Information"></field:display>
<field:display name="Further Approval Needed" feature="Pledge Donor Information"></field:display>
<field:display name="Pledge Location" feature="Pledge Sector and Location"></field:display>
<field:display name="Add Pledge Location Button" feature="Pledge Sector and Location"></field:display>
<field:display name="Remove Pledge Location Button" feature="Pledge Sector and Location"></field:display>
<field:display name="Pledge Program" feature="Pledge Sector and Location"></field:display>
<field:display name="Add Pledge Program Button" feature="Pledge Sector and Location"></field:display>
<field:display name="Remove Pledge Program Button" feature="Pledge Sector and Location"></field:display>
<field:display name="Pledge Sector" feature="Pledge Sector and Location"></field:display>
<field:display name="Add Pledge Sector Button" feature="Pledge Sector and Location"></field:display>
<field:display name="Remove Pledge Sector Button" feature="Pledge Sector and Location"></field:display>
<field:display name="Pledge Funding - Type Of Assistance" feature="Pledge Funding"></field:display>
<field:display name="Pledge Funding - Aid Modality" feature="Pledge Funding"></field:display>
<field:display name="Pledge Funding - Year Range" feature="Pledge Funding"></field:display>
<field:display name="Add Pledge Funding Button" feature="Pledge Funding"></field:display>
<field:display name="Remove Pledge Funding Button" feature="Pledge Funding"></field:display>
<field:display name="Pledge Contact 1 - Name" feature="Pledge Contact 1"></field:display>
<field:display name="Pledge Contact 1 - Title" feature="Pledge Contact 1"></field:display>
<field:display name="Pledge Contact 1 - Organization" feature="Pledge Contact 1"></field:display>
<field:display name="Pledge Contact 1 - Ministry" feature="Pledge Contact 1"></field:display>
<field:display name="Pledge Contact 1 - Address" feature="Pledge Contact 1"></field:display>
<field:display name="Pledge Contact 1 - Telephone" feature="Pledge Contact 1"></field:display>
<field:display name="Pledge Contact 1 - Email" feature="Pledge Contact 1"></field:display>
<field:display name="Pledge Contact 1 - Fax" feature="Pledge Contact 1"></field:display>
<field:display name="Pledge Contact 1 - Alternate Contact" feature="Pledge Contact 1"></field:display>
<field:display name="Pledge Contact 2 - Name" feature="Pledge Contact 2"></field:display>
<field:display name="Pledge Contact 2 - Title" feature="Pledge Contact 2"></field:display>
<field:display name="Pledge Contact 2 - Organization" feature="Pledge Contact 2"></field:display>
<field:display name="Pledge Contact 2 - Ministry" feature="Pledge Contact 2"></field:display>
<field:display name="Pledge Contact 2 - Address" feature="Pledge Contact 2"></field:display>
<field:display name="Pledge Contact 2 - Telephone" feature="Pledge Contact 2"></field:display>
<field:display name="Pledge Contact 2 - Email" feature="Pledge Contact 2"></field:display>
<field:display name="Pledge Contact 2 - Fax" feature="Pledge Contact 2"></field:display>
<field:display name="Pledge Contact 2 - Alternate Contact" feature="Pledge Contact 2"></field:display>
<field:display name="Budget Classification" feature="Budget"></field:display>
<field:display name="Budget Department" feature="Budget"></field:display>
<field:display name="Budget Sector" feature="Budget"></field:display>
<field:display name="Budget Organization" feature="Budget"></field:display>
<field:display name="Budget Program" feature="Budget"></field:display>

<field:display name="Beneficiary Agency" feature="Beneficiary Agency"></field:display> 
<field:display name="Contracting Agency" feature="Contracting Agency"></field:display> 
<field:display name="Executing Agency" feature="Executing Agency"></field:display>
<field:display name="Implementing Agency" feature="Implementing Agency"></field:display>
<field:display name="Regional Group" feature="Regional Group"></field:display>
<field:display name="Responsible Organization" feature="Responsible Organization"></field:display> 
<field:display name="Sector Group" feature="Sector Group"></field:display>
<field:display name="Beneficiary Agency Groups" feature="Beneficiary Agency"></field:display> 
<field:display name="Beneficiary Agency  Department/Division"  feature="Beneficiary Agency"></field:display>
<field:display name="Contracting Agency Department/Division"  feature="Contracting Agency"></field:display>
<field:display name="Contracting Agency Groups" feature="Contracting Agency"></field:display>
<field:display name="Contracting Agency Acronym"  feature="Contracting Agency"></field:display>
<field:display name="Executing Agency Groups" feature="Executing Agency"></field:display>
<field:display name="Executing Agency Type" feature="Executing Agency"></field:display>   
<field:display name="Executing Agency Department/Division" feature="Executing Agency"></field:display>
<field:display name="Implementing Agency Groups" feature="Implementing Agency"></field:display>
<field:display name="Implementing Agency Type" feature="Implementing Agency"></field:display>  
<field:display name="Implementing Agency Department/Division"  feature="Implementing Agency"></field:display> 
<field:display name="Regional Group" feature="Regional Group"></field:display>
<field:display name="Regional Group Department/Division"  feature="Regional Group"></field:display> 
<field:display name="Responsible Organization Groups" feature="Responsible Organization"></field:display> 
<field:display name="Responsible Organization Department/Division"  feature="Responsible Organization"></field:display>
<field:display name="Sector Group Department/Division"  feature="Sector Group"></field:display>

<feature:display name="Use icons for Sectors in Project List" module="/GIS"></feature:display>
<feature:display name="Gap Analysis Map" module="/GIS"></feature:display>

<!-- create entries for each and every available measure -->
<%
	pageContext.setAttribute("allMeasuresList", MeasuresVisibility.allMeasures);
%>
<logic:iterate id="measureName" name="allMeasuresList" scope="page" type="java.lang.String">
	<feature:display name="${measureName}" module="Measures"></feature:display>
</logic:iterate>
<!-- new gis -->
<module:display name="Public GIS" parentModule="PUBLIC VIEW"></module:display>
<module:display name="Public Report Generator" parentModule="PUBLIC VIEW"></module:display>
<!--  -->
<!-- new dashboard -->
<module:display name="Public Dashboards" parentModule="PUBLIC VIEW"></module:display>
<!--  -->

<module:display name="Activity Preview" parentModule="PUBLIC VIEW"></module:display>
<feature:display name="Contacts" module="Activity Preview"></feature:display>
<feature:display name="Version History" module="Activity Preview"></feature:display>

<module:display name="Show Editable Export Formats" parentModule="PUBLIC VIEW"></module:display>
<feature:display name="Background of organization" module="Dashboard Org. Info"></feature:display> 
<feature:display name="Org. Description" module="Dashboard Org. Info"></feature:display> 
<feature:display name="Key Areas of Focus" module="Dashboard Org. Info"></feature:display>
<feature:display name="Show Names As Acronyms" module="Dashboard Org. Info"></feature:display>
<!-- preview activity -->
<module:display name="/Activity Form/Organizations/Donor Organization" parentModule="/Activity Form/Organizations"></module:display>