<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/src/main/resources/tld/moduleVisibility.tld" prefix="module"%>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

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
