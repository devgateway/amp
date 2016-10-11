<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<script language=JavaScript type=text/javascript>
  function fnGoBack() {
      history.back(1);
      return false;
  }
</script>

<table border="0"  width="100%" cellpadding="0" cellspacing="0">
       <tr>
         <td colspan="2" valign="top"><jsp:include page="../../aim/view/teamPagesHeader.jsp" /><td>
       </tr>
       <tr>
         <td style="height: 330pt;">
           <div align="center" style="font-size: 10pt;">
             <digi:trn key="calendar:noEvents">
               <b>No Event Types</b>
             </digi:trn>
             <br /><br />
               <a href="#" onclick="return fnGoBack()"><digi:trn key="calendar:goBack">Go Back</digi:trn></a>
           </div>
         </td>
       </tr>
</table>
