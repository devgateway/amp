/*
 * Glossary Tooltips
 */
var initialize_tooltips = function() {
	$('img.info-icon')
	  .mouseover(function() {
	    var info = $('#' + $(this).attr('id').replace(/_icon$/, ''));
	    var offset = $(this).offset();
	    
	    info.css({ left: 0, top: 0, width: 'auto' });
	    var width = info.width();
	    width = width - parseInt((width - info.height()) / 1.5);
	    
	    var difference = $(document.body).width() - width - 16;
	    if (offset.left > difference) offset.left = difference;
	    
	    this.timer = setTimeout(function() {
	      info
	        .css({ left: offset.left - 16, top: offset.top - 16, width: width })
	        .fadeIn();
	    }, 250);
	  })
	  .mouseout(function() {
	    clearTimeout(this.timer);
	  });
	
	$('div.info').mouseout(function() {
	  $(this).fadeOut();
	});
}

$(function() {
  initialize_tooltips();
});