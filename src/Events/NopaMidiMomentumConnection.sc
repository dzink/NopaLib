NopaMidiMomentumConnection : NopaMidiConnection{
	var <>func;
	var lastMessageTime;
	var lastMessageValue;
	var <sensitivity;

	const maxTime = 0.25;
	const minTime = 0.025;
	const maxValue = 1;
	const changeScale = 3;

	*cc {
		arg param, ccNum = 0, channel = nil, srcID = nil;
		var p = super.new();
		p.init();
		p.func = p.ccFunc(param, ccNum, channel, srcID);
		^ p;
	}

	init {
		lastMessageTime = 0;
		lastMessageValue = 0;
	}

	ccFunc {
		arg param, ccNum = 0, channel = nil, srcID = nil;
		this.func = MIDIFunc.cc({
			arg cc, val;
			val = lastMessageValue + this.midiFlex(val);
			this.param.midi = val;
			this.setCc(cc, val);
		}, ccNum, channel, srcID);
	}

	setCc {
		arg cc, val;
		// @TODO
		^ this;
	}

	sensitivity_ {
		arg s;
		sensitivity = max(s, 1);
		^ this;
	}

	/**
	 * Compares incoming MIDI messages to previous messages in order to scale up
	 * or down very fast/slow MIDI movements.
	 */
	midiFlex {
		arg val, now;
		var timeDiff, valueDiff;
		now = now ? SystemClock.seconds;
		timeDiff = (now - lastMessageTime).clip(minTime, maxTime) / maxTime;
		valueDiff = (val - lastMessageValue).clip(maxValue.neg, maxValue) / maxValue;
		lastMessageTime = now;
		lastMessageValue = now;

		^ valueDiff * timeDiff.linexp(minTime, maxTime, sensitivity, sensitivity.reciprocal) * changeScale;
	}
}
