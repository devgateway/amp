Wicket.Event.add(window, "domready", function(event){
    setupFileUpload();
});

function setupFileUpload(){
    $(function () {
        $('#${componentMarkupId}').fileupload({
            url: '${url}',
            paramName: '${paramName}',
            singleFileUploads: true,
            minFileSize: 1,
            maxFileSize: 20000000,
            add: function (e, data) {
                //$.each(data.files, function (index, file) {
                //    alert('Added file: ' + file.name);
                //});
                $(this).find('[role=fileUploadedMsg]').show();
                $(this).find('[role=fileUploadedMsg]').html('${uploadStartedMsg}');
                data.submit();
            },
            done: function (e, data){
                //alert('upload done! result[' + JSON.stringify(data.result) + '] status[' + data.textStatus + '] jqXHR[' + JSON.stringify(data.jqXHR) +']');
                var result=eval(data.result)[0];
                $(this).find('[role=fileUploadedMsg]').html(result.uploadTxt);
            },
            fail: function (e, data){
                //alert('upload failed! result[' + JSON.stringify(data.result) + '] status[' + data.textStatus + '] jqXHR[' + JSON.stringify(data.jqXHR) +']');
                alert('${uploadFailedMsg}');
                $(this).find('[role=fileUploadedMsg]').hide();
            }
        });
    });
}

