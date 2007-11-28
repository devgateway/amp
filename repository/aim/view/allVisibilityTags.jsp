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

<module:display name="Activity Costing" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="Calendar" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="Channel Overview" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="Components" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="Contact Information" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="Cross Cutting Issues" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="DOCUMENTS MANAGEMENT"></module:display>
<module:display name="Desktop Sections" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="Document Management" parentModule="DOCUMENTS MANAGEMENT"></module:display>
<module:display name="Document" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="Financial Progress"  parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="Financial Progress" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="Funding" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="Help"></module:display>
<module:display name="Issues" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="M & E" parentModule="MONITORING AND EVALUATING"></module:display>
<module:display name="MONITORING AND EVALUATING"></module:display>
<module:display name="NATIONAL PLAN DASHBOARD"></module:display>
<module:display name="National Planning Dashboard" parentModule="NATIONAL PLAN DASHBOARD"></module:display>
<module:display name="Organizations" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="PROJECT MANAGEMENT"></module:display>
<module:display name="Paris Indicators" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="Physical Progress" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="Previews" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="Program" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="Project ID and Planning" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="REPORTING"></module:display>
<module:display name="References" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="Reports" parentModule="REPORTING"></module:display>
<module:display name="Scenarios" parentModule="PROJECT MANAGEMENT"></module:display>
<module:display name="TREND ANALYSIS"></module:display>
<module:display name="Trend Analysis and Forecasting" parentModule="TREND ANALYSIS"></module:display>

<feature:display  name="Funding Organizations" module="Funding"></feature:display>
<feature:display module="Contact Information" name="Mofed Contact Information"></feature:display>
<feature:display module="Funding" name="Funding Organizations"></feature:display>
<feature:display module="Funding" name="MTEF Projections"></feature:display>
<feature:display module="Organizations" name="Beneficiary Agency"></feature:display>
<feature:display module="Organizations" name="Contracting Agency"></feature:display>
<feature:display module="Organizations" name="Executing Agency"></feature:display>
<feature:display module="Organizations" name="Implementing Agency"></feature:display>
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
<feature:display name="Components" module="Components"></feature:display>
<feature:display name="Content Repository" module="Document Management"></feature:display>
<feature:display name="Contracting Agency" module="Organizations"></feature:display>
<feature:display name="Costing Tab" module="Activity Costing"></feature:display>
<feature:display name="Costing" module="Activity Costing"></feature:display>
<feature:display name="Cross Cutting Issues" module="Cross Cutting Issues"></feature:display>
<feature:display name="Dashboard Tab" module="M & E"></feature:display>
<feature:display name="Dashboard" module="M & E"></feature:display>
<feature:display name="Documents Tab" module="Document Management"></feature:display>
<feature:display name="Documents Tab" module="Document"></feature:display>
<feature:display name="Donor Contact Information" module="Contact Information"></feature:display>
<feature:display name="Edit Activity" module="Previews"></feature:display>
<feature:display name="Executing Agency" module="Organizations"></feature:display>
<feature:display name="Financial Progress Tab" module="Financial Progress"></feature:display>
<feature:display name="Funding Organizations Tab" module="Funding"></feature:display>
<feature:display name="Funding Organizations"  module="Funding"></feature:display>
<feature:display name="Funding Organizations" module="Funding"></feature:display>
<feature:display name="Identification" module="Project ID and Planning"></feature:display>
<feature:display name="Implementing Agency" module="Organizations"></feature:display>
<feature:display name="Issues" module="Issues"></feature:display>
<feature:display name="Location" module="Project ID and Planning"></feature:display>
<feature:display name="Logframe" module="Previews"></feature:display>
<feature:display name="Mofed Contact Information" module="Contact Information"></feature:display>
<feature:display name="My Messages" module="Desktop Sections"></feature:display>
<feature:display name="My Tasks" module="Desktop Sections"></feature:display>
<feature:display name="NPD Dashboard" module="National Planning Dashboard"></feature:display>
<feature:display name="NPD Programs " module="National Planning Dashboard"></feature:display>
<feature:display name="Paris Indicators Tab" module="Paris Indicators"></feature:display>
<feature:display name="Paris Indicators" module="Paris Indicators"></feature:display>
<feature:display name="Physical Progress" module="Components"></feature:display>
<feature:display name="Physical Progress" module="Physical Progress"></feature:display>
<feature:display name="Planning" module="Project ID and Planning"></feature:display>
<feature:display name="Preview Activity" module="Previews"></feature:display>
<feature:display name="Program" module="Program"></feature:display>
<feature:display name="Project Fiche" module="Previews"></feature:display>
<feature:display name="Project Performance" module="National Planning Dashboard"></feature:display>
<feature:display name="Project Risk" module="National Planning Dashboard"></feature:display>
<feature:display name="Proposed Project Cost" module="Funding"></feature:display>
<feature:display name="References Tab" module="References"></feature:display>
<feature:display name="References" module="References"></feature:display>
<feature:display name="Regional Funding Tab" module="Funding"></feature:display>
<feature:display name="Regional Funding" module="Funding"></feature:display>
<feature:display name="Related Documents" module="Document"></feature:display>
<feature:display name="Related Documents" module="Document"></feature:display>
<feature:display name="Reports Contact Information" module="Contact Information"></feature:display>
<feature:display name="SISIN" module="Components"></feature:display>
<feature:display name="Sectors" module="Project ID and Planning"></feature:display>
<feature:display name="Web Resources" module="Document"></feature:display>
<field:display  name="Remove Location" feature="Location"></field:display>
<field:display feature="Donor Contact Information" name="Donor Email"></field:display>
<field:display feature="Donor Contact Information" name="Donor First Name"></field:display>
<field:display feature="Edit Activity" name="Edit Activity Button"></field:display>
<field:display feature="Funding Organizations" name="Type Of Assistance"></field:display>
<field:display feature="Identification" name="Activity Budget"></field:display>
<field:display feature="Identification" name="Data Source"></field:display>
<field:display feature="Identification" name="Description"></field:display>
<field:display feature="Identification" name="Objectives"></field:display>
<field:display feature="Identification" name="Organizations and Project ID"></field:display>
<field:display feature="Identification" name="Purpose"></field:display>
<field:display feature="Identification" name="Results"></field:display>
<field:display feature="Identification" name="Project Title"></field:display>
<field:display feature="MTEF Projections" name="MTEFProjections"></field:display>
<field:display feature="Mofed Contact Information" name="Mofed Email"></field:display>
<field:display feature="Mofed Contact Information" name="Mofed First Name"></field:display>
<field:display feature="Planning" name="Line Ministry Rank"></field:display>
<field:display feature="Preview Activity" name="Preview Button"></field:display>
<field:display feature="Sectors" name="Level 1 Sectors List"></field:display>
<field:display name="A.C. Chapter" feature="Identification"></field:display>
<field:display name="Accession Instrument" feature="Identification"></field:display>
<field:display name="Active Funding Organization" feature="Funding Organizations"></field:display>
<field:display name="Activity Budget" feature="Identification"></field:display>
<field:display name="Activity Created By" feature="Identification"></field:display>
<field:display name="Activity Created On" feature="Identification"></field:display>
<field:display name="Activity Updated By" feature="Identification"></field:display>
<field:display name="Activity Updated On" feature="Identification"></field:display>
<field:display name="Actors" feature="Issues"></field:display>
<field:display name="Actual Approval Date" feature="Planning" ></field:display>
<field:display name="Actual Approval Date" feature="Planning"></field:display>
<field:display name="Actual Start Date" feature="Planning"></field:display>
<field:display name="Actual/Planned Commitments" feature="Regional Funding"></field:display>
<field:display name="Actual/Planned Disbursements" feature="Regional Funding"></field:display>
<field:display name="Actual/Planned Expenditures" feature="Regional Funding"></field:display>
<field:display name="Add Actors Link" feature="Issues"></field:display>
<field:display name="Add Commitment Button" feature="Funding Organizations"></field:display>
<field:display name="Add Components Button" feature="Components"></field:display>
<field:display name="Add Disbursement Button" feature="Funding Organizations"></field:display>
<field:display name="Add Documents Button" feature="Related Documents"></field:display>
<field:display name="Add Expenditure Button" feature="Funding Organizations"></field:display>
<field:display name="Add Funding Button - Proposed Project Cost" feature="Proposed Project Cost"></field:display>
<field:display name="Add Indicator Button" feature="Activity"></field:display>
<field:display name="Add Issues Button" feature="Issues"></field:display>
<field:display name="Add Location" feature="Location"></field:display>
<field:display name="Add Measures Link" feature="Issues"></field:display>
<field:display name="Add Programs Button - National Plan Objective" feature="Program"></field:display>
<field:display name="Add Programs Button - Primary Programs" feature="Program"></field:display>
<field:display name="Add Programs Button - Secondary Programs" feature="Program"></field:display>
<field:display name="Add Regional Fundings" feature="Regional Funding"></field:display>
<field:display name="Add Scheme Link" feature="Sectors"></field:display>
<field:display name="Add Sector Level 1 Link" feature="Sectors"></field:display>
<field:display name="Add Sector Level 2 Link" feature="Sectors"></field:display>
<field:display name="Add Sector Level 3 Link" feature="Sectors"></field:display>
<field:display name="Add Sectors Button" feature="Sectors"></field:display>
<field:display name="Add Web Resource Button" feature="Web Resources"></field:display>
<field:display name="Adjustment Type Commitment" feature="Funding Organizations"></field:display>
<field:display name="Adjustment Type Disbursement" feature="Funding Organizations"></field:display>
<field:display name="Adjustment Type Expenditure" feature="Funding Organizations"></field:display>
<field:display name="Amount Commitment" feature="Funding Organizations"></field:display>
<field:display name="Amount Disbursement" feature="Funding Organizations"></field:display>
<field:display name="Amount Expenditure" feature="Funding Organizations"></field:display>
<field:display name="Base Value" feature="Activity"></field:display>
<field:display name="Beneficiary Agency Add Organizations Button" feature="Beneficiary Agency"></field:display>
<field:display name="Beneficiary Agency Remove Organizations Button" feature="Beneficiary Agency"></field:display>
<field:display name="Classification Expenditure" feature="Funding Organizations"></field:display>
<field:display name="Comments Base Value" feature="Activity"></field:display>
<field:display name="Comments Current Value" feature="Activity"></field:display>
<field:display name="Comments Revised Target Value" feature="Activity"></field:display>
<field:display name="Comments Target Value" feature="Activity"></field:display>
<field:display name="Component Name" feature="Issues"></field:display>
<field:display name="Component description" feature="Issues"></field:display>
<field:display name="Components Actual/Planned Commitments" feature="Components"></field:display>
<field:display name="Components Actual/Planned Disbursements" feature="Components"></field:display>
<field:display name="Components Actual/Planned Expenditures" feature="Components"></field:display>
<field:display name="Add Physical Progress Link" feature="Physical Progress"></field:display>
<field:display name="Components Agency Source" feature="SISIN"></field:display>
<field:display name="Components Classification Program Code" feature="SISIN"></field:display>
<field:display name="Components Currency Commitments" feature="Components"></field:display>
<field:display name="Components Currency Disbursements" feature="Components"></field:display>
<field:display name="Components Currency Expenditures" feature="Components"></field:display>
<field:display name="Components Date Commitments" feature="Components"></field:display>
<field:display name="Components Date Disbursements" feature="Components"></field:display>
<field:display name="Components Date Expenditures" feature="Components"></field:display>
<field:display name="Components Financing Source" feature="SISIN"></field:display>
<field:display name="Components Grand Total Commitments" feature="Components"></field:display>
<field:display name="Components Grand Total Disbursements" feature="Components"></field:display>
<field:display name="Components Grand Total Expenditures" feature="Components"></field:display>
<field:display name="Components Localization" feature="SISIN"></field:display>
<field:display name="Components Perspective Commitments" feature="Components"></field:display>
<field:display name="Components Perspective Disbursements" feature="Components"></field:display>
<field:display name="Components Perspective Expenditures" feature="Components"></field:display>
<field:display name="Components Physical Progress" feature="Components"></field:display>
<field:display name="Components Remove Managed Documents Button" feature="Managed Documents"></field:display>
<field:display name="Remove Physical Progress Link" feature="Physical Progress"></field:display>
<field:display name="Components Stage" feature="SISIN"></field:display>
<field:display name="Components Total Amount Commitments" feature="Components"></field:display>
<field:display name="Components Total Amount Disbursements" feature="Components"></field:display>
<field:display name="Components Total Amount Expenditures" feature="Components"></field:display>
<field:display name="Conditions for Fund Release" feature="Funding Organizations"></field:display>
<field:display name="Contact Name" feature="Reports Contact Information"></field:display>
<field:display name="Contracting Agency Add Organizations Button" feature="Contracting Agency"></field:display>
<field:display name="Contracting Agency Remove Organizations Button" feature="Contracting Agency"></field:display>
<field:display name="Costing Activity Name" feature="Costing"></field:display>
<field:display name="Costing Assumptions" feature="Costing"></field:display>
<field:display name="Costing Contribution Gap" feature="Costing"></field:display>
<field:display name="Costing Donor" feature="Costing"></field:display>
<field:display name="Costing Due Date" feature="Costing"></field:display>
<field:display name="Costing Grand Total Contribution" feature="Costing"></field:display>
<field:display name="Costing Grand Total Cost" feature="Costing"></field:display>
<field:display name="Costing Inputs" feature="Costing"></field:display>
<field:display name="Costing Progress" feature="Costing"></field:display>
<field:display name="Costing Total Contribution" feature="Costing"></field:display>
<field:display name="Costing Total Cost" feature="Costing"></field:display>
<field:display name="Cumulative Commitment" feature="Funding Organizations"></field:display>
<field:display name="Cumulative Disbursement" feature="Funding Organizations"></field:display>
<field:display name="Currency Commitment" feature="Funding Organizations"></field:display>
<field:display name="Currency Commitments" feature="Regional Funding"></field:display>
<field:display name="Currency Disbursement" feature="Funding Organizations"></field:display>
<field:display name="Currency Disbursements" feature="Regional Funding"></field:display>
<field:display name="Currency Expenditure" feature="Funding Organizations"></field:display>
<field:display name="Currency Expenditures" feature="Regional Funding"></field:display>
<field:display name="Current Completion Date" feature="Planning"></field:display>
<field:display name="Current Value" feature="Activity"></field:display>
<field:display name="Data Source" feature="Identification"></field:display>
<field:display name="Date Base Value" feature="Activity"></field:display>
<field:display name="Date Commitment" feature="Funding Organizations"></field:display>
<field:display name="Date Commitments" feature="Regional Funding"></field:display>
<field:display name="Date Current Value" feature="Activity"></field:display>
<field:display name="Date Disbursement" feature="Funding Organizations"></field:display>
<field:display name="Date Disbursements" feature="Regional Funding"></field:display>
<field:display name="Date Expenditure" feature="Funding Organizations"></field:display>
<field:display name="Date Expenditures" feature="Regional Funding"></field:display>
<field:display name="Date Revised Target Value" feature="Activity"></field:display>
<field:display name="Date Target Value" feature="Activity"></field:display>
<field:display name="Date Team Leader" feature="Identification"></field:display>
<field:display name="Delegated Cooperation" feature="Funding Organizations"></field:display>
<field:display name="Delegated Partner" feature="Funding Organizations"></field:display>
<field:display name="Description" feature="Identification"></field:display>
<field:display name="Document Comment" feature="Related Documents"></field:display>
<field:display name="Document Date" feature="Related Documents"></field:display>
<field:display name="Document Description" feature="Related Documents"></field:display>
<field:display name="Document FileName" feature="Related Documents"></field:display>
<field:display name="Document Language" feature="Related Documents"></field:display>
<field:display name="Document Title" feature="Related Documents"></field:display>
<field:display name="Document Type" feature="Related Documents"></field:display>
<field:display name="Donor Agency" feature="Funding Organizations"></field:display>
<field:display name="Donor Commitment Date" feature="Funding Organizations"></field:display>
<field:display name="Donor Email" feature="Donor Contact Information"></field:display>
<field:display name="Donor Fax Number" feature="Donor Contact Information"></field:display>
<field:display name="Donor First Name" feature="Donor Contact Information"></field:display>
<field:display name="Donor Group" feature="Funding Organizations"></field:display>
<field:display name="Donor Last Name" feature="Donor Contact Information"></field:display>
<field:display name="Donor Organization" feature="Donor Contact Information"></field:display>
<field:display name="Donor Phone Number" feature="Donor Contact Information"></field:display>
<field:display name="Donor Title" feature="Donor Contact Information"></field:display>
<field:display name="Draft" feature="Identification"></field:display>
<field:display name="Edit Components Link" feature="Components"></field:display>
<field:display name="Edit Funding Button- Proposed Project Cost" feature="Proposed Project Cost"></field:display>
<field:display name="Edit Funding Link" feature="Regional Funding"></field:display>
<field:display name="Exchange Rate" feature="Funding Organizations"></field:display>
<field:display name="Executing Agency Add Organizations Button" feature="Executing Agency"></field:display>
<field:display name="Executing Agency Remove Organizations Button" feature="Executing Agency"></field:display>
<field:display name="FY" feature="Budget"></field:display>
<field:display name="Final Date for Contracting" feature="Planning"></field:display>
<field:display name="Final Date for Disbursements" feature="Planning"></field:display>
<field:display name="Financial Instrument" feature="Budget"></field:display>
<field:display name="Financing Instrument" feature="Funding Organizations"></field:display>
<field:display name="Funding Organization Id" feature="Funding Organizations"></field:display>
<field:display name="Funding Organization Name" feature="Funding Organizations"></field:display>
<field:display name="Funding Organization" feature="Funding Organizations"></field:display>
<field:display name="Government Approval Procedures" feature="Budget"></field:display>
<field:display name="Grand Total Commitments" feature="Components"></field:display>
<field:display name="Grand Total Disbursements" feature="Components"></field:display>
<field:display name="Implementation Level" feature="Location"></field:display>
<field:display name="Implementation Location" feature="Location"></field:display>
<field:display name="Implementing Agency Add Organizations Button" feature="Implementing Agency"></field:display>
<field:display name="Implementing Agency Remove Organizations Button" feature="Implementing Agency"></field:display>
<field:display name="Indicator Base Value" feature="Activity"></field:display>
<field:display name="Indicator Current Value" feature="Activity"></field:display>
<field:display name="Indicator Description" feature="Activity"></field:display>
<field:display name="Indicator ID" feature="Activity"></field:display>
<field:display name="Indicator Name" feature="Activity"></field:display>
<field:display name="Indicator Target Value" feature="Activity"></field:display>
<field:display name="Issues" feature="Issues"></field:display>
<field:display name="Joint Criteria" feature="Budget"></field:display>
<field:display name="Lessons Learned" feature="Identification"></field:display>
<field:display name="Level 1 Sectors List" feature="Sectors"></field:display>
<field:display name="Level 2 Sectors List" feature="Sectors"></field:display>
<field:display name="Level 3 Sectors List" feature="Sectors"></field:display>
<field:display name="Line Ministry Rank" feature="Planning"></field:display>
<field:display name="Logframe Category" feature="Logframe"></field:display>
<field:display name="Logframe Preview Button" feature="Logframe" ></field:display>
<field:display name="Measures Taken" feature="Issues"></field:display>
<field:display name="Ministry of Planning Rank" feature="Planning"></field:display>
<field:display name="Mofed Email" feature="Mofed Contact Information"></field:display>
<field:display name="Mofed Fax Number" feature="Mofed Contact Information"></field:display>
<field:display name="Mofed First Name" feature="Mofed Contact Information"></field:display>
<field:display name="Mofed Last Name" feature="Mofed Contact Information"></field:display>
<field:display name="Mofed Organization" feature="Mofed Contact Information"></field:display>
<field:display name="Mofed Phone Number" feature="Mofed Contact Information"></field:display>
<field:display name="Mofed Title" feature="Mofed Contact Information"></field:display>
<field:display name="Objectives" feature="Identification"></field:display>
<field:display name="Organizations Selector" feature="Funding Organizations"></field:display>
<field:display name="Organizations and Project ID" feature="Identification"></field:display>
<field:display name="Overall Contribution" feature="Planning"></field:display>
<field:display name="Overall Cost" feature="Planning"></field:display>
<field:display name="Perspective Commitment" feature="Funding Organizations"></field:display>
<field:display name="Perspective Commitments" feature="Regional Funding"></field:display>
<field:display name="Perspective Disbursement" feature="Funding Organizations"></field:display>
<field:display name="Perspective Expenditure" feature="Funding Organizations"></field:display>
<field:display name="Physical Progress" feature="Issues"></field:display>
<field:display name="Physical progress description" feature="Issues"></field:display>
<field:display name="Physical progress title" feature="Issues"></field:display>
<field:display name="Printer Friendly Button Performance" feature="Dashboard"></field:display>
<field:display name="Printer Friendly Button Risk" feature="Dashboard"></field:display>
<field:display name="Program Background" feature="Admin NPD"></field:display>
<field:display name="Program Beneficiaries" feature="Admin NPD"></field:display>
<field:display name="Program Code" feature="Admin NPD"></field:display>
<field:display name="Program Description" feature="Admin NPD"></field:display>
<field:display name="Program Description" feature="Program"></field:display>
<field:display name="Program Environment Considerations" feature="Admin NPD"></field:display>
<field:display name="Program Lead Agency" feature="Admin NPD"></field:display>
<field:display name="Program Name" feature="Admin NPD"></field:display>
<field:display name="Program Objectives" feature="Admin NPD"></field:display>
<field:display name="Program Outputs" feature="Admin NPD"></field:display>
<field:display name="Program Target Groups" feature="Admin NPD"></field:display>
<field:display name="Program Type" feature="Admin NPD"></field:display>
<field:display name="Project Code" feature="Budget"></field:display>
<field:display name="Project Fiche Button" feature="Project Fiche" ></field:display>
<field:display name="Project Id" feature="Identification"></field:display>
<field:display name="Project Title" feature="Identification"></field:display>
<field:display name="Projection Amount" feature="MTEF Projections"></field:display>
<field:display name="Projection Currency Code" feature="MTEF Projections"></field:display>
<field:display name="Projection Date" feature	="MTEF Projections"></field:display>
<field:display name="Projection Name" feature="MTEF Projections"></field:display>
<field:display name="Proposed Approval Date" feature="Planning"></field:display>
<field:display name="Proposed Completion Date" feature="Planning"></field:display>
<field:display name="Proposed Project Amount" feature="Proposed Project Cost"></field:display>
<field:display name="Proposed Project Currency" feature="Proposed Project Cost"></field:display>
<field:display name="Proposed Project Date" feature="Proposed Project Cost"></field:display>
<field:display name="Proposed Project Planned" feature="Proposed Project Cost"></field:display>
<field:display name="Proposed Start Date" feature="Planning"></field:display>
<field:display name="Purpose" feature="Identification"></field:display>
<field:display name="Region" feature="Location"></field:display>
<field:display name="Regional Funding Perspective Disbursements" feature="Regional Funding"></field:display>
<field:display name="Regional Funding Perspective Expenditures" feature="Regional Funding"></field:display>
<field:display name="Remove Actors Button" feature="Issues"></field:display>
<field:display name="Remove Components Button" feature="Components"></field:display>
<field:display name="Remove Documents Button" feature="Related Documents"></field:display>
<field:display name="Remove Funding Button - Proposed Project Cost" feature="Proposed Project Cost"></field:display>
<field:display name="Remove Fundings" feature="Regional Funding"></field:display>
<field:display name="Remove Issues Button" feature="Issues"></field:display>
<field:display name="Remove Measures Button" feature="Issues"></field:display>
<field:display name="Remove Program Button - National Plan Objective" feature="Program"></field:display>
<field:display name="Remove Program Button - Primary Programs" feature="Program"></field:display>
<field:display name="Remove Sectors Button" feature="Sectors"></field:display>
<field:display name="Remove Web Resource Button" feature="Web Resources"></field:display>
<field:display name="Results" feature="Identification"></field:display>
<field:display name="Revised Target Value" feature="Activity"></field:display>
<field:display name="Risk" feature="Activity"></field:display>
<field:display name="SISIN Code" feature="SISIN"></field:display>
<field:display name="SISIN Sector" feature="SISIN"></field:display>
<field:display name="Sector" feature="Sectos"></field:display>
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
<field:display name="Sub-Vote" feature="Budget"></field:display>
<field:display name="Target Value" feature="Activity"></field:display>
<field:display name="Team" feature="Identification"></field:display>
<field:display name="Total Amount Commitments" feature="Regional Funding"></field:display>
<field:display name="Total Amount Disbursements" feature="Regional Funding"></field:display>
<field:display name="Total Amount Expenditures" feature="Regional Funding"></field:display>
<field:display name="Total Committed" feature="Funding Organizations"></field:display>
<field:display name="Total Costs" feature="Costing"></field:display>
<field:display name="Total Disbursed" feature="Funding Organizations"></field:display>
<field:display name="Total Donor Commitments" feature="Regional Funding"></field:display>
<field:display name="Total Donor Disbursements" feature="Regional Funding"></field:display>
<field:display name="Total Donor Expenditures" feature="Regional Funding"></field:display>
<field:display name="Total Expended" feature="Funding Organizations"></field:display>
<field:display name="Type Of Assistance" feature="Funding Organizations"></field:display>
<field:display name="Undisbursed Funds" feature="Funding Organizations"></field:display>
<field:display name="Unexpended Funds" feature="Funding Organizations"></field:display>
<field:display name="View Schemes Link" feature="Sectors"></field:display>
<field:display name="Vote" feature="Budget"></field:display>
<field:display name="Web Resource Description" feature="Web Resources"></field:display>
<field:display name="Web Resources Title" feature="Web Resources"></field:display>
<field:display name="Web Resources Url" feature="Web Resources"></field:display>
<field:display name="Without Baseline Button Performance" feature="Dashboard"></field:display>
	