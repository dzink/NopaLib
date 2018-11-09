/**
 * NopaCenterableString: A class to help convert params into
 * human-readable strings. Similar to NopaString, only positive numbers
 * are prefixed with "+".
 */


NopaCenterableString : NopaString {
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
