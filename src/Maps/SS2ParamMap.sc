/**
* A map of parameters which can be used to generate arrays suitable for Node
* args.
*/

SS2ParamMap : IdentityDictionary {
  var cachedArgsArray = nil;

  *new {
    arg n = 8;
    var p = super.new(n);
    ^ p;
  }

  *newFrom {
    arg aCollection;
    var p = super.newFrom(aCollection);
    p.setAll;
    ^ p;
  }

  put {
    arg key, value;
    if (value.isKindOf(SS2Param)) {
      super.put(key, value);
    } {
      ("Only SS2Params and subclasses can be added to a SS2ParamMap.")
    };
  }

  asArray {
    arg args = nil;
    var array = Array();
    args = args.defaultWhenNil(this.keys);
    args.do {
      arg key;
      array = array.add(key);
      array = array.add(this[key].transformOut());
    };
    ^ array;
  }

  /**
   * For the full map, get it faster by using the cached array.
   */
  fastArgsArray {
    var array;
    array = if (cachedArgsArray.isNil) {
      this.asArray;
    } {
      cachedArgsArray;
    };
    ^ array;
  }

  asPreset {
    arg args = nil;
    ^ SS2ParamMapPreset.newFrom(this, args: args);
  }

  setNormalized {
    arg key, normalized = 0;
    this[key].normalized = normalized;
    cachedArgsArray = nil;
    ^ this;
  }

  setAllNormalized {
    arg normalized = 0;
    this.do { |p| p.normalized = normalized; };
    cachedArgsArray = nil;
    ^ this;
  }

  setValue {
    arg key, value = 0;
    this[key].value = value;
    cachedArgsArray = nil;
    ^ this;
  }

  setAllValue {
    arg value = 0;
    this.do { |p| p.value = value; };
    cachedArgsArray = nil;
    ^ this;
  }

  setMidi {
    arg key, midi = 63;
    this[key].midi = midi;
    cachedArgsArray = nil;
    ^ this;
  }

  setAllMidi {
    arg midi = 0;
    this.do { |p| p.midi = midi; };
    cachedArgsArray = nil;
    ^ this;
  }

  setSymbol {
    arg key, symbol;
    this[key].symbol = symbol;
    cachedArgsArray = nil;
    ^ this;
  }

  /**
   * Note: not all params will respond to symbol_, so this checks first.
   */
  setAllSymbol {
    arg symbol = 0;
    this.do {
      arg p;
      if (p.respondsTo(\symbol_)) {
        p.symbol = symbol;
      };
    };
    cachedArgsArray = nil;
    ^ this;
  }

  setIndex {
    arg key, index = 0;
    this[key].index = index;
    cachedArgsArray = nil;
    ^ this;
  }

  setAllIndex {
    arg index = 0;
    this.do {
      arg p;
      if (p.respondsTo(\index_)) {
        p.index = index;
      };
      cachedArgsArray = nil;
      ^ this;
    }
  }

  /**
   * A helper function that will compare this paramMap with arguments to a
   * given SynthDef.
   */
  compareToSynthDefs {
    arg synthDefs;
    var sdControlNames = [], paramControlNames, param, sd;
    synthDefs.asArray.do {
        arg synthDef;
        synthDef.allControlNames.do {
            arg controlName;
            sdControlNames = sdControlNames.add(controlName.name);
        };
    };
    paramControlNames = this.keys;
    param = paramControlNames.difference(sdControlNames);
    sd = sdControlNames.difference(paramControlNames);
    if (param.size.asBoolean) {
      ("In SS2ParamMap but not in SynthDefs:").warn;
      (param.asString).warn;
    };
    if (sd.size.asBoolean) {
      ("In SynthDefs but not in SS2Param:").warn;
      (sd.asString).warn;
    };
    ^[param, sd];
  }

  /**
   * Import from a preset.
   */
  import {
    arg preset;
    preset.export(this);
    ^ this;
  }

  /**
   * Export to a preset.
   */
  export {
    arg preset;
    preset.import(this);
    ^ this;
  }

  /**
   * Randomize a set of params in the map.
   * @param Float blend
   *   The amount to randomize. 0 will not randomize at all, while 1 will ignore
   *   the previous value completely. 0.5 will split the difference between the
   *   previous value and the new value.
   * @param Array args
   *   The list of args to randomize.
   */
  randomize {
    arg blend = 1, args = nil;
    args = args.defaultWhenNil(this.keys);
    args.do {
      arg key;
      if (this.at(key).isKindOf(SS2ParamContinuous)) {
        this.at(key).normalized = blend.linlin(0, 1, this.at(key).normalized, 1.0.rand);
      };
    };
    ^ this;
  }

  /**
   * Blends with another ParamMap.
   */
  blend {
    arg paramMap, blend = 0.5, args = nil;
    var p = SS2ParamMap();
    args = args.defaultWhenNil(this.keys);
    this.blendFrom(this, paramMap, blend, args);
    ^ this;
  }

  /**
   * Sets params to be a blend of two other ParamMaps.
   * @param ParamMap paramMap1
   *   A ParamMap to blend. Param structure is copied from this ParamMap.
   * @param ParamMap paramMap2
   *   A ParamMap to blend.
   * @param float blend
   *   Blend amount. 0 is balanced towards paramMap1, 1 towards paramMap2. 0.5
   *   is in the middle.
   * @param Array args
   *   An array of keys to blend.
   * @return
   *   this ParamMap, after it has had its params blended.
   */
  blendFrom {
    arg paramMap1, paramMap2, blend = 0.5, args = nil;
    args = args.defaultWhenNil(paramMap1.keys);
    args.do {
      arg key;
      if (paramMap1[key].isNil.not && paramMap2[key].isNil.not) {

        // In the case that paramMap1 is this, we don't need to copy.
        it (this != paramMap1) {
          this[key] = paramMap1[key].deepCopy;
        };
        this[key].normalized = blend.linlin(0, 1, paramMap1[key].normalized, paramMap2[key].normalized);
      };
    };
    ^ this;
  }
}
