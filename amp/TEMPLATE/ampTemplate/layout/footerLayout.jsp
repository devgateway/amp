<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%--
<TABLE class="padding" cellSpacing=0 cellPadding=0 width="100%" border=0>
	<TR><TD>
	--%>
		<TABLE class="padding" cellSpacing=0 cellPadding=0 width="100%" border=0>
		  	<TR bgColor=#484846><TD bgColor=#ffffff align="center">
	      	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TR>
						<TD align="centre">
						<digi:img src="images/dg_footer_logo.jpg"/>
						</TD>
						<TD width=1 align="right"><digi:img src="images/contact_footer_img.jpg"/></TD>
						<TD align="left" vAlign="center" class="feedbk">
						<FONT color="#1B5887">
							<digi:trn key="aim:feedBackOrContact">
								FEEDBACK / CONTACT
							</digi:trn>
							<FONT color="#1B5887">
						</TD>
          			<TD align=left>
							<FONT color="#1B5887">
							AMP version <b><tiles:getAsString name="version"/></b> -
							<digi:trn key="aim:ampDevelepmentCredits">
								Developed in partnership with OECD, UNDP, WB and DGF
							</digi:trn>
							</FONT>
							&nbsp;&nbsp;&nbsp;
					 	</TD>
					</TR>
				</TABLE>
			</TD></TR>
			<TR bgColor=#484846><TD bgColor=#484846 align="center">
	      	<TABLE cellSpacing=3 cellPadding=3 border=0>
					<TR>
						<logic:notEmpty name="currentMember" scope="session">
							<digi:secure actions="TRANSLATE">
	  			     			<TD align=center>
									<digi:insert attribute="transmode"/>
							 	</TD>
							</digi:secure>
						</logic:notEmpty>				
						<logic:notEmpty name="currentMember" scope="session">						
							<digi:secure actions="ADMIN">
		  			     		<TD valign=top>
									<TABLE width="98%" border="0" cellpadding="0" cellspacing="0">
								      <TR>
								        	<TD align=center>
												<TABLE border="0" cellpadding="1" cellspacing="0">
								            	<TR>
														<TD bgColor="firebrick">
								                		<TABLE border="0" cellpadding="3" cellspacing="1">
								                  		<TR align=center bgcolor="Gold">
                      										<TD>
																<A href='<digi:site property="url"/>/admin/'>Admin</A>
								                      		</TD>
                      										<TD>
																<a href='<digi:site property="url"/>/admin/switchDevelopmentMode.do'><digi:trn key="admin:devMode">User/Dev Mode</digi:trn></a>
								                      		</TD>
																</TR>
								                		</TABLE>
									               </TD>
													</TR>
										      </TABLE>
											</TD>
							      	</TR>
								   </TABLE>
	 							</TD>						
								</digi:secure>								
							</logic:notEmpty>
						
							<% if (session.getAttribute("ampAdmin") != null && session.getAttribute("ampAdmin").equals("yes")) { %>
			  	        		<TD align=center vAlign="top">
									<digi:insert attribute="ampAdminLink"/>
								</TD>
							<% } %>
					</TR>
				</TABLE>			
			</TD></TR>
		</TABLE>
		<%--
	</TD></TR>

	<TR><TD align="center">
		<digi:insert attribute="flatLangSwitch" >
			<tiles:put name="redirectToRoot" value="True" />
		</digi:insert>	
	</TD></TR>

</TABLE>
--%>
