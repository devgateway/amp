<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
	
	<%-- request.setAttribute("compatibility_shim", String) - use it to overwrite specific pages' X-UA-Compatible meta tags --%>
	<c:choose>
    	<c:when test="${empty compatibility_shim}">
			<meta http-equiv="X-UA-Compatible" content="chrome=1; IE=edge">
		</c:when>
    	<c:otherwise>
        	<meta http-equiv="X-UA-Compatible" content="<c:out value="${compatibility_shim}" />">
    	</c:otherwise>
	</c:choose>
	<digi:base />
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/EnterHitBinder.js'/>" >.</script>
	<digi:context name="digiContext" property="context"/>
	<script language="JavaScript" type="text/javascript">
    <!--
    function addLoadEvent(func) {
    	  var oldonload = window.onload;
    	  if (typeof window.onload != 'function') {
    	    window.onload = func;
    	  } else {
    	    window.onload = function () {
    	      if (oldonload) {
    	        oldonload();
    	      }
    	      func();
    	  }
    	}
    }  	
	-->  
    </script>
    
	<%@include file="title.jsp"%>
	
	<!-- Scripts  -->
	
	<script language="JavaScript" type="text/javascript" src="/repository/aim/view/scripts/common.js"></script>
	<digi:ref href="css/ampPrint.css" type="text/css" rel="stylesheet" media="print" />
	<link rel="stylesheet" href="tabs/css/bootstrap.css">
	<link rel="stylesheet" href="tabs/css/bootstrap-theme.css">
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/node_modules/amp-boilerplate/dist/amp-boilerplate.js"/>"></script>
	
<% if(org.digijava.kernel.util.SiteUtils.isEffectiveLangRTL() == true) {%>
     <link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/node_modules/amp-boilerplate/src/css/boilerplate-rtl.css">
	 <link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css_2/amp-rtl.css"> 
<% } %>	
</head>


<BODY leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">
<div id="amp-header"></div>
<div class="headerTop">
	<logic:present name="currentMember" scope="session">
		<jsp:include page="headerTop_2.jsp"/>
	</logic:present>
	<logic:notPresent name="currentMember" scope="session">
		<digi:insert attribute="headerTop" />
	</logic:notPresent>
</div>
<digi:insert attribute="headerMiddle"/>
<%-- <logic:notPresent name="bootstrap_insert" scope="request">  --%>
	<div class="breadcrump_1">&nbsp;</div>
	<div style="width:1000px;margin:0 auto;">
		<table width="100%" id="homelayout">
			<tr><td>		
				<digi:insert attribute="body"/>
			</td></tr>
		</table>
    </div>
    <div class="footerText" >
    	<digi:insert attribute="footer"/>
    </div>    
<%--  </logic:notPresent> --%>
<%--<logic:present name="bootstrap_insert" scope="request">
	<digi:insert attribute="body"/>
	<!-- no footer in boostrap-iframe hacks  -->
</logic:present>
 --%>
</body>
</html>