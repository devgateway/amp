<%@ page language="java" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>


<div id="amp-footer"></div>

<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/node_modules/amp-boilerplate/dist/amp-boilerplate.js"/>"></script>
<script src="/repository/bootstrap/bootstrap.min.js"></script>
    
<script type="text/javascript">
var boilerplate = new window.boilerplate(
     	  {
     		showAdminFooter: true
		  });


$( document ).on( "switchLanguage", function(event) {
  //alert( 'aa' );
});
$(boilerplate.menu).on( "switchLanguage", function(event) {
	//  alert( 'bb' );
	});
</script>



