SS2ParamListValues : SS2ParamList {
  var <symbols;
  var <values;

  *new {
    arg symbols = [], values = [];
    var p;
    p = super.new(symbols: symbols);
    p.values = values;
    p.init(a_symbols: symbols, a_values: values);
    ^ p;
  }

  // @TODO: a_min = 0, a_max = 0, a_warp = 0, a_round = 0 are dummies to
  // prevent an annoying warning.
  init {
    arg a_symbols = [], a_values = [], a_min = 0, a_max = 0, a_warp = 0, a_round = 0;
    controlSpec = ControlSpec(minval: a_min, maxval: a_max, warp: a_warp);
    this.symbols = a_symbols;
    this.values = a_values;
    round = 0;
    normalized = 0;
    this.recalculate();
    ^ this;
  }

  // For map and unmap, we have to scale the numbers so that all indices have
  // even space and the last one doesn"t get left out.
  map {
    arg n;
    var i;
    if (values.size > 0) {
      n = n.defaultWhenNil(normalized);
      n = n * (symbols.size / (symbols.size - 1));
      i = this.index(n.round(1));
      ^ values[i];
    } {
      ^ 0;
    };
  }

  unmap {
    arg v;
    var n, i;
    v = v.defaultWhenNil(value);
    i = values.find([v]);
    if (i.isNil) {
      ("Value not found in SS2ParamListValues.").warn;
      ^ normalized;
    } {
      n = i / symbols.size;
      ^ n;
    };
  }

  values_ {
    arg a_values;
    values = a_values.asArray.collect(_.asFloat);
    ^ this;
  }

  value_ {
    arg v;
    value = v;
    normalized = this.unmap(value);
    ^ this;
  }

  min {
    ^ values.min;
  }

  max {
    ^ values.max;
  }
}
