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
<bean:define id="searchFieldWidth" toScope="page" value="130px"/>
<logic:notEmpty scope="request" name="reqSearchFieldWidth">
	<bean:define id="searchFieldWidth" toScope="page">${reqSearchFieldWidth}</bean:define>
</logic:notEmpty>	

<c:set var="selectorHeaderSize" scope="page" value="11" />
<logic:notEmpty scope="request" name="reqSelectorHeaderSize">
	<c:set var="selectorHeaderSize" scope="page" value="${reqSelectorHeaderSize}" />
</logic:notEmpty>	
		<div id="grouping_selector_wrapper">
			<div class="innertabheader" ><h3><digi:trn>Grouping Selector</digi:trn></h3></div>
			<div>		
				<ul  class="inside" >
					<logic:iterate id="element" name="elements" scope="page">
						<li style="cursor: pointer;"
							onclick="getRowSelectorInstance(this, ${propertyObj}, new DivManager('${element.htmlDivId}', ${propertyObj}), true).toggleRow()" 
							onMouseover="getRowSelectorInstance(this, ${propertyObj}, new DivManager('${element.htmlDivId}', ${propertyObj}), true).markRow(false) " 
							onMouseout="getRowSelectorInstance(this, ${propertyObj}, new DivManager('${element.htmlDivId}', ${propertyObj}), true).unmarkRow(false) ">
								<a onMouseover="this.style.backgroundColor='#c7d4db'" onMouseout="this.style.backgroundColor='#ffffff'">
									<digi:trn>${element.name}</digi:trn>
									<span >
										(${element.rootHierarchyListable.countDescendants-1})
										<button type="button" onclick="getRowSelectorInstance(this.parentNode, ${propertyObj}, new DivManager('${element.htmlDivId}', ${propertyObj}), true).toggleRow()" 
										style="display: none;">Fake</button>
									</span>
								</a>
						</li>
					</logic:iterate>
				</ul>
			</div>
	</div>
	<div id="member_selector_wrapper" >
			<div  class="innertabheader"><h3><digi:trn>Member Selector</digi:trn></h3><div class="memberSelectorInputWrapper" >
					<input onkeypress="getSearchManagerInstanceByEl(this).clear()" id="${searchManagerId}" type="text" style="margin-top:0px; width: ${searchFieldWidth};" class="inputx" />&nbsp;
					<button class="buttonx" onclick="getSearchManagerInstanceById('${searchManagerId}').findPrev()"  type="button">&lt;&lt;</button>
					<button class="buttonx" onclick="getSearchManagerInstanceById('${searchManagerId}').findNext()"  type="button">&gt;&gt;</button>
				</div>
				</div>
                	<div id="membercontainer">
					<c:set var="displayProperty"> </c:set>
					<logic:iterate id="element" name="elements" scope="page">
						<div  id="${element.htmlDivId}">
							<bean:define id="reqEntityList" name="element" property="rootHierarchyListable.children" toScope="request" />
							<bean:define id="reqSelectedEntityIds" toScope="request">${element.actionFormProperty}</bean:define>
							<div class="hiddenNameDiv" style="display: none;"><digi:trn>${element.name}</digi:trn></div>
							<ul >
								<li>
									<input type="checkbox" onclick="toggleCheckChildren(this);buildLabels();" class="root_checkbox"/> 
										<span style="font-family: Arial; font-size: 12px;">
											<digi:trn><c:out value="${element.rootHierarchyListable.label}"/></digi:trn>
										</span>
										<div style="display:none">
											${element.rootHierarchyListable.additionalSearchString}
										</div>
									<%@include file="hierarchyLister.jsp" %>
								</li>
							</ul>
						</div>
						<c:set var="displayProperty"> display: none;</c:set>
					</logic:iterate>
                    </div>
	</div>
	
	