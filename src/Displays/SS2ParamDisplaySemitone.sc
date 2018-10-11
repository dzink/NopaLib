/**
 * SS2ParamDisplaySemitone: A class to help convert params into human-readable
 * semitones based on midi ratios - i.e. 0.5 is the ratio for one octave down,
 * so "-12st", 2 is one octave up, so "+12st", etc.
 */

SS2ParamDisplaySemitone : SS2ParamDisplayCenterable {
  var <convertFromRatio = false;
  var <convertFromHz = false;
  const maxSiIndex = 0;
  const minSiIndex = -2;

  *new {
		arg digits = 3, scale = 1;
		var p = super.new();
		p.init(digits, scale);
    ^ p;
	}

	init {
		arg a_digits = 3, a_scale = 1;
    this.digits = a_digits;
		this.units = "st";
		this.scale = a_scale;
    ^ this;
	}

  map {
		arg n;
    var s;
    n = this.getFromParam(n);
    if (convertFromRatio) {
      n = n.ratiomidi().round(0.00001);
    };
    if (convertFromHz) {
      n = n.cpsmidi().round(0.00001);
    };
		s = this.shorten(n * scale);
    ^ s;
  }

	unmap {
		arg param, n;
		n = this.parse(n);
    if (convertFromRatio) {
      n = n.midiratio().round(0.00001);
    };
    if (convertFromHz) {
      n = n.midicps().round(0.00001);
    };
    param.value = n;
	}

	getFromParam {
    arg n;
    if (n.isKindOf(SS2Param)) {
      n = n.value;
    };
    ^ n;
  }

	posString {
		^ "+";
	}

  convertFromRatio_ {
    arg convert = true;
    convertFromRatio = convert.asBoolean;
    ^ this;
  }

  convertFromHz_ {
    arg convert = true;
    convertFromHz = convert.asBoolean;
    ^ this;
  }


  /**
   * Prefixes are steps and cents.
   */
  siPrefixes {
    ^ [
      [0, "st"],
      [-2, "c"],
    ];
  }
}
