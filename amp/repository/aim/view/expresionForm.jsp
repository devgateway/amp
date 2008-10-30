<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>



<<script type="text/javascript">
<!--
	var last_element;

	function init(){
		last_element = "";
	}

	function keyPress(key){
		oldvalue = document.getElementById("expresion").value
		document.getElementById("expresion").value = oldvalue + key;
	}

	init();
	
//-->
</script>

<digi:instance property="expresionbuilderForm" />
<digi:context name="digiContext" property="context" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<digi:form action="/expresionBuilderMaganer.do" method="post">

	<table bgColor=#ffffff cellPadding=5 cellSpacing=1 width=705>
		<tr>
			<td class=r-dotted-lg width=14>&nbsp;</td>
			<td align=left class=r-dotted-lg vAlign=top width=752>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb> <c:set
						var="translation">
						<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
					</c:set> <digi:link module="aim" href="/admin.do" styleClass="comment"
						title="${translation}">
						<digi:trn key="aim:AmpAdminHome">
				            Admin Home
				            </digi:trn>
					</digi:link>&nbsp;&gt;&nbsp; <c:set var="translation">
						<digi:trn key="aim:clickexpresionbuildermanager">Click here to view Expresion Buildern</digi:trn>
					</c:set> <digi:link module="aim"
						href="/expresionBuilderMaganer.do?method=listExpresions"
						styleClass="comment" title="${translation}">
						<digi:trn key="aim:expresionbuildermanager">Expresion Builder Manager</digi:trn>
					</digi:link>&nbsp;&gt;&nbsp; <digi:trn key="aim:expresions:editexpresion">Edit Expresion</digi:trn>
					</span></td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td colspan="2">
						<span class=subtitle-blue> <digi:trn
							key="aim:expresions:editexpresion">Edit Expresion</digi:trn> 
						</span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span
						class=subtitle-blue> 
							<digi:errors /> &nbsp; <br />
					</td>
				</tr>

				<tr>
					<td noWrap width=100% vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0
							class="box-border-nopadding" width="100%">
	
							<tr bgColor=#f4f4f2>
								<td valign="top">
								<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0
									width="562" border=0>
									<tr>
										<td bgColor=#ffffff class=box-border width="560">
										<table border=1 cellPadding=1 cellSpacing=1 class="box-border" width="100%">
	
											<tr>
												<td width="30%" align="center" valign="top">												
													    <select size="3" style="width: 90%">
															<option value="1">ACTUAL</option>
															<option value="2">ACTUAL</option>
															<option value="3">ACTUAL</option>
														</select>
												</td>
												<td width="70%">
													<table border="1" width="100%">
														<tr>
															<td>
																Column Name
															</td>
														</tr>	
														<tr>
															<td>
																<input name="Test" style="width: 100%">
														
															</td>
														</tr>
														<tr>
															<td>
																Description
															</td>
														</tr>
														<tr>
															<td>
																<textarea rows="" cols="" style="width: 100%"></textarea>
															</td>
														</tr>
														<tr>
															<td>
																<textarea rows="" cols="" style="width: 100%;" name="expresion" id="expresion" readonly="readonly"></textarea>
																<button style="width: 10px" type="button" onclick="keyPress('1');">1</button>
																<button style="width: 10px" type="button" onclick="keyPress('2');">2</button>
																<button style="width: 10px" type="button" onclick="keyPress('3');">3</button>
																<button style="width: 10px" type="button" onclick="keyPress('4');">4</button>
																<button style="width: 10px" type="button" onclick="keyPress('5');">5</button>
																<button style="width: 10px" type="button" onclick="keyPress('6');">6</button>
																<button style="width: 10px" type="button" onclick="keyPress('7');">7</button>
																<button style="width: 10px" type="button" onclick="keyPress('8');">8</button>
																<button style="width: 10px" type="button" onclick="keyPress('9');">9</button>
																<button style="width: 10px" type="button" onclick="keyPress('.');">.</button>

																<button style="width: 10px" type="button" onclick="keyPress('+');">+</button>
																<button style="width: 10px" type="button" onclick="keyPress('-');">&ndash;</button>
																<button style="width: 10px" type="button" onclick="keyPress('=');">=</button>
																<button style="width: 10px" type="button" onclick="keyPress('*');">*</button>
																<button style="width: 10px" type="button" onclick="keyPress('/');">/</button>
																<button style="width: 10px" type="button" onclick="keyPress('(');">(</button>
																<button style="width: 10px" type="button" onclick="keyPress(')');">)</button>
																<button style="width: 10px" type="button" onclick="keyPress('>');">&gt;</button>
																<button style="width: 10px" type="button" onclick="keyPress('<');">&lt;</button>

																<button type="button">Test Expresion for Syntax</button>
															</td>
														</tr>
													</table>
												</td>
											</tr>

											<tr>
												<td width="100%" colspan="2" align="center">
													<button name="method" type="button" style="width: 60px;">Save</button>&nbsp;&nbsp;
													<button name="method" type="button" style="width: 60px;">Cancel</button>
												</td>
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
			</td>
		</tr>
	</table>

	<br />

</digi:form>





