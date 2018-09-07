SS2ParamDisplayPercent : SS2ParamDisplayNormalized {
  *new {
    arg round = 0.01, centered = false;
    var p = super.new();
    p.init(round, centered);
    ^ p;
  }

  init {
    arg a_round = 0.01, a_centered = false;
    round = a_round;
    units = "%";
    scale = 100;
    centered = a_centered;
    ^ this;
  }
}
