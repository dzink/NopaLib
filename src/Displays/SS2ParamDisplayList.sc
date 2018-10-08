/**
 * SS2ParamDisplayCenterable: A class to help convert params into
 * human-readable strings. Similar to SS2ParamDisplay, only positive numbers
 * are prefixed with "+".
 */


SS2ParamDisplayList : SS2ParamDisplay {
  map {
    arg param;
    var symbol = param.symbol;
    if (symbol.isNil) {
      symbol = param.value;
    }
    ^ symbol;
  }

  unmap {
    arg param, symbol;
    param.symbol = symbol.asSymbol;
    ^ this;
  }

}
