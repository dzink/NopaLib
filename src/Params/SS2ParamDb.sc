SS2ParamDb : SS2ParamInf {
  var <>silentDb = -80;

  // *new {
  //   arg min = 0, max = 1, warp = 0, round = 0.0001;
  //   var p = super.new();
  //   p.init(a_min: min, a_max: max, a_warp: warp, a_round: round);
  //   ^ p;
  // }

  init {
    arg a_min = -inf, a_max = 0, a_warp = nil, a_round = 0;
    var warp;
    warp = if (a_warp.isNil) {
      if (min(a_min, a_max) > 0) { \exp } { 4 };
    } {
      a_warp;
    };
    controlSpec = ControlSpec(minval: a_min, maxval: a_max, warp: warp);
    round = a_round;
    if (a_min == -inf) {
      normalized = 0;
      this.min = silentDb;
      this.minInf = true;
    } {
      this.normalized_(0, true, true);
      this.minInf = false;
    };
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
    n = n.defaultWhenNil(value);
    ^ n;
  }

  db_ {
    arg db;
    this.value = db;
    ^ this;
  }

  amps {
    arg n;
    n = n.defaultWhenNil(value).dbamp;
    ^ n;
  }

  amps_ {
    arg amp;
    this.value = amp.ampdb;
  }

  transformOut {
    arg n = nil;
    if (prCachedTransform.isNil) {
      n = n.defaultWhenNil(this.value);
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
