<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="globalsettings" %>

<digi:instance property="aimUserEmailForm" />


<table bgColor=#ffffff border=0 cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td width=14>&nbsp;
		</td>
		<td align=left vAlign=top width=520><br>
			<table border=0 cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td width="3%">&nbsp;</td>				
					<td align="left" valign="top" class="text">
						<digi:trn key="aim:emailSentTo">
						An e-mail has been sent to</digi:trn> 
						<b><c:out value="${aimUserEmailForm.email}" /></b>
    				</td>
				</tr>
				<tr>
					<td width="3%">&nbsp;</td>				
					<td align="left" valign="top" class="text">
						<digi:trn key="aim:clickOnLinkToCreatePassword">
						Click on the link included in the e-mail to create a new password
						</digi:trn>
    				</td>
				</tr>				
			</table>
		</td>
		<td bgColor=#dbe5f1 vAlign=top>
	      <table align=center border=0 cellPadding=3 cellSpacing=0 width="90%">
      		 <tr>
		          <td vAlign=top><br/>
		          	<jsp:include page="countriesLnk.jsp"  />		          
						<BR/><BR/><BR/>						
      	     	</td>
        		</tr>
        		<tr>
		          <td vAlign=top>&nbsp;</td>
        		</tr>
        		<tr>
	          	<td vAlign=top>
						 <strong>
						 <digi:trn key="aim:loginWarning">
						 You are signing-in to one or more secure applications for        
	        		     official business. You have been granted the right to access these        
    	      		 	 applications and the information contained in them to facilitate        
        	   			 your official business. Your accounts and passwords are your        
						 responsibility. Do not share them with anyone.        
						 </digi:trn>
						 </strong>
						<BR><BR>
          		</td>
  				</tr> 
        		<tr>
          		<td vAlign=top>&nbsp;</td>
  				</tr>
	      </table>
		</td>
		</tr>
</table>