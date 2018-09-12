/**
 * SS2ParamDisplayDb: A class to help convert params into human-readable
 * decibels based on attenuation ratios - i.e. 0.5 is the ratio for -6dB,
 * so "-6.02dB", 2 is "+6.02dB", etc.
 */


SS2ParamDisplayDb : SS2ParamDisplay {

  *new {
		arg digits = 3, scale = 1;
		var p = super.new();
		p.init(digits, scale);
    ^ p;
	}

	init {
		arg a_digits = 3, a_scale = 1;
    this.digits = a_digits;
		this.units = "dB";
		this.scale = a_scale;
    ^ this;
	}

  map {
		arg n;
    var s;
    n = this.getFromParam(n).abs.ampdb();
		s = this.shorten(n * scale) ++ units;
    ^ s;
  }

	unmap {
		arg param, n;
		param.value = this.parse(n).dbamp;
    ^ this;
	}

	getFromParam {
    arg n;
    if (n.isKindOf(SS2Param)) {
      n = n.value;
    };
    ^ n;
  }

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
}
