# NopaLib
### Normalized Parameter Transformation Library
This library bridges the metaphorical gap between a human-semantic "low" to "high" range and more algorithmically useful objects.

NopaLib is based on the simple idea of a knob, which travels from a minimum value to a maximum value. Since this is a concept most musicians are familiar with, this "normalized" value is the base value of all NopaLib parameters. This normalized value is then used to get and set real values, sync a number of objects together, and display values in a human readable manner.

The "Nopa" name comes from the term Normalized Parameter.

## A volume knob example
It's probably easier to describe what NopaLib does with an example. Imagine a familiar volume knob. This knob goes from "off" to "maximum."

- NopaLib knows how to handle decibels, which are a human-readable way of expressing volume. The subclass `SS2ParamDb` accepts decibels as number values as well as terms like `\off`, and `\unity`.
- NopaLib can optionally generate a widget that can handle this range.
- NopaLib can convert those decibels into a convenient human readable value - instead of outputting `6.52421855251`, it knows roughly how much specificity decibels require, as well as how to format them: `+6.5dB`. Values can also be assigned via this format. *Note: I think that units and syntax are a very underrated hint to how an interface works.*
- NopaLib allows you to decide where the "sweet spot" on the knob is. Some people (barbarians) prefer linear to exponential curves for volume knobs, so both options are available, as well as curves that bias the parameter towards either the high or low end.
- When dealing with volumes in SynthDefs, decibels are often not as useful as amps. NopaLib knows how to optionally convert everything that it sends to a Node into amps.
- NopaLib can be assigned targets that will receive any new values as they come in. Widgets, MIDI connections, Synth Nodes, and other objects can all stay in sync once they're assigned to a NopaLib parameter.
- Speaking of MIDI, NopaLib will also convert MIDI input using the same low-high metaphor; `0` is "low" while `127` is "high."
- Sometimes there's a "special" function that should occur every time a parameter is touched. NopaLib can assign that function as a target as well.

### Groups of parameters
NopaLib also works well with knobs en masse. Imagine a whole group of knobs, all of which control different parameters (they can be mixed between many kinds of parameters, they don't all need to be volume).

- NopaLib can keep all your parameters organized with a SS2ParamMap. You can assign a parameter to `~myCoolDictionary.volume` and it will be easily available anywhere in the SuperCollider environment without cluttering up your environment variables.
- NopaLib can generate a GUI for all knobs with a single command.
- NopaLib can connect all the parameters (or a subset) to a single Node, so that you don't need to assign each individual target.
- NopaLib can store and retrieve the values of all the knobs to create a preset. It can also save these presets to disk.

## NopaParams
NopaLib Params represent a parameter to a Synth, or a parameter used in a Pbind, or any other value that's used to control SuperCollider behavior.

The core of a NopaLibParam is its normalized value which is always a range from [0..1] inclusive. 0 represents the lower end of a range, while 1 represents the upper. There will usually be valid values in between, but there could be edge cases with only two extreme values.

### NopeParam flavors
There are a few kinds of NopaParams which all have sensible defaults:
- SS2SS2ParamContinuous - a generalized param with a continuous range defined by a minimum, maximum, and optional curve, extremely typical.
- SS2ParamMirror - like SS2ParamContinuous, only it ranges from [-n..0..n], with a curve bias in the middle or on both ends.
- SS2ParamDb - a param for decibels.
- SS2Semitone - a param for semitones.
- SS2Hz - a param for Hz.
- SS2ParamInf - a param where one or both ends are "infinity."
- SS2ParamList - a param where instead of a continuous range, there are multiple symbols passed in which are enumerated to values in order (`[\a, \b, \c, \d]` would map to 1, 2, 3, 4).
- SS2ParamListValues - like a SS2ParamList only instead of enumerating the values, they are specifically assigned.

```~width = SS2ParamContinuous(0, 1, 2); // Bias towards 0.
~shape = SS2ParamContinuous(0, 1, -2); // Bias towards 1.
~volume = SS2ParamDb(\off, \unity);
~filter = SS2ParamHz(100, 11000);
~algorithm = SS2ParamList([\square, \sine, \triangle,]);
~speedCoarse = SS2SS2ParamListValues([\slow, \fast], [0.1, 10]);
```

### Conversion strategies
NopaParams have conversion strategies for certain scenarios (mostly when sending values to a Node). Each flavor has its own strategies, but here are some examples:

```~volume = SS2ParamDb(\off, \unity); // Dbs are more understandable...
~volume.convertToAmps = true; // But the SynthDef uses amps.

~note = SS2ParamSemitone(24, 48); // Semitones make more sense sometimes...
~note.convertToRatio = true; // But the SynthDef expects a midiratio.
```

## Readables
Readables are strategies to turn NopaParams into something human-readable. Different NopaParam flavors will have different defaults, but these can be overridden with a few special readables:

- SS2SS2ParamDisplayPercent: Represents the param as a percentage between the minimum and maximum values.
- SS2SS2ParamDisplayNormalized: Display the normalized range, from [0..1]. This can be scaled and can also have units appended.

```~width = SS2ParamContinuous(0, 1, 2);
~width.displayStrategy = SS2SS2ParamDisplayNormalized(10); // Displays [0..10];
