/**
 * SS2SS2ParamDisplay: A class to help convert params into human-readable
 * strings. SS2SS2ParamDisplay is generalized, and passes parameter values
 * through mostly unchanged.
 */

SS2ParamDisplay : Object {
  var <units;
  var <digits;
  var <scale;

  const <posString = "";
  const <negString = "-";
  const <zeroString = "";

  const maxSiIndex = 9;
  const minSiIndex = -9;

  *new {
		arg units = "", digits = 3, scale = 1;
		var p = super.new();
		p.init(units, digits, scale);
    ^ p;
	}

	init {
		arg a_units = "", a_digits = 3, a_scale = 1;
		this.units = a_units;
		this.scale = a_scale;
		this.digits = a_digits;
    ^ this;
	}

  /**
   * Note: setting digits to nil or 0 disables SI/sn notation.
   */
  digits_ {
    arg a_digits;
    digits = a_digits.defaultWhenNil(0).asFloat();
  }

  units_ {
    arg a_units;
    units = a_units.defaultWhenNil("").asString();
  }

  scale_ {
    arg a_scale;
    scale = a_scale.defaultWhenNil(1).asFloat();
  }

  map {
		arg n;
    var s;
    n = this.getFromParam(n);
		s = this.shorten(n * scale) ++ units;
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
    ^ this;
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

  /**
   * Shorten large values for display.
   */
  shorten {
    arg n = 0, significantDigits = nil;
    var base, s, log10, si;
    n = this.getFromParam(n);
    significantDigits = significantDigits.defaultWhenNil(digits).asFloat();
    si = [1, ""];

    if (n.abs == inf) {
      ^ this.posneg(n) ++ "∞";
    };

    if (significantDigits > 0) {
      log10 = if (n == 0) { 1 } { n.abs.log10.floor };

      // Fallback for scientific notation outside of SI prefixes.
      if (log10 == log10.clip(minSiIndex, maxSiIndex)) {
        si = this.siPrefixes.detect({ |c| (c[0] <= log10) });
      } {
        si = [log10, "e" ++ log10.asString()];
      };
      base = n.abs / (10 ** si[0]);

      // Round to significantDigits;
      base = base.round(10 ** (significantDigits - base.log10.floor - 1).neg);

      s = this.posneg(n) ++ base.asString() ++ si[1];
      ^ s;
    } {
      s = this.posneg(n) ++ n.abs.asString();
      ^ s;
    };
  }


  /**
   * Parse a string, and return a float value. This checks for special posneg
   * strings, SI units, exponentials, and units. It is fairly good at tossing
   * out extraneous data.
   * Also, scaling is applied here.
   */
  parse {
    arg s;
    var n, prefix, matchSi;
    s = s.asString();
    s = s.replace("∞", "inf");
    s = s.replace(posString.asString(), "");
    s = s.replace(negString.asString(), "-");
    s = s.replace(zeroString.asString(), "");
    s = s.replace(" ", "");
    s = s.replace(",", "");
    s = s.replace(units.asString(), "");

    n = s.asFloat() / scale;

    prefix = this.parseSi(s);
    if (prefix.isNil.not) {
      n = n * (10 ** prefix[0]);
    };

    ^n;
  }

  /**
   * Find if any SI characters follow the digits, return array as in siPrefixes
   * @param s <string>
   *   A string that can be parsed as a number, ie "-5.33ns" (ns = nansec).
   * @return array
   *   Returns an array from siPrefixes in format [<log10>, <string>]
   */
  parseSi {
    arg s;
    var si, matchSi;
    matchSi = s.findRegexp("[0-9]([a-zA-Z])");
    if (matchSi.isArray && matchSi[0].size > 1) {
      matchSi = matchSi[0][1][1].asString;
      si = this.siPrefixes().detect { |test| test[1] == matchSi; };
    };
    ^ si;
  }

  /**
   * Get Systèm International list, in format [<log10>, <string>].
   */
  siPrefixes {
    ^ [
      [9, "G"],
      [6, "M"],
      [3, "K"],
      [0, ""],
      [-3, "m"],
      [-6, "µ"],
      [-9, "n"],
    ];
  }

  /**
   * This kind of display is never centered.
   */
  centered {
    ^ false;
  }
}
