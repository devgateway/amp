<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<!-- the PropertyListable bean defined as attribute of the request -->
<bean:define id="bean" name="listable"
	type="org.dgfoundation.amp.PropertyListable" scope="request"
	toScope="page" />
<!-- style of display -->
<bean:define id="style" name="listableStyle" type="java.lang.String"
	scope="request" toScope="page" />
<!-- trn tags prefix -->
<bean:define id="prefix" name="listableTrnPrefix"
	type="java.lang.String" scope="request" toScope="page" />


<!-- Display the bean in a table style -->
<logic:equal name="style" value="table">
	<TABLE width="100%">
		<TR>
			<TD colspan="2" align="center"><I>
              ${listable.beanName}</I></TD>
		</TR>
		<logic:iterate id="prop" name="bean" property="propertiesMap">
			<TR>
				<TD align="right"><B><digi:trn key="${prefix}:${prop.key}">${prop.key}</digi:trn>
				:</B></TD>
				<TD><logic:equal name="prop" property="value.class.simpleName"
					value="Boolean">
					<digi:trn key="${prefix}:${prop.value}">${prop.value}</digi:trn>
				</logic:equal> <logic:notEqual name="prop"
					property="value.class.simpleName" value="Boolean">
					<digi:trn key="${prefix}:${prop.value}">${prop.value}</digi:trn>
				</logic:notEqual></TD>


			</TR>
		</logic:iterate>
	</TABLE>
</logic:equal>

<!-- Display the bean in a comma separated list style -->
<logic:equal name="style" value="list">
	<I>${listable.beanName}</I>
	<logic:iterate id="prop" name="bean" property="propertiesMap">
		<B> <digi:trn key="${prefix}:${prop.key}">${prop.key}</digi:trn> :</B>
		<logic:equal name="prop" property="value.class.simpleName"
			value="Boolean">
			<digi:trn key="${prefix}:${prop.value}">${prop.value}</digi:trn>
		</logic:equal>
		<logic:notEqual name="prop" property="value.class.simpleName"
			value="Boolean">
			<digi:trn key="${prefix}:${prop.value}">${prop.value}</digi:trn>
		</logic:notEqual>

	</logic:iterate>
</logic:equal>

<logic:equal name="style" value="settingsList">
	<I>${listable.beanName}</I>
	<logic:iterate id="prop" name="bean" property="propertiesMap">
    	<c:if test="${prop.key != 'renderEndYear' && prop.key != 'renderStartYear' }">
            <digi:trn key="${prefix}:${prop.key}">${prop.key}</digi:trn>:
            <logic:equal name="prop" property="value.class.simpleName" value="Boolean">
                <digi:trn key="${prefix}:${prop.value}">${prop.value}</digi:trn>
            </logic:equal>
            <logic:notEqual name="prop" property="value.class.simpleName"
                value="Boolean">
                <digi:trn key="${prefix}:${prop.value}">${prop.value}</digi:trn>
            </logic:notEqual>|
        </c:if>
	</logic:iterate>
</logic:equal>
