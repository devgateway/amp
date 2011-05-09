<script type="text/javascript">
    dojo.require("dijit.layout.TabContainer");
    dojo.require("dijit.layout.ContentPane");
</script>

   <link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/dojo/1.6/dijit/themes/claro/claro.css">
<div dojoType="dijit.layout.TabContainer" style="width: 400px; height: 100px;"
tabPosition="right-h">
    <div dojoType="dijit.layout.ContentPane" title="My first tab" selected="true">
        Lorem ipsum and all around...
    </div>
    <div dojoType="dijit.layout.ContentPane" title="My second tab">
        Lorem ipsum and all around - second...
    </div>
    <div dojoType="dijit.layout.ContentPane" title="My last tab">
        Lorem ipsum and all around - last...
    </div>
</div>