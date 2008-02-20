<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="helpForm"/>
<TABLE align=center border=0 cellPadding=2 cellSpacing=3 width="100%" bgcolor="#f4f4f2">

	<TR>
		<TD class=r-dotted-lg-buttom vAlign=top>
			<TABLE border=0 cellPadding=0 cellSpacing=0 width="100%" >
        		<TR><TD>
              		<TABLE border=0 cellPadding=0 cellSpacing=0 >
              			<TR bgColor=#f4f4f2>
                 			<TD bgColor=#c9c9c7 class=box-title> <digi:trn key="aim:help:editcreate"> Edit/Create</digi:trn></TD>
                    		<TD background="module/aim/images/corner-r.gif"	height=17 width=17></TD>
						</TR>
					</TABLE>
				</TD></TR>
				<TR><TD bgColor=#ffffff class=box-border align=left>
					<TABLE>
						<TR>
							<TD nowrap>
								<c:set var="topicEdit">
									<digi:trn key="aim:help:clickToEditHelpTopic">Click here to Edit Help Topic</digi:trn>
								</c:set>
								[<digi:link href="/helpActions.do?actionType=editHelpTopic&topicKey=${helpForm.topicKey}&wizardStep=0" 
								title="${topicEdit}" ><digi:trn key="aim:help:editTopic">Edit Topic</digi:trn></digi:link>]
							</TD>
							<TD nowrap>
								<c:set var="topicCreate">
									<digi:trn key="aim:help:clickToAddHelpTopic">Click here to Create Help Topic</digi:trn>
								</c:set>
								[<digi:link href="/helpActions.do?actionType=createHelpTopic&wizardStep=0" 
								title="${topicCreate}" ><digi:trn key="aim:help:createTopic">Create Topic</digi:trn></digi:link>]
							</TD>
							<TD nowrap>
								<c:set var="topicDelete">
									<digi:trn key="aim:help:clickToDeleteHelpTopic">Click here to Delete Help Topic</digi:trn>
								</c:set>
								[<digi:link href="/helpActions.do?actionType=deleteHelpTopic&topicKey=${helpForm.topicKey}" 
								title="${topicDelete}" ><digi:trn key="aim:help:removeTopic">Remove Topic</digi:trn></digi:link>]
							</TD>
						</TR>
					</TABLE>
				</TD></TR>
			</TABLE>
		</TD>
	</TR>
	
</TABLE>