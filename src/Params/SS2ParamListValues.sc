SS2ParamListValues : SS2ParamList {
  var <values;

  *new {
    arg symbols = [], values = [];
    var p;
    p = super.new();
    p.values = values;
    p.init(a_symbols: symbols, a_values: values);
    ^ p;
  }

  *fromPairs {
    arg pairs = [];
    var symbols, values;
    #symbols, values = pairs.unlace(2);
    ^ SS2ParamListValues(symbols, values);
  }

  // @TODO: a_min = 0, a_max = 0, a_warp = 0, a_round = 0 are dummies to
  // prevent an annoying warning.
  init {
    arg a_symbols = [], a_values = [];
    this.symbols = a_symbols;
    this.values = a_values;
    this.normalized_(0, true, true);
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
      i = this.index(n);
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
      n = (i / this.size).trunc(this.size.reciprocal);
      ^ n;
    };
  }

  values_ {
    arg a_values;
    values = a_values.asArray.collect(_.asFloat);
    ^ this;
  }

  value_ {
    arg v, notifyObservers = true;
    value = v;
    normalized = this.unmap(value);
    if (notifyObservers) {
      this.notifyObservers();
    };
    ^ this;
  }

  min {
    ^ values.minItem;
  }

  max {
    ^ values.maxItem;
  }
}
