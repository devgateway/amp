<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

   
<table width="100%">
    <tr>
        <td height="210px" valign="top" >
            <table id="orgTable" border="1" width="100%">
                <th colspan="2" class="tableHeaderCls"><digi:trn>Summary</digi:trn></th>
                <tr>
                    <td width="30%"><digi:trn>Group</digi:trn>:</td><td>${orgGroup.orgGrpName}&nbsp;</td>
                </tr>
                 <tr>
                    <td width="30%"><digi:trn>Type</digi:trn>:</td><td>${orgGroup.orgType.orgType}&nbsp;</td>
                </tr>
                <tr>
                    <td width="30%"><digi:trn>Organization Name</digi:trn>:</td><td>${organization.name}&nbsp;</td>
                </tr>
                <tr>
                    <td width="30%"><digi:trn>Organization Acronym</digi:trn>:</td><td>${organization.acronym}&nbsp;</td>
                </tr>
                <tr>
                    <td width="30%"><digi:trn>Donor Group</digi:trn>:</td><td>${organization.orgGrpId.orgGrpName}&nbsp;</td>
                </tr>
                <tr>
                    <td width="30%"><digi:trn>Web Link</digi:trn>:</td><td>${organization.orgUrl}&nbsp;</td>
                </tr>
                <tr>
                    <td width="30%"><digi:trn>Contact Name</digi:trn>:</td><td>${organization.contactPersonName}&nbsp;</td>
                </tr>
                <tr>
                    <td width="30%"><digi:trn>Contact Phone</digi:trn>:</td><td>${organization.phone}&nbsp;</td>
                </tr>
                <tr>
                    <td width="30%"><digi:trn>Contact Email</digi:trn>:</td><td>${organization.email}&nbsp;</td>
                </tr>

            </table>
        </td>
    </tr>
    <tr>
        <td height="210px" valign="top">
            <jsp:include page="/orgProfile/showLargestProjects.do" flush="true"/>
        </td>
    </tr>
</table>


 
<script language="javascript">
setStripsTable("orgTable", "tableEven", "tableOdd");
setHoveredTable("orgTable");
</script>
