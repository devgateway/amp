<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>


<li id="config_${item.mainEntity.id}"><input type="radio"
	id="config_${item.mainEntity.id}_radio" name="sector_config_check"
	title="${item.mainEntity.name}" value="${item.mainEntity.id}"
	onclick="uncheckAllRelatedEntities('sector_check');uncheckAllRelatedEntities('sub_sector_check');">
	<span>${item.mainEntity.classification.secSchemeName} </span> <br />
	<ul style="list-style-type: none">
		<c:forEach items="${item.subordinateEntityList}" var="sectorHelper">
			<li>
				<input type="checkbox" name="sector_check"
						id="sector_check_${sectorHelper.mainEntity.ampSectorId}"
						title="${sectorHelper.mainEntity.name}"
						value="${sectorHelper.mainEntity.ampSectorId}"
						onClick="manageSectorEntities(this,${item.mainEntity.id},${sectorHelper.mainEntity.ampSectorId})" />
				<span>${sectorHelper.mainEntity.name}</span><br />
				<ul style="list-style-type: none">
					<c:forEach items="${sectorHelper.subordinateEntityList}"
						var="subSector">
						<li><input type="checkbox"
							id="sub_sector_check_${subSector.ampSectorId}"
							class="sub_sector_check_${sectorHelper.mainEntity.ampSectorId}"
							name="sub_sector_check" title="${subSector.name}"
							value="${subSector.ampSectorId}"
							onclick="manageSectorEntities(this,${item.mainEntity.id});checkParentOption('sector_check',${sectorHelper.mainEntity.ampSectorId})" /><span><c:out
									value="${subSector.name}" />
						</span></li>
					</c:forEach>
				</ul>
			</li>
		</c:forEach>
	</ul>
</li>