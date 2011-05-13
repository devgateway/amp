<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

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
	
</script>
<digi:instance property="gisIndicatorSectorRegionForm" />
<digi:form action="/indSectRegManager.do~actType=viewAll">
<html:hidden name="gisIndicatorSectorRegionForm" property="selectedPage"/>


<table width="60%" border="0" cellpadding="15">
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
                    <td>
                        <digi:errors/>
                    </td>
                </tr>
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
					<td nowrap="nowrap">
			           <b><digi:trn key="aim:indsearchkey">Keyword</digi:trn>:</b>
			        </td>
			        <td>
			           <html:text property="keyWord" style="width:120px;font-family:verdana;font-size:11px;" />&nbsp;
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
			</table>
		</td>		
    </tr>	
	
	<tr>
		<td>
			
			<table border="0" width="100%" align="center" style="font-family:verdana;font-size:11px;">
				<tr bgColor="#d7eafd">
					<td nowrap="nowrap" width="30%">
						<strong><digi:trn key="gis:indicatorSectorRegionManager:indicatorName">Indicator Name</digi:trn></strong>
					</td>
					<td nowrap="nowrap" width="30%">
						<strong><digi:trn key="gis:indicatorSectorRegionManager:sectorName">Sector Name</digi:trn></strong>
					</td>
					<td  nowrap="nowrap" width="30%">
						<strong><digi:trn key="gis:indicatorSectorRegionManager:regionName">Region Name</digi:trn></strong>
					</td>
                                        <td>
						<strong><digi:trn key="gis:indicatorSectorRegionManager:operations">Operations</digi:trn></strong>
					</td>
				</tr>
				<c:forEach var="indSecReg" items="${gisIndicatorSectorRegionForm.indSectList}" varStatus="stat">
					<tr>
						<td nowrap="nowrap">
							${indSecReg.indicator.name}
						</td>
						<td nowrap="nowrap">
						 	${indSecReg.sector.name}
						</td>
                                                <td nowrap="nowrap">
                                                    <c:choose>
                                                        <c:when test="${empty indSecReg.location.ampRegion&& not empty indSecReg.location.dgCountry}">
                                                            National
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${indSecReg.location.location.name}
                                                        </c:otherwise>
                                                    </c:choose>
                                               
						 	
						</td>
                                                
						<td nowrap="nowrap">
							<a href="/widget/indSectRegManager.do~actType=edit~indSectId=${indSecReg.id}">
								<digi:trn key="gis:editLink">Edit</digi:trn>
							</a>
                                                        &nbsp;|
                                                        <a href="/widget/indSectRegManager.do~actType=addEditValue~indSectId=${indSecReg.id}">
								<digi:trn key="gis:addEditValueLink">Add/Edit Value</digi:trn>
							</a>
                                                        
							|&nbsp;
							<a href="/widget/indSectRegManager.do~actType=delete~indSectId=${indSecReg.id}">
								<img border="0" src='<digi:file src="images/deleteIcon.gif"/>'>
							</a>
						</td>
					</tr>
				</c:forEach>
                                <!-- pagination -->
                                <tr>
                                    <td colspan="4">
                                        <digi:trn key="gis:Pages">Pages</digi:trn>:
                                           <c:choose>
                                                <c:when test="${gisIndicatorSectorRegionForm.selectedPage=='1'}">
                                                      <<&nbsp;<
                                                </c:when>
                                                <c:otherwise>
                                                     <a  href="javascript:gotoPage(1)"> << </a>
                                                     <a  href="javascript:gotoPage(${gisIndicatorSectorRegionForm.selectedPage-1})"> < </a>
                                                 </c:otherwise>
                                            </c:choose>|
                                            <c:set var="start">
                                                <c:choose>
                                                    <c:when test="${gisIndicatorSectorRegionForm.selectedPage<=5}">
                                                        1
                                                    </c:when>
                                                     <c:when test="${gisIndicatorSectorRegionForm.selectedPage+2>=gisIndicatorSectorRegionForm.pages}">
                                                        ${gisIndicatorSectorRegionForm.pages-4}
                                                    </c:when>
                                                    <c:otherwise>
                                                    ${gisIndicatorSectorRegionForm.selectedPage-2}
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:set>
                                            
                                              <c:set var="end">
                                                <c:choose>
                                                    <c:when test="${gisIndicatorSectorRegionForm.selectedPage<=5&&gisIndicatorSectorRegionForm.pages>=5}">
                                                        5
                                                    </c:when>
                                                     <c:when test="${gisIndicatorSectorRegionForm.pages<5}">
                                                        ${gisIndicatorSectorRegionForm.pages}
                                                    </c:when>
                                                    <c:when test="${gisIndicatorSectorRegionForm.selectedPage+2<=gisIndicatorSectorRegionForm.pages}">
                                                        ${gisIndicatorSectorRegionForm.selectedPage+2}
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${gisIndicatorSectorRegionForm.pages}
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:set>
                                      
                                        <c:forEach  var="page"  begin="${start}"  end="${end}">
                                            <c:choose>
                                               
                                                <c:when test="${page==gisIndicatorSectorRegionForm.selectedPage}">
                                                      ${page}|
                                                </c:when>
                                                <c:otherwise>
                                                    <a  href="javascript:gotoPage(${page})">${page}</a>|
                                                 </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                                <c:choose>     
                                                <c:when test="${gisIndicatorSectorRegionForm.selectedPage==gisIndicatorSectorRegionForm.pages}">
                                                      >&nbsp;>>
                                                </c:when>
                                                <c:otherwise>
                                                      <a  href="javascript:gotoPage(${gisIndicatorSectorRegionForm.selectedPage+1})"> > </a>
                                                      <a  href="javascript:gotoPage(${gisIndicatorSectorRegionForm.pages})"> >> </a>
                                                 </c:otherwise>
                                             </c:choose> &nbsp;${gisIndicatorSectorRegionForm.selectedPage}&nbsp;<digi:trn key="gis:pagination:of">of</digi:trn>&nbsp;${gisIndicatorSectorRegionForm.pages}
                                    </td>
                                </tr>
                                    <!-- end of pagination -->
			</table>


		</td>
	</tr>
</table>

</digi:form>
