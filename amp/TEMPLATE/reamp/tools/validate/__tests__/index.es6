jest.dontMock('../index.es6');
jest.dontMock('../../index.es6');
var validate = require('../index.es6');

describe("keypress event to string conversion", () => {
  var keyPressEventToString, mockEvent;
  beforeEach(()=>{
    keyPressEventToString = validate.keyPressEventToString;
    mockEvent = {
      which: 0,
      target:{
        selectionStart: 0,
        selectionEnd: 0,
        value: ""
      }
    };
  });

  it("should add char to input", () => {
    mockEvent.which = "a".charCodeAt(0);
    expect(keyPressEventToString(mockEvent)).toBe("a");
  });

  it("should replace selection", () => {
    mockEvent.which = "a".charCodeAt(0);
    mockEvent.target.value="1234";
    mockEvent.target.selectionStart = "1";
    mockEvent.target.selectionEnd = "3";
    expect(keyPressEventToString(mockEvent)).toBe("1a4");
  });
});