# Meteor Better Starscript

A very cool meteor addon adding some very cool features to starscript.

## Variables
The biggest feature of this addon is a variable support. These can be used in starscript, or in commands. Variables are stored in a json file called `variables.json` in `.minecraft/meteor-client/better-starscript`. They are global, so they are the same on every server and every profile.

## New methods
* `meteor.get_module_setting(module, setting)` - returns a setting value of module as a string
* `var.get(name)` - returns a value of variable name
* `var.set(name, value)` - sets a value of already existing variable

## New command/s
* `.var`/`.variable`
  * `create <name> <type> <value>` - creates a new variable.
  * `remove <name>` - removes a variable
  * `get <name>` - writes all variable info to chat
  * `set <name> <value>` - works like `var.set()`
  * `list` - lists all existing variables and their values
