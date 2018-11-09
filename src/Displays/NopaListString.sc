/**
 * NopaCenterableString: A class to help convert params into
 * human-readable strings. Similar to NopaString, only positive numbers
 * are prefixed with "+".
 */


NopaListString : NopaString {
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
