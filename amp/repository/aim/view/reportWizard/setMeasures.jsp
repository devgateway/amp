<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

				<logic:iterate name="myForm" id="ampMeasures" property="sortedAmpMeasures" type="org.digijava.module.aim.dbentity.AmpMeasures">
						<feature:display name="${ampMeasures.measureName}" module="Measures">
							<script type="text/javascript" >
                                insertMeasureInfo('${ampMeasures.measureId}', '${ampMeasures.measureName}');
							</script>
							<li class="list1 text-align" id="measure_${ampMeasures.measureId}">
								<input type="checkbox" value="${ampMeasures.measureId}" style='line-height:15px; margin-top:6px;'/>
								<digi:trn key="aim:reportBuilder:${ampMeasures.measureName}">
									<c:out value="${ampMeasures.measureName}"/>
								</digi:trn>
								<span style="display: none" original_measure_name="<c:out value='${ampMeasures.measureName}' />"></span>
								<logic:notEmpty name="ampMeasures" property="description" >
									<img src= "../ampTemplate/images/help.gif" border="0" title="<digi:trn>${ampMeasures.description}</digi:trn>">
								</logic:notEmpty>
							</li>
						</feature:display>
				</logic:iterate>