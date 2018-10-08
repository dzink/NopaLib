SS2ParamWidget : CompositeView {

	param_ {
		arg param;
		this.buildWidget(param);
		^ this;
	}

	buildWidget {
		arg param = nil;
		this.buildComposite();
		if (param.isNil.not) {
			this.register(param);
			this.observe(param);
		};
		^ this;
	}

	buildComposite {
		this.decorator = FlowLayout(this.bounds).gap_(0@0).margin_(0@0);
		^ this;
	}

	register {
		arg param;
		^ this;
	}

}
