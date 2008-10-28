<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<!-- closePopup.jsp starts -->
<!-- invoked to close myself and reload my parent (after save was performed) -->
<logic:present name="close">

<script type="text/javascript">
	<%if(request.getParameter("reloadParent")!=null) {%>
	window.opener.location.href = window.opener.location.href;
	<%}%>
	window.close();
</script>	
</logic:present>
<!-- closePopup.jsp ends -->