<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

		<TABLE class="padding" cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TR><TD  bgcolor="#27415f" align="center">
	      	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TR>
          			<TD align=center>
                    		<div class="footerText">
                      <img src="images/AMPLogo.png"  align="absmiddle" hspace="5" vspace="5" />
							<FONT color="#FFFFFF">
							AMP <tiles:getAsString name="version"/> build <tiles:getAsString name="build_version"/> -
							<digi:trn key="aim:ampDevelopmentCredits">
								Developed and implemented by 
								<a href='http://developmentgateway.org' style="color:#FFFFFF" target="_blank">Development Gateway (DG)</a>
								 in partnership with OECD, UNDP, WB, and Government of Ethiopia.
							</digi:trn>
							</FONT>
                            </div>
<!-- TODO: Determine where this goes, needed for admin screen -->
						<div style="background-color:#FFFFFF">
						<logic:notEmpty name="currentMember" scope="session">
							<digi:secure actions="ADMIN">
                                    <A href='<digi:site property="url"/>/admin/'>Admin</A>
                                    <a href='<digi:site property="url"/>/admin/switchDevelopmentMode.do'><digi:trn key="admin:devMode">User/Dev Mode</digi:trn></a>
                            </digi:secure>
                        </logic:notEmpty>
                        </div>
<!-- ENDTODO: Determine where this goes, needed for admin screen -->
					 	</TD>
					</TR>
				</TABLE>
			</TD>
            </TR>
		</TABLE>
