SS2ParamDb : SS2ParamInf {
  var <>silentDb = -80;

  *new {
    arg min = 0, max = 1, warp = nil, round = 0.0001;
    var p = super.new();
    p.init(a_min: min, a_max: max, a_warp: warp, a_round: round);
    ^ p;
  }

  init {
    arg a_min = -inf, a_max = 0, a_warp = nil, a_round = 0;
    var warp;
    warp = if (a_warp.isNil) {
      if (min(a_min, a_max) > 0) { \exp } { -3 };
    } {
      a_warp;
    };
    [\dbwarp, warp, a_warp].postln;
    round = a_round;
    if (a_min == -inf) {
      controlSpec = ControlSpec(minval: silentDb, maxval: a_max, warp: warp);
      minInf = true;
    } {
      controlSpec = ControlSpec(minval: a_min, maxval: a_max, warp: warp);
      minInf = false;
    };
    this.normalized = 0;
    ^ this;
  }

  ensureDefaultDisplay {
    if (displayStrategy.isNil) {
      displayStrategy = SS2ParamDisplayDb();
    };
    ^ this;
  }

  db {
    arg n;
    n = n ? value;
    ^ n;
  }

  db_ {
    arg db;
    this.value = db;
    ^ this;
  }

  amps {
    arg n;
    n = (n ? value).dbamp;
    ^ n;
  }

  amps_ {
    arg amp;
    this.value = amp.ampdb;
  }

  transformOut {
    arg n = nil;
    if (prCachedTransform.isNil) {
      n = n ? this.value;
      n = switch(conversionStrategy,
        \amps, { this.amps(n) },
        n
      );
      prCachedTransform = n;
    };
    ^ prCachedTransform;
  }

  convertToAmps {
    ^ conversionStrategy == \amps;
  }

  convertToAmps_ {
    arg convert = true;
    conversionStrategy = if (convert, {\amps}, {\none});
    ^ this;
  }

}
