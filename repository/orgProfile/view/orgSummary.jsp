<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<script type="text/javascript">
    function printLargestProjects(){
        openURLinResizableWindow("/orgProfile/printLargestProjects.do", 780, 500);
    }
    var additionalInfoPanel; // Additional Information Panel
    function  showAdditionalInformation(){
        var additionalInfoDiv = document.getElementById('orgAdditionalInformation');
        additionalInfoDiv.style.display="block";
        additionalInfoDiv.stytelvisibility="visible";
        additionalInfoPanel=new YAHOO.widget.Panel("orgAdditionalInformation",{
            constraintoviewport: true,
            Underlay:"shadow",
            modal: true,
            close:true,
            visible:true,
            fixedcenter: true,
            draggable:false} );
        var msg='\n<digi:trn >Additional Information</digi:trn>';
        additionalInfoPanel.setHeader(msg);
        additionalInfoPanel.render();

    }

    function  saveAdditionalInfo(){
        var postString		="action=save&orgBackground=" + document.getElementById("orgBackgroundId").value+
            "&orgDescription="+document.getElementById("orgDescriptionId").value+"&orgId="+document.getElementById("orgSummaryOrgId").value ;
    <digi:context name="url" property="context/orgProfile/showOrgSummary.do"/>
            var url = "${url}";
            YAHOO.util.Connect.asyncRequest("POST", url, additionalInfoCallback, postString);
        }

        var additionalInfoResponseSuccess = function(o){
            document.getElementById("orgSummaryActionId").value="view"
            document.orgProfOrgProfileFilterForm.submit();
        }

        var additionalInfoResponseFailure = function(o){
            // Access the response object's properties in the
            // same manner as listed in responseSuccess( ).
            // Please see the Failure Case section and
            // Communication Error sub-section for more details on the
            // response object's properties.
            //alert("Connection Failure!");
        }
        var additionalInfoCallback =
            {
            success:additionalInfoResponseSuccess,
            failure:additionalInfoResponseFailure
        };
        function  loadLargestProjects(){
            $(".tab_organization_profile_unselected").each(function(index) {
                this.style.display="block";
            });

            $(".tab_organization_profile_selected").each(function(index) {
                this.style.display="none";
            });
     
        }
        function  hideLargestProjects(){
            $(".tab_organization_profile_selected").each(function(index) {
                this.style.display="block";
            });

            $(".tab_organization_profile_unselected").each(function(index) {
                this.style.display="none";
            });

        }


</script>
<c:set var="largestPrjTblName">
    <c:choose>
        <c:when test="${sessionScope.orgProfileFilter.largestProjectNumb==-1}">
            <digi:trn>All</digi:trn>
        </c:when>
        <c:otherwise>
            ${sessionScope.orgProfileFilter.largestProjectNumb}
        </c:otherwise>
    </c:choose><digi:trn>LARGEST PROJECTS</digi:trn> (${sessionScope.orgProfileFilter.year-1})

</c:set>

<DIV id="tabs">
    <UL>
        <div  class="tab_organization_profile_selected">
            <LI>
                <a name="node">
                    <div>
                        <digi:trn>Organization Profile</digi:trn>
                    </div>
                </a>
            </LI>
        </div>
        <div  class="tab_organization_profile_unselected" style="display: none">
            <LI>
                <span>
                    <a href="javascript:hideLargestProjects()">
                        <div title='<digi:trn>Hide  largest projects table and show summary</digi:trn>'>
                            <digi:trn>Organization Profile</digi:trn>
                        </div>
                    </a>
                </span>
            </LI>
        </div>

        <div  class="tab_organization_profile_unselected" style="display: none">
            <LI>
                <a name="node">
                    <div>
                        ${largestPrjTblName}
                    </div>
                </a>
            </LI>
        </div>

        <div  class="tab_organization_profile_selected">
            <LI>
                <span>
                    <a  href="javascript:loadLargestProjects()">
                        <div title='<digi:trn>Hide summary and show largest projects table</digi:trn>'>
                            ${largestPrjTblName}
                        </div>
                    </a>
                </span>
            </LI>
        </div>
    </UL>
</DIV>
 <div class="contentbox_border chartPlaceCss tab_organization_profile_selected">
<c:set var="organization" scope="request" value="${sessionScope.orgProfileFilter.organization}"/>
<c:set var="orgGroup" scope="request" value="${sessionScope.orgProfileFilter.orgGroup}"/>
<c:set var="orgsCount" scope="request" value="${fn:length(sessionScope.orgProfileFilter.orgIds)}"/>
<digi:instance property="orgSummaryForm" />
<digi:form action="/showOrgSummary.do" method="post">
    <html:hidden property="action" styleId="orgSummaryActionId"/>
    <html:hidden name="orgSummaryForm" property="orgId" styleId="orgSummaryOrgId"/>
        <table class="tableElement" border="0" width="100%" cellspacing="0" cellpadding="0">
            <tr>
                <th colspan="2" class="tableHeaderCls"><digi:trn>Organization Profile</digi:trn></th>
            </tr>
            <tr>
                <td width="30%"><digi:trn>Type</digi:trn>:</td>
                <td>
                    <c:choose>
                        <c:when test="${orgsCount==1}">
                            ${organization.orgGrpId.orgType}
                        </c:when>
                        <c:when test="${empty orgGroup&&orgsCount==0}">
                            <digi:trn>All</digi:trn>
                        </c:when>
                        <c:when test="${orgsCount>0}">
                            <digi:trn>Multiple Organizations Selected</digi:trn>
                        </c:when>
                        <c:otherwise>
                            <digi:trn>${orgGroup.orgType}</digi:trn>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td width="30%"><digi:trn>Organization Name</digi:trn>:</td>
                <td>
                    <c:choose>
                        <c:when test="${orgsCount>1}">
                            <digi:trn>Multiple Organizations Selected</digi:trn>
                        </c:when>
                        <c:when test="${orgsCount==1}">
                            ${organization.name}&nbsp;
                        </c:when>
                        <c:otherwise>
                            <digi:trn>N/A</digi:trn>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td width="30%"><digi:trn>Organization Acronym</digi:trn>:</td>
                <td>
                    <c:choose>
                        <c:when test="${orgsCount>1}">
                            <digi:trn>Multiple Organizations Selected</digi:trn>
                        </c:when>
                        <c:when test="${orgsCount==1}">
                            ${organization.acronym}&nbsp;
                        </c:when>
                        <c:otherwise>
                            <digi:trn>N/A</digi:trn>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td width="30%"><digi:trn>Donor Group</digi:trn>:</td>
                <td>
                    <c:choose>
                        <c:when test="${orgsCount==1}">
                            ${organization.orgGrpId.orgGrpName}
                        </c:when>
                        <c:when test="${empty orgGroup&&orgsCount==0}">
                            <digi:trn>All</digi:trn>
                        </c:when>
                        <c:when test="${orgsCount>0}">
                            <digi:trn>Multiple Organizations Selected</digi:trn>
                        </c:when>
                        <c:otherwise>
                            ${orgGroup.orgGrpName}
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td width="30%"><digi:trn>Web Link</digi:trn>:</td>
                <td>
                    <c:choose>
                        <c:when test="${orgsCount>1}">
                            <digi:trn>Multiple Organizations Selected</digi:trn>
                        </c:when>
                        <c:when test="${orgsCount==1}">
                            ${organization.orgUrl}&nbsp;
                        </c:when>
                        <c:otherwise>
                            <digi:trn>N/A</digi:trn>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <c:if test="${orgsCount!=1}">
                <tr>
                    <td width="30%"><digi:trn>Contact</digi:trn>:</td>
                    <td>
                        <c:choose>
                            <c:when test="${orgsCount>1}">
                                <digi:trn>Multiple Organizations Selected</digi:trn>
                            </c:when>
                            <c:otherwise>
                                <digi:trn>N/A</digi:trn>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:if>
            <c:if test="${orgsCount==1}">
                <tr>
                    <td width="30%"><digi:trn>Contact Name</digi:trn>:</td><td>${organization.contactPersonName}&nbsp;</td>
                </tr>
                <tr>
                    <td width="30%"><digi:trn>Contact Phone</digi:trn>:</td><td>${organization.phone}&nbsp;</td>
                </tr>
                <tr>
                    <td width="30%"><digi:trn>Contact Email</digi:trn>:</td><td>${organization.email}&nbsp;</td>
                </tr>
            </c:if>
        </table>
        <c:if test="${orgsCount==1}">
            <a href="javascript:showAdditionalInformation()"><digi:trn>Show Additional Information</digi:trn></a>
            <div id="orgAdditionalInformation"  style="visibility:hidden;display:none;width:600px;height:200px;z-index:3">
                <table cellSpacing=0 cellPadding=0 width="100%" border=0  align="left">
                    <tr><td><digi:trn>Background of donor</digi:trn>:</td><td><html:textarea name="orgSummaryForm" styleId="orgBackgroundId" property="orgBackground"  cols="40" rows="3"/></td></tr>
                    <tr><td> <digi:trn>Description</digi:trn>:</td><td><html:textarea name="orgSummaryForm" styleId="orgDescriptionId" property="orgDescription" cols="40" rows="3"/></td></tr>
                    <tr><td colspan="2" align="center"><input type="button" value="<digi:trn>Save</digi:trn>" onclick="saveAdditionalInfo()"/></td></tr>
                </table>
            </div>
        </c:if>
    </digi:form>
</div>
<div class="tab_organization_profile_unselected contentbox_border chartPlaceCss" style="display:none">
    <div style="float:left">
        <a href="javascript:printLargestProjects()">
            <img alt="print preview" src="/TEMPLATE/ampTemplate/images/print_icon.gif" border="0" >
        </a>
    </div>
    <jsp:include page="/orgProfile/showLargestProjects.do" flush="true"/>
</div>

<script language="javascript" type="text/javascript">
    setStripsTable("tableEven", "tableOdd");
    setHoveredTable();
</script>

