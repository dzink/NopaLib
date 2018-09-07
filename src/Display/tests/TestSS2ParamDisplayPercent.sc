TestSS2ParamDisplayPercent : UnitTest {
  var display;

  setUp {
    display = SS2ParamDisplayPercent(scale: 20);
  }

  tearDown {
    display.free;
  }

  test_new {
    this.assertEquals(display.units, "%");
    this.assertEquals(display.scale, 100);
  }
}
