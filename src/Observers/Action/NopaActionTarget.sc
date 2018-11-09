/**
 * A generic action observer which will perform any action indicated.
 */

NopaActionTarget : NopaAbstractTarget {
	var func;

	*new {
		arg func;
		var p = super.new();
		^ p.init(func);
	}

	init {
		arg a_func;
		func = a_func;
		^ this;
	}

	observe {
		arg param;
		func.value(param);
		^ this;
	}
}
