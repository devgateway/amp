<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>


<li id="config_${item.value.ampThemeId}"><input type="checkbox"
	id="config_${item.value.ampThemeId}_radio" name="${item.key}"
	title="${item.value.name}" value="${item.value.ampThemeId}"
	onclick="toggleRelatedLike(this,'${item.key}_level1');toggleRelatedLike(this,'${item.key}_level2');">
	<span>${item.value.name} </span> <br />
	<ul style="list-style-type: none">
		<c:forEach items="${item.value.siblings}" var="children">
			<li>
				<input type="checkbox" name="${item.key}" class="${item.key}_level1"
						title="${children.name}"
						value="${children.ampThemeId}"
						onclick="toggleRelatedLike(this,'${item.key}_level2_${children.ampThemeId}');" />
				<span>${children.name}</span><br />
				<ul style="list-style-type: none">
				<c:forEach items="${children.siblings}" var="subChildren">
								<li><input type="checkbox"
							class="${item.key}_level2_${children.ampThemeId}"
							name="${item.key}" title="${subChildren.name}"
							value="${subChildren.ampThemeId}"/><span><c:out
									value="${subChildren.name}" />
						</span></li>
					</c:forEach>
				</ul>
					
			</li>
		</c:forEach>
	</ul>
</li>