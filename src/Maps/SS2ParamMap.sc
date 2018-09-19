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
      array = array.add(this[key].value);
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
    ^ SS2ParamMapPreset(this, args);
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
    this.do { |p| if (p.respondsTo(\symbol_)) { p.symbol = symbol }; };
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
    this.do { |p| if (p.respondsTo(\index_)) { p.index = index }; };
    cachedArgsArray = nil;
    ^ this;
  }

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

  import {
    arg preset;
    preset.export(this);
    ^ this;
  }

  export {
    arg preset;
    preset.import(this);
    ^ this;
  }

}
