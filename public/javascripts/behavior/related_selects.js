$('select.dynamic_select.parent').livequery(function() {
    var parent = $(this);  
    var child_element = parent.nextAll('select.dynamic_select.child')[0];
    if (!child_element) {
        //If the errors are being shown, there's an added DIV that breaks the logic, added this lines to get it from the parent if the first time fails
        child_element = parent.parent().nextAll('select.dynamic_select.child')[0];
        parent.data('child_element', child_element)
        if (!child_element) {
            alert("JavaScript Error: Can't find child list!");
            return false;
        }
    } else {
        parent.data('child_element', child_element)
    }
	
	
    var grouped_options = [];
    var groups = child_element.getElementsByTagName('optgroup');
    // This happens if the element has been parsed already
    if (groups.length < 1)
        return true;
    var dupped_options = [];
    for (i = 0; i < groups.length; i++) {
        dupped_options = [];
        var options = groups[i].getElementsByTagName('option');
	  
        for (var k = 0; k < options.length; k++) {
            dupped_options[k] = options[k].cloneNode(true);
	    
            // IE has a buggy implementation of cloneNode which causes option tags
            // to lose their selected attribute. This hack is for applying it again.
            if (options[k].getAttribute("selected")) {
                dupped_options[k].setAttribute("selected", "selected");
            }
        }
	  
        grouped_options[groups[i].label] = dupped_options;
    }
    parent.data('grouped_options', grouped_options);
	
    parent.change(function(e) {
        var selected = e.target.options[this.selectedIndex].text;
        var child_element = $(e.target).data('child_element');
        var child_options = $(e.target).data('grouped_options')[selected];
        var oldval = child_element.value; // Save value
    
        // Clear list by removing all child nodes
        while (child_element.childNodes.length >= 1) {
            child_element.removeChild(child_element.firstChild);
        }
    
        // Reinsert options
        if (typeof(child_options) == 'undefined') {
            child_element.style.visibility = 'hidden';
        } else {
            child_element.style.visibility = 'visible';
    
            for (var i = 0; i < child_options.length; i++) {
                child_element.appendChild(child_options[i]);
            }
        }
    });
	
	
    // Manually fire event to initialize list
    parent.change();
});