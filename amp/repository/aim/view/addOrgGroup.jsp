<%@page import="org.digijava.kernel.util.SiteUtils"%>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<% if(SiteUtils.isEffectiveLangRTL()) { %>
  <link rel="stylesheet" href="/TEMPLATE/ampTemplate/css_2/amp-rtl.css">
<% } %>
<script language="JavaScript">
    function check() {
		var str = document.aimAddOrgGroupForm.orgGrpName.value;
		var type = trim(document.aimAddOrgGroupForm.orgTypeId.value);
		str = trim(str);
		if (str == null || str.length == 0) {
			alert("Please enter name for this Group");
			document.aimAddOrgGroupForm.orgGrpName.focus();
			return false;
		}
		else if (type == "-1" || type == null || type.length == 0) {
			alert("Please select type for this Group");
			document.aimAddOrgGroupForm.orgTypeId.focus();
			return false;
		}
		else {
			document.aimAddOrgGroupForm.orgGrpName.value = str;
			document.aimAddOrgGroupForm.target = "_self";
			document.aimAddOrgGroupForm.submit();
			return true;
		}
	}

	function trim ( inputStringTrim ) {
		fixedTrim = "";
		lastCh = " ";
		for (x=0; x < inputStringTrim.length; x++) {
			ch = inputStringTrim.charAt(x);
			if ((ch != " ") || (lastCh != " ")) { fixedTrim += ch; }
				lastCh = ch;
		}
		if (fixedTrim.charAt(fixedTrim.length - 1) == " ") {
			fixedTrim = fixedTrim.substring(0, fixedTrim.length - 1); }
		return fixedTrim;
	}

	function load() {
		document.aimAddOrgGroupForm.orgGrpName.focus();
	}
	</script>
	
	<digi:instance property="aimAddOrgGroupForm" />
	<script language="JavaScript">
	<c:if test="${aimAddOrgGroupForm.flag=='refreshParent'}">
        <digi:context name="selectLoc" property="/aim/editOrganisation.do" />
        url = "<%= selectLoc %>?orgGroupAdded=true&ampOrgId="+window.opener.document.aimAddOrgForm.ampOrgId.value+"&actionFlag="+window.opener.document.aimAddOrgForm.actionFlag.value;
        window.opener.document.aimAddOrgForm.action = url;
        window.opener.document.aimAddOrgForm.target = window.opener.name;
        window.opener.document.aimAddOrgForm.submit();
        window.close();
    </c:if>
</script>
<div class="admin-content">
<digi:context name="digiContext" property="context"/>
<bean:write name="aimAddOrgGroupForm" property="flag"/>
<%-- Add vertical spacing. It will look much better in this way
<div style="height: 75px">
</div --%>

<div class="addOrgBox">

    <digi:form action="/editOrgGroup.do" method="post">

        <html:hidden property="action" />
        <html:hidden property="ampOrgGrpId" />
        <html:hidden property="ampOrgId" />

        <h1><digi:trn key="aim:addOrgGroup">Add Organization Group</digi:trn></h1>

        <digi:errors />

        <table width="650" border="0" cellspacing="3" cellpadding="3" align="center">
          <tr>
            <td align=right><label><digi:trn key="aim:orgGroupName">Name</digi:trn><font color="#ff0000">*</font></label> </td>
            <td><html:text property="orgGrpName" size="45" /></td>
          </tr>
           <tr>
            <td align=right><label><digi:trn key="aim:orgGroupCode">Group  Code</digi:trn></label></td>
            <td><html:text property="orgGrpCode" size="15" /></td>
          </tr>
           <tr>
            <td align=right><label><digi:trn key="aim:orgGroupType">Type</digi:trn><font color="#ff0000">*</font></label></td>
            <td>
                <html:select property="orgTypeId">
                    <c:set var="translation">
                        <digi:trn key="aim:btnSelectType">Select Type</digi:trn>
                    </c:set>
                    <html:option value="-1">-- ${translation} --</html:option>
                    <logic:notEmpty name="aimAddOrgGroupForm" property="orgTypeColl">
                        <html:optionsCollection name="aimAddOrgGroupForm" property="orgTypeColl"
                                                value="ampOrgTypeId" label="orgType" />
                    </logic:notEmpty>
                </html:select>
            </td>
          </tr>
        </table>

        <%--

                <digi:trn key="aim:orgGroupLevel">Level</digi:trn>

                  <html:select property="levelId">
                    <html:option value="-1">-- Select Level --</html:option>
                    <logic:notEmpty name="aimAddOrgGroupForm" property="level">
                        <html:optionsCollection name="aimAddOrgGroupForm" property="level"
                            value="ampLevelId" label="name" />
                    </logic:notEmpty>
                </html:select>
         --%>
        <div class="buttons">
            <input type="button" value="Save" class="dr-menu" onclick="check()">

            <input type="reset" value="Reset" class="dr-menu">

            <input type="button" value="Cancel" class="dr-menu" onclick="window.close()">
        </div>


    </digi:form>

</div>
</div>