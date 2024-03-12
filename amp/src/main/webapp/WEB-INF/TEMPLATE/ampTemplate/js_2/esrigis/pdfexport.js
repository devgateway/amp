
function createOperationalLayerTOC(layer){
    var tocDOM = dojo.byId("toc");
    tocDOM.innerHTML = "Creating refrence layer legend...";
    
    esri.request({
        url: layer.url + "/legend",
        content: {
            f: "json"
        },
        handleAs: "json",
        preventCache: true,
        callbackParamName: "callback",
        load: function(response, io){
            var tocTableOuter = dojo.create("table", {
                id: "TOC-Table"
            }, tocDOM, "only");
            var tocTable = dojo.create("tbody", null, tocTableOuter);
            dojo.forEach(response.layers, function(layerInfo, i){
                if (layerInfo.legend.length == 1) {
                    var row = dojo.create("tr", {
                        id: layer.id + "-" + layerInfo.layerId + "-TocItem"
                    }, tocTable, "last");
                    var td = dojo.create("td", null, row, "last");
                    var div = dojo.create("div", null, td);
                    var layerChk = new dijit.form.CheckBox({
                        id: layer.id + "-" + layerInfo.layerId + "-CHK",
                        onClick: toggleRefrenceMapServiceLayer,
                        checked: false,
                        title: "Visible in Map",
                        style: "margin: 4px 0px 0px 0px;"
                    }, div);
                    layerChk.set("class", layer.id);
                    dojo.create("td", {
                        innerHTML: "<div class=\"tocItem\"><img style=\"vertical-align:middle;\" src='" + layer.url + "/" + layerInfo.layerId + "/images/" + layerInfo.legend[0].url + "'/>&nbsp;" + layerInfo.layerName + "</div>"
                    }, row, "last");
                }
                else {
                    var row = dojo.create("tr", null, tocTable, "last");
                    var td = dojo.create("td", null, row, "last");
                    var div = dojo.create("div", null, td);
                    var layerChk = new dijit.form.CheckBox({
                        id: layer.id + "-" + layerInfo.layerId + "-CHK",
                        onClick: toggleRefrenceMapServiceLayer,
                        checked: false,
                        title: "Visible in Map",
                        style: "margin: 4px 0px 0px 0px;"
                    }, div);
                    layerChk.set("class", layer.id);
                    var td2 = dojo.create("td", {
                        innerHTML: "&nbsp;" + layerInfo.layerName
                    }, row, "last");
                    var tl = layerInfo.legend.length;
                    dojo.forEach(layerInfo.legend, function(legend, i){
                        if ((i + 1) === tl) {
                            var row = dojo.create("tr", {
                                id: layer.id + "-" + layerInfo.layerId + "-TocItem"
                            }, tocTable, "last");
                        }
                        else {
                            var row = dojo.create("tr", null, tocTable, "last");
                        }
                        dojo.create("td", {
                            innerHTML: "&nbsp;"
                        }, row, "last");
                        dojo.create("td", {
                            innerHTML: "<div class=\"tocItem\"><img style=\"vertical-align:middle;\" src=\"" + layer.url + "/" + layerInfo.layerId + "/images/" + legend.url + "\"/>&nbsp;" + legend.label + "</div>"
                        }, row, "last");
                    });
                }
            });
        },
        error: function(error, io){
            tocDOM.innerHTML = "An error occured, please refresh your browser.";
        }
    });
    
}
    
    
function exportPDF(){
    var visiblelayers = {};
    dojo.forEach(map.layerIds, function(layer, i1){
        var ml = map.getLayer(layer);
        switch (ml.declaredClass) {
            case "esri.layers.ArcGISDynamicMapServiceLayer":
                var s = dojo.forEach(ml.layerInfos, function(sublayer, i2){
                    if (dojo.indexOf(ml.visibleLayers, sublayer.id.toString()) !== -1) {
                        visiblelayers[sublayer.name] = true;
                    }
                    else {
                        visiblelayers[sublayer.name] = false;
                    }
                });
                break;
            default:
                visiblelayers[ml.id] = ml.visible;
                break;
        }
    });
    
    var params = {
        "xMin": map.extent.xmin,
        "yMin": map.extent.ymin,
        "xMax": map.extent.xmax,
        "yMax": map.extent.ymax,
        "Spatial_Reference": map.spatialReference.wkid,
        "Map_Scale": esri.geometry.getScale(map),
        "Visiblelayers": dojo.toJson(visiblelayers),
        "Layout": "Landscape8x11",
        "Include_Attributes": true,
        "Map_Title": "test",
        "PointGraphics": getFeatureSet("point"),
        "LineGraphics": getFeatureSet("polyline"),
        "PolyGraphics": getFeatureSet("polygon")
    };
    exportMapGP.submitJob(params, pdfCompleteCallback, pdfStatusCallback, pdfErrorCallback);
}

function pdfCompleteCallback (jobInfo){
    if (jobInfo.jobStatus == "esriJobSucceeded") {
        exportMapGP.getResultData(jobInfo.jobId, "PDF", setPdfDownloadLink);
    }
    else {
        pdfErrorCallback();
    }
}

 function pdfStatusCallback (jobInfo){
    if (jobInfo.jobStatus === "esriJobFailed") {
        pdfErrorCallback();
    }
}

 function pdfErrorCallback(error){
    //dijit.byId("exportPDFBtn").set("disabled", false);
    //dojo.style("pdfRequest", "display", "none");
   // dojo.style("pdfRequestFinished", "display", "none");
    //dojo.style("pdfRequestError", "display", "block");
	 console.log(error);
}

function setPdfDownloadLink (pdf){
   // dijit.byId("exportPDFBtn").set("disabled", false);
    //dojo.style("pdfRequest", "display", "none");
    //dojo.style("pdfRequestError", "display", "none");
    //dojo.style("pdfRequestFinished", "display", "block");
	console.log(pdf.value.url);
}
    
function getFeatureSet(type){
    var fset = new esri.tasks.FeatureSet();
    dojo.forEach(map.graphics.graphics, function(graphic){
        if (graphic.geometry.type === type) {
            fset.features.push(graphic);
        }
    });
    if (fset.features.length > 0) {
        return fset;
    }
    else {
        return "";
    }
}



