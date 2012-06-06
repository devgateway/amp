<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>

<%@page import="org.digijava.module.aim.util.FeaturesUtil"%>
<%@page import="org.digijava.module.aim.dbentity.AmpGlobalSettings"%>
<%@page import="java.util.Collections"%>
<%@page import="org.dgfoundation.amp.ar.ArConstants"%>

<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/tabview.css" />
<digi:instance property="gisDashboardForm" />
<digi:form action="/showGisDashboard.do">
<bean:define id="reqBeanSetterObject" toScope="request" name="gisDashboardForm"/>

<div id="tabview_container" class="yui-navset" style="display: block; overflow: hidden; height: 80%; padding-bottom: 0px;margin-top: 15px;margin-left: 5px;margin-right: 5px">
	<ul class="yui-nav" style="border-bottom: 1px solid #CCCCCC">
		<li class="selected"><a href="#donorsTab"><div><digi:trn>Donor Agencies</digi:trn></div></a></li>
		<li><a href="#sectorsTab"><div><digi:trn>Sectors</digi:trn></div></a></li>
		<li><a href="#programsTab"><div><digi:trn>Programs</digi:trn></div></a></li>
	</ul>
	<div class="yui-content" style="height: 92%;margin-top: 10px;" >
		<div id="donorsTab" style="height: 91%;">
			<div class="grayBorder">
				<bean:define id="reqElements" toScope="request" name="gisDashboardForm" property="donorElements" />
				<bean:define id="reqPropertyObj" toScope="request" value="donorsPropertyObj" />
				<bean:define id="reqSearchManagerId" toScope="request" value="donorsTab_search" />
				<jsp:include page="gisFilterTable.jsp"/>
			</div>
		</div>		
		<div id="sectorsTab" class="yui-hidden"  style="height: 91%;">
			<div class="grayBorder">
				<bean:define id="reqElements" toScope="request" name="gisDashboardForm" property="sectorElements" />
				<bean:define id="reqPropertyObj" toScope="request" value="sectorsPropertyObj" />
				<bean:define id="reqSearchManagerId" toScope="request" value="sectorsTab_search" />
				<jsp:include page="gisFilterTable.jsp"/>
			</div>
		</div>
		<div id="programsTab" class="yui-hidden"  style="height: 91%;" >
			<div class="grayBorder">
				<bean:define id="reqElements" toScope="request" name="gisDashboardForm" property="programElements" />
				<bean:define id="reqPropertyObj" toScope="request" value="programsPropertyObj" />
				<bean:define id="reqSearchManagerId" toScope="request" value="programsTab_search" />
				<jsp:include page="gisFilterTable.jsp"/>
			</div>
		</div>
	</div>
</div>
<div style="width:50%; float:left;font-size: 11px;white-space: nowrap;">
	<span style="width: 150px"><digi:trn>Source of Data</digi:trn></span>
	<select id="mapModeFin" style="width:150px" value="commitment">
	    <option value="fundingData"><digi:trn>Activity Funding Data</digi:trn></option>
		<option value="pledgesData"><digi:trn>Pledges Data</digi:trn></option>
	</select>							
</div>
<div style="display: block; overflow:hidden;width:50%;font-size: 11px; float:right;white-space: nowrap;">
		<span style="width: 150px"><digi:trn>Funding type</digi:trn></span>
	<select id="fundingType" onChange="" style="width:150px" value="commitment">
	    <field:display name="Measure Commitment" feature="GIS DASHBOARD">
	     	<option value="commitment"><digi:trn>Commitment</digi:trn></option>
	    </field:display>
	    <field:display name="Measure Disbursement" feature="GIS DASHBOARD">
	       	<option value="disbursement"><digi:trn>Disbursement</digi:trn></option>
	    </field:display>
	    <field:display name="Measure Expenditure" feature="GIS DASHBOARD">
	       	<option value="expenditure"><digi:trn>Expenditure</digi:trn></option>
		</field:display>
	</select>
</div>
<div style="width:50%; float:left;font-size: 11px;white-space: nowrap;">
	<span style="width: 150px"><digi:trn>Calendar</digi:trn></span>
	
	<select id="mapModeFin" style="width:150px" value="commitment">
	    <option value="fundingData"><digi:trn>Activity Funding Data</digi:trn></option>
		<option value="pledgesData"><digi:trn>Pledges Data</digi:trn></option>
	</select>							
</div>
<div style="display: block; overflow:hidden;width:50%;font-size: 11px; float:right;white-space: nowrap;">
	<span style="width: 150px"><digi:trn>Currency</digi:trn></span>
	<select id="fundingType" onChange="" style="width:150px" value="commitment">
	    <field:display name="Measure Commitment" feature="GIS DASHBOARD">
	     	<option value="commitment"><digi:trn>Commitment</digi:trn></option>
	    </field:display>
	    <field:display name="Measure Disbursement" feature="GIS DASHBOARD">
	       	<option value="disbursement"><digi:trn>Disbursement</digi:trn></option>
	    </field:display>
	    <field:display name="Measure Expenditure" feature="GIS DASHBOARD">
	       	<option value="expenditure"><digi:trn>Expenditure</digi:trn></option>
		</field:display>
	</select>
</div>


<div style="clear:both;text-align:center;padding:2px 0px 0px 0px;">
	<hr>
	<input type='button' value='<digi:trn>Apply</digi:trn>' onClick='applySectorFilter()' class="buttonx">
	&nbsp;
	<input type='button' value='<digi:trn>Cancel</digi:trn>' onClick='hidePanel(0)' class="buttonx">
</div>


</digi:form>
