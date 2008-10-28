<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<script type="javascript"></script>


<digi:errors/>
<digi:instance property="aimChannelOverviewDescriptionForm" />

<TABLE cellSpacing=0 cellPadding=0 align="left" vAlign="top" border=0 width="100%">
<TR><TD>
<jsp:include page="teamPagesHeader.jsp" flush="true" />
</TD></TR>
<TR><TD>

<TABLE cellSpacing=5 cellPadding=0 align="left" vAlign="top" border=0 width="100%">
<TR>
	<TD class=r-dotted-lg width=9>&nbsp;
	</TD>
	<TD class=r-dotted-lg vAlign="top" align="left">
		<!-- contents -->

			<TABLE width="760" cellSpacing=0 cellPadding=0 vAlign="top" align="left" bgcolor="#f4f4f4" class="box-border-nopadding">
			<TR><TD bgcolor="#f4f4f4">
			
			<TABLE width="760" cellSpacing=3 cellPadding=3 vAlign="top" align="left" bgcolor="#f4f4f4">
				<TR>
					<TD align="center"><h3>
						<bean:write name="aimChannelOverviewDescriptionForm" property="name" /></h3>
					</TD>
				</TR>
				<TR bgColor=#f4f4f2>
					<TD vAlign="top" align="center" width="100%">
						<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="left" bgColor=#f4f4f2>
							<TR>
								<TD width="100%" bgcolor="#F4F4F2" height="17">
									<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2" height="17">
                           	<TR bgcolor="#F4F4F2" height="17"> 
                              	<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp;
											<digi:trn key="aim:description">Description</digi:trn></TD>
	                              <TD><IMG src="../ampTemplate/images/corner-r.gif" width="17" height="17"></TD>
   	                        </TR>
      	                  </TABLE>									
								</TD>
							</TR>
							<TR>
								<TD width="750" bgcolor="#F4F4F2" align="left" class=box-border>
									<bean:write name="aimChannelOverviewDescriptionForm" property="description" />								
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>				
				<TR>
					<TD align="center">
						<html:button property="button" value="Back" onclick="history.go(-1)" styleClass="dr-menu"/>
					</TD>
				</TR>
			</TABLE>

			</TD></TR>
			
			</TABLE>
		<!-- end -->
	</TD>
</TR>
</TABLE>

</TD></TR>
</TABLE>
