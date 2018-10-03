SS2ParamSlider : SS2ParamWidget {

	*new {
		arg parent = nil, bounds = 48@24, param = nil, register = false;
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
		knob = Slider(this, 48@12);
		magicTitle = SS2WidgetMagicTitle(this, bounds: 48@12, param: param);
		if (param.isNil.not) {
			this.register(param);
			this.observe(param);
		};
		^ this;
	}

	buildKnob {
		arg param;
		knob.thumbSize_(8)
			.action_({
				arg a_knob;
				param.normalized = a_knob.value;
				magicTitle.observe(param);
			})
			.keyDownAction_({
				arg doc, char, mod, unicode, keycode, key;
				this.textFieldAppearAction.();
			})
			.focusGainedAction_({
				magicTitle.showValue;
			})
			.focusLostAction_({
				magicTitle.showLabel;
			});
		^ this;
	}
}
