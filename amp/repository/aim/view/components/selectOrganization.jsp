<%@ page pageEncoding="UTF-8"
	import="org.digijava.module.aim.dbentity.AmpOrganisation,java.util.*"%>
<%@ page import="org.digijava.kernel.util.SiteUtils"%>
<%@ page import="org.digijava.kernel.request.TLSUtils" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-core" prefix="fn"%>

<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/common/TranslationManager.js"></script>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<% if(SiteUtils.isEffectiveLangRTL()) { %>
  <link rel="stylesheet" href="/TEMPLATE/ampTemplate/css_2/amp-rtl.css">
<% } %>

<script language="JavaScript">
    var isRtl = <%=TLSUtils.getCurrentLocale().getLeftToRight() == false%>;
    var language = '<%=TLSUtils.getCurrentLocale().getCode()%>';
    var region = '<%=TLSUtils.getCurrentLocale().getRegion()%>';

	function checkNumericField(objName,comma,period,hyphen)
	{
		var numberfield = objName;
		if (checkNumericFieldValue(objName,comma,period,hyphen) == false)
		{
			numberfield.select();
			numberfield.focus();
			return false;
		}
		else
		{
			return true;
		}
	}

	function checkNumericFieldValue(objName,comma,period,hyphen)
	{
// only allow 0-9 be entered, plus any values passed
// (can be in any order, and don't have to be comma, period, or hyphen)
// if all numbers allow commas, periods, hyphens or whatever,
// just hard code it here and take out the passed parameters
		var checkOK = "0123456789" + comma + period + hyphen;
		var checkStr = TranslationManager.convertNumbersToWesternArabicIfNeeded(isRtl, language, region, objName.value);
		var allValid = true;
		var decPoints = 0;
		var allNum = "";

		for (i = 0;  i < checkStr.length;  i++)
		{
			ch = checkStr.charAt(i);
			for (j = 0;  j < checkOK.length;  j++)
			if (ch == checkOK.charAt(j))
			break;
			if (j == checkOK.length)
			{
				allValid = false;
				break;
			}
			if (ch != ",")
			allNum += ch;
		}
		if (!allValid)
		{
			alertsay = "Please enter only numbers in the \"Number of results per page\"."
			alert(alertsay);
			return (false);
		}
	}



	function validate() {
		<c:set var="translation">
			<digi:trn jsFriendly='true'>Please choose an organization to add</digi:trn>
		</c:set>
		if(document.aimSelectOrganizationForm.selectedOrganisationFromPages.value != "-1") return true;

		if (document.aimSelectOrganizationForm.selOrganisations.checked != null) { // only one
			if (document.aimSelectOrganizationForm.selOrganisations.checked == false) {
				alert("${translation}");
				return false;
			}
		}
		else { // many
			var length = document.aimSelectOrganizationForm.selOrganisations.length;
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimSelectOrganizationForm.selOrganisations[i].checked == true) {
					flag = 1;
					break;
				}
			}

			if (flag == 0) {
				alert("${translation}");
				return false;
			}
		}
		return true;
	}
	
	
	function setOrganization(id) {
		<digi:context name="selOrg" property="context/module/moduleinstance/selectOrganizationComponent.do?edit=true&orgSelReset=false&subAction=organizationSelected"/>
	    document.aimSelectOrganizationForm.action = "<%= selOrg %>&id="+id;
		document.aimSelectOrganizationForm.selectedOrganisationFromPages.value=-1;
	    document.aimSelectOrganizationForm.submit();
		return true;
	}
	
	function selectOrganization() {
		var flag = validate();
		if (flag == false)
			return false;

		
		<digi:context name="selOrg" property="context/module/moduleinstance/selectOrganizationComponent.do?edit=true&orgSelReset=false&subAction=organizationSelected"/>
	    document.aimSelectOrganizationForm.action = "<%= selOrg %>";
		//document.aimSelectOrganizationForm.target = window.opener.name;
		document.aimSelectOrganizationForm.selectedOrganisationFromPages.value=-1;
	    document.aimSelectOrganizationForm.submit();
		
		return true;
	}

	function resetForm() {
		document.aimSelectOrganizationForm.ampOrgTypeId.value=-1;
		document.aimSelectOrganizationForm.keyword.value="";
		document.aimSelectOrganizationForm.tempNumResults.value = TranslationManager.convertNumbersToEasternArabicIfNeeded(isRtl, language, region, '10');
	
	}




	function selectOrganizationPages(page) {
	   <digi:context name="searchOrg" property="context/module/moduleinstance/selectOrganizationComponent.do?edit=true&orgSelReset=false&subAction=selectPage&page=" />
	   var val = "<%=searchOrg%>";
	   val = val + page;
	   document.aimSelectOrganizationForm.action = val;
	   document.aimSelectOrganizationForm.selectedOrganisationFromPages.value=page;
	   document.aimSelectOrganizationForm.submit();
	   return true;
	}	

	function searchOrganization() {
		if(checkNumericField(document.aimSelectOrganizationForm.tempNumResults	,'','','')==true)
		{
            var convertedResults = TranslationManager.convertNumbersToWesternArabicIfNeeded(isRtl, language, region, document.aimSelectOrganizationForm.tempNumResults.value);
            if (convertedResults == 0) {
			    alert ("Invalid value at 'Number of results per page'");
			    document.aimSelectOrganizationForm.tempNumResults.focus();
			    return false;
			} else {
			    <digi:context name="searchOrg" property="context/module/moduleinstance/selectOrganizationComponent.do?edit=true&subAction=search"/>
			    document.aimSelectOrganizationForm.action = "<%= searchOrg %>";
                document.aimSelectOrganizationForm.tempNumResults.value = convertedResults;
			    document.aimSelectOrganizationForm.submit();
				return true;
			}
		}
		else return false;
	}

	function searchAlpha(val) {
        var convertedResults = TranslationManager.convertNumbersToWesternArabicIfNeeded(isRtl, language, region, document.aimSelectOrganizationForm.tempNumResults.value);
		if (convertedResults == 0) {
			  alert ("Invalid value at 'Number of results per page'");
			  document.aimEditActivityForm.tempNumResults.focus();
			  return false;
		} else {
			 <digi:context name="searchOrg" property="context/module/moduleinstance/selectOrganizationComponent.do"/>
			 url = "<%= searchOrg %>?alpha=" + val + "&orgSelReset=false&edit=true&subAction=search";
		     document.aimSelectOrganizationForm.action = url;
             document.aimSelectOrganizationForm.tempNumResults.value = convertedResults;
		     document.aimSelectOrganizationForm.submit();
			 return true;
		}
	}
		
	function searchAlphaAll(val) {
        var convertedResults = TranslationManager.convertNumbersToWesternArabicIfNeeded(isRtl, language, region, document.aimSelectOrganizationForm.tempNumResults.value);
		if (convertedResults == 0) {
			  alert ("Invalid value at 'Number of results per page'");
			  document.aimSelectOrganizationForm.tempNumResults.focus();
			  return false;
		} else {
			 <digi:context name="searchOrg" property="context/module/moduleinstance/selectOrganizationComponent.do?edit=true&subAction=search&viewAll=viewAll"/>
			    document.aimSelectOrganizationForm.action = "<%= searchOrg %>";
		      var aux= document.aimSelectOrganizationForm.tempNumResults.value;
             document.aimSelectOrganizationForm.tempNumResults.value = convertedResults;
		     document.aimSelectOrganizationForm.submit();
		      //document.aimSelectOrganizationForm.tempNumResults.value=aux;
			  return true;
		}
	}

	function load() {
	}

	function unload() {
	}

	function closeWindow() {
		window.close();
	}

	window.onload=function(){
		processLoad();
	}
	

</script>

<digi:instance property="aimSelectOrganizationForm" />
<digi:form action="/selectOrganizationComponent.do" method="post">

<html:hidden property="selectedOrganisationFromPages" />
<html:hidden property="<%= org.digijava.module.aim.uicomponents.form.selectOrganizationComponentForm.EXCLUDED_ORG_IDS_SEPARATED %>"/>

	<script language="JavaScript">
	//if use client yes
	
	<logic:equal name="aimSelectOrganizationForm" property="useClient" value="true">
		function setValues(id,name){
			window.opener.document.getElementById('<bean:write name="aimSelectOrganizationForm" property="valueHoder"/>').value =id;
			window.opener.document.getElementById('<bean:write name="aimSelectOrganizationForm" property="nameHolder"/>').value =name;
			window.close();
		}
	</logic:equal>
	//was selected

	function processLoad(){	
	<logic:equal name="aimSelectOrganizationForm" property="afterSelect" value="true">
	//call back function 
	<logic:notEmpty name="aimSelectOrganizationForm" property="callbackFunction">
		window.opener.<bean:write name="aimSelectOrganizationForm" property="callbackFunction"/>;
	</logic:notEmpty>	
	//Refresh
	<logic:equal name="aimSelectOrganizationForm" property="refreshParent" value="true">
		window.opener.document.location=window.opener.document.location;
	</logic:equal>
		//fcous on parent windows
		window.opener.focus();
		//close this
		window.close();
	</logic:equal>
	}
</script>


    <logic:iterate id="iterAdParam" property="aditionalParameters" name="aimSelectOrganizationForm" type="Map.Entry">
        <% if (iterAdParam.getKey().equals(new String("removeAddButton")) && iterAdParam.getValue().equals(new String("true"))) { %>
            <bean:define id="removeAddButton" value="true"></bean:define>
        <% } %>
    </logic:iterate>



	<table width="100%" cellSpacing="5" cellPadding="5" vAlign="top" border="0" class="content-dir">
		<tr>
			<td vAlign="top">
			<table bgcolor="#f4f4f2" cellPadding="5" cellSpacing="5" width="100%" class="box-border-nopadding">
				<tr>
					<td align=left valign="top">
					<table bgcolor=#f4f4f2 cellpadding="0" cellspacing="0" width="100%"
						class=box-border-nopadding>
						<tr bgcolor="#4A687A">
							<td vAlign="center" width="100%" align="center" class="textalb" height="20">
								<digi:trn>Search Organizations</digi:trn>
                            </td>
						</tr>
						<tr>
							<td align="center" bgcolor="#F8F8F8">
								<table cellSpacing="2" cellPadding="2">
									<tr>
										<td>
											<digi:trn>Select Organization type</digi:trn>
										</td>
										<td>
											<c:set var="translation">
												<digi:trn>All</digi:trn>
											</c:set>
											<html:select property="ampOrgTypeId" styleClass="inp-text" styleId="ampOrgTypeId">
												<logic:equal value="false" name="aimSelectOrganizationForm" property="filterDonorGroups">
													<html:option value="-1">${translation}</html:option>
												</logic:equal>
												<logic:notEmpty name="aimSelectOrganizationForm" property="orgTypes">
													<html:optionsCollection name="aimSelectOrganizationForm" property="orgTypes" value="ampOrgTypeId" label="orgType" />
												</logic:notEmpty>
											</html:select>
										</td>
									</tr>
									<tr>
										<td>
											<digi:trn>Enter a keyword</digi:trn>
										</td>
										<td>
											<html:text property="keyword" styleClass="inp-text" styleId="keyword"/>
										</td>
									</tr>
									<tr>
										<td>
											<digi:trn>Number of results per page</digi:trn>
										</td>
										<td>
											<html:text property="tempNumResults" size="2" styleClass="inp-text" styleId="tempNumResults"/>
										</td>
									</tr>
	
									<tr>
										<td align="center" colspan="2">
											<html:button styleClass="buttonx" property="submitButton" onclick="return searchOrganization()" styleId="orgSearchBtn">
												<digi:trn>Search</digi:trn>
											</html:button> &nbsp; 
											<html:button styleClass="buttonx" property="resetButton" onclick="resetForm()">
												<digi:trn>Clear</digi:trn>
											</html:button> &nbsp;
											<html:button styleClass="buttonx" property="submitButton" onclick="closeWindow()">
												<digi:trn>Close</digi:trn>
											</html:button>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td align="left" vAlign="top">
						<table bgcolor="#f4f4f2" cellPadding="0" cellSpacing="0" width="100%" class="box-border-nopadding">
							<tr bgcolor="#4A687A">
								<td vAlign="center" width="100%" align="center" class="textalb"	height="20">
									<digi:trn>List of Organizations</digi:trn>
								</td>
							</tr>
							<logic:notEmpty name="aimSelectOrganizationForm" property="organizations">
								<tr>
									<td align="left" vAlign="top">
										<table width="100%" cellPadding="3" id="searchResults">
											<logic:iterate name="aimSelectOrganizationForm"	id="organisations" property="organizations"	type="org.digijava.module.aim.dbentity.AmpOrganisation">		
												<tr>
													<td bgcolor=#F8F8F8 width="1%">&nbsp;</td>		
                                                        <logic:equal name="aimSelectOrganizationForm" property="multiSelect" value="true">
                                                            <td bgcolor="#F8F8F8" width="10%">
                                                                <html:multibox	property="selOrganisations" styleClass="orgsMultibox">
                                                                    <bean:write name="organisations" property="ampOrgId" />
                                                                </html:multibox>&nbsp;
                                                            </td>
                                                        </logic:equal>
                                                    <td bgcolor="#F8F8F8" width="90%">
														<c:out	value="${organisations.acronym}" />&nbsp;&nbsp; ( <c:out	value="${organisations.name}"/> )
													</td>		
													<logic:equal name="aimSelectOrganizationForm" property="multiSelect" value="false">
														<td bgcolor="#F8F8F8" width="1%">
															<logic:equal name="aimSelectOrganizationForm" property="useClient" value="false">
																<html:button property="select" styleClass="buttonx" onclick="setOrganization(${organisations.ampOrgId})">
																	<digi:trn>Select</digi:trn>
																</html:button>
															</logic:equal>
															<logic:equal name="aimSelectOrganizationForm" property="useClient" value="true">
																<logic:equal name="aimSelectOrganizationForm" property="useAcronym" value="true">
																	<input type="button" class="buttonx" title="<digi:trn>Add</digi:trn>" value="+" onclick="setValues(<c:out value="${organisations.ampOrgId}"/>,'<%=org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(organisations.getName())%>')" />
																</logic:equal>
																<logic:equal name="aimSelectOrganizationForm" property="useAcronym" value="false">
																	<input class="buttonx" type="button" title="<digi:trn>Add</digi:trn>" value="+" onclick="setValues(<c:out value="${organisations.ampOrgId}"/>,'<%=org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(organisations.getName())%>')" />
																</logic:equal>
															</logic:equal>
														</td>
													</logic:equal>
												</tr>
											</logic:iterate>
											<!-- code recheck selOrganisations lost by pagination -->
									        <c:if test="${aimSelectOrganizationForm.allSelectedOrgsIds != null}">
                                                <c:forEach items="${aimSelectOrganizationForm.allSelectedOrgsIds}" var="itemy">
                                                    <c:set var="mio">
                                                        <c:out value="${itemy}" />
                                                    </c:set>
                                                    <script language="javascript">
                                                        for(var i = 0; i < 10; i++){
                                                            var chekbox = document.getElementsByName("selOrganisations")[i];
                                                            var elebox = chekbox.value;
                                                            if(elebox==${mio}){
                                                                chekbox.checked = true;
                                                            }
                                                        }
                                                    </script>
                                                </c:forEach>
									        </c:if>
                                            <!-- end code recheck selOrganisations lost by pagination -->

											<tr>
												<td align="center" colspan="3" bgcolor=#F8F8F8>
													<table cellPadding=5>
														<tr>
			                                                <logic:notEqual value="true" name="removeAddButton">
			                                                	<logic:equal name="aimSelectOrganizationForm" property="multiSelect" value="true">
			                                                		<td>
																		<html:button styleClass="buttonx" property="submitButton" onclick="return selectOrganization()" styleId="selOrgBtn">
																			<digi:trn>Add</digi:trn>
																		</html:button>
																	</td>
			                                                	</logic:equal>													
			                                                </logic:notEqual>
															<td>
																<html:button styleClass="buttonx" property="submitButton" onclick="closeWindow()">
																<digi:trn>Close</digi:trn>
																</html:button>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<logic:notEmpty name="aimSelectOrganizationForm" property="pages">
									<tr>
										<td align="center">
										<table align="center" cellpadding="0" cellspacing="0">
											<tr>
												<td height="18" align="center" style="size: 9px; color: #333333">
													<span><digi:trn key="aim:pages">Pages</digi:trn></span>
                                                    <c:if test="${aimSelectOrganizationForm.currentPage > 1}">

														<c:set var="translation">
															<digi:trn key="aim:firstpage">First Page</digi:trn>
														</c:set>
														<span>
                                                        <a style="text-decoration: none; color: #333333"
                                                            href="javascript:selectOrganizationPages(1)"
                                                            title="${translation}"> &lt;&lt; </a>
														</span>
                                                        <c:set var="translation">
                                                            <digi:trn key="aim:previouspage">Previous Page</digi:trn>
                                                        </c:set>
                                                        <c:set var="prevPage">
                                                            ${aimSelectOrganizationForm.currentPage -1}
                                                        </c:set>
														<span>
															<a style="text-decoration: none; color: #333333"
																href="javascript:selectOrganizationPages(${prevPage})"
																title="${translation}"> &lt; </a>
														</span>
                                                    </c:if>

                                                    <c:set var="length" value="${aimSelectOrganizationForm.pagesToShow}"></c:set>
                                                    <c:set var="start" value="${aimSelectOrganizationForm.startPage}" />

												    <logic:iterate name="aimSelectOrganizationForm"
													property="pages" id="pages" type="java.lang.Integer"
													offset="start" length="length">
                                                        <c:if test="${aimSelectOrganizationForm.currentPage == pages}">
															<span><font color="#FF0000"><digi:easternArabicNumber><%=pages%></digi:easternArabicNumber></font></span>
                                                        </c:if>
                                                        <c:if test="${aimSelectOrganizationForm.currentPage != pages}">
                                                            <c:set var="translation">
                                                                <digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
                                                            </c:set>
															<span><a style="color: #333333; text-decoration: none;"
                                                                href="javascript:selectOrganizationPages(<%=pages%>);"><digi:easternArabicNumber><%=pages%></digi:easternArabicNumber></a>
															</span>
                                                        </c:if>
                                                        |&nbsp;
                                                    </logic:iterate>

                                                    <c:if test="${aimSelectOrganizationForm.currentPage != aimSelectOrganizationForm.pagesSize}">

                                                        <c:set var="nextPage">
                                                            ${aimSelectOrganizationForm.currentPage + 1}
                                                        </c:set>

                                                        <c:set var="translation">
                                                            <digi:trn key="aim:nextpage">Next Page</digi:trn>
                                                        </c:set>
														<span>
                                                        	<a style="color: #333333; text-decoration: none;" href="javascript:selectOrganizationPages(${nextPage})" title="${translation}"> &gt; </a>
														</span>
                                                        &nbsp;
                                                        <c:set var="translation">
                                                            <digi:trn key="aim:lastpage">Last Page</digi:trn>
                                                        </c:set>
														<span>
															<a style="color: #333333; text-decoration: none;" href="javascript:selectOrganizationPages(${aimSelectOrganizationForm.pagesSize});" title="${translation}">
																&gt;&gt;
															</a>
                                                        &nbsp;
														</span>
                                                    </c:if>

													<span><digi:easternArabicNumber><c:out value="${aimSelectOrganizationForm.currentPage}"></c:out></digi:easternArabicNumber>&nbsp;</span>
													<span><digi:trn key="aim:of">of</digi:trn>&nbsp;</span>
                                                    <span><digi:easternArabicNumber><c:out value="${aimSelectOrganizationForm.pagesSize}"></c:out></digi:easternArabicNumber></span>
                                                </td>
											</tr>
										</table>
										</td>
									</tr>
								</logic:notEmpty>
							</logic:notEmpty>

                            <c:if test="${empty aimSelectOrganizationForm.alphaPages && empty aimSelectOrganizationForm.pages}">
                                <tr>
                                    <td align="center">
                                        <table align="center" cellpadding="0" cellspacing="0">
                                            <tr>
                                                <td height="18" align="center" style="size: 9px; color: #333333">
                                                    <digi:trn>No results for this search</digi:trn>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </c:if>

							<logic:notEmpty name="aimSelectOrganizationForm" property="alphaPages">
								<tr>
									<td align="center">
									<table cellpadding="0" cellspacing="0">
										<tr>
											<td height="18" align="center" style="size: 9px;">
                                                <logic:iterate name="aimSelectOrganizationForm" property="alphaPages" id="alphaPages" type="java.lang.String">
                                                    <c:if test="${alphaPages != null}">
                                                        <c:if
                                                            test="${aimSelectOrganizationForm.currentAlpha == alphaPages}">
                                                            <font color="#FF0000"><%=alphaPages%></font>
                                                        </c:if>
                                                        <c:if
                                                            test="${aimSelectOrganizationForm.currentAlpha != alphaPages}">
                                                            <c:set var="translation">
                                                                <digi:trn key="aim:clickToViewNextPage">Click here to go to next page</digi:trn>
                                                            </c:set>
                                                            <a style="color: #333333"
                                                                href="javascript:searchAlpha('<%=alphaPages%>')"
                                                                title="${translation}"><%=alphaPages%></a>
                                                        </c:if>
                                                    |&nbsp;
                                                    </c:if>
											    </logic:iterate>
                                            </td>
										</tr>
										<tr>
											<td height="18" align="center"><c:set var="translation">
												<digi:trn key="aim:clickToViewAllSearchPages">Click here to view all search pages</digi:trn>
											</c:set> <a style="color: #333333; size: 9px; text-decoration: none;"
												href="javascript:searchAlphaAll('viewAll')"
												title="${translation}"><digi:trn key="aim:clickToViewAllSearchPages">Click here to view all search pages</digi:trn></a></td>
										</tr>
									</table>
									</td>
								</tr>
							</logic:notEmpty>
						</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</digi:form>

<script type="text/javascript">
var enterBinder	= new EnterHitBinder('selOrgBtn');
enterBinder.map(["keyword"], "orgSearchBtn");
</script>


