SS2MidiFunc {
	var <>func;

	*bend {
		arg param, channel = nil, srcID = nil;
		var p = super.new();
		p.func = MIDIFunc.bend({
			arg val = 0;
			param.normalized = val / 16383.0;
		}, channel, srcID);
		^ p;
	}

	*cc {
		arg param, ccNum = 0, channel = nil, srcID = nil;
		var p = super.new();
		p.func = MIDIFunc.cc({
			arg val = 0;
			param.midi = val;
		}, ccNum, channel, srcID);
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
}
