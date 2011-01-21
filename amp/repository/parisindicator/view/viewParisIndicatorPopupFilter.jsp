<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category"%>

<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/tabview.css" />

<digi:instance property="parisIndicatorForm"/>
<bean:define id="sy" name="parisIndicatorForm" property="startYears" type="int[]"></bean:define>
<bean:define id="ey" name="parisIndicatorForm" property="endYears" type="int[]"></bean:define>
<bean:define id="form" name="parisIndicatorForm" type="org.digijava.module.parisindicator.form.PIForm"></bean:define>

<div id="myFilter" style="display: none;">
    <div id="tabview_container" class="yui-navset">
        <ul class="yui-nav">
            <li class="selected">
                <a href="#keyword"><div><digi:trn>Dates and Currency</digi:trn></div></a>
            </li>
            <li>
                <a href="#financing"><div><digi:trn>Groups and Donors</digi:trn></div></a> 
            </li>
            <li>
                <a href="#sectorsgroups"><div><digi:trn>Status and Sectors</digi:trn></div></a>
            </li>
        </ul>
        <div class="yui-content" style="background-color: #EEEEEE">
            <div id="keyword" class="yui-tab-content">
                <br />
                    <table width="100%" style="vertical-align: top;" align="center" cellpadding="7px" cellspacing="7px" >
                        <tr valign="top">
                            <td align="center">
                                <table align="center" cellpadding="2" cellspacing="2">
                                    <tr bgcolor="#EEEEEE">
                                        <td colspan="5">
                                            <b><digi:trn key="rep:filter:CalendarTitle">Calendar</digi:trn></b>
                                        </td>
                                        <td colspan="4">
                                            <html:select property="selectedCalendar" styleClass="dr-menu" styleId="selectedCalendar">
                                                <logic:notEmpty name="parisIndicatorForm" property="calendars">
                                                    <html:optionsCollection property="calendars" value="ampFiscalCalId" label="name"/>
                                                </logic:notEmpty>
                                            </html:select>
                                        </td>
                                    </tr>
                                    <tr><td>&nbsp;</td></tr>
                                    <tr>
                                        <td colspan="5">
                                            <b><digi:trn key="rep:filter:StartYear">Start Year</digi:trn></b>&nbsp;
                                        </td>
                                        <td colspan="4">
                                            <%String selected = "";%>
                                            <html:select property="selectedStartYear" styleClass="dr-menu" styleId="selectedStartYear">
                                                <%for(int i = 0; i < sy.length; i++) {%>
                                                    <%selected = (sy[i] == form.getSelectedStartYear()) ? "selected='selected'" : "";%>
                                                    <option value='<%=sy[i]%>' <%=selected%>><%=sy[i]%></option>
                                                <%}%>
                                            </html:select>
                                        </td>
                                    </tr>
                                    <tr><td>&nbsp;</td></tr>
                                    <tr>
                                        <td colspan="5">
                                            <b><digi:trn key="rep:filter:EndYear">End Year</digi:trn></b>&nbsp;
                                        </td>
                                        <td colspan="4">
                                            <html:select property="selectedEndYear" styleClass="dr-menu" styleId="selectedEndYear">
                                                <%for(int i = 0; i < sy.length; i++) {%>
                                                    <%selected = (sy[i] == form.getSelectedEndYear()) ? "selected='selected'" : "";%>
                                                    <option value='<%=sy[i]%>' <%=selected%>><%=sy[i]%></option>
                                                <%}%>
                                            </html:select>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <td>
                                <table align="center" cellpadding="2" cellspacing="2">
                                    <tr bgcolor="#EEEEEE">
                                        <td colspan="5">
                                            <b><digi:trn key="rep:pop:CurrencyType">Currency Type:</digi:trn></b>
                                        </td>
                                    </tr>
                                    <tr bgcolor="#EEEEEE">
                                        <td colspan="4">
                                            <html:select property="selectedCurrency" name="parisIndicatorForm" styleClass="dr-menu" styleId="selectedCurrency">
                                                <logic:notEmpty name="parisIndicatorForm" property="currencyTypes">
                                                    <html:optionsCollection name="parisIndicatorForm" property="currencyTypes" value="currencyCode" label="currencyName"/>
                                                </logic:notEmpty>
                                            </html:select>
                                        </td>
                                    </tr>
                                    <tr><td>&nbsp;</td></tr>
                                    <tr bgcolor="#EEEEEE">
                                        <td colspan="5"><b><digi:trn key="rep:filer:financingInstrument">Financing Instrument</digi:trn></b></td>
                                    </tr>
                                    <tr bgcolor="#EEEEEE">
                                        <td colspan="5">
                                            <category:showoptions firstLine="All" styleClass="dr-menu" property="selectedFinancingIstruments" name="parisIndicatorForm" styleId="selectedFinancingIstruments"
                                                keyName="<%=org.digijava.module.categorymanager.util.CategoryConstants.FINANCING_INSTRUMENT_KEY %>" multiselect="true" size="6"/>
                                        </td>
                                    </tr>        
                                </table>
                            </td> 
                        </tr>
                        <tr bgcolor="#EEEEEE">
                                
                        </tr>
                </table>
            </div>
            <div id="financing" class="yui-hidden">
                <br />
                    <table width="100%" style="vertical-align: top;" align="center" cellpadding="7px" cellspacing="7px" >
                        <tr>
                            <td>
                                <table align="center" cellpadding="2" cellspacing="2">
                                    <tr bgcolor="#EEEEEE">
                                        <td colspan="5"><b><digi:trn>Donor Groups:</digi:trn></b></td>
                                        <td>&nbsp;</td>
                                        <td colspan="5"><b><digi:trn key="rep:pop:Groups">Groups:</digi:trn></b></td>
                                    </tr>
                                    <tr bgcolor="#EEEEEE">
                                        <td colspan="5" styleClass="inp-text">
                                            <html:select property="selectedDonorGroups" name="parisIndicatorForm" styleClass="dr-menu" size="8" multiple="true" styleId="selectedDonorGroups">
                                                <html:option value="0"><digi:trn key="aim:allGroups">All Groups</digi:trn></html:option>
                                                <logic:notEmpty name="parisIndicatorForm" property="donorGroups">
                                                    <html:optionsCollection name="parisIndicatorForm" property="donorGroups" value="ampOrgGrpId" label="orgGrpName"/>
                                                </logic:notEmpty>
                                            </html:select>
                                        </td>
                                        <td>&nbsp;</td>                                 
                                        <td colspan="5" styleClass="inp-text">
                                            <html:select property="selectedDonors" size="6" style="width: 300px" styleClass="inp-text" multiple="true" styleId="selectedDonors">
                                                <html:option value="0"><digi:trn key="aim:allDonors">All Donors</digi:trn></html:option>
                                                <html:optionsCollection property="donors" value="ampOrgId" label="acronym" />
                                            </html:select>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                </table>
            </div>
            <div id="sectorsgroups" class="yui-hidden">
                <br />
                    <table width="100%" style="vertical-align: top;" align="center" cellpadding="7px" cellspacing="7px" >
                        <tr valign="top">
                            <td align="center">
                                <table align="center" cellpadding="2" cellspacing="2">
                                    <tr bgcolor="#EEEEEE">
                                        <td colspan="5"><b><digi:trn key="rep:pop:sectors">Sectors:</digi:trn></b></td>
                                        <td>&nbsp;</td>
                                        <td colspan="5"><b><digi:trn key="rep:pop:Status">Status:</digi:trn></b></td>
                                    </tr>
                                    <tr bgcolor="#EEEEEE">
                                        <td colspan="5" styleClass="inp-text">
                                            <html:select styleId="selectedSectors" property="selectedSectors" size="8" style="width: 300px" styleClass="inp-text" multiple="true">
                                                <html:option value="0"><digi:trn key="aim:allSectors">All Sectors</digi:trn></html:option>
                                                <html:optionsCollection property="sectors" value="ampSectorId" label="name" />
                                            </html:select>
                                        </td>
                                        <td>&nbsp;</td>                                 
                                        <td colspan="5" styleClass="inp-text">
                                            <category:showoptions firstLine="All" styleClass="dr-menu" property="selectedStatuses" name="parisIndicatorForm" styleId="selectedStatuses"
                                                keyName="<%=org.digijava.module.categorymanager.util.CategoryConstants.ACTIVITY_STATUS_KEY %>" multiselect="true" size="6"/>
                                        </td>
                                    </tr>
                                </table>
                            </td> 
                        </tr>
                        <tr bgcolor="#EEEEEE">
                            <td colspan="5" styleClass="inp-text">&nbsp;<br/><br/></td>
                        </tr>
                </table>
            </div>
        </div>
    </div>
    <div style="background-color: #EEEEEE; ">
        <br />
        <table width="100%">
            <tr>
                <td align="center" colspan="5">
                    <input type="button" value="<digi:trn key="rep:filer:ApplyFiltersToReport">Apply Filters to the Report</digi:trn>" class="dr-menu" onclick="submitFilters();">
                </td>
            </tr>
        </table>
    </div>
</div>