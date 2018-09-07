SS2ParamDisplayCenterable : SS2ParamDisplay {
  var> centered;

  centered {
    ^ centered;
  }

  center {
    arg center = true;
    centered = center;
    ^ this;
  }

	posString {
		^ if (centered) { "+"; } { ""; };
	}

}
