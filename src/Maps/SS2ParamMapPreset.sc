SS2ParamMapPreset : IdentityDictionary {
	*new {
		arg map, args = nil;
		var p = super.new();
		p.import(map, args);
		^ p;
	}

	import {
		arg map, args = nil;
		args = args.defaultWhenNil(map.keys);
		args.do {
			arg key;
			this.put(key, map[key].export);
		};
		^ this;
	}

	export {
		arg map, args = nil;
		args = args.defaultWhenNil(map.keys);
		args.do {
			arg key;
			map[key].import(this.at(key));
		};
		^ this;
	}
}
