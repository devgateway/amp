<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/category" prefix="category"%>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>

<digi:instance property="aimEditActivityForm" />

<c:if test="${!empty funding.mtefProjections}">
    <tr bgcolor="#FFFFCC">
        <td colspan="4" style="text-transform: uppercase">
            <a><digi:trn key="aim:plannedexpenditures">MTEF Projections</digi:trn></a>
        </td>
    </tr>

    <logic:iterate name="funding" property="mtefProjections"
        id="projection" type="org.digijava.module.aim.helper.MTEFProjection">
         <tr bgcolor="#ffffff">
            <td align="right" bgcolor="#FFFFFF">
                <module:display name="/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections/MTEF Projections Table/MTEF Projection"
                       parentModule="/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections/MTEF Projections Table">
                    <c:set var="PROJECTION_ID"><%= org.digijava.module.aim.helper.MTEFProjection.PROJECTION_ID %></c:set>
                    <c:set var="PIPELINE_ID"><%= org.digijava.module.aim.helper.MTEFProjection.PIPELINE_ID %></c:set>
                    <c:choose>
                        <c:when test="${PROJECTION_ID eq projection.projected}">
                            <b><digi:trn>Projected</digi:trn></b>
                        </c:when>
                        <c:when test="${PIPELINE_ID eq projection.projected}">
                            <b><digi:trn>Pipeline</digi:trn></b>
                        </c:when>
                    </c:choose>
                </module:display>
             </td>

             <td align="right">
                 <module:display name="/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections/MTEF Projections Table/Projection Date"
                                 parentModule="/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections/MTEF Projections Table">
                     <b>${projection.projectionDate}</b>
                 </module:display>&nbsp;
             </td>

             <td align="right" bgcolor="#FFFFFF">
                <module:display name="/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections/MTEF Projections Table/Amount"
                                parentModule="/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections/MTEF Projections Table">
                    <b>${projection.amount}</b>&nbsp;
                </module:display>
                <module:display name="/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections/MTEF Projections Table/Currency"
                                parentModule="/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections/MTEF Projections Table">
                    <b>${projection.currencyCode}</b>
                </module:display>&nbsp;
             </td>
         </tr>
    </logic:iterate>

</c:if>

<tr>
    <td colspan="4" height="7px"></td>
</tr>



