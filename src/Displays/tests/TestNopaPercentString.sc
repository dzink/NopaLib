TestNopaPercentString : TestNopaString {
  var display;

  setUp {
    display = NopaPercentString(scale: 20);
  }

  tearDown {
    display.free;
  }

  test_new {
    this.assertEquals(display.units, "%");
    this.assertEquals(display.scale, 100);
  }
}
