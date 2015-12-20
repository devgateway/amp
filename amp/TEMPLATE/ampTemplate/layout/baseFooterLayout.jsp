<%@ page language="java"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>


<div id="amp-footer" style=""></div>

<script type="text/javascript">
$( document ).ready(function() {
	  var boilerplate = new window.boilerplate({
	    showAdminFooter: true,
	    showLogin: <digi:secure authenticated="true">false</digi:secure><digi:secure authenticated="false">true
	    </digi:secure>,
	    loginDropdown: true
	  });
});
  
</script>