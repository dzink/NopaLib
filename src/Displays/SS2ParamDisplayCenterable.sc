/**
 * SS2ParamDisplayCenterable: A class to help convert params into
 * human-readable strings. Similar to SS2ParamDisplay, only positive numbers
 * are prefixed with "+".
 */


SS2ParamDisplayCenterable : SS2ParamDisplay {
  var <centered;

  centered_ {
    arg center = true;
    centered = center.asBoolean();
    ^ this;
  }

  center {
    centered = true;
    ^ this;
  }

	posString {
		^ if (centered) { "+"; } { ""; };
	}

	lowerBound {
		^ if (centered) { -1 } { 0 };
	}

}
