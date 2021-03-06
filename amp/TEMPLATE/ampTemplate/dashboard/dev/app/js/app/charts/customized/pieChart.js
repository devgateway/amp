
nv.models.customizedPieChart = function() {
    "use strict";

    //============================================================
    // Public Variables with Default Settings
    //------------------------------------------------------------

    var pie = nv.models.pie();
    var legend = nv.models.legend().margin({top: 0, right: 0, bottom: 0, left: 0});

    var margin = {top: 30, right: 20, bottom: 20, left: 20}
    //var legendMargin = {top: 30, right: 20, bottom: 20, left: 20}
        , width = null
        , height = null
        , showLegend = true
        , color = nv.utils.defaultColor()
        , tooltips = true
        , rtl = false
        , tooltip = function(key, y, e, graph) {
            return '<h3 style="background-color: '
                + e.color + '">' + key + '</h3>'
                + '<p>' +  y + '</p>';
        }
        , state = nv.utils.state()
        , defaultState = null
        , noData = "No Data Available."
        , duration = 250
        , dispatch = d3.dispatch('tooltipShow', 'tooltipHide', 'stateChange', 'changeState','renderEnd')
        ;

    //============================================================
    // Private Variables
    //------------------------------------------------------------

    var showTooltip = function(e, offsetElement) {
        var tooltipLabel = pie.x()(e.point);
        var left = e.pos[0] + ( (offsetElement && offsetElement.offsetLeft) || 0 ),
            top = e.pos[1] + ( (offsetElement && offsetElement.offsetTop) || 0),
            y = pie.valueFormat()(pie.y()(e.point)),
            content = tooltip(tooltipLabel, y, e, chart)
            ;
        nv.tooltip.show([left, top], content, e.value < 0 ? 'n' : 's', null, offsetElement);
    };

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

    //============================================================
    // Chart function
    //------------------------------------------------------------

    function chart(selection) {

        renderWatch.reset();
        renderWatch.models(pie);

        selection.each(function(data) {
            var container = d3.select(this);
            nv.utils.initSVG(container);

            var that = this;
            var availableWidth = (width || parseInt(container.style('width'), 10) || 960)
                    - margin.left - margin.right,
                availableHeight = (height || parseInt(container.style('height'), 10) || 400)
                    - margin.top - margin.bottom
                ;

            chart.update = function() { container.transition().call(chart); };
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

            // Display No Data message if there's nothing to show.
            if (!data || !data.length) {
                var noDataText = container.selectAll('.nv-noData').data([noData]);

                noDataText.enter().append('text')
                    .attr('class', 'nvd3 nv-noData')
                    .attr('dy', '-.7em')
                    .style('text-anchor', 'middle');

                noDataText
                    .attr('x', margin.left + availableWidth / 2)
                    .attr('y', margin.top + availableHeight / 2)
                    .text(function(d) { return d });

                return chart;
            } else {
                container.selectAll('.nv-noData').remove();
            }

            // Setup containers and skeleton of chart
            var wrap = container.selectAll('g.nv-wrap.nv-pieChart').data([data]);
            var gEnter = wrap.enter().append('g').attr('class', 'nvd3 nv-wrap nv-pieChart').append('g');
            var g = wrap.select('g');

            var heightOffset = $($(container[0])[0]).find(".legend.nvd3-svg")[0].getBoundingClientRect().height;
            gEnter.append('g').attr('class', 'nv-pieWrap').attr('transform', 'translate(0,' + heightOffset + ')');
            gEnter.append('g').attr('class', 'nv-legendWrap');

            // Legend
            if (showLegend) {
                legend.width( availableWidth ).key(pie.x());

                wrap.select('.nv-legendWrap')
                    .datum(data)
                    .call(legend);
            }
            //wrap.attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');

            // Main Chart Component(s)
            pie.width(availableWidth).height(availableHeight);
            var pieWrap = g.select('.nv-pieWrap').datum([data]);
            d3.transition(pieWrap).call(pie);

            // Event Handling/Dispatching (in chart's scope)
            legend.dispatch.on('stateChange', function(newState) {
                for (var key in newState) {
                    state[key] = newState[key];
                }
                dispatch.stateChange(state);
                chart.update();
            });

            pie.dispatch.on('elementMouseout.tooltip', function(e) {
                dispatch.tooltipHide(e);
            });


            if(rtl) {
                wrap.select('.nv-pieLabels').selectAll('text')[0].forEach(function (element) {
                    if (element.textContent.length > 0 && element.textContent.lastIndexOf("%")) {
                        element.textContent = "%" + element.textContent.substring(0, element.textContent.length - 1);
                    }
                });
            }

            dispatch.on('changeState', function(e) {
                if (typeof e.disabled !== 'undefined') {
                    data.forEach(function(series,i) {
                        series.disabled = e.disabled[i];
                    });
                    state.disabled = e.disabled;
                }
                chart.update();
            });

        });

        renderWatch.renderEnd('pieChart immediate');
        return chart;
    }

    //============================================================
    // Event Handling/Dispatching (out of chart's scope)
    //------------------------------------------------------------

    pie.dispatch.on('elementMouseover.tooltip', function(e) {
        e.pos = [e.pos[0] +  margin.left, e.pos[1] + margin.top];
        dispatch.tooltipShow(e);
    });

    dispatch.on('tooltipShow', function(e) {
        if (tooltips) showTooltip(e);
    });

    dispatch.on('tooltipHide', function() {
        if (tooltips) nv.tooltip.cleanup();
    });

    //============================================================
    // Expose Public Variables
    //------------------------------------------------------------

    // expose chart's sub-components
    chart.legend = legend;
    chart.dispatch = dispatch;
    chart.pie = pie;
    chart.options = nv.utils.optionsFunc.bind(chart);

    // use Object get/set functionality to map between vars and chart functions
    chart._options = Object.create({}, {    	
        // simple options, just get/set the necessary values
        noData:         {get: function(){return noData;},         set: function(_){noData=_;}},
        tooltipContent: {get: function(){return tooltip;},        set: function(_){tooltip=_;}},
        tooltips:       {get: function(){return tooltips;},       set: function(_){tooltips=_;}},
        showLegend:     {get: function(){return showLegend;},     set: function(_){showLegend=_;}},
        defaultState:   {get: function(){return defaultState;},   set: function(_){defaultState=_;}},
        // options that require extra logic in the setter
        color: {get: function(){return color;}, set: function(_){
            color = _;
            legend.color(color);
            pie.color(color);
        }},
        duration: {get: function(){return duration;}, set: function(_){
            duration = _;
            renderWatch.reset(duration);
        }},
        margin: {get: function(){return margin;}, set: function(_){
            margin.top    = _.top    !== undefined ? _.top    : margin.top;
            margin.right  = _.right  !== undefined ? _.right  : margin.right;
            margin.bottom = _.bottom !== undefined ? _.bottom : margin.bottom;
            margin.left   = _.left   !== undefined ? _.left   : margin.left;
        }},
        /*legendMargin: {get: function(){return legendMargin;}, set: function(_){
        	legendMargin.top    = _.top    !== undefined ? _.top    : legendMargin.top;
        	legendMargin.right  = _.right  !== undefined ? _.right  : legendMargin.right;
        	legendMargin.bottom = _.bottom !== undefined ? _.bottom : legendMargin.bottom;
        	legendMargin.left   = _.left   !== undefined ? _.left   : legendMargin.left;
        }},*/
    });
    //we create the RTL property for the pieChart
    chart.rtl = function(_) {
        if (!arguments.length) return rtl;
        rtl = _;
        return chart;
    };



    nv.utils.inheritOptions(chart, pie);
    nv.utils.initOptions(chart);
    return chart;
};