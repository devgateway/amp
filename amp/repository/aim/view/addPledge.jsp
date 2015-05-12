<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script src="/repository/bootstrap/hacks.js"></script>

<style>
	#iframe-container{
		margin-top: 10px;
	}
</style>

<div id="iframe-container">
	<iframe src="/aim/selectPledgeLocation.do?edit=false" width="100%" seamless="seamless" frameborder="0" marginheight="0" marginwidth="0" id="bootstrap_iframe"></iframe>
</div>

<!-- Given that pledges don't have a footer. We initialize the menu here -->
<script type="text/javascript">
var boilerplate = new window.boilerplate(
     	  {
     		showAdminFooter: true,
     		showLogin: false
		  });

</script>
