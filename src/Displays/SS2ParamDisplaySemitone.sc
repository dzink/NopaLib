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
		this.units = "";
		this.scale = a_scale;
    ^ this;
	}

	posString {
		^ "+";
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
