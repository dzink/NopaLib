/**
 * An trivial abstract class where normalized is the same as value.
 */

SS2Param : Object {
  var <normalized, <value;
  var >displayStrategy;
  var <>label;
  var <>action;
  var widgets;

  const <midiMax = 127.0;
  const <midiMin = 0.0;
  const <initMidi = 63.5;

  *new {
    var p = super.new();
    p.init();
    ^ p;
  }

  init {
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
    ^ 0;
  }

  max {
    ^ 1;
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
      normalized = value;
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
      value = this.map(n);
      if (performAction) {
        this.performAction();
        this.syncWidgets();
      };
    }
    ^ this;
  }

  map {
    arg n;
    n = n.defaultWhenNil(normalized);
    ^ n;
  }

  /**
   * Unmap from the controlSpec.
   */
  unmap {
    arg v;
    v = v.defaultWhenNil(value);
    ^ v;
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

  widgets {
    ^ widgets.defaultWhenNil(List[]);
  }

  /**
   * Register a widget to be synced to this Parameter's value.
   * @param w SS2ParamWidget
   * @param setProperties boolean
   *   If true, will set w's properties to match this parameter.
   */
  registerWidget {
    arg w, setProperties = false;
    widgets = this.widgets.add(w);
    if (w.param.isNil && setProperties) {
      w.param = this;
      w.setPropertiesFromParam(this);
    }
    ^ this;
  }

  deregisterWidget {
    arg w;
    widgets = this.widgets.remove(w);
    ^ this;
  }

  syncWidgets {
    this.widgets.do {
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
    this.value = n.defaultWhenNil(0).asFloat();
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
