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

				<logic:iterate name="myForm" id="ampMeasures"property="ampMeasures" type="org.digijava.module.aim.dbentity.AmpMeasures">
					<field:display name="${ampMeasures.aliasName}" feature="Measures">
							<li class="list1" id="measure_${ampMeasures.measureId}">
								<input type="checkbox" value="${ampMeasures.measureId}" />
								<digi:trn key="aim:reportBuilder:${ampMeasures.aliasName}">
									<c:out value="${ampMeasures.aliasName}"/>
								</digi:trn>
								<logic:equal name="ampMeasures" property="aliasName" value="Undisbursed Balance">
										<img src= "../ampTemplate/images/help.gif" border="0" title="Cumulative Commitment - Cumulative Disbursement (independent of filters)">
								</logic:equal>
							</li>
					</field:display>
				</logic:iterate>