package codes.rorak.meteorbetterstarscript.command;

import codes.rorak.meteorbetterstarscript.variable.Variable;
import codes.rorak.meteorbetterstarscript.variable.Variables;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * An argument type for variables.
 * Uses the <code>Variable.Var</code> class
 * Checks if the variable exists
 */
public final class VariableArgumentType implements ArgumentType<Variable.Var> {
    private static final DynamicCommandExceptionType NO_SUCH_VARIABLE = new DynamicCommandExceptionType(_name -> Text.literal("Variable with name '" + _name + "' does not exist!"));

    /**
     * Creates a new instance of the class
     * @return the new instance of the class
     */
    @Contract(value=" -> new", pure=true)
    public static @NotNull VariableArgumentType create() { return new VariableArgumentType(); }

    /**
     * Returns the variable from the command argument
     * @param _context The command context
     * @param _name The name of the argument
     * @return The value of that argument as <code>Variable.Var</code>
     */
    public static @NotNull Variable.Var get(@NotNull CommandContext<?> _context, @NotNull String _name) {
        return _context.getArgument(_name, Variable.Var.class);
    }
    @Override
    public @NotNull Variable.Var parse(@NotNull StringReader _reader) throws CommandSyntaxException {
        String $name = _reader.readString();
        Variable.Var $var;
        if (($var = Variables.get().getVar($name))==null) throw NO_SUCH_VARIABLE.create($name);
        return $var;
    }

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> _context, @NotNull SuggestionsBuilder _builder) {
        return CommandSource.suggestMatching(Variables.get().getAll().stream().map(Variable.Var::getName), _builder);
    }
}
