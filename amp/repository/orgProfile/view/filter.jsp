<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>






<digi:instance property="orgProfOrgProfileFilterForm"/>

<digi:form action="/showOrgProfile.do">


    <table>
        <tr>
            <td><b><digi:trn key="orgProfile:filer:Organization">Organization</digi:trn></b></td>
            <td>
                <html:select property="org" styleClass="inp-text">
                    <html:optionsCollection property="organizations"
                value="ampOrgId" label="name" />
                    
                </html:select>
            </td>
       
            <td ><b><digi:trn key="orgProfile:filer:fiscalCalendar">Fiscal Year</digi:trn></b></td>
            
       
            
            <td align="center">
                <html:select property="year" styleClass="inp-text">
                    <html:optionsCollection property="years" label="wrappedInstance" value="wrappedInstance" />
                </html:select>
            </td>
            
    
     
            <td><b><digi:trn key="orgProfile:filer:Currency">Currency</digi:trn></b></td>
       
            <td>
                <html:select property="currency" styleClass="inp-text">
                    <html:optionsCollection property="currencies"
                value="ampCurrencyId" label="currencyName" />
                    
                </html:select>
            </td>
     
            <td>
                 <html:select property="transactionType" styleClass="inp-text">
                     <html:option value="0">Comm</html:option>
                     <html:option value="1">Disb</html:option>
                </html:select>
         
            </td>
            <td align="center">
                
                <html:submit styleClass="buton" property="apply"><digi:trn key="orgProfile:filer:Apply">Apply</digi:trn></html:submit>&nbsp;
                
              
            </td>
            
        </tr>
    </table>
</digi:form>

