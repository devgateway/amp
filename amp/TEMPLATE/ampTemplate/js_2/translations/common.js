/**
 *
 * @param originalTranslation labesl to be translated
 * @param processTranslations Function that will process the success event.
 */

function getTranslations(originalTranslation,processTranslations){
    $.ajax({
        type: "POST",
        url: "http://local.amp.org:8080/rest/translations/label-translations",
        data: JSON.stringify(originalTranslation),
        contentType: "application/json; charset=utf-8",
        Accept:"application/json",
        crossDomain: true,
        dataType: "json",
        success: function(data, status, jqXHR) {
        processTranslations(data);
    },
        error: function (jqXHR, status) {
            // error handler
            // No need to do further processing, the labels will be shown in the original language
            console.log(jqXHR);
        }
    });
}