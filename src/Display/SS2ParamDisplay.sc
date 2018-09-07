SS2ParamDisplay : Object {
  var <>units;
  var <>round;
  var <>scale;

  const <posString = "";
  const <negString = "-";
  const <zeroString = "";

  *new {
		arg round = 0.01, units = '', scale = 1;
		var p = super.new();
		p.init(round, units, scale);
    ^ p;
	}

	init {
		arg a_round = 0.01, a_units = '', a_scale = 1;
		units = a_units;
		scale = a_scale;
		round = a_round;
    ^ this;
	}

  map {
		arg n;
    var s;
    n = this.getFromParam(n);
		s = this.shorten(n * scale);
    ^ s;
  }

  getFromParam {
    arg n;
    if (n.isKindOf(SS2Param)) {
      n = n.value;
    };
    ^ n;
  }

	unmap {
		arg param, n;
		param.value = this.parse(n);
	}

  // Based on the value of n, return a +/- prefix.
  posneg {
    arg n = 0;
    n = n.asFloat;
    if (n < 0, { ^ this.negString });
    if (n > 0, { ^ this.posString });
    if (n == 0, { ^ this.zeroString });
  }

	negString {
		^ "-";
	}

	posString {
		^ "";
	}

	zeroString {
		^ "";
	}

  // Take huge values like 1,500,000 and display like 1.5M
  shorten {
    arg n = 0, significantDigits = 4;
    var prefix, s;
    n = this.getFromParam(n);
    this.compressPrefixes().do {
      arg testPrefix;
      if (n >= testPrefix[0]) {
        prefix = testPrefix;
      };
    };

    // Handle when n is too small/big for all prefixes.
    if (prefix.isNil) {
      prefix = [1, ""];
    };

    // Finally we can apply the prefix!
    n = (n/prefix[0]);
    n = n.asFloat().round(round);
    s = if (n < 1000) {
      this.posneg(n) ++ n.abs.asStringPrec(significantDigits);
    } {
      this.posneg(n) ++ n.abs.asStringPrec(significantDigits + n.log10.floor);
    };
    s = s ++ prefix[1] ++ units.asString();
    ^ s;
  }


  // Return a value based on a string that looks like the display.
  // Similar to the opposite of SS2ParamDisplay::shorten
  parse {
    arg s;
    var n, prefix, matchPrefix;
    s = s.asString();
    s = s.replace(posString.asString(), "");
    s = s.replace(negString.asString(), "-");
    s = s.replace(zeroString.asString(), "");
    s = s.replace(" ", "");
    s = s.replace(",", "");
    s = s.replace(units.asString(), "");

    n = s.asFloat() / scale;

    // Match the prefix units
    matchPrefix = s.findRegexp("[0-9]([a-zA-Z])");
    if (matchPrefix.isArray && matchPrefix[0].size > 1) {
      matchPrefix = matchPrefix[0][1][1].asString;
      prefix = this.compressPrefixes().select {
        arg testPrefix;
        testPrefix[1] == matchPrefix;
      };
      if (prefix.size > 0) {
        n = n * prefix[0][0];
      };
    };
    ^n;
  }

  compressPrefixes {
    ^ [
      [0.000000001, "n"],
      [0.000001, "µ"],
      [0.001, "m"],
      [1, ""],
      [1000, "K"],
      [1000000, "M"],
      [1000000000, "G"],
    ];
  }

  centered {
    ^ false;
  }
}
