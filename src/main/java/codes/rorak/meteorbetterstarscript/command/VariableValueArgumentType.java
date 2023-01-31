package codes.rorak.meteorbetterstarscript.command;

import codes.rorak.meteorbetterstarscript.BetterStarscriptException;
import codes.rorak.meteorbetterstarscript.variable.Variable;
import codes.rorak.meteorbetterstarscript.variable.VariableType;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import meteordevelopment.meteorclient.utils.misc.MeteorStarscript;
import meteordevelopment.starscript.Script;
import meteordevelopment.starscript.value.Value;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * An argument type for a variable value
 * Checks if the value is the same type as the variable
 */
public final class VariableValueArgumentType implements ArgumentType<String> {
    private static final Dynamic2CommandExceptionType VALUE_NOT_COMPATIBLE = new Dynamic2CommandExceptionType((_v, _t) -> Text.literal("Value " + _v + " is not compatible with type " + _t + "!"));

    /**
     * Creates a new instance of the class
     * @return the new instance of the class
     */
    @Contract(value=" -> new", pure=true)
    public static @NotNull VariableValueArgumentType create() { return new VariableValueArgumentType(); }

    /**
     * Returns a value from the command argument. Checks if the value is compatible with the parameter containing the type.
     * Also compiles the message into starscript and runs it, then gets the output
     * @param _context The command context
     * @param _valueName The name of the parameter with the value
     * @param _typeName The name of the parameter with the type
     * @return The value of that parameter
     * @throws CommandSyntaxException when value is not compatible with the type -> commands auto handle it
     * @throws BetterStarscriptException when the meteor starscript fails.
     */
    public static @NotNull Value getWithType(@NotNull CommandContext<?> _context, @NotNull String _valueName, @NotNull String _typeName) throws CommandSyntaxException, BetterStarscriptException {
        VariableType $type = _context.getArgument(_typeName, VariableType.class);
        return get(_context, _valueName, $type);
    }
    /**
     * Returns a value from the command argument. Checks if the value is compatible with the parameter containing the variable name.
     * Also compiles the message into starscript and runs it, then gets the output
     * @param _context The command context
     * @param _valueName The name of the parameter with the value
     * @param _varName The name of the parameter with the variable name
     * @return The value of that parameter
     * @throws CommandSyntaxException when value is not compatible with the type -> commands auto handle it
     * @throws BetterStarscriptException when the meteor starscript fails.
     */
    public static @NotNull Value getWithVar(@NotNull CommandContext<?> _context, @NotNull String _valueName, @NotNull String _varName) throws CommandSyntaxException, BetterStarscriptException {
        VariableType $type = _context.getArgument(_varName, Variable.Var.class).getType();
        return get(_context, _valueName, $type);
    }

    private static @NotNull Value get(@NotNull CommandContext<?> _context, @NotNull String _valueName, @NotNull VariableType _type) throws CommandSyntaxException, BetterStarscriptException {
        String $str = _context.getArgument(_valueName, String.class);
        Script $script = MeteorStarscript.compile($str);
        if ($script==null) throw new BetterStarscriptException();
        String $finalMsg = MeteorStarscript.run($script);
        Value $value = valueFromString($finalMsg);
        if (_type.notIn($value)) {
            if (_type==VariableType.string) return Value.string($value.toString());
            throw VALUE_NOT_COMPATIBLE.create($value.toString(), _type.toString());
        }
        return $value;
    }

    @Override
    public @NotNull String parse(@NotNull StringReader _reader) {
        String $str = _reader.getRemaining();
        _reader.setCursor(_reader.getTotalLength());
        return $str;
    }

    private static @NotNull Value valueFromString(@NotNull String _str) {
        if (_str.matches("(true|false)")) return Value.bool(Boolean.parseBoolean(_str));
        try {
            return Value.number(Double.parseDouble(_str));
        }
        catch (NumberFormatException _ex) {
            return Value.string(_str);
        }
    }
}
