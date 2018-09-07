SS2ParamMap : IdentityDictionary {
  /**
   * A map of parameters which can be used to set Node args. Not to be confused
   * with an SS2MidiMap, which maps these parameters to an outside controller.
   */
  var cachedArgsArray = nil;

  *new {
    arg n = 8;
    var p = super.new(n);
    p = p.initAll;
    ^ p;
  }

  *newFrom {
    arg aCollection;
    var p = super.newFrom(aCollection);
    p.initAll;
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

  importAll {
    arg preset;
    preset.keysValuesDo {
      arg key, importValue;
      this.import(key, importValue);
    }
  }

  import {
    arg key, importValue;
    this[key].import(importValue);
  }

  asArgsArray {
    arg key;
    ^ [key, this[key].value];
  }

  allAsArgsArray {
    var args;
    args = this.keys.collect {
      arg key;
      this.asArgsArray(key);
    };
    args = args.asArray.flatten;
    ^ args;
  }

  initAll {
    arg m = nil;
    m = m.defaultWhenNil(63.5);
    this.do {
      arg param;
      if (param.value.isNil || param.midi.isNil) {
        param.setMidi(m);
      };
    }
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

}
