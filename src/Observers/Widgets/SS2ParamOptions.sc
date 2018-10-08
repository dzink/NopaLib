SS2ParamOptions : SS2ParamWidget {
	var < select;
	var <> labelText;

	*new {
		arg parent = nil, bounds = 48@48, param = nil, register = false;
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
		select = ListView(this, 48@36);
		labelText = StaticText(this, 48@12);

		if (param.isNil.not) {
			this.register(param);
		};
		^ this;
	}

	buildSelect {
		arg param;
		select.items_(param.symbols)
			.font_(Font("Helvetica",9))
			.action_({
				arg select;
				param.index = select.value;
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

	register {
		arg param;
		if (param.isKindOf(SS2ParamList)) {
			this.buildSelect(param);
			this.buildLabelText(param);
			this.observe(param);
		} {
			"Widget param should be a kind of SS2Param".warn;
		};
		^ this;
	}

	observe {
		arg param;
		AppClock.play({
			select.value = param.index;
			});
		[\observe, param.index, select.value].postln;
		^ this;
	}
}
