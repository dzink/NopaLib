SS2ParamSemitone : SS2ParamContinuous {

  /**
   * @param min Float
   *   The number value will represent at normalized = 0.
   * @param max Float
   *   The number value will represent at normalized = 1.
   * @param warp Mixed
   *   \lin is a linear warp, \exp is an exponential warp, Floats will create
   *   a curve.
   * @param round Float
   *   The amount to round value to.
   */
  *new {
    arg min = -24, max = 24, round = 1;
    var p = super.new();
    p.init(a_min: min, a_max: max, a_round: round);
    ^ p;
  }

  init {
    arg a_min = -24, a_max = 24, a_round = 1;
    controlSpec = ControlSpec(minval: a_min.midiratio, maxval: a_max.midiratio, warp: \exp);
    round = a_round;
    this.normalized_(0, true, true);
    this.notifyObservers();
    ^ this;
  }

  ensureDefaultDisplay {
    if (displayStrategy.isNil) {
      this.displayStrategy_(SS2ParamDisplaySemitone());
    };
  }

  semitones {
    arg n;
    n = n.defaultWhenNil(value);
    ^ n;
  }

  semitones_ {
    arg st;
    this.value = st;
    ^ this;
  }

  ratio {
    arg n;
    n = n.defaultWhenNil(value).midiratio;
    ^ n;
  }

  ratio_ {
    arg ratio;
    this.value = ratio.ratiomidi;
  }

  hz {
    arg n;
    n = n.defaultWhenNil(value).midicps;
    ^ n;
  }

  hz_ {
    arg ratio;
    this.value = ratio.cpsmidi;
  }


  transformOut {
    arg n = nil;
    n = n.defaultWhenNil(value);
    ^ switch(conversionStrategy,
      \hz, { this.hz(n) },
      \ratio, { this.ratio(n) },
      n
    );
  }

  convertFromHz {
    ^ conversionStrategy == \hz;
  }

  convertFromHz_ {
    arg convert = true;
    conversionStrategy = if (convert, {\hz}, {\none});
    ^ this;
  }

  convertFromRatio {
    ^ conversionStrategy == \ratio;
  }

  convertFromRatio_ {
    arg convert = true;
    conversionStrategy = if (convert, {\hz}, {\none});
    ^ this;
  }

  validConversionStrategies {
    ^ [\none, \hz, \ratio];
  }

}
