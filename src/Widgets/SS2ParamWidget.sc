SS2ParamWidget {
	var <textField;
	var <knob;
	var <labelText;
	var <valueText;
	var <composite;

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

	buildWidget {
		arg container;
		composite = CompositeView(container, 52@96);
		this.buildComposite();

		knob = Knob(composite, 48@32);
		labelText = StaticText(composite, 48@8);
		valueText = StaticText(composite, 48@14);
		textField = TextField(composite, 48@10);
		this.buildKnob();
		this.buildLabelText();
		this.buildValueText();
		this.buildTextField();
		^ this;
	}

	buildComposite {
		composite.decorator = FlowLayout(composite.bounds);
		^ this;
	}

	buildKnob {
		knob.centered_(param.displayStrategy.centered)
			.action_({
				arg a_knob;
				param.normalized = a_knob.value;
				this.syncToParam();
			})
			.keyDownAction_({
				arg doc, char, mod, unicode, keycode, key;
				this.textFieldAppearAction.();
			});
		^ this;
	}

	buildLabelText {
		labelText.string_(param.label)
			.align_(\center)
			.font_(Font("Helvetica",9));
		^ this;
	}

	buildValueText {
		valueText.string_(param.display)
			.align_(\center)
			.font_(Font("Helvetica",9))
			.mouseDownAction_(this.textFieldAppearAction);
		^ this;
	}

	buildTextField {
		textField.font_(Font("Helvetica",11))
			.string_(param.display)
			.focusLostAction_(this.textFieldSubmitAction)
			.keyDownAction_({
				arg doc, char, mod, unicode, keycode, key;
				if (key == 16777220) {
					this.textFieldSubmitAction.(textField);
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
		^ {
			arg a_textField;
			param.display = a_textField.value;
			this.syncToParam();
			textField.string_(param.display).visible_(false);
			valueText.string_(param.display).visible_(true);
		};
	}

	syncToParam {
		knob.value = param.normalized;
		valueText.string_(param.display);
		textField.string_(param.display);
	}

}
