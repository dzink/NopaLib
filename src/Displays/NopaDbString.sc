/**
 * NopaDbString: A class to help convert params into human-readable
 * decibels based on attenuation ratios - i.e. 0.5 is the ratio for -6dB,
 * so "-6.02dB", 2 is "+6.02dB", etc.
 */


NopaDbString : NopaCenterableString {
  *new {
		arg digits = 2, scale = 1;
		var p = super.new();
		p.init(digits, scale);
    ^ p;
	}

	init {
		arg a_digits = 2, a_scale = 1;
    this.digits = a_digits;
		this.units = "dB";
		this.scale = a_scale;
    ^ this;
	}

  // map {
	// 	arg n;
  //   var s;
  //   n = this.getFromParam(n);
	// 	s = (n * scale).round(10 ** digits.neg).asString() ++ units;
  //   ^ s;
  // }
  //
	// unmap {
	// 	arg param, n;
	// 	n = this.parse(n).dbamp;
  //   if (convertToAmps) {
  //     n = n.dbamp.round(0.00001);
  //   };
  //   param.value = n;
  //   ^ this;
	// }
  //
	// getFromParam {
  //   arg n;
  //   if (n.isKindOf(NopaAbstractParam)) {
  //     n = n.value;
  //   };
  //   ^ n;
  // }

  centered {
    ^ false;
  }

	negString {
		^ "-";
	}

	posString {
		^ "+";
	}

	zeroString {
		^ "+";
	}

  /**
   * No prefixes.
   */
  siPrefixes {
    ^ [
      [3, "K"],
      [0, ""],
      [-3, "m"],
    ];
  }
}
