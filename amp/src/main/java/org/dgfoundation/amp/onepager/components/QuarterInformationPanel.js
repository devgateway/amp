/*
 * Copyright (c) 2013 Development Gateway (www.developmentgateway.org)
 */

function initializeQuarter(q1, q2, q3, q4){
    //bind event on document ready
    $(function() {
        $('#quarterInformationPanel-Q1').html(q1);
        $('#quarterInformationPanel-Q2').html(q2);
        $('#quarterInformationPanel-Q3').html(q3);
        $('#quarterInformationPanel-Q4').html(q4);

        $('.quarterHover').hover(
            function(){
                //get the current element's position
                var pos = $(this).offset();
                // .outerWidth() takes into account border and padding.
                var width = $(this).outerWidth();
                $('#quarterInformationPanel').css({top: pos.top, left: (pos.left + width)});
                $('#quarterInformationPanel').show();
            },
            function(){
                $('#quarterInformationPanel').hide();
            }
        );
    });
}