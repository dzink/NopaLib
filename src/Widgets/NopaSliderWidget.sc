NopaSliderWidget : NopaKnobWidget {

	*new {
		arg parent = nil, bounds = 48@28, param = nil, register = false;
		var p = super.new(parent, bounds);
		p.init(param);
		^ p;
	}

	init {
		arg param;
		this.buildWidget(param);
		^ this;
	}

	buildWidget {
		arg param = nil;
		this.buildComposite();
		knob = Slider(this, 48@16);
		magicTitle = NopaWidgetMagicTitle(this, bounds: 48@12, param: param);
		if (param.isNil.not) {
			this.register(param);
			this.observe(param);
		};
		^ this;
	}

	buildKnob {
		arg param;
		super.buildKnob(param);
		knob.thumbSize_(8);
		^ this;
	}
}
