/*
 * blah blah
 */


L._svgishDivIcon = L.Icon.extend({

  options: {
    className: 'leaflet-div-style-icon',
    html: '',

    stroke: true,
    color: '#3388ff',
    weight: 3,
    // opacity: 1,
    lineCap: 'round',
    lineJoin: 'round',
    dashArray: null,
    fill: true,
    fillColor: null,  // same as color by default
    // fillOpacity: 0.2,
  },

  createIcon: function(oldIcon) {
    var div = (oldIcon && oldIcon.tagName === 'DIV') ? oldIcon : document.createElement('div');
    div.innerHTML = this.options.html;
    this._setCssStyles(div, this.options);
    this._setIconStyles(div, 'icon');
    return div;
  },

  _setCssStyles: function(el, options) {
    if (options.stroke) {
      var borderTemplate = '{width}px {dasher} {color}';
      var dasher = options.dashArray ? 'dashed' : 'solid';  // TODO: sometimes dotted?
      el.style.border = L.Util.template(borderTemplate, {
        width: options.weight,
        dasher: dasher,
        color: options.color
        // TODO: opacity... use rgba color for now
      });
    }
    if (options.fill) {
      el.style.background = options.fillColor || options.color;
      // TODO: fillOpacity... use rgba fillColor for now
    }
  },

  createShadow: function() {
    return null;
  }

});


L.RectangleDivIcon = L._svgishDivIcon.extend({
  initialize: function(size, options) {
    L.setOptions(this, options);
    this._size = L.point(size);
  },

  _setCssStyles: function(el, options) {
    var weight = options.weight ? options.weight : 0,
        innerWidth = this._size.x - (weight / 2),
        innerHeight = this._size.y - (weight / 2),
        offsetX = (innerWidth / 2) + weight;
        offsetY = (innerHeight / 2) + weight;
    el.style.width = innerWidth + 'px';
    el.style.height = innerHeight + 'px';
    el.style.marginLeft = -offsetX + 'px';
    el.style.marginTop = -offsetY + 'px';
    return L._svgishDivIcon.prototype._setCssStyles.apply(this, arguments);
  }
});


L.CircleDivIcon = L._svgishDivIcon.extend({
  initialize: function(radius, options) {
    L.setOptions(this, options);
    this._radius = radius;
  },

  _setCssStyles: function(el, options) {
    var weight = options.weight ? options.weight : 0,
        innerRadius = this._radius - (weight / 2),
        offset = innerRadius + weight;
    el.style.width = innerRadius * 2 + 'px';
    el.style.height = innerRadius * 2 + 'px';
    el.style.marginLeft = -offset + 'px';
    el.style.marginTop = -offset + 'px';
    el.style.borderRadius = innerRadius + weight + 'px';
    return L._svgishDivIcon.prototype._setCssStyles.apply(this, arguments);
  }
});


L.circleDivIcon = function(radius, options) {
  return new L.CircleDivIcon(radius, options);
};

L.rectangleDivIcon = function(size, options) {
  return new L.RectangleDivIcon(size, options);
};
