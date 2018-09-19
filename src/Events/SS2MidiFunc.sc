SS2MidiFunc {
	var <>func;
	var <>param;

	*bend {
		arg channel = nil, srcID = nil, param = nil;
		var p = super.new();
		p.func = MIDIFunc.bend({
			arg val = 0;
			p.normalized = val / 16383.0;
		}, channel, srcID);
		if (param.isNil.not) {
			p.param = param;
		};
		^ p;
	}

	*cc {
		arg ccNum = 0, channel = nil, srcID = nil, param = nil;
		var p = super.new();
		p.func = MIDIFunc.cc({
			arg cc, val = 0;
			p.midi_(val);
		}, ccNum, channel, srcID);
		if (param.isNil.not) {
			p.param = param;
		};
		^ p;
	}

	disable {
		func.disable;
		^ this;
	}

	enable {
		func.enable;
		^ this;
	}

	enabled_ {
		arg enabled = true;
		if (enabled.asBoolean()) {
			this.enable;
		} {
			this.disable;
		};
		^ this;
	}

	normalized_ {
		arg n;
		param.normalized = n;
		^ this;
	}

	midi_ {
		arg m;
		param.midi = m;
		^ this;
	}

	register {
		arg a_param;
		this.param = a_param;
		^ this;
	}
}
