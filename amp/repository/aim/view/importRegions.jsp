<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ page import="java.util.Map"%>
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="ampModule/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="ampModule/aim/scripts/dscript120_ar_style.js"/>"></script>

<!-- this is for the nice tooltip widgets -->
<DIV id="TipLayer"
	style="visibility: hidden; position: absolute; z-index: 1000; top: -100;"></DIV>


	


<style type="text/css">
<!--
div.fileinputs {
	position: relative;
	height: 30px;
	width: 300px;
}

input.file {
	width: 300px;
	margin: 0;
}

input.file.hidden {
	position: relative;
	text-align: right;
	-moz-opacity: 0;
	filter: alpha(opacity :   0);
	width: 300px;
	opacity: 0;
	z-index: 2;
}

div.fakefile {
	position: absolute;
	top: 0px;
	left: 0px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}

div.fakefile input {
	margin-bottom: 5px;
	margin-left: 0;
	width: 217px;
}

div.fakefile2 {
	position: absolute;
	top: 0px;
	left: 217px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}

div.fakefile2 input {
	width: 83px;
}
-->
</style>

<script type="text/javascript">
	function showOrHideKeywordsDiv(show) {
		if (show) {
			document.getElementById('textDiv').style.display = 'block';
			document.getElementById('keywordsDiv').style.display = 'block';
		} else {
			document.getElementById('textDiv').style.display = 'none';
			document.getElementById('keywordsDiv').style.display = 'none';
		}
	}
	function checkSelectedLanguages() {
		var submit = true;
		if ($("select[name='exportFormat'] option:selected").val() == 2) {
			var selected = $("input[name='selectedLanguages']:checked");
			var num = selected.length;
			var englishSelected = false;
			selected.each(function() {
				if ($(this).val() == 'en') {
					englishSelected = true;
				}

			});
			if (englishSelected) {
				if (num > 2 || num == 1) {
					submit = false;
				}
			} else {
				submit = false;
			}
		}
		if (!submit) {
			alert("<digi:trn>Please select only one language in addition to english language</digi:trn>")
		}
		return submit;
	}
</script>
<div  class="admin-content">
<digi:instance property="aimDynLocationManagerForm" />
<digi:context name="digiContext" property="context" />
<!--  AMP Admin Logo -->
<!-- End of Logo -->

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="1000"
	align="center">
	<tr>
		<td align=left valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%" border="0">
				<tr>
					<!-- Start Navigation -->
					<td colspan="2" height=33 bgcolor=#F2F2F2><span class=crumb
						style="color: #376091;"> <c:set var="translation">
								<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
							</c:set> <digi:link href="/admin.do" title="${translation}" ampModule="aim">
								<digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
							</digi:link>&nbsp;&gt;&nbsp; <span style="color: #000000"><digi:trn>Region Manager</digi:trn></span></td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height="16" colspan="2" vAlign="middle" align="center"><span
						style="font-size: 12px; color: #000000;"> <b><digi:trn>Region Manager</digi:trn></b>
						<hr />
					</span></td>
				</tr>
				<tr>
					<td height="16" vAlign="middle" colspan="2"><digi:errors /></td>
				</tr>
				<tr>
					<td noWrap width="95%" vAlign="top">
						<table cellspacing="1" cellspacing="1" border="0" align="center">


							<tr>
								<td><br />
								<br />
								<br /></td>
							</tr>

							<digi:form action="/importIntoDynLocationManagerXSL.do" method="post"
								enctype="multipart/form-data">
								<tr>
									<td align="left">
										<!-- <html:file property="fileUploaded"></html:file> -->
										<div class="fileinputs">
											<!-- We must use this trick so we can translate the Browse button. AMP-1786 -->
											<!-- CSS content must be put in a separated file and a class must be generated -->
											<input id="fileUploaded" name="fileUploaded" type="file"
												class="file">
										</div>
									</td>
								</tr>
								<tr>
									<td><html:select property="option">
											<option value="1">
												<digi:trn>Overwrite</digi:trn>
											</option>
											<option value="2">
												<digi:trn>Only New</digi:trn>
											</option>
									</html:select></td>
								</tr>
								<tr>
									<td class="button-section-padding"><c:set var="translation">
											<digi:trn>Import</digi:trn>
										</c:set> <html:submit style="dr-menu" value="${translation}"
											property="import" /></td>
								</tr>

							</digi:form>
						</table>
					</td>
					<td style="vertical-align: top;">
						<table align="center" cellpadding="0" cellspacing="0" width="90%"
							border="0">
							<tr>
								<td>
									<!-- Other Links -->
									<table cellpadding="0" cellspacing="0" width="120">
										<tr>
											<td bgColor=#c9c9c7><b
												style="font-size: 12px; padding-left: 5px;"> <digi:trn
														key="aim:otherLinks">
													Other links
													</digi:trn>
											</b></td>
											<td class="header-corner" height="17"
												width=17></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff>
									<table cellPadding=5 cellspacing="1" width="100%"
										class="inside">
										<tr>
											<td class="inside"><digi:link
													href="/dynLocationManager.do">
													<digi:trn>Region Manager</digi:trn>
												</digi:link></td>
										</tr>
										<tr>
											<!--<td>
													<digi:img src="ampModule/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>-->
											<td class="inside"><digi:link href="/admin.do"
													ampModule="aim">
													<digi:trn key="aim:AmpAdminHome">
													Admin Home
													</digi:trn>
												</digi:link></td>
										</tr>
										<!-- end of other links -->
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
<script  type="text/javascript" src="<digi:file src="ampModule/aim/scripts/fileUpload.js"/>"></script>
   	
<script type="text/javascript">
	initFileUploads('<digi:trn jsFriendly="true" key="aim:browse">Browse...</digi:trn>');
</script>
</div>




