NopaPercentString : NopaCenterableString {
  *new {
    arg digits = 3, centered = false, scale = 100;
    var p = super.new();
    p.init(digits, centered);
    ^ p;
  }

  init {
    arg a_digits = 3, a_centered = false, a_scale = 100;
    this.digits = a_digits;
		this.units = "%";
		this.scale = a_scale;
    centered = a_centered;
    ^ this;
  }

  map {
    arg n;
    var s;
    n = this.getFromParam(n).linlin(0, 1, this.lowerBound(), 1) * scale;
    s = this.shorten(n, digits) ++ units;
    ^ s;
  }

  unmap {
    arg param, n;
    param.value = (this.parse(n)).linlin(this.lowerBound(), 1, param.min, param.max);
    ^ this;
  }

  getFromParam {
    arg n;
    if (n.isKindOf(NopaAbstractParam)) {
      n = n.value.linlin(n.min, n.max, 0, 1);
    };
    ^ n;
  }
}
