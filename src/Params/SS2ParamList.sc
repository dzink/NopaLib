SS2ParamList : SS2Param {
  var <symbols;
  var value;
  var normalScale;

  *new {
    arg symbols, min = 0, max = 1, warp = \lin;
    var p;
    p = super.new();
    p.init(a_symbols: symbols, a_min: min, a_max: max, a_warp: warp);
    ^ p;
  }

  init {
    arg a_symbols, a_min = 0, a_max = 1, a_warp = \lin, a_round = 0;
    controlSpec = ControlSpec(minval: a_min, maxval: a_max, warp: a_warp);
    this.symbols = a_symbols;
    round = 0;
    normalized = 0;
    this.recalculate();
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
    var i;
    n = n.defaultWhenNil(normalized);
    i = n.linlin(0, normalScale, 0, this.lastIndex).floor.clip(0, this.lastIndex);
    ^ i;
  }

  index_ {
    arg i;
    this.normalized = i.floor.clip(0, this.lastIndex).linlin(0, this.lastIndex, 0, normalScale);
    [i, normalized, normalScale].postln;
    ^ this;
  }

  // For map and unmap, we have to scale the numbers so that all indices have
  // even space and the last one doesn"t get left out.
  map {
    arg n;
    n = n.defaultWhenNil(normalized);
    n = (n * normalScale.reciprocal).trunc((this.size + 1).reciprocal);
    ^ controlSpec.map(n);
  }

  unmap {
    arg v;
    var n;
    v = v.defaultWhenNil(value);
    n = controlSpec.unmap(v);
    n = n * normalScale;
    ^ n;
  }

  constrainLimitsToSymbols {
    this.min = 0;
    this.max = this.lastIndex;
    this.warp = \lin;
    this.recalculate();
    ^ this;
  }

  symbols_ {
    arg a_symbols;
    symbols = a_symbols.asArray.collect(_.asSymbol);
    ^ this;
  }

  recalculate {
    normalScale = (this.lastIndex / this.size);
    this.normalized_(normalized, recalculate: true);
    ^ this;
  }

  size {
    ^ symbols.defaultWhenNil([]).size.asFloat();
  }

  lastIndex {
    ^ symbols.defaultWhenNil([]).size.asFloat() - 1;
  }
}
