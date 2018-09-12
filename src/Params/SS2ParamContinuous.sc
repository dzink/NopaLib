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
    this.normalized = 0;
    widgets = List[];
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
   * @param performAction Boolean
   *   If true, will perform the action in this.action.
   */
  value_ {
    arg v, performAction = true;
    var min = this.min(), max = this.max();
    if (v != value) {
      value = if (min < max) {
        v.clip(min, max);
      } {
        v.clip(max, min);
      };
      normalized = this.unmap(value);
      if (performAction) {
        this.performAction();
        this.syncWidgets();
      };
    }
    ^ this;
  }

  /**
   * Sets the parameter via normalized.
   * @param n Float
   * @param performAction Boolean
   *   If true, will perform the action in this.action.
   * @param recalculate
   *   Forces recalculation. Use this when ControlSpec may have changed.
   */
  normalized_ {
    arg n, performAction = true, recalculate = false;
    if (n != normalized || recalculate) {
      normalized = n.clip(0, 1);
      value = this.map(n).round(round);
      if (performAction) {
        this.performAction();
        this.syncWidgets();
      };
    }
    ^ this;
  }

  /**
   * Map from the controlSpec.
   */
  map {
    arg n;
    n = n.defaultWhenNil(normalized);
    ^ controlSpec.map(n);
  }

  /**
   * Unmap from the controlSpec.
   */
  unmap {
    arg v;
    v = v.defaultWhenNil(value);
    ^ controlSpec.unmap(v).round(round);
  }


  /**
   * Symantic duplicate for normalized_. Useful for recalculating normalized/
   * values when this's properties have been updated.
   */
  recalculate {
    this.normalized_(normalized, recalculate: true);
    ^ this;
  }

  /**
   * @param n Float
   *   A number (defaults to normalized)
   * @return Float
   *   n scaled to 0..127
   */
  midi {
    arg n;
    n = n.defaultWhenNil(normalized);
    ^ n.linlin(0, 1, midiMin, midiMax);
  }

  /**
   * @param m Float
   *   A MIDI number 0..127.
   * @return Float
   *   A corresponding normalized number.
   */
  normalizeMidi {
    arg m = 0;
    ^ m.linlin(midiMin, midiMax, 0, 1);
  }

  /**
   * @param m Float
   *   A MIDI number 0..127.
   * @return Float
   *   A corresponding value.
   */
  midiMap {
    arg m = 0;
    ^ this.map(this.normalizeMidi(m));
  }

  /**
   * @param v Float
   *   A value.
   * @return Float
   *   A MIDI number 0..127.
   */
  midiUnmap {
    arg v;
    v = v.defaultWhenNil(value);
    ^ this.midi(this.unmap(v));
  }

  /**
   * Sets normalized via a MIDI input.
   */
  midi_ {
    arg m;
    this.normalized = this.normalizeMidi(m);
    ^ this;
  }

  /**
   * Add/subtract a given MIDI step to normalized.
   */
  incrementMidi {
    arg n = 1;
    var m = this.midi + n;
    this.midi = m;
    ^ this;
  }




  /**
   * Displays a human-readable version of this param (or an optional value).
   */
  display {
    arg n = nil;
    this.ensureDefaultDisplay();
    n = n.defaultWhenNil(this);
    ^ displayStrategy.map(n);
  }

  /**
   * Sets this param to the given string input.
   */
  display_ {
    arg n;
    this.ensureDefaultDisplay();
    this.displayStrategy.unmap(this, n);
    ^ this;
  }

  /**
  * Displays a human-readable value using a temporary displayStrategy.
  */
  displayAs {
  arg a_display, n = nil;
  n = n.defaultWhenNil(this);
  ^ a_display.map(n);
  }

  displayStrategy {
  this.ensureDefaultDisplay();
  ^ displayStrategy;
  }

  /**
   * Makes sure there's at least a basic displayStrategy before attempting to
   * display values.
   */
  ensureDefaultDisplay {
    if (displayStrategy.isNil) {
      this.displayStrategy_(SS2ParamDisplay());
    };
  }

  /**
   * When streamed to output, show the display value.
   */
  printOn {
    arg stream;
    stream << this.display();
  }

  /**
   * Performs the function registered in action.
   */
  performAction {
    var a = action.defaultWhenNil({});
    ^ a.value(this);
  }

  /**
   * Register a widget to be synced to this Parameter's value.
   * @param w SS2ParamWidget
   * @param setProperties boolean
   *   If true, will set w's properties to match this parameter.
   */
  registerWidget {
    arg w, setProperties = false;
    widgets = widgets.add(w);
    if (w.param.isNil && setProperties) {
      w.param = this;
      w.setPropertiesFromParam(this);
    }
    ^ this;
  }

  deregisterWidget {
    arg w;
    widgets = widgets.remove(w);
    ^ this;
  }

  syncWidgets {
    widgets.do {
      arg w;
      w.syncToParam();
    }
    ^ this;
  }

  /**
   * Import a value from string. Should be trivial in most subclasses.
   */
  import {
    arg n;
    this.normalized = n.defaultWhenNil(0).asFloat();
    ^ this;
  }

  /**
   * Export a value to a string. Should be trivial in most subclasses.
   */
  export {
    arg n;
    ^ normalized.asString;
  }

}
