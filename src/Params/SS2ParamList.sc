SS2ParamList : SS2Param {
  var <>symbols;

  *new {
    arg symbols, min = 0, max = 1, warp = \lin, round = 1;
    var p = super.new();
    p.init(a_symbols: symbols, a_min: min, a_max: max, a_warp: warp, a_round: round);
    ^ p;
  }

  init {
    arg a_symbols, a_min, a_max, a_warp, a_round;
    controlSpec = ControlSpec(minval: a_min, maxval: a_max, warp: a_warp);
    symbols = a_symbols.asArray.collect(_.asSymbol);
    round = a_round;
    this.normalized = 0;
    value = this.map();
  }

  symbol {
    arg n = nil;
    n = n.defaultWhenNil(normalized);
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
    var i = n.linlin(0, 1, 0, symbols.size).floor.clip(0, symbols.size - 1);
    ^ i;
  }

  index_ {
    arg i;
    this.normalized = i.floor.linlin(0, symbols.size, 0, 1);
  }

  // For map and unmap, we have to scale the numbers so that all indices have
  // even space and the last one doesn't get left out.
  map {
    arg n;
    n = n.defaultWhenNil(normalized);
    n = n * (symbols.size / (symbols.size - 1));
    ^ controlSpec.map(n);
  }

  unmap {
    arg v;
    var n;
    v = v.defaultWhenNil(value);
    n = controlSpec.unmap(v);
    n = n * ((symbols.size - 1) / symbols.size);
    ^ n;
  }


  constrainLimitsToSymbols {
    this.min = 0;
    this.max = symbols.size - 1;
    this.warp = \lin;
    this.round = 1;
  }
}
