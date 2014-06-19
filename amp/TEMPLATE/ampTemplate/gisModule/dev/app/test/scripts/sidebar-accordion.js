require(['/test/util/frame.js'], function(frame) {
  frame.testPage('/', function(QUnit, frame$, done) {

    QUnit.test('Sidebar accordion loads in the correct state', function(assert) {

      assert.ok(Array.prototype.every.call(frame$('#sidebar-tools .accordion-toggle'), function(toggle) {
        return frame$(toggle).hasClass('collapsed');
      }), 'All accordion toggles loaded collapsed.');

      assert.ok(Array.prototype.every.call(frame$('#sidebar-tools .accordion-body'), function(accordionBody) {
        return frame$(accordionBody).is(':hidden');
      }), 'All accordion bodies load load collapsed.');
    });

    QUnit.test('Sidebar accordion toggles body visibility', function(assert) {
      var firstTool = frame$('#sidebar-tools .sidebar-tool:first-child');
      var lastTool = frame$('#sidebar-tools .sidebar-tool:last-child');

      // click a tool and then check if it has expanded
      frame$('.accordion-toggle', firstTool).click();
      assert.ok(! frame$('.accordion-toggle', firstTool).hasClass('collapsed'),
                'The toggle has no `collapsed` class after being clicked');

      assert.ok(frame$('.accordion-body', firstTool).is(':visible'),
                'The accordion\'s body becomes visible after being clicked');

      // BLAAAAA stupid boostrap tries to animate so this doesn't work
      // even though jQuery effects should be off.
      // click another tool and check if the first collapsed
      // frame$('.accordion-toggle', lastTool).click();
      // assert.ok(frame$('.accordion-body', firstTool).is(':hidden'),
      //           'Accordions collapse when others are open');
    });

    done();
  });
});
