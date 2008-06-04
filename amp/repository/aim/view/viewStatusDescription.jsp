<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

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

