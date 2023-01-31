package codes.rorak.meteorbetterstarscript.variable;

import meteordevelopment.starscript.value.Value;
import net.minecraft.text.Style;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static codes.rorak.meteorbetterstarscript.command.VarCommand.*;

/**
 * An enum representing all possible variable types
 */
public enum VariableType {
    /**
     * A boolean variable.
     * Values: true, false
     */
    bool,
    /**
     * A number variable (double)
     */
    number,
    /**
     * A normal string/text variable
     */
    string;

    /**
     * Checks if the value is compatible with the type
     * @param _v the value
     * @return if it is not (true -> it is not; false -> it is)
     */
    public boolean notIn(@NotNull Value _v) {
        return (this != bool || !_v.isBool()) && (this != number || !_v.isNumber()) && (this != string || !_v.isString());
    }

    /**
     * Gets the chat style of the current value
     * @return the style
     */
    @Contract(pure=true)
    public @NotNull Style style() {
        return switch (this) {
            case bool -> BOOL;
            case number -> NUMBER;
            case string -> STRING;
        };
    }

    /**
     * Name of the string type
     */
    public static final String STRING_NAME = "string";
    /**
     * Name of the number type
     */
    public static final String NUMBER_NAME = "number";
    /**
     * Name of the bool type
     */
    public static final String BOOL_NAME = "bool";
}
