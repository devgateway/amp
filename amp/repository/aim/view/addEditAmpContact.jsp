<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<digi:context name="digiContext" property="context"/>
<digi:instance property="addressbookForm"/>

<script type="text/javascript">

	function checkNumber(phoneOrFaxId){
		var phoneOrFax=document.getElementById(phoneOrFaxId);
	 	var number=phoneOrFax.value;
	 	var validChars= "0123456789()+ ";
	 	for (var i = 0;  i < number.length;  i++) {
	  		var ch = number.charAt(i);
	  		if (validChars.indexOf(ch)==-1){
	   			alert('enter correct number');
	   			phoneOrFax.value=number.substring(0,i);
	   			return false;	   
	  		}
	 	}	 
	 return true;
	}

	function saveContact(){
		if(validateInfo()){
		    <digi:context name="addCont" property="context/addressBook.do?actionType=saveContact"/>
		    document.addressbookForm.action = "<%= addCont %>";
		    document.addressbookForm.target = "_self";
		    document.addressbookForm.submit();
		}
	}

	function validateInfo(){
		if(document.getElementById('name').value==null || document.getElementById('name').value==''){
			alert('Please Enter Name');
			return false;
		}
		if(document.getElementById('lastname').value==null || document.getElementById('lastname').value==''){
			alert('Please Enter lastname');
			return false;
		}
		if(document.getElementById('email').value==null || document.getElementById('email').value==''){
			alert('Please Enter email');
			return false;
		}else if(document.getElementById('email').value.indexOf('@')==-1){
			alert('Please Enter Correct Email');
			return false;
		}
		if (checkNumber('phone')==false){
			return false;
		}
		if(checkNumber('fax')==false){
			return false;
		}		
		return true;
	}
</script>

<digi:form action="/addressBook.do?actionType=saveContact" method="post">	
	<table bgColor=#ffffff cellPadding=5 cellSpacing=1 >
		<tr>
			<td class=r-dotted-lg width=14>&nbsp;</td>
			<td align=left class=r-dotted-lg vAlign=top width=752>
				<table bgcolor="#ffffff" cellPadding=5 cellSpacing=0 width="100%">
					<tr>
						<!-- Start Navigation -->
						<td height=33>
							<span class=crumb>
					           <c:set var="translation">
									<digi:trn>Click here to view MyDesktop</digi:trn>
								</c:set>
								<digi:link href="/showDesktop.do" styleClass="comment" title="${translation}">
									<digi:trn>Portfolio</digi:trn>
								</digi:link>&nbsp;&gt;&nbsp;
								<digi:link href="/addressBook.do?actionType=viewAddressBook" styleClass="comment" title="${translation}">
									<digi:trn>Address Book</digi:trn>
								</digi:link>							
								&nbsp;&gt;&nbsp;
								<digi:trn>Add/Edit Contact</digi:trn>
				              </span>
						</td>
						<!-- End navigation -->
					</tr>				
					<tr>
						<td><digi:trn>All fields marked with <font size="2" color="#FF0000">*</font> are required.</digi:trn></td>
					</tr>
					<tr>
						<td><digi:errors /></td>
					</tr>
					<tr>
						<td noWrap width=100% vAlign="top">
							<table bgcolor="#ffffff" border="0" >
								<tr>
									<td>
										<table>
											<tr>
												<td noWrap width=690 vAlign="top">
													<table bgColor="#ffffff" cellPadding="0" cellSpacing="0"class="box-border-nopadding" width="100%">
														<tr bgColor="#f4f4f2">
															<td vAlign="top" width="100%">&nbsp;</td>
														</tr>
														<tr bgColor="#ffffff">
															<td valign="top">
																<table align="center" bgColor="#f4f4f2" cellPadding="0"	cellSpacing="0" border="0">
																	<tr>
																		<td bgColor="#ffffff" class="box-border" width="680">
																			<table border="0" class="box-border" width="100%">
																				<tr bgColor="#dddddb">
																					<td bgColor="#dddddb" height="20" align="center"colspan="5">
																						<digi:trn>Add/Edit Contact</digi:trn>
																					</td>
																				</tr>
																				<!-- Page Logic -->
																				<tr>
																					<td width="100%">
																						<table border="0" bgColor="#f4f4f2">
																							<tr height="5"><td>&nbsp;</td></tr>
																							<tr>
																								<td rowspan="9" width="30%"/>
																								<td align="right"><strong><digi:trn>Firstname</digi:trn></strong><font color="red">*</font></td>
																								<td align="left"><html:text property="name" styleId="name"/></td>
																							</tr>
																							<tr>
																								<td align="right"><strong><digi:trn>Lastname</digi:trn></strong><font color="red">*</font></td>
																								<td align="left"><html:text property="lastname" styleId="lastname"/></td>
																							</tr>
																							<tr>
																								<td align="right"><strong><digi:trn>Email</digi:trn></strong><font color="red">*</font></td>
																								<td align="left"><html:text property="email" styleId="email"/></td>
																							</tr>
																							<tr>
																								<td align="right"><strong><digi:trn>Title</digi:trn></strong> </td>
																								<td align="left"><html:text property="title"/></td>
																							</tr>
																							<tr>
																								<td align="right"><strong><digi:trn>Organization</digi:trn></strong></td>
																								<td align="left"><html:text property="organisationName"/></td>
																							</tr>
																							<tr>
																								<td align="right"><strong><digi:trn>Phone Number</digi:trn></strong></td>
																								<td align="left"><html:text property="phone" styleId="phone" onkeyup="checkNumber('phone')"/></td>
																							</tr>
																							<tr>
																								<td align="right"><strong><digi:trn>Fax</digi:trn></strong></td>
																								<td align="left"><html:text property="fax" styleId="fax" onkeyup="checkNumber('fax')"/></td>
																							</tr>		
																							<tr height="5px"><td colspan="2"/></tr>
																							<tr>
																								<td colspan="4" align="center"><html:button property="" styleClass="dr-menu" onclick="saveContact()">Save</html:button> </td>			
																							</tr>																
																						</table>
																					</td>
																				</tr>
																				<!-- end page logic -->
																			</table>
																		</td>
																	</tr>
																	<tr>
																		<td bgColor=#f4f4f2>&nbsp;</td>
																	</tr>
																</table>
															</td>
															<td noWrap width=10 vAlign="top"></td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>	
</digi:form>