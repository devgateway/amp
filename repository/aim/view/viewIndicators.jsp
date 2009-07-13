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



<jsp:include page="teamPagesHeader.jsp" flush="true" />

<jsp:include page="viewIndicatorsPopin.jsp" flush="true" />

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
	    document.aimViewIndicatorsForm.submit();
	  }
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
	    document.aimViewIndicatorsForm.submit();
	  
	}

	function setStripsTable(tableId, classOdd, classEven) {
		var tableElement = document.getElementById(tableId);
		rows = tableElement.getElementsByTagName('tr');
		for(var i = 0, n = rows.length; i < n; ++i) {
			if(i%2 == 0)
				rows[i].className = classEven;
			else
				rows[i].className = classOdd;
		}
		rows = null;
	}
	
	function setHoveredTable(tableId, hasHeaders) {

		var tableElement = document.getElementById(tableId);
		if(tableElement){
	    	var className = 'Hovered',
	        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
	        rows      = tableElement.getElementsByTagName('tr');

			for(var i = 0, n = rows.length; i < n; ++i) {
				rows[i].onmouseover = function() {
					this.className += ' ' + className;
				};
				rows[i].onmouseout = function() {
					this.className = this.className.replace(pattern, ' ');

				};
			}
			rows = null;
		}
	}
	
</script>

<style type="text/css">
		.jcol{												
		padding-left:10px;												 
		}
		.jlien{
			text-decoration:none;
		}
		.tableEven {
			background-color:#dbe5f1;
			font-size:8pt;
			padding:2px;
		}

		.tableOdd {
			background-color:#FFFFFF;
			font-size:8pt;
			padding:2px;
		}
		 
		.Hovered {
			background-color:#a5bcf2;
		}
		
		.notHovered {
			background-color:#FFFFFF;
		}
		
		
</style>

<digi:instance property="aimViewIndicatorsForm" />

<digi:form action="/viewIndicators.do" method="post">
  <html:hidden property="sortBy" styleId="sortBy"/>
  <table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=800 border=0>
    <tr>
      <td align=left vAlign=top width=800>
        <table cellPadding=5 cellSpacing=0 width="100%" border=0>
          <tr>
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
          </tr><%-- End navigation --%>
          <tr>
            <td height="16" vAlign="center" width="100%">
              <span class="subtitle-blue">
                <digi:trn key="aim:nIndicatorManager">
                Indicator Manager
                </digi:trn>
              </span>
            </td>
          </tr>
          <tr>
            <td height=16 vAlign="center" width="100%">
              <digi:errors />
              <c:if test="${!empty aimViewIndicatorsForm.themeName}">
                This indicator assigned to <span style="color:Red;">${aimViewIndicatorsForm.themeName}</span> 
                <c:if test="${aimViewIndicatorsForm.flag == 'project'}">
                 Activity
                </c:if>
                <c:if test="${aimViewIndicatorsForm.flag != 'project'}">
                 Program
                </c:if>
              </c:if>
            </td>
          </tr>
          <tr>
            <td noWrap width=100% vAlign="top">
              <table width="100%" cellspacing=0 cellSpacing=0 border=0>
                <tr>
                  <td noWrap width=100% vAlign="top">
                    <table bgColor=#d7eafd cellPadding=0 cellSpacing=0 width="100%" valign="top">
                      <tr bgColor=#ffffff>
                        <td vAlign="top" width="100%">
                          <table width="100%" cellspacing=0 cellpadding=0 valign="top" align="left" border="0">
                            <tr>
                              <td>
                                <table style="font-family:verdana;font-size:11px;" border="0" width="100%">
                                  <tr>
                                    <td>
                                      <digi:trn key="aim:indsector">Sector</digi:trn>:
                                    </td>
                                    <td>
                                      <html:select property="sectorId" styleClass="inp-text" onchange="document.aimViewIndicatorsForm.submit()">
                                    	<html:option value="-1">-<digi:trn key="aim:selsector">Select sector-</digi:trn></html:option>
										<c:if test="${!empty aimViewIndicatorsForm.sectors}">
												<html:optionsCollection name="aimViewIndicatorsForm" property="sectors" 
											value="ampSectorId" label="name" />						
										</c:if>
									 </html:select>
                                    </td>
                                	<td nowrap="nowrap">
                                      <digi:trn key="aim:indsearchkey">Keyword</digi:trn>:
                                    </td>
                                    <td>
                                      <html:text property="keyword" style="width:120px;font-family:verdana;font-size:11px;" />
                                    </td>
                                    <td>
                                    <c:set var="trngo">
                    					  <digi:trn key="aim:searchindbykey">Go</digi:trn>
                    				 </c:set>
                                      <input type="submit" value="${trngo}" class="dr-menu"/>
                                    </td>
                                    <td>
                                     <c:set var="trnviewall">
                    					  <digi:trn key="aim:viewallind">View All</digi:trn>
                    				 </c:set>
                                      <input type="submit" value="${trnviewall}" onclick="viewall();" class="dr-menu" />
                                    </td>
                                  </tr>
                                  <tr>
                                    <td colspan="6" width="100%" align="center">
                                      <table width="100%" align="center"  border="0" style="font-family:verdana;font-size:11px;">
                                        <tr>
											<td>
												<table width="100%" BORDER=0 cellpadding="2" cellspacing="0">
													<tr height="30" style="background-color:#999999; color:#000000;">
			                                          	<td width="60%">
			                                            	<a href="javascript:sortByVal('0')" style="background-color:#999999;color:#000000;">
			                                                	<b><digi:trn key="aim:indicator">Indicator Name
			                                                	</digi:trn></b> <img alt="" src="../ampTemplate/images/arrow_up_down.gif" border="0" height="10" />
			                                             	</a>
			                                          	</td>
			                                          	<td width="33%" align="left">
			                                        		<a href="javascript:sortByVal('1')" style="background-color:#999999;color:#000000;">
			                                               		<b><digi:trn key="aim:indsector">Sector
			                                                	</digi:trn></b> <img alt="" src="../ampTemplate/images/arrow_up_down.gif" border="0" height="10" />
			                                              	</a>
			                                            </td>
				                                        <td width="7%">
				                                          	&nbsp;
				                                        </td>
			                                        </tr>
												</table>
											</td>	
										</tr>
										<tr>
											<td>
												<div  style="overflow:auto;width:100%;height:220px;max-height:220px;"  >
													<table width="100%" BORDER=0 cellpadding="2" cellspacing="0" id="dataTable">
				                                        <c:if test="${!empty aimViewIndicatorsForm.allIndicators}">
				                                         	<c:forEach var="indItr" items="${aimViewIndicatorsForm.allIndicators}">
				                                            	<tr height="25">
						                                            <td width="60%">
							                                            <a class="itr" href="javascript:editIndicator('${indItr.id}');">${indItr.name}</a>
						                                            </td>
					                                               	<td width="33%" nowrap="nowrap">
						                                                <c:if test="${!empty indItr.sectorNames}">
						                                            	<c:forEach var="indsectname" items="${indItr.sectorNames}">
						                                            	   ${indsectname}<br>
						                                            	</c:forEach>
						                                            	</c:if>
						                                         	</td>
				                                              		<td align="center" width="7%">
				                                              			<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																		<c:set target="${urlParams}" property="indicatorId">
																				${indItr.id}
																		</c:set>
																		<c:set var="translation">
																			<digi:trn key="aim:clickToDeleteIndicator">Click here to Delete Indicator</digi:trn>
																		</c:set>
																		<digi:link href="/viewIndicators.do~indicator=delete${tIndType}" name="urlParams" title="${translation}" onclick="return deletePrgIndicator()">
																			<img src= "../ampTemplate/images/trash_12.gif" border="0">
																		</digi:link>
																	</td>
								                           		</tr>
				                                          	</c:forEach>
				                                        </c:if>
													</table>
												</div>
											</td>
										</tr>
										<tr height="10">
										</tr>
                                        <tr>
                                          <td colspan="6" align="left">
                                          	<field:display name="Add New Indicator" feature="Admin">
                                            	<input type="button" value="<digi:trn key='btn:addIndicator'>Add Indicators</digi:trn>" id="addBtn" onclick="addIndicator();" style="font-family:verdana;font-size:11px;"/>
                                            </field:display>
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
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
<script language="javascript">
	setStripsTable("dataTable", "tableEven", "tableOdd");
	setHoveredTable("dataTable", false);
</script>
</digi:form>
