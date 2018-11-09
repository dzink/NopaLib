TestNopaString : TestNopaParamMeta {
  var param;
  var display;

  setUp {
    display = NopaString(0.25, "Hz", 1);
    param = NopaAbstractParam(0, 100, \lin, 10);
  }

  tearDown {
    param.free;
    display.free;
  }

  test_new {
    this.assertEquals(display.digits, 0.25, "Digits should be 0.25");
    this.assertEquals(display.units, "Hz", "Units should be Hz");
    this.assertEquals(display.scale, 1, "Scale should be 1");
  }

  test_map {
    param.max = 10000;
    param.value = 10000;
    this.assertEquals(display.map(param), "10KHz", "Map from a parameter");
    this.assertEquals(display.map(10000), "10KHz", "Map from a float");
  }

  test_unmap {
    param.max = 10000000;
    display.unmap(param, "10M");
    this.assertFloatEquals(param.value, 10000000, "Unmapping sets the parameter");
  }

  test_shorten {
    display.round = 3;

    this.assertEquals(display.map(0.000000001), "1nHz", "Shorten nano should work.");
    this.assertEquals(display.map(0.000000011), "11nHz", "Shorten nano 11 should work.");
    this.assertEquals(display.map(0.000000111), "111nHz", "Shorten nano 111 should work.");
    this.assertEquals(display.map(0.000001111), "1.111µHz", "Shorten nano 1111 should work.");
    this.assertEquals(display.map(0.000011111), "11.11µHz", "Shorten nano 11111 should work.");
    this.assertEquals(display.map(0.000111111), "111.1µHz", "Shorten nano 111111 should work.");
    this.assertEquals(display.map(0.001111111), "1.111mHz", "Shorten nano 1111111 should work.");
    this.assertEquals(display.map(0.011111111), "11.11mHz", "Shorten nano 11111111 should work.");
    this.assertEquals(display.map(0.111111111), "111.1mHz", "Shorten nano 111111111 should work.");
    this.assertEquals(display.map(1.111111111), "1.111Hz", "Shorten 1.111111111 should work.");
    this.assertEquals(display.map(11.111111111), "11.11Hz", "Shorten 11.111111111 should work.");
    this.assertEquals(display.map(11111.111111111), "11.11KHz", "Shorten 11111.111111111 should work.");
    this.assertEquals(display.map(11111111.111111111), "11.11MHz", "Shorten 11111111.111111111 should work.");
    this.assertEquals(display.map(11111111111.111111111), "11.11GHz", "Shorten 11111111111.111111111 should work.");

    // @TODO this isn"t right, but I need to figure out what to do in this case.
    this.assertEquals(display.map(11111111111111.111111111), "11111.111GHz", "Shorten 11111111111111.111111111 should work.");
  }

  test_parse {
    this.assertFloatEquals(display.parse("1.111nHz"), 0.000000001111, "Parse nano should work.");

    // @TODO looks like pico doesn"t match on OSX
    // this.assertFloatEquals(display.parse("1.111µHz"), 0.000001111, "Parse pico should work.");

    this.assertFloatEquals(display.parse("11.111mHz"), 0.011111, "Parse milli should work.");
    this.assertFloatEquals(display.parse("11.111Hz"), 11.111, "Parse ones should work.");
    this.assertFloatEquals(display.parse("11.111KHz"), 11111, "Parse kilo should work.");
    this.assertFloatEquals(display.parse("11.111MHz"), 11111000, "Parse mega should work.");

    // Note - more than a few G wraps around with floats.
    this.assertFloatEquals(display.parse("1.1111GHz"), 1111100000, "Parse giga should work.");
    this.assertFloatEquals(display.parse("+111Hz"), 111, "Leading plusses are fine.");
    this.assertFloatEquals(display.parse("+1 1  1Hz"), 111, "Spaces are fine.");
    this.assertFloatEquals(display.parse("-1 1  1Hz"), -111, "Negatives are fine.");
    this.assertFloatEquals(display.parse("-1,000,000Hz"), -1000000, "Commas are fine.");

    display.units = "m";
    this.assertFloatEquals(display.parse("111m"), 111, "Units are ignored even if they look like metric prefixes.");
  }

  test_posineg {
    this.assertEquals(display.posneg(-1), "-", "Posneg negative should be -");
    this.assertEquals(display.posneg(1), "", "Posneg positive should be blank");
    this.assertEquals(display.posneg(0), "", "Posneg zero should be blank");
  }

  test_getFromParam {
    param.value = 10;
    this.assertFloatEquals(display.getFromParam(param), 10, "GetFromParam via param returns value");
    this.assertFloatEquals(display.getFromParam(5), 5, "GetFromParam via number returns number");
  }
}
