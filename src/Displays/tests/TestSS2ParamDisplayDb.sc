TestSS2ParamDisplayDb : TestSS2ParamDisplay {
  var param;
  var display;

  setUp {
    display = SS2ParamDisplayDb();
    param = SS2ParamContinuous(0, 2, \lin, 10);
    param.displayStrategy = display;
  }

  tearDown {
    param.free;
    display.free;
  }

  test_map {
    this.assertEquals(display.map(0), "-∞dB", "Display dB for 0");
    this.assertEquals(display.map(0.5), "-6.02dB", "Display dB for 0.5");
    this.assertEquals(display.map(1), "+0dB", "Display dB for 1");
    this.assertEquals(display.map(2), "+6.02dB", "Display dB for 2");
  }

  test_unmap {
    this.assertFloatEquals(param.display_("-∞dB").value, 0, "Unmap dB for 0", 0.01);
    this.assertFloatEquals(param.display_("-6.02dB").value, 0.5, "Unmap dB for 0.5", 0.01);
    param.display =
    this.assertFloatEquals(param.display_("+0dB";).value, 1, "Unmap dB for 1", 0.01);
    this.assertFloatEquals(param.display_("+6.02dB").value, 2, "Unmap dB for 2", 0.01);
  }
}
