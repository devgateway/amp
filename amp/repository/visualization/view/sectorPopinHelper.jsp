<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>


<li id="config_${item.mainEntity.id}"><input type="radio"
	id="config_${item.mainEntity.id}_radio" name="sector_config_check"
	title="${item.mainEntity.name}" value="${item.mainEntity.id}"
	onclick="uncheckAllRelatedEntities('sector_check');uncheckAllRelatedEntities('sub_sector_check');">
	<span><digi:trn>${item.mainEntity.name} configuration</digi:trn>
</span> <br />
	<ul style="list-style-type: none">
		<c:forEach items="${item.subordinateEntityList}" var="sectorHelper">
			<li><c:if
					test="${visualizationform.filter.dashboardType eq '3' }">
					<input type="radio" name="sector_check"
						title="${sectorHelper.mainEntity.name}"
						value="${sectorHelper.mainEntity.ampSectorId}"
						onClick="manageSectorEntities(this,${item.mainEntity.id},${sectorHelper.mainEntity.ampSectorId})">
				</c:if> <c:if test="${visualizationform.filter.dashboardType ne '3' }">
					<input type="checkbox" name="sector_check"
						title="${sectorHelper.mainEntity.name}"
						value="${sectorHelper.mainEntity.ampSectorId}"
						onClick="manageSectorEntities(this,${item.mainEntity.id},${sectorHelper.mainEntity.ampSectorId})" />
				</c:if> <span><digi:trn>${sectorHelper.mainEntity.name}</digi:trn>
			</span> <br />
				<ul style="list-style-type: none">
					<c:forEach items="${sectorHelper.subordinateEntityList}"
						var="subSector">
						<li><input type="checkbox"
							class="sub_sector_check_${sectorHelper.mainEntity.ampSectorId}"
							name="sub_sector_check" title="${subSector.name}"
							value="${subSector.ampSectorId}"
							onclick="manageSectorEntities(this,${item.mainEntity.id});" /><span><c:out value="${subSector.name}"/></span>
						</li>
					</c:forEach>
				</ul></li>
		</c:forEach>
	</ul></li>