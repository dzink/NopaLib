SS2ParamDisplaySemitone : SS2ParamDisplay {

  *new {
		arg round = 0.01, scale = 1;
		var p = super.new();
		p.init(round, scale);
    ^ p;
	}

	init {
		arg a_round = 0.01, a_scale = 1;
		scale = a_scale;
		round = a_round;
		units = "st";
    ^ this;
	}

  map {
		arg n;
    var s;
    n = this.getFromParam(n).ratiomidi();
		s = this.shorten(n * scale);
    ^ s;
  }

	unmap {
		arg param, n;
		param.value = this.parse(n).midiratio();
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
