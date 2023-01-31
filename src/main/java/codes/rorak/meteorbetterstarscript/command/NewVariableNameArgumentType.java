package codes.rorak.meteorbetterstarscript.command;

import codes.rorak.meteorbetterstarscript.variable.Variables;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * An argument type for new variable names.
 * Checks if variable with that named doesn't already exist.
 */
public final class NewVariableNameArgumentType implements ArgumentType<String> {
    private static final DynamicCommandExceptionType VARIABLE_ALREADY_EXISTS = new DynamicCommandExceptionType(_name -> Text.literal("Variable with name '" + _name + "' already exists!"));

    /**
     * Returns a new instance of the class.
     * @return The new instance of the class.
     */
    @Contract(value=" -> new", pure=true)
    public static @NotNull NewVariableNameArgumentType create() { return new NewVariableNameArgumentType(); }
    @Override
    public @NotNull String parse(@NotNull StringReader _reader) throws CommandSyntaxException {
        String $name = _reader.readString();
        if (Variables.get().exists($name)) throw VARIABLE_ALREADY_EXISTS.create($name);
        return $name;
    }

    /**
     * Returns a string that was passed as a parameter to the command.
     * @param _context The command context
     * @param _name The name of that parameter
     * @return The value of that parameter
     */
    public static @NotNull String get(@NotNull CommandContext<?> _context, @NotNull String _name) {
        return _context.getArgument(_name, String.class);
    }
}
