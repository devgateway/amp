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

<logic:present name="persistenceProperties" scope="request" >
	<bean:define id="pMap" name="bean" property="persistencePropertiesMap" toScope="page"/>
</logic:present>
<logic:notPresent name="persistenceProperties" scope="request" >
	<bean:define id="pMap" name="bean" property="propertiesMap" toScope="page"/>
</logic:notPresent>


<!-- Display the bean in a table style -->
<logic:equal name="style" value="table">
	<TABLE width="100%">
		<TR>
			<TD colspan="2" align="center"><I>
              ${listable.beanName}</I></TD>
		</TR>
		<logic:iterate id="prop" name="pMap">
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
	<logic:iterate id="prop" name="bean" property="	">
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
	<logic:iterate id="prop" name="pMap">
    	<c:if test="${prop.key != 'renderEndYear' && prop.key != 'renderStartYear' }">
            <digi:trn key="${prefix}:${prop.key}"><i>${prop.key}</i></digi:trn>:
            <c:if test="${prop.key == 'teamAssignedOrgs' ||  prop.key == 'locationSelected' }">
            	${prop.value}
            </c:if>
            <c:if test="${prop.key != 'teamAssignedOrgs' &&  prop.key != 'locationSelected'}">
            	<digi:trn key="${prefix}:${prop.value}">${prop.value}</digi:trn>
            </c:if>
            |
        </c:if>${prop.key}
	</logic:iterate>
</logic:equal>
