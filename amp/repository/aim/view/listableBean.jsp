<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<!-- the PropertyListable bean defined as attribute of the request -->
<bean:define id="bean" name="listable" type="org.dgfoundation.amp.PropertyListable" scope="request"	toScope="page" />
<!-- style of display -->
<bean:define id="style" name="listableStyle" type="java.lang.String" scope="request" toScope="page" />
<!-- trn tags prefix -->
<bean:define id="prefix" name="listableTrnPrefix" type="java.lang.String" scope="request" toScope="page" />

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
	<c:set var="morePropValue" scope="page" value=""/>
	<c:set var="showPropValue" scope="page" value="false"/>
	<c:set var="morePropCount" scope="page" value="6"/>

	<logic:iterate id="prop" name="pMap">
		<c:if test="${prop.key != 'renderEndYear' && prop.key != 'renderStartYear' }">
	        <c:set var="description">
	          	<c:if test="${prop.value.showDescription==true}">
	        		<digi:trn>${prop.value.description}</digi:trn>
	        	</c:if>
	        </c:set>
	       
	        <c:set var="value">
				    <c:forTokens items="${prop.value.value}" delims="," var="propName" varStatus="status">
				    	<c:if test="${status.count < morePropCount}">
				      		${propName},
				    	</c:if>
				    	<c:if test="${status.count == morePropCount}">
							<c:set var="showPropValue" scope="page" value="true"/>
				    		<c:set var="morePropValue" scope="page">${morePropValue} <br/>
					          	<c:if test="${prop.value.showDescription==true}">
					        		<digi:trn>${prop.value.description}</digi:trn>
					        	</c:if>
				    		</c:set>
				    	</c:if>
				    	<c:if test="${status.count >= morePropCount}">
				    		<c:set var="morePropValue" scope="page">${morePropValue} ${propName},</c:set>
				    	</c:if>
				    </c:forTokens>		
	        </c:set>
	  
	  	    <c:if test="${prop.value.position =='LEFT'}">
	    	   	 ${description} ${value}
	        </c:if>
	        
	    	<c:if test="${prop.value.position =='RIGTH'}">
	    		${value} ${description}
	    	</c:if>
	    	
    		<c:if test="${prop.value.position==NULL}">
    	 	 	 ${name} ${value} 
	    	</c:if>
       		<c:if test="${prop.value.hiddenValue!=true || prop.value.showDescription==true}">
 				/
 			</c:if>
 		 </c:if>
 	   </logic:iterate>
 	   <c:if test="${showPropValue == true}">
	 	   <span onclick="showDiv()" style="color: blue; cursor: pointer;"><digi:trn>more...</digi:trn></span>
			<div id="morePropId" style="display: none;">${morePropValue}</div>
	   </c:if>
</logic:equal>

