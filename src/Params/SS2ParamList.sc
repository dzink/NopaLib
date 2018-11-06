SS2ParamList : SS2Param {
  var <symbols;
  var normalScale;

  *new {
    arg symbols = [];
    var p;
    p = super.new();
    p.init(a_symbols: symbols);
    ^ p;
  }

  init {
    arg a_symbols = [];
    this.symbols = a_symbols;
    this.normalized_(0, true, true);
    ^ this;
  }

  symbol {
    arg n = nil;
    ^ symbols[this.index(n)];
  }

  symbol_ {
    arg s;
    var i = symbols.find([s]);
    if (i.isNil) {
      // @TODO throw warning.
    } {
      this.index = i;
    };
    ^ this;
  }

  index {
    arg n;
    n = n ? normalized;
    ^ n.linlin(0, 1, 0, this.size).floor.clip(0, this.lastIndex);
  }

  unIndex {
    arg i;
    ^ (i.floor + 0.5).clip(0, this.size).linlin(0, this.size, 0, 1);
  }

  index_ {
    arg i;
    this.value = i;
    ^ this;
  }

  // For map and unmap, we have to scale the numbers so that all indices have
  // even space and the last one doesn"t get left out.
  map {
    arg n;
    ^ this.index(n);
  }

  unmap {
    arg i;
    ^ this.unIndex(i);
  }

  symbols_ {
    arg a_symbols;
    symbols = a_symbols.asArray.collect(_.asSymbol);
    normalScale = symbols.size.reciprocal;
    ^ this;
  }

  recalculate {
    normalScale = (this.lastIndex / this.size);
    this.normalized_(normalized, recalculate: true);
    ^ this;
  }

  size {
    ^ (symbols ? []).size.asFloat();
  }

  lastIndex {
    ^ (symbols ? []).size.asFloat() - 1;
  }


  min {
    ^ 0;
  }

  max {
    ^ this.lastIndex;
  }

  /**
   * Makes sure there's at least a basic displayStrategy before attempting to
   * display values.
   */
  ensureDefaultDisplay {
    if (displayStrategy.isNil) {
      this.displayStrategy_(SS2ParamDisplayList());
    };
  }

  import {
    arg n;
    this.symbol = n;
    ^ this;
  }

  export {
    ^ this.symbol;
  }

}
