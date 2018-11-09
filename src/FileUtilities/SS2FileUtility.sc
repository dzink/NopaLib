SS2FileUtility {
	*formatTimeStamp {
		arg date = nil;
		date = date.deepCopy;
		if (date.isKindOf(String).not) {
			if (date.isNil) {
				date = Date.gmtime;
			};
			date = date.asSortableString();
		};
		^ date;
	}

	*generateFileName {
		arg folder = "./", prefix = "", descriptor = "", extension = ".sc";
		var fileName;
		if (prefix != "") {
			prefix = prefix ++ "_";
		};
		fileName = (folder.asString() +/+ prefix.asString() ++ descriptor ++ extension);
		^ fileName.standardizePath();
	}

	*generateWildCardFileName {
		arg folder = "./", prefix = "", extension = ".sc";
		^ SS2FileUtility.generateFileName(folder, prefix, "*", extension);
	}

	*writeSafe {
		arg fileName, contents;
		^ SS2FileUtility.write(fileName, contents, true);
	}

	*write {
		arg fileName, contents, safe = false;
		if (File.exists(fileName).not || safe.not) {
			var file = File(fileName, "w");
			file.write(contents);
			file.close;
			^ file;
		} {
			(fileName ++ " already exists, not generating new file").warn;
			^ false;
		};
	}

	*read {
		arg fileName;

	}

	*execute {
		arg fileName;
		if (File.exists(fileName)) {
			^ fileName.load();
		} {
			(fileName ++ " does not exist, not executing.").warn;
			^ false;
		};
	}

	*executeAll {
		arg fileName;
		^ fileName.loadPaths();
	}
}
