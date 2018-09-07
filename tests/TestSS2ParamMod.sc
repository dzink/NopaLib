TestSS2ParamMod : UnitTest {
  var param;
  const midi = 116;
  const valueAt71 = 6.9;
  const midiAt58 = 116.12746865702;

  setUp {
    param = SS2ParamMod(1, 10, 2, 0.1, 'sec');
  }

  tearDown {
    param.free;
  }

  test_new {
    this.assertEquals(param.min, 1, 'Minimum should be 5');
    this.assertEquals(param.max, 10, 'Maximum should be 10');
    this.assertEquals(2, param.curve, 'Curve should be 4');
    this.assertEquals(param.round, 0.1, 'Round should be 0.1');
    this.assertEquals(param.units, 'sec', 'Units should be sec');
  }

  test_setMidi {
    param.setMidi(midi);
    this.assertFloatEquals(midi, param.getMidi, 'setMidi is transparent');
    this.assertFloatEquals(valueAt71, param.getValue, 'setMidi also sets value');

    param.setMidi((midi - SS2ParamMod.midiMax).abs);
    this.assertFloatEquals(valueAt71.neg, param.getValue, 'setMidi transposes negatives');
  }

  test_setValue {
    param.setValue(valueAt71);
    this.assertFloatEquals(valueAt71, param.getValue, 'setValue is transparent');
    this.assertFloatEquals(midiAt58, param.getMidi, 'setValue also sets midi');
  }

  test_setValueAtCenter {
    param.setValue(0.5);
    this.assertFloatEquals(0, param.getValue, 'Setting value below min sets to 0');
    this.assertFloatEquals(SS2ParamMod.midiCenter, param.getMidi, 'Setting value below min sets midi to middle');
  }

  test_import {
    param.import(valueAt71);
    this.assertFloatEquals(valueAt71, param.getValue, 'import value should be transparent');
    this.assertFloatEquals(midiAt58, param.getMidi, 'import also sets midi');
  }

  test_export {
    var v;
    param.import(valueAt71);
    v = param.export;
    this.assertFloatEquals(valueAt71, v, 'export value should be transparent');
  }

  test_setValueRounding {
    param.setValue(5.277);
    this.assertFloatEquals(5.3, param.value, 'setValue respects rounding');

    param.setValue(5.3177);
    this.assertFloatEquals(5.3, param.value, 'setValue rounds down also');

    param.setRound(1);
    param.setValue(5.277);
    this.assertFloatEquals(5, param.value, 'setValue respects integer rounding');

    param.setRound(0);
    param.setValue(5.27793);
    this.assertFloatEquals(5.27793, param.value, 'setValue rounding can be disabled');

    param.setRound(1);
    this.assertFloatEquals(7, param.getValueFromMidi(midi), 'getValueFromMidi uses rounding');
  }

}
