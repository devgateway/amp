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

<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"  src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>

   <c:set var="helpBody">
       <digi:trn key='orgProfile:helpBpdy'>Sector Breakdown,5 Largest Projects,Regional Breakdown, Paris Declaration are rendering  data of the previous fiscal year</digi:trn>
   </c:set>
  
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>

<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/container.css'/>"/>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/yahoo-dom-event.js"/>" ></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/container-min.js"/>" ></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/connection-min.js"/>" ></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/dragdrop-min.js"/>" ></script>
<digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="<digi:file src="module/aim/css/amptabs.css"/>"/>

<style>

            .tableEven {
                background-color:#dbe5f1;
                font-size:8pt;
                padding:2px;
            }

            .tableOdd {
                background-color:#FFFFFF;
                font-size:8pt;!important
                padding:2px;
            }

            .Hovered {
                background-color:#a5bcf2;
            }


            .toolbar{
                width: 350px;
                background: #addadd;
                background-color: #addadd;
                padding: 3px 3px 3px 3px;
                position: relative;
                top: 10px;
                left: 10px;
                bottom: 100px;

            }
</style>

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
                draggable:true} );
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
    function setStripsTable(tableId, classOdd, classEven) {
        var tableElement = document.getElementById(tableId);
        rows = tableElement.getElementsByTagName('tr');
        for(var i = 0, n = rows.length; i < n; ++i) {
            if(i%2 == 0)
                rows[i].className = classEven;
            else
                rows[i].className = classOdd;
        }
        rows = null;
    }
    function setHoveredTable(tableId) {

        var tableElement = document.getElementById(tableId);
        if(tableElement){
            var className = 'Hovered',
            pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
            rows      = tableElement.getElementsByTagName('tr');

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
</script>
<digi:instance property="orgProfOrgProfileFilterForm"/>

<digi:form action="/showOrgProfile.do">

    <!-- this is for the nice tooltip widgets -->
    <table border="0" align="left" width="100%">
        <tr>
            <td>
                <div style="width:99.7%;background-color:#ccdbff;padding:2px 2px 2px 2px;Font-size:8pt;font-family:Arial,Helvetica,sans-serif;">
                    <span style="cursor:pointer;float:left;">
                        <DIV id="subtabs">
                            <UL>
                                <LI>
                                    <div>
                                        <span id="changeFilterLink">
                                            <a>
                                                <digi:trn>Change Filter</digi:trn>
                                            </a>&nbsp;&nbsp;
                                        </span>
                                    </div>
                                </LI>
                            </UL>
                        </DIV>
                    </span>
                    <span style="cursor:pointer;font-style: italic;float:right;" id="displaySettingsButton"><digi:trn>Show Current Settings</digi:trn>  &gt;&gt;</span>
                    &nbsp;
                </div>
                <div style="display:none;background-color:#FFFFCC;padding:2px 2px 2px 2px;" id="currentDisplaySettings" >
                    <table cellpadding="0" cellspacing="0" border="0" width="80%" >
                        <tbody id="filterSettingsTable">
                            <tr>
                                <td style="font-size:11px;font-family:Arial,Helvetica,sans-serif" valign="top">
                                    <strong>
                                        <digi:trn>Selected Filters:</digi:trn>
                                    </strong>
                                </td>
                            </tr>
                            <td>
                                <digi:trn>Organization Group</digi:trn>:${orgProfOrgProfileFilterForm.orgGroupName}&nbsp;
                                <digi:trn>Organization</digi:trn>:${orgProfOrgProfileFilterForm.orgName},&nbsp;
                                <digi:trn>Year</digi:trn>:${orgProfOrgProfileFilterForm.year}, &nbsp;
                                <digi:trn>Currency</digi:trn>:${orgProfOrgProfileFilterForm.currencyCode}, &nbsp;
                                <digi:trn>Transaction Type</digi:trn>:
                                <c:if test="${orgProfOrgProfileFilterForm.transactionType=='0'}">
                                    <digi:trn>Comm</digi:trn>
                                </c:if>
                                <c:if test="${orgProfOrgProfileFilterForm.transactionType=='1'}">
                                    <digi:trn>Disb</digi:trn>
                                </c:if>,&nbsp;
                                <digi:trn>Show data only from workspace</digi:trn>:
                                <c:if test="${orgProfOrgProfileFilterForm.workspaceOnly}">
                                    <digi:trn>Yes</digi:trn>
                                </c:if>
                                <c:if test="${!orgProfOrgProfileFilterForm.workspaceOnly}">
                                    <digi:trn>No</digi:trn>
                                </c:if>
                            </td>

                        </tbody>
                    </table>
                </div>

            </td>
        </tr>
    </table>
    <div id="filter" style="visibility:hidden;display:none;">
       <div style="height:300px;width:450px;overflow:auto">
        <table cellpadding="5" cellspacing="0">
            <tr>
                <td nowrap align="left" colspan="2">
                    <html:checkbox  property="workspaceOnly"><b><digi:trn>From Workspace Only</digi:trn> </b></html:checkbox>
                    <html:select property="transactionType" styleClass="inp-text">
                        <html:option value="0"><digi:trn>Comm</digi:trn></html:option>
                        <html:option value="1"><digi:trn>Disb</digi:trn></html:option>
                    </html:select>

                </td>
            </tr>
            <tr>
                <td>
                      <b><digi:trn>Organization Group</digi:trn>:</b>
                </td>
                <td align="left" >
                        <html:select property="orgGroupId" styleClass="inp-text"  styleId="org_group_dropdown_id">
                            <html:option value="-1"><digi:trn>All</digi:trn></html:option>
                            <html:optionsCollection property="orgGroups" value="ampOrgGrpId" label="orgGrpName" />
                        </html:select> 
                </td>
            </tr>
            <tr>
                <td>
                     <b><digi:trn key="orgProfile:filer:Organization">Organization</digi:trn>:</b>
                </td>
                <td align="left" >
                    <span id="org_select">
                        <html:select property="orgId" styleClass="inp-text" styleId="org_dropdown_id">
                            <html:option value="-1"><digi:trn>All</digi:trn></html:option>
                            <html:optionsCollection property="organizations" value="ampOrgId" label="name" />
                        </html:select>
                    </span>
                </td>

            </tr>
            <tr>
                <td>
                     <b><digi:trn>Currency Code</digi:trn>:</b>
                </td>
                <td>
                   
                    <html:select property="currencyId" styleClass="inp-text">
                        <html:optionsCollection property="currencies"
                                            value="ampCurrencyId" label="currencyName" /></html:select>
                </td>
            </tr>
            <tr>
                <td><b><digi:trn>Fiscal Calendar</digi:trn>:</b></td>
                <td align="left">
                    <html:select property="fiscalCalendarId" styleClass="inp-text">
                        <html:option value="-1"><digi:trn>None</digi:trn></html:option>
                        <html:optionsCollection property="fiscalCalendars" label="name" value="ampFiscalCalId" />
                    </html:select>
                </td>
            </tr>
            <tr>
                <td><b><digi:trn key="orgProfile:filer:fiscalCalendar">Fiscal Year</digi:trn>:</b></td>
                <td align="left">
                   
                    <html:select property="year" styleClass="inp-text">
                        <html:optionsCollection property="years" label="wrappedInstance" value="wrappedInstance" />
                    </html:select>
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
                    <digi:img  src="module/widget/images/help1.gif" title="${helpBody}"/>
                </td>
            </tr>
        </table>
        </div>
    </div>

    <br>



</digi:form>

