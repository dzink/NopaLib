NopaInfParam : NopaContinuousParam {
  var <minInf = false;
  var <maxInf = false;

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
    arg min = 0, max = 1, warp = 0, round = 0.0001;
    var p = super.new();
    p.init(a_min: min, a_max: max, a_warp: warp, a_round: round);
    ^ p;
  }

  init {
    arg a_min = 0, a_max = 1, a_warp = \lin, a_round = 0.0001;
    controlSpec = ControlSpec(minval: a_min, maxval: a_max, warp: a_warp);
    round = a_round;
    this.normalized_(0, true, true);
    ^ this;
  }

  /**
   * Sets the parameter via value.
   * @param v Float
   * @param notifyObservers Boolean
   *   If true, will perform the action in this.notifyObserversion.
   */
  value_ {
    arg v, notifyObservers = true;
    if (v != value) {
      #normalized, value = this.prGetNomalizedAndValueFromBounds(v);
      this.actOnNewValue(notifyObservers);
    };
    ^ this;
  }

  value {
    if (normalized == 0 && minInf) {
      ^ -inf;
    };
    if (normalized == 1 && maxInf) {
      ^ inf;
    };
    ^ value;
  }

  /**
   * This is made more readable via returns.
   * @return
   *   An array with the results for normalized and value.
   */
  prGetNomalizedAndValueFromBounds {
    arg v;
    var min, max;
    if (minInf.asBoolean && v == -inf) {
      ^ [0, -inf];
    };
    if (maxInf.asBoolean && v == inf) {
      ^ [1, inf];
    };
    min = this.min();
    max = this.max();
    v = if (min < max) {
      v.clip(min, max);
    } {
      v.clip(max, min);
    };
    ^ [this.unmap(v), v];
  }

  /**
   * Sets the parameter via normalized.
   * @param n Float
   * @param notifyObservers Boolean
   *   If true, will notify observers.
   * @param recalculate
   *   Forces recalculation. Use this when ControlSpec may have changed.
   */
  normalized_ {
    arg n, notifyObservers = true, recalculate = false;
    if (n != normalized || recalculate) {
      normalized = n.clip(0, 1);
      value = switch(n,
        0, { -inf },
        1, {inf},
        { this.map(n) }
      );
      this.actOnNewValue(notifyObservers);
    }
    ^ this;
  }

  minInf_ {
    arg m;
    minInf = m.asBoolean;
    this.recalculate;
    ^ this;
  }

  maxInf_ {
    arg m;
    maxInf = m.asBoolean;
    this.recalculate;
    ^ this;
  }

}
