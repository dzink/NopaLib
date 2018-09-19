SS2ParamDb : SS2ParamContinuous {

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
    arg min = -inf, max = 0, warp = nil, round = 0;
    var p = super.new();
    p.init(a_min: min, a_max: max, a_warp: warp, a_round: round);
    ^ p;
  }

  init {
    arg a_min = -inf, a_max = 0, a_warp = nil, a_round = 0;
    var warp;
    warp = if (a_warp.isNil) {
      if (min(a_min, a_max) > -inf) { \exp } { 4 };
    } {
      a_warp;
    };
    controlSpec = ControlSpec(minval: a_min.dbamp, maxval: a_max.dbamp, warp: warp);
    round = a_round;
    this.normalized_(0, true, true);
    ^ this;
  }

  ensureDefaultDisplay {
    if (displayStrategy.isNil) {
      this.displayStrategy_(SS2ParamDisplayDb());
    };
  }

  map {
    arg n;
    n = n.defaultWhenNil(normalized);
    n = controlSpec.map(n);
    ^ n.ampdb.round(round).dbamp;
  }

  db {
    arg n;
    n = n.normalized(value);
    ^ n.ampdb;
  }

  db_ {
    arg db;
    this.value = db.dbamp;
  }

}
