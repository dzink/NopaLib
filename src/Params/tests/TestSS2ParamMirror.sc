TestSS2ParamMirror : TestSS2ParamMeta {
  var param;

  setUp {
    param = SS2ParamMirror(5, \lin, 0.1);
  }

  tearDown {
    param.free;
  }

  test_value {
    param.value = 0;
    this.assertFloatEquals(param.value, 0, "Value should be 0");
    this.assertFloatEquals(param.normalized, 0.5, "Normalized should be 0.5");
    this.assertFloatEquals(param.midi, 63.5, "Midi should be 63.5");

    param.value = 5;
    this.assertFloatEquals(param.value, 5, "Value should be 5");
    this.assertFloatEquals(param.normalized, 1, "Normalized should be 1");
    this.assertFloatEquals(param.midi, 127, "Midi should be 127");

    param.value = -5;
    this.assertFloatEquals(param.value, -5, "Value should be -5");
    this.assertFloatEquals(param.normalized, 0, "Normalized should be 0");
    this.assertFloatEquals(param.midi, 0, "Midi should be 0");
  }

  test_normalized {
    param.normalized = 0.51;
    this.assertFloatEquals(param.value, 0, "Value should be 0");
    this.assertFloatEquals(param.normalized, 0.51, "Normalized should be 0.51");
    this.assertFloatEquals(param.midi, 64.77, "Midi should be 64.77");
  }

  test_midi {
    param.midi = 64.77;
    this.assertFloatEquals(param.value, 0, "Value should be 0");
    this.assertFloatEquals(param.normalized, 0.51, "Normalized should be 0.51");
    this.assertFloatEquals(param.midi, 64.77, "Midi should be 64.77");
  }

}
