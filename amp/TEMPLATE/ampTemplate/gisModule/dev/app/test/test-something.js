define(['qunit', '/some-dep.js'], function(QUnit, dep) {
  QUnit.test('Testing tests test', function(assert) {
    assert.ok(dep.isGood, "The dep is good.");
  });
});
