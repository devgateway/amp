<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<SCRIPT language="JavaScript">
function addLink(id){
  <digi:context name="rev" property="context/module/moduleinstance/addQuickLink.do" />
  openURLinWindow("<%=rev%>?memId="+id,400,100);
	}

</SCRIPT>

<TABLE align=center border=0 cellPadding=2 cellSpacing=3 width="100%">
	<TR>
		<TD  vAlign=top>
			<TABLE border=0 cellPadding=0 cellSpacing=0 width="100%">
        		<TR><TD>
              	<TABLE border=0 cellPadding=0 cellSpacing=0 bgColor=#f4f4f2>
              		<TR>
                 		<TD bgColor=#ffffff  
							title='<digi:trn key="aim:ListofRelatedLinks">Frequently Used Links for Desktop</digi:trn>'>
							<span id="desktopFont">	<digi:trn key="aim:relatedLinks">Related Links</digi:trn> </span>
							</TD>
                    	
						</TR>
					</TABLE>
				</TD></TR>
				<tr height=2px><td height=2px bgcolor="#000000"></td></tr>
				<bean:define id="translation">
					<digi:trn key="aim:clickToViewLinkDetails">Click here to view Link Details</digi:trn>
				</bean:define>				
				<logic:notEmpty name="myLinks" scope="session">
				<TR><TD bgColor=#ffffff  align=left>
					<TABLE border=0 cellPadding=1 cellSpacing=1 width="100%" >
					<% int rCount = 1; %>
					
					<logic:iterate name="myLinks" id="link" scope="session" 
					type="org.digijava.module.aim.helper.Documents"> 
						<% if (rCount < 5) { rCount ++; %>
						<% if (rCount%2==0) { %>
						<TR><TD title='<%=translation%>' >
							<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10>
								<digi:link href="/viewQuickLinks.do">
									<bean:write name="link" property="title"/>
								</digi:link>
						</TD></TR>
						<% } %>
								  <% if (rCount%2==0) { %>
						<TR><TD title='<%=translation%>' bgcolor="c9c9c7">
							<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10>
								<digi:link href="/viewQuickLinks.do">
									<bean:write name="link" property="title"/>
								</digi:link>
						</TD></TR>
						<% } %>
						<% } %>
					</logic:iterate>
					<bean:size id="linkCount" name="myLinks" scope="session" />
					<c:if test="${linkCount > 5}">
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewMoreLinks">Click here to view more Links</digi:trn>
						</bean:define>					
						<TR><TD title='<%=translation%>'>
							<digi:link href="/viewQuickLinks.do">
								<digi:trn key="aim:more">..more</digi:trn>
							</digi:link>							
						</TD></TR>
					</c:if>
					</TABLE>
				</TD></TR>
				</logic:notEmpty>
				<logic:empty name="myLinks" scope="session">
				<TR><TD bgColor=#ffffff align=left>
					<digi:trn key="aim:noDesktopLinks">No links</digi:trn>
				</TD></TR>
				</logic:empty>
				
				<TR><TD>
					<bean:define id="memberId" name="currentMember" scope="session" 
					property="memberId" />
					<A href="javascript:addLink(${memberId})">
					<digi:trn key="aim:addLinks">Add Links</digi:trn></A>
				</TD></TR>	
	<tr height=2px><td height=2px bgcolor="#000000"></td></tr>			
			</TABLE>	
		</TD>
	</TR>
</TABLE>
