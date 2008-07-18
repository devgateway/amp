<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<digi:instance property="gisIndicatorSectorRegionForm" />
<digi:form action="/indSectRegManager.do">


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
                <digi:trn key="admin:Navigation:indicatorSectorRegionManager">Indicator Sector Region Manager</digi:trn>
			</span>
		</td>
	</tr>
	<tr>
		<td>
			<span class="subtitle-blue"><digi:trn key="gis:indicatorSectorRegionManager:pageHeader">Indicator Sector Region Manager</digi:trn></span>
		</td>
	</tr>
	<tr>
		<td>
			<a href="/gis/indSectRegManager.do~actType=create"><digi:trn key="gis:indicatorSectorRegionManager:createNew">Create new Indicator</digi:trn></a>
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
						 	${indSecReg.location.ampRegion.name}
						</td>
                                                
						<td nowrap="nowrap">
							<a href="/gis/indSectRegManager.do~actType=edit~indSectId=${indSecReg.id}">
								<digi:trn key="gis:editLink">Edit</digi:trn>
							</a>
                                                        &nbsp;|
                                                        <a href="/gis/indSectRegManager.do~actType=addEditValue~indSectId=${indSecReg.id}">
								<digi:trn key="gis:addEditValueLink">Add/Edit Value</digi:trn>
							</a>
                                                        
							|&nbsp;
							<a href="/gis/indSectRegManager.do~actType=delete~indSectId=${indSecReg.id}">
								<img border="0" src='<digi:file src="images/deleteIcon.gif"/>'>
							</a>
						</td>
					</tr>
				</c:forEach>
			</table>


		</td>
	</tr>
</table>

</digi:form>
