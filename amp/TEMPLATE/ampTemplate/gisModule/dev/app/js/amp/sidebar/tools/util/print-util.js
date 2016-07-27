var $ = require('jquery');
var _ = require('underscore');


function printMap(options) {
    options = options || {};
    var mapContainer = $('#map-container').clone(true, true);
    var translateEl = ['.leaflet-map-pane', '.leaflet-tile', '.leaflet-zoom-animated', '.leaflet-marker-icon', '.leaflet-popup'];
    var removedEl = ['.legend', '.datasources', '.leaflet-control-container', '#map-header'];
    _.each(translateEl, function (el) {
        var elem = mapContainer.find(el);
        var original = $(el);
        var transformRegExp = /^(translate|matrix)\(*/i;
        elem.each(function (index, e) {
            e = $(e);
            var position = $(original[index]).position();
            var leafletPos = original[index]._leaflet_pos;
            if(e.hasClass("leaflet-popup") && position) {
                e.css("position", "absolute");
                e.css("left", position.left);
                e.css("top", position.top);
                e.css("transform", "none");
            } else if(transformRegExp.test(e.css('transform')) && leafletPos) {
                e.css("position", "absolute");
                e.css("left", leafletPos.x);
                e.css("top", leafletPos.y);
                e.css("transform", "none");
            }
        });
        mapContainer.find(".table").css("font-size", "0.9em");
    });
    _.each(removedEl, function (el) {
        var elem = mapContainer.find(el);
        if(!elem.hasClass('expanded')) {
            elem.css("display","none");
        }
    });
    if(!mapContainer.find('.legend').hasClass('expanded')) {
        mapContainer.find('.datasources').css("right", "0");
    }
    // check scrolls
    var script = '';
    var scrollDivs = ['datasources-content', 'scroll-container'];
    _.each(scrollDivs, function (el) {
        var elem = mapContainer.find('.' + el);
        var originalElem = $('.' + el);
        if (originalElem.scrollTop() > 0) {
            var id = el + '-id';
            elem.attr('id', id);
            script += 'document.getElementById("' + id + '").scrollTop = ' + originalElem.scrollTop() + ';';
        }
    });


    var styleLocation = document.location.href.replace("index.html", "compiled-css/main.css");
    var headers = '<meta http-equiv="content-type" content="text/html; charset=UTF-8"><link rel="stylesheet" href="' + styleLocation + '">';
    var html = "<html><head>" + headers + "</head><body>" + mapContainer[0].outerHTML + "</body></html>";
    console.log(html);
    $.ajax({
        data: JSON.stringify({
            content: html,
            width: window.innerWidth,
            height: window.innerHeight,
            javascript: script
        }),
        contentType: 'application/json',
        headers : {
            'Accept' : 'image/png',
            'Content-Type' : 'application/json'
        },
        method: 'POST',
        url: '/rest/commons/print',
        dataType: '*',
        success: options.success,
        error: options.error
    });
}

module.exports = {
    printMap: printMap
};
