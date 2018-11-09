NopaPresetUtility : Event {
	var <> path;

	*new {
		arg path;
		var p = super.new();
		p.path = path;
		^ p;
	}

	put {
		arg key, preset;
		key = key ? NopaFileUtility.formatTimeStamp();
		if (preset.isKindOf(NopaDictionary)) {
			preset = preset.asPreset();
		};
		if (preset.isKindOf(NopaPreset)) {
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
		var fileName = NopaFileUtility.generateFileName(path, descriptor: key);
		var contents;
		if (this[key].isNil.not) {
			contents = this[key].asCompileString();
			NopaFileUtility.write(fileName, contents);
		} {
			(key.asString() ++ " key not found, cannot save").warn;
		};
		^ this;
	}

	load {
		arg key;
		var fileName = NopaFileUtility.generateFileName(path, descriptor: key);
		this.prLoadPath(fileName);
		^ this;
	}

	prLoadPath {
		arg fileName;
		fileName.loadPaths(action: {
			arg file;
			var key = PathName(file).fileNameWithoutExtension;
			this.put(key.asSymbol, NopaFileUtility.execute(file));
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
		var fileName = NopaFileUtility.generateWildCardFileName(path);
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
		timeStamp = NopaFileUtility.formatTimeStamp(timeStamp);
		path = NopaFileUtility.generateWildCardFileName(path, "migration");
		migrations = NopaFileUtility.executeAll(path);
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
