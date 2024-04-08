<%@ page language="java"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>

<div id="amp-footer" style="footer"></div>
<script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/node_modules/amp-boilerplate/dist/amp-boilerplate.js"/>"></script>
<script type="text/javascript">
  // AMP-22515: wait until page is ready to avoid race conditions on IE11 that break the MM.
  $(document).ready(function() {
	  var boilerplate = new window.boilerplate({
	    showAdminFooter: true,
	    showLogin: <digi:secure authenticated="true">false</digi:secure><digi:secure authenticated="false">true
	    </digi:secure>,
	    loginDropdown: true
	  });
  });
</script>
