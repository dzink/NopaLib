SS2ParamWidget {
	var <textField;
	var <knob;
	var <labelText;
	var <valueText;
	var <composite;
	var <param;

	var param;

	*new {
		arg container, param, register = false;
		var p = super.new();
		p.init(container, param);
		^ p;
	}

	init {
		arg container, a_param;
		param = a_param;
		this.buildWidget(container);
		^ this;
	}

	param_ {
		arg a_param;
		param = a_param;
		this.setPropertiesFromParam(param);
		^ this;
	}

	buildWidget {
		arg container;
		composite = CompositeView(container, 52@96);
		this.buildComposite();

		knob = Knob(composite, 48@32);
		labelText = StaticText(composite, 48@8);
		valueText = StaticText(composite, 48@14);
		textField = TextField(composite, 48@10);
		if (param.isNil.not) {
			this.resetBuildProperties();
		};
		^ this;
	}

	buildComposite {
		composite.decorator = FlowLayout(composite.bounds);
		^ this;
	}

	setPropertiesFromParam {
		arg p;
		p = p.defaultWhenNil(param);
		if (p.isKindOf(SS2Param)) {
			this.buildKnob(p);
			this.buildLabelText(p);
			this.buildValueText(p);
			this.buildTextField(p);
		} {
			"Widget param should be a kind of SS2Param".warn;
		};
		^ this;
	}

	buildKnob {
		arg p;
		knob.centered_(p.displayStrategy.centered)
			.action_({
				arg a_knob;
				p.normalized = a_knob.value;
				this.syncToParam();
			})
			.keyDownAction_({
				arg doc, char, mod, unicode, keycode, key;
				this.textFieldAppearAction.();
			});
		^ this;
	}

	buildLabelText {
		arg p;
		labelText.string_(p.label)
			.align_(\center)
			.font_(Font("Helvetica",9));
		^ this;
	}

	buildValueText {
		arg p;
		valueText.string_(p.display)
			.align_(\center)
			.font_(Font("Helvetica",9))
			.mouseDownAction_(this.textFieldAppearAction);
		^ this;
	}

	buildTextField {
		arg p;
		textField.font_(Font("Helvetica",11))
			.string_(p.display)
			.focusLostAction_(this.textFieldSubmitAction(p))
			.keyDownAction_({
				arg doc, char, mod, unicode, keycode, key;
				if (key == 16777220) {
					this.textFieldSubmitAction(p).(textField);
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
		arg p;
		^ {
			arg a_textField;
			p.display = a_textField.value;
			this.syncToParam();
			textField.string_(p.display).visible_(false);
			valueText.string_(p.display).visible_(true);
		};
	}

	syncToParam {
		knob.value = param.normalized;
		valueText.string_(param.display);
		textField.string_(param.display);
	}

}
