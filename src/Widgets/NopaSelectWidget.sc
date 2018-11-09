NopaSelectWidget : NopaOptionWidget {
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
		select = PopUpMenu(this, 48@16);
		labelText = StaticText(this, 48@12);

		if (param.isNil.not) {
			this.register(param);
		};
		^ this;
	}

}
