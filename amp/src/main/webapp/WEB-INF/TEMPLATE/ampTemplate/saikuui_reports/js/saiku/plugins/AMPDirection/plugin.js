var AMPDirection = {};

AMPDirection.checkRtlDirection = function() {
    $.getJSON("/rest/amp/settings", function(data) {
        var isRtl = data["rtl-direction"];
        if (isRtl) {
            $('head').append('<link rel="stylesheet" href="/src/main/webapp/WEB-INF/TEMPLATE/ampTemplate/css_2/amp-rtl.css">');
        }
    });
};

