TestSS2ParamList : TestSS2Param {
  var param;

  setUp {
    param = SS2ParamList([\low, \mid, \high, \ultra]).constrainLimitsToSymbols();
  }

  tearDown {
    param.free;
  }

  test_index {
    param.index = 0;
    this.assertFloatEquals(param.value(), 0, "Index 0 should have value 0");
    this.assertFloatEquals(param.normalized(), 0, "Index 0 should have normalized 0");
    this.assertFloatEquals(param.midi(), 0, "Index 0 should have midi 0");
    this.assertEquals(param.symbol(), \low, "Index 0 should have symbol \\low");

    param.index = 3;
    this.assertFloatEquals(param.value(), 3, "Index 3 should have value 3");
    this.assertFloatEquals(param.normalized(), 0.75, "Index 3 should have normalized 0.75");
    this.assertFloatEquals(param.midi(), 95.25, "Index 3 should have midi 95.25");
    this.assertEquals(param.symbol(), \ultra, "Index 3 should have symbol \\ultra");
  }

  test_symbol {
    param.symbol = \low;
    this.assertFloatEquals(param.value(), 0, "Symbol \\low should have value 0");
    this.assertFloatEquals(param.normalized(), 0, "Symbol \\low should have normalized 0");
    this.assertFloatEquals(param.midi(), 0, "Symbol \\low should have midi 0");
    this.assertEquals(param.symbol(), \low, "Symbol \\low should have symbol \\low");

    param.symbol = \ultra;
    this.assertFloatEquals(param.value(), 3, "Symbol \\ultra should have value 3");
    this.assertFloatEquals(param.normalized(), 0.75, "Symbol \\ultra should have normalized 0.75");
    this.assertFloatEquals(param.midi(), 95.25, "Symbol \\ultra should have midi 95.25");
    this.assertEquals(param.symbol(), \ultra, "Symbol \\ultra should have symbol \\ultra");
  }

  test_value {
    param.value = 0;
    this.assertFloatEquals(param.value(), 0, "Value 0 should have value 1");
    this.assertFloatEquals(param.normalized(), 0, "Value 0 should have normalized 0");
    this.assertFloatEquals(param.midi(), 0, "Value 0 should have midi 0");
    this.assertEquals(param.symbol(), \low, "Value 0 should have symbol \\low");

    param.value = 3;
    this.assertFloatEquals(param.value(), 3, "Value 3 should have value 10");
    this.assertFloatEquals(param.normalized(), 0.75, "Value 3 should have normalized 0.75");
    this.assertFloatEquals(param.midi(), 95.25, "Value 3 should have midi 95.25");
    this.assertEquals(param.symbol(), \ultra, "Value 3 should have symbol \\ultra");
  }

}
