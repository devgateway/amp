dojo.require("esri.tasks._task");
dojo.require("esri.tasks.gp");

dojo.declare("esri.tasks.CustomPrintTask", esri.tasks._Task, {
  constructor: function ( /*String*/ url, /*object*/ params) {
    this.url = url;
    this.printGp = new esri.tasks.Geoprocessor(this.url);
    this._handler = dojo.hitch(this, this._handler);
    if (params && params.async) {
      this.async = params.async;
    }
  },

  _handler: function (response, io, callback, errback, dfd) {
    try {
      var result;    
      if (this.async) {
        if(response.jobStatus === "esriJobSucceeded"){
          //If the name of the result is not the default one "Output_File", users should use the Print Service as a general GP service.
          this.printGp.getResultData(response.jobId, "Output_File", dojo.hitch(this, function(output){
            //If the result is not the default format as output.value, users should use the print service as a GP service.
            result = output.value;
            this._successHandler([result], "onComplete", callback, dfd);
          }));
        }
        else {
          this._errorHandler(err, errback, dfd);
        }
      }
      else {
        result = response[0].value;
        this._successHandler([result], "onComplete", callback, dfd);
      }
    } catch (err) {
      this._errorHandler(err, errback, dfd);
    }
  },

  execute: function ( /*esri.tasks.PrintParameters*/ params, /*Function?*/ callback, /*Function?*/ errback) {
    var _h = this._handler,
      _e = this._errorHandler;    

    var printTemplate = params.template || new esri.tasks.PrintTemplate();
    var exportOptionsInput = printTemplate.exportOptions;
    var exportOptions;
    if (exportOptionsInput) {
      var width = exportOptionsInput.width;
      var height = exportOptionsInput.height;
      var dpi = exportOptionsInput.dpi;
      exportOptions = {
        outputSize: [width, height],
        dpi: dpi
      };
    }

    if (printTemplate.preserveScale === false) {
      this._preserveScale = false;
    } else {
      this._preserveScale = true;
    }

    var layoutOptionsInput = printTemplate.layoutOptions;
    var layoutOptions, legendIds = [], legendId = {};
    if (layoutOptionsInput) {
      this.legendAll = false;
      if (!layoutOptionsInput.legendLayers) {
        this.legendAll = true;
      }
      else {
        dojo.forEach(layoutOptionsInput.legendLayers, function(legendLayer, idx){
          legendId.id = legendLayer.layerId;
          if (legendLayer.subLayerIds) {
            legendId.subLayerIds = legendLayer.subLayerIds;
          }
          legendIds.push(legendId);
        });
      }
      var scalebarMetricUnit, scalebarNonMetricUnit;
      if (layoutOptionsInput.scalebarUnit === "Miles" || layoutOptionsInput.scalebarUnit === "Kilometers") {
        scalebarMetricUnit = "Kilometers";
        scalebarNonMetricUnit = "Miles";
      } else if (layoutOptionsInput.scalebarUnit === "Meters" || layoutOptionsInput.scalebarUnit === "Feet") {
        scalebarMetricUnit = "Meters";
        scalebarNonMetricUnit = "Feet";
      }
      var scalebarLabel = {
        Miles: "mi",
        Kilometers: "km",
        Yards: "yd",
        Feet: "ft",
        Meters: "m"
      };
      layoutOptions = {
        titleText: layoutOptionsInput.titleText,
        authorText: layoutOptionsInput.authorText,
        copyrightText: layoutOptionsInput.copyrightText,
        scaleBarOptions: {
          metricUnit: scalebarMetricUnit,
          metricLabel: scalebarLabel[scalebarMetricUnit],
          nonMetricUnit: scalebarNonMetricUnit,
          nonMetricLabel: scalebarLabel[scalebarNonMetricUnit]
        },
        legendOptions: {
          operationalLayers: legendIds
        }
      };
    }

    var map = params.map;
    
    var webMapJson = this._getPrintDefinition(map);
    //users can change the SR of the printout
    if (params.outSpatialReference) {
      webMapJson.mapOptions.spatialReference = params.outSpatialReference.toJson();
    }
    dojo.mixin(webMapJson, {
      exportOptions: exportOptions,
      layoutOptions: layoutOptions
    });
    if (this.allLayerslegend) {
      dojo.mixin(webMapJson.layoutOptions, {
        legendOptions: {
          operationalLayers: this.allLayerslegend
        }
      });
    }
    var webMapJsonText = dojo.toJson(esri._sanitize(webMapJson, true));
    var format = printTemplate.format;
    var template = printTemplate.layout;
    var printParams = {
      Web_Map_as_JSON: webMapJsonText,
      Format: format,
      Layout_Template: template
    };
    // Publishing an arcpy script as a custom print service, you can have more parameters.
    if (params.extraParameters) {
      printParams = dojo.mixin(printParams, params.extraParameters);
    }

    var dfd = new dojo.Deferred(esri._dfdCanceller);
    var load = function (r, i) {
        _h(r, i, callback, errback, dfd);
      };
    var error = function (r) {
        _e(r, errback, dfd);
      };
    
    if (this.async) {
      dfd._pendingDfd = this.printGp.submitJob(printParams, load, null, error);
    }
    else {
      dfd._pendingDfd = this.printGp.execute(printParams, load, error);
    }

    return dfd;
  },

  onComplete: function () {
  },

  _multipointLayer: function () {
    this.layerDefinition = {
      name: "multipointLayer",
      geometryType: "esriGeometryMultipoint",
      drawingInfo: {
        renderer: null
      }
    };

    this.featureSet = {
      geometryType: "esriGeometryMultipoint",
      features: []
    };
  },

  _polygonLayer: function () {
    this.layerDefinition = {
      name: "polygonLayer",
      geometryType: "esriGeometryPolygon",
      drawingInfo: {
        renderer: null
      }
    };

    this.featureSet = {
      geometryType: "esriGeometryPolygon",
      features: []
    };
  },

  _pointLayer: function () {
    this.layerDefinition = {
      name: "pointLayer",
      geometryType: "esriGeometryPoint",
      drawingInfo: {
        renderer: null
      }
    };

    this.featureSet = {
      geometryType: "esriGeometryPoint",
      features: []
    };
  },

  _polylineLayer: function () {
    this.layerDefinition = {
      name: "polylineLayer",
      geometryType: "esriGeometryPolyline",
      drawingInfo: {
        renderer: null
      }
    };

    this.featureSet = {
      geometryType: "esriGeometryPolyline",
      features: []
    };
  },

  _createFeatureCollection: function (gLayer) {
    var polygonCollectionLayer = new this._polygonLayer();
    var polylineCollectionLayer = new this._polylineLayer();
    var pointCollectionLayer = new this._pointLayer();
    var multipointCollectionLayer = new this._multipointLayer();
    var gJson;
    dojo.forEach(gLayer.graphics, function (g, idx) {
      if (g.geometry.type === "polygon") {
        gJson = g.toJson();
        polygonCollectionLayer.featureSet.features.push(gJson);
        if (gLayer.renderer) {
          polygonCollectionLayer.layerDefinition.drawingInfo.renderer = gLayer.renderer.toJson();
        }
        else {
          delete polygonCollectionLayer.layerDefinition.drawingInfo;
        }
        if (gLayer.fields) {
          polygonCollectionLayer.layerDefinition.fields = gLayer.fields;
        }
      } else if (g.geometry.type === "polyline") {
        gJson = g.toJson();
        polylineCollectionLayer.featureSet.features.push(gJson);
        if (gLayer.renderer) {
          polylineCollectionLayer.layerDefinition.drawingInfo.renderer = gLayer.renderer.toJson();
        }
        else {
          delete polylineCollectionLayer.layerDefinition.drawingInfo;
        }
        if (gLayer.fields) {
          polylineCollectionLayer.layerDefinition.fields = gLayer.fields;
        }
      } else if (g.geometry.type === "point") {
        
    	gJson = g.toJson();
        pointCollectionLayer.featureSet.features.push(gJson);
        if (gLayer.renderer) {
          pointCollectionLayer.layerDefinition.drawingInfo.renderer = gLayer.renderer.toJson();
        }
        else {
          delete pointCollectionLayer.layerDefinition.drawingInfo;
        }
        if (gLayer.fields) {
          pointCollectionLayer.layerDefinition.fields = gLayer.fields;
        }
      } else if (g.geometry.type === "multipoint") {
        gJson = g.toJson();
        multipointCollectionLayer.featureSet.features.push(gJson);
        if (gLayer.renderer) {
          multipointCollectionLayer.layerDefinition.drawingInfo.renderer = gLayer.renderer.toJson();
        }
        else {
          delete multipointCollectionLayer.layerDefinition.drawingInfo;
        }
        if (gLayer.fields) {
          multipointCollectionLayer.layerDefinition.fields = gLayer.fields;
        }
      }
    }, this);

    var layers = [];
    if (pointCollectionLayer.featureSet.features.length > 0) {
      layers.push(pointCollectionLayer);
    }
    if (polylineCollectionLayer.featureSet.features.length > 0) {
      layers.push(polylineCollectionLayer);
    }
    if (polygonCollectionLayer.featureSet.features.length > 0) {
      layers.push(polygonCollectionLayer);
    }
    if (multipointCollectionLayer.featureSet.features.length > 0) {
      layers.push(multipointCollectionLayer);
    }
    var featureCollectionLayers = {
      layers: layers
    };
    var featureCollection = {
      id: gLayer.id,
      opacity: gLayer.opacity,
      featureCollection: featureCollectionLayers
    };
    return featureCollection;
  },

  _getPrintDefinition: function (map) {
    var opLayers = this._createOperationalLayers(map);
    var operationalLayers = {
      operationalLayers: opLayers
    };
    
    var extent = map.extent, sr = map.spatialReference;
    if (map.spatialReference._isWrappable()) {
      extent = extent._normalize(true);
      sr = extent.spatialReference;
    }

    var mapOptions = {
      mapOptions: {
        extent: extent.toJson(),
        spatialReference: sr.toJson()
      }
    };
    if (this._preserveScale) {
      dojo.mixin(mapOptions.mapOptions, {
        scale: esri.geometry.getScale(map)
      });
    }
    if (map.timeExtent) {
      dojo.mixin(mapOptions.mapOptions, {
        time: [map.timeExtent.startTime.getTime(), map.timeExtent.endTime.getTime()]
      });
    }

    var webMapJson = {};
    dojo.mixin(webMapJson, mapOptions, operationalLayers);
    return webMapJson;
  },

  _createOperationalLayers: function (map) {
    
	  	var i, 
	  	layer, 
	  	layerType, 
	  	opLayer, 
	  	opLayers = [];
    
    if (this.legendAll) {
    	this.allLayerslegend = [];
    	} else {
    		this.allLayerslegend = null;
    	}
    
    //dynamic, tile map service layer, wms, image
    
    for (i = 0; i < map.layerIds.length; i++) {
      layer = map.getLayer(map.layerIds[i]);
      if (!layer.loaded || !layer.visible) {
        continue;
      }
      if (layer.credential && layer.url.indexOf("token=") === -1) {
        layer.url += "?token=" + layer.credential.token;
      }
      layerType = layer.declaredClass;

      switch (layerType) {
      case "esri.layers.ArcGISDynamicMapServiceLayer":
        var layers = [];
        
        if (layer._params.dynamicLayers) {
          var dynamicLayers = dojo.fromJson(layer._params.dynamicLayers);
          dojo.forEach(dynamicLayers, function(dynamicLayer, idx){
            layers.push({id: dynamicLayer.id, layerDefinition: dynamicLayer});
          });          
        }
        else {
          dojo.forEach(layer.layerInfos, function (layerInfo, idx) {
            var subLayerInfo = {
              id: layerInfo.id,
              layerDefinition: {
                definitionExpression: null,
                layerTimeOptions: null
              }
            };
            if (layer.layerDefinitions && layer.layerDefinitions[layerInfo.id]) {
              subLayerInfo.layerDefinition.definitionExpression = layer.layerDefinitions[layerInfo.id];
            }
            if (layer.layerTimeOptions && layer.layerTimeOptions[layerInfo.id]) {
              subLayerInfo.layerDefinition.layerTimeOptions = layer.layerTimeOptions[layerInfo.id];
            }
            if (subLayerInfo.layerDefinition.definitionExpression || subLayerInfo.layerDefinition.layerTimeOptions) {
              layers.push(subLayerInfo);
            }
          });
        }
        
        opLayer = {
          id: layer.id,
          url: layer.url,
          title: layer.id,
          opacity: layer.opacity,
          visibleLayers: layer.visibleLayers,
          layers: layers
        };
        opLayers.push(opLayer);
        
        if (this.allLayerslegend) {
          this.allLayerslegend.push({
            id: layer.id,
            subLayerIds: layer.visibleLayers
          });
        }
        break;
      case "esri.layers.ArcGISImageServiceLayer":
        opLayer = {
          id: layer.id,
          url: layer.url,
          title: layer.id,
          opacity: layer.opacity,
          bandIds: layer.bandIds,
          compressionQuality: layer.compressionQuality,
          format: layer.format,
          interpolation: layer.interpolation
        };
        if (layer.mosaicRule) {
          dojo.mixin(opLayer, {
            mosaicRule: layer.mosaicRule.toJson()
          });
        }
        if (layer.renderingRule) {
          dojo.mixin(opLayer, {
            renderingRule: layer.renderingRule.toJson()
          });
        }
        opLayers.push(opLayer);
        if (this.allLayerslegend) {
          this.allLayerslegend.push({
            id: layer.id
          });
        }
        break;
      case "esri.layers.WMSLayer":
        //It seems print service doesn't need format
        /*var format = layer.imageFormat.split("/")[1];
        if (format.toLowerCase() === "png") {
          format = "png24";
        }*/
        opLayer = {
          id: layer.id,
          url: layer.url,
          title: layer.title,
          opacity: layer.opacity,
          type: "wms",
          version: layer.version,
          transparentBackground: layer.imageTransparency,
          visibleLayers: layer.visibleLayers
        };
        opLayers.push(opLayer);
        if (this.allLayerslegend) {
          this.allLayerslegend.push({
            id: layer.id,
            subLayerIds: layer.visibleLayers
          });
        }
        break;
      case "esri.virtualearth.VETiledLayer":
        opLayer = {
          id: layer.id,
          visibility: layer.visible,
          type: "BingMaps" + layer.mapStyle,
          culture: layer.culture,
          opacity: layer.opacity
        };
        opLayers.push(opLayer);
        break;
      case "esri.layers.OpenStreetMapLayer":
        opLayer = {
          id: layer.id,
          title: layer.id,
          type: "OpenStreetMap",
          opacity: layer.opacity,
          url: layer.tileServers[0]
        };
        opLayers.push(opLayer);
        break;
      case "esri.layers.WMTSLayer":
        opLayer = {
          id: layer.id,
          url: layer.url,
          title: layer.id,
          opacity: layer.opacity,
          type: "wmts",
          layer: layer.layerInfos[0].identifier,
          style: layer.layerInfos[0].style,
          format: layer.layerInfos[0].format,
          tileMatrixSet: layer.layerInfos[0].tileMatrixSet
        };
        opLayers.push(opLayer);
        break;
      case "esri.layers.KMLLayer":
        opLayer = {
          id: layer.id,
          title: layer.id,
          type: "KML",
          url: layer.url,
          opacity: layer.opacity
        };
        opLayers.push(opLayer);
        if (this.allLayerslegend) {
          this.allLayerslegend.push({
            id: layer.id
          });
        }
        break;
      case "esri.layers.MapImageLayer":
        var images = layer.getImages();
        //server 10.1 beta2 only support one image for one layer, this is a workaround.
        dojo.forEach(images, function (image, idx) {
          opLayer = {
            id: layer.id + "_image" + idx,
            type: "image",
            title: layer.id,
            opacity: layer.opacity,
            extent: image.extent.toJson(),
            url: image.href
          };
          opLayers.push(opLayer);
        });
        break;
      default:
        if (layer instanceof esri.layers.TiledMapServiceLayer || layer instanceof esri.layers.DynamicMapServiceLayer) {
          opLayer = {
            id: layer.id,
            url: layer.url,
            title: layer.id,
            opacity: layer.opacity
          };
          opLayers.push(opLayer);
        }
      }
    }
    
    //END  //dynamic, tile map service layer, wms, image

    //GRAPHIC LAYERS
    
    for (i = 0; i < map.graphicsLayerIds.length; i++) {
      layer = map.getLayer(map.graphicsLayerIds[i]);
      if (!layer.loaded || !layer.visible) {
        continue;
      }
      layerType = layer.declaredClass;
      switch (layerType) {
      case "esri.layers.FeatureLayer":
        if (layer.url) {
          opLayer = {
            id: layer.id,
            url: layer.url,
            title: layer.id,
            opacity: layer.opacity,
            minScale: layer.minScale,
            maxScale: layer.maxScale,
            layerDefinition: {
              drawingInfo: {
                renderer: layer.renderer.toJson()
              }
            }
          };
          if (layer.getDefinitionExpression()) {
            dojo.mixin(opLayer.layerDefinition, {
              definitionExpression: layer.getDefinitionExpression()
            });
          }
          if (layer.mode !== 2) {
            if (layer.getSelectedFeatures().length > 0) {
              var selectionObjectIds = dojo.map(layer.getSelectedFeatures(), function (selectedFeature) {
                return selectedFeature.attributes[layer.objectIdField];
              });
              if (selectionObjectIds.length > 0 && layer.getSelectionSymbol()) {
                dojo.mixin(opLayer, {
                  selectionObjectIds: selectionObjectIds,
                  selectionSymbol: layer.getSelectionSymbol().toJson()
                });
              }
            }
          } else {
            var objectIds = dojo.map(layer.getSelectedFeatures(), function (selectedFeature) {
              return selectedFeature.attributes[layer.objectIdField];
            });
            if (objectIds.length === 0 || !layer._params.drawMode) {
              break;
            }
            dojo.mixin(opLayer.layerDefinition, {
              objectIds: objectIds
            });
            var selectedRenderer = null;
            if (layer.getSelectionSymbol()){
              selectedRenderer = new esri.renderer.SimpleRenderer(layer.getSelectionSymbol());
            }
            dojo.mixin(opLayer.layerDefinition.drawingInfo, {
              renderer: selectedRenderer && selectedRenderer.toJson()
            });
          }
        } else {
          opLayer = this._createFeatureCollection(layer);
          //opLayer = layer.toJson();
        }
        opLayers.push(opLayer);
        if (this.allLayerslegend) {
          this.allLayerslegend.push({
            id: layer.id
          });
        }
        break;
      case "esri.layers.GraphicsLayer":
      case "esri.ux.layers.AmpCluster":
      case "esri.ux.layers.ClusterLayer":
    	opLayer = this._createFeatureCollection(layer);
        opLayers.push(opLayer);
        if (this.allLayerslegend) {
          this.allLayerslegend.push({
            id: layer.id
          });
        }
        break;
      default:
      }
    }

    if (map.graphics && map.graphics.graphics.length > 0) {
      opLayer = this._createFeatureCollection(map.graphics);
      opLayers.push(opLayer);
    }
    return opLayers;
  }
});

dojo.declare("esri.tasks.PrintParameters", null, {
  map: null,
  template: null,
  outSpatialReference: null,
  extraParameters: null
});

dojo.declare("esri.tasks.PrintTemplate", null, {
  label: null,
  exportOptions: {
    width: 800,
    height: 1100,
    dpi: 96
  },
  layoutOptions: null,
  format: "PNG32",
  layout: "MAP_ONLY",
  preserveScale: true
});

dojo.declare("esri.tasks.LegendLayer", null, {
  layerId: null,
  subLayerIds: null
});

