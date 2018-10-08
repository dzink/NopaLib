SS2ParamKnob : CompositeView {
	var < knob;
	var < magicTitle;

	*new {
		arg parent, bounds = 48@48, param = nil, register = false;
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
		knob = Knob(this, 48@32);
		magicTitle = SS2WidgetMagicTitle(this, param: param);
		if (param.isNil.not) {
			this.register(param);
			this.observe(param);
		};
		^ this;
	}

	buildComposite {
		this.decorator = FlowLayout(this.bounds).gap_(0@0).margin_(0@0);
		this.mouseEnterAction_({
				AppClock.play({
					this.magicTitle.showValue;
				});
			})
			.mouseLeaveAction_({
				AppClock.play({
					this.magicTitle.showLabel;
				});
			});
		^ this;
	}

	register {
		arg param;
		if (param.isKindOf(SS2Param)) {
			this.buildKnob(param);
			this.buildMagicTitle(param);
			this.observe(param);
		} {
			"Widget param should be a kind of SS2Param".warn;
		};
		^ this;
	}

	buildKnob {
		arg param;
		knob.centered_(param.displayStrategy.centered)
			.action_({
				arg a_knob;
				param.normalized = a_knob.value;
				this.register(param);
			})
			.keyDownAction_({
				arg doc, char, mod, unicode, keycode, key;
				magicTitle.showTextField;
			})
			.focusGainedAction_({
				magicTitle.showValue;
			})
			.focusLostAction_({
				magicTitle.showLabel;
			});
		^ this;
	}

	buildMagicTitle {
		arg param;
		magicTitle.register(param);
		^ this;
	}

	observe {
		arg param;
		AppClock.play({
			knob.value = param.normalized;
			magicTitle.observe(param);
		});
		^ this;
	}

}
