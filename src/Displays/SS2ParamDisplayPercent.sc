SS2ParamDisplayPercent : SS2ParamDisplayCenterable {
  *new {
    arg digits = 3, centered = false;
    var p = super.new();
    p.init(digits, centered);
    ^ p;
  }

  init {
    arg a_digits = 3, a_centered = false;
    this.digits = a_digits;
		this.units = "%";
		this.scale = 100;
    centered = a_centered;
    ^ this;
  }

  map {
    arg n;
    var s;
    n = this.getFromParam(n).linlin(0, 1, this.lowerBound(), 1) * scale;
    s = this.shorten(n) ++ units;
    ^ s;
  }

  unmap {
    arg param, n;
    param.value = (this.parse(n)).linlin(this.lowerBound(), 1, n.min, n.max);
  }

  getFromParam {
    arg n;
    if (n.isKindOf(SS2Param)) {
      n = n.value.linlin(n.min, n.max, this.lowerBound, 1);
    };
    ^ n;
  }
}
