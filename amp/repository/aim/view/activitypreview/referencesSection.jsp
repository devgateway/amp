<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/src/main/webapp/WEB-INF/tld/moduleVisibility.tld" prefix="module"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean"%>

<digi:instance property="aimEditActivityForm" />
<%--@elvariable id="aimEditActivityForm" type="org.digijava.module.aim.form.EditActivityForm"--%>

<module:display name="References" parentModule="PROJECT MANAGEMENT">
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
</module:display>
