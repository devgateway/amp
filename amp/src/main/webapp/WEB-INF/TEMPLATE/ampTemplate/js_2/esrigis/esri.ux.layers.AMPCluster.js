/*
 * Adding array IndexOf method on IE explorer 
 * */
if (!('indexOf' in Array.prototype)) {
    Array.prototype.indexOf= function(find, i /*opt*/) {
        if (i===undefined) i= 0;
        if (i<0) i+= this.length;
        if (i<0) i= 0;
        for (var n= this.length; i<n; i++)
            if (i in this && this[i]===find)
                return i;
        return -1;
    };
}


dojo.declare('esri.ux.layers.AmpCluster', esri.layers.GraphicsLayer, {
	
	constructor: function(options) {
		
		if (typeof Object.create === 'undefined') {
    	    Object.create = function (o) { 
    	        function F() {} 
    	        F.prototype = o; 
    	        return new F(); 
    	    };
    	}
		
		this.expandedPointsLayers=new esri.layers.GraphicsLayer(options);
		options.map.addLayer(this.expandedPointsLayers);

		
        //basic esri.layers.GraphicsLayer option(s)
        this.displayOnPan = options.displayOnPan || false;

        //set the map
        //TODO: find a way to not pass in the map as a config option.
        this._map = options.map;

        //base connections to update clusters during user/map interaction
        dojo.connect(this._map, 'onZoomStart', dojo.hitch(this, this.handleMapZoomStart));
        dojo.connect(this._map, 'onExtentChange', dojo.hitch(this, this.handleMapExtentChange));

        //may or may not be needed. not sure
        this.spatialReference = new esri.SpatialReference({ wkid: 102100 });
        this.initialExtent = (
    			this.fullExtent = new esri.geometry.Extent(-20037507.0671618, -19971868.8804086, 20037507.0671618, 19971868.8804086, this.spatialReference)
    		);

        //this could be moved to sit within a function
        this.levelPointTileSpace = [];

        //holds all the features for this cluster layer
        this._features = [];
        this.onClusterExpand=options.onClusterExpand;
        //set incoming features
        //this will throw an error if the features WKID is not in below list.  
        //projected: 102100, 102113
        //geographic: 4326, 4269, 4267
        //TODO: add more supported WKIDs?  read what the map WKID is and only allow that?
        //TODO: throw error and NOT continue to add layer to map.  right now, if error is thrown, an empty graphics layer will be added to the map
        try {
            this.setFeatures(options.features);
        } catch (ex) {
            alert(ex);
        }

        //connects for cluster layer itself that handles the loading and mouse events on the graphics
        dojo.connect(this, 'onLoad', this.handleLayerLoaded);
        //dojo.connect(this, 'onMouseOver', this.handleMouseOver);
        //dojo.connect(this, 'onMouseOut', this.handleMouseOut);
		dojo.connect(this, 'onClick', this.handleMouseClick);
		dojo.connect(this, "onVisibilityChange", function(visible){
			if(visible){
				this.expandedPointsLayers.show();
			}else{
				this.expandedPointsLayers.hide();
			}
			});
        //default symbol bank for clusters and single graphics
        //TODO: allow for user supplied symbol bank to override.  just use an ESRI renderer somehow?
        this.symbolBank = options.symbolBank;

        //how far away the flare will be from the center of the cluster in pixels - Number
        this._flareDistanceFromCenter = options.flareDistanceFromCenter || 20;

        //the number of flare graphics to limit the cluster to - Number
        this._flareLimit = options.flareLimit || 20;

        //info template for all single graphics and flare graphics - esri.InfoTemplate
        this._infoTemplate = options.infoWindow.template || null;

        //info window width - Number
        this._infoWindowWidth = options.infoWindow.width || 150;

        //info window height - Number
        this._infoWindowHeight = options.infoWindow.height || 100;
        
        this._showExpanded = options.showexpanded || false;
        
        //following the basics of creating a custom layer
        this.loaded = true;
        this.onLoad(this);
    },
    
    //clear all graphics when zoom starts
    handleMapZoomStart: function() {
        this.clear();
    },

    //re-cluster on extent change
    //TODO: maybe only use features that fall within current extent?  Add that as an option?
    handleMapExtentChange: function(extent, delta, leveChange, lod) {
        this.clusterFeatures(true);
    },
    
    //this function may not be needed exactly as is below.  somehow, the attributes need to be mapped to the points.
    setFeatures: function(features) {
	var i=0;
        this._features = [];
        var wkid = features[0].geometry.spatialReference.wkid;
        if (wkid != 102100) {
            if (wkid == 4326 || wkid == 4269 || wkid == 4267) {
                dojo.forEach(features, function(feature) {
                    point = esri.geometry.geographicToWebMercator(feature.geometry);
                    
                    
                    var newPoint=dojo.clone(point); //clone in order to avoid the cricular reference issue
                    newPoint.attribues=dojo.mixin({index:i},feature.attributes);
                  
                    point.attributes=dojo.mixin({point:newPoint,index:i},feature.attributes);
                    this._features.push(point);
                    i++;
                }, this);
            } else {
                throw 'Input Spatial Reference Must Be in Either WKID: 102110 or WKID: 4326';
                return;
            }
        } else {
        	
            dojo.forEach(features, function(feature) {
                point = feature.geometry;

                var newPoint=dojo.clone(point);
            	newPoint.attributes=dojo.mixin({index:i},feature.attributes);
            	
            	point.attributes=dojo.mixin({point:newPoint,index:i},feature.attributes);
                this._features.push(point);
                i++;
                
            }, this);
        }
    },

    //fires when cluster layer is loaded, but not added to map yet.
    handleLayerLoaded: function(lyr) {
        this.clusterFeatures(true);
    },

    //fires when any graphic (clustered or single) is moused over
    handleMouseOver: function(evt) {
	    var graphic = evt.graphic;

        if (graphic.symbol.type == 'textsymbol' || graphic.symbol.type == 'simplelinesymbol') {
            if (graphic.attributes) {
                if (graphic.attributes.baseGraphic && graphic.attributes.baseGraphic.task) {
                    graphic.attributes.baseGraphic.task.cancel();
                }
            }
            return;
        }
        if (graphic.attributes.isCluster) { //cluster mouse over
            if (graphic.attributes.clustered) {
                if (graphic.task) {
                    graphic.task.cancel();
                }
                return;
            }
        } else { //single marker or cluster flare mouse over
            if (graphic.attributes.baseGraphic) { //cluster flare
                graphic.attributes.baseGraphic.task.cancel();
            }
		    this.showInfoWindow(graphic);
            return;
        }
		
		this.showTooltip(evt);
        graphic.clusterGraphics = [];

        var cSize = graphic.attributes.clusterSize;
        var lineSymbol = new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([0, 0, 0, 1]), 1);

        //polyline used to "tie" flare to cluster
        //set up initially with the center pt of the cluster as the first point and a dummy point @ 0,0 for a placeholder
        var line = new esri.geometry.Polyline(map.spatialReference);
        line.addPath([graphic.geometry, new esri.geometry.Point(0, 0)]);

        //polyline graphic
        var lineGraphic = new esri.Graphic(line, lineSymbol);

        //creating a circle to evenly distribute our flare graphics around
        if (cSize > 1 && cSize <= this._flareLimit) {  //cSize > 1 may not be needed
            //takes the number of points (flares) for the cluster
            var numPoints = graphic.attributes.clusterSize;

            //takes the pixel distance from the center of the graphic to flare out the graphics
            var bufferDistance = this.getPixelDistanceFromCenter(graphic.geometry);

            //center of cluster graphic
            var centerPoint = graphic.geometry;
            
            //variables used to plot points evenly around the cluster
            var dblSinus, dblCosinus, x, y, pt, ptGraphic, p, l;

            for (var i = 0; i < numPoints; i++) {
                dblSinus = Math.sin((Math.PI * 2.0) * (i / numPoints));
                dblCosinus = Math.cos((Math.PI * 2.0) * (i / numPoints));

                x = centerPoint.x + bufferDistance * dblCosinus;
                y = centerPoint.y + bufferDistance * dblSinus;

                //constructing the flare graphic point
                pt = new esri.geometry.Point(x, y, this._map.spatialReference)
				var currentSymbol = new esri.symbol.PictureMarkerSymbol('/esrigis/structureTypeManager.do~action=displayIcon~id='+ graphic.attributes[i].Type_id, 21, 25);
              
                ptGraphic = new esri.Graphic(pt, currentSymbol, dojo.mixin({ baseGraphic: graphic },graphic.attributes[i]), this._infoTemplate);

                //try to always bring flare graphic to front of everything else
                p = this.add(ptGraphic);
                p.getDojoShape().moveToFront();

                //reset our 0,0 placeholder point in line to the actual point of the recently created flare graphic
                line.setPoint(0, 1, pt);
                lineGraphic = new esri.Graphic(line, lineSymbol, { baseGraphic: graphic });
                
                //try to always have connector line behind everything else
                l = this.add(lineGraphic);
                l.getDojoShape().moveToBack();

                //store flare graphic and connector graphic
                graphic.clusterGraphics.push(p);
                graphic.clusterGraphics.push(l);
            }
            
            //set "clustered" flag
            graphic.attributes.clustered = true;
			
        }
    },
    
	
	 showTooltip:function(evt){
        
		 
        var tipContent = "Click to spread out flares";
        var dialog = new dijit.TooltipDialog({
          id: "tooltipDialog",
          content: tipContent,
          style: "position: absolute; width: 160px; font-size:9px;z-index:100;background-image:none;"
        });
        dialog.startup();

        dijit.placeOnScreen(dialog.domNode, {x: evt.pageX, y: evt.pageY}, ["TL", "BL"], {x: 5, y: 5});
      },
	  
	  
	  closeDialog:function() {
        var widget = dijit.byId("tooltipDialog");
        if (widget) {
          widget.destroy();
        }
      },
	
    //helper method to figure out the distance in real world coordinates starting from a center pt and using a pixel distance
    getPixelDistanceFromCenter: function(centerGeom) {
        var distance = this._flareDistanceFromCenter;  //pixel distance from center
        var screenGeom = esri.geometry.toScreenGeometry(this._map.extent, this._map.width, this._map.height, centerGeom);
        screenGeom.x = screenGeom.x + distance;
        screenGeom.y = screenGeom.y + distance;
        var newDistance = esri.geometry.toMapGeometry(this._map.extent, this._map.width, this._map.height, screenGeom);
        var length = esri.geometry.getLength(centerGeom, newDistance);
        return length;
    },

    //fires when any cluster graphic (flare or individual) is moused out of
    //this utilizes the DelayedTask from ExtJS's library.  If anyone wants to re-write using Dojo, by all means...
    handleMouseOut: function(evt) {
		
        var graphic = evt.graphic,
            task;

        if (graphic.symbol.type == 'textsymbol') {
			if (graphic.attributes.baseGraphic){
				graphic=graphic.attributes.baseGraphic;
			}else{
            return;
			}
        }
		
        if (graphic.attributes.isCluster) {
            task = new DelayedTask(function(g) {
                this.removeFlareGraphics(g.clusterGraphics);
                delete g.clusterGraphics;
                g.attributes.clustered = false;
				this.closeDialog();
            }, this, [graphic]);
            task.delay(500);
            graphic.task = task;
        } else {
            if (graphic.attributes.baseGraphic) { 
			this.closeDialog();
			//cluster flare
                task = new DelayedTask(function(g) {
                    this.removeFlareGraphics(g.attributes.baseGraphic.clusterGraphics);
                    delete g.attributes.baseGraphic.clusterGraphics;
                    g.attributes.baseGraphic.attributes.clustered = false;
					
                }, this, [graphic]);
                task.delay(500);
                graphic.attributes.baseGraphic.task = task;
            }
            if (map.infoWindow.isShowing) {
                map.infoWindow.hide();
            }
        }
    },
	
	handleMouseClick: function(evt) {
        var graphic = evt.graphic;
		var textSymbol;
		if (graphic.symbol.type == 'textsymbol') {
			if (graphic.attributes.baseGraphic){
				textSymbol=graphic
				graphic=graphic.attributes.baseGraphic;
			}else{
            	return;
			}
        }
		
        var i=0;
        if (graphic.attributes.isCluster){
			
			graphic.hide()
				if(!textSymbol){
					textSymbol=graphic.attributes.textSymbol;
				}
			textSymbol.hide();
        	dojo.forEach(graphic.attributes,function(item){
				var pt=graphic.attributes[i].point;
				var currentSymbol = new esri.symbol.PictureMarkerSymbol('/esrigis/structureTypeManager.do~action=displayIcon~id='+ graphic.attributes[i].Type_id, 21, 25);
				
				var ptGraphic = new esri.Graphic(pt, currentSymbol, dojo.mixin({ baseGraphic: graphic },graphic.attributes[i]), this._infoTemplate);   
					
				var ptNumber=this._features.indexOf(pt);
				
				this.expandedPointsLayers.add(ptGraphic);
				for(var j in this._features){
        			if(this._features[j].attributes.index==pt.attributes.index){
	     	      		 this._features.splice(j,1);
    		        break;
            	}
		    }
				this.onClusterExpand();
					//console.log(this._features.length);
			      i++;	
				
        		
        	},this);
        	
        }else{
        this.showInfoWindow(graphic);
       
        }
    },
	
    clean : function(){
    	this.clear();
    	this.expandedPointsLayers.clear();
    	this._features = [];
    },
    
    resetCluster:function(){
    	dojo.forEach(this.expandedPointsLayers.graphics,function(item){
    		var point=item.geometry
    		var newPoint=dojo.clone(item.geometry); //clone in order to avoid the cricular reference issue
    		newPoint.attribues=dojo.mixin({},point.attributes);
            point.attributes=dojo.mixin({point:newPoint},point.attributes);
            
            this._features.push(point);
		},this);
    	//this.setFeatures(this._features);
    	this.expandedPointsLayers.clear();
    	this.clusterFeatures(true);
		},
    
    //removes the flare graphics from the map when a cluster graphic is moused out
    removeFlareGraphics: function(graphics) {
        if (graphics && graphics.length) {
            for (var i = 0; i < graphics.length; i++) {
                this.remove(graphics[i]);
            }
        }
    },
    
    //shows info window for specified graphic
    showInfoWindow: function(graphic) {
        if (map.infoWindow.isShowing) {
            map.infoWindow.hide();
        }
        map.infoWindow.setContent(graphic.getContent());
        map.infoWindow.setTitle(graphic.getTitle());
        map.infoWindow.resize(this._infoWindowWidth, this._infoWindowHeight);
        var graphicCenterSP = esri.geometry.toScreenGeometry(this._map.extent, this._map.width, this._map.height, graphic.geometry);
        map.infoWindow.show(graphicCenterSP, map.getInfoWindowAnchor(graphicCenterSP));
    },

    //core clustering function
    //right now, the clustering algorithim is based on the baseMap's tiling scheme (layerIds[0]).  as the comment says below, this can probably be substituted with an origin, array of grid pixel resolution & grid pixel size.
    //could probably be cleaned up and compacted a bit more.
    clusterFeatures: function(redraw) {
        this.clear();

        var df = dojox.lang.functional,
        	level = map.getLevel(),
        	extent =map.extent;

        var tileInfo = map.getLayer(map.layerIds[0]).tileInfo;  //get current tiling scheme.  This restriction can be removed.  the only thing required is origin, array of grid pixel resolution, & grid pixel size

        var toTileSpaceF = df.lambda("point, tileWidth,tileHeight,oPoint " 
        		+ "-> [Math.floor((oPoint.y - point.y)/tileHeight),Math.floor((point.x-oPoint.x)/tileWidth), point]");  //lambda function to map points to tile space

        var levelResolution = customLods[level].resolution;
        var width = levelResolution * tileInfo.width;
        var height = levelResolution * tileInfo.height;

        var toTileSpace = df.partial(toTileSpaceF, df.arg, width, height, tileInfo.origin);  //predefine width, height, origin point for toTileSpaceF function
        var extentTileCords = df.map([new esri.geometry.Point(extent.xmin, extent.ymin), new esri.geometry.Point(extent.xmax, extent.ymax)], toTileSpace);  //map extent corners to tile sapce
        var minRowIdx = extentTileCords[1][0];
        var maxRowIdx = extentTileCords[0][0];
        var minColIdx = extentTileCords[0][1];
        var maxColIdx = extentTileCords[1][1];

        //points to tiles
        if (!this.levelPointTileSpace[level] || redraw) {
            var pointsTileSpace = df.map(this._features, toTileSpace);  //map all points to tilespace
            var tileSpaceArray = [];
            dojo.forEach(pointsTileSpace, function(tilePoint, ptIndex) {  //swizel points[row,col,point] to row[col[points[]]]
                if (tileSpaceArray[tilePoint[0]]) {
                    var y = tileSpaceArray[tilePoint[0]];
                    if (y[tilePoint[1]]) {
                        y[tilePoint[1]].push(tilePoint[2]);
                    } else {
                        y[tilePoint[1]] = tilePoint[1];
                        y[tilePoint[1]] = [tilePoint[2]];
                    }
                } else {
                    var y = tileSpaceArray[tilePoint[0]] = [];
                    y[tilePoint[1]] = tilePoint[1];
                    y[tilePoint[1]] = [tilePoint[2]];
                }
            });
            this.levelPointTileSpace[level] = tileSpaceArray;  //once this has been computed store this in a level array
        }
        var tileCenterPointF = df.lambda("cPt,nextPt->{x:(cPt.x+nextPt.x)/2,y:(cPt.y+nextPt.y)/2}");

        dojo.forEach(this.levelPointTileSpace[level], function(row, rowIndex) {
            if ((rowIndex >= minRowIdx) & (rowIndex <= maxRowIdx)) {
                dojo.forEach(row, function(col, colIndex) {
                    if (col) {
                        if ((colIndex >= minColIdx) & (colIndex <= maxColIdx)) {
                            var pointsToBeAdded = [];
                            if (col.length > 2 && !this._showExpanded) { //clustered graphic

                                var tileCenterPoint = df.reduce(col, tileCenterPointF);
                                var sym;
                                if (col.length <= 15) {
                                    sym = this.symbolBank.less16;
                                } else if (col.length > 15 && col.length <= 30) {
                                    sym = this.symbolBank.less30;
                                } else if (col.length > 30 && col.length <= 50) {
                                    sym = this.symbolBank.less50;
                                } else {
                                    sym = this.symbolBank.over50;
                                }
                                
                                //get attributes for info window
                                var atts = dojo.map(col, function(item) {
                                    return item.attributes;
                                });
                                
                                //mixin attributes w/ other properties
                                var graphicAtts = dojo.mixin(atts, { isCluster: true, clusterSize: col.length });

                                //add cluster to map
								var clusterGraphic=new esri.Graphic(new esri.geometry.Point(tileCenterPoint.x, tileCenterPoint.y), sym, graphicAtts)
                            	
								
                                //initial testing w/ IE8 shows that TextSymbols are not displayed for some reason
                                //this may be an isolated issue.  more testing needed.
                                //it should work fine for IE7, FF, Chrome
								var clusterLabel=new esri.Graphic(new esri.geometry.Point(tileCenterPoint.x, tileCenterPoint.y), new esri.symbol.TextSymbol(col.length).setOffset(0, -5),{baseGraphic:clusterGraphic})
								clusterGraphic.attributes=dojo.mixin(clusterGraphic.attributes,{textSymbol:clusterLabel})
								this.add(clusterGraphic);
								this.add(clusterLabel);

                            } else { //single graphic
                                dojo.forEach(col, function(point) {
                                	var currentSymbol = new esri.symbol.PictureMarkerSymbol('/esrigis/structureTypeManager.do~action=displayIcon~id='+ point.attributes.Type_id, 21, 25);
                                    this.add(new esri.Graphic(point, currentSymbol, dojo.mixin({ isCluster: false },point.attributes), this._infoTemplate));
                                }, this);
                            }
                        }
                    }
                }, this);
            }
        }, this);
    }
});