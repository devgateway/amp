var $ = require('jquery');
var _ = require('underscore');


function addRtlStyle(location) {
    var isRtl = app.data.generalSettings.get("rtl-direction");
    if (isRtl) {
        var styleLocation = location.replace("gisModule/dist/index.html", "css_2/amp-rtl.css");
        var styleLink = '<link rel="stylesheet" href="' + styleLocation + '">';
        return styleLink;
    }
    return '';
}

function rewriteCssUrls(cssText, cssFileUrl) {
    var baseDir = cssFileUrl.substring(0, cssFileUrl.lastIndexOf('/') + 1);
    var origin = window.location.origin;
    return cssText.replace(/url\s*\(\s*(['"]?)([^'")\s]+)\1\s*\)/gi, function (match, quote, url) {
        if (/^(https?:|data:|#|//)/.test(url)) {
            return match;
        }
        if (url.charAt(0) === '/') {
            return 'url(' + quote + origin + url + quote + ')';
        }
        return 'url(' + quote + baseDir + url + quote + ')';
    });
}

function fetchAndInlineCSS(url) {
    if (typeof window.fetch === 'undefined' || !url) {
        return Promise.resolve(url ? '<link rel="stylesheet" href="' + url + '">' : '');
    }
    return window.fetch(url)
        .then(function (r) {
            if (!r.ok) { throw new Error('HTTP ' + r.status); }
            return r.text();
        })
        .then(function (css) {
            return '<style>' + rewriteCssUrls(css, url) + '</style>';
        })
        .catch(function () {
            return '<link rel="stylesheet" href="' + url + '">';
        });
}

function fixImagePaths(container) {
    var origin = window.location.origin;
    container.find('[src]').each(function () {
        var src = this.getAttribute('src');
        if (!src || src.indexOf('data:') === 0) { return; }
        if (/^\/[^/]/.test(src)) {
            this.setAttribute('src', origin + src);
            return;
        }
        if (src.indexOf('http://') === 0) {
            this.setAttribute('src', src.replace('http://', 'https://'));
        }
    });
}

function inlineTileImages(container, done) {
    if (typeof window.fetch === 'undefined') {
        done();
        return;
    }
    var imgs = container.find('img.leaflet-tile');
    var total = imgs.length;
    if (total === 0) {
        done();
        return;
    }
    var count = 0;
    var finish = function () {
        if (++count >= total) {
            done();
        }
    };
    imgs.each(function () {
        var img = this;
        var src = img.getAttribute('src');
        if (!src || src.indexOf('data:') === 0) {
            finish();
            return;
        }
        window.fetch(src, { mode: 'cors' })
            .then(function (response) { return response.blob(); })
            .then(function (blob) {
                var reader = new FileReader();
                reader.onloadend = function () {
                    img.src = reader.result;
                    finish();
                };
                reader.onerror = finish;
                reader.readAsDataURL(blob);
            })
            .catch(finish);
    });
}

// TODO this should be split into at least two method:
// 1- html select and transform
// 2- post of the html
function printMap(options) {
    options = options || {};
    var mapContainer = $('#map-container').clone(true, true);
    var translateEl = ['.leaflet-map-pane', '.leaflet-tile', '.leaflet-zoom-animated', '.leaflet-marker-icon', '.leaflet-popup'];
    var removedEl = ['.legend', '.datasources', '.leaflet-control-container', '#map-header', '.load-more'];
    _.each(translateEl, function (el) {
        var elem = mapContainer.find(el);
        var original = $(el);
        var transformRegExp = /^(translate3d|translate|matrix)\(*/i;
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
    });
    mapContainer.find(".table").css("font-size", "0.9em"); // this should change for fonts
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

    fixImagePaths(mapContainer);

    var styleLocation          = document.location.href.replace("index.html", "compiled-css/main.css");
    var styleTabsLocation      = document.location.href.replace("gisModule/dist/index.html", "tabs/css/less/tabs.css");
    var styleBootstrapLocation = document.location.href.replace("gisModule/dist/index.html", "tabs/css/bootstrap.css");
    var styleBootstrapThemeLocation = document.location.href.replace("gisModule/dist/index.html", "tabs/css/bootstrap-theme.css");
    var fontBaseLocation       = document.location.href.replace("index.html", "fonts");

    var cssUrls = [styleLocation, styleBootstrapLocation, styleTabsLocation, styleBootstrapThemeLocation];
    var isRtl = app.data.generalSettings.get("rtl-direction");
    if (isRtl) {
        cssUrls.push(document.location.href.replace("gisModule/dist/index.html", "css_2/amp-rtl.css"));
    }

    // TODO remove this does not work the font wof file should be in the phantom installation path
    var fontFace = "@font-face {" +
                    " font-family: 'Open Sans';" +
                    " src: url('" + fontBaseLocation + "/open_sans_light/OpenSans-Light-webfont.eot');"+
                    " src: url('" + fontBaseLocation + "/open_sans_light/OpenSans-Light-webfont.eot?#iefix') format('embedded-opentype'), url('" + fontBaseLocation + "/open_sans_light/OpenSans-Light-webfont.woff') format('woff'), url('" + fontBaseLocation + "/open_sans_light/OpenSans-Light-webfont.ttf') format('truetype'), url('" + fontBaseLocation + "/open_sans_light/OpenSans-Light-webfont.svg#open_sanslight') format('svg');"+
                    " font-weight: 300;" +
                    " font-style: normal;" +
                    "} " +
                    "@font-face {" +
                    " font-family: 'Open Sans';" +
                    " src: url('" + fontBaseLocation + "/open_sans_extrabold/OpenSans-ExtraBold-webfont.eot');" +
                    " src: url('" + fontBaseLocation + "/open_sans_extrabold/OpenSans-ExtraBold-webfont.eot?#iefix') format('embedded-opentype'), url('" + fontBaseLocation + "/open_sans_extrabold/OpenSans-ExtraBold-webfont.woff') format('woff'), url('" + fontBaseLocation + "/open_sans_extrabold/OpenSans-ExtraBold-webfont.ttf') format('truetype'), url('" + fontBaseLocation + "/open_sans_extrabold/OpenSans-ExtraBold-webfont.svg#open_sansextrabold') format('svg');" +
                    " font-weight: 800;" +
                    " font-style: normal;" +
                    "}";

    Promise.all(cssUrls.map(fetchAndInlineCSS)).then(function (inlinedStyles) {
        var headers = '<meta http-equiv="content-type" content="text/html; charset=UTF-8">' +
                      inlinedStyles.join('') +
                      '<style>' + fontFace + '</style>';

        inlineTileImages(mapContainer, function () {
            var html = "<html><head>" + headers + "</head><body onload=\"restoreScroll()\">" +
                "<script>function restoreScroll() {" + script + "}</script>" +
                mapContainer[0].outerHTML + "</body></html>";
            //console.log(html); Only for Debugging
            $.ajax({
                data: JSON.stringify({
                    content: html,
                    width: window.innerWidth,
                    height: window.innerHeight
                }),
                contentType: 'application/json',
                headers : {
                    'Accept' : 'image/png',
                    'Content-Type' : 'application/json; charset=utf-8'
                },
                method: 'POST',
                url: '/rest/commons/print',
                dataType: 'text',
                success: options.success,
                error: options.error
            });
        });
    });
}

module.exports = {
    printMap: printMap,
    addRtlStyle: addRtlStyle
};
