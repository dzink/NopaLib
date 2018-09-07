TestSS2Preset : UnitTest {
  var preset, paramMap;

  setUp {
    paramMap = SS2ParamMap[
      \a -> SS2Param(5, 10, 0.1, 'sec'),
      \b -> SS2ParamCurve(5, 10, 4, 0.1, 'sec'),
      \c -> SS2ParamMod(0, 10, 4, 1, 'sec'),
      \d -> SS2ParamList.fromPairs([\low, 1, \mid, 4, \high, 10], 'sec'),
      \e -> 25,
    ];
    preset = SS2Preset.newFrom(paramMap);
  }

  tearDown {
    paramMap.free;
    preset.free;
  }

  test_new {
    [\a, \b, \c, \d].do {
      arg key;
      this.assert(preset.at(key).isNil.not, 'Sets key \\' ++ key.asString);
    };
  }

  test_importAll {
    var preset2 = SS2Preset[
      \a -> 1,
      \b -> 2,
      \c -> 4,
      \d -> \high,
    ];
    paramMap.importAll(preset2);
    preset.importAll(paramMap);

    preset2.keysValuesDo {
      arg key, value;
      if (value.isKindOf(Float)) {
        this.assertFloatEquals(value, preset[key], 'Preset import is accurate at \\' ++ key.asString);
      } {
        this.assertEquals(value, preset[key], 'Preset import is accurate at \\' ++ key.asString);
      };
    };
  }
}
