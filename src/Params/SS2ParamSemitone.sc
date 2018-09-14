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
    [\cspec, a_min, controlSpec.minval, a_max, controlSpec.maxval].postln;
    round = a_round;
    this.normalized_(0, true, true);
    this.act();
    ^ this;
  }

  ensureDefaultDisplay {
    if (displayStrategy.isNil) {
      this.displayStrategy_(SS2ParamDisplaySemitone());
    };
  }

  map {
    arg n;
    n = n.defaultWhenNil(normalized);
    n = controlSpec.map(n);
    [\map, n, controlSpec.minval].postln;
    ^ n.ratiomidi.round(round).midiratio;
  }

}
