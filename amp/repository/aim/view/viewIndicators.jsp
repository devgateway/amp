<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>



<jsp:include page="teamPagesHeader.jsp"  />
<style type="text/css">

a.itr:hover {
	border-width: 0; /* IE */
}

.itr {
	position: relative;
	text-decoration: none;
	
}

.itr:hover .bpop 
{
	display: block;
	position: absolute;
	width: 100px;
	background-color: white;
	padding: 3px 5px 4px 5px;
	border: 1px Silver solid;
	
	left: 8em;
	top: 0.6em;
}
.itr {
	display: inline-block;
	top: 0.15em;
	line-height: 1.05;
}
.bpop {
	display: none;
}
</style>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="javascript">




   function setOverImg(index){
	  document.getElementById("img"+index).src="/TEMPLATE/ampTemplate/module/aim/images/tab-righthover1.gif"
	}
	
   function setOutImg(index){
	  document.getElementById("img"+index).src="/TEMPLATE/ampTemplate/module/aim/images/tab-rightselected1.gif"
	}
	
	function sortByVal(value){
	  if(value!=null){
	    <digi:context name="viewIndicators" property="context/module/moduleinstance/viewIndicators.do" />
	    document.getElementById("sortBy").value=value;
	    document.aimViewIndicatorsForm.target = "_self";
	    document.aimViewIndicatorsForm.submit();
	  }
	}
	
	function addIndicator(){
	  <digi:context name="addIndicator" property="context/module/moduleinstance/addNewIndicator.do?indicator=new" />
	  openURLinWindow("<%= addIndicator %>",500, 300);
	}
	
	function editIndicator(id){
	  <digi:context name="viewEditIndicator" property="context/module/moduleinstance/viewEditIndicator.do" />
	  openURLinWindow("<%= viewEditIndicator %>?id="+id,500, 300);
	}
	
	function deletePrgIndicator(){  
		<c:set var="translation"> 
			<digi:trn key="aim:doYouWantToDelIndicatorCheckProgramFirst">Do you want to delete the Indicator ? Please check whether the indicator is being used by some Program.</digi:trn>
		</c:set>
					return confirm("${translation}");
			}
			
	
	//if yu remove or rename this function please look in addNewIndicator.jsp
	function viewall(){
	    <digi:context name="viewIndicators" property="context/module/moduleinstance/viewIndicators.do?sector=viewall" />
	    document.aimViewIndicatorsForm.action = "<%= viewIndicators %>";
	    document.aimViewIndicatorsForm.target = "_self";
	    document.aimViewIndicatorsForm.submit();
	  
	}
	function filterIndicators(){
		  <digi:context name="viewIndicators" property="context/module/moduleinstance/viewIndicators.do" />
		 document.aimViewIndicatorsForm.action = "<%= viewIndicators %>";
		 document.aimViewIndicatorsForm.target = "_self";
		 document.aimViewIndicatorsForm.submit();
	}
	function exportXSL(){
        <digi:context name="exportUrl" property="context/module/moduleinstance/exportIndicatorManager2XSL.do"/>;
    document.aimViewIndicatorsForm.action="${exportUrl}";
    document.aimViewIndicatorsForm.target="_blank";
    document.aimViewIndicatorsForm.submit();
}
</script>
<h1 class="admintitle"><digi:trn>Indicator manager</digi:trn></h1>
<digi:instance property="aimViewIndicatorsForm" />

<digi:form action="/viewIndicators.do" method="post">
	<div id="viewIndicatorsContainer">

		<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="800"
			border="0">
			<tr>
				<td class=r-dotted-lg width=14>&nbsp;</td>
				<td align=left class=r-dotted-lg valign="top" width="800">
					<table cellPadding=5 cellspacing="0" width="100%" border="0">
						<!--<tr>
            <td height=33><span class=crumb>
              <c:set var="translation">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
              </c:set>
              <digi:link href="/admin.do" styleClass="comment" title="${translation}" >
                <digi:trn key="aim:AmpAdminHome">
                Admin Home
                </digi:trn>
              </digi:link>&nbsp;&gt;&nbsp;
              <digi:trn key="aim:nIndicatorManager">
              Indicator Manager
              </digi:trn>
            </td>
          </tr>-->
						<%-- End navigation --%>
						
						<tr>
							<td vAlign="center" width="100%"><jsp:include
									page="/repository/aim/view/adminXSLExportToolbar.jsp" /></td>
						</tr>
						<tr>
							<td height=16 vAlign="center" width="100%"><span
								style="font-family: Tahoma; font-size: 11px;"><digi:errors />
							</span> <c:if test="${!empty aimViewIndicatorsForm.themeName}">
                This indicator assigned to <span style="color: Red;">${aimViewIndicatorsForm.themeName}</span>
									<c:if test="${aimViewIndicatorsForm.flag == 'project'}">
                 Activity
                </c:if>
									<c:if test="${aimViewIndicatorsForm.flag != 'project'}">
                 Program
                </c:if>
								</c:if></td>
						</tr>
						<tr>
							<td noWrap width="100%" vAlign="top">
								<table width="100%" cellspacing="0" cellspacing="0" border="0">
									<tr>
										<td noWrap width="100%" vAlign="top">
											<table bgColor=#d7eafd cellpadding="0" cellspacing="0"
												width="100%" valign="top">
												<tr bgColor=#ffffff>
													<td vAlign="top" width="100%"><html:hidden
															property="sortBy" styleId="sortBy" />
														<table width="100%" cellspacing="0" cellpadding="0"
															valign="top" align="left" border="0">
															<tr>
																<td>
																	<table style="font-family: verdana; font-size: 11px;"
																		border="0" width="100%">
																		<tr>
																			<td><b><digi:trn key="aim:indsector">Sector</digi:trn>:</b>
																			</td>
																			<td id="sectorSelectContainer"><html:select
																					property="sectorId" styleClass="inp-text">
																					<html:option value="-1">-<digi:trn
																							key="aim:selsector">Select sector-</digi:trn>
																					</html:option>
																					<c:if
																						test="${!empty aimViewIndicatorsForm.sectors}">
																						<html:optionsCollection
																							name="aimViewIndicatorsForm" property="sectors"
																							value="ampSectorId" label="name" />
																					</c:if>
																				</html:select></td>
																			<td nowrap="nowrap"><b><digi:trn
																						key="aim:indsearchkey">Keyword</digi:trn>:</b></td>
																			<td id="sectorKeywordContainer"><html:text
																					property="keyword"
																					style="width:120px;font-family:verdana;font-size:11px;" />
																			</td>
																			<td><c:set var="trngo">
																					<digi:trn key="aim:searchindbykey">Go</digi:trn>
																				</c:set> <input class="buttonx" type="submit"
																				value="${trngo}" class="dr-menu" onclick="filterIndicators();"/></td>
																			<td><c:set var="trnviewall">
																					<digi:trn key="aim:viewallind">View All</digi:trn>
																				</c:set> <input class="buttonx" type="submit"
																				value="${trnviewall}" onclick="viewall();"
																				class="dr-menu" /></td>
																		</tr>
																		<tr>
																			<td colspan="6" width="100%" align="center"
																				class="report">
																				<table cellpadding="0" cellspacing="0" width="100%"
																					class="inside" id="dataTable">
																					<thead>
																						<tr>
																							<td width="80%" class="inside" bgcolor="#c7d4db">
																								<c:if
																									test="${empty aimViewIndicatorsForm.sortBy || aimViewIndicatorsForm.sortBy=='nameAsc'}">
																									<a href="javascript:sortByVal('nameDesc')">
																										<b><digi:trn>Indicator Name</digi:trn>
																									</b> <img src="/repository/aim/images/up.gif"
																										border="0" /> </a>
																								</c:if> <c:if
																									test="${not empty aimViewIndicatorsForm.sortBy && aimViewIndicatorsForm.sortBy!='nameAsc'}">
																									<a href="javascript:sortByVal('nameAsc')">
																										<b><digi:trn key="aim:indicator">Indicator Name
                                                </digi:trn>
																									</b>
																									<c:if
																											test="${aimViewIndicatorsForm.sortBy=='nameDesc'}">
																											<img src="/repository/aim/images/down.gif"
																												border="0" />
																										</c:if> </a>
																								</c:if></td>
																							<td width="18%" class="inside" bgcolor="#c7d4db">
																								<c:if
																									test="${aimViewIndicatorsForm.sortBy=='sectAsc'}">
																									<a href="javascript:sortByVal('sectDesc')">
																										<b><digi:trn>Sector</digi:trn>
																									</b><img src="/repository/aim/images/up.gif"
																										border="0" /> </a>
																								</c:if> <c:if
																									test="${aimViewIndicatorsForm.sortBy!='sectAsc'}">
																									<a href="javascript:sortByVal('sectAsc')">
																										<b><digi:trn>Sector</digi:trn>
																									</b> <c:if
																											test="${aimViewIndicatorsForm.sortBy=='sectDesc'}">
																											<img src="/repository/aim/images/down.gif"
																												border="0" />
																										</c:if> </a>
																								</c:if></td>
																							<td class="inside ignore" bgcolor="#c7d4db">&nbsp;

																							</td>
																						</tr>
																					</thead>
																					<c:if
																						test="${!empty aimViewIndicatorsForm.allIndicators}">
																						<tbody class="yui-dt-data">
																							<c:forEach var="indItr"
																								items="${aimViewIndicatorsForm.allIndicators}">
																								<tr>
																									<td width="80%" class="inside"><a
																										class="itr"
																										href="javascript:editIndicator('${indItr.id}');">
																											<c:out value="${indItr.name}"/></a></td>
																									<td width="18%" class="inside" nowrap="nowrap">
																										<c:if test="${!empty indItr.sectorNames}">
																											<c:forEach var="indsectname"
																												items="${indItr.sectorNames}">
                                                                                                                                                                                                        <c:out value="${indsectname}"/><br>
																											</c:forEach>
																										</c:if></td>
																									<td align="right" width="2%"
																										class="inside ignore"><jsp:useBean
																											id="urlParams" type="java.util.Map"
																											class="java.util.HashMap" /> <c:set
																											target="${urlParams}" property="indicatorId">
																${indItr.id}
														</c:set> <c:set var="translation">
																											<digi:trn key="aim:clickToDeleteIndicator">
																 Click here to Delete Indicator
														</digi:trn>
																										</c:set> <digi:link href="/removeIndicator.do"
																											name="urlParams" title="${translation}"
																											onclick="return deletePrgIndicator()">
																											<img src="../ampTemplate/images/trash_12.gif"
																												border="0">
																										</digi:link></td>
																								</tr>
																							</c:forEach>
																						</tbody>
																					</c:if>

																				</table></td>
																		</tr>
																		<tr>
																			<td colspan="6" align="center"><field:display
																					name="Add New Indicator" feature="Admin">
																					<input type="button"
																						value="<digi:trn key='btn:addIndicator'>Add Indicators</digi:trn>"
																						id="addBtn" onclick="addIndicator();"
																						class="buttonx" />
																				</field:display></td>
																		</tr>
																	</table></td>
															</tr>
														</table> </td>
												</tr>
											</table></td>
									</tr>
								</table></td>
						</tr>
					</table></td>
			</tr>
		</table>

	</div>
	</digi:form>