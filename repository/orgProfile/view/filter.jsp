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

<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>

<script type="text/javascript" language="javascript">
    var filter; // Filter panel
    var exportSettingsPanel; // Filter panel
    $(document).ready(function(){

        $("#org_group_dropdown_id").change(function () {
            var orgGroupId=$('#org_group_dropdown_id option:selected').val();
            var partialUrl=addActionToURL('getOrganizations.do');
            var url=partialUrl+'?orgGroupId='+orgGroupId+"&reset=false";
            var async=new Asynchronous();
            async.complete=buildOrgDropDown;
            async.call(url);

        });
        $("#region_dropdown_id").change(function () {
            var regionId=$('#region_dropdown_id option:selected').val();
            var partialUrl=addActionToURL('getZones.do');
            var url=partialUrl+'?selRegionId='+regionId;
            var async=new Asynchronous();
            async.complete=buildZonesDropDown;
            async.call(url);

        });
        $("#changeFilterLink").click(function () {
            var filterDiv = document.getElementById('filter');
            filterDiv.style.display="block";
            filterDiv.stytelvisibility="visible";
            filter=new YAHOO.widget.Panel("filter",{
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

        $("#changeExportSettingsLink").click(function () {
    <digi:context name="url" property="context/orgProfile/showExportOptions.do"/>
                var url = "${url}";
                YAHOO.util.Connect.asyncRequest("POST", url, exportSettingsCallback, null);
                exportSettingsPanel=new YAHOO.widget.Panel("exportSettingsPanel",{
                    width:"520px",
                    height:"610px",
                    constraintoviewport: true,
                    Underlay:"shadow",
                    modal: true,
                    close:true,
                    visible:true,
                    fixedcenter: true,
                    draggable:false} );
                var msg='\n<digi:trn >Export Options</digi:trn>';
                exportSettingsPanel.setHeader(msg);
                exportSettingsPanel.setBody("<img src='images/amploading.gif' alt=''>");
                exportSettingsPanel.render();
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
                $("#region_dropdown_id option").removeAttr("selected");
                $("#zone_dropdown_id option").removeAttr("selected");
            });

        });

        var exportSettingsResponseSuccess = function(o){
            var response = o.responseText;
            exportSettingsPanel.setBody(response);
        }

        var exportSettingsResponseFailure = function(o){
            // Access the response object's properties in the
            // same manner as listed in responseSuccess( ).
            // Please see the Failure Case section and
            // Communication Error sub-section for more details on the
            // response object's properties.
            //alert("Connection Failure!");
        }
        var exportSettingsCallback =
            {
            success:exportSettingsResponseSuccess,
            failure:exportSettingsResponseFailure
        };
        function closeSettingsPopin(){
            exportSettingsPanel.hide();
        }
         function exportPage(){
			<digi:context name="url" property="context/module/moduleinstance/showExportOptions.do?actionType=export" />
            document.orgProfileExportOptionsForm.action="${url}";
			document.orgProfileExportOptionsForm.target="_blank";
			document.orgProfileExportOptionsForm.submit();
            exportSettingsPanel.hide();
        }

        function checkAllSeleceted(){
            var valid=true;
            var allSelected=false;
            var selectZone=document.getElementById("zone_dropdown_id");
            var selectedVals = selectZone.options;
            var selectedNumb=0;
            for (var i = 0; i <selectedVals.length; i++){
                var option=selectZone.options[i];
                if(option.selected){
                    selectedNumb++;
                    if(option.value==-1){
                        allSelected=true;
                    }

                }
            }
            if( allSelected&&selectedNumb>1){
                valid=false;
            }
            if(valid){
            <digi:context name="url" property="context/module/moduleinstance/showOrgProfile.do" />
             document.orgProfOrgProfileFilterForm.action="${url}";
             document.orgProfOrgProfileFilterForm.target="_self";
             document.orgProfOrgProfileFilterForm.submit();
             }
             else{
                 alert('<digi:trn jsFriendly="true">Select either all option or concrete options</digi:trn>')
             }
    
            return valid;
        }

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
        function  buildZonesDropDown(status, statusText, responseText, responseXML){
            var zoneSelect=document.getElementById("zone_select");
            zoneSelect.innerHTML=responseText;
        }
</script>
<digi:instance property="orgProfOrgProfileFilterForm"/>

<digi:form action="/showOrgProfile.do">

    <table border="0" align="left" width="100%">
        <tr>
            <td>
                <div id="orgProfToolbarId" style="display: none;width:99.7%;height:20px;background-color:#ccdbff;padding:2px 2px 2px 2px;Font-size:8pt;font-family:Arial,Helvetica,sans-serif;">
                    <span style="cursor:pointer;float:left;">
                        <DIV id="subtabs">
                            <UL>
                                <LI>
                                    <div>
                                        <a target="_blank" onclick="exportPDF(); return false;">
                                            <digi:img width="15px" height="15px" hspace="0" vspace="0" src="/TEMPLATE/ampTemplate/images/icons/pdf.gif" border="0" alt='Export to PDF'/><digi:trn>Export to PDF</digi:trn>
                                        </a>
                                        <a target="_blank" onclick="exportWord(); return false;">
                                            <digi:img  hspace="0" vspace="0" height="15px" src="/TEMPLATE/ampTemplate/images/icons/doc.gif" border="0" alt='Export to Word'/> <digi:trn>Export to Word</digi:trn>
                                        </a>
                                        <div id="changeFilterLink" style="display: inline" title="<digi:trn>Click on this icon to view filter</digi:trn>">
                                            <a>
                                                <digi:img width="15px" height="15px" hspace="0" vspace="0" src="/TEMPLATE/ampTemplate/images/add_filters.png" border="0" alt='Apply Filter'/>
                                                <digi:trn>Filter</digi:trn>
                                            </a>
                                            
                                        </div>
                                        <div id="changeExportSettingsLink" style="display: inline" title="<digi:trn>Click on this icon to view additional export options</digi:trn>">
                                            <a>
                                                <digi:img width="15px" height="15px" hspace="0" vspace="0" src="/TEMPLATE/ampTemplate/images/file-export-16x16.png" border="0" alt=''/>
                                                <digi:trn>Export Options</digi:trn>
                                            </a>
                                        </div>
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
                            <digi:trn>locations</digi:trn>:
                            <c:choose>
                                <c:when test="${not empty orgProfOrgProfileFilterForm.locationsName}">
                                    ${orgProfOrgProfileFilterForm.locationsName}
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
                                <c:when test="${orgProfOrgProfileFilterForm.transactionType=='0'}">
                                    <digi:trn>COMMITMENTS</digi:trn>
                                </c:when>
                                <c:otherwise>
                                    <digi:trn>Commitments & Disbursements</digi:trn>
                                </c:otherwise>
                            </c:choose>
                            |&nbsp;
                            <digi:trn>Number of project to display</digi:trn>:
                            <c:choose>
                                <c:when test="${orgProfOrgProfileFilterForm.largestProjectNumb==-1}">
                                    <digi:trn>All</digi:trn>
                                </c:when>
                                <c:otherwise>
                                    ${orgProfOrgProfileFilterForm.largestProjectNumb}
                                </c:otherwise>
                            </c:choose>|&nbsp;
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
    <div id="filter" style="visibility:hidden;display:none;width:700px;height:600px;">
        <div style="width:450px;float:left">
            <table cellpadding="5" cellspacing="0">
                <tr>
                    <td nowrap align="left" colspan="2">
                        <html:checkbox  property="workspaceOnly"><b><digi:trn>SHOW ONLY ORGANIZATION PROFILE DATA FROM THIS WORKSPACE</digi:trn></b></html:checkbox><br/>
                        <font style="color:red;font-weight:bold"><digi:trn>Note: If left unchecked it will show data for all of the current workspaces in AMP</digi:trn></font>
                    </td>
                </tr>
                <module:display name="Pledges" parentModule="PROJECT MANAGEMENT">
                  <tr>
                    <td nowrap align="left" colspan="2">
                        <html:checkbox  property="pledgeVisible"><b><digi:trn>Include pledges in Pledges/Commitments/Disbursements/Expenditures</digi:trn></b></html:checkbox><br/>
                    </td>
                </tr>
                </module:display>
                <feature:display module="Funding" name="Expenditures">
                  <tr>
                    <td nowrap align="left" colspan="2">
                        <html:checkbox  property="expendituresVisible"><b><digi:trn>Include expenditures in Pledges/Commitments/Disbursements/Expenditures</digi:trn></b></html:checkbox><br/>
                    </td>
                </tr>
                </feature:display>


                <tr>
                    <td align="left" colspan="2">
                        <fieldset>
                            <html:radio property="transactionType" value="0"><digi:trn>COMMITMENTS</digi:trn></html:radio>
                            <html:radio property="transactionType" value="1"><digi:trn>DISBURSEMENTS</digi:trn></html:radio>
                            <html:radio property="transactionType" value="2"><digi:trn>COMMITMENTS & DISBURSEMENTS</digi:trn></html:radio>
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
                        <b><digi:trn>Currency</digi:trn>:</b>
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
                    <td><b><digi:trn>Number of years in time series</digi:trn>:</b></td>
                    <td align="left">
                        <html:select property="yearsInRange" styleClass="selectDropDown">
                            <html:option value="1">1</html:option>
                            <html:option value="2">2</html:option>
                            <html:option value="3">3</html:option>
                            <html:option value="4">4</html:option>
                            <html:option value="5">5</html:option>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td><b><digi:trn>Number of project to display</digi:trn>:</b></td>
                    <td align="left">

                        <html:select property="largestProjectNumb" styleClass="selectDropDown">
                            <html:option value="5">5</html:option>
                            <html:option value="10">10</html:option>
                            <html:option value="20">20</html:option>
                            <html:option value="50">50</html:option>
                            <html:option value="-1"><digi:trn>All</digi:trn></html:option>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <font style="color:red;font-weight:bold"><digi:trn>Note: fiscal year will affect the data range for the graps as follows:</digi:trn></font>
                        <p>
                            <digi:trn>
                                *Pledges/Commitments/Disbursements graph will display a range of three years (the current year and the two previous years) **Type of Aid and ODA Profile will display a range of five years (the current year and the previous four years) </digi:trn> *** <digi:trn key='orgProfile:helpBpdy'>Sector Breakdown,5 Largest Projects,Regional Breakdown, Paris Declaration will display  data of the previous fiscal year</digi:trn></p>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            &nbsp;
                        </td>
                    </tr>
                    <tr>
                        <td align="center" colspan="2">
                            <input type="button" class="button" onclick="checkAllSeleceted();" value="<digi:trn key="orgProfile:filer:Apply">Apply</digi:trn>">
                    </td>
                </tr>
            </table>
        </div>
        <div style="width:200px;float:left">
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

                    </td>
                </tr>

                <tr align="left" style="background-color:rgb(153, 153, 153); color: rgb(0, 0, 0);">
                    <td>
                        <digi:trn>Location</digi:trn>
                    </td>
                </tr>
                <tr>
                    <td align="left">

                        <digi:trn>Region</digi:trn> : <html:select property="selRegionId" styleId="region_dropdown_id" styleClass="selectDropDown">
                            <html:option value="-1"><digi:trn>All</digi:trn></html:option>
                            <html:optionsCollection property="regions" value="id" label="name" />
                        </html:select>

                        <digi:trn>Zone</digi:trn> : <span id="zone_select">
                            <html:select property="selZoneIds" styleClass="selectDropDown" styleId="zone_dropdown_id" multiple="true" size="8">
                                <c:if test="${!empty orgProfOrgProfileFilterForm.zones}">
                                    <html:option value="-1"><digi:trn>All</digi:trn></html:option>
                                    <html:optionsCollection property="zones" value="id" label="name" />
                                </c:if>
                            </html:select>
                        </span>
                        <input type="button" class="button" value="<digi:trn>Deselect All</digi:trn>" id="deselectAll">
                        <p style="color:red;font-weight:bold"><digi:trn> Note: Data will include all organizations in the current group if none from organizations list is selected.</digi:trn> </p>
                    </td>
                </tr>
            </table>

        </div>
    </div>

    <br>



</digi:form>

<div id="exportSettingsPanel" class="content">
</div>

