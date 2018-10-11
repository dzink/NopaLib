SS2ParamMapPreset : IdentityDictionary {
	var < migrateTimeStamp;
	const < migrationFileTemplate =
"// @CHANGEME what should this migration do?

Event[
	// @CHANGEME what should this migration do?
	\\timeStamp -> \"@timeStamp\",
	\\up -> {
		arg preset;

		\"@CHANGEME I performed an action\"
	},
	// @CHANGEME how should this migration be undone?
	\\down -> {
		arg preset;

		\"@CHANGEME I rolled back an action\"
	},
];";

	*new {
		var p = super.new();
		p.migrateTimeStamp = nil;
		^ p;
	}

	*newFrom {
		arg map, timeStamp = nil, args = nil;
		var p;
		if (map.isKindOf(SS2ParamMap)) {
			p = super.new();
			p.import(map, args);
		} {
			p = super.newFrom(map);
		};
		p.migrateTimeStamp = timeStamp;
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

	migrateTimeStamp_ {
		arg timeStamp;
		migrateTimeStamp = SS2FileUtility.formatTimeStamp(timeStamp);
		^ this;
	}

	/**
	 * Helper function to generate a migration file.
	 * @param String path
	 *   A folder to generate the file in.
	 * @param String timeStamp
	 *   A timeStamp to use. Default will be the current time
	 *	 Date.asSortableString()
	 */
	*generateMigration {
		arg path, descriptor = nil;
		var fileContents, fileName, file, timeStamp;
		timeStamp = SS2FileUtility.formatTimeStamp();
		descriptor = descriptor.defaultWhenNil(timeStamp).asString();
		fileName = SS2FileUtility.generateFileName(path, "migration", descriptor);
		fileContents = migrationFileTemplate.replace("@timeStamp", timeStamp);
		^ SS2FileUtility.writeSafe(fileName, fileContents);
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

		this.prMigrate(migrations, timeStamp, direction);
		^ this;
	}

	prMigrate {
		arg migrations, timeStamp, direction = \up;
		if (migrateTimeStamp <(timeStamp)) {
			// Migrating up
			if (direction != \down) {
				migrations = this.prFilterMigrations(migrations, migrateTimeStamp, timeStamp);
				this.prExecuteMigrations(migrations, \up);
			};
		} {
			// Migrating down
			if (direction != \up) {
				migrations = this.prFilterMigrations(migrations, timeStamp, migrateTimeStamp);
				this.prExecuteMigrations(migrations.reverse(), \down);
			};
		};
		this.migrateTimeStamp = timeStamp;
		^ this;
	}

	prExecuteMigrations {
		arg migrations, key, minStamp, maxStamp;
		var tempPreset = this.deepCopy;
		var prefix = if (key == \up, {"Migrate: "}, {"Roll Back: "});

		migrations.do {
			arg migration;
			var result;
			result = migration[key].(tempPreset);
			if (result.isKindOf(String)) {
				(prefix ++ result).postln;
			};
		};
		this.clear;
		tempPreset.keysValuesDo {
			arg key, value;
			this[key] = value;
		};
	}

	prFilterMigrations {
		arg migrations, minStamp, maxStamp;
		var selectedMigrations = migrations.select {
			arg migration;
			((migration.timeStamp > minStamp) && (migration.timeStamp <= maxStamp));
		};
		^ selectedMigrations;
	}

	/**
	 * Rename a given key to a new key.
	 */
	renameKeyAt {
		arg oldKey, newKey;
		if (this[oldKey].isNil.not) {
			this[newKey] = this[oldKey];
			this.removeAt(oldKey);
		};
		^ this;
	}

	/**
	 * Replace a symbol or value in this preset at a given key.
	 * @param key
	 *   The key in the preset to replace at. If the key is not found, nothing
	 *   happens.
	 * @param replacementDictionary
	 *   A set of find/replace values. The key is the find, the value is the
	 *   replace. For example:
	 *     p = SS2ParamMapPreset[\a -> 1, \b -> \sine];
	 *     p.replaceValueAt(\a, IdentityDictionary[1 -> 2]);
	 *     p.replaceValueAt(\b, IdentityDictionary[\sine -> \saw]);
	 *   will return ParamMapPreset[ (a -> 2), (b -> saw) ].
	 *   If the current value is not in the replacementDictionary, nothing
	 *	 happens.
	 */
	replaceValueAt {
		arg key, replacementDictionary = IdentityDictionary[];
		if (this[key].isNil.not) {
			if (replacementDictionary[this[key]].isNil.not) {
				this[key] = replacementDictionary[this[key]];
			};
		};
		^ this;
	}

	/**
	 * Transform a value in this preset at a given key.
	 */
	transformValueAt {
		arg key, func;
		if (this[key].isNil.not) {
			this[key] = func.(this[key], key);
		};
		^ this;
	}

	/**
	 * Add a default value at a given key.
	 */
	addDefaultAt {
		arg key, value;
		if (this[key].isNil) {
			this[key] = value;
		};
		^ this;
	}

	storeOn {
		arg stream;
		var d;
		stream <<< this.class << '[';
		this.storeItemsOn(stream);
		stream << '].migrateTimeStamp_(' <<< migrateTimeStamp << ')';
	}
}
