
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<c:set scope="session" var="currentLevel">${currentLevel + 1}</c:set>

<digi:instance property="aimVisibilityManagerForm" />

<bean:define name="currentTemplate" id="currentTemplate" type="org.digijava.module.aim.dbentity.AmpTemplatesVisibility" scope="request" toScope="page"/>
<bean:define id="moduleAux" name="moduleAux" type="org.dgfoundation.amp.visibility.AmpTreeVisibility" scope="request" toScope="page"/>
<bean:define id="moduleAux2" name="moduleAux" property="root" type="org.digijava.module.aim.dbentity.AmpModulesVisibility" scope="page"/>
                    <c:if test="${currentLevel == 1}">
						<div id="divmodule<bean:write name="moduleAux" property="root.id"/>">
                    </c:if>
	
	                    <c:if test="${currentLevel == 1}">
    	                    <li style="list-style:none" id="limodule:<bean:write name="moduleAux" property="root.id"/>" title="<digi:trn key='<%="fm:tooltip:"+moduleAux.getRoot().getNameTrimmed() %>'><bean:write name="moduleAux" property="root.name"/></digi:trn>">
<div style="float:right;">
<a href="#" onclick="sortTree(<bean:write name="moduleAux" property="root.id"/>, false);return false;"><digi:trn key="fm:ascendingorder">Ascending order</digi:trn></a>
<a href="#" onclick="sortTree(<bean:write name="moduleAux" property="root.id"/>, true);return false;"><digi:trn key="fm:descendingorder">Descending order</digi:trn></a>
</div>
<br />
                        </c:if>
    
                        <c:if test="${currentLevel != 1}">
                            <li id="limodule:<bean:write name="moduleAux" property="root.id"/>" title="<digi:trn key='<%="fm:tooltip:"+moduleAux.getRoot().getNameTrimmed() %>'><bean:write name="moduleAux" property="root.name"/></digi:trn>">
                        </c:if>
    
                        <logic:equal name="aimVisibilityManagerForm" property="mode" value="addNew">
                            <input onclick="toggleChildrenVisibility('limodule:<bean:write name="moduleAux" property="root.id"/>')"
                             type="checkbox" id="moduleVis:<bean:write name="moduleAux" property="root.id"/>" 
                             name="moduleVis:<bean:write name="moduleAux" property="root.id"/>" 
                             value="moduleVis:<bean:write name="moduleAux" property="root.id"/>"
                            />
                        </logic:equal>
                        <logic:equal name="aimVisibilityManagerForm" property="mode" value="editTemplateTree">
                            <input onclick="toggleChildrenVisibility('limodule:<bean:write name="moduleAux" property="root.id"/>')" 
                            type="checkbox" id="moduleVis:<bean:write name="moduleAux" property="root.id"/>" 
                            name="moduleVis:<bean:write name="moduleAux" property="root.id"/>" 
                            value="moduleVis:<bean:write name="moduleAux" property="root.id"/>" 
                            <%= moduleAux2.isVisibleTemplateObj(currentTemplate)?"checked":"" %>
                        />
                        </logic:equal>
                        <c:if test="${currentLevel == 1}">
                        <a id="module:<bean:write name="moduleAux" property="root.id"/>" style="font-size: 12px;color:#0e69b3;text-decoration:none">
                            <digi:trn key='<%="fm:"+moduleAux.getRoot().getNameTrimmed().replace("&","-")%>'><bean:write name="moduleAux" property="root.properName"/></digi:trn>
                        </a>
                        </c:if>
                        <c:if test="${currentLevel != 1}">
                        <a href="#" id="module:<bean:write name="moduleAux" property="root.id"/>" style="font-size: 12px;color:#0e69b3;text-decoration:none">
                            <digi:trn key='<%="fm:"+moduleAux.getRoot().getNameTrimmed().replace("&","-")%>'><bean:write name="moduleAux" property="root.properName"/></digi:trn>
                        </a>
                        </c:if>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-[<a
											style="font-size: 12px; cursor: pointer; color: #006699; text-decoration: none"
											title="Click to edit module description"
											onClick='showDescriptionToolbox("textarea_${moduleAux.root.id}_module")'"><digi:trn>edit description</digi:trn></a>]
																				
							<textarea rows="2" cols="20" style="display:none;z-index: 10" id="textarea_${moduleAux.root.id}_module" onblur="return enterKeyIsPressed(this)" onkeypress="return enterKeyIsPressed(this,event)"  >${moduleAux.root.description}</textarea>

<c:if test="${currentLevel == 1}">
					<div style="padding-left:15px">
						<ul name="dhtmltreeArray" id="dhtmltree:<bean:write name="moduleAux" property="root.id"/>" class="dhtmlgoodies_tree">
</c:if>
<c:if test="${currentLevel != 1}">
						<ul>
</c:if>

							<bean:define id="size" name="moduleAux2" property="submodules"/>
							<logic:notEmpty name="size">
							<logic:iterate name="moduleAux" property="sorteditems" id="moduleAuxIt" type="java.util.Map.Entry" >
								<bean:define name="currentTemplate" id="currentTemplate" type="org.digijava.module.aim.dbentity.AmpTemplatesVisibility" scope="page" toScope="request"/>
								<bean:define id="moduleAux" name="moduleAuxIt" property="value" type="org.dgfoundation.amp.visibility.AmpTreeVisibility" scope="page" toScope="request"/>
								<jsp:include page="generateTreeXLevelVisibility.jsp" />								

							</logic:iterate>
							</logic:notEmpty>
							<logic:empty name="size">
								<bean:define name="currentTemplate" id="currentTemplate" type="org.digijava.module.aim.dbentity.AmpTemplatesVisibility" scope="page" toScope="page"/>
								<logic:iterate name="moduleAux" property="sorteditems" id="feature" type="java.util.Map.Entry" >
								
									<bean:define id="featureAux" name="feature" property="value" type="org.dgfoundation.amp.visibility.AmpTreeVisibility" scope="page"/>
									<bean:define id="featureAux2" name="featureAux" property="root" type="org.digijava.module.aim.dbentity.AmpFeaturesVisibility" scope="page"/>

									<c:set var="featureDescription">
										<digi:trn key='<%="fm:tooltip:"+featureAux.getRoot().getNameTrimmed()%>'><bean:write name="featureAux" property="root.description"/></digi:trn>
									</c:set>
									<c:set var="featureName">
										<digi:trn key='<%="fm:"+featureAux.getRoot().getNameTrimmed().replace("&","-")%>'><bean:write name="featureAux" property="root.name"/></digi:trn>
									</c:set>
									
									<logic:notEqual name="featureDescription" value="${featureName}" >
										<li id="lifeature:<bean:write name="featureAux" property="root.id"/>" title="${featureDescription} | ${featureName}">
									</logic:notEqual>
									<logic:equal name="featureDescription" value="${featureName}">
										<li id="lifeature:<bean:write name="featureAux" property="root.id"/>" title="${featureDescription}">
									</logic:equal>
									
										<logic:equal name="aimVisibilityManagerForm" property="mode" value="addNew">
											<input onclick="toggleChildrenVisibility('lifeature:<bean:write name="featureAux" property="root.id"/>')" 
												type="checkbox" id="featureVis:<bean:write name="featureAux" property="root.id"/>" 
												name="featureVis:<bean:write name="featureAux" property="root.id"/>" 
											value="featureVis:<bean:write name="featureAux" property="root.id"/>"
											/>
										</logic:equal>

										<logic:equal name="aimVisibilityManagerForm" property="mode" value="editTemplateTree">
											<input onclick="toggleChildrenVisibility('lifeature:<bean:write name="featureAux" property="root.id"/>')" 
												type="checkbox" id="featureVis:<bean:write name="featureAux" property="root.id"/>" 
												name="featureVis:<bean:write name="featureAux" property="root.id"/>" 
												value="featureVis:<bean:write name="featureAux" property="root.id"/>"
												<%= featureAux2.isVisibleTemplateObj(currentTemplate)?"checked":"" %>
											/>
										</logic:equal>
									<a href="#" id="feature:<bean:write name="featureAux" property="root.id"/>" style="font-size: 12px;color:#0e69b3;text-decoration:none">
										<digi:trn key='<%="fm:"+featureAux.getRoot().getNameTrimmed().replace("&","-")%>'><bean:write name="featureAux" property="root.name"/></digi:trn>
									</a>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-[<a
											style="font-size: 12px; cursor: pointer; color: #006699; text-decoration: none"
											title="Click to edit feature description"
											onClick='showDescriptionToolbox("textarea_${featureAux.root.id}_feature")'><digi:trn>edit description</digi:trn></a>]
										<textarea rows="2" cols="20" style="display:none;z-index: 10"
											id="textarea_${featureAux.root.id}_feature"
											onblur="return enterKeyIsPressed(this)" onkeypress="return enterKeyIsPressed(this,event)">${featureDescription}</textarea>
										<ul>
										<logic:iterate name="featureAux" property="sorteditems" id="field" type="java.util.Map.Entry" >
											<bean:define id="fieldAux" name="field" property="value" type="org.dgfoundation.amp.visibility.AmpTreeVisibility" scope="page"/>
											<bean:define id="fieldAux2" name="fieldAux" property="root" type="org.digijava.module.aim.dbentity.AmpFieldsVisibility" scope="page"/>
											
											<c:set var="fieldDescription">
												<digi:trn key='<%="fm:tooltip:"+fieldAux.getRoot().getNameTrimmed()%>'><bean:write name="fieldAux" property="root.description"/></digi:trn>
											</c:set>
											<c:set var="fieldName">
												<digi:trn key='<%="fm:"+fieldAux.getRoot().getNameTrimmed().replace("&","-")%>'><bean:write name="fieldAux" property="root.name"/></digi:trn>
											</c:set>
									
											<logic:notEqual name="fieldDescription" value="${fieldName}" >
												<li class="dhtmlgoodies_sheet.gif" title="${fieldDescription} | ${fieldName}">
											</logic:notEqual>
											<logic:equal name="fieldDescription" value="${fieldName}">
												<li class="dhtmlgoodies_sheet.gif" title="${fieldDescription}">
											</logic:equal>
																						
												<logic:equal name="aimVisibilityManagerForm" property="mode" value="addNew">
													<input  onclick="toggleParentVisibilityOfTheField('fieldVis:<bean:write name="fieldAux" property="root.id"/>')"
													type="checkbox" id="fieldVis:<bean:write name="fieldAux" property="root.id"/>"
													name="fieldVis:<bean:write name="fieldAux" property="root.id"/>" 
													value="fieldVis:<bean:write name="fieldAux" property="root.id"/>"
													/>
												</logic:equal>
												<logic:equal name="aimVisibilityManagerForm" property="mode" value="editTemplateTree">
													<input onclick="toggleParentVisibilityOfTheField('fieldVis:<bean:write name="fieldAux" property="root.id"/>')"
													type="checkbox" id="fieldVis:<bean:write name="fieldAux" property="root.id"/>"
													name="fieldVis:<bean:write name="fieldAux" property="root.id"/>" 
													value="fieldVis:<bean:write name="fieldAux" property="root.id"/>" 
													<%= fieldAux2.isVisibleTemplateObj(currentTemplate)?"checked":"" %>
												/>
												</logic:equal>
												<a id="field:<bean:write name="fieldAux" property="root.id"/>" style="font-size: 12px;color:#0e69b3;text-decoration:none">
													<digi:trn key='<%="fm:"+fieldAux.getRoot().getNameTrimmed().replace("&","-")%>'><bean:write name="fieldAux" property="root.name"/></digi:trn>
												</a>
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-[<a style="font-size: 12px; cursor:pointer;color:#006699;text-decoration:none" title="Click to edit field description" onClick='showDescriptionToolbox("textarea_${fieldAux.root.id}_field")'><digi:trn>edit description</digi:trn></a>]
												<textarea rows="2" cols="20" style="display:none;z-index: 10" id="textarea_${fieldAux.root.id}_field" onblur="return enterKeyIsPressed(this)" onkeypress="return enterKeyIsPressed(this,event)">${fieldDescription}</textarea>		
											</li>	
										</logic:iterate>
									</ul>
								</li>
							</logic:iterate>
								
						</logic:empty>
						</ul>
<c:if test="${currentLevel > 1}">
					</li>
</c:if>
<c:if test="${currentLevel == 1}">
                    </div>
					</div>
</c:if>

<c:set scope="session" var="currentLevel">${currentLevel - 1}</c:set>
