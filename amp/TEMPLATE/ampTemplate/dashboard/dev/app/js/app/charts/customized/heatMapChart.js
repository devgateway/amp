nv.models.heatmap = function() {
    "use strict";

    //============================================================
    // Public Variables with Default Settings
    //------------------------------------------------------------

    var margin = {top: 0, right: 0, bottom: 0, left: 0}
        , width = 500
        , height = 500
        , getX = function(d) { return d.x }
        , getY = function(d) { return d.y }
        , id = Math.floor(Math.random() * 10000) //Create semi-unique ID in case user doesn't select one
        , duration = 250
        , dispatch = d3.dispatch('chartClick', 'elementClick', 'elementDblClick', 'elementMouseover', 'elementMouseout', 'renderEnd')
        ;


    //============================================================
    // chart function
    //------------------------------------------------------------

    var renderWatch = nv.utils.renderWatch(dispatch);

    function chart(selection) {
        renderWatch.reset();
        renderWatch.renderEnd('heatmap immediate');
        return chart;
    }

    //============================================================
    // Expose Public Variables
    //------------------------------------------------------------

    chart.dispatch = dispatch;
    chart.options = nv.utils.optionsFunc.bind(chart);

    chart._options = Object.create({}, {
        // simple options, just get/set the necessary values
        width:      {get: function(){return width;}, set: function(_){width=_;}},
        height:     {get: function(){return height;}, set: function(_){height=_;}},
        x:          {get: function(){return getX;}, set: function(_){getX=_;}},
        id:         {get: function(){return id;}, set: function(_){id=_;}},

        // options that require extra logic in the setter
        margin: {get: function(){return margin;}, set: function(_){
            margin.top    = typeof _.top    != 'undefined' ? _.top    : margin.top;
            margin.right  = typeof _.right  != 'undefined' ? _.right  : margin.right;
            margin.bottom = typeof _.bottom != 'undefined' ? _.bottom : margin.bottom;
            margin.left   = typeof _.left   != 'undefined' ? _.left   : margin.left;
        }},
        y: {get: function(){return getY;}, set: function(_){
            getY=d3.functor(_);
        }}
    });

    nv.utils.initOptions(chart);
    return chart;
};

nv.models.heatMapChart = function() {
	"use strict";

	//============================================================
    // Public Variables with Default Settings
    //------------------------------------------------------------	

    var heatmap = nv.models.heatmap();

    var margin = {top: 30, right: 20, bottom: 20, left: 20}
    var width = null;
	var height = null;
    var showLegend = false;
    var color = nv.utils.defaultColor();
    var	state = nv.utils.state();
    var defaultState = null;
    var noData = "No Data Available.";
    var duration = 250;
    var dispatch = d3.dispatch('tooltipShow', 'tooltipHide', 'stateChange', 'changeState','renderEnd');
    var shortTextLength = 17;
    var innerMargin = {
    		top : 120,
			right : 0,
			bottom : 100,
			left : 150
    };
    
    const initialWidth = 960;
    const initialHeight = 400;
    const containerWidth = 1024;

    //============================================================
    // Private Variables
    //------------------------------------------------------------
    var renderWatch = nv.utils.renderWatch(dispatch);

    var stateGetter = function(data) {
    	return function(){
    		return {
    			active: data.map(function(d) { return !d.disabled })
            };
        }
    };

    var stateSetter = function(data) {
        return function(state) {
            if (state.active !== undefined) {
                data.forEach(function (series, i) {
                    series.disabled = !state.active[i];
                });
            }
        }
    };
  
    var shortenText = function(text, length) {
    	if (text.length > length) {
    		text = text.substring(0, length) + '...';
    	}
    	return text;
    };

    //============================================================
    // Chart function
    //------------------------------------------------------------

    function chart(selection) {
    	var _self = this;
    	this.rendered = false;
    	var _ = require('underscore'); // This doesnt works on top of the file :(((

        renderWatch.reset();
        renderWatch.models(heatmap);

        selection.each(function(data) {
        	// Get currency for later.        	       	
        	var currencyId = app.settingsWidget.definitions.getSelectedOrDefaultCurrencyId();
        	var selectedCurrency = app.settingsWidget.definitions.findCurrencyById(currencyId).value;        	
        	var newShortTextLength = !data[0].values.model.get('showFullLegends') ? shortTextLength : 100;
        	
        	var container = d3.select(this);
            nv.utils.initSVG(container);

            var availableWidth = (width || parseInt(container.style('width'), 10) || initialWidth)
                    - margin.left - margin.right;
            var availableHeight = (height || parseInt(container.style('height'), 10) || initialHeight)
                    - margin.top - margin.bottom;

            chart.container = this;

            state.setter(stateSetter(data), chart.update)
                .getter(stateGetter(data))
                .update();

            //set state.disabled
            state.disabled = data.map(function(d) { return !!d.disabled });

            if (!defaultState) {
                var key;
                defaultState = {};
                for (key in state) {
                    if (state[key] instanceof Array)
                        defaultState[key] = state[key].slice(0);
                    else
                        defaultState[key] = state[key];
                }
            }

        	var cubeSize = 30;
        	var width = containerWidth - innerMargin.left - innerMargin.right;
        	var topSectionHeight = 180;
        	var legendSectionHeight = 20;
        	var height = topSectionHeight + (cubeSize * data[0].values.y.length) + legendSectionHeight;
        	var legendElementHeight = 22;
        	const undefinedColor = '#666';
        	const noColor = '#FFFFFF';
        	var categories = getCategoriesByThreshold(undefinedColor, noColor, data[0].values.model);
        	
        	$(container[0]).css('height', height + 'px').attr('class', 'dash-chart nvd3-svg heatmap-chart');
        	
        	var svg = container
        		.append("g")
        		.attr("transform", "translate(" + innerMargin.left + "," + innerMargin.top + ")")
        		.attr("class", "heatmap-main-container");
        	
        	// Add SVG filter for cell highlight.
        	// For more info about these filters see: http://www.svgbasics.com/filters4.html // http://apike.ca/prog_svg_filter_feColorMatrix.html // http://alistapart.com/article/finessing-fecolormatrix
        	svg.append("defs").append("filter").attr("id", "filterSaturate").append("feColorMatrix").attr("in", "SourceGraphic").attr("type", "saturate").attr("values", "5");
        	svg.append("defs").append("filter").attr("id", "filterLuminanceToAlpha").append("feColorMatrix").attr("in", "SourceGraphic").attr("type", "luminanceToAlpha");
        	svg.append("defs").append("filter").attr("id", "filterBlur").append("feGaussianBlur").attr("in", "SourceGraphic").attr("stdDeviation", "2");
        	svg.append("defs").append("filter").attr("id", "filterDarken").append("feColorMatrix").attr("type", "matrix").attr("values", "0.5 0 0 0 0 0 0.5 0 0 0 0 0 0.5 0 0 0 0 0 1 0");

        	// Rows container.
        	var yAxisLabelsContainer = svg        	
				.append("g")
				.attr("class", "heatmap-yAxis-container");
        	
        	// Rows.
        	var yAxisLabels = yAxisLabelsContainer
        		.selectAll(".yLabel")
        		.data(data[0].values.y)
        		.enter()
        		.append("text")
        		.text(function(d) {
        			return shortenText(d, newShortTextLength);
        		})
        		.attr("x", 0)
        		.attr("y", function(d, i) {
        			return i * cubeSize;
        		})
        		.style("text-anchor", "end")
        		.attr("transform", "translate(-6," + cubeSize / 1.5 + ")")
        		.attr("class", function(d, i) {
        							return "yLabel mono axis nv-series";
        		})
        		.attr('data-title', function(d) {
        			return d;
        		});
        	
        		// Format "Others" special row if needed.
        		if (data[0].values.yCount < data[0].values.yTotalCount) {
        			var self = this;
        			var data2 = data;
        			var textElement = $(container[0]).find('.yLabel').last();
        			$(textElement).attr("class", function(d, i) {
        				return $(textElement).attr('class') + ' legend-others';
        			});
        		}
        	
        		// Add Totals special row.
        		yAxisLabelsContainer.append("text")
        			.text("TOTALS")
        			.attr("x", 0)
        			.attr("y", (data[0].values.y.length * cubeSize))
        			.attr("class", "yLabel mono axis nv-series heatmap-totals")
        			.style("text-anchor", "end")
        			.style("font-weight", "bold")
            		.attr("transform", "translate(-6," + cubeSize / 1.5 + ")")
            		.attr('data-i18n', 'amp.dashboard:chart-heatmap-totals');

        		// Columns container.
        		var xAxisLabelsContainer = svg
        			.append("g")
        			.attr("transform", "translate(18.5, -5)")
        			.attr("class", "heatmap-xAxis-container");

        		// Columns
        		// Notice in transform functions the order is very important if you have to apply more than one, like translate and rotate.
        		// cubeSize is where we define how big are the cubes so if we change it in the future the chart will resize correctly.
        		var xAxisLabels = xAxisLabelsContainer
        			.selectAll(".xLabel")
        			.data(data[0].values.x)
        			.enter()
        			.append("text")
        			.text(function(d) {
        				return shortenText(d, newShortTextLength);
        			})
        			.attr("x", function(d, i) {
        				return i * cubeSize;
        			})
        			.attr("y", 0)
        			.attr("transform", function(d, i) {
        				return "rotate(270, " + (cubeSize * i) + ", 0)";
        			})
        			.attr("class", function(d, i) {
        							return "xLabel mono axis nv-series";
        			})
        			.attr('data-title', function(d) {
        				return d;
        			});
        		
        		// Add Totals special column.
        		xAxisLabelsContainer.append("text")
        			.text("TOTALS")
        			.attr("x", cubeSize * data[0].values.x.length)
        			.attr("y", 0)
        			.attr("class", "xLabel mono axis nv-series heatmap-totals")
        			.style("font-weight", "bold")
            		.attr("transform", function(d, i) {
        				return "rotate(270, " + (cubeSize * data[0].values.x.length) + ", 0)";
        			})
        			.attr('data-i18n', 'amp.dashboard:chart-heatmap-totals');

        		// Cubes
        		var cubesContainer = svg
    				.append("g")
    				.attr("class", "heatmap-cubes-container");
        		for (var i = 0; i < data[0].values.length; i++) {
        			createCube(cubesContainer, $.extend(data[0].values[i], {tooltip: selectedCurrency + ' ' + data[0].values[i].amount}), cubeSize, noColor, categories);        			    				
        		}        
        		// Add total's row in the end.        		
        		for (var i = 0; i < data[0].values.x.length; i++) {
        			createCube(cubesContainer, {x: i + 1, y: data[0].values.y.length + 1, value: data[0].values.xPTotals[i], tooltip: selectedCurrency + ' ' + data[0].values.xTotals[i]}, cubeSize, noColor, categories);
        		}
        		// Add total's column on the right side.
        		for (var j = 0; j < data[0].values.y.length; j++) {
        			createCube(cubesContainer, {x: data[0].values.x.length + 1 , y: j + 1, value: data[0].values.yPTotals[j], tooltip: selectedCurrency + ' ' + data[0].values.yTotals[j]}, cubeSize, noColor, categories);
        		}
        		
        		// Add percentage legends.
        		createLegends(svg, data, cubeSize, categories, legendElementHeight);
        		
        		app.translator.translateDOM(svg[0]);
        		
        		// Recalculate margins if we are showing the full legends.
            	if (data[0].values.model.get('showFullLegends')) {        		
            		var top = svg.select('.heatmap-xAxis-container').node().getBBox().height;
            		var left = svg.select('.heatmap-yAxis-container').node().getBBox().width + 25;
            		svg.attr("transform", "translate(" + left + "," + top + ")");
            	}
        });
        
        renderWatch.renderEnd('heatmap immediate');
        return chart;
    }
    
    function getCategoriesByThreshold(undefinedColor, noColor, model) {
    	var categories = new Array();
    	categories.push({min: -99, max: -1, color: undefinedColor});
    	categories.push({min: -1, max: 0, color: noColor});
    	var colors = model.get('heatmap_config').models[0].get('amountColors');
    	var i = 2;
    	for (var property in colors) {
    	    if (colors.hasOwnProperty(property)) {
    	    	categories.push({min: parseInt(property), color: colors[property], max: null});
    	    	if (i > 2) {
    	    		categories[i - 1].max = parseInt(property);
    	    	}
    	    	i++;
    	    }
    	}
    	categories[i - 1].max = 101;
    	return categories;
    }
    
    function createLegends(svg, data, cubeSize, categories, legendElementHeight) {
    	var legendsContainer = svg
			.append("g")
			.attr("transform", "translate(0, " + (((data[0].values.y.length + 1) * cubeSize) + 10) + ")")
			.attr("class", "heatmap-legends-container");
    	var legendsPool = [app.translator.translateSync("amp.dashboard:chart-heatmap-legend-less-than") + " " + categories[2].max + "%",
    	                   app.translator.translateSync("amp.dashboard:chart-heatmap-legend-between") + " " + categories[3].min + "% " + app.translator.translateSync("amp.dashboard:chart-heatmap-legend-and") + " <" + categories[3].max + "% ",
    	                   app.translator.translateSync("amp.dashboard:chart-heatmap-legend-between") + " " + categories[4].min + "% " + app.translator.translateSync("amp.dashboard:chart-heatmap-legend-and") + " <" + categories[4].max + "% ",
    	                   app.translator.translateSync("amp.dashboard:chart-heatmap-legend-between") + " " + categories[5].min + "% " + app.translator.translateSync("amp.dashboard:chart-heatmap-legend-and") + " <" + categories[5].max + "% ",
    	                   app.translator.translateSync("amp.dashboard:chart-heatmap-legend-between") + " " + categories[6].min + "% " + app.translator.translateSync("amp.dashboard:chart-heatmap-legend-and") + " <" + categories[6].max + "% ",
    	                   app.translator.translateSync("amp.dashboard:chart-heatmap-legend-more-than") + " " + categories[7].min + "%"];
    	var maxLegendTextWidth = 0;
    	for (var i = 0; i < legendsPool.length; i++) {
    		var auxWidth = calculateTextWidth(legendsPool[i]);
    		if (auxWidth > maxLegendTextWidth) {
    			maxLegendTextWidth = auxWidth;
    		}
    		$("#tempSpan").remove();
    	}
    	maxLegendTextWidth += 20;
    	
    	for (var i = 0; i < legendsPool.length; i++) {
    		var legends = legendsContainer
				.append("rect")
				.attr("x", (i * maxLegendTextWidth))
				.attr("width", maxLegendTextWidth)
				.attr("height", legendElementHeight)
				.attr("class", "bordered")
				.style("fill", categories[i + 2].color);
		
			var text = legendsContainer.append("text"); 
			text.attr('font-family', 'Arial')
				.attr('font-size', '11px')
				.attr("y", 15)
				.attr("x", ((i * maxLegendTextWidth) + ((maxLegendTextWidth - calculateTextWidth(legendsPool[i])) / 2)))
				.text(legendsPool[i]); // Why "text" instead of "html", because it doesnt work on IE.
    	}
    	
    	legendsContainer.append("text")
			.text("LEGENDS")
			.attr("x", -6)
			.attr("y", 15)
			.attr("class", "yLabel mono axis nv-series heatmap-totals")
			.style("text-anchor", "end")
			.style("font-weight", "bold")
			.attr('data-i18n', 'amp.dashboard:chart-heatmap-legends');
    }
    
    function createCube(cubesContainer, data, cubeSize, noColor, categories) {
    	var selfData = data;
    	var cube = cubesContainer
			.append("rect")
			.attr("x", ((data.x - 1) * cubeSize))
			.attr("y", ((data.y - 1) * cubeSize))
			.attr("rx", 4)
			.attr("ry", 4)
			.attr("class", "bordered")
			.attr("width", cubeSize)
			.attr("height", cubeSize)
			.style("fill", noColor)
			.attr("data-x", data.x - 1)
			.attr("xid", data.xid)
			.attr("yid", data.yid)
			.attr("data-y", data.y - 1);
		
		cube.transition()
			.duration(1000)
			.style("fill", calculateColorFromCategories(data.value, categories, noColor));
		
		var text = cubesContainer.append("text"); 
		text.attr('font-family', 'Arial')
			.attr('font-size', '11px')
			.attr("x", ((data.y - 1) * cubeSize))
			.attr("xid", data.xid)
			.attr("y", ((data.y - 1) * cubeSize) + 19)
			.attr("yid", data.yid)
			.attr("class", "heatmap-cell")
			.attr("x", function() {
				// The "offsets" we add on each return call are tied to the current font family and size, so if we change them we might need to change the offsets too.
				var d = data;
				var auxVal = d.value;
				if (auxVal > 0 && auxVal < 1) {
					return ((d.x - 1) * cubeSize) + 4;
				} else if (auxVal < 10) {
					return ((d.x - 1) * cubeSize) + 8;
				} else if (auxVal == 100) {
					return ((d.x - 1) * cubeSize) + 1;	
				} else {
					return ((d.x - 1) * cubeSize) + 5;
				}					
			}).text(function() {
				// Cant use "html" function on IE.
				var d = data;
				var auxVal = d.value;
				if (auxVal > -1) {
					if (auxVal > 0 && auxVal < 1) {
						return '<1%';
					} else {
						return auxVal + '%';
					}
				} else {
                    if (auxVal == -1) {
                        return '';
                    } else {
                        return app.translator.translateSync("amp.dashboard:chart-heatmap-legend-n-a");
					}
				}
			})
			.on("click", function(obj) {				
				// Remove all filters applied.
				$($(cubesContainer[0]).find("rect")).removeAttr("filter");
				// Apply "darken" to the row and column.		
				$($(cubesContainer[0]).find("[data-x='"+ (selfData.x - 1) + "']")).attr("filter", "url(#filterDarken)");
				$($(cubesContainer[0]).find("[data-y='"+ (selfData.y - 1) + "']")).attr("filter", "url(#filterDarken)");
				// Highlight this cell.
				$($(cubesContainer[0]).find("[data-y='"+ (selfData.y - 1) + "']" + "[data-x='"+ (selfData.x - 1) + "']")).removeAttr("filter");
			});

		if (data.tooltip) {
			text.attr('data-title', data.tooltip)
				.attr("class", "nv-series heatmap-cell");
		}
    }
       
    function calculateColorFromCategories(value, categories, noColor) {
    	var color = noColor;
    	for (var i = 0; i < categories.length; i++) {
    		if ((value >= categories[i].min) && (value < categories[i].max)) {
    			color = categories[i].color;
    			break;
    		}
    	}
    	return color;
    }
    
    function calculateTextWidth(text) {
    	$("body").append("<span id='tempSpan' class='invisible'>" + text + "</span>");
    	var auxWidth = $("#tempSpan").width();
    	$("#tempSpan").remove();
    	return auxWidth;
    }

    //============================================================
    // Expose Public Variables
    //------------------------------------------------------------

    // expose chart's sub-components
    chart.legend = {};
    chart.dispatch = dispatch;
    chart.heatmap = heatmap;
    chart.options = nv.utils.optionsFunc.bind(chart);
    chart.shortTextLength = function(_) {
        return shortTextLength;
    };

    // use Object get/set functionality to map between vars and chart functions
    chart._options = Object.create({}, {    	
        // simple options, just get/set the necessary values
        noData:         {get: function(){return noData;},         set: function(_){noData=_;}},
        defaultState:   {get: function(){return defaultState;},   set: function(_){defaultState=_;}},
        // options that require extra logic in the setter
        duration: {get: function(){return duration;}, set: function(_){
            duration = _;
            renderWatch.reset(duration);
        }},
        color: {},
        margin: {get: function(){return margin;}, set: function(_){
            margin.top    = _.top    !== undefined ? _.top    : margin.top;
            margin.right  = _.right  !== undefined ? _.right  : margin.right;
            margin.bottom = _.bottom !== undefined ? _.bottom : margin.bottom;
            margin.left   = _.left   !== undefined ? _.left   : margin.left;
        }},
        tooltipContent: {}
    });
    
    chart.height = function(_) {
        if (!arguments.length) return height;
        height = _;
        return chart;
    };
    
    nv.utils.inheritOptions(chart, heatmap);
    nv.utils.initOptions(chart);
    return chart;
};