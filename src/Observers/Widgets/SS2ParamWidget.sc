SS2ParamWidget : SS2ParamObserver {
	var <textField;
	var <knob;
	var <labelText;
	var <valueText;
	var <composite;

	*new {
		arg container, param, register = false;
		var p = super.new();
		p.init(container, param);
		^ p;
	}

	init {
		arg container, param;
		this.buildWidget(container, param);
		^ this;
	}

	param_ {
		arg param;
		this.setPropertiesFromParam(param);
		^ this;
	}

	buildWidget {
		arg container, param = nil;
		composite = CompositeView(container, 52@96);
		this.buildComposite();

		knob = Knob(composite, 48@32);
		labelText = StaticText(composite, 48@8);
		valueText = StaticText(composite, 48@14);
		textField = TextField(composite, 48@10);
		if (param.isNil.not) {
			this.register(param);
			this.observe(param);
		};
		^ this;
	}

	buildComposite {
		composite.decorator = FlowLayout(composite.bounds);
		^ this;
	}

	register {
		arg param;
		if (param.isKindOf(SS2Param)) {
			this.buildKnob(param);
			this.buildLabelText(param);
			this.buildValueText(param);
			this.buildTextField(param);
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
				this.textFieldAppearAction.();
			});
		^ this;
	}

	buildLabelText {
		arg param;
		labelText.string_(param.label)
			.align_(\center)
			.font_(Font("Helvetica",9));
		^ this;
	}

	buildValueText {
		arg param;
		valueText.string_(param.display)
			.align_(\center)
			.font_(Font("Helvetica",9))
			.mouseDownAction_(this.textFieldAppearAction);
		^ this;
	}

	buildTextField {
		arg param;
		textField.font_(Font("Helvetica",11))
			.string_(param.display)
			.focusLostAction_(this.textFieldSubmitAction(param))
			.keyDownAction_({
				arg doc, char, mod, unicode, keycode, key;
				if (key == 16777220) {
					this.textFieldSubmitAction(param).(textField);
				};
			})
			.bounds_(valueText.bounds)
			.visible_(false);
		^ this;
	}

	textFieldAppearAction {
		^ {
			textField.visible_(true).focus(true);
			valueText.visible_(false);
		};
	}

	textFieldSubmitAction {
		arg param;
		^ {
			arg a_textField;
			param.display = a_textField.value;
			this.observe(param);
			textField.string_(param.display).visible_(false);
			valueText.string_(param.display).visible_(true);
		};
	}

	observe {
		arg param;
		knob.value = param.normalized;
		valueText.string_(param.display);
		textField.string_(param.display);
		^ this;
	}

}
