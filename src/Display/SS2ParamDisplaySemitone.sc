/**
 * SS2ParamDisplaySemitone: A class to help convert params into human-readable
 * semitones based on midi ratios - i.e. 0.5 is the ratio for one octave down,
 * so "-12st", 2 is one octave up, so "+12st", etc.
 */

SS2ParamDisplaySemitone : SS2ParamDisplayCenterable {

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
    n = this.getFromParam(n).ratiomidi().round(0.00001);
		s = this.shorten(n * scale) ++ units;
    ^ s;
  }

	unmap {
		arg param, n;
		param.value = this.parse(n).midiratio().round(0.00001);
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
}
