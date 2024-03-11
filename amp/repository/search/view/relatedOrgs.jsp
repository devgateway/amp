<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>
<%@ taglib uri="/src/main/resources/tld/fn.tld" prefix="fn" %>
<script type="text/javascript">
<!--
	function toggleResultsGroup(groupId){
		var div = $('#div_'+groupId);
		var imgPlus = $('#'+groupId + '_plus');
		var imgMinus = $('#'+groupId + '_minus');
		
		if(div.is(":visible") == true){
			div.hide();
			imgMinus.hide();
			imgPlus.show();
		}else{
			div.show();
			imgMinus.show();
			imgPlus.hide();
		}
	}
//-->
</script>
<logic:present name="resultActivitiesWithOrgs" scope="request">
    <tr>
        <td bgcolor="#dadada">
            <img id="activityRows${requestScope.relatedOrgIndex}_plus"  onclick="toggleResultsGroup('activityRows${requestScope.relatedOrgIndex}')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif" align="absmiddle" style="float:left;"/>
            <img id="activityRows${requestScope.relatedOrgIndex}_minus" onclick="toggleResultsGroup('activityRows${requestScope.relatedOrgIndex}')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif" style="display:none;float:left;" align="absmiddle"/>
            &nbsp;&nbsp;${fn:length(requestScope.resultActivitiesWithOrgs)} <strong><digi:trn>activities using this organization as</digi:trn> <digi:trn>${requestScope.relatedOrgType}.</digi:trn></strong>
        </td>
    </tr>
    <tr>
        <td width="100%">
            <div id="div_activityRows${requestScope.relatedOrgIndex}" style="display : none;width:100%;">
                <table width="100%" cellpadding="2" cellspacing="2" id="dataTable1">
                    <c:forEach var="item" items="${requestScope.resultActivitiesWithOrgs}">
                        <tr>
                            <td>
                       	<digi:link module="aim"
											href="/viewActivityPreview.do?activityId=${item.ampActivityId}"><bean:write name="item" property="objectName" /></digi:link><br />
                        </td>
                        </tr>
                       </c:forEach> 
                </table>
            </div>
        </td>
    </tr>
</logic:present>