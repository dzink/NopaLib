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
		map.keysValuesDo {
			arg key, param;
			this.put(key, param.export);
		};
		^ this;
	}

	export {
		arg map, args = nil;
		args = args.defaultWhenNil(this.keys);
		map.keysValuesDo {
			arg key, param;
			param.import(this.at(key));
		};
		^ this;
	}
}
