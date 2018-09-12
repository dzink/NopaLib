/**
 * SS2ParamDisplayCenterable: A class to help convert params into
 * human-readable strings. Similar to SS2ParamDisplay, only positive numbers
 * are prefixed with "+".
 */


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
