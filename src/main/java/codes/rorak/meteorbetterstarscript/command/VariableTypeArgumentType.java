package codes.rorak.meteorbetterstarscript.command;

import codes.rorak.meteorbetterstarscript.variable.VariableType;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


/**
 * An argument type for variable type.
 * Uses the <code>VariableType</code> enum
 * Checks if the type is valid
 */
public final class VariableTypeArgumentType implements ArgumentType<VariableType> {
    private static final DynamicCommandExceptionType NO_SUCH_TYPE = new DynamicCommandExceptionType(_name -> Text.literal("Type " + _name + " doesn't exist."));
    /**
     * Creates a new instance of the class
     * @return the new instance of the class
     */
    @Contract(value=" -> new", pure=true)
    public static @NotNull VariableTypeArgumentType create() { return new VariableTypeArgumentType(); }
    /**
     * Returns the variable type from the command argument
     * @param _context The command context
     * @param _name The name of the argument
     * @return The value of that argument as <code>VariableType</code>
     */
    public static @NotNull VariableType get(@NotNull CommandContext<?> _context, @NotNull String _name) {
        return _context.getArgument(_name, VariableType.class);
    }

    @Override
    public @NotNull VariableType parse(@NotNull StringReader _reader) throws CommandSyntaxException {
        String $typeName = _reader.readString();

        VariableType $type;
        try {
            $type = VariableType.valueOf($typeName);
        }
        catch (IllegalArgumentException _ex) {
            throw NO_SUCH_TYPE.create($typeName);
        }

        return $type;
    }

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> _context, @NotNull SuggestionsBuilder _builder) {
        return CommandSource.suggestMatching(Arrays.stream(VariableType.values()).map(VariableType::toString), _builder);
    }

    @Override
    public @NotNull Collection<String> getExamples() {
        return Arrays.stream(VariableType.values()).map(VariableType::toString).collect(Collectors.toList());
    }
}
