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



<HTML>

	<digi:base />

	<script type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/EnterHitBinder.js'/>" >.</script>
	<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

	
    

	<digi:context name="digiContext" property="context"/>



	<HEAD>

		<TITLE>
			<%
			String title=(String)((org.apache.struts.tiles.ComponentContext) request.getAttribute("org.apache.struts.taglib.tiles.CompContext")).getAttribute("title");
			String key=(title.replaceAll(" ",""));
			%>
			<c:set var="key">aim:pagetitle:<%=key%></c:set>
				<digi:trn>Aid Management Platform </digi:trn> 
				<digi:trn key="${key}">
					<%=title%>
				</digi:trn>
		</TITLE>
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">

		<META HTTP-EQUIV="Expires" CONTENT="0">

		<META HTTP-EQUIV="Cache-Control" CONTENT="private">

		<script language="javascript">

		function quitRnot1(message)
		{
		}

		</script>
            
        <!-- Dependencies --> 
        <script type="text/javascript" src="<digi:file src="script/yui/yahoo-dom-event.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/container_core-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/element-beta-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/connection-min.js"/>"></script>
        
        <!-- Source File -->
        <script type="text/javascript" src="<digi:file src="script/yui/menu-min.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="script/yui/yahoo-dom-event.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="script/yui/container-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="script/yui/menu-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="script/yui/element-beta-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="script/yui/tabview-min.js"/>"></script> 

        <!-- Core + Skin CSS -->
        <digi:ref href="css/menu.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/container.css" type="text/css" rel="stylesheet" />

        <!-- Stylesheet of AMP -->
        <digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />


	</HEAD>



	<BODY leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">

		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top" align="left">

			<TBODY>

			<TR>

				<TD width="100%" vAlign="center" align="left">

					<digi:insert attribute="headerTop" />

				</TD>

			</TR>

			<TR>
				<TD width="100%" align="center" bgcolor="#376091">
					<TABLE cellSpacing=0 cellPadding=0 width="98%">
						<TBODY>
						  	<TR>
                                <TD>
                                    <digi:insert attribute="headerMiddle" />
									<!-- digi:insert attribute="dropdownLangSwitch" /-->
                                </TD>
                                <TD align="right" valign="middle" nowrap="nowrap">
                                    <digi:insert attribute="loginWidget" />
                                </TD>
							</TR>
						</TBODY>
					</TABLE>
				</TD>
			</TR>
			<TR>

				<TD width="100%" vAlign="top" align="left">

					<TABLE bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" vAlign="top" align="left" border=0>

						<TR>
							<TD align=center vAlign=top>
								<TABLE width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="left" border="0">

									<TR>

										<TD vAlign="top" align="left" width="85%">
											<logic:notPresent name="currentMember">
											<br/>
											</logic:notPresent>

											<digi:insert attribute="body" />
<br/><br/><br/><br/>
										</TD>

										<TD vAlign="top" align="left" width="15%">
											<TABLE width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="left" border="0">
												<logic:present name="currentMember">

												<TR><TD vAlign="top" align="left">

													<digi:insert attribute="myReports" />

												</TD></TR>
											

												<bean:define id="lead" property="teamHead" name="currentMember" scope="session"/>
												
													<TR><TD vAlign="top" align="left">
														<digi:insert attribute="myMessages"/>
													</TD></TR>
												<TR>
														<TD vAlign="top" align="left">
															<digi:insert attribute="myLinks" />
														</TD>
		
												</logic:present>
												</TR>
										</TABLE>

										</TD>

									</TR>

								</TABLE>
                            </TD>

						</TR>

					</TABLE>

				</TD>

			</TR>


			</TBODY>

		</TABLE>
        
		<table cellpadding="0" cellspacing="0" width="100%" style="clear:both;position:fixed;bottom:0px;top:auto;">
			<TR>

				<TD width="100%" bgcolor="#27415f">
					 <digi:insert attribute="footer" />
				</TD>

			</TR>
		</TABLE>

	</BODY>

</HTML>

