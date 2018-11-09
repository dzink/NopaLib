TestNopaNormalizedString : TestNopaString {
  var param;
  var display;

  setUp {
    display = NopaNormalizedString(scale: 20);
    param = NopaContinuousParam(-2, 2, \lin);
    param.displayStrategy = display;
  }

  tearDown {
    param.free;
    display.free;
  }

  test_map {
    this.assertEquals(param.normalized_(0).display, "0", "Display normalized for 0");
    this.assertEquals(param.normalized_(0.5).display, "10", "Display normalized for 10");
    this.assertEquals(param.normalized_(1).display, "20", "Display normalized for 20");
  }

  test_map_center {
    display.center();
    this.assertEquals(param.normalized_(0).display, "-20", "Display normalized for -20");
    this.assertEquals(param.normalized_(0.5).display, "0", "Display normalized for 0");
    this.assertEquals(param.normalized_(1).display, "+20", "Display normalized for 20");
  }

  test_unmap {
    this.assertFloatEquals(param.display_("0").normalized, 0, "Unmap normalized for 0", 0.01);
    this.assertFloatEquals(param.display_("10").normalized, 0.5, "Unmap normalized for 10", 0.01);
    this.assertFloatEquals(param.display_("20").normalized, 1, "Unmap normalized for 20", 0.01);
  }

  test_unmap_center {
    display.center();
    this.assertFloatEquals(param.display_("+20").normalized, 1, "Unmap normalized for 20", 0.01);
    this.assertFloatEquals(param.display_("+10").normalized, 0.75, "Unmap normalized for 10", 0.01);
    this.assertFloatEquals(param.display_("0").normalized, 0.5, "Unmap normalized for 0", 0.01);
    this.assertFloatEquals(param.display_("-10").normalized, 0.25, "Unmap normalized for 10", 0.01);
    this.assertFloatEquals(param.display_("-20").normalized, 0, "Unmap normalized for 20", 0.01);
  }
}
