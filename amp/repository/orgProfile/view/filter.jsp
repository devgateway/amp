<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"  src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>

<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/container.css'/>"/>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/yahoo-dom-event.js"/>" ></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/container-min.js"/>" ></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/connection-min.js"/>" ></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/dragdrop-min.js"/>" ></script>
<digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="<digi:file src="module/aim/css/amptabs.css"/>"/>



<script language="javascript">
    var filter; // Filter panel
    $(document).ready(function(){

        $("#org_group_dropdown_id").change(function () {
            var orgGroupId=$('#org_group_dropdown_id option:selected').val();
            var partialUrl=addActionToURL('getOrganizations.do');
            var url=partialUrl+'?orgGroupId='+orgGroupId;
            var async=new Asynchronous();
            async.complete=buildOrgDropDown;
            async.call(url);

        });
        $("#changeFilterLink").click(function () {
            var filterDiv = document.getElementById('filter');
            filterDiv.style.display="block";
            filterDiv.stytelvisibility="visible";
            filter=new YAHOO.widget.Panel("filter",{
                x:"20",
                y:"20",
                constraintoviewport: true,
                Underlay:"shadow",
                modal: true,
                close:true,
                visible:true,
                fixedcenter: true,
                draggable:false} );
            var msg='\n<digi:trn >ORG PROFILE FILTER</digi:trn>';
            filter.setHeader(msg);
            filter.render();
        });

        $("#displaySettingsButton").click(function () {
            $("#currentDisplaySettings").toggle();
            var title=$("#displaySettingsButton").text();
            var titleEnd=title.substr(title.length-2,2);
            if(titleEnd=='>>'){
                $("#displaySettingsButton").html("<digi:trn>Hide Current Settings</digi:trn> &lt;&lt;")
            }
            else{
                $("#displaySettingsButton").html("<digi:trn>Show Current Settings</digi:trn> &gt;&gt;")
            }
          
         
        });
        $("#deselectAll").click(function () {
            $("#org_dropdown_id option").removeAttr("selected");
        });

    });


    function addActionToURL(actionName){
        var fullURL=document.URL;
        var lastSlash=fullURL.lastIndexOf("/");
        var partialURL=fullURL.substring(0,lastSlash);
        return partialURL+"/"+actionName;
    }



    function buildOrgDropDown(status, statusText, responseText, responseXML){
        var orgSelect=document.getElementById("org_select");
        orgSelect.innerHTML=responseText;

    }
    function setStripsTable(classOdd, classEven) {
        var tableElements = $(".tableElement");
        for(var j = 0; j < tableElements.length; j++) {
            rows = tableElements[j].getElementsByTagName('tr');
            for(var i = 0, n = rows.length; i < n; ++i) {
                if(i%2 == 0)
                    rows[i].className = classEven;
                else
                    rows[i].className = classOdd;
            }
            rows = null;

        }

    }
    function setHoveredTable() {
        var tableElements = $(".tableElement");
        for(var j = 0; j < tableElements.length; j++) {
            if(tableElements){
                var className = 'Hovered',
                pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
                rows      = tableElements[j].getElementsByTagName('tr');

                for(var i = 0, n = rows.length; i < n; ++i) {
                    rows[i].onmouseover = function() {
                        this.className += ' ' + className;
                    };
                    rows[i].onmouseout = function() {
                        this.className = this.className.replace(pattern, ' ');
                      
                    };
                }
                rows = null;
            }
        }
    }
</script>
<digi:instance property="orgProfOrgProfileFilterForm"/>

<digi:form action="/showOrgProfile.do">

    <table border="0" align="left" width="100%">
        <tr>
            <td>
                <div style="width:99.7%;height:20px;background-color:#ccdbff;padding:2px 2px 2px 2px;Font-size:8pt;font-family:Arial,Helvetica,sans-serif;">
                    <span style="cursor:pointer;float:left;">
                        <DIV id="subtabs">
                            <UL>
                                <LI>
                                    <div>
                                        <a target="_blank" onclick="exportPDF(); return false;">
                                            <digi:img width="15px" height="15px" hspace="0" vspace="0" src="/TEMPLATE/ampTemplate/imagesSource/common/pdf.gif" border="0" alt='Export to PDF'/>
                                        </a>
                                        <a target="_blank" onclick="exportWord(); return false;">
                                            <digi:img  hspace="0" vspace="0" src="/TEMPLATE/ampTemplate/imagesSource/common/doc.gif" border="0" alt='Export to Word'/>
                                        </a>
                                        <span id="changeFilterLink">
                                            <a>
                                                <digi:img width="15px" height="15px" hspace="0" vspace="0" src="/TEMPLATE/ampTemplate/imagesSource/common/filter.png" border="0" alt='Apply Filter'/>
                                            </a>
                                        </span>
                                    </div>
                                </LI>
                            </UL>
                        </DIV>
                    </span>
                    <span style="cursor:pointer;font-family:Arial; font-size:10px; font-style: italic;float:right;" id="displaySettingsButton"><digi:trn>Show Current Settings</digi:trn>  &gt;&gt;</span>
                    &nbsp;
                </div>
                <div style="display:none;background-color:#FFFFCC;padding:2px 2px 2px 2px;" id="currentDisplaySettings" >
                    <table cellpadding="0" cellspacing="0" border="0" width="80%" >
                        <tbody id="filterSettingsTable">
                            <tr>
                                <td valign="top">
                                    <strong>
                                        <digi:trn>Selected Filters:</digi:trn>
                                    </strong>
                                </td>
                            </tr>
                        <td>
                            <digi:trn>Organization Group</digi:trn>:
                            <c:choose>
                            <c:when test="${not empty orgProfOrgProfileFilterForm.orgGroupName}">
                                ${orgProfOrgProfileFilterForm.orgGroupName}
                            </c:when> 
                                <c:otherwise>
                                    <digi:trn>All</digi:trn>
                                </c:otherwise>
                            </c:choose>
                            |&nbsp;
                            <digi:trn>Organization</digi:trn>:
                            <c:choose>
                            <c:when test="${not empty orgProfOrgProfileFilterForm.orgsName}">
                                ${orgProfOrgProfileFilterForm.orgsName}
                            </c:when> 
                                <c:otherwise>
                                    <digi:trn>All</digi:trn>
                                </c:otherwise>
                            </c:choose>
                            |&nbsp;
                            <digi:trn>Year</digi:trn>:${orgProfOrgProfileFilterForm.year}| &nbsp;
                            <digi:trn>Currency</digi:trn>:${orgProfOrgProfileFilterForm.currencyCode}| &nbsp;
                            <digi:trn>Transaction Type</digi:trn>:
                            <c:choose>

                                <c:when test="${orgProfOrgProfileFilterForm.transactionType=='1'}">
                                    <digi:trn>DISBURSEMENTS</digi:trn>
                                </c:when>
                                <c:otherwise>
                                    <digi:trn>COMMITMENTS</digi:trn>
                                </c:otherwise>
                            </c:choose>
                            |&nbsp;
                            <digi:trn>Show data only from this workspace</digi:trn>:
                            <c:if test="${orgProfOrgProfileFilterForm.workspaceOnly}">
                                <digi:trn>Yes</digi:trn>
                            </c:if>
                            <c:if test="${!orgProfOrgProfileFilterForm.workspaceOnly}">
                                <digi:trn>No</digi:trn>
                            </c:if>| <digi:trn>Date Range Bar Graph 3 years</digi:trn>| <digi:trn>Date Range Tables: 5 Years</digi:trn>
                        </td>

                        </tbody>
                    </table>
                </div>

            </td>
        </tr>
    </table>
    <div id="filter" style="visibility:hidden;display:none;width:700px;height:400px;">
        <div style="width:450px;float:left">
            <table cellpadding="5" cellspacing="0">
                <tr>
                    <td nowrap align="left" colspan="2">
                        <html:checkbox  property="workspaceOnly"><b><digi:trn>SHOW ONLY ORGANIZATION PROFILE DATA FROM THIS WORKSPACE?</digi:trn></b></html:checkbox><br/>
                        <font style="color:red;font-weight:bold"><digi:trn>Note: If left unchecked it will show data for all of the current workspaces in AMP</digi:trn></font>
                    </td>
                </tr>
                <tr>
                    <td align="left" colspan="2">
                        <fieldset>
                            <html:radio property="transactionType" value="0"><digi:trn>COMMITMENTS</digi:trn></html:radio>
                            <html:radio property="transactionType" value="1"><digi:trn>DISBURSEMENTS</digi:trn></html:radio>
                        </fieldset>
                    </td>
                </tr>
                <tr>
                    <td>
                        <b><digi:trn>Organization Group</digi:trn>:</b>
                    </td>
                    <td align="left" >
                        <html:select property="orgGroupId"   styleId="org_group_dropdown_id" styleClass="selectDropDown">
                            <html:option value="-1"><digi:trn>All</digi:trn></html:option>
                            <html:optionsCollection property="orgGroups" value="ampOrgGrpId" label="orgGrpName" />
                        </html:select>
                           
                    </td>
                </tr>

                <tr>
                    <td>
                        <b><digi:trn>Currency Code</digi:trn>:</b>
                    </td>
                    <td>

                        <html:select property="currencyId"  styleClass="selectDropDown">
                            <html:optionsCollection property="currencies"
                                                    value="ampCurrencyId" label="currencyName" /></html:select>
                        </td>
                    </tr>
                    <tr>
                        <td><b><digi:trn>Fiscal Calendar</digi:trn>:</b></td>
                    <td align="left">
                        <html:select property="fiscalCalendarId" styleClass="selectDropDown">
                            <html:option value="-1"><digi:trn>None</digi:trn></html:option>
                            <html:optionsCollection property="fiscalCalendars" label="name" value="ampFiscalCalId" />
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td><b><digi:trn key="orgProfile:filer:fiscalCalendar">Fiscal Year</digi:trn>:</b></td>
                    <td align="left">

                        <html:select property="year" styleClass="selectDropDown">
                            <html:optionsCollection property="years" label="wrappedInstance" value="wrappedInstance" />
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <font style="color:red;font-weight:bold"><digi:trn>Note: fiscal year will affect the data range for the graps as follows:</digi:trn></font>
                        <p>
                            <digi:trn>
                            *Pledges/Commitments/Disbursements graph will display a range of three years (the current year and the two previous years) **Type of Aid and ODA Profile will display a range of five years (the current year and the previous four years) </digi:trn> *** <digi:trn key='orgProfile:helpBpdy'>Sector Breakdown,5 Largest Projects,Regional Breakdown, Paris Declaration are rendering  data of the previous fiscal year</digi:trn></p>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        &nbsp;
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="2">
                        <html:submit styleClass="button" property="apply"><digi:trn key="orgProfile:filer:Apply">Apply</digi:trn></html:submit>
                    </td>
                </tr>
            </table>
        </div>
            <div style="width:200px">
            <table cellpadding="2" border="0" >
                <tr align="left" style="background-color:rgb(153, 153, 153); color: rgb(0, 0, 0);">
                    <td>
                        <digi:trn>Organizations</digi:trn>
                       
                    </td>
                </tr>
                <tr>
                    <td>
                        <span id="org_select">
                            <html:select property="orgIds" styleClass="selectDropDown" styleId="org_dropdown_id" multiple="true" size="20">
                                <html:optionsCollection property="organizations" value="ampOrgId" label="name" />
                            </html:select>
                        </span>
                        <input type="button" class="button" value="<digi:trn>Deselect All</digi:trn>" id="deselectAll">
                        <p style="color:red;font-weight:bold"><digi:trn> Note: Data will include all organizations in the current group if none from organization's list is selected.</digi:trn> </p>
                    </td>
                </tr>
            </table>

        </div>
    </div>

    <br>



</digi:form>

