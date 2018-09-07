TestSS2Param : UnitTest {
  var param;

  setUp {
    param = SS2Param(5, 10, \lin, 0.1);
  }

  tearDown {
    param.free;
  }

  test_new {
    this.assertEquals(param.min, 5, 'Minimum should be 5');
    this.assertEquals(param.max, 10, 'Maximum should be 10');
    this.assert(param.warp.isKindOf(LinearWarp), 'Warp should be linear');
    this.assertEquals(param.round, 0.1, 'Round should be 0.1');
  }

  test_value {
    param.value = 7.5;
    this.assertFloatEquals(param.value, 7.5, 'Value should be 7.5');
    this.assertFloatEquals(param.normalized, 0.5, 'Normalized should be 0.5');
    this.assertFloatEquals(param.midi, 63.5, 'Midi should be 63.5');

    param.value = 0;
    this.assertFloatEquals(param.value, 5.0, 'Value should be clipped min');
    param.value = 20;
    this.assertFloatEquals(param.value, 10.0, 'Value should be clipped max');

  }

  test_normalized {
    param.normalized = 0.5;
    this.assertFloatEquals(param.value, 7.5, 'Value should be 7.5');
    this.assertFloatEquals(param.normalized, 0.5, 'Normalized should be 0.5');
    this.assertFloatEquals(param.midi, 63.5, 'Midi should be 63.5');
  }

  test_midi {
    param.midi = 63.5;
    this.assertFloatEquals(param.value, 7.5, 'Value should be 7.5');
    this.assertFloatEquals(param.normalized, 0.5, 'Normalized should be 0.5');
    this.assertFloatEquals(param.midi, 63.5, 'Midi should be 63.5');
  }

  test_min_max {
    param.normalized = 0.5;
    param.min = 0;
    this.assertFloatEquals(param.min, 0.0, 'Min should be reset');
    this.assertFloatEquals(param.value, 5.0, 'Min should recalc value');
    param.max = 20;
    this.assertFloatEquals(param.max, 20.0, 'Max should be reset');
    this.assertFloatEquals(param.value, 10.0, 'Max should recalc value');
  }

  test_warp {
    param.normalized = 0.5;
    param.min = 1;
    param.max = 5;
    param.warp = \lin;
    this.assertFloatEquals(param.value, 3.0, 'Linear Warp');
    param.warp = \exp;
    this.assertFloatEquals(param.value, 2.2360679774998, 'Exponential Warp');
    param.warp = 0;
    this.assertFloatEquals(param.value, 3.0, 'Curve 0 Warp');
    param.warp = -2;
    this.assertFloatEquals(param.value, 3.92423431452, 'Curve -2 Warp');
    param.warp = 2;
    this.assertFloatEquals(param.value, 2.07576568548, 'Curve 2 Warp');
  }

  test_display {
    var tempdisplay;

    param.value = 10;
    param.displayStrategy = SS2ParamDisplay(1, "Hz", 1);
    this.assertEquals(param.display(), "10Hz", 'Display param');
    this.assertEquals(param.display(5), "5Hz", 'Display float');

    param.displayStrategy = SS2ParamDisplay(1, "kg", 2);
    this.assertEquals(param.display(), "20kg", 'New display param');
    this.assertEquals(param.display(5), "10kg", 'New display param');

    tempdisplay = SS2ParamDisplay(10, "USD", 2);
    this.assertEquals(param.displayAs(tempdisplay), "20USD", 'Temp display param');
    this.assertEquals(param.displayAs(tempdisplay, 5), "10USD", 'Temp display float');
    this.assertEquals(param.display(), "20kg", 'Temp display is temp param');
    this.assertEquals(param.display(5), "10kg", 'Temp display is temp float');
  }
}
