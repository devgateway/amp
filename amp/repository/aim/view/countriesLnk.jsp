<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/globalsettings" prefix="globalsettings"%>

<c:set var="cn">
	<globalsettings:value name="Default Country" />
</c:set>
<c:choose>
	<c:when test="${cn=='et'}">
		<c:set var="translation">
			<digi:trn key="aim:clickToUseAmp${cn}">Click here to Use AMP Ethiopia</digi:trn>
		</c:set>
		<digi:link href="/index.do" title="${translation}">
			<digi:trn key="aim:useAMP${cn}Now"> Use AMP Ethiopia now</digi:trn>&nbsp;
	</digi:link>
	</c:when>
	<c:when test="${cn=='bi'}">
		<c:set var="translation">
			<digi:trn key="aim:clickToUseAmp${cn}">Click here to Use AMP Burundi</digi:trn>
		</c:set>
		<digi:link href="/index.do" title="${translation}">
			<digi:trn key="aim:useAMP${cn}Now"> Use AMP Burundi now</digi:trn>&nbsp;
	</digi:link>
	</c:when>
	<c:when test="${cn=='bf'}">
		<c:set var="translation">
			<digi:trn key="aim:clickToUseAmp${cn}">Click here to Use AMP Burkina Faso</digi:trn>
		</c:set>
		<digi:link href="/index.do" title="${translation}">
			<digi:trn key="aim:useAMP${cn}Now"> Use AMP Burkina Faso now</digi:trn>&nbsp;
	</digi:link>
	</c:when>
	<c:when test="${cn=='bo'}">
		<c:set var="translation">
			<digi:trn key="aim:clickToUseAmp${cn}">Click here to Use AMP Bolivia</digi:trn>
		</c:set>
		<digi:link href="/index.do" title="${translation}">
			<digi:trn key="aim:useAMP${cn}Now"> Use AMP Bolivia now</digi:trn>&nbsp;
	</digi:link>
	</c:when>
	<c:when test="${cn=='dc'}">
		<c:set var="translation">
			<digi:trn key="aim:clickToUseAmp${cn}">Click here to Use AMP DRC</digi:trn>
		</c:set>
		<digi:link href="/index.do" title="${translation}">
			<digi:trn key="aim:useAMP${cn}Now"> Use AMP DRC now</digi:trn>&nbsp;
	</digi:link>
	</c:when>
	<c:when test="${cn=='cz'}">
		<c:set var="translation">
			<digi:trn key="aim:clickToUseAmp${cn}">Click here to Use AMP Czech</digi:trn>
		</c:set>
		<digi:link href="/index.do" title="${translation}">
			<digi:trn key="aim:useAMP${cn}Now"> Use AMP Czech now</digi:trn>&nbsp;
	</digi:link>
	</c:when>
	<c:when test="${cn=='gh'}">
		<c:set var="translation">
			<digi:trn key="aim:clickToUseAmp${cn}">Click here to Use AMP Ghana</digi:trn>
		</c:set>
		<digi:link href="/index.do" title="${translation}">
			<digi:trn key="aim:useAMP${cn}Now"> Use AMP Ghana now</digi:trn>&nbsp;
	</digi:link>
	</c:when>
	<c:when test="${cn=='mg'}">
		<c:set var="translation">
			<digi:trn key="aim:clickToUseAmp${cn}">Click here to Use AMP Madagascar</digi:trn>
		</c:set>
		<digi:link href="/index.do" title="${translation}">
			<digi:trn key="aim:useAMP${cn}Now"> Use AMP Madagascar now</digi:trn>&nbsp;
	</digi:link>
	</c:when>
	<c:when test="${cn=='mw'}">
		<c:set var="translation">
			<digi:trn key="aim:clickToUseAmp${cn}">Click here to Use AMP Malawi</digi:trn>
		</c:set>
		<digi:link href="/index.do" title="${translation}">
			<digi:trn key="aim:useAMP${cn}Now"> Use AMP Malawi now</digi:trn>&nbsp;
	</digi:link>
	</c:when>
	<c:when test="${cn=='me'}">
		<c:set var="translation">
			<digi:trn key="aim:clickToUseAmp${cn}">Click here to Use AMP Montenegro</digi:trn>
		</c:set>
		<digi:link href="/index.do" title="${translation}">
			<digi:trn key="aim:useAMP${cn}Now"> Use AMP Montenegro now</digi:trn>&nbsp;
	</digi:link>
	</c:when>
	<c:when test="${cn=='tz'}">
		<c:set var="translation">
			<digi:trn key="aim:clickToUseAmp${cn}">Click here to Use AMP Tanzania</digi:trn>
		</c:set>
		<digi:link href="/index.do" title="${translation}">
			<digi:trn key="aim:useAMP${cn}Now"> Use AMP Tanzania now</digi:trn>&nbsp;
	</digi:link>
	</c:when>

	<c:otherwise>
		<c:set var="translation">
			<digi:trn key="aim:clickToUseAmp">Click here to Use AMP</digi:trn>
		</c:set>
		<digi:link href="/index.do" title="${translation}">
			<digi:trn key="aim:useAMPNow"> Use AMP now</digi:trn>&nbsp;
	</digi:link>
	</c:otherwise>
</c:choose>
