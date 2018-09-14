SS2MidiFuncResponsive : SS2MIDIFunc{
	var <>func;
	var lastMessageTime;
	var lastMessageValue;
	var <sensitivity;

	const maxTime = 0.25;
	const minTime = 0.025;
	const maxValue = 1;

	*cc {
		arg param, ccNum = 0, channel = nil, srcID = nil;
		var p = super.new();
		lastMessageTime = 0;
		lastMessageValue = 0;
		p.func = MIDIFunc.cc({
			arg cc, val;
			val = lastMessageValue + this.midiFlex(val);
			param.midi = val;
			this.setCc(cc, val);
		}, ccNum, channel, srcID);
		^ p;
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
		now = now.defaultWhenNil(SystemClock.seconds);
		timeDiff = min(now - lastMessageTime, maxtime) / maxTime;
		valueDiff = (val - lastMessageValue).clip(maxValue.neg, maxValue) / maxValue;
		lastMessageTime = now;
		lastMessageValue = now;

		^ valueDiff * timeDiff.linexp(minTime, maxTime, 1, sensitivity.reciprocal) * changeScale;
	}
}
