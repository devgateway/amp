<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/resources/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>

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

