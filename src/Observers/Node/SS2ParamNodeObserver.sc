SS2ParamNodeObserver : SS2ParamObserver {
	var node;
	var symbol;

	*new {
		arg node, symbol = nil;
		var p = super.new();
		^ p.init(node, symbol);
	}

	init {
		arg a_node, a_symbol;
		node = a_node;
		symbol = a_symbol;
		^ this;
	}

	observe {
		arg param;
		node.set(symbol, param.value);
		^ this;
	}

	register {
		arg param;
		^ this;
	}
}
