Wicket.Event.add(window, "domready", function(event){
    //setupFileUpload('#${componentMarkupId}', '${url}', '${paramName}');
});

$.getScript("/TEMPLATE/ampTemplate/script/common/FileTypeValidator.js");

function setupFileUpload(componentId, componentUrl, componentParamName, deferUpload, importButtonMarkupId){
    $(function () {
        var pendingUploadData = null;
        var waitForUpload = false;
        var uploadInProgress = false;
        var allowImportClick = false;
        var importButton = importButtonMarkupId ? document.getElementById(importButtonMarkupId) : null;
        var fileInput = $(componentId).find('input[type=file]').get(0);

        function submitPendingUpload() {
            var uploadData = pendingUploadData;
            pendingUploadData = null;
            if (uploadData) {
                uploadInProgress = true;
                uploadData.submit();
            }
        }

        if (deferUpload && importButton && fileInput) {
            $(componentId).find('.fileupload-progress, .fileupload-loading').hide();
            if (importButton.ampDeferredUploadClickHandler) {
                importButton.removeEventListener('click', importButton.ampDeferredUploadClickHandler, true);
            }
            importButton.ampDeferredUploadClickHandler = function (event) {
                if (allowImportClick) {
                    allowImportClick = false;
                    return;
                }

                event.preventDefault();
                event.stopImmediatePropagation();
                if (uploadInProgress) {
                    return;
                }
                waitForUpload = true;
                if (pendingUploadData) {
                    submitPendingUpload();
                } else {
                    fileInput.click();
                }
            };
            importButton.addEventListener('click', importButton.ampDeferredUploadClickHandler, true);
        }

        $(componentId).fileupload({
            url: componentUrl,
            paramName: componentParamName,
            singleFileUploads: true,
            iframe:true,
            dataType:'json',
            minFileSize: 1,
            maxFileSize: 20000000,
            add: function (e, data) {
            	try {
	            	if (!FileTypeValidator.isValid(data.files[0].name)) {
	            		alert(FileTypeValidator.errorMessage);
	            		$('#uploadLabel').text(FileTypeValidator.errorMessage);
	            		$(this).find('[role=fileUploadedMsg]').html('');
	                    $(this).find('[role=fileUploadedMsg]').hide();
	            	} else if (data.files[0].size > parseFloat("${uploadMaxFileSize}")) {
	            		alert("${uploadFailedTooBigMsg}");
	            		$('#uploadLabel').text("${uploadNoFileLabel}");
	            		$(this).find('[role=fileUploadedMsg]').html('');
	                    $(this).find('[role=fileUploadedMsg]').hide();
	            	} else {
		            	$(this).find('[role=fileUploadedMsg]').show();
		                $(this).find('[role=fileUploadedMsg]').html(" \"" + "${uploadStartedMsg}" + data.files[0].size + "\" bytes");            	
                        if (deferUpload) {
                            pendingUploadData = data;
                            if (waitForUpload) {
                                submitPendingUpload();
                            }
                        } else {
                            data.submit();
                        }
	            	}
            	} catch(err) {
            		alert(FileTypeValidator.errorMessage);
            		$('#uploadLabel').text(FileTypeValidator.errorMessage);
            		$(this).find('[role=fileUploadedMsg]').html('');
                    $(this).find('[role=fileUploadedMsg]').hide();
            	}
            },
            done: function (e, data){
                //alert('upload done! result[' + JSON.stringify(data.result) + '] status[' + data.textStatus + '] jqXHR[' + JSON.stringify(data.jqXHR) +']');
            	var result = eval(data.result)[0];
                $(this).find('[role=fileUploadedMsg]').html(result.uploadTxt);
                if (deferUpload && waitForUpload && importButton) {
                    waitForUpload = false;
                    uploadInProgress = false;
                    allowImportClick = true;
                    importButton.click();
                }
            },
            fail: function (e, data){
                //alert('upload failed! result[' + JSON.stringify(data.result) + '] status[' + data.textStatus + '] jqXHR[' + JSON.stringify(data.jqXHR) +']');
                alert("${uploadFailedMsg}");
                $('#uploadLabel').text("${uploadNoFileLabel}");
                $(this).find('[role=fileUploadedMsg]').html('');
                $(this).find('[role=fileUploadedMsg]').hide();
                waitForUpload = false;
                uploadInProgress = false;
                pendingUploadData = null;
            }
        });
    });
}

