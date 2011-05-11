﻿dojo.declare(
    "PrintMap",
    null,
    {
        map: null,
        mapState: null,
        extent: null,
        layers: [],
        allUrls: [],
        tileUrls: [],
        dynamicUrls: [],
        featureUrls: [],
        width: 0,
        height: 0,

        constructor: function(/*esri.Map*/map, /*div*/mapState) {
            this.map = map;
            this.mapState = mapState;
            var layerIds = map.layerIds;
            this.width = map.width;
            this.height = map.height;
            dojo.forEach(layerIds, dojo.hitch(this, function(layerId) {
                var layer = this.map.getLayer(layerId);
                this.layers.push(layer);
            }));
        },

        getMap: function() {
            return this.map;
        },

        getMapState: function() {
            return this.mapState;
        },

        updateExtent: function(extent) {
            this.extent = extent;
            //reset tile urls
            this.tileUrls = [];
            //reset dynamic urls
            this.dynamicUrls = [];

            dojo.forEach(this.layers, dojo.hitch(this, function(layer) {
                if (layer instanceof esri.layers.ArcGISTiledMapServiceLayer) {
                    var candidateTileInfo = esri.TileUtils.getCandidateTileInfo(this.map, layer.tileInfo, this.extent);
                    var layerTileUrls = [];
                    var tileXOffset = Math.ceil(this.map.width / layer.tileInfo.width);
                    var tileYOffset = Math.ceil(this.map.height / layer.tileInfo.height);
                    var delta = this.map._visibleDelta ? 
                        this.map._visibleDelta : this.map.__visibleDelta;
                    for (var x = 0; x <= tileXOffset; x++) {
                        for (var y = 0; y <= tileYOffset; y++) {
                            var tileUrl = layer.url + "/tile/" + this.map.getLevel() + "/" + (candidateTileInfo.tile.coords.row + y) +
                                                                                            "/" + (candidateTileInfo.tile.coords.col + x);
                            layerTileUrls.push({ "url": tileUrl, "row": candidateTileInfo.tile.coords.row + y, "col": candidateTileInfo.tile.coords.col + x });
                        }
                    }
                    this.tileUrls.push({ "tiles": layerTileUrls, "transparency": layer.opacity, "clipOptions": { "offsetX": candidateTileInfo.tile.offsets.x - delta.x, "offsetY": candidateTileInfo.tile.offsets.y - delta.y, "width": this.width, "height": this.height} });
                } else if (layer instanceof esri.layers.ArcGISDynamicMapServiceLayer) {
                    var dynamicUrl = layer.url + "/export?bbox=" + this.extent.xmin + "," + this.extent.ymin + "," + this.extent.xmax + "," + this.extent.ymax + "&size=" + this.width + "," + this.height + "&transparent=true&format=png24&f=image";
                    this.dynamicUrls.push({ "url": dynamicUrl, "width": this.width, "height": this.height, "transparency": layer.opacity });
                }
            }));
            var state = {"tileLayers":this.tileUrls, "dynamicLayers": this.dynamicUrls};
            this.mapState.value = dojo.toJson(state);
        },

        getExtent: function() {
            return this.extent;
        },

        getLayers: function() {
            return this.layers;
        },

        getLOD: function() {
            return this.map.getLevel();
        }
    }
);