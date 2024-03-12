If you are including this widget in your code, then this main.js should be the only code you need to read.
This widget will trigger a 'cancel' and 'apply' events fired when the user does those actions.

```
// Sample Init:
this.app.filtersWidget = new FiltersWidget({
  el:this.$('#filter-popup'),
  draggable: true,
  translator: this.app.translator
});

// Register state once init is done.
this.app.filtersWidget.loaded.then(function() {
  self.app.state.register(self, 'filters', {
     get: function() { return self.app.filtersWidget.serialize();},
     set: function(state) { return self.app.filtersWidget.deserialize(state);},
     empty: null
  });
});
```

 Exposed Methods:
         loaded: a deferred var that resolves when loading is done.
    showFilters: call when you show the widget, so it can save current state to restore if user cancels.
      serialize: convert the filter's states into a JSON blob.
    deserialize: restore the filter's state based on a JSON blob.
