package codes.rorak.meteorbetterstarscript;

import codes.rorak.meteorbetterstarscript.command.VarCommand;
import codes.rorak.meteorbetterstarscript.variable.Variables;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.systems.commands.Commands;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.misc.MeteorStarscript;
import meteordevelopment.starscript.Starscript;
import meteordevelopment.starscript.value.Value;
import meteordevelopment.starscript.value.ValueMap;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.util.Objects;

public final class BetterStarscript extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final String ID = "better-starscript";
    public static final File FOLDER = new File(MeteorClient.FOLDER, ID);

    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor Addon Template");

        MeteorStarscript.ss.getGlobals().get("meteor").get().getMap().set("get_module_setting",this::getModuleSetting);

        MeteorStarscript.ss.set("var", new ValueMap()
            .set("get", this::varGet)
            .set("set", this::varSet)
        );

        Commands.get().add(new VarCommand());
    }

    private Value varSet(Starscript _ss, int _argCount) {
        if (_argCount != 2) _ss.error("var.set() requires 2 arguments, got %d", _argCount);

        Value $newValue = _ss.pop();
        String $name = _ss.popString("First argument to var.set() needs to be a string.");

        Boolean $result = Variables.get().set($name, $newValue);
        if ($result==null) _ss.error("Variable with name " + $name + " does not exist!");
        assert $result!=null;
        if (!$result) _ss.error("Value " + $newValue + " is not compatible with type " + Objects.requireNonNull(Variables.get().getVar($name)).getType());

        return Value.string("");
    }

    private Value varGet(Starscript _ss, int _argCount) {
        if (_argCount != 1) _ss.error("var.get() requires 1 argument, got %d", _argCount);

        String $name = _ss.popString("First argument to var.get() needs to be a string.");
        Value $value = Variables.get().get($name);
        if ($value == null) _ss.error("Variable with name " + $name + " does not exist!");
        return $value;
    }

    private @NotNull Value getModuleSetting(Starscript _ss, int _argCount) {
        if (_argCount != 2) _ss.error("meteor.get_module_setting() requires 2 arguments, got %d.", _argCount);

        String $settingName = _ss.popString("Second argument to meteor.get_module_setting() needs to be a string.");
        String $moduleName = _ss.popString("First argument to meteor.get_module_setting() needs to be a string.");
        Module $module = Modules.get().get($moduleName);
        if ($module == null) _ss.error("%s is not a valid module.", $moduleName);
        assert $module != null;
        Setting<?> $setting = $module.settings.get($settingName);
        if ($setting == null) _ss.error("%s is not a valid setting of module %s.", $settingName, $module.title);
        assert  $setting != null;
        return Value.string($setting.get().toString());
    }

    @Override
    public @NotNull String getPackage() {
        return "com.example.addon";
    }

}
