Number.prototype.formatMoney = function (c, d, t) {
    var n = this,
        c = isNaN(c = Math.abs(c)) ? 2 : c,
        d = d == undefined ? "." : d,
        t = t == undefined ? "," : t,
        s = n < 0 ? "-" : "",
        i = parseInt(n = Math.abs(+n || 0).toFixed(c)) + "",
        j = (j = i.length) > 3 ? j % 3 : 0;
    return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
};

function formatDate(dString) {	
	var result
	if (dString == null) {
		result = ''
	} else {
		var i = dString.indexOf('T')
		if (i == -1) {
			result = dString
		} else {
			var d = dString.substring(0, i)
			var date = new Date(d)
			var day = date.getDate();
			var month = date.getMonth() + 1;
			var year = date.getFullYear();
			result = month + "/" + day + "/" + year.toString().substring(2);
		}
	}	
	return result
};

DelayedTask = function (fn, scope, args) {
    var me = this,
        id,
        call = function () {
            clearInterval(id);
            id = null;
            fn.apply(scope, args || []);
        };


    /**
     * Cancels any pending timeout and queues a new one
     * @param {Number} delay The milliseconds to delay
     * @param {Function} newFn (optional) Overrides function passed to constructor
     * @param {Object} newScope (optional) Overrides scope passed to constructor. Remember that if no scope
     * is specified, this will refer to the browser window.
     * @param {Array} newArgs (optional) Overrides args passed to constructor
     */
    me.delay = function (delay, newFn, newScope, newArgs) {
        me.cancel();
        fn = newFn || fn;
        scope = newScope || scope;
        args = newArgs || args;
        id = setInterval(call, delay);
    };


    /**
     * Cancel the last queued timeout
     */
    me.cancel = function () {
        if (id) {
            clearInterval(id);
            id = null;
        }
    };
};


define(["dojo/_base/declare", "dojo", "dojox/lang/functional", "dojox/lang/functional/curry",
    "dojox/lang/functional/fold", "dijit/TooltipDialog", "esri/layers/GraphicsLayer", "esri/graphic", "esri/symbols/SimpleMarkerSymbol",
    "esri/symbols/SimpleLineSymbol", "esri/geometry/Point", "esri/SpatialReference", "esri/symbols/TextSymbol", "dojo/_base/xhr"],
    function (declare, dojo, functional, curry, fold, TooltipDialog, GraphicsLayer, Graphic, SimpleMarkerSymbol, SimpleLineSymbol, Point, SpatialReference, TextSymbol, xhr) {
        return declare("clusterLayer", [GraphicsLayer], {
            constructor: function (options) {

                this.customLods = [
                    {"level":0, "resolution":156543.033928, "scale":591657527.591555},
                    {"level":1, "resolution":78271.5169639999, "scale":295828763.795777},
                    {"level":2, "resolution":39135.7584820001, "scale":147914381.897889},
                    {"level":3, "resolution":19567.8792409999, "scale":73957190.948944},
                    {"level":4, "resolution":9783.93962049996, "scale":36978595.474472},
                    {"level":5, "resolution":4891.96981024998, "scale":18489297.737236},
                    {"level":6, "resolution":2445.98490512499, "scale":9244648.868618},
                    {"level":7, "resolution":1222.99245256249, "scale":4622324.434309},
                    {"level":8, "resolution":611.49622628138, "scale":2311162.217155},
                    {"level":9, "resolution":305.748113140558, "scale":1155581.108577},
                    {"level":10, "resolution":152.874056570411, "scale":577790.554289},
                    {"level":11, "resolution":76.4370282850732, "scale":288895.277144},
                    {"level":12, "resolution":38.2185141425366, "scale":144447.638572},
                    {"level":13, "resolution":19.1092570712683, "scale":72223.819286},
                    {"level":14, "resolution":9.55462853563415, "scale":36111.909643},
                    {"level":15, "resolution":4.77731426794937, "scale":18055.954822},
                    {"level":16, "resolution":2.38865713397468, "scale":9027.977411},
                    {"level":17, "resolution":1.19432856685505, "scale":4513.988705},
                    {"level":18, "resolution":0.597164283559817, "scale":2256.994353},
                    {"level":19, "resolution":0.298582141647617, "scale":1128.497176}
                ];


                //basic esri.layers.GraphicsLayer option(s)
                this.displayOnPan = options.displayOnPan || false;
                this.fromProjectPage = options.fromProjectPage || false;

                //set the map
                //TODO: find a way to not pass in the map as a config option.
                this._map = options.map;
                this._extent = options.extent;
                this._mapview = options.mapview;
                this.id = "clusterLayer";

                //Expanded points layer
                var expandedPointsLayers = new GraphicsLayer();
                
                this._expandedPointsLayers = expandedPointsLayers;
                
                this._map.addLayer(this._expandedPointsLayers);
                //base connections to update clusters during user/map interaction
                dojo.connect(this._map, 'onZoomStart', dojo.hitch(this, this.handleMapZoomStart));
                dojo.connect(this._map, 'onExtentChange', dojo.hitch(this, this.handleMapExtentChange));

                //may or may not be needed. not sure
                //this.spatialReference = new SpatialReference({ wkid: 4326 });
                this.initialExtent = (this.fullExtent = this._map.geographicExtent);

                //this could be moved to sit within a function
                this.levelPointTileSpace = [];
                this._lods = this.customLods;
                //holds all the features for this cluster layer
                this._features = [];
                this._featuresMecator = [];

                //set incoming features
                //this will throw an error if the features WKID is not in below list.
                //projected: 102100, 102113
                //geographic: 4326, 4269, 4267
                //TODO: add more supported WKIDs?  read what the map WKID is and only allow that?
                //TODO: throw error and NOT continue to add layer to map.  right now, if error is thrown, an empty graphics layer will be added to the map
                try {
                    this.setFeatures(options.features);
                } catch (ex) {
                    //alert(ex);
                }
                
                

                //connects for cluster layer itself that handles the loading and mouse events on the graphics
                dojo.connect(this, 'onLoad', this.handleLayerLoaded);
                dojo.connect(this, 'onMouseOver', this.handleMouseOver);
                dojo.connect(this._expandedPointsLayers, 'onClick', this.handleMouseOver);
                dojo.connect(this, 'onMouseOut', this.handleMouseOut);

                //Expanded points layer
                dojo.connect(this, 'onClick', this.handleMouseClick);
                dojo.connect(this, "onVisibilityChange", function (visible) {
                    if (visible) {
                        expandedPointsLayers.show();
                    } else {
                        expandedPointsLayers.hide();
                    }
                });

                //default symbol bank for clusters and single graphics
                //TODO: allow for user supplied symbol bank to override.  just use an ESRI renderer somehow?
                this.symbolBank = {
                    "single": new SimpleMarkerSymbol(SimpleMarkerSymbol.STYLE_CIRCLE, 10,
                        new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, new dojo.Color([255, 255, 255,0.7]), 1),
                        new dojo.Color([240, 119, 0, 1])),

                    "less16": new SimpleMarkerSymbol(SimpleMarkerSymbol.STYLE_CIRCLE, 20,
                        new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, new dojo.Color([255, 255, 255,1]), 1),
                        new dojo.Color([240, 119, 0, 1])),

                    "less30": new SimpleMarkerSymbol(SimpleMarkerSymbol.STYLE_CIRCLE, 30,
                        new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, new dojo.Color([255, 255, 255,1]), 1),
                        new dojo.Color([240, 119, 0, 1])),

                    "less50": new SimpleMarkerSymbol(SimpleMarkerSymbol.STYLE_CIRCLE, 30,
                        new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, new dojo.Color([255, 255, 255,1]), 1),
                        new dojo.Color([240, 119, 0, 1])),

                    "over50": new SimpleMarkerSymbol(SimpleMarkerSymbol.STYLE_CIRCLE, 45,
                        new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, new dojo.Color([255, 255, 255,1]), 1),
                        new dojo.Color([240, 119, 0, 1]))

                };
                
                
                //how far away the flare will be from the center of the cluster in pixels - Number
                this._flareDistanceFromCenter = options.flareDistanceFromCenter || 20;

                //the number of flare graphics to limit the cluster to - Number
                this._flareLimit = options.flareLimit || 20;

                //info template for all single graphics and flare graphics - esri.InfoTemplate
                //this._infoTemplate = options.infoWindow.template || null;

                //info window width - Number
                //this._infoWindowWidth = options.infoWindow.width || 150;

                //info window height - Number
                //this._infoWindowHeight = options.infoWindow.height || 100;

                //following the basics of creating a custom layer
                this.restoreSavedPoints()
                
                this.loaded = true;
                this.onLoad(this);

            },

            //clear all graphics when zoom starts
            handleMapZoomStart: function () {
                this.clear();
            },

            //re-cluster on extent change
            //TODO: maybe only use features that fall within current extent?  Add that as an option?
            handleMapExtentChange: function (extent, delta, leveChange, lod) {
                if (this._map) {
                    //this.clusterFeatures();
                    if (this._haveExpandedPoints) {
                        //this.resetCluster();
                    }
                    this._mapview.setExpandedPointsLayers([]);
                }
            },

            clearAll: function () {
                this.clear();
                this._expandedPointsLayers.clear();
            },
            saveExpandedPointsOnMapModel: function (){
               var coordinates = this._mapview.getExpandedPointsLayers()
               dojo.forEach(this._expandedPointsLayers.graphics, function (item) {                  
                  var pt = item.geometry;                                   
                  coordinates.push({"x":pt.x, "y":pt.y});                 
              }, this);
              this._mapview.setExpandedPointsLayers(coordinates)                
            },
            saveClusterOnMapModel: function (x,y){
              var coordinates = this._mapview.getExpandedPointsLayers()                                              
              coordinates.push({"x":x, "y":y, 'isCluster':true});               
              this._mapview.setExpandedPointsLayers(coordinates)                
            },
            
            restoreSavedPoints: function (){
               var self = this;
               var i = 0;
               dojo.forEach(self._mapview.getExpandedPointsLayers(), function (point) {            
                  for (var j in self._features) {
                      var pt = self._features[j];                      
                         if (pt.x == point.x && pt.y == point.y ) {                            
                                var ptGraphic = new Graphic(pt, self.symbolBank.single, dojo.mixin(pt.attributes, { isExpanded: true }));
                                self._expandedPointsLayers.add(ptGraphic);
                              self._features.splice(j, 1);                               
                               i++;
                               break;
                            }
                        }                 
              }, this);  
              this._haveExpandedPoints = (i > 0);
            },

            //this function may not be needed exactly as is below.  somehow, the attributes need to be mapped to the points.
            setFeatures: function (features) {
                var i = 0;
                this._features = [];
                var wkid = features[0].geometry.spatialReference.wkid;
                if (wkid != 102100) {
                    if (wkid == 4326 || wkid == 4269 || wkid == 4267) {
                        dojo.forEach(features, function (feature) {
                            point = feature.geometry;
                            var newPoint = dojo.clone(point); //clone in order to avoid the cricular reference issue
                            newPoint.attribues = dojo.mixin({index: i}, feature.attributes);

                            point.attributes = dojo.mixin({point: newPoint, index: i}, feature.attributes);
                            this._features.push(point);
                            var mercatorPoint = esri.geometry.geographicToWebMercator(point);
                            mercatorPoint.attributes = dojo.mixin({point: newPoint, index: i}, feature.attributes);
                            this._featuresMecator.push(mercatorPoint);
                            i++;
                        }, this);
                    } else {
                        throw 'Input Spatial Reference Must Be in Either WKID: 102110 or WKID: 4326';
                        return;
                    }
                } else {
                    dojo.forEach(features, function (feature) {
                        point = feature.geometry;

                        var newPoint = dojo.clone(point);
                        newPoint.attributes = dojo.mixin({index: i}, feature.attributes);

                        point.attributes = dojo.mixin({point: newPoint, index: i}, feature.attributes);
                        this._features.push(point);
                        i++;
                    }, this);
                }
            },

            //fires when cluster layer is loaded, but not added to map yet.
            handleLayerLoaded: function (lyr) {
                if (this._map) {
                    this.clusterFeatures();
                }
            },        
            //fires when any graphic (clustered or single) is moused over
            handleMouseOver: function (evt) {
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
                } else if (graphic.attributes.isExpanded) {
                    this._map.getLayer('clusterLayer').showInfoWindow(graphic);
                }
                else { //single marker or cluster flare mouse over
                    if (!graphic.attributes.isExpanded && graphic.attributes.baseGraphic) { //cluster flare
                        graphic.attributes.baseGraphic.task.cancel();
                    }
                    //this.showInfoWindow(graphic);
                    return;
                }

                
                graphic.clusterGraphics = [];

                var cSize = graphic.attributes.clusterSize;
                var lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.STYLE_SHORTDASHDOTDOT, new dojo.Color([171, 65, 0, 1]), 2);

                //polyline used to "tie" flare to cluster
                //set up initially with the center pt of the cluster as the first point and a dummy point @ 0,0 for a placeholder
                var line = new esri.geometry.Polyline(new SpatialReference({ wkid: 4326 }));
                line.addPath([graphic.geometry, new Point(0, 0)]);

                //polyline graphic
                var lineGraphic = new Graphic(line, lineSymbol);

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
                        pt = new Point(x, y, new SpatialReference({ wkid: 4326 }));
                        ptGraphic = new Graphic(pt, this.symbolBank.single, dojo.mixin(graphic.attributes[i]));

                        //try to always bring flare graphic to front of everything else
                        p = this.add(ptGraphic);
                        p.getDojoShape().moveToFront();

                        //reset our 0,0 placeholder point in line to the actual point of the recently created flare graphic
                        line.setPoint(0, 1, pt);
                        lineGraphic = new Graphic(line, lineSymbol, { baseGraphic: graphic });

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

            resetCluster: function () {
                dojo.forEach(this._expandedPointsLayers.graphics, function (item) {
                    var point = item.geometry
                    var newPoint = dojo.clone(item.geometry); //clone in order to avoid the cricular reference issue
                    newPoint.attribues = dojo.mixin({}, point.attributes);
                    point.attributes = dojo.mixin({point: newPoint}, point.attributes);

                    this._features.push(point);
                }, this);
                //this.setFeatures(this._features);
                this._expandedPointsLayers.clear();
                this.clusterFeatures(true);
            },

            handleMouseClick: function (evt) {
                var graphic = evt.graphic;
                var textSymbol;
                if (graphic.symbol.type == 'textsymbol') {
                    if (graphic.attributes.baseGraphic) {
                        textSymbol = graphic;
                        graphic = graphic.attributes.baseGraphic;
                    } else {
                        return;
                    }
                }

                var i = 0;
                if (graphic.attributes.isCluster) {                   
                    this.saveClusterOnMapModel(graphic.geometry.x,graphic.geometry.y);                   
                    graphic.hide();
                    if (!textSymbol) {
                        textSymbol = graphic.attributes.textSymbol;
                    }
                    textSymbol.hide();

                    dojo.forEach(graphic.attributes, function (item) {
                        var pt = graphic.attributes[i].point;                       
                        var ptGraphic = new Graphic(pt, this.symbolBank.single, dojo.mixin(graphic.attributes[i], {isExpanded: true}));
                        this._expandedPointsLayers.add(ptGraphic);
                        for (var j in this._features) {
                            if (this._features[j].attributes.index == pt.attribues.index) {
                                this._features.splice(j, 1);
                                break;
                            }
                        }
                        i++;
                    }, this);
                    this._haveExpandedPoints = true;
                    this._expandedPointsLayers.show();
                    this.saveExpandedPointsOnMapModel()
                    this.removeFlareGraphics(graphic);
                } else {
                    this.showInfoWindow(graphic);
                }
            },

            //helper method to figure out the distance in real world coordinates starting from a center pt and using a pixel distance
            getPixelDistanceFromCenter: function (centerGeom) {
                var distance = this._flareDistanceFromCenter;  //pixel distance from center
                //var screenGeom = esri.geometry.toScreenGeometry(this._map.extent, this._map.width, this._map.height, centerGeom);
                var screenGeom = esri.geometry.toScreenGeometry(this._map.extent, this._map.width, this._map.height, esri.geometry.geographicToWebMercator(centerGeom));
                screenGeom.x = screenGeom.x + distance;
                screenGeom.y = screenGeom.y + distance;
                var newDistance = esri.geometry.toMapGeometry(this._map.extent, this._map.width, this._map.height, screenGeom);
                var length = esri.geometry.getLength(centerGeom, esri.geometry.webMercatorToGeographic(newDistance));
                return length;
            },

            //fires when any cluster graphic (flare or individual) is moused out of
            //this utilizes the DelayedTask from ExtJS's library.  If anyone wants to re-write using Dojo, by all means...
            handleMouseOut: function (evt) {            	
                var graphic = evt.graphic,
                    task;

                if (graphic.symbol.type == 'textsymbol') {
                    return;
                }

                if (graphic.attributes.isCluster) {
                    task = new DelayedTask(function (g) {
                        this.removeFlareGraphics(g.clusterGraphics);
                        delete g.clusterGraphics;
                        g.attributes.clustered = false;
                    }, this, [graphic]);
                    task.delay(800);
                    graphic.task = task;                   
                } else {
                    if (graphic.attributes.baseGraphic) { //cluster flare
                        task = new DelayedTask(function (g) {
                            this.removeFlareGraphics(g.attributes.baseGraphic.clusterGraphics);
                            delete g.attributes.baseGraphic.clusterGraphics;
                            g.attributes.baseGraphic.attributes.clustered = false;
                        }, this, [graphic]);
                        task.delay(800);
                        graphic.attributes.baseGraphic.task = task;
                    }
                    /*if (this._map.infoWindow.isShowing) {
                        this._map.infoWindow.hide();
                    } */
                }
            },

            //removes the flare graphics from the map when a cluster graphic is moused out
            removeFlareGraphics: function (graphics) {
                if (graphics && graphics.length) {
                    for (var i = 0; i < graphics.length; i++) {
                        this.remove(graphics[i]);
                    }
                }
            },

            //shows info window for specified graphic
            showInfoWindow: function (graphic) {
                if (!this.fromProjectPage){
                    if (this._map.infoWindow.isShowing) {
                        this._map.infoWindow.hide();
                    }
                    this._map.infoWindow.setTitle('Project Information');
                    this.getProjectInfo(graphic.attributes.id, graphic);
                }
            },

            getProjectInfo: function (id, g) {
                var self = this;
                if (this._request){
                    this._request.abort();
                }

                self._request = $.ajax({
                    url: app.API_ROOT + "/aid/project/"+id,
                    type: "GET",
                    dataType: 'json',
                    success: function (data) {
                        self.showPopUp(data,g);
                    }
                });

            },

            showPopUp: function (result, graphic) {
               $("#popup-chart-container").remove();                
               var recipients = this.getRecipientNames(result)
               var donors = this.getDonorNames(result)
               var commitment = 0
               
               var donorList = new Array()
               for(var i =0; i < result.transactions.length;i++)
               {             
                 if(result.transactions[i].tr_type_id == 1)
                 {
                    if(result.transactions[i].tr_funding_org != undefined && donorList.indexOf(result.transactions[i].tr_funding_org.name) == -1){
                       commitment = commitment + ((result.transactions[i].tr_constant_value != undefined) ?  result.transactions[i].tr_constant_value : 0)
                       donorList.push(result.transactions[i].tr_funding_org.name)
                       }  
                 }                                                  
               }              
               commitment = (commitment!='' && commitment!=0) ? commitment.formatMoney(2, '.', ',') : '';
               var currency = (commitment!='' && commitment!=0) ? "USD" : '';
               var sector = (result.sector3)? result.sector3.name:'';
               var endDate = formatDate(result.end_Date);
               var startDate = formatDate(result.start_Date);
               var projectURL = this.getBaseURL() +'dashboard#/project/' + result.project_id
               var content = '<div id="titleSection"><a href="' + projectURL + '" target="_blank">'+ result.title +'</a></div>' +
                    '<table class="attrTable" cellpadding="0px" cellspacing="0px"><tbody>'+
                   '<tr valign="top" class="alt"><td colspan="2" class="attrName">Donor (s)</td><td colspan="2" class="attrValue">'+  donors + '</td></tr>'+
                   '<tr valign="top"><td colspan="2" class="attrName">Sector</td><td colspan="2" class="attrValue">' +    sector  + '</td></tr>' +
                   '<tr valign="top" class="alt"><td colspan="2" class="attrName">Commitment</td><td colspan="2" class="attrValue">' + currency + ' ' + commitment +  '</td></tr>' +
                   '<tr valign="top"><td class="attrName" style="width:15%;">Start Date</td><td class="attrValue" style="width:35%;">'+ startDate +'</td><td class="attrName endDate" style="width:15%;">End Date</td><td class="attrValue" style="width:35%;">' + endDate + '</td></tr>' +
                   '<tr valign="top" class="alt"><td colspan="2" class="attrName">Recipient</td><td colspan="2" class="attrValue">'+ recipients +'</td></tr>' +
                   '</tbody></table>';
                 
               this._map.infoWindow.setTitle('Project Information');
               this._map.infoWindow.resize(450,300);
               this._map.infoWindow.setContent(content);
               this._map.infoWindow.show(graphic.geometry);
               $('.sizer.content').show();
           },
           getBaseURL:function() {              
              if(document.referrer.length>0){
                var urlSplits = document.referrer.split("/")               
                return urlSplits[0] + "//" + urlSplits[2] + "/";                
              }              
              return location.protocol + "//" + location.hostname + "/";
           },
                      
            getDonorNames:function(result) {
                if (!_.isUndefined(result.transactions) && result.transactions.length > 0) {
                    var donorList= _.pluck(result.transactions, "tr_provider");                
                    if (donorList [0] != undefined && donorList[0] != null) {
                        var donorNameList = _.uniq(_.pluck(_.pluck(result.transactions, "tr_provider"), "name"));
                        if (donorNameList.length > 0)
                            return donorNameList.join(", ");
                        else
                            return "N/A";
                    }                
                    donorList = _.pluck(result.transactions, "tr_funding_org")
                    if (donorList [0] != undefined && donorList[0] != null) {
                    	var donorNameList = _.uniq(_.pluck(_.pluck(result.transactions, "tr_funding_org"), "name"));
                        if (donorNameList.length > 0)
                            return donorNameList.join(", ");
                        else
                            return "N/A";
                    }                
                    donorList = _.pluck(result.transactions, "tr_funding_region")
                    if (donorList [0] != undefined && donorList[0] != null) {
                    	var donorNameList = _.uniq(_.pluck(_.pluck(result.transactions, "tr_funding_region"), "name"));
                        if (donorNameList.length > 0)
                            return donorNameList.join(", ");
                        else
                            return "N/A";
                    }
                    
                }
                else
                    return "";
            },
            getRecipientNames:function (result) {
                if (!_.isUndefined(result.transactions) && result.transactions.length > 0) {
                    var receiverList = _.pluck(result.transactions, "tr_receiver_country");
                    if (receiverList [0] != undefined) {
                        var receiverNameList = _.uniq(_.pluck(_.pluck(result.transactions, "tr_receiver_country"), "name"));
                        if (receiverNameList.length > 0)
                            return receiverNameList;
                        else
                            return "N/A";
                    }                
                    receiverList = _.pluck(result.transactions, "tr_receiver_region");
                    if (receiverList [0] != undefined) {
                        var receiverNameList = _.uniq(_.pluck(_.pluck(result.transactions, "tr_receiver_region"), "name"));
                        if (receiverNameList.length > 0)
                            return receiverNameList;
                        else
                            return "N/A";
                    }             
                    
                }
                else
                    return "";

            },
            checkIfExpanded: function (x,y) {
               var isExpanded = false;
               var points = this._mapview.getExpandedPointsLayers()
               for(var i = 0;i<points.length;i++)
               {
                   if (points[i].x == x && points[i].y == y)
                   {
                      isExpanded = true;
                      break;
                   }                   
               }
               return isExpanded;
            },
            //core clustering function
            //right now, the clustering algorithim is based on the baseMap's tiling scheme (layerIds[0]).  as the comment says below, this can probably be substituted with an origin, array of grid pixel resolution & grid pixel size.
            //could probably be cleaned up and compacted a bit more.
            clusterFeatures: function (redraw, map) {
                this.clear();
                if (map) {
                    this._map = map;
                }
                var df = functional;
                var level = this._map.getLevel() + 2;
                var extent = null;
                //Hack to fix the geographic Extent issue when the extent is set by esri.graphicsExtent
                if (this._extent){
                    extent = this._extent;
                    level = this._map._fixExtent(extent).lod.level
                }else{
                    extent = this._map.geographicExtent;
                }

                var tileInfo = this._map.getLayer(this._map.layerIds[0]).tileInfo;  //get current tiling scheme.  This restriction can be removed.  the only thing required is origin, array of grid pixel resolution, & grid pixel size
                var toTileSpaceF = df.lambda("point, tileWidth,tileHeight,oPoint "
                    + "-> [Math.floor((oPoint.y - point.y)/tileHeight),Math.floor((point.x-oPoint.x)/tileWidth), point]");  //lambda function to map points to tile space
                var levelResolution = this._lods[level].resolution;
                //var levelResolution = tileInfo.lods[level].resolution;
                var width = levelResolution * tileInfo.width;
                var height = levelResolution * tileInfo.height;
                //console.log('level= ' + level);
                //console.log('levelResolution= ' + levelResolution);
                //console.log('level resolution= '+levelResolution);
                //console.log('width resolution= '+width);
                //console.log('height resolution= '+height);

                var toTileSpace = df.partial(toTileSpaceF, df.arg, width, height, tileInfo.origin);  //predefine width, height, origin point for toTileSpaceF function
                var extentTileCords = df.map([esri.geometry.geographicToWebMercator(new Point(extent.xmin, extent.ymin)), esri.geometry.geographicToWebMercator(new Point(extent.xmax, extent.ymax))], toTileSpace);  //map extent corners to tile sapce

                var minRowIdx = extentTileCords[1][0];
                var maxRowIdx = extentTileCords[0][0];
                var minColIdx = extentTileCords[0][1];
                var maxColIdx = extentTileCords[1][1];

                //points to tiles
                if (!this.levelPointTileSpace[level] || redraw) {
                    var pointsTileSpace = df.map(this._featuresMecator, toTileSpace);  //map all points to tilespace
                    var tileSpaceArray = [];
                    dojo.forEach(pointsTileSpace, function (tilePoint, ptIndex) {  //swizel points[row,col,point] to row[col[points[]]]
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

                dojo.forEach(this.levelPointTileSpace[level], function (row, rowIndex) {
                    if ((rowIndex >= minRowIdx) & (rowIndex <= maxRowIdx)) {
                        dojo.forEach(row, function (col, colIndex) {
                            if (col) {
                                if ((colIndex >= minColIdx) & (colIndex <= maxColIdx)) {
                                    var pointsToBeAdded = [];
                                    if (col.length > 2) { //clustered graphic                                       
                                                                         
                                       
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
                                        var atts = dojo.map(col, function (item) {
                                            return item.attributes;
                                        });

                                        //mixin attributes w/ other properties
                                        var graphicAtts = dojo.mixin(atts, { isCluster: true, clusterSize: col.length });

                                        //add cluster to map
                                        //var paux =  new Point(tileCenterPoint.x, tileCenterPoint.y);
                                        var clusterGraphic = new Graphic(esri.geometry.webMercatorToGeographic(new Point(tileCenterPoint.x, tileCenterPoint.y)), sym, graphicAtts);
                                        //this.add(new Graphic(new Point(tileCenterPoint.x, tileCenterPoint.y), sym, graphicAtts));

                                        //initial testing w/ IE8 shows that TextSymbols are not displayed for some reason
                                        //this may be an isolated issue.  more testing needed.
                                        //it should work fine for IE7, FF, Chrome
                                        //var paux_1 =  new Point(tileCenterPoint.x, tileCenterPoint.y);

                                        //this.add(new Graphic(new Point(tileCenterPoint.x, tileCenterPoint.y), new esri.symbol.TextSymbol(col.length).setOffset(0, -5)));

                                        var clusterLabel = new Graphic(esri.geometry.webMercatorToGeographic(new Point(tileCenterPoint.x, tileCenterPoint.y)),
                                            new TextSymbol(col.length).setOffset(0, -5).setColor(new dojo.Color([255,255,255])), {baseGraphic: clusterGraphic});
                                        clusterGraphic.attributes = dojo.mixin(clusterGraphic.attributes, {textSymbol: clusterLabel});
                                        var expanded =  this.checkIfExpanded(clusterLabel.geometry.x,clusterLabel.geometry.y) || this.checkIfExpanded(clusterGraphic.geometry.x,clusterGraphic.geometry.y)
                                        if(expanded != true){                                           
                                            this.add(clusterGraphic);
                                            this.add(clusterLabel);
                                        }     
                                        //console.log("x" + clusterGraphic.geometry.x)
                                        //console.log("y" + clusterGraphic.geometry.y)
                                        //console.log("x" + clusterLabel.geometry.x)
                                        //console.log("y" + clusterLabel.geometry.y)

                                    } else { //single graphic
                                        dojo.forEach(col, function (point) {
                                            this.add(new Graphic(point, this.symbolBank.single, dojo.mixin(point.attributes, { isCluster: false })));
                                        }, this);
                                    }
                                }
                            }
                        }, this);
                    }
                }, this);
            }
        });
    });


