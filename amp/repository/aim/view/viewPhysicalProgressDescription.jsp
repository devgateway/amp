<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<script type="javascript">
function login()
{
	<digi:context name="addUrl" property="context/module/moduleinstance/login.do" />
    document.aimPhysicalProgressForm.action = "<%=addUrl%>";
    document.aimPhysicalProgressForm.submit();
}
</script>
<digi:errors/>
<digi:instance property="aimPhysicalProgressForm" />
<logic:equal name="aimPhysicalProgressForm" property="validLogin" value="false">
<digi:form action="/login.do" name="aimPhysicalProgressForm" type="org.digijava.module.aim.form.PhysicalProgressForm" 
method="post">
<h3 align="center"> Invalid Login. Please Login Again. </h3><p align="center"><html:submit styleClass="dr-menu" value="Log In" onclick="login()" /></p>
</digi:form>
</logic:equal>

<logic:equal name="aimPhysicalProgressForm" property="validLogin" value="true">



<TABLE cellspacing="0" cellpadding="0" align="center" vAlign="top" border="0" width="100%">
<TR>
	<TD vAlign="top" align="center">
		<!-- contents -->

			<TABLE width="99%" cellspacing="0" cellpadding="0" vAlign="top" align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
			<TR><TD bgcolor="#f4f4f4">
			
			<TABLE width="100%" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4">
				<TR bgColor=#f4f4f2>
            	<TD align=left>
						<SPAN class=crumb>					
							<jsp:useBean id="urlPhysicalProgress" type="java.util.Map" class="java.util.HashMap"/>
							<c:set target="${urlPhysicalProgress}" property="ampActivityId">
								<bean:write name="aimPhysicalProgressForm" property="ampActivityId"/>
							</c:set>
							<c:set target="${urlPhysicalProgress}" property="tabIndex" value="2"/>
							<c:set var="translation">
								<digi:trn key="aim:clickToViewPhysicalProgress">Click here to view Physical Progress</digi:trn>
							</c:set>
							<digi:link href="/viewPhysicalProgress.do" name="urlPhysicalProgress" styleClass="comment" title="${translation}" >
								<digi:trn key="aim:physicalProgress">Physical Progress</digi:trn>
							</digi:link> &gt; 
							<bean:write name="aimPhysicalProgressForm" property="title" /> &gt; 
							<bean:write name="aimPhysicalProgressForm" property="perspective"/> Perspective
						</SPAN>
					</TD>
				</TR>
				<TR bgColor=#f4f4f2>
					<TD vAlign="top" align="center" width="100%">
						<TABLE width="98%" cellpadding="0" cellspacing="0" vAlign="top" align="center" bgColor=#f4f4f2>
							<TR>
								<TD width="100%" bgcolor="#F4F4F2" height="20">
									<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2" height="20" width="100%" align="center">
                           	<TR bgcolor="#F4F4F2" height="20"> 
                              	<TD bgcolor="#C9C9C7" class="box-title" align="center">
											<bean:write name="aimPhysicalProgressForm" property="title" /></TD>
   	                        </TR>
      	                  </TABLE>									
								</TD>
							</TR>
							<tr>
								<td>
									<table width="100%" cellPadding="5" cellSpacing="1" vAlign="top" align="center" bgColor=#ffffff
									class="box-border-nopadding">
										<tr bgcolor="#f4f4f2">
											<td width="17%" vAlign="top" align="left"><b>
												<digi:trn key="aim:component">Component</digi:trn></b>
											</td>
											<td width="83%" vAlign="top" align="left">
												<bean:write name="aimPhysicalProgressForm" property="compTitle" />
											</td>
										</tr>
									</table>	
								</td>
							</tr>
							<tr>
								<td>
									<table width="100%" cellPadding="5" cellSpacing="1" vAlign="top" align="center" bgColor=#ffffff
									class="box-border-nopadding">
										<tr bgcolor="#f4f4f2">
											<td width="17%" vAlign="top" align="left"><b>
												<digi:trn key="aim:reportingDate">Reporting Date</digi:trn></b>
											</td>
											<td width="83%" vAlign="top" align="left">
												<bean:write name="aimPhysicalProgressForm" property="ppRepDate" />
											</td>
										</tr>
									</table>	
								</td>
							</tr>
							<TR>
								<TD width="100%" bgcolor="#F4F4F2" align="center">
									<TABLE width="100%" cellPadding="5" cellSpacing="1" vAlign="top" align="center" bgColor=#ffffff
									class="box-border-nopadding">
										<TR bgcolor="#f4f4f2">
											<TD width="100" vAlign="top" align="left"><b>
												<digi:trn key="aim:description">
												Description</digi:trn></b>
											</TD>
											<TD vAlign="top" align="left">
												<bean:write name="aimPhysicalProgressForm" property="description" />								
											</TD>
										</TR>
										
									</TABLE>
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>				

			</TABLE>

			</TD></TR>
			
			</TABLE>
		<!-- end -->
	</TD>
</TR>
<TR>
	<TD>&nbsp;</TD>
</TR>
</TABLE>
</logic:equal>



