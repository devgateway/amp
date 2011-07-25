<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ page import="org.dgfoundation.amp.ar.AmpARFilter"%>

	<jsp:include page="headerTop_2.jsp"/>
    <center>
<div class="main_menu" id="userHomeMenu" >
    	<table cellpadding="0"cellspacing="0" width="1000">
        	<tr>
            	<td style="width:900px;" valign="top"><digi:insert attribute="headerMiddle"/></td>
                <td><digi:secure authenticated="true">
         <div class="workspace_info"> <!-- I think this class should be renamed to correspong the logout item -->   						
   			<digi:link styleClass="loginWidget" href="/j_spring_logout" module="aim">
				<digi:trn key="aim:logout">LOGOUT</digi:trn>
			</digi:link>
		</div>	
		</digi:secure></td>
            </tr>
        </table>
    	
		
	
	</div>
 </center>