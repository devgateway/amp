Wicket.Event.add(window, "domready", function(event){
    //setupFileUpload('#${componentMarkupId}', '${url}', '${paramName}');
});

function setupFileUpload(componentId, componentUrl, componentParamName){
    $(function () {
    	if($(componentId).fileupload===undefined)return;
        $(componentId).fileupload({
            url: componentUrl,
            paramName: componentParamName,
            singleFileUploads: true,
            iframe:true,
            dataType:'json',
            minFileSize: 1,
            maxFileSize: 20000000,
            add: function (e, data) {
            	if (data.files[0].size > parseFloat('${uploadMaxFileSize}')) {
            		alert('${uploadFailedTooBigMsg}');
            		$('#uploadLabel').text('${uploadNoFileLabel}');
            		$(this).find('[role=fileUploadedMsg]').html('');
                    $(this).find('[role=fileUploadedMsg]').hide();
            	} else {
	            	$(this).find('[role=fileUploadedMsg]').show();
	                $(this).find('[role=fileUploadedMsg]').html(" '" + '${uploadStartedMsg}' + data.files[0].size + "' bytes");            	
	                data.submit();
            	}
            },
            done: function (e, data){
            	var result = eval(data.result)[0];
                $(this).find('[role=fileUploadedMsg]').html(result.uploadTxt);
            },
            fail: function (e, data){
                alert('${uploadFailedMsg}');
                $('#uploadLabel').text('${uploadNoFileLabel}');
                $(this).find('[role=fileUploadedMsg]').html('');
                $(this).find('[role=fileUploadedMsg]').hide();
            }
        });
    });
}

