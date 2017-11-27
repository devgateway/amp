var AMPDirection = {};

AMPDirection.checkRtlDirection = function() {
    $.getJSON("/rest/amp/settings?" + new Date().getTime(), function(data) {
        var isRtl = data["rtl-direction"];
        if (isRtl) {
            $('head').append('<link rel="stylesheet" href="/TEMPLATE/ampTemplate/css_2/amp-rtl.css">');
        }
    });
};

