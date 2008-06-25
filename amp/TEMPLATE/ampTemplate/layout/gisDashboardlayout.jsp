<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<HTML>
	<digi:base />
	<digi:context name="digiContext" property="context"/>
	<HEAD>
		<TITLE><tiles:getAsString name="title"/></TITLE>
		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">		
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
		<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
     	<META HTTP-EQUIV="EXPIRES" CONTENT="0">		
		<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
	    <digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />
	</HEAD>
	
    <BODY leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">
		<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=0 valign="top" align="left">
			<TBODY>
			<TR>
				<TD width="100%" bgColor=#323232 vAlign="center" align="left" height="10">
					<digi:insert attribute="headerTop" />
				</TD>
			</TR>
			<TR>
				<TD width="100%" align="center" bgcolor="#376091">
					<TABLE cellSpacing=0 cellPadding=0 width="98%">

						<TBODY>

						  	<TR>

						   	<TD align="left" vAlign="center">

									<digi:insert attribute="headerMiddle" />
								</TD>	
							 	<TD align="right" vAlign="middle" style="padding-right:5px;">
										<digi:insert attribute="loginWidget" />
								</TD>
					</TR>
						</TBODY>
					</TABLE>
				</TD>
			</TR>
			<TR>
				<TD width="100%" vAlign="top" align="left">
					<TABLE bgColor=#ffffff cellPadding=0 cellSpacing=0 width="99%" vAlign="top" align="left" border=0>
						<TR>
							<TD  width="10">&nbsp;</td>
							<TD align=center vAlign=top>
								<TABLE width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="left" border="0">
									<TR>
										<TD vAlign="top" align="left" width="75%" >
											<digi:insert attribute="body" />
										</TD>
										<td valign="top">
											Pie Chart											
										</td>										
									</TR>
									<tr>
										<td>
											<table cellpadding="15">
												<tr>
													<td>
														<digi:insert attribute="widget1">
															<digi:put name="widget-teaser-param">chart_widget1</digi:put>
														</digi:insert>
													
													</td>
													<td>
														<digi:insert attribute="widget2">
															<digi:put name="widget-teaser-param">chart_widget2</digi:put>
														</digi:insert>
													
													</td>
													<td>
														<digi:insert attribute="widget3">
															<digi:put name="widget-teaser-param">chart_widget3</digi:put>
														</digi:insert>
													
													</td>
												</tr>
												<tr>
													<td>
														<digi:insert attribute="widget4">
															<digi:put name="widget-teaser-param">chart_widget4</digi:put>
														</digi:insert>
													
													</td>
													<td>
														<digi:insert attribute="widget5">
															<digi:put name="widget-teaser-param">chart_widget5</digi:put>
														</digi:insert>
													
													</td>
													<td>
														<digi:insert attribute="widget6">
															<digi:put name="widget-teaser-param">chart_widget6</digi:put>
														</digi:insert>
													
													</td>
												</tr>
											</table>
										</td>
										<td>
											<table cellpadding="5">
												<tr>
													<td>
														<digi:insert attribute="tableWidget5">
															<digi:put name="widget-teaser-param">test_widget1</digi:put>
														</digi:insert>
													</td>
													<td rowspan="2">
														<digi:insert attribute="tableWidget6">
															<digi:put name="widget-teaser-param">test_widget1</digi:put>
														</digi:insert>
													</td>
												</tr>
												<tr>
													<td>
														<digi:insert attribute="tableWidget7">
															<digi:put name="widget-teaser-param">test_widget1</digi:put>
														</digi:insert>
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>
											<digi:insert attribute="tableWidget1">
												<digi:put name="widget-teaser-param">test_widget1</digi:put>
											</digi:insert>
										
										</td>
										<td>
											<digi:insert attribute="tableWidget1">
												<digi:put name="widget-teaser-param">test_widget1</digi:put>
											</digi:insert>
										</td>
									</tr>
									<tr>
										<td>
											<digi:insert attribute="tableWidget3">
												<digi:put name="widget-teaser-param">test_widget1</digi:put>
											</digi:insert>
										
										</td>
										<td>
											<digi:insert attribute="tableWidget4">
												<digi:put name="widget-teaser-param">test_widget1</digi:put>
											</digi:insert>
										</td>
									</tr>
									
								</TABLE>
							</TD>
						</TR>
					</TABLE>
				</TD>
			</TR>
			<TR>
				<TD width="100%" >
				   <digi:insert attribute="footer" />
				</TD>
			</TR>
			</TBODY>
		</TABLE>
	</BODY>
</HTML>

