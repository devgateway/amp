<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:context name="digiContext" property="context" />
<digi:instance property="gisIndicatorSectorRegionForm" />

<script language="JavaScript" type="text/javascript">
    function gotoPage(page){
    	document.gisIndicatorSectorRegionForm.selectedPage.value = page;
        document.gisIndicatorSectorRegionForm.target="_self";
        document.gisIndicatorSectorRegionForm.submit();            
    }

    function viewall(){
	    <digi:context name="viewIndicators" property="context/module/moduleinstance/indSectRegManager.do?actType=viewAll&view=all" />
	    document.gisIndicatorSectorRegionForm.action = "<%= viewIndicators %>";
	    document.gisIndicatorSectorRegionForm.submit();
	  
	}

	function resetSearch(){
		gisIndicatorSectorRegionForm.action="${contextPath}/widget/indSectRegManager.do?actType=resetSearch";
		gisIndicatorSectorRegionForm.target = "_self";
		gisIndicatorSectorRegionForm.submit();
		return true;
	}

	function searchAlpha(val) {
		 <digi:context name="searchIndSec" property="context/module/moduleinstance/indSectRegManager.do?actType=viewAll"/>
		 url = "<%= searchIndSec %>&alpha=" + val;
		 gisIndicatorSectorRegionForm.action = url;
		 gisIndicatorSectorRegionForm.submit();
		 return true;		
	}
	
</script>

<digi:form action="/indSectRegManager.do~actType=viewAll">
<html:hidden name="gisIndicatorSectorRegionForm" property="selectedPage"/>

<table width="80%" border="0" cellpadding="15">
	<tr>
		<td>
			<span class="crumb">
              <c:set var="translation">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
              </c:set>
              <html:link  href="/aim/admin.do" styleClass="comment" title="${translation}" >
                <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
              </html:link>&nbsp;&gt;&nbsp;
                <digi:trn key="admin:Navigation:ResultsDashboardDataManager">Results Dashboard Data Manager</digi:trn>
			</span>
		</td>
     <tr>
        <td><digi:errors/></td>
    </tr>	
	<tr>
		<td>
			<span class="subtitle-blue"><digi:trn key="gis:ResultsDashboardDataManager:pageHeader">Results Dashboard Data Manager</digi:trn></span>
		</td>
	</tr>
	<tr>
		<td>
			<a href="/widget/indSectRegManager.do~actType=create"><digi:trn key="gis:indicatorSectorRegionManager:createNew">Create new Indicator</digi:trn></a>
		</td>
	</tr>
	
	<tr>
		<td width="100%">
			<table>
				<tr>
					<td>
                    	<b><digi:trn>Sector</digi:trn>:</b>
                    </td>
                    <td>
                    	<html:select property="sectorId" styleClass="inp-text">
                        	<html:option value="-1">-<digi:trn>Select sector-</digi:trn></html:option>
							<c:if test="${!empty gisIndicatorSectorRegionForm.sectors}">
								<html:optionsCollection name="gisIndicatorSectorRegionForm" property="sectors"	value="ampSectorId" label="name" />						
							</c:if>
						</html:select>
                    </td>
                    <td>
                    	<b><digi:trn>Region</digi:trn>:</b>
                    </td>
                    <td>
                    	<html:select property="regionId" styleClass="inp-text">
                        	<html:option value="-1">-<digi:trn>Select Region-</digi:trn></html:option>
							<c:if test="${!empty gisIndicatorSectorRegionForm.regions}">
								<html:optionsCollection name="gisIndicatorSectorRegionForm" property="regions"	value="id" label="name" />						
							</c:if>
						</html:select>
                    </td>
					<td nowrap="nowrap">
			           <b><digi:trn key="aim:indsearchkey">Keyword</digi:trn>:</b>
			        </td>
			        <td>
			           <html:text property="keyWord" style="width:120px;font-family:verdana;font-size:11px;" />&nbsp;
			        </td>
			         <td align="left" width="10%" nowrap="nowrap"> 
			              	<digi:trn>Results</digi:trn>&nbsp;
							<html:select property="resultsPerPage" styleClass="inp-text">
								<html:option value="10">10</html:option>
								<html:option value="20">20</html:option>
								<html:option value="50">50</html:option>
								<html:option value="-1">ALL</html:option>
							</html:select>
			        </td>
			        <td>
				         <c:set var="trngo">
				         	<digi:trn key="aim:searchindbykey">Go</digi:trn>
				         </c:set>
				         <input type="submit" value="${trngo}" class="dr-menu"/>
			        </td>
			        <td>
			        	<c:set var="resetTrn">
			        		<digi:trn>reset</digi:trn>			        		
			        	</c:set>
			        	<input type="button" value="${resetTrn}" class="dr-menu" onclick="return resetSearch()">
			        </td>
			        <!-- 
			        	<td>
			        	<c:set var="trnviewall">
			            	<digi:trn key="aim:viewallind">View All</digi:trn>
			            </c:set>
			            <input type="submit" value="${trnviewall}" onclick="viewall();" class="dr-menu" />
			         </td>
			         -->			        
				</tr>
			</table>
		</td>		
    </tr>	
	
	<tr>
		<td>			
			<table border="0" width="100%" align="center" style="font-family:verdana;font-size:11px;">
				<tr bgColor="#c9c9c7">
					<td width="30%">
						<c:if test="${not empty gisIndicatorSectorRegionForm.sortBy && gisIndicatorSectorRegionForm.sortBy!='nameAscending'}">
							<digi:link href="/indSectRegManager.do~actType=viewAll&sortBy=nameAscending&reset=false">
								<strong><digi:trn>Indicator Name</digi:trn></strong>
							</digi:link>																					
						</c:if>
						<c:if test="${empty gisIndicatorSectorRegionForm.sortBy || gisIndicatorSectorRegionForm.sortBy=='nameAscending'}">
							<digi:link href="/indSectRegManager.do~actType=viewAll&sortBy=nameDescending&reset=false">
								<strong><digi:trn>Indicator Name</digi:trn></strong>
							</digi:link>																					
						</c:if>
						<c:if test="${empty gisIndicatorSectorRegionForm.sortBy || gisIndicatorSectorRegionForm.sortBy=='nameAscending'}"><img  src="/repository/aim/images/up.gif"/></c:if>
						<c:if test="${not empty gisIndicatorSectorRegionForm.sortBy && gisIndicatorSectorRegionForm.sortBy=='nameDescending'}"><img src="/repository/aim/images/down.gif"/></c:if>
					</td>
					<td width="30%">
						<c:if test="${not empty gisIndicatorSectorRegionForm.sortBy && gisIndicatorSectorRegionForm.sortBy!='sectNameAscending'}">
							<digi:link href="/indSectRegManager.do~actType=viewAll&sortBy=sectNameAscending&reset=false">
								<strong><digi:trn>Sector Name</digi:trn></strong>
							</digi:link>																					
						</c:if>
						<c:if test="${empty gisIndicatorSectorRegionForm.sortBy || gisIndicatorSectorRegionForm.sortBy=='sectNameAscending'}">
							<digi:link href="/indSectRegManager.do~actType=viewAll&sortBy=sectNameDescending&reset=false">
								<strong><digi:trn>Sector Name</digi:trn></strong>
							</digi:link>																					
						</c:if>
						<c:if test="${not empty gisIndicatorSectorRegionForm.sortBy && gisIndicatorSectorRegionForm.sortBy=='sectNameAscending'}"><img  src="/repository/aim/images/up.gif"/></c:if>
						<c:if test="${not empty gisIndicatorSectorRegionForm.sortBy && gisIndicatorSectorRegionForm.sortBy=='sectNameDescending'}"><img src="/repository/aim/images/down.gif"/></c:if>
					</td>
					<td width="30%">
						<c:if test="${not empty gisIndicatorSectorRegionForm.sortBy && gisIndicatorSectorRegionForm.sortBy!='regionNameAscending'}">
							<digi:link href="/indSectRegManager.do~actType=viewAll&sortBy=regionNameAscending&reset=false">
								<strong><digi:trn>Region Name</digi:trn></strong>
							</digi:link>																					
						</c:if>
						<c:if test="${empty gisIndicatorSectorRegionForm.sortBy || gisIndicatorSectorRegionForm.sortBy=='regionNameAscending'}">
							<digi:link href="/indSectRegManager.do~actType=viewAll&sortBy=regionNameDescending&reset=false">
								<strong><digi:trn>Region Name</digi:trn></strong>
							</digi:link>																					
						</c:if>
						<c:if test="${not empty gisIndicatorSectorRegionForm.sortBy && gisIndicatorSectorRegionForm.sortBy=='regionNameAscending'}"><img  src="/repository/aim/images/up.gif"/></c:if>
						<c:if test="${not empty gisIndicatorSectorRegionForm.sortBy && gisIndicatorSectorRegionForm.sortBy=='regionNameDescending'}"><img src="/repository/aim/images/down.gif"/></c:if>
					</td>
					<td>
						<strong><digi:trn>Sub Group</digi:trn></strong> 
					</td>
					<td>
						<strong><digi:trn>Operations</digi:trn></strong>
					</td>
				</tr>				
				<c:forEach var="indSecRegWithSubGrp" items="${gisIndicatorSectorRegionForm.indSectsWithSubGroups}" varStatus="stat">
					<c:set var="background">
						<c:if test="${stat.index%2==0}">#ffffff</c:if>
						<c:if test="${stat.index%2==1}">#d7eafd</c:if>
					</c:set>
					<tr bgcolor="${background}">
						<td nowrap="nowrap" width="30%">
							${indSecRegWithSubGrp.indSector.indicator.name}
						</td>
						<td nowrap="nowrap" width="30%">
						 	${indSecRegWithSubGrp.indSector.sector.name}
						</td>
                        <td nowrap="nowrap" width="20%">
                        	<c:choose>
                            	<c:when test="${empty indSecRegWithSubGrp.indSector.location.ampRegion&& not empty indSecRegWithSubGrp.indSector.location.dgCountry}">
                                	National
                                </c:when>
                                <c:otherwise>
                                	${indSecRegWithSubGrp.indSector.location.location.name}
                                </c:otherwise>
                            </c:choose>
						</td>
						<td nowrap="nowrap" width="10%">
							<c:if test="${not empty indSecRegWithSubGrp.subGroups}">
								<c:forEach var="subGrp" items="${indSecRegWithSubGrp.subGroups}">
									<div>${subGrp}</div> 
								</c:forEach>
							</c:if>
						</td>
						<td nowrap="nowrap">
							<a href="/widget/indSectRegManager.do~actType=edit~indSectId=${indSecRegWithSubGrp.indSector.id}">
								<digi:trn key="gis:editLink">Edit</digi:trn>
							</a>
                            &nbsp;|
                            <a href="/widget/indSectRegManager.do~actType=addEditValue~indSectId=${indSecRegWithSubGrp.indSector.id}">
								<digi:trn key="gis:addEditValueLink">Add/Edit Value</digi:trn>
							</a>
                                                        
							|&nbsp;
							<a href="/widget/indSectRegManager.do~actType=delete~indSectId=${indSecRegWithSubGrp.indSector.id}">
								<img border="0" src='<digi:file src="images/deleteIcon.gif"/>'>
							</a>
						</td>
					</tr>
				</c:forEach>
				<logic:notEmpty name="gisIndicatorSectorRegionForm" property="pages">
					<tr>
						<td colspan="4" nowrap="nowrap">
							<digi:trn>Pages :</digi:trn>
							<c:if test="${gisIndicatorSectorRegionForm.selectedPage > 1}">
								<jsp:useBean id="urlParamsFirst" type="java.util.Map" class="java.util.HashMap"/>
								<c:set target="${urlParamsFirst}" property="selectedPage" value="1"/>
								<c:set var="translation">
									<digi:trn>First Page</digi:trn>
								</c:set>
								<digi:link href="/indSectRegManager.do~actType=viewAll&reset=false"  style="text-decoration=none" name="urlParamsFirst" title="${translation}"  >
									&lt;&lt;
								</digi:link>
								<jsp:useBean id="urlParamsPrevious" type="java.util.Map" class="java.util.HashMap"/>
								<c:set target="${urlParamsPrevious}" property="selectedPage" value="${gisIndicatorSectorRegionForm.selectedPage -1}"/>
								<c:set var="translation">
									<digi:trn>Previous Page</digi:trn>
								</c:set>
								<digi:link href="/indSectRegManager.do~actType=viewAll&reset=false" name="urlParamsPrevious" style="text-decoration=none" title="${translation}" >
									&lt;
								</digi:link>
							</c:if>														
							<c:set var="start" value="${gisIndicatorSectorRegionForm.offset}"/>
							<logic:iterate name="gisIndicatorSectorRegionForm" property="pages" id="pages" type="java.lang.Integer" offset="${start}" length="5">	
								<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
								<c:set target="${urlParams1}" property="selectedPage"><%=pages%></c:set>																
								<c:if test="${gisIndicatorSectorRegionForm.selectedPage == pages}">
									<font color="#FF0000"><%=pages%></font>
								</c:if>
								<c:if test="${gisIndicatorSectorRegionForm.selectedPage != pages}">
									<c:set var="translation">
										<digi:trn>Click here to go to Next Page</digi:trn>
									</c:set>
									<digi:link href="/indSectRegManager.do~actType=viewAll&reset=false" name="urlParams1" title="${translation}" >
										<%=pages%>
									</digi:link>
								</c:if>
								|&nbsp;
							</logic:iterate>					
							<c:if test="${gisIndicatorSectorRegionForm.selectedPage != gisIndicatorSectorRegionForm.pagesSize}">
								<jsp:useBean id="urlParamsNext" type="java.util.Map" class="java.util.HashMap"/>
								<c:set target="${urlParamsNext}" property="selectedPage" value="${gisIndicatorSectorRegionForm.selectedPage+1}"/>																
								<c:set var="translation">
									<digi:trn>Next Page</digi:trn>
								</c:set>
								<digi:link href="/indSectRegManager.do~actType=viewAll&reset=false"  style="text-decoration=none" name="urlParamsNext" title="${translation}"  >
									&gt;
								</digi:link>
								<jsp:useBean id="urlParamsLast" type="java.util.Map" class="java.util.HashMap"/>																
								<c:set target="${urlParamsLast}" property="selectedPage" value="${gisIndicatorSectorRegionForm.pagesSize}"/>																																
								<c:set var="translation">
									<digi:trn key="aim:lastpage">Last Page</digi:trn>
								</c:set>
								<digi:link href="/indSectRegManager.do~actType=viewAll&reset=false"  style="text-decoration=none" name="urlParamsLast" title="${translation}">
									&gt;&gt; 
								</digi:link>
							</c:if>
							&nbsp;
							<c:out value="${gisIndicatorSectorRegionForm.selectedPage}"></c:out>&nbsp;<digi:trn key="aim:of">of</digi:trn>&nbsp;<c:out value="${gisIndicatorSectorRegionForm.pagesSize}"></c:out>
						</td>
					</tr>
				</logic:notEmpty>
				
				
				<logic:notEmpty name="gisIndicatorSectorRegionForm" property="alphaPages">
					<tr>
						<td align="center" colspan="4">
							<table width="90%">
								<tr>
								    <td>
									    <c:if test="${not empty gisIndicatorSectorRegionForm.currentAlpha}">
									    	<c:if test="${gisIndicatorSectorRegionForm.currentAlpha!='viewAll'}">
										    	<c:if test="${gisIndicatorSectorRegionForm.currentAlpha!=''}">														    	
											    	<c:set var="trnViewAllLink">
														<digi:trn>Click here to view all search pages</digi:trn>
													</c:set>
													<a href="javascript:searchAlpha('viewAll')" title="${trnViewAllLink}"><digi:trn key="aim:viewAllLink">viewAll</digi:trn></a>
												</c:if>
											</c:if>
										</c:if>
										<logic:iterate name="gisIndicatorSectorRegionForm"  property="alphaPages" id="alphaPages" type="java.lang.String" >
											<c:if test="${alphaPages != null}">
												<c:if test="${gisIndicatorSectorRegionForm.currentAlpha == alphaPages}">
													<font color="#FF0000"><%=alphaPages %></font>
												</c:if>
												<c:if test="${gisIndicatorSectorRegionForm.currentAlpha != alphaPages}">
													<c:set var="translation">
														<digi:trn>Click here to go to next page</digi:trn>
													</c:set>
													<a href="javascript:searchAlpha('<%=alphaPages%>')" title="${translation}" ><%=alphaPages %></a>
												</c:if>
												|&nbsp;
											</c:if>
										</logic:iterate>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</logic:notEmpty>
				
				<logic:notEmpty name="gisIndicatorSectorRegionForm" property="alphaPages">
					<tr>
						<td bgColor="#f4f4f2" colspan="4">
							<c:if test="${not empty gisIndicatorSectorRegionForm.currentAlpha}">
								<c:if test="${gisIndicatorSectorRegionForm.currentAlpha!='viewAll'}">
							   		<c:if test="${gisIndicatorSectorRegionForm.currentAlpha!=''}">														    	
						    			<digi:trn>Click on viewAll to see all existing organizations.</digi:trn>
									</c:if>
								</c:if>
							</c:if>										
						</td>
					</tr>
				</logic:notEmpty>
				                               
			</table>
		</td>
	</tr>
</table>
</digi:form>
