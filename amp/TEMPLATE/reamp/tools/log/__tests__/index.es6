jest.dontMock("../index.js");
var AmpDebug = require("../index.js");

describe("amp debug", () => {
  it("should create debuggers", () => {
    var myDebugger = AmpDebug("amp:mydebugger");
  });
});