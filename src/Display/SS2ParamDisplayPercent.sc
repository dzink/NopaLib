SS2ParamDisplayPercent : SS2ParamDisplayNormalized {
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
}
