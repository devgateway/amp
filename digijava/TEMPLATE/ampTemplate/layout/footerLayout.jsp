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
		  	<TR bgColor=#484846><TD bgColor=#484846 align="center">
	      	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TR>
						<TD width=1 align="right"><digi:img src="images/feedback.gif"/></TD>
						<TD align=left vAlign="center" class="feedbk">
							<digi:trn key="aim:feedBackOrContact">
								FEEDBACK / CONTACT
							</digi:trn>
						</TD>
          			<TD align=right>
							<FONT color="#FFFFFF">
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
