NopaWidgetMagicTitle : CompositeView {
	var <textField;
	var <labelText;
	var <valueText;
	var <composite;

	*new {
		arg parent = nil, bounds = 48@20, param = nil;
		var c;
		c = super.new(parent, bounds);
		c.init(param);
		^ c;
	}

	init {
		arg a_param;
		this.buildWidget(a_param);
		^ this;
	}

	param_ {
		arg param;
		this.setPropertiesFromParam(param);
		^ this;
	}

	buildWidget {
		arg param = nil;
		labelText = StaticText(bounds: this.bounds);
		valueText = StaticText(bounds: this.bounds);
		textField = TextField(bounds: this.bounds);
		this.buildComposite();

		if (param.isNil.not) {
			this.register(param);
		};
		^ this;
	}

	buildComposite {
		this.layout = StackLayout(labelText, valueText, textField)
			.mode_(0)
			.index_(0);
			// .mouseLeaveAction_({this.showLabel});
		^ this;
	}

	register {
		arg param;
		if (param.isKindOf(NopaAbstractParam)) {
			this.buildLabelText(param);
			this.buildValueText(param);
			this.buildTextField(param);
			this.observe(param);
		} {
			"Widget param should be a kind of NopaAbstractParam".warn;
		};
		^ this;
	}

	buildLabelText {
		arg param;
		labelText.string_(param.label)
			.align_(\center)
			.font_(Font("Helvetica",9))
			.mouseDownAction_({this.showTextField});
		^ this;
	}

	buildValueText {
		arg param;
		valueText.string_(param.display)
			.align_(\center)
			.font_(Font("Helvetica",9))
			.mouseDownAction_({this.showTextField});
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
			});
		^ this;
	}

	textFieldSubmitAction {
		arg param;
		^ {
			arg a_textField;
			param.display = a_textField.value;
			this.observe(param);
			this.showLabel;
		};
	}

	observe {
		arg param;
		valueText.string_(param.display);
		textField.string_(param.display);
		this.toolTip = param.display;
		^ this;
	}

	showLabel {
		this.layout.index_(0);
		^ this;
	}

	showValue {
		this.layout.index_(1);
		^ this;
	}

	showTextField {
		this.layout.index_(2);
		textField.focus;
		^ this;
	}

}
