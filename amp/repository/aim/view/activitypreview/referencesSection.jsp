<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>

<digi:instance property="aimEditActivityForm" />
<%--@elvariable id="aimEditActivityForm" type="org.digijava.ampModule.aim.form.EditActivityForm"--%>

<ampModule:display name="References" parentModule="PROJECT MANAGEMENT">
    <fieldset>
        <legend>
		<span class=legend_label id="referenceslink" style="cursor: pointer;">
			<digi:trn>References</digi:trn>
		</span>
        </legend>
        <div id="referencesdiv" class="toggleDiv">
            <ul>
                <logic:notEmpty name="aimEditActivityForm" property="documents.referenceDocs">
                    <logic:iterate name="aimEditActivityForm" property="documents.referenceDocs" id="doc">
                        <li>
				<span class="word_break">
					<bean:write name="doc" property="categoryValue"/>
				</span>
                        </li>
                    </logic:iterate>
                </logic:notEmpty>
            </ul>
        </div>
    </fieldset>
</ampModule:display>
