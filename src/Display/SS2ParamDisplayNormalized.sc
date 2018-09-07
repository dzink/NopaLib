SS2ParamDisplayNormalized : SS2ParamDisplayCenterable {

	*new {
		arg round = 0.01, units = "", scale = 1, centered = false;
		var p = super.new();
		p.init(round, units, scale, centered);
		^ p;
	}

	init {
		arg a_round = 0.01, a_units = "", a_scale = 1, a_centered = false;
		round = a_round;
		units = a_units;
		scale = a_scale;
		centered = a_centered;
		^ this;
	}

	map {
		arg n;
		var s;
		n = this.getFromParam(n).linlin(0, 1, this.lowerBound(), 1) * scale;
		s = this.shorten(n);
		^ s;
	}

	unmap {
		arg param, n;
		param.normalized = (this.parse(n)).linlin(this.lowerBound(), 1, 0, 1);
	}

	getFromParam {
		arg n;
		if (n.isKindOf(SS2Param)) {
			n = n.normalized;
		};
		^ n;
	}

	lowerBound {
		^ if (centered) { -1 } { 0 };
	}

}
