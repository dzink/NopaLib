SS2ParamMapPresetDictionary : Event {
	var <> path;

	*new {
		arg path;
		var p = super.new();
		p.path = path;
		^ p;
	}

	put {
		arg key, preset;
		key = key ? SS2FileUtility.formatTimeStamp();
		if (preset.isKindOf(SS2ParamMap)) {
			preset = preset.asPreset();
		};
		if (preset.isKindOf(SS2ParamMapPreset)) {
			super.put(key, preset);
		} {
			Exception(preset.class.asString() ++ " cannot set as preset").throw;
		};
		^ this;
	}

	get {
		arg key;
		^ this.at(key);
	}

	save {
		arg key;
		var fileName = SS2FileUtility.generateFileName(path, descriptor: key);
		var contents;
		if (this[key].isNil.not) {
			contents = this[key].asCompileString();
			SS2FileUtility.write(fileName, contents);
		} {
			(key.asString() ++ " key not found, cannot save").warn;
		};
		^ this;
	}

	load {
		arg key;
		var fileName = SS2FileUtility.generateFileName(path, descriptor: key);
		this.prLoadPath(fileName);
		^ this;
	}

	prLoadPath {
		arg fileName;
		fileName.loadPaths(action: {
			arg file;
			var key = PathName(file).fileNameWithoutExtension;
			this.put(key.asSymbol, SS2FileUtility.execute(file));
		});
	}

	saveAll {
		this.keysDo {
			arg key;
			this.save(key, path);
		};
		^ this;
	}

	loadAll {
		var fileName = SS2FileUtility.generateWildCardFileName(path);
		this.prLoadPath(fileName);
		^ this;
	}

	migrateUp {
		arg path, timeStamp = nil;
		^ this.migrate(path, timeStamp, \up);
	}

	migrateDown {
		arg path, timeStamp = nil;
		^ this.migrate(path, timeStamp, \down);
	}

	migrateTo {
		arg path, timeStamp = nil;
		if (timeStamp.isNil.not) {
			^ this.migrate(path, timeStamp, \to);
		};
	}

	/**
	 * Run all relevant migrations.
	 */
	migrate {
		arg path, timeStamp = nil, direction = \up;
		var migrations, functions;
		timeStamp = SS2FileUtility.formatTimeStamp(timeStamp);
		path = SS2FileUtility.generateWildCardFileName(path, "migration");
		migrations = SS2FileUtility.executeAll(path);
		migrations.sortBy(\timeStamp);
		this.keysValuesDo {
			arg key, preset;
			preset.prMigrate(migrations, timeStamp, direction);
			this[key] = preset;
		};
		^ this;
	}

	putAndSave {
		arg key, preset;
		this.put(key, preset);
		this.save(key);
		^ this;
	}

}
