SS2ParamSemitone : SS2ParamContinuous {

  ensureDefaultDisplay {
    if (displayStrategy.isNil) {
      this.displayStrategy_(SS2ParamDisplaySemitone());
    };
  }

  semitones {
    arg n;
    n = n.defaultWhenNil(value);
    ^ n;
  }

  semitones_ {
    arg st;
    this.value = st;
    ^ this;
  }

  ratio {
    arg n;
    n = n.defaultWhenNil(value).midiratio;
    ^ n;
  }

  ratio_ {
    arg ratio;
    this.value = ratio.ratiomidi;
  }

  hz {
    arg n;
    n = n.defaultWhenNil(value).midicps;
    ^ n;
  }

  hz_ {
    arg ratio;
    this.value = ratio.cpsmidi;
  }

  transformOut {
    arg n = nil;
    if (prCachedTransform.isNil) {
      n = n.defaultWhenNil(this.value);
      n = switch(conversionStrategy,
        \hz, { this.hz(n) },
        \ratio, { this.ratio(n) },
        n
      );
      prCachedTransform = n;
    };
    ^ prCachedTransform;
  }

  convertToHz {
    ^ conversionStrategy == \hz;
  }

  convertToHz_ {
    arg convert = true;
    conversionStrategy = if (convert, {\hz}, {\none});
    ^ this;
  }

  convertToRatio {
    ^ conversionStrategy == \ratio;
  }

  convertToRatio_ {
    arg convert = true;
    conversionStrategy = if (convert, {\hz}, {\none});
    ^ this;
  }

  validConversionStrategies {
    ^ [\none, \hz, \ratio];
  }

}
