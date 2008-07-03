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
<module:display name="Activity Approval Process" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Activity Costing" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Activity Levels" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Add & Edit Activity" parentModule="PARIS INDICATORS"></module:display> 
<module:display name="Admin Home" parentModule="PARIS INDICATORS"></module:display> 
<module:display name="Calendar" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Channel Overview" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Components Resume" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Components" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Components_Resume" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Contact Information" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Contracting" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Cross Cutting Issues" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="DOCUMENTS MANAGEMENT"></module:display> 
<module:display name="Desktop Sections" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Document Management" parentModule="DOCUMENTS MANAGEMENT"></module:display> 
<module:display name="Document" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Financial Progress" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Funding" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="HELP"></module:display> 
<module:display name="Issues" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="M & E" parentModule="MONITORING AND EVALUATING"></module:display> 
<module:display name="MONITORING AND EVALUATING"></module:display> 
<module:display name="Messaging System"></module:display> 
<module:display name="NATIONAL PLAN DASHBOARD"></module:display> 
<module:display name="National Planning Dashboard" parentModule="NATIONAL PLAN DASHBOARD"></module:display> 
<module:display name="Organizations" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="PARIS INDICATORS"></module:display> 
<module:display name="PI Reports" parentModule="REPORTING"></module:display> 
<module:display name="PROJECT MANAGEMENT"></module:display> 
<module:display name="Paris Indicators" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Physical Progress" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Portfolio" parentModule="PARIS INDICATORS"></module:display> 
<module:display name="Previews" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Program" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Project ID and Planning" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="REPORTING"></module:display> 
<module:display name="References" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="Reports" parentModule="REPORTING"></module:display> 
<module:display name="Scenarios" parentModule="PROJECT MANAGEMENT"></module:display> 
<module:display name="TREND ANALYSIS"></module:display> 
<module:display name="Trend Analysis and Forecasting" parentModule="TREND ANALYSIS"></module:display>
<feature:display module="Contracting" name="Contracting"></feature:display> 
<feature:display module="Funding" name="Commitments"></feature:display> 
<feature:display module="Funding" name="Disbursement Orders"></feature:display> 
<feature:display module="Funding" name="Disbursement"></feature:display> 
<feature:display module="Funding" name="Disbursements"></feature:display> 
<feature:display module="Funding" name="Expenditures"></feature:display> 
<feature:display module="Funding" name="Funding Information"></feature:display> 
<feature:display module="Funding" name="MTEF Projections"></feature:display> 
<feature:display module="Organizations" name="Beneficiary Agency"></feature:display> 
<feature:display module="Organizations" name="Contracting Agency"></feature:display> 
<feature:display module="Organizations" name="Executing Agency"></feature:display> 
<feature:display module="Organizations" name="Implementing Agency"></feature:display> 
<feature:display module="Organizations" name="Regional Group"></feature:display> 
<feature:display module="Organizations" name="Sector Group"></feature:display> 
<feature:display module="Project ID and Planning" name="Identification"></feature:display> 
<feature:display module="Project ID and Planning" name="Location"></feature:display> 
<feature:display module="Project ID and Planning" name="Planning"></feature:display> 
<feature:display module="Project ID and Planning" name="Sectors"></feature:display> 
<feature:display module="Reports" name="Contribution Report"></feature:display> 
<feature:display name="Activity" module="M & E"></feature:display> 
<feature:display name="Admin NPD" module="National Planning Dashboard"></feature:display> 
<feature:display name="Admin" module="M & E"></feature:display> 
<feature:display name="Beneficiary Agency" module="Organizations"></feature:display> 
<feature:display name="Budget" module="Project ID and Planning"></feature:display> 
<feature:display name="Calendar" module="Calendar"></feature:display> 
<feature:display name="Channel Overview" module="Channel Overview"></feature:display>
<feature:display name="Component Report" module="Reports"></feature:display> 
<feature:display name="Components" module="Components"></feature:display> 
<feature:display name="Content Repository" module="Document Management"></feature:display> 
<feature:display name="Contracting Agency" module="Organizations"></feature:display>  
<feature:display name="Costing" module="Activity Costing"></feature:display> 
<feature:display name="Cross Cutting Issues" module="Cross Cutting Issues"></feature:display> 
<feature:display name="Portfolio Dashboard" module="M & E"></feature:display> 
<feature:display name="Disbursement Orders" module="Funding"></feature:display> 
<feature:display name="Documents Tab" module="Document"></feature:display> 
<feature:display name="Donor Contact Information" module="Contact Information"></feature:display> 
<feature:display name="Donor Report" module="Reports"></feature:display> 
<feature:display name="Edit Activity" module="Previews"></feature:display> 
<feature:display name="Executing Agency" module="Organizations"></feature:display>  
<feature:display name="Financial Progress" module="Financial Progress"></feature:display>
<feature:display name="Funding Information" module="Funding"></feature:display> 
<feature:display name="Government Contact Information" module="Contact Information"></feature:display> 
<feature:display name="Identification" module="Project ID and Planning"></feature:display> 
<feature:display name="Implementing Agency" module="Organizations"></feature:display> 
<feature:display name="Issues" module="Issues"></feature:display> 
<feature:display name="Level Links" module="Activity Levels"></feature:display> 
<feature:display name="Location" module="Project ID and Planning"></feature:display> 
<feature:display name="Logframe" module="Previews"></feature:display> 
<feature:display name="Lucene Debug" module="Admin Home"></feature:display> 
<feature:display name="Measures" module="Reports"></feature:display> 
<feature:display name="Messages" module="Messaging System"></feature:display> 
<feature:display name="My Messages" module="Desktop Sections"></feature:display> 
<feature:display name="My Tasks" module="Desktop Sections"></feature:display> 
<feature:display name="NPD Dashboard" module="National Planning Dashboard"></feature:display> 
<feature:display name="NPD Programs" module="National Planning Dashboard"></feature:display> 
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
<feature:display name="Planning" module="Project ID and Planning"></feature:display> 
<feature:display name="Preview Activity" module="Previews"></feature:display> 
<feature:display name="Program" module="Program"></feature:display> 
<feature:display name="Project Coordinator" module="Contact Information"></feature:display> 
<feature:display name="Project Fiche" module="Previews"></feature:display> 
<feature:display name="Proposed Project Cost" module="Funding"></feature:display> 
<feature:display name="References" module="References"></feature:display> 
<feature:display name="Regional Funding" module="Funding"></feature:display> 
<feature:display name="Regional Group" module="Organizations"></feature:display> 
<feature:display name="Regional Report" module="Reports"></feature:display> 
<feature:display name="Related Documents" module="Document"></feature:display> 
<feature:display name="Reports Contact Information" module="Contact Information"></feature:display> 
<feature:display name="Sector Group" module="Organizations"></feature:display> 
<feature:display name="Sector Ministry Contact" module="Contact Information"></feature:display> 
<feature:display name="Sectors" module="Project ID and Planning"></feature:display> 
<feature:display name="Target Value" module="PI Reports"></feature:display> 
<feature:display name="Web Resources" module="Document"></feature:display> 
<field:display feature="Disbursement Orders" name="Disbursement Orders Tab"></field:display> 
<field:display feature="Donor Contact Information" name="Donor Email"></field:display> 
<field:display feature="Donor Contact Information" name="Donor First Name"></field:display> 
<field:display feature="Edit Activity" name="Edit Activity Button"></field:display> 
<field:display feature="Edit Activity" name="Validate Activity Button"></field:display> 
<field:display feature="Funding Information" name="Type Of Assistance"></field:display> 
<field:display feature="Identification" name="AMP ID"></field:display> 
<field:display feature="Identification" name="Activity Budget"></field:display> 
<field:display feature="Identification" name="Data Source"></field:display> 
<field:display feature="Identification" name="Description"></field:display> 
<field:display feature="Identification" name="Objective Assumption"></field:display> 
<field:display feature="Identification" name="Objective Comments"></field:display> 
<field:display feature="Identification" name="Objective Objectively Verifiable Indicators"></field:display> 
<field:display feature="Identification" name="Objective Verification"></field:display> 
<field:display feature="Identification" name="Objective"></field:display> 
<field:display feature="Identification" name="Objectives"></field:display> 
<field:display feature="Identification" name="Organizations and Project ID"></field:display> 
<field:display feature="Identification" name="Project Title"></field:display> 
<field:display feature="Identification" name="Purpose"></field:display> 
<field:display feature="Identification" name="Results"></field:display> 
<field:display feature="MTEF Projections" name="MTEFProjections"></field:display> 
<field:display feature="Planning" name="Line Ministry Rank"></field:display> 
<field:display feature="Preview Activity" name="Preview Button"></field:display> 
<field:display feature="Project Coordinator" name="Project Coordinator Email"></field:display> 
<field:display feature="Project Coordinator" name="Project Coordinator First Name"></field:display> 
<field:display feature="Sector Ministry Contact" name="Sector Ministry Contact Email"></field:display> 
<field:display feature="Sector Ministry Contact" name="Sector Ministry Contact First Name"></field:display> 
<field:display feature="Sectors" name="Level 1 Sectors List"></field:display> 
<field:display name="A.C. Chapter" feature="Identification"></field:display> 
<field:display name="AMP ID" feature="Identification"></field:display> 
<field:display name="Accession Instrument" feature="Identification"></field:display> 
<field:display name="Active Funding Organization" feature="Funding Information"></field:display> 
<field:display name="Activity Budget" feature="Identification"></field:display> 
<field:display name="Contracting Activity Category" feature="Contracting"></field:display> 
<field:display name="Activity Created By" feature="Identification"></field:display> 
<field:display name="Activity Created On" feature="Identification"></field:display> 
<field:display name="Activity Creator" feature="Identification"></field:display> 
<field:display name="Activity Updated By" feature="Identification"></field:display> 
<field:display name="Activity Updated On" feature="Identification"></field:display> 
<field:display name="Actors" feature="Issues"></field:display> 
<field:display name="Actual Approval Date" feature="Planning" ></field:display> 
<field:display name="Actual Approval Date" feature="Planning"></field:display> 
<field:display name="Actual Commitments" feature="Measures"></field:display> 
<field:display name="Actual Completion Date" feature="Planning"></field:display> 
<field:display name="Actual Disbursement Orders" feature="Disbursement Orders"></field:display> 
<field:display name="Actual Disbursement Orders" feature="Measures"></field:display> 
<field:display name="Actual Disbursements" feature="Measures"></field:display> 
<field:display name="Actual Expenditures" feature="Measures"></field:display> 
<field:display name="Actual Start Date" feature="Planning"></field:display> 
<field:display name="Actual/Planned Commitments" feature="Regional Funding"></field:display> 
<field:display name="Actual/Planned Disbursements" feature="Regional Funding"></field:display> 
<field:display name="Actual/Planned Expenditures" feature="Regional Funding"></field:display> 
<field:display name="Add Actors Link" feature="Issues"></field:display> 
<field:display name="Add Commitment Button" feature="Funding Information"></field:display> 
<field:display name="Add Components Button" feature="Activity - Component Step"></field:display> 
<field:display name="Add Disbursement Button" feature="Funding Information"></field:display> 
<field:display name="Add Disbursement Order Button" feature="Disbursement Orders"></field:display> 
<field:display name="Contracting Add Disbursement" feature="Contracting"></field:display> 
<field:display name="Add Documents Button" feature="Related Documents"></field:display> 
<field:display name="Add Expenditure Button" feature="Expenditures"></field:display> 
<field:display name="Add Funding Button - Proposed Project Cost" feature="Proposed Project Cost"></field:display> 
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
<field:display name="Adjustment Type Commitment" feature="Funding Information"></field:display> 
<field:display name="Adjustment Type Disbursement" feature="Disbursement"></field:display> 
<field:display name="Adjustment Type Disbursement" feature="Funding Information"></field:display> 
<field:display name="Adjustment Type Expenditure" feature="Funding Information"></field:display> 
<field:display name="Adjustment Type of Disbursement Order" feature="Disbursement Orders"></field:display> 
<field:display name="Amount Commitment" feature="Funding Information"></field:display> 
<field:display name="Amount Disbursement" feature="Disbursement"></field:display> 
<field:display name="Amount Disbursement" feature="Funding Information"></field:display> 
<field:display name="Amount Expenditure" feature="Funding Information"></field:display> 
<field:display name="Amount of Disbursement Order" feature="Disbursement Orders"></field:display> 
<field:display name="Assumptions" feature="Identification"></field:display> 
<field:display name="Base Value" feature="Activity"></field:display> 
<field:display name="Beneficiary Agency Add Organizations Button" feature="Beneficiary Agency"></field:display> 
<field:display name="Beneficiary Agency Remove Organizations Button" feature="Beneficiary Agency"></field:display> 
<field:display name="Beneficiary Agency" feature="Beneficiary Agency"></field:display> 
<field:display name="Contracting Cancel Saving" feature="Contracting"></field:display> 
<field:display name="Contracting Central Amount" feature="Contracting"></field:display> 
<field:display name="Channel Overview Tab" feature="Channel Overview"></field:display>
<field:display name="Changed Date" feature="Planning"></field:display> 
<field:display name="Classification Expenditure" feature="Funding Information"></field:display> 
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
<field:display name="Components Currency Commitments" feature="Activity - Component Step"></field:display> 
<field:display name="Components Currency Disbursements" feature="Activity - Component Step"></field:display> 
<field:display name="Components Currency Expenditures" feature="Activity - Component Step"></field:display> 
<field:display name="Components Date Commitments" feature="Activity - Component Step"></field:display> 
<field:display name="Components Date Disbursements" feature="Activity - Component Step"></field:display> 
<field:display name="Components Date Expenditures" feature="Activity - Component Step"></field:display> 
<field:display name="Components Grand Total Commitments" feature="Activity - Component Step"></field:display> 
<field:display name="Components Grand Total Disbursements" feature="Activity - Component Step"></field:display> 
<field:display name="Components Grand Total Expenditures" feature="Activity - Component Step"></field:display> 
<field:display name="Components Physical Progress" feature="Activity - Component Step"></field:display> 
<field:display name="Components Total Amount Commitments" feature="Activity - Component Step"></field:display> 
<field:display name="Components Total Amount Disbursements" feature="Activity - Component Step"></field:display> 
<field:display name="Components Total Amount Expenditures" feature="Activity - Component Step"></field:display> 
<field:display name="Conditions for Fund Release" feature="Funding Information"></field:display> 
<field:display name="Contact Name" feature="Reports Contact Information"></field:display> 
<field:display name="Contract Completion" feature="Contracting"></field:display> 
<field:display name="Contract Description" feature="Contracting"></field:display> 
<field:display name="Contract Execution Rate" feature="Contracting"></field:display> 
<field:display name="Contract Name" feature="Contracting"></field:display> 
<field:display name="Contract Organization" feature="Contracting"></field:display> 
<field:display name="Contract Validity Date" feature="Contracting"></field:display> 
<field:display name="Contract Validity" feature="Contracting"></field:display> 
<field:display name="Contract of Disbursement Order" feature="Disbursement Orders"></field:display> 
<field:display name="Contract of Disbursement Order" feature="Disbursement"></field:display> 
<field:display name="Contract type" feature="Contracting"></field:display> 
<field:display name="Contracting Agency Add Organizations Button" feature="Contracting Agency"></field:display> 
<field:display name="Contracting Agency Remove Organizations Button" feature="Contracting Agency"></field:display> 
<field:display name="Contracting Agency" feature="Contracting Agency"></field:display> 
<field:display name="Contracting Disbursements" feature="Contracting"></field:display> 
<field:display name="Contracting Organization Text" feature="Contracting"></field:display> 
<field:display name="Contracting Status" feature="Contracting"></field:display> 
<field:display name="Contracting Tab" feature="Contracting"></field:display>
<field:display name="Contracting Tab Status" feature="Contracting"></field:display> 
<field:display name="Contribution Amount" feature="Costing"></field:display> 
<field:display name="Contribution Currency" feature="Costing"></field:display> 
<field:display name="Contribution Donors" feature="Costing"></field:display> 
<field:display name="Contribution Financing Type" feature="Costing"></field:display> 
<field:display name="Contribution Type of Assistance" feature="Costing"></field:display> 
<field:display name="Costing Activity Id" feature="Costing"></field:display> 
<field:display name="Costing Activity Name" feature="Costing"></field:display> 
<field:display name="Costing Assumptions" feature="Costing"></field:display> 
<field:display name="Costing Contribution Gap" feature="Costing"></field:display> 
<field:display name="Costing Donor" feature="Costing"></field:display> 
<field:display name="Costing Due Date" feature="Costing"></field:display> 
<field:display name="Costing Inputs" feature="Costing"></field:display> 
<field:display name="Costing Progress" feature="Costing"></field:display> 
<field:display name="Costing Total Contribution" feature="Costing"></field:display> 
<field:display name="Costing Total Cost" feature="Costing"></field:display> 
<field:display name="Costing Tab" feature="Costing"></field:display>
<field:display name="Creation Date" feature="Planning"></field:display> 
<field:display name="Creation date" feature="Admin"></field:display> 
<field:display name="Credit/Donation" feature="Planning"></field:display> 
<field:display name="Cumulative Commitment" feature="Funding Information"></field:display> 
<field:display name="Cumulative Disbursement" feature="Funding Information"></field:display> 
<field:display name="Currency Commitment" feature="Funding Information"></field:display> 
<field:display name="Currency Commitments" feature="Regional Funding"></field:display> 
<field:display name="Currency Disbursement" feature="Disbursement"></field:display> 
<field:display name="Currency Disbursement" feature="Funding Information"></field:display> 
<field:display name="Currency Disbursements" feature="Regional Funding"></field:display> 
<field:display name="Currency Expenditure" feature="Funding Information"></field:display> 
<field:display name="Currency Expenditures" feature="Regional Funding"></field:display> 
<field:display name="Currency of Disbursement Order" feature="Disbursement Orders"></field:display> 
<field:display name="Current Value" feature="Activity"></field:display> 
<field:display name="Data Source" feature="Identification"></field:display> 
<field:display name="Date Base Value" feature="Activity"></field:display> 
<field:display name="Date Commitment" feature="Funding Information"></field:display> 
<field:display name="Date Commitments" feature="Regional Funding"></field:display> 
<field:display name="Date Current Value" feature="Activity"></field:display> 
<field:display name="Date Disbursement" feature="Disbursement"></field:display> 
<field:display name="Date Disbursement" feature="Funding Information"></field:display> 
<field:display name="Date Disbursements" feature="Regional Funding"></field:display> 
<field:display name="Date Expenditure" feature="Funding Information"></field:display> 
<field:display name="Date Expenditures" feature="Regional Funding"></field:display> 
<field:display name="Date Revised Target Value" feature="Activity"></field:display> 
<field:display name="Date Target Value" feature="Activity"></field:display> 
<field:display name="Date Team Leader" feature="Identification"></field:display> 
<field:display name="Date of Disbursement Order" feature="Disbursement Orders"></field:display> 
<field:display name="Delegated Cooperation" feature="Funding Information"></field:display> 
<field:display name="Delegated Partner" feature="Funding Information"></field:display> 
<field:display name="Delete Contract" feature="Contracting"></field:display> 
<field:display name="Delete Regional Funding Button" feature="Regional Funding"></field:display> 
<field:display name="Delete Selected" feature="Contracting"></field:display> 
<field:display name="Description" feature="Admin"></field:display> 
<field:display name="Contract Description" feature="Contracting"></field:display> 
<field:display name="Description" feature="Identification"></field:display> 
<field:display name="Disbursement Order Contract ID" feature="Disbursement Orders"></field:display> 
<field:display name="Disbursement Order Number" feature="Disbursement Orders"></field:display> 
<field:display name="Disbursement Orders Tab" feature="Disbursement Orders"></field:display> 
<field:display name="Contracting Disbursements Global Currency" feature="Contracting"></field:display> 
<field:display name="Disbursements" feature="Contracting"></field:display> 
<field:display name="Document Comment" feature="Related Documents"></field:display> 
<field:display name="Document Date" feature="Related Documents"></field:display> 
<field:display name="Document Description" feature="Related Documents"></field:display> 
<field:display name="Document FileName" feature="Related Documents"></field:display> 
<field:display name="Document Language" feature="Related Documents"></field:display> 
<field:display name="Document Title" feature="Related Documents"></field:display> 
<field:display name="Document Type" feature="Related Documents"></field:display> 
<field:display name="Donor Agency" feature="Funding Information"></field:display> 
<field:display name="Donor Commitment Date" feature="Funding Information"></field:display> 
<field:display name="Donor Email" feature="Donor Contact Information"></field:display> 
<field:display name="Donor Fax Number" feature="Donor Contact Information"></field:display> 
<field:display name="Donor First Name" feature="Donor Contact Information"></field:display> 
<field:display name="Donor Group" feature="Funding Information"></field:display> 
<field:display name="Donor Last Name" feature="Donor Contact Information"></field:display> 
<field:display name="Donor Organization" feature="Donor Contact Information"></field:display> 
<field:display name="Donor Phone Number" feature="Donor Contact Information"></field:display> 
<field:display name="Donor Title" feature="Donor Contact Information"></field:display> 
<field:display name="Donor Type" feature="Funding Information"></field:display> 
<field:display name="Draft" feature="Identification"></field:display> 
<field:display name="Edit Components Link" feature="Activity - Component Step"></field:display> 
<field:display name="Edit Contract" feature="Contracting"></field:display> 
<field:display name="Edit Funding Button- Proposed Project Cost" feature="Proposed Project Cost"></field:display> 
<field:display name="Edit Funding Link" feature="Regional Funding"></field:display> 
<field:display name="Environment" feature="Cross Cutting Issues"></field:display> 
<field:display name="Equal Opportunity" feature="Cross Cutting Issues"></field:display> 
<field:display name="Exchange Rate" feature="Funding Information"></field:display> 
<field:display name="Executing Agency Add Organizations Button" feature="Executing Agency"></field:display> 
<field:display name="Executing Agency Percentage" feature="Executing Agency"></field:display> 
<field:display name="Executing Agency Remove Organizations Button" feature="Executing Agency"></field:display> 
<field:display name="Executing Agency" feature="Executing Agency"></field:display> 
<field:display name="FY" feature="Budget"></field:display> 
<field:display name="Final Date for Contracting" feature="Planning"></field:display> 
<field:display name="Final Date for Disbursements" feature="Planning"></field:display> 
<field:display name="Financial Instrument" feature="Budget"></field:display> 
<field:display name="Financing Instrument" feature="Funding Information"></field:display> 
<field:display name="Funding Organization Id" feature="Funding Information"></field:display> 
<field:display name="Funding Organization Name" feature="Funding Information"></field:display> 
<field:display name="Funding Organization" feature="Funding Information"></field:display> 
<field:display name="Funding Organizations Tab" feature="Funding Information"></field:display>
<field:display name="Financial Progress Tab" feature="Financial Progress"></field:display>
<field:display name="Government Agreement Number" feature="Identification"></field:display> 
<field:display name="Government Approval Procedures" feature="Budget"></field:display> 
<field:display name="Government Email" feature="Government Contact Information"></field:display> 
<field:display name="Government Fax Number" feature="Government Contact Information"></field:display> 
<field:display name="Government Fax Number" feature="Project Coordinator"></field:display> 
<field:display name="Government Fax Number" feature="Sector Ministry Contact"></field:display> 
<field:display name="Government First Name" feature="Government Contact Information"></field:display> 
<field:display name="Government Last Name" feature="Government Contact Information"></field:display> 
<field:display name="Government Organization" feature="Government Contact Information"></field:display> 
<field:display name="Government Phone Number" feature="Government Contact Information"></field:display> 
<field:display name="Government Title" feature="Government Contact Information"></field:display> 
<field:display name="Grand Total Commitments" feature="Activity - Component Step"></field:display> 
<field:display name="Grand Total Cost" feature="Costing"></field:display> 
<field:display name="Grand Total Disbursements" feature="Activity - Component Step"></field:display> 
<field:display name="Contracting IB" feature="Contracting"></field:display> 
<field:display name="Contracting IFIs" feature="Contracting"></field:display> 
<field:display name="Contracting INV" feature="Contracting"></field:display> 
<field:display name="Implementation Level" feature="Location"></field:display> 
<field:display name="Implementation Location" feature="Location"></field:display> 
<field:display name="Implementing Agency Add Organizations Button" feature="Implementing Agency"></field:display> 
<field:display name="Implementing Agency Remove Organizations Button" feature="Implementing Agency"></field:display> 
<field:display name="Implementing Agency" feature="Implementing Agency"></field:display> 
<field:display name="Indicator Base Value" feature="Activity"></field:display> 
<field:display name="Indicator Current Value" feature="Activity"></field:display> 
<field:display name="Indicator Description" feature="Activity"></field:display> 
<field:display name="Indicator ID" feature="Activity"></field:display> 
<field:display name="Indicator Name" feature="Activity"></field:display> 
<field:display name="Indicator Target Value" feature="Activity"></field:display> 
<field:display name="Indicator Type" feature="Admin"></field:display> 
<field:display name="Indicator code" feature="Admin"></field:display> 
<field:display name="Indicator name" feature="Admin"></field:display> 
<field:display name="Issues" feature="Issues"></field:display> 
<field:display name="Joint Criteria" feature="Budget"></field:display> 
<field:display name="Last changed by" feature="Identification"></field:display> 
<field:display name="Lessons Learned" feature="Identification"></field:display> 
<field:display name="Level 1 Sectors List" feature="Sectors"></field:display> 
<field:display name="Level 2 Sectors List" feature="Sectors"></field:display> 
<field:display name="Level 3 Sectors List" feature="Sectors"></field:display> 
<field:display name="Line Ministry Rank" feature="Planning"></field:display> 
<field:display name="Logframe Category" feature="Activity"></field:display> 
<field:display name="Logframe Preview Button" feature="Logframe" ></field:display> 
<field:display name="Logframe Preview Button" feature="Logframe"></field:display> 
<field:display name="Measures Taken" feature="Issues"></field:display> 
<field:display name="Ministry of Planning Rank" feature="Planning"></field:display> 
<field:display name="Minorities" feature="Cross Cutting Issues"></field:display> 
<field:display name="NPD Program Description" feature="Program"></field:display> 
<field:display name="National Plan Objective" feature="NPD Programs"></field:display> 
<field:display name="National Plan Objective" feature="Program"></field:display> 
<field:display name="National Planning Objectives" feature="NPD Programs"></field:display> 
<field:display name="Objective Comments" feature="Identification"></field:display> 
<field:display name="Objective" feature="Identification"></field:display> 
<field:display name="Objectively Verifiable Indicators" feature="Identification"></field:display> 
<field:display name="Objectives" feature="Identification"></field:display> 
<field:display name="Organizations Selector" feature="Funding Information"></field:display> 
<field:display name="Organizations and Project ID" feature="Identification"></field:display> 
<field:display name="Overall Contribution" feature="Planning"></field:display> 
<field:display name="Overall Cost" feature="Planning"></field:display> 
<field:display name="Paris Survey" feature="Paris Indicators"></field:display>
<field:display name="Perspective Commitment" feature="Funding InformationFunding Information"></field:display> 
<field:display name="Perspective Disbursement" feature="Disbursement"></field:display> 
<field:display name="Perspective Expenditure" feature="Funding Information"></field:display> 
<field:display name="Physical Progress" feature="Physical Progress"></field:display> 
<field:display name="Physical progress description" feature="Physical Progress"></field:display> 
<field:display name="Physical progress title" feature="Physical Progress"></field:display>
<field:display name="Physical Progress Tab" feature="Physical Progress"></field:display> 
<field:display name="Planned Commitments" feature="Measures"></field:display> 
<field:display name="Planned Disbursements" feature="Measures"></field:display> 
<field:display name="Planned Expenditures" feature="Measures"></field:display> 
<field:display name="Planning Ministry Rank" feature="Planning"></field:display> 
<field:display name="Primary Program" feature="NPD Programs"></field:display> 
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
<field:display name="Project Code" feature="Budget"></field:display> 
<field:display name="Project Coordinator Email" feature="Project Coordinator"></field:display> 
<field:display name="Project Coordinator Fax Number" feature="Project Coordinator"></field:display> 
<field:display name="Project Coordinator First Name" feature="Project Coordinator"></field:display> 
<field:display name="Project Coordinator Last Name" feature="Project Coordinator"></field:display> 
<field:display name="Project Coordinator Organization" feature="Project Coordinator"></field:display> 
<field:display name="Project Coordinator Phone Number" feature="Project Coordinator"></field:display> 
<field:display name="Project Coordinator Title" feature="Project Coordinator"></field:display> 
<field:display name="Project Fiche Button" feature="Project Fiche" ></field:display> 
<field:display name="Project Fiche Button" feature="Project Fiche"></field:display> 
<field:display name="Project Id" feature="Identification"></field:display> 
<field:display name="Project Title" feature="Identification"></field:display> 
<field:display name="Projection Amount" feature="MTEF Projections"></field:display> 
<field:display name="Projection Currency Code" feature="MTEF Projections"></field:display> 
<field:display name="Projection Date" feature ="MTEF Projections"></field:display> 
<field:display name="Projection Name" feature="MTEF Projections"></field:display> 
<field:display name="Proposed Approval Date" feature="Planning"></field:display> 
<field:display name="Proposed Completion Date" feature="Planning"></field:display> 
<field:display name="Proposed Completion Dates" feature="Planning"></field:display> 
<field:display name="Proposed Project Amount" feature="Proposed Project Cost"></field:display> 
<field:display name="Proposed Project Currency" feature="Proposed Project Cost"></field:display> 
<field:display name="Proposed Project Date" feature="Proposed Project Cost"></field:display> 
<field:display name="Proposed Project Planned" feature="Proposed Project Cost"></field:display> 
<field:display name="Proposed Start Date" feature="Planning"></field:display> 
<field:display name="Purpose" feature="Identification"></field:display>
<field:display name="References Tab" feature="References"></field:display>  
<field:display name="Region" feature="Location"></field:display> 
<field:display name="Regional Funding Perspective Commitments" feature="Regional Funding"></field:display> 
<field:display name="Regional Funding Perspective Disbursements" feature="Regional Funding"></field:display> 
<field:display name="Regional Funding Perspective Expenditures" feature="Regional Funding"></field:display>
<field:display name="Regional Funding Tab" feature="Regional Funding"></field:display> 
<field:display name="Regional Group Add Organizations Button" feature="Regional Group"></field:display> 
<field:display name="Regional Group Remove Organizations Button" feature="Regional Group"></field:display> 
<field:display name="Regional Group" feature="Regional Group"></field:display> 
<field:display name="Regional Percentage" feature="Location"></field:display> 
<field:display name="Contracting Regional Amount" feature="Contracting"></field:display> 
<field:display name="Remove Actors Button" feature="Issues"></field:display> 
<field:display name="Remove Components Button" feature="Activity - Component Step"></field:display> 
<field:display name="Remove Documents Button" feature="Related Documents"></field:display> 
<field:display name="Remove Funding Button - Proposed Project Cost" feature="Proposed Project Cost"></field:display> 
<field:display name="Remove Fundings" feature="Regional Funding"></field:display> 
<field:display name="Remove Issues Button" feature="Issues"></field:display> 
<field:display name="Remove Location" feature="Location"></field:display> 
<field:display name="Remove Measures Button" feature="Issues"></field:display> 
<field:display name="Remove Physical Progress Link" feature="Physical Progress"></field:display> 
<field:display name="Remove Program Button - National Plan Objective" feature="Program"></field:display> 
<field:display name="Remove Program Button - Primary Programs" feature="Program"></field:display> 
<field:display name="Remove Program Button - Secondary Programs" feature="Program"></field:display> 
<field:display name="Remove Sectors Button" feature="Sectors"></field:display> 
<field:display name="Remove Web Resource Button" feature="Web Resources"></field:display> 
<field:display name="Results" feature="Identification"></field:display> 
<field:display name="Revised Target Value" feature="Activity"></field:display> 
<field:display name="Risk" feature="Activity"></field:display> 
<field:display name="Same as Proposed Approval Date" feature="Planning"></field:display> 
<field:display name="Same as Proposed Start Date" feature="Planning"></field:display> 
<field:display name="Contracting Save Button" feature="Contracting"></field:display> 
<field:display name="Secondary Program" feature="NPD Programs"></field:display> 
<field:display name="Sector Group Add Organizations Button" feature="Sector Group"></field:display> 
<field:display name="Sector Group Remove Organizations Button" feature="Sector Group"></field:display> 
<field:display name="Sector Group" feature="Sector Group"></field:display> 
<field:display name="Sector Ministry Contact Email" feature="Sector Ministry Contact"></field:display> 
<field:display name="Sector Ministry Contact Fax Number" feature="Sector Ministry Contact"></field:display> 
<field:display name="Sector Ministry Contact First Name" feature="Sector Ministry Contact"></field:display> 
<field:display name="Sector Ministry Contact Last Name" feature="Sector Ministry Contact"></field:display> 
<field:display name="Sector Ministry Contact Organization" feature="Sector Ministry Contact"></field:display> 
<field:display name="Sector Ministry Contact Phone Number" feature="Sector Ministry Contact"></field:display> 
<field:display name="Sector Ministry Contact Title" feature="Sector Ministry Contact"></field:display> 
<field:display name="Primary Sector" feature="Sectors"></field:display>
<field:display name="Primary Sub-Sector" feature="Sectors"></field:display>
<field:display name="Primary Sub-Sub-Sector" feature="Sectors"></field:display>
<field:display name="Secondary Sector" feature="Sectors"></field:display>
<field:display name="Secondary Sub-Sector" feature="Sectors"></field:display>
<field:display name="Secondary Sub-Sub-Sector" feature="Sectors">&nbsp;</field:display>
<field:display name="Percentage" feature="Sectors"></field:display>
<field:display name="Signature of Contract" feature="Contracting"></field:display> 
<field:display name="Contracting Start of Tendering" feature="Contracting"></field:display> 
<field:display name="Status" feature="Planning"></field:display> 
<field:display name="Sub Program Level 1" feature="NPD Dashboard"></field:display> 
<field:display name="Sub Program Level 2" feature="NPD Dashboard"></field:display> 
<field:display name="Sub Program Level 3" feature="NPD Dashboard"></field:display> 
<field:display name="Sub Program Level 4" feature="NPD Dashboard"></field:display> 
<field:display name="Sub Program Level 5" feature="NPD Dashboard"></field:display> 
<field:display name="Sub Program Level 6" feature="NPD Dashboard"></field:display> 
<field:display name="Sub Program Level 7" feature="NPD Dashboard"></field:display> 
<field:display name="Sub Program Level 8" feature="NPD Dashboard"></field:display> 
<field:display name="Sub-Program" feature="Budget"></field:display> 
<field:display name="Sub-Sector" feature="Sectors"></field:display> 
<field:display name="Sub-Sub-Sector" feature="Sectors"></field:display> 
<field:display name="Sub-Vote" feature="Budget"></field:display> 
<field:display name="Target Value" feature="Activity"></field:display> 
<field:display name="Team" feature="Identification"></field:display> 
<field:display name="Total Amount Commitments" feature="Regional Funding"></field:display> 
<field:display name="Total Amount Disbursements" feature="Regional Funding"></field:display> 
<field:display name="Total Amount Expenditures" feature="Regional Funding"></field:display> 
<field:display name="Contracting Total Amount" feature="Contracting"></field:display> 
<field:display name="Total Commitments" feature="Measures"></field:display> 
<field:display name="Total Committed" feature="Funding Information"></field:display> 
<field:display name="Total Disbursed" feature="Funding Information"></field:display> 
<field:display name="Total Disbursements of Contract" feature="Contracting"></field:display> 
<field:display name="Total Donor Commitments" feature="Regional Funding"></field:display> 
<field:display name="Total Donor Disbursements" feature="Regional Funding"></field:display> 
<field:display name="Total Donor Expenditures" feature="Regional Funding"></field:display> 
<field:display name="Total EC Contribution" feature="Contracting"></field:display> 
<field:display name="Total Expended" feature="Funding Information"></field:display> 
<field:display name="Contracting Total National Contribution" feature="Contracting"></field:display> 
<field:display name="Total Ordered" feature="Disbursement Orders"></field:display> 
<field:display name="Total Private Contribution" feature="Contracting"></field:display> 
<field:display name="Type Of Assistance" feature="Funding Information"></field:display> 
<field:display name="Contracting Type" feature="Contracting"></field:display> 
<field:display name="Undisbursed Balance" feature="Measures"></field:display> 
<field:display name="Undisbursed Funds" feature="Funding Information"></field:display> 
<field:display name="Unexpended Funds" feature="Funding Information"></field:display> 
<field:display name="Verifications" feature="Identification"></field:display> 
<field:display name="View Schemes Link" feature="Sectors"></field:display> 
<field:display name="Vote" feature="Budget"></field:display> 
<field:display name="Web Resource Description" feature="Web Resources"></field:display> 
<field:display name="Web Resources Title" feature="Web Resources"></field:display> 
<field:display name="Web Resources Url" feature="Web Resources"></field:display> 
<field:display name="Without Baseline Button Performance" feature="Portfolio Dashboard"></field:display> 
<field:display name="addMessageButton" feature="Messages"></field:display> 
<field:display name='Delete Regional Funding Button' feature='Regional Funding'></field:display> 
<field:display name="Contract Number" feature="Planning"></field:display>