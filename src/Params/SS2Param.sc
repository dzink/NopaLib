SS2Param : Object {
  var <controlSpec;
  var <normalized, <value;
  var <>displayStrategy;

  var <round;

  const <midiMax = 127.0;
  const <midiMin = 0.0;
  const <initMidi = 63.5;

  *new {
    arg min = 0, max = 1, warp = 0, round = 0;
    var p = super.new();
    p.init(a_min: min, a_max: max, a_warp: warp, a_round: round);
    ^ p;
  }

  init {
    arg a_min = 0, a_max = 1, a_warp = \lin, a_round = 0;
    controlSpec = ControlSpec(minval: a_min, maxval: a_max, warp: a_warp);
    round = a_round;
    this.normalized = 0;
    value = this.map();
    ^ this;
  }

  // SETTERS & GETTERS

  value_ {
    arg v;
    var min = this.min(), max = this.max();
    value = if (min < max) {
      v.clip(min, max);
    } {
      v.clip(max, min);
    };
    normalized = this.unmap(value);
    ^ this;
  }

  normalized_ {
    arg n;
    normalized = n.clip(0, 1);
    value = this.map(n);
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

  // CALCULATED VALUES
  recalculate {
    this.normalized = normalized;
    ^ this;
  }

  midi {
    ^ normalized.linlin(0, 1, midiMin, midiMax);
  }

  midi_ {
    arg m;
    ^ this.normalized_(m.linlin(midiMin, midiMax, 0, 1));
  }

  map {
    arg n;
    n = if(n.isNil, { normalized }, { n });
    ^ controlSpec.map(n);
  }

  unmap {
    arg v;
    v = v.defaultWhenNil(value);
    ^ controlSpec.unmap(v);
  }

  incrementMidi {
    arg n = 1;
    var m = this.midi + n;
    this.midi = m;
    ^ this;
  }

  display {
    arg n = nil;
    this.ensureDefaultDisplay();
    n = n.defaultWhenNil(this);
    ^ displayStrategy.map(n);
  }

  display_ {
    arg n;
    this.ensureDefaultDisplay();
    this.displayStrategy.unmap(this, n);
    ^ this;
  }

  ensureDefaultDisplay {
    if (displayStrategy.isNil) {
      this.displayStrategy_(SS2ParamDisplay());
    };
  }

  displayAs {
    arg a_display, n = nil;
    n = n.defaultWhenNil(this);
    ^ a_display.map(n);
  }

  printOn {
    arg stream;
    stream << this.display();
  }
}
