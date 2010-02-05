<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
	
<bean:define id="elements" toScope="page" scope="request" name="reqElements" />
<bean:define id="propertyObj" toScope="page" scope="request" name="reqPropertyObj" />
<bean:define id="searchManagerId" toScope="page" scope="request" name="reqSearchManagerId" />
<bean:define id="searchFieldWidth" toScope="page" value="150px"/>
<logic:notEmpty scope="request" name="reqSearchFieldWidth">
	<bean:define id="searchFieldWidth" toScope="page">${reqSearchFieldWidth}</bean:define>
</logic:notEmpty>	
	
	<div class="grouping_selector_wrapper" style="float: left; width: 40%; padding: 0px; height: 98%">
		<div style="background: #4C6185; margin:0px; color: white; font-weight: bold; font-size: small; padding:2px; height: 7%;
			border-left: 1px solid black; border-bottom: 1px solid black;border-top: 1px solid black;">
			&nbsp;<digi:trn>Grouping Selector</digi:trn>
		</div>
		<div style="border: 1px solid #b3c5d4; height: 93%; width: 100%; background: white;">		
					<table style="width: 100%; " >
						<logic:iterate id="element" name="elements" scope="page">
							<tr style="cursor: pointer;"
								onclick="getRowSelectorInstance(this, ${propertyObj}, new DivManager('${element.htmlDivId}', ${propertyObj}), true).toggleRow()" 
								onMouseover="getRowSelectorInstance(this, ${propertyObj}, new DivManager('${element.htmlDivId}', ${propertyObj}), true).markRow(false)" 
								onMouseout="getRowSelectorInstance(this, ${propertyObj}, new DivManager('${element.htmlDivId}', ${propertyObj}), true).unmarkRow(false)">
								<td style="font-family: Arial; font-size: 12px;  padding: 4px;" width="90%"><digi:trn>${element.name}</digi:trn></td>
								<td style="font-family: Arial; font-size: 12px;  padding: 4px;">(${element.rootHierarchyListable.countDescendants})
									<button type="button"
									onclick="getRowSelectorInstance(this.parentNode, ${propertyObj}, new DivManager('${element.htmlDivId}', ${propertyObj}), true).toggleRow()" 
									style="display: none;">Fake</button>
								</td>
							</tr>
						</logic:iterate>
					</table>
		</div>
	</div>
	<div class="member_selector_wrapper" style="margin-left:40%; padding: 0px; height: 98%;">
		<div style="background: #4C6185; margin:0px; color: white; font-weight: bold; font-size: small; padding:2px; height: 7%;
			border-right: 1px solid black; border-bottom: 1px solid black;border-top: 1px solid black;">
				&nbsp;<digi:trn>Member Selector</digi:trn>&nbsp;&nbsp;
				<input onkeypress="getSearchManagerInstanceByEl(this).clear()" id="${searchManagerId}" type="text" 
					style="width: ${searchFieldWidth}; height:18px; vertical-align: middle;font-size: 10px;" />&nbsp; 
				<button style="padding: 0px; font-size: 10px;" onclick="getSearchManagerInstanceById('${searchManagerId}').findPrev()" class="buton" type="button">&lt;&lt;</button>&nbsp;
				<button style="padding: 0px; font-size: 10px;" onclick="getSearchManagerInstanceById('${searchManagerId}').findNext()" class="buton" type="button">&gt;&gt;</button>
		</div>
					<c:set var="displayProperty"> </c:set>
					<logic:iterate id="element" name="elements" scope="page">
						<div style="height: 93%; display:none; border: 1px solid #b3c5d4; overflow: auto; background: white;" id="${element.htmlDivId}">
							<bean:define id="reqEntityList" name="element" property="rootHierarchyListable.children" toScope="request" />
							<bean:define id="reqSelectedEntityIds" toScope="request">${element.actionFormProperty}</bean:define>
							<ul style="list-style-type: none;">
								<li>
									<input type="checkbox" onclick="toggleCheckChildren(this)"/> 
										<span style="font-family: Arial; font-size: 12px;">
											<digi:trn>${element.rootHierarchyListable.label}</digi:trn>
										</span>
									<jsp:include page="hierarchyLister.jsp" />
								</li>
							</ul>
						</div>
						<c:set var="displayProperty"> display: none;</c:set>
					</logic:iterate>
	</div>
	