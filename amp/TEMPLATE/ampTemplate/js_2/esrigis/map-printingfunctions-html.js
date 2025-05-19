var printIndex=0;

var originalWindowsSize=$(window).width();
var originalWindowsHeightSize=$(window).height();

$(window).resize(function(){
	createCarouSelAndReize();
});



function createCarouSelAndReize(){
	$("#snapShoots").show();
	$(".list_carousel li").css({"width":($(window).width()*.1)+"px"});
	$(".list_carousel li").css({"height":($(window).height()*.1)+"px"});
	$(".print_snap_shoot_item").css("transform","scale("+.1/originalWindowsSize*$(window).width()+","+.1/originalWindowsHeightSize*$(window).height()+")");  			
	$(".print_snap_shoot_item").css("-ms-transform","scale("+.1/originalWindowsSize*$(window).width()+","+.1/originalWindowsHeightSize*$(window).height()+")");  			
	$(".print_snap_shoot_item").css("-webkit-transform","scale("+.1/originalWindowsSize*$(window).width()+","+.1/originalWindowsHeightSize*$(window).height()+")");  			
	$(".print_snap_shoot_item").css("-o-transfor","scale("+.1/originalWindowsSize*$(window).width()+","+.1/originalWindowsHeightSize*$(window).height()+")");
	
}

function printAllSnapShoots(){
	  
	$('<iframe id="print_all"/>').appendTo('body');
	var allHTML="";
	  $(".print_snap_shoot_item").each(function(index,element){
		  var elementHtml=$(element).html();
		  allHTML=allHTML+"<h1 >AMP GIS "+(index +1)+"</h1>"+"<div>"+elementHtml+"</div><p style='page-break-after: always;'></p>";
	  });
	     $('#print_all').contents().find('body').append(allHTML);
	     $('#print_all').contents().find('.map').css("position","relative");
	     $('#print_all').contents().find('.map').css("top","");
	     $('#print_all')[0].contentWindow.print();
		 $('#print_all').remove();
		 //$(".print_snap_shoot_item").each(function(index,element){
		 //	 $(element).fadeOut('fast',function(){
			//  $(element).parent().remove();
		 // });
		 // });
}



function captureMap(){
			var $li= $('<li>');
			var $div= $('<div id="print_'+printIndex+'" class="print_snap_shoot_item">');
		
			$li.click(function(){
	    var printContents = $div.html();
		    
	    /*$.ajax({
		    	url: "/esrigis/mapPrinting.do",
		    	type: "POST",
		    	data: { html:printContents} ,
		    	dataType: "json",
		    	success: function(returnData) {
		    		document.location=returnData.URL;
		    		
		    }
		    });
		    */
		   $('<iframe  id="priting" style="position:absolute;z-index:0;display:none"/>').appendTo('#mainWindow');
		   $('#priting').contents().find('body').append(printContents);
	       $('#priting')[0].contentWindow.print();
		   $($li).fadeOut('fast');
		   
		   $('#priting').remove();
		   $($li).remove();
		});
		
		
		
		
		setTimeout( function() {
			var html=new htmlWithStyles().html($("#map")[0]);
			$div.html(html);
			$li.append($div);
			$('#printList').append($li);
			createCarouSelAndReize();
			
			
			
		}, 200 );
}


function print(){
	
}