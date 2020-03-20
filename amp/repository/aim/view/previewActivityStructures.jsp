<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<module:display name="/Activity Form/Structures" parentModule="/Activity Form">
<c:set var="maxImagesCount" value="6"></c:set>
<style type="text/css">

	.thumbnail{
	position: relative;
	z-index: 0;
	}
	
	.thumbnail:hover{
	background-color: transparent;
	z-index: 50;
	}
	
	.thumbnail span{ /*CSS for enlarged image*/
	position: absolute;
	background-color: lightyellow;
	padding: 5px;
	left: -1000px;
	border: 1px dashed gray;
	visibility: hidden;
	color: black;
	text-decoration: none;
	}
	
	.thumbnail span img{ /*CSS for enlarged image*/
	border-width: 0;
	padding: 2px;
	}
	
	.thumbnail:hover span{ /*CSS for enlarged image on hover*/
	visibility: visible;
	top: -40;
	left: 10px; /*position for enlarged image */
	
	}

</style>
<script type="text/javascript">

YAHOO.util.Event.onDOMReady(startStructureSection);

function startStructureSection(){
	$("#structureslink").click(function() {
		  $("div#structuresdiv").toggle('slow', function() {
		  });
	});
	loadImages();
}

var maxNumDisplayedImgs = parseInt("${maxImagesCount}");
var lastImgIdx = maxNumDisplayedImgs - 1;

function loadImages(){
	
	var imgUrl = "/aim/displayStructureImage.do?";
	var urlParams = "";
	var imgDomId = "";
	var idx = 0;

	<logic:iterate id="structure" name="aimEditActivityForm" property="structures">
		var imgCount = parseInt("${fn:length(structure.images)}");
		$("#aPrev${structure.ampStructureId}").hide();
		if (imgCount <= maxNumDisplayedImgs){
			$("#aNext${structure.ampStructureId}").hide();
		}
		
		<logic:iterate id="image" name="structure" property="images">
			urlParams = "structureId=${structure.ampStructureId}&imgId=${image.id}";
			imgDomId = "${structure.ampStructureId}_${image.id}";
	        $('#'+imgDomId ).attr("src", imgUrl + urlParams);
	        $('#zoomImg' + imgDomId).attr("src", imgUrl + urlParams);
	        
	        if(idx >= maxNumDisplayedImgs) {
		        $('#a${structure.ampStructureId}_'+ idx ).hide();
	        }
	        
	        idx++;
		</logic:iterate>
	</logic:iterate>
	
}

function moveDisplayedImages(structureId, imgCount, next){
	var maxIdx = imgCount - 1;
	var firstImgIdx = lastImgIdx - (maxNumDisplayedImgs - 1);
	
	if(next){
		if((lastImgIdx + 1) == maxIdx){
			$("#aNext${structure.ampStructureId}").hide();
		}else{
			$("#aNext${structure.ampStructureId}").show();
		}
		$("#aPrev${structure.ampStructureId}").show();
		lastImgIdx = lastImgIdx + 1;
		$('#a'+ structureId + "_" +  firstImgIdx ).hide();	
		$('#a'+ structureId + "_" + lastImgIdx ).show();
	}else{
		if(firstImgIdx == 1){
			$("#aPrev${structure.ampStructureId}").hide();
		}else{
			$("#aPrev${structure.ampStructureId}").show();
		}
		$("#aNext${structure.ampStructureId}").show();
		$('#a'+ structureId + "_" + (firstImgIdx - 1)).show();
		$('#a'+ structureId + "_" + lastImgIdx ).hide();
		lastImgIdx = lastImgIdx - 1;
	};
	
}

</script>

	<fieldset>
		<legend>
			<span class=legend_label id="structureslink" style="cursor: pointer;">
				<digi:trn>Structures</digi:trn>&nbsp; 
			</span>		
		</legend>
		
		<div id="structuresdiv" class="toggleDiv">

		<logic:iterate id="structure" name="aimEditActivityForm"
			property="structures">
			<table style="cellspacing:1; cellPadding:3; width:100%;">
				<tr bgcolor="#f0f0f0">
					<td colspan="2" align="center">
						<b>
								${fn:escapeXml(structure.title)}
						</b>
					</td>
				</tr>
				
				<module:display
					name="/Activity Form/Structures/Structure Type"
					parentModule="/Activity Form/Structures">
					<tr>
						<td bgcolor="#f0f0f0" style="padding-left:5px;" width="15%"><digi:trn key="trn:type">Type</digi:trn></td>
						<td align="left"><b> ${structure.type.name} </b></td>
					</tr>
				</module:display>
				<module:display
					name="/Activity Form/Structures/Structure Title"
					parentModule="/Activity Form/Structures">
					<tr>
						<td bgcolor="#f0f0f0" style="padding-left:5px;"><digi:trn key="trn:title">Title</digi:trn></td>
						<td align="left"> <b> ${fn:escapeXml(structure.title)} </b></td>
					</tr>
				</module:display>
				<module:display
					name="/Activity Form/Structures/Structure Description"
					parentModule="/Activity Form/Structures">
					<tr>
						<td bgcolor="#f0f0f0" style="padding-left:5px;"><digi:trn key="trn:description">Description</digi:trn></td>
						<td align="left"><b> ${fn:escapeXml(structure.description)} </b></td>
					</tr>
				</module:display>
				<module:display
					name="/Activity Form/Structures/Structure Latitude"
					parentModule="/Activity Form/Structures">
					<c:if test="${not empty structure.latitude}">
					<tr>
						<td bgcolor="#f0f0f0" style="padding-left:5px;"><digi:trn key="trn:latitude">Latitude</digi:trn></td>
						<td align="left"> <b> ${structure.latitude} </b></td>
					</tr>
					</c:if>
				</module:display>
				<module:display
					name="/Activity Form/Structures/Structure Longitude"
					parentModule="/Activity Form/Structures">
					<c:if test="${not empty structure.longitude}">
					<tr>
						<td bgcolor="#f0f0f0" style="padding-left:5px;"><digi:trn key="trn:longitude">Longitude</digi:trn></td>
						<td align="left"><b> ${structure.longitude} </b></td>
					</tr>
					</c:if>
				</module:display>
				<c:if test="${not empty structure.coordinates}">
					<tr>
						<td bgcolor="#f0f0f0" valign="top" style="padding-left:5px;"><digi:trn
						>Coordinates</digi:trn></td>
						<td>
							<table>
								<logic:iterate id="coordinate" name="structure" property="coordinates"
											   type="org.digijava.module.aim.dbentity.AmpStructureCoordinate">
									<tr>
										<td><b> ${coordinate.latitude}</b></td>
										<td><b> ${coordinate.longitude}</b></td>
									</tr>
								</logic:iterate>
							</table>
						</td>
					</tr>
				</c:if>
			
			<c:if test="${not empty structure.images}">
				<tr bgcolor="#f0f0f0">
					<td align="center"colspan="2">
						<digi:trn key="trn:images">Images</digi:trn>
					</td>
				</tr>
				<tr>
					<td class="inside" colspan="2">
						<div align="center" style="vertical-align: middle;">
						<img id="aPrev${structure.ampStructureId}" 
						onclick="moveDisplayedImages(${structure.ampStructureId},${fn:length(structure.images)}, false);" 
						src="/TEMPLATE/ampTemplate/img_2/ico_arr_left.gif" title="<digi:trn>Previous</digi:trn>" align="middle">
						<img id="aNext${structure.ampStructureId}" 
						onclick="moveDisplayedImages(${structure.ampStructureId},${fn:length(structure.images)}, true);" 
						src="/TEMPLATE/ampTemplate/img_2/ico_arr_right.gif" title="<digi:trn>Next</digi:trn>" align="middle">
						</div>
						<br />
						<div align="center" style="vertical-align: middle;">
						<logic:iterate id="image" name="structure" property="images" indexId="idx">
									<a id="a${structure.ampStructureId}_${idx}" class="thumbnail" target="_blank" href="/aim/displayStructureImage.do?structureId=${structure.ampStructureId}&imgId=${image.id}">
										<img id="${structure.ampStructureId}_${image.id}" width="75px" height="75px" border="0" />
										<span><img id="zoomImg${structure.ampStructureId}_${image.id}" width="150px" height="150px"/>
										<br /><digi:trn>View Image</digi:trn></span></a>
	
						</logic:iterate>
						</div>
						
					</td>
				</tr>
			</c:if>
	
	</table>
	<br />
	<hr>
	</logic:iterate>
	</div>
	</fieldset>
</module:display>


<%--
<!-- STRUCTURES SECTION -->
<fieldset>
	<legend>
		<span class=legend_label id="structureslink" style="cursor: pointer;">
			<digi:trn>Structures</digi:trn>
		</span>	
	</legend>
	<div id="structuresdiv" >
		<c:if test="${not empty aimEditActivityForm.structures.actStructures}">
		
			
			<c:forEach var="compo" items="${aimEditActivityForm.structures.actStructures}">
			<table width="100%" style="font-size:10px;">
				<tr>
					<td align="left" width="15%"><b><digi:trn>Type</digi:trn></b></td>
					<td align="left">${compo.type.name}</td>
				</tr>
				<tr>
					<td align="left" width="15%"><b><digi:trn>Title</digi:trn></b></td>
					<td align="left">${compo.title}</td>
				</tr>
				<tr>
					<td align="left" width="15%"><b><digi:trn>Description</digi:trn></b></td>
					<td align="left">${compo.description}</td>
				</tr>
				<tr>
					<td align="left" width="15%"><b><digi:trn>Latitude</digi:trn></b></td>
					<td align="left">${compo.latitude}</td>
				</tr>
				<tr>
					<td align="left" width="15%"><b><digi:trn>Longitude</digi:trn></b></td>
					<td align="left">${compo.longitude}</td>
				</tr>
			</table>
			
			<hr>
			
			</c:forEach>
		
	</c:if>
	</div>
</fieldset>

<!-- END STURCTURES SECTION -->


 --%>
