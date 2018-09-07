SS2Preset : IdentityDictionary {

  *newFrom {
    arg paramMap;
    var p = super.new;
    p.importAll(paramMap);
    ^ p;
  }

  clean {
    arg paramMap;
    this.keysDo {
      arg key;
      if (paramMap[key].isNil) {
        this[key].free;
      }
    }
  }

  import {
    arg key, param;
    this[key] = param.export;
  }

  importAll {
    arg paramMap;
    paramMap.keysValuesDo {
      arg key, param;
      this.import(key, param);
    };
  }
}
