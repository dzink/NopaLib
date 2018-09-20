SS2ParamMirror : SS2ParamContinuous {
  const <centerBuffer = 0.015748031496063;

  *new {
    arg max = 1, warp = 0, round = 0;
    var p = super.new();
    p.init(a_max: max, a_warp: warp, a_round: round);
    ^ p;
  }

  init {
    arg a_max = 1, a_warp = \lin, a_round = 0, a_min = 0;
    controlSpec = ControlSpec(minval: 0, maxval: a_max, warp: a_warp);

    if (controlSpec.warp.isKindOf(ExponentialWarp)) {
      ("SS2Params with Exponential curves will fail upon being calculated.");
    };

    round = a_round;
    this.normalized_(0.5, true, true);
    value = this.map();
    ^ this;
  }


  value_ {
    arg v, notifyObservers = true;
    var max = this.max();
    value = v.clip(max.neg, max);
    normalized = this.unmap(v);
    if (notifyObservers) {
      this.notifyObservers();
    };
    ^ this;
  }

  map {
    arg n;
    var scale;
    n = n.defaultWhenNil(normalized);
    scale = (n - 0.5).abs;
    scale = scale.linlin(centerBuffer, 0.5, 0, 1).clip(0, 1);
    scale = controlSpec.map(scale);
    scale = scale * (n - 0.5).sign;
    ^ scale;
  }

  unmap {
    arg v;
    var scale;
    v = v.defaultWhenNil(value);
    if (v == 0) {
      ^ 0.5;
    };
    scale = controlSpec.unmap(v.abs);
    scale = scale.linlin(0, 1, centerBuffer, 0.5).clip(centerBuffer, 0.5);
    scale = (scale * v.sign) + 0.5;
    ^ scale;
  }

  centered {
    ^ true;
  }
}
