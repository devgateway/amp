<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@taglib uri="/taglib/struts-html" prefix="html"%>
<%@taglib uri="/taglib/digijava" prefix="digi"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<digi:instance property="aimNPDForm"/>

<script language="javascript" type="text/javascript">

	function getDescription() {
	document.getElementById("t1").innerHTML="txt";	
		
	}
	
	var localIndicators=[];
	
	function setUpWin(){
		showGraph();
		setExcelURL();
	}
	
	function showGraph(){
		var getImage  = document.getElementById('graph');
		if (getImage != null){
			getImage.src = window.opener.curGraphURL;
		}
	}
	
	function setExcelURL(){
		var exportLink  = document.getElementById('export2xsl');
		var url = window.opener.addActionToURL('exportIndicators2xsl.do');
		var params = window.opener.getInidcatorsParam();
		exportLink.href=url+params;
	}

	window.onload=setUpWin;

</script>

<style>

	table#indres {
		border : silver solid;
		border-width: 1px 1px 0px 0px;
		
	}

	table#indres td{
		border : silver solid;
		border-width: 0px 0px 1px 1px;
		
	}

</style>

<style type="text/css" media="print">

    .noPrint{
        display: none;
    }

</style>



<table width="100%" cellSpacing="5" cellPadding="5" vAlign="top" border="0">
	<tr>
		<td valign="top">
			<table cellPadding="3" cellSpacing="3" width="100%" class="box-border-nopadding">
				<tr>
					<td align="left" vAlign="top">
     
     				<c:if test="${aimNPDForm.mode == 1}">
							<table width="100%" cellSpacing="5" cellPadding="5" vAlign="top" border="0">
								<tr>
									<td valign="top">
										<table cellPadding="5" cellSpacing="5" width="100%">
                                            <tr class="noPrint">
                                                <td align="left"
                                                    <digi:link styleId="export2xsl" href="/exportIndicators2xsl.do~programId=${aimNPDForm.programId}">
                                                        <digi:trn key="rep:tool:exporttoexcel">Export to Excel</digi:trn>
                                                        &nbsp;<digi:img src="images/xls_icon.jpg" border="0"/></digi:link>
                                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <digi:link styleId="printWin" href="#" onclick="window.print(); return false;">
                                                        <digi:trn key="aim:print">Print</digi:trn>
                                                        &nbsp;<digi:img src="images/print_icon.gif" border="0"/></digi:link>
                                            </td>
                                        </tr>
									            <tr>
												<td align="left" vAlign="top">
													<table bgcolor="#f4f4f2" cellPadding="0" cellSpacing="0" width="100%" class="box-border-nopadding">
														<tr bgcolor="#006699">
															<td valign="center" width="100%" align ="center" class="textalb" height="20">
																<digi:trn key="aim:indGrid:indicGraphHeader">Graph</digi:trn>
															</td>
														</tr>
														<tr>
															<td bgcolor="#ecf3fd">
																<img id="graph">
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</c:if>
					
					<c:if test="${aimNPDForm.mode != 1}">
                     <tr  class="noPrint">
					<td align="left">
									<digi:link styleId="export2xsl" href="/exportIndicators2xsl.do~programId=${aimNPDForm.programId}"><digi:trn key="rep:tool:exporttoexcel">Export to Excel</digi:trn>&nbsp;<digi:img src="images/xls_icon.jpg" border="0"/></digi:link>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<digi:link styleId="printWin" href="#" onclick="window.print(); return false;"><digi:trn key="aim:print">Print</digi:trn> &nbsp;<digi:img src="images/print_icon.gif" border="0"/></digi:link>
								</td>
                     </tr>
					</c:if>
						<table bgcolor="#f4f4f2" cellPadding="0" cellSpacing="0" width="100%" class="box-border-nopadding">
							<tr bgcolor="#006699">
								<td valign="center" width="100%" align ="center" class="textalb" height="20">
									<digi:trn key="aim:indGrid:indicatorsHader">indicators</digi:trn>
								</td>
							</tr>
							<tr>
								<td bgcolor="#ecf3fd" align="center">
									<table width="80%" id="indres" cellspacing="0">
										<tr>
											<td>
												&nbsp;
											</td>
											<c:forEach  var="year" items="${aimNPDForm.selYears}">
												<td colspan="3" align="center">
													<strong>${year}</strong>
												</td>
											</c:forEach>
										</tr>
										<tr>
											<td>
												<strong><digi:trn key="aim:indGrid:indicname">Indicator Name</digi:trn></strong>
											</td>
											<c:forEach  var="year" items="${aimNPDForm.selYears}">
                                                                                                <td align="center">
                                                                                                    <strong><digi:trn key="aim:indGrid:baseVal">Base</digi:trn></strong>
                                                                                                </td>
												<td align="center">
													<strong><digi:trn key="aim:indGrid:actualVal">Actual</digi:trn></strong>
												</td>
												<td align="center">
													<strong><digi:trn key="aim:indGrid:targetVal">Target</digi:trn></strong>
												</td>
				
											</c:forEach>
											
										</tr>
										<c:if test="${!empty aimNPDForm.indicators}">
											<c:forEach var="indRow" items="${aimNPDForm.indicators}">
												<tr>
													<td> 
														<span title="${indRow.description}">${indRow.name}</span>
													</td>
													<c:forEach  var="val" items="${indRow.values}">
                                                                                                                 <td align="right">
															${val.baseValue}
														</td>
														<td align="right">
															${val.actualValue}
														</td>
														<td align="right">
															${val.targetValue}
														</td>
															
												</c:forEach>
												</tr>
											</c:forEach>
										</c:if>
									</table>
								</td>
							</tr>
							<tr>
								<td bgcolor="#ecf3fd">
									&nbsp;
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<table width="100%" class="noPrint">
	<tr>
		<td align="center">
		<html:button styleClass="dr-dialogmenu" property="submitButton" onclick="window.close();">
				<digi:trn key="aim:close">Close</digi:trn>
			</html:button>
		</td>
	</tr>

</table>