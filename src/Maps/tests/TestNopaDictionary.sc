TestNopaDictionary : UnitTest {
  var paramMap;

  setUp {
    paramMap = NopaDictionary[
      \a -> NopaContinuousParam(5, 10, 0.1, 'sec'),
      \b -> NopaAbstractParamCurve(5, 10, 4, 0.1, 'sec'),
      \c -> NopaAbstractParamMod(0, 10, 4, 1, 'sec'),
      \d -> NopaListParam.fromPairs([\low, 1, \mid, 4, \high, 10], 'sec'),
    ];
  }

  tearDown {
    paramMap.free;
  }

  test_new {
    [\a, \b, \c, \d].do {
      arg key;
      paramMap.setMidi(key, 63.5);
      this.assert(paramMap.at(key).isNil.not, 'Sets key \\' ++ key.asString);
      this.assert(paramMap[key].isKindOf(NopaAbstractParam), 'Has an NopaAbstractParam at \\' ++ key.asString);
      this.assertFloatEquals(63.5, paramMap[key].getMidi, 'Midi is 63.5 at \\' ++ key.asString);
    };

    this.assert(paramMap.at(\e).isNil, 'Does not add non-NopaAbstractParams');
  }

  test_importAll {
    var vals = IdentityDictionary[
      \a -> 1,
      \b -> 2,
      \c -> 4,
      \d -> \high,
    ];
    paramMap.importAll(vals);
    vals.keysValuesDo {
      arg key, value;
      if (value.isKindOf(Float)) {
        this.assertFloatEquals(value, paramMap[key].export, 'Import is accurate at \\' ++ key.asString);
      } {
        this.assertEquals(value, paramMap[key].export, 'Import is accurate at \\' ++ key.asString);
      };
    };
  }

  test_argsArray {
    var expectedKeys, args;
    var vals = NopaPreset[
      \a -> 1,
      \b -> 2,
      \c -> 4,
      \d -> \high,
    ];
    expectedKeys = [\a, \b, \c, \d];
    paramMap.importAll(vals);
    args = paramMap.allAsArgsArray;
    expectedKeys.do {
      arg key;
      this.assert(args.includes(key), 'Args array has key at \\' ++ key.asString);
    };
    args.pairsDo {
      arg key, value;
      this.assert(expectedKeys.includes(key), 'Key is in args array at \\' ++ key.asString);
      this.assertFloatEquals(value, paramMap[key].getValue, 'Args array is accurate at \\' ++ key.asString);
    };
  }

  test_compareToSynthDef {
    var synthDef, sdParams, synthParams;
    synthDef = SynthDef(\test, {
        arg a, b, d, z, j;
        Out.kr(0, SinOsc.ar(20));
    }).add();
    #synthParams, sdParams = paramMap.compareToSynthDefs([synthDef]);
    this.assertEquals(synthParams.size, 1, 'SynthDef compare counts the correct number of parameters in the ParamMap.');
    this.assertEquals(synthParams.asArray[0], \c, 'SynthDef compare returns the correct symbol from the ParamMap.');
    this.assertEquals(sdParams.size, 2, 'SynthDef compare counts the correct number of parameters in the SynthDef.');
    this.assertEquals(sdParams.asArray[0], \z, 'SynthDef compare returns the correct symbol from the SynthDef.');
  }
}
