<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="umViewEditUserForm" />
<digi:context name="digiContext" property="context" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
<script language="javascript" type="text/javascript">
function goAction(value){
  if(value!=null){
    document.getElementById("event").value=value;
    document.forms[0].submit();
  }
}
function resetPasswordFields(){
  document.umViewEditUserForm.newPassword.value='';
  document.umViewEditUserForm.confirmNewPassword.value=''
}
function validate(str,value){
  var newPassword=document.umViewEditUserForm.newPassword.value;
  var confirmNewPassword=document.umViewEditUserForm.confirmNewPassword.value;
  if(newPassword.length==0||confirmNewPassword.length==0||newPassword!=confirmNewPassword){
    alert(str);
  }
  else{
    goAction(value);
  }

}
</script>
<digi:form action="/viewEditUser.do" method="post">
  <html:hidden name="umViewEditUserForm" property="event" styleId="event"/>
  <table align="center">
    <tr>
      <!-- Start Navigation -->
      <td height=33 width="400" colspan="2">
        <span class=crumb>
          <c:set var="translation">
            <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
          </c:set>
          <digi:link module="aim" href="/admin.do" styleClass="comment" title="${translation}" >
            <digi:trn key="aim:AmpAdminHome">
            Admin Home
            </digi:trn>
          </digi:link>&nbsp;&gt;&nbsp;

          <c:set var="translation">
            <digi:trn key="aim:clickToViewAllUsers">Click here to goto users manager</digi:trn>
          </c:set>
          <digi:link href="/viewAllUsers.do" styleClass="comment" title="${translation}" >
            <digi:trn key="aim:viewAllUsers">
            List of users
            </digi:trn>
          </digi:link>&nbsp;&gt;&nbsp;

          <digi:trn key="aim:viewEditUser:EditUser">
          Edit user
          </digi:trn>
        </span>
      </td>
      <!-- End navigation -->
    </tr>
    <tr>
      <td colspan="2">
        <span class=subtitle-blue>
          <digi:trn key="aim:viewEditUser:EditUserHeader">
          Edit user
          </digi:trn>
        </span>
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <digi:errors/>
        &nbsp;
        <br/>
        <logic:equal name="umViewEditUserForm" property="displaySuccessMessage" value="true" >
          <b>
            <digi:trn key="aim:viewEditUser:changePasswordSuccessMessage">
            The password has been changed successfully
            </digi:trn>
          </b>
        </logic:equal>
      </td>
    </tr>
    <tr>
      <td>
      &nbsp;
      </td>
    </tr>
    <tr style="height:20px;" bgcolor="#c9c9c7">
      <td bgcolor="#ffffff">

      </td>
      <td align="center">
        <b><digi:trn key="aim:viewEditUser:edit">Edit</digi:trn> ${umViewEditUserForm.name}</b>
      </td>
    </tr>
    <tr align="center">
      <td>

      </td>
      <td align="center" bgcolor="#c9c9c7">
        <table bgcolor="#ffffff" width="100%">
          <tr>
            <td>
              <digi:trn key="aim:viewEditUser:firstName">
              First name
              </digi:trn>
            </td>
            <td>
              <html:text name="umViewEditUserForm" property="firstNames" style="font-family:verdana;font-size:11px;width:180px;"/>
            </td>
          </tr>
          <tr>
            <td>
              <digi:trn key="aim:viewEditUser:lastName">
              Last name
              </digi:trn>
            </td>
            <td>
              <html:text name="umViewEditUserForm" property="lastName" style="font-family:verdana;font-size:11px;width:180px;"/>
            </td>
          </tr>
          <tr>
            <td>
              <digi:trn key="aim:viewEditUser:email">
              Email
              </digi:trn>
            </td>
            <td>
              <html:text name="umViewEditUserForm" property="email" style="font-family:verdana;font-size:11px;width:180px;"/>
            </td>
          </tr>
          <tr>
            <td>
              <digi:trn key="aim:viewEditUser:country">
              Country
              </digi:trn>
            </td>
            <td>
              <html:select name="umViewEditUserForm" property="selectedCountryIso" style="font-family:verdana;font-size:11px;width:180px;">
                <c:set var="translation">
                  <digi:trn key="aim:viewEditUser:selectCountry">
                  --Select country--
                  </digi:trn>
                </c:set>
                <html:option value="-1">${translation}</html:option>
                <c:if test="${!empty umViewEditUserForm.countries}">
                  <c:forEach var="cn" items="${umViewEditUserForm.countries}">
                    <c:set var="cnName">
                      <digi:trn key="aim:cn:${cn.name}">${cn.name}</digi:trn>
                    </c:set>
                    <html:option value="${cn.iso}">${cnName}</html:option>
                  </c:forEach>
                </c:if>
              </html:select>
            </td>
          </tr>
          <tr>
            <td>
              <digi:trn key="aim:viewEditUser:mailingAddress">
              Mailing address
              </digi:trn>
            </td>
            <td>
              <html:text name="umViewEditUserForm" property="mailingAddress" style="font-family:verdana;font-size:11px;width:180px;"/>
            </td>
          </tr>
          <tr>
            <td>
            &nbsp;
            </td>
          </tr>
          <tr>
            <td>
              <digi:trn key="aim:viewEditUser:organizationType">
              Organization type
              </digi:trn>
            </td>
            <td>
              <html:select name="umViewEditUserForm" property="selectedOrgTypeId" onchange="goAction('typeSelected');" style="font-family:verdana;font-size:11px;width:180px;">
                <c:set var="translation">
                  <digi:trn key="aim:viewEditUser:selectOrganisationType">
                  --Select organisation type--
                  </digi:trn>
                </c:set>
                <html:option value="-1">${translation}</html:option>
                <c:if test="${!empty umViewEditUserForm.orgTypes}">
                  <html:optionsCollection name="umViewEditUserForm" property="orgTypes" value="ampOrgTypeId" label="orgType"/>
                </c:if>
              </html:select>
            </td>
          </tr>
          <tr>
            <td>
              <digi:trn key="aim:viewEditUser:organizationGroup">
              Organization group
              </digi:trn>
            </td>
            <td>
              <html:select name="umViewEditUserForm" property="selectedOrgGroupId" onchange="goAction('groupSelected');" style="font-family:verdana;font-size:11px;width:180px;">
                <c:set var="translation">
                  <digi:trn key="aim:viewEditUser:selectOrganisationGroup">
                  --Select organisation group--
                  </digi:trn>
                </c:set>
                <html:option value="-1">${translation}</html:option>
                <c:if test="${!empty umViewEditUserForm.orgGroups}">
                  <html:optionsCollection name="umViewEditUserForm" property="orgGroups" value="ampOrgGrpId" label="orgGrpName"/>
                </c:if>
              </html:select>
            </td>
          </tr>
          <tr>
            <td>
              <digi:trn key="aim:viewEditUser:organisationName">
              Organisation name
              </digi:trn>
            </td>
            <td>
              <html:select name="umViewEditUserForm" property="selectedOrgName" style="font-family:verdana;font-size:11px;width:180px;">
                <c:set var="translation">
                  <digi:trn key="aim:viewEditUser:selectOrganisation">
                  --Select organisation--
                  </digi:trn>
                </c:set>
                <html:option value="-1">${translation}</html:option>
                <c:if test="${!empty umViewEditUserForm.orgs}">
                  <html:optionsCollection name="umViewEditUserForm" property="orgs" value="name" label="name"/>
                </c:if>
              </html:select>
            </td>
          </tr>
          <tr>
            <td>
            &nbsp;
            </td>
          </tr>
          <tr>
            <td>
              <digi:trn key="aim:viewEditUser:languageSettings">
              Language settings
              </digi:trn>
            </td>
            <td>
              <html:select name="umViewEditUserForm" property="selectedLanguageCode" style="font-family:verdana;font-size:11px;width:180px;">
                <c:set var="translation">
                  <digi:trn key="aim:viewEditUser:selectLanguage">
                  --Select language--
                  </digi:trn>
                </c:set>
                <html:option value="-1">${translation}</html:option>
                <c:if test="${!empty umViewEditUserForm.languages}">
                  <html:optionsCollection name="umViewEditUserForm" property="languages" value="code" label="name"/>
                </c:if>
              </html:select>
            </td>
          </tr>
          <tr>
            <td>
            &nbsp;
            </td>
          </tr>
          <tr>
            <td colspan="2" align="center">
              <c:set var="translation">
                <digi:trn key="aim:viewEditUser:saveButton">
                Save
                </digi:trn>
              </c:set>
              <input type="button" value="${translation}" onclick="goAction('save');" style="font-family:verdana;font-size:11px;width:60px;"/>

              <c:set var="translation">
                <digi:trn key="aim:viewEditUser:cancelButton">
                Cancel
                </digi:trn>
              </c:set>
              <input type="button" value="${translation}" onclick="history.back();" style="font-family:verdana;font-size:11px;width:60px;"/>
            </td>
          </tr>
        </table>
        <br />

        <table border="0" class="border" width="40%">

          <tr>
            <c:set var="translation">
              <digi:trn key="aim:viewEditUser:changePasswordButton">
              Change password
              </digi:trn>
            </c:set>

            <td width="10%" align="right"><b><digi:trn key="aim:viewEditUser:password">Password:</digi:trn> </b></td>

            <td width="20%"><html:password name="umViewEditUserForm" property="newPassword" /></td>
          </tr>
          <tr>
            <td width="10%" align="right"><b><digi:trn key="aim:viewEditUser:confirmPassword">Confirm:</digi:trn> </b></td>
            <td width="20%"><html:password name="umViewEditUserForm" property="confirmNewPassword" /></td>
          </tr>
          <tr>
           <c:set var="errMsg">
           <digi:trn key="aim:viewEditUser:errorMessage">
              Either fields are blank or their values do not match
            </digi:trn>
            </c:set>


            <td><input type="button" value="${translation}" onclick="validate('${errMsg}','changePassword');" style="font-family:verdana;font-size:11px;"/></td>
            <td>
              <c:set var="translation">
                <digi:trn key="aim:viewEditUser:resetPasswordButton">
                Reset
                </digi:trn>
              </c:set>
              <input type="button" value="${translation}" onclick="resetPasswordFields()" style="font-family:verdana;font-size:11px;"/></td>
          </tr>

        </table>
</td>
    </tr>
  </table>
</digi:form>
