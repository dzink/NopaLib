TestSS2ParamDisplaySemitone : TestSS2ParamDisplay {
  var param;
  var display;

  setUp {
    display = SS2ParamDisplaySemitone();
    param = SS2Param(0.25, 4, \exp);
    param.displayStrategy = display;
  }

  tearDown {
    param.free;
    display.free;
  }

  test_new {
    this.assertEquals(display.units, "st");
  }

  test_map {
    this.assertEquals(display.map(0.25), "-24st", "Display semitone for -24");
    this.assertEquals(display.map(1), "0st", "Display semitone for 0");
    this.assertEquals(display.map(4), "+24st", "Display semitone for 24");
  }

  test_unmap {
    this.assertFloatEquals(param.display_("-24st").value, 0.25, "Unmap semitone for -24", 0.01);
    this.assertFloatEquals(param.display_("0st").value, 1, "Unmap semitone for 0", 0.01);
    this.assertFloatEquals(param.display_("24st").value, 4, "Unmap semitone for 24", 0.01);
  }
}
