jest.dontMock("../index.es6");
jest.dontMock('amp/tools');
var createActions = require('../index.es6');

describe("creating", () => {
  it("should forward", () => {
    var actions = createActions({});
    expect(actions.forward).toBeDefined();
  })

  it("should create actions", () => {
    var actions = createActions({
      A: null,
      B: 'number',
      C: ['string', 'number']
    }).forward();
    expect(typeof actions.A).toBe("function");
    expect(typeof actions.B).toBe("function");
    expect(typeof actions.C).toBe("function");
  })
});