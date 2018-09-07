SS2ParamListValues : SS2ParamList {
  var <symbols;
  var <values;

  *new {
    arg symbols, values;
    var p = super.new();
    p.init(a_symbols: symbols, a_values: values);
    ^ p;
  }

  // @TODO: a_min = 0, a_max = 0, a_warp = 0, a_round = 0 are dummies to
  // prevent an annoying warning.
  init {
    arg a_symbols = [], a_values = [], a_min = 0, a_max = 0, a_warp = 0, a_round = 0;
    this.symbols = a_symbols;
    this.values = a_values;
    this.normalized = 0;
    value = this.map();
  }

  // For map and unmap, we have to scale the numbers so that all indices have
  // even space and the last one doesn't get left out.
  map {
    arg n;
    var i;
    n = n.defaultWhenNil(normalized);
    n = n * (symbols.size / (symbols.size - 1));
    i = this.index(n);
    ^ values[i];
  }

  unmap {
    arg v;
    var n, i;
    v = v.defaultWhenNil(value);
    i = values.find([v]);
    if (i.isNil) {
      ("Value not found in SS2SS2ParamListValues.").warn;
      ^ 0;
    } {
      n = i / symbols.size;
      [\ffff, v, i, n].postln;
      ^ n;
    };
  }

  symbols_ {
    arg a_symbols;
    symbols = a_symbols.asArray.collect(_.asSymbol);
  }

  values_ {
    arg a_values;
    values = a_values.asArray.collect(_.asFloat);
  }

  value_ {
    arg v;
    value = v;
    normalized = this.unmap(value);
    ^ this;
  }
}
