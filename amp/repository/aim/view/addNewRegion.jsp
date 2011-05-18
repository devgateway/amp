<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@ taglib uri="/taglib/category" prefix="category" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />



<script language="JavaScript">

    function validate(string,length) {

        if (string.length > length) {

            return false;

        }

        if (string.length < length) {

            return false;

        }



        var valid="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"



        for (var i=0; i<length; i++) {

            if (valid.indexOf(string.charAt(i)) < 0) {

                return false;

            }

        }



        return true;

    }



    function addLoc() {

        level = document.aimNewAddLocationForm.categoryLevelCountry.value;

        if ((document.aimNewAddLocationForm.name.value.length==0) ||

            (document.aimNewAddLocationForm.name.value==null)) {

            alert('Please enter name for this location.');

            document.aimNewAddLocationForm.name.focus();

            return false;

        }

        if (level=="true") { //is country

            if ((document.aimNewAddLocationForm.iso.value.length==0) ||

                (document.aimNewAddLocationForm.iso.value==null)) {

                alert('Please enter iso code for this country');

                document.aimNewAddLocationForm.iso.focus();

                return false;

            }

            if (validate(document.aimNewAddLocationForm.iso.value, 2) == false){

                alert('Select alphabets only for 2-character iso code');

                return false;

            }



            if ((document.aimNewAddLocationForm.iso3.value.length==0) ||

                (document.aimNewAddLocationForm.iso3.value==null)) {

                alert('Please enter iso3 code for this country');

                document.aimNewAddLocationForm.iso3.focus();

                return false;

            }


            if (validate(document.aimNewAddLocationForm.iso.value, 2) == false){
                alert('Select alphabets only for 2-character iso code');
                return false;

            }

            if (validate(document.aimNewAddLocationForm.iso3.value, 3) == false){
                alert('Select alphabets only for 3-character iso3 code');
                return false;
            }

        }
        else {

            if (isNaN(document.aimNewAddLocationForm.gsLat.value)) {

                alert('Please enter only numerical values into Latitude Field.');

                document.aimNewAddLocationForm.gsLat.focus();

                return false;

            }

            if (isNaN(document.aimNewAddLocationForm.gsLong.value)) {

                alert('Please enter only numerical values into Longitude Field.');

                document.aimNewAddLocationForm.gsLong.focus();

                return false;

            }

        }

        document.aimNewAddLocationForm.event.value = "save";

        document.aimNewAddLocationForm.submit();

    }



    function textCounter(field, countfield, maxlimit) {

        if (field.value.length > maxlimit) // if too long...trim it!

        field.value = field.value.substring(0, maxlimit);

    // otherwise, update 'characters left' counter

    else

    countfield.value = maxlimit - field.value.length;

}



function getIso(code) {

if (code == 2)

window.open("http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2");

else if (code == 3)

window.open("http://en.wikipedia.org/wiki/ISO_3166-1_alpha-3");

else

return false;

}



function move() {

  <digi:context name="selectLoc" property="context/module/moduleinstance/dynLocationManager.do" />

url = "<%= selectLoc%>";

document.aimNewAddLocationForm.event.value = "";

document.aimNewAddLocationForm.action = "<%= selectLoc%>";

document.aimNewAddLocationForm.submit();

}



function load() {

document.aimNewAddLocationForm.name.focus();

}



function unload() {

}



</script>



<!--  AMP Admin Logo -->

<jsp:include page="teamPagesHeader.jsp"  />

<!-- End of Logo -->



<digi:context name="digiContext" property="context"/>

<digi:instance property="aimNewAddLocationForm" />



<digi:form action="/addNewLocation.do" method="post">

  <html:hidden property="event" />
   <html:hidden property="categoryIndex" />
     <html:hidden property="categoryLevelCountry" />
      <html:hidden property="parentLocationId" />
       <html:hidden property="parentCatValId" />



 
    
    
    <table cellPadding=5 cellSpacing=0 width="600">
        
        <tr>
            
            <!-- Start Navigation -->

            <td height=33 width="600"><span class=crumb>
                
                <c:set var="translation">
                    
                    <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                    
                </c:set>
                
                <digi:link href="/admin.do" styleClass="comment" title="${translation}" >
                    
                    <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
                    
                </digi:link>&nbsp;&gt;&nbsp;
                
                <c:set var="translation">
                    
                    <digi:trn key="aim:clickToViewRegionManager">Click here to goto Region Manager</digi:trn>
                    
                </c:set>
                
                <digi:link href="/dynLocationManager.do" styleClass="comment" title="${translation}" >
                    
                    <digi:trn key="aim:regionManager">Region Manager</digi:trn>
                    
                </digi:link>&nbsp;&gt;&nbsp;
                
                <c:if test="${aimNewAddLocationForm.event == 'add'}">
                    
                    <digi:trn key="aim:AmpAddALocation">Add</digi:trn>
                    <c:out value="${aimNewAddLocationForm.parentCatValName}"/>
                    
                </c:if>
                
                <c:if test="${aimNewAddLocationForm.event == 'edit'}">
                    
                    <digi:trn key="aim:AmpEditALocation">Edit</digi:trn>
                    <c:out value="${aimNewAddLocationForm.parentCatValName}"/>
                    
                </c:if>
                
            </td>
            
            <!-- End navigation -->
            
        </tr>
        
        <tr>
            
            <td height=16 vAlign=center width=600 ><span class=subtitle-blue>
                
                <digi:trn key="aim:regionManager">Region Manager</digi:trn>
                
            </td>
            
        </tr>
        
        <tr>
            
            <td noWrap width=600 vAlign="top">
                
                <table width=600 cellspacing=1 cellSpacing=1>
                    
                    <tr><td noWrap width=600 vAlign="top">
                            
                            <table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
                                
                                <tr bgColor=#f4f4f2>
                                    
                                    <td vAlign="top" width="100%">
                                        
                                        &nbsp;
                                        
                                    </td>
                                    
                                </tr>
                                
                                <tr bgColor=#f4f4f2>
                                    
                                    <td valign="top">
                                        
                                        <table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%" border=0>
                                            
                                            <tr>
                                                
                                                <td bgColor=#ffffff class=box-border>
                                                    
                                                    <table border=0 cellPadding=1 cellSpacing=1 class=box-border width="100%">
                                                        
                                                        <!-- Page Logic -->

                                                        <tr>
                                                            
                                                            <td><digi:errors /></td>
                                                            
                                                        </tr>
                                                        
                                                        <tr>
                                                            
                                                            <td>
                                                                
                                                                <table width="100%" border="0" cellspacing="4">
                                                                    
                                                                    <tr>
                                                                        <c:set var="locName">
                                                                            <digi:trn key="aim:addLocation:Name">
                                                                                Name
                                                                            </digi:trn>
                                                                            
                                                                            
                                                                        </c:set>
                                                                        
                                                                        <td width="30%" align="right"><FONT color=red>* </FONT><c:out value="${locName}"/></td>
                                                                        <td width="70%" align="left">
                                                                            
                                                                            <html:text property="name" size="25"
                                                                                       
                                                                                       onkeydown="textCounter(this.form.name,this.form.cname,200);"
                                                                                       
                                                                                       onkeyup="textCounter(this.form.name,this.form.cname,200);" />
                                                                                       
                                                                            <input readonly type="text" name="cname" size="3" maxlength="4" value="">
                                                                            
                                                                            <digi:trn key="aim:AmpAddLocCharleft">characters left</digi:trn>
                                                                            
                                                                        </td>
                                                                        
                                                                    </tr>
                                                                    
                                                                    <c:choose>
                                                                        
                                                                        <c:when test="${aimNewAddLocationForm.categoryLevelCountry}">
                                                                        
                                                                            <tr>
                                                                                
                                                                                <td width="30%" align="right"><FONT color=red>* </FONT>ISO</td>
                                                                                <td width="70%" align="left">
                                                                                    
                                                                                    <html:text property="iso" size="5" maxlength="2" />
                                                                                    
                                                                                    <br>
                                                                                    
                                                                                    <a href="javascript:getIso(2)">
                                                                                        
                                                                                    <digi:trn key="aim:isoCodeList">ISO code list</digi:trn></a>
                                                                                    
                                                                                </td>
                                                                                
                                                                            </tr>
                                                                            
                                                                            <tr>
                                                                                
                                                                                <td width="30%" align="right"><FONT color=red>* </FONT>ISO3</td>
                                                                                <td width="70%" align="left">
                                                                                    
                                                                                    <html:text property="iso3" size="5" maxlength="3" />
                                                                                    
                                                                                    <br>
                                                                                    
                                                                                    <a href="javascript:getIso(3)">
                                                                                        
                                                                                    <digi:trn key="aim:iso3CodeList">ISO3 code list</digi:trn></a>
                                                                                    
                                                                                </td>
                                                                                
                                                                            </tr>
                                                                              <tr>
                                                                                
                                                                                <td width="30%" align="right"><digi:trn key="aim:lcCode">Code</digi:trn></td>
                                                                                <td width="70%" align="left"><html:text property="code"
                                                                                                                        size="10" /></td>
                                                                            </tr>
                                                                            
                                                                            
                                                                            <tr>
                                                                                
                                                                                <td width="30%" align="right"></td>
                                                                                <td width="70%" align="left">
                                                                                    
                                                                                    <digi:trn key="aim:allMarkedRequiredField">
                                                                                        All fields marked with an <FONT color=red><B><BIG>*</BIG>
                                                                                        </B></FONT> are required.</digi:trn>
                                                                                    </td>
                                                                                    
                                                                                </tr>
                                                                            
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <tr>
                                                                                
                                                                                <td width="30%" align="right"><digi:trn key="aim:lcCode">Code</digi:trn></td>
                                                                                <td width="70%" align="left"><html:text property="code"
                                                                                                                        size="10" /></td>
                                                                            </tr>
                                                                            
                                                                            <tr>
                                                                                
                                                                                <td width="30%" align="right"><digi:trn key="aim:lcLatitude">Latitude</digi:trn></td>
                                                                                <td width="70%" align="left"><html:text property="gsLat"
                                                                                                                        size="10" /></td>
                                                                            </tr>
                                                                            
                                                                            <tr>
                                                                                
                                                                                <td width="30%" align="right"><digi:trn key="aim:lcLongitude">Longitude</digi:trn></td>
                                                                                <td width="70%" align="left"><html:text property="gsLong"
                                                                                                                        size="10" /></td>
                                                                            </tr>
                                                                            
                                                                            <tr>
                                                                                
                                                                                <td width="30%" align="right"><digi:trn key="aim:lcGeoCode">Geo Code</digi:trn></td>
                                                                                <td width="70%" align="left"><html:text
                                                                                    property="geoCode" size="10" /></td>
                                                                            </tr>
                                                                            
                                                                            <tr>
                                                                                <td width="30%" align="right"><digi:trn key="aim:lcDescription">Description</digi:trn></td>
                                                                                <td width="70%" align="left"><html:textarea
                                                                                    property="description" cols="40" rows="3" /></td>
                                                                            </tr>
                                                                        </c:otherwise>
                                                                        
                                                                    </c:choose>
                                                                    
                                                                    
                                                                    
                                                                    <tr>
                                                                        
                                                                        <td width=30% align=right>
                                                                        </td>
                                                                        <td width=70% align=left>
                                                                            <font color=red><digi:trn key="aim:statusMandatoryFields">* Mandatory fields</digi:trn></font>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td colspan="2" width="60%">
                                                                            
                                                                            <table width="100%" cellspacing="5">
                                                                                
                                                                                <tr>
                                                                                    
                                                                                    <td width="45%" align="right">
                                                                                        
                                                                                        <c:set var="translation">
                                                                                            <digi:trn key="btn:regionManagerSave">
                                                                                                Save
                                                                                            </digi:trn>
                                                                                        </c:set>
                                                                                        
                                                                                    <input type="button" value="${translation}" class="dr-menu" onclick="addLoc()"></td>
                                                                                    
                                                                                    <td width="8%" align="left">
                                                                                        
                                                                                        <c:set var="translation">
                                                                                            <digi:trn key="btn:regionManagerReset">
                                                                                                Reset
                                                                                            </digi:trn>
                                                                                        </c:set>
                                                                                        
                                                                                    <input type="reset" value="${translation}" class="dr-menu"></td>
                                                                                    
                                                                                    <td width="45%" align="left">
                                                                                        
                                                                                        <c:set var="translation">
                                                                                            <digi:trn key="btn:regionManagerCancel">
                                                                                                Cancel
                                                                                            </digi:trn>
                                                                                        </c:set>
                                                                                        
                                                                                    <input type="button" value="${translation}" class="dr-menu" onclick="move()"></td>
                                                                                    
                                                                                </tr>
                                                                                
                                                                            </table>
                                                                            
                                                                        </td>
                                                                        
                                                                    </tr>
                                                                    
                                                                </table>
                                                                
                                                            </td>
                                                            
                                                        </tr>
                                                        
                                                        <!-- end page logic -->
                                                        
                                                    </table>
                                                    
                                                </td>
                                                
                                            </tr>
                                            
                                        </table>
                                        
                                    </td>
                                    
                                </tr>
                                
                                <tr><td bgColor=#f4f4f2>
                                        
                                        &nbsp;
                                        
                                </td></tr>
                                
                            </table>
                            
                        </td>
                        
                    </tr>
                    
                </table>
                
            </td>
            
        </tr>
        
    </table>
    
</digi:form>



