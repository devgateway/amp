<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:ref href="css/demoUI.css" rel="stylesheet" type="text/css" />
<digi:context name="digiContext" property="context" />

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="1"><digi:img src="images/ui/header/logo.gif"/></td>
		<td width="99%">
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td><digi:img src="images/ui/header/logoRight.gif"/></td>
					<td class="headerBgr" width="99%">
					 &nbsp;
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<table border="0" cellpadding="0" cellspacing="0" width="100%">
							<tr class="menuTop">
								<td><digi:img src="images/ui/header/menuStartTop.gif"/></td>
							</tr>
							<tr class="menuBgr">
								<td>
								
									<table border="0" cellpadding="0" cellspacing="0" width="100%">
										<tr>

											<td width="1"><digi:img src="images/ui/header/menuStart.gif"/></td>
											<td nowrap valign="center">
												<a href="<digi:site property="url"/>" class="demo">Home</a>&nbsp;
											</td>
											<td width="1"><digi:img src="images/ui/header/navLinkSep.gif"/></td>
											<%--<td nowrap valign="center">
												&nbsp;<a href="<digi:site siteId="demosite"/>/editor/showPage.do?key=About us" class="demo">About us</a>&nbsp;
											</td>
											<td width="1"><digi:img src="images/ui/header/navLinkSep.gif"/></td>--%>
											<td nowrap valign="center">
												&nbsp;<a href="<%= digiContext %>/forum/index.do" class="demo">Forum</a>&nbsp;
											</td>
											<td width="1"><digi:img src="images/ui/header/navLinkSep.gif"/></td>
											<td nowrap valign="center">
												&nbsp;<digi:insert attribute="sdm" />&nbsp;
											</td>
											<td width="1"><digi:img src="images/ui/header/navLinkSep.gif"/></td>
											<digi:secure actions="ADMIN">
												<td nowrap valign="center">
													&nbsp;<a href="<%= digiContext %>/editor/index.do" class="demo">Pages</a>&nbsp;
												</td>
												<td width="1"><digi:img src="images/ui/header/navLinkSep.gif"/></td>
											</digi:secure>
			
											
										</tr>
									</table>										
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

