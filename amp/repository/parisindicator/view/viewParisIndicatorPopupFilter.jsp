<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category"%>





<style type="text/css">
<!--

.clear,.innerTab {
	clear: both;
}

div#filterContainer {
	width: 740px;
	min-height: 350px;
	margin: 0 auto;
	background: #fff;
}

div.filterContainerHeader {
	background: #e9e9fb;
	border-bottom: 1px solid #bfd4e0;
	height: 26px;
}

div.filterContainerHeader h2 {
	line-height: 26px;
	font-size: 12px;
	font-weight: bold;
	text-indent: 11px;
	width: 90%;
	float: left;
}



div.filterContainerTabs {
	width: 740px;
	margin: 0 auto;
	margin-top: 11px;
}

div.groupingSelector {
	width: 288px;
	float: left;
	border: 1px solid #cccccc;
	border-right: none;
	height: 155px;
}



div.membersSelector {
	width: 425px;
	float: right;
	border: 1px solid #cccccc;
	height: 155px;
}

div.innerTabHeader {
	border-bottom: 1px solid #cccccc;
	background: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif);
	height: 31px;
}

div.innerTabHeader h3 {
	height: 31px;
	line-height: 31px;
	text-indent: 11px;
	width: 40%;
	float: left;
	font-size: 11px;
	color: #767676;
	margin: 0;
	padding: 0;
}

div.membersSelector div.memsearch {
	padding: 1px 0px 0px 0px;
	width: 230px;
	float: right;
	text-align: right;
}



.innerTabHeader input.buttonx {
	font-size: 11px;
}

.groupingSelector ul {
	margin-bottom: 15px;
	width: 285px;
	
}

.groupingSelector ul li {
	list-style: none;
	border-bottom: 1px solid #d0d0d0;
}

.groupingSelector ul li a {
	padding: 5px;
	font-size: 12px;
	display: block;
	background: #fff;
	color: #000;
	text-decoration: none;
}

.groupingSelector ul li a:hover {
	background: #bfd2df;
}


div.innerTab {
	height: 115px;
	overflow-x: hidden;
	overflow-y: scroll;
	margin: 2px;
	width: 99%;
}


.datesCurrenceyFormFooter {
	padding: 15px 0px 0px 0px;
	clear: both;
}

.datesCurrenceyFormFooter #calendarForm label {
	width: 100px;
	font-size: 11px;
	display: inline-block;
}

p {
	padding: 0px 0px 10px 0px;
}

.datesCurrenceyFormFooter select {
	background-color: #FFFFFF;
	border: 1px solid #D0D0D0;
	color: #000;
	font-size: 11px;
	padding: 0px;
	width: 150px;
}


.tabSubmit {
	text-align: center;
	padding: 0px 0px 5px 0px;
}

.groupingSelectors li a span {
	display: inline-block;
	float: right;
	font-size: 11px;
}
.groupingSelector,  .groupingSelectors{
	margin: 0;
	padding: 0;
}


label {
	text-indent: 10px;
}
li table{
	visibility:visible; 
}

#status_sector_search, #donorTab_search, #financingsInsTab_search
{
	width: 150px;
}

-->
</style>
<script type="text/javascript">
var tabView = new YAHOO.widget.TabView('demo');
</script>


<digi:instance property="parisIndicatorForm" />
<html:hidden property="defaultStartYear"/>
<html:hidden property="defaultEndYear"/>
<html:hidden property="defaultCalendar"/>
<html:hidden property="defaultCurrency"/>
<bean:define id="sy" name="parisIndicatorForm" property="startYears"
	type="int[]"></bean:define>
<bean:define id="ey" name="parisIndicatorForm" property="endYears"
	type="int[]"></bean:define>
<bean:define id="form" name="parisIndicatorForm"
	type="org.digijava.ampModule.parisindicator.form.PIForm"></bean:define>
<bean:define id="reqBeanSetterObject" toScope="request"
	name="parisIndicatorForm" />
<div id="filterContainer" style="display: none;" class="yui-skin-sam">
	<div class="filterContainerTabs">
		<div id="demo" class="yui-navset">
			<ul class="yui-nav">
				<li class="selected"><a href="#tab1"><div><digi:trn>Dates and Currency</digi:trn>
					</div>
				</a>
				</li>
				<li><a href="#tab2"><div><digi:trn>Groups and Donors</digi:trn></div>
				</a>
				</li>
				<li><a href="#tab3"><div><digi:trn>Status and Sectors</digi:trn></div>
				</a>
				</li>
			</ul>
			<div class="yui-content" style="background-color: #FFFFFF;">
				<div>
					<div id="tabDatesCurrencey">
						<bean:define name="parisIndicatorForm" id="element"
							property="financingInstrumentsElements" toScope="request" />
						<div class="groupingSelector">
							<div class="innerTabHeader">
								<h3>
									<digi:trn>Grouping Selector</digi:trn>
								</h3>
							</div>
							<span class="clear"></span>
							<div class="innerTab">
								<ul class="groupingSelectors">
									<c:forEach var="element"
										items="${parisIndicatorForm.financingInstrumentsElements}">
										<li><a href="#" onclick="showFilterDiv('${element.htmlDivId}','financingsInsTab_search');return false;"><digi:trn>${element.name}</digi:trn>
												(${element.rootHierarchyListable.countDescendants-1}) </a></li>
									</c:forEach>
								</ul>
							</div>
						</div>
						<div class="membersSelector">
							<div class="innerTabHeader">
								<h3><digi:trn>Members selector</digi:trn></h3>
								 <div class="memsearch">
									<input onkeypress="getSearchManagerInstanceByEl(this).clear()" id="financingsInsTab_search" type="text"   class="inputx" />
									 <input type="button" class="buttonx" onclick="getSearchManagerInstanceById('financingsInsTab_search').findPrev()"  value='&lt;&lt;' />
									 <input type="button" onclick="getSearchManagerInstanceById('financingsInsTab_search').findNext()"  class="buttonx" value="&gt;&gt;"/>
								</div>
							</div>
							<c:forEach var="element"
								items="${parisIndicatorForm.financingInstrumentsElements}">
								<div class="innerTab" style="display: none;"
									id="${element.htmlDivId}">
									<bean:define id="reqEntityList" name="element"
										property="rootHierarchyListable.children" toScope="request" />
									<bean:define id="reqSelectedEntityIds" toScope="request">${element.actionFormProperty}</bean:define>
									<ul style="list-style-type: none;margin: 0; padding: 0;">
										<li><input type="checkbox"
											onclick="toggleCheckChildren(this)" class="root_checkbox" />
											<span style="font-family: Arial,sans-serif; font-size: 12px;"> <digi:trn>${element.rootHierarchyListable.label}</digi:trn>
										</span> <jsp:include
												page="/repository/aim/view/filters/hierarchyLister.jsp" />
										</li>
									</ul>
								</div>
							</c:forEach>
						</div>

					</div>
					<div class="datesCurrenceyFormFooter">
					<div id="calendarForm">
						<div style="width: 40%; float: left;">
							<p>
								<label><digi:trn>Calendar</digi:trn>
								</label>
								<html:select property="selectedCalendar"
									styleId="selectedCalendar">
									<logic:notEmpty name="parisIndicatorForm" property="calendars">
										<html:optionsCollection property="calendars"
											value="ampFiscalCalId" label="name" />
									</logic:notEmpty>
								</html:select>
							</p>
							<p>
								<label>Currency type</label>
								<html:select property="selectedCurrency"
									name="parisIndicatorForm" styleId="selectedCurrency">
									<logic:notEmpty name="parisIndicatorForm"
										property="currencyTypes">
										<html:optionsCollection name="parisIndicatorForm"
											property="currencyTypes" value="currencyCode"
											label="currencyName" />
									</logic:notEmpty>
								</html:select>
							</p>
						</div>
						<div style="width: 60%; float: left;">
							<p>
								<label><digi:trn>Start Year</digi:trn> </label>
								<%
										String selected = "";
									%>
								<html:select property="selectedStartYear"
									styleId="selectedStartYear">
									<%
											for (int i = 0; i < sy.length; i++) {
										%>
									<%
											selected = (sy[i] == form.getSelectedStartYear()) ? "selected='selected'"
															: "";
										%>
									<option value='<%=sy[i]%>' <%=selected%>><%=sy[i]%></option>
									<%
											}
										%>
								</html:select>
							</p>
							<p>
								<label><digi:trn>End Year</digi:trn> </label>
								<html:select property="selectedEndYear"
									styleId="selectedEndYear">
									<%
											for (int i = 0; i < sy.length; i++) {
										%>
									<%
											selected = (sy[i] == form.getSelectedEndYear()) ? "selected='selected'"
															: "";
										%>
									<option value='<%=sy[i]%>' <%=selected%>><%=sy[i]%></option>
									<%
											}
										%>
								</html:select>
							</p>


						</div>
						<div class="clear"></div>
						<div class="tabSubmit">
							<input type="button" class="buttonx_sm" style="font-size: 10px;" 
								value="<digi:trn>Apply Filters to the Report</digi:trn>" onclick="submitFilters();"/>
							<input type="button" style="font-size: 10px;"
								class="buttonx_sm" value="<digi:trn>Reset and Start Over</digi:trn>" onclick="resetPIFilters();"/>
						</div>
					</div>
					</div>
					<span class="clear"></span>
				</div>
				<div>
					<div>
						<bean:define name="parisIndicatorForm" id="element"
							property="donorElements" toScope="request" />
						<div class="groupingSelector">
							<div class="innerTabHeader">
								<h3>
									<digi:trn>Grouping Selector</digi:trn>
								</h3>
							</div>
							<span class="clear"></span>
							<div class="innerTab">
								<ul class="groupingSelectors">
									<c:forEach var="element"
										items="${parisIndicatorForm.donorElements}">
										<li><a  href="#" onclick="showFilterDiv('${element.htmlDivId}','donorTab_search');return false;"><digi:trn>${element.name}</digi:trn>
												(${element.rootHierarchyListable.countDescendants-1}) </a>
										</li>
									</c:forEach>
								</ul>
							</div>
						</div>
						<div class="membersSelector">
						<div class="innerTabHeader">
								<h3><digi:trn>Members selector</digi:trn></h3>
								 <div class="memsearch">
									<input onkeypress="getSearchManagerInstanceByEl(this).clear()" id="donorTab_search" type="text"   class="inputx" />
									 <input type="button" class="buttonx" onclick="getSearchManagerInstanceById('donorTab_search').findPrev()"  value='&lt;&lt;' />
									 <input type="button" onclick="getSearchManagerInstanceById('donorTab_search').findNext()"  class="buttonx" value="&gt;&gt;"/>
									 </div>
							</div>
							<c:forEach var="element"
								items="${parisIndicatorForm.donorElements}">
								<div class="innerTab" style="display: none;"
									id="${element.htmlDivId}">
									<bean:define id="reqEntityList" name="element"
										property="rootHierarchyListable.children" toScope="request" />
									<bean:define id="reqSelectedEntityIds" toScope="request">${element.actionFormProperty}</bean:define>
									<ul style="list-style-type: none;margin: 0; padding: 0;">
										<li><input type="checkbox"
											onclick="toggleCheckChildren(this)" class="root_checkbox" />
											<span style="font-family: Arial,sans-serif; font-size: 12px;"> <digi:trn>${element.rootHierarchyListable.label}</digi:trn>
										</span> <jsp:include
												page="/repository/aim/view/filters/hierarchyLister.jsp" />
										</li>
									</ul>
								</div>
							</c:forEach>
						</div>
						<div class="clear"></div>
						<div class="tabSubmit">
							<input type="button" class="buttonx_sm" style="font-size: 10px;"
								value="<digi:trn>Apply Filters to the Report</digi:trn>"
								onclick="submitFilters();" />
								<input type="button" style="font-size: 10px;"
								class="buttonx_sm" value="<digi:trn>Reset and Start Over</digi:trn>" onclick="resetPIFilters();"/>
						</div>

					</div>
					<span class="clear"></span>

				</div>

				<div>
				<div>
						<bean:define name="parisIndicatorForm" id="element"
							property="sectorStatusesElements" toScope="request" />
						<div class="groupingSelector">
							<div class="innerTabHeader">
								<h3>
									<digi:trn>Grouping Selector</digi:trn>
								</h3>
							</div>
							<span class="clear"></span>
							<div class="innerTab">
								<ul class="groupingSelectors">
									<c:forEach var="element"
										items="${parisIndicatorForm.sectorStatusesElements}">
										<li><a href="#" onclick="showFilterDiv('${element.htmlDivId}','status_sector_search');return false;"><digi:trn>${element.name}</digi:trn>
												(${element.rootHierarchyListable.countDescendants-1}) </a>
										</li>
									</c:forEach>
								</ul>
							</div>
						</div>
						<div class="membersSelector">
								<div class="innerTabHeader">
								<h3><digi:trn>Members selector</digi:trn></h3>
								 <div class="memsearch">
									<input onkeypress="getSearchManagerInstanceByEl(this).clear()" id="status_sector_search" type="text"   class="inputx" />
									 <input type="button" class="buttonx" onclick="getSearchManagerInstanceById('status_sector_search').findPrev()"  value='&lt;&lt;' />
									 <input type="button" onclick="getSearchManagerInstanceById('status_sector_search').findNext()"  class="buttonx" value="&gt;&gt;"/>
									 </div>
							</div>
							<c:forEach var="element"
								items="${parisIndicatorForm.sectorStatusesElements}">
								<div class="innerTab" style="display: none;"
									id="${element.htmlDivId}">
									<bean:define id="reqEntityList" name="element"
										property="rootHierarchyListable.children" toScope="request" />
									<bean:define id="reqSelectedEntityIds" toScope="request">${element.actionFormProperty}</bean:define>
									<ul style="list-style-type: none;margin: 0; padding: 0;">
										<li><input type="checkbox"
											onclick="toggleCheckChildren(this)" class="root_checkbox" />
											<span style="font-family: Arial,sans-serif; font-size: 12px;"> <digi:trn>${element.rootHierarchyListable.label}</digi:trn>
										</span> <jsp:include
												page="/repository/aim/view/filters/hierarchyLister.jsp" />
										</li>
									</ul>
								</div>
							</c:forEach>
						</div>
						<div class="clear"></div>
						<div class="tabSubmit">
							<input type="button" class="buttonx_sm" style="font-size: 10px;"
								value="<digi:trn>Apply Filters to the Report</digi:trn>"
								onclick="submitFilters();" />
								<input type="button" style="font-size: 10px;"
								class="buttonx_sm" value="<digi:trn>Reset and Start Over</digi:trn>" onclick="resetPIFilters();"/>
						</div>

					</div>
					<span class="clear"></span>
				</div>
			</div>
		</div>
	</div>
</div>


