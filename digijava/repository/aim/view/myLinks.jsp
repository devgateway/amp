<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<SCRIPT language="JavaScript">
	function addLinks(id) {
		window.name = "opener" + new Date().getTime();		  
		var t = ((screen.width)-420)/2;
		var l = ((screen.height)-110)/2;
		addLinksWindow = window.open("","",'resizable=no,width=420,height=110,top='+l+',left='+t);
		addLinksWindow.document.open();
		addLinksWindow.document.write(getAddLinksWindowString(id));
		addLinksWindow.document.close();
	}

</SCRIPT>

<TABLE align=center border=0 cellPadding=2 cellSpacing=3 width="100%">
	<TR>
		<TD class=r-dotted-lg-buttom vAlign=top>
			<TABLE border=0 cellPadding=0 cellSpacing=0 width="100%">
        		<TR><TD>
              	<TABLE border=0 cellPadding=0 cellSpacing=0 width="138" bgColor=#f4f4f2>
              		<TR bgColor=#f4f4f2>
                 		<TD bgColor=#c9c9c7 class=box-title width=121>
								<A title="<digi:trn key="aim:ListofRelatedLinks">Frequently Used Links for Desktop</digi:trn>">
									<digi:trn key="aim:relatedLinks">Related Links</digi:trn>
								</A>
							</TD>
                    	<TD background="module/aim/images/corner-r.gif" 
							height=17 width=17></TD>
						</TR>
					</TABLE>
				</TD></TR>
				<logic:notEmpty name="myLinks" scope="session">
				<TR><TD bgColor=#ffffff class=box-border align=left>
					<TABLE border=0 cellPadding=1 cellSpacing=1 width="100%" >
					<logic:iterate name="myLinks" id="link" scope="session" 
					type="org.digijava.module.aim.helper.Documents"> 
						<TR><TD>
							<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10>
								<bean:define id="translation">
									<digi:trn key="aim:clickToViewLinkDetails">Click here to view Link Details</digi:trn>
								</bean:define>
								<digi:link href="/viewRelatedLinks.do" title="<%=translation%>" >
									<bean:write name="link" property="title"/>
								</digi:link>
						</TD></TR>
					</logic:iterate>
					<bean:size id="linkCount" name="myLinks" scope="session" />
					<c:if test="${linkCount > 5}">
						<TR><TD>
							<bean:define id="translation">
								<digi:trn key="aim:clickToViewMoreLinks">Click here to view more Links</digi:trn>
							</bean:define>
							<digi:link href="/viewRelatedLinks.do" title="<%=translation%>" >
								<digi:trn key="aim:more">..more</digi:trn>
							</digi:link>							
						</TD></TR>
					</c:if>
					</TABLE>
				</TD></TR>
				</logic:notEmpty>
				<logic:empty name="myLinks" scope="session">
				<TR><TD bgColor=#ffffff class=box-border align=left>
					<digi:trn key="aim:noDesktopLinks">No links</digi:trn>
				</TD></TR>
				</logic:empty>
				
				<TR><TD>
					<bean:define id="memberId" name="currentMember" scope="session" 
					property="memberId" />
					<A href="javascript:addLinks(<c:out value="${memberId}"/>)">
					<digi:trn key="aim:addLinks">Add Links</digi:trn></A>
				</TD></TR>				
			</TABLE>	
		</TD>
	</TR>
</TABLE>
