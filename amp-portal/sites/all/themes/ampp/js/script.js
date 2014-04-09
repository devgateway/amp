// $Id: script.js,v 1.4 2012/02/23 20:40:04 eabrahamyan Exp $


(function ($) {

$(document).ready(function(){$('#close').live('click', function(){$(this.parentNode.parentNode).hide('fast');});});

})(jQuery);