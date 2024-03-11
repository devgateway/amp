<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>
<table cellpadding="30" width="100%">
	<tr >
		<td>
			<table width="100%">	  	
				<tr>				   
				    <td> <img src="/TEMPLATE/ampTemplate/images/arrow-014E86.gif"/>
				    	<digi:edit key="aim:statusDescription:${param.catEditKey}">no description</digi:edit><br>
							<digi:secure actions="ADMIN, TRANSLATE">
								<script language="JavaScript">
									  document.write('<A HREF=${contextPath}/editor/showEditText.do?id=aim:statusDescription:${param.catEditKey}&lang=en&body=no%20text%20preview&referrer='+location.href+'>');
								      document.write('Edit');        
								      document.write('</A>');
								</script>
							</digi:secure>
					</td>    
			  </tr>
			  <tr>
				<td>
		 			<table width="100%" cellpadding="50">
						<tr>							
					 		<td  align="center">
					 			<c:set var="trn"><digi:trn key="aim:btn:close">Close</digi:trn> </c:set>
					 			<input type="button" class="dr-menu" value="${trn}" onclick="closeWindow()"/>
					 		</td>					 		
						</tr>
					 
					</table>
		 		</td>			  	
			  </tr>  
			</table>
		</td>    
	</tr>
</table>



<!-- 





 -->



<script langauage="JavaScript">
	function closeWindow() {	
		window.close();	
	}
</script>

