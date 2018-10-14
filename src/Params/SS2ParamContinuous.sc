SS2ParamContinuous : SS2Param {
  var <controlSpec;
  var <round;

  const <midiMax = 127.0;
  const <midiMin = 0.0;
  const <initMidi = 63.5;

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

  round_ {
    arg r;
    round = r;
    this.recalculate;
    ^ this;
  }

  min {
    ^ controlSpec.minval;
  }

  min_ {
    arg m;
    controlSpec.minval = m;
    this.recalculate;
    ^ this;
  }

  max {
    ^ controlSpec.maxval;
  }

  max_ {
    arg m;
    controlSpec.maxval = m;
    this.recalculate;
    ^ this;
  }

  warp {
    ^ controlSpec.warp;
  }

  warp_ {
    arg w;
    controlSpec.warp = w;
    this.recalculate;
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
    var min = this.min(), max = this.max();
    if (v != value) {
      value = if (min < max) {
        v.clip(min, max);
      } {
        v.clip(max, min);
      };
      normalized = this.unmap(value);
      this.actOnNewValue(notifyObservers);
    }
    ^ this;
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
      value = this.map(n);
    }
    ^ this;
  }

  /**
   * Map from the controlSpec.
   */
  map {
    arg n;
    n = n.defaultWhenNil(normalized);
    ^ controlSpec.map(n).round(round);
  }

  /**
   * Unmap from the controlSpec.
   */
  unmap {
    arg v;
    v = v.defaultWhenNil(value);
    ^ controlSpec.unmap(v);
  }


  /**
   * Symantic duplicate for normalized_. Useful for recalculating normalized/
   * values when this's properties have been updated.
   */
  recalculate {
    this.normalized_(normalized, recalculate: true);
    ^ this;
  }
}
