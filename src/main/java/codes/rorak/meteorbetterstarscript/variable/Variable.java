package codes.rorak.meteorbetterstarscript.variable;

import codes.rorak.meteorbetterstarscript.BetterStarscriptException;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;
import meteordevelopment.starscript.value.Value;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The class that is used to represent the variable on the database level.
 * DO NOT MANIPULATE WITH THIS OBJECT -> use the <code>get()</code> method pls
 */
@Document(collection = "variables", schemaVersion = "1.0")
@SuppressWarnings("DeprecatedIsStillUsed")
public final class Variable {
    /**
     * A container for variables
     */
    public class Var {
        private final String name;
        private final VariableType type;
        private final Value value;

        /**
         * Constructor that creates the <code>Var</code> object using the values of its <code>Variable</code> parent
         */
        public Var() {
            value = value();
            type = type();
            name = Variable.this.name;
        }

        /**
         * Returns the name of the variable
         * @return the name of the variable
         */
        public @NotNull String getName() { return name; }

        /**
         * Returns the type of the variable
         * @return the type of the variable as <code>VariableType</code>
         */
        public @NotNull VariableType getType() { return type; }

        /**
         * Returns the value of the variable
         * @return The value of the variable as <code>Variable</code>
         */
        public @NotNull Value getValue() { return value; }
    }
    @Id
    private String name;
    private String type;
    private String value;

    /**
     * Constructor for creating the plain Variable object
     * Used by database.
     * DO NOT USE
     * @deprecated please don't use this
     * @param _name The name as STRING
     * @param _type The type as STRING
     * @param _value The value as STRING
     */
    @Contract(pure=true)
    @Deprecated
    public Variable(@NotNull String _name, @NotNull String _type, @NotNull String _value) {
        setName(_name);
        setType(_type);
        setValue(_value);
    }

    /**
     * An empty constructor for no reason.
     * Maybe I'm scared to delete it because I have no idea how the database works and if it uses this lol
     * @deprecated well don't use this too pls
     */
    @Contract(pure=true)
    @SuppressWarnings("unused")
    @Deprecated
    public Variable() {}

    /**
     * The one and only constructor for the variable object.
     * @param _name The name of the variable
     * @param _type The type of the variable
     * @param _value The value of the variable
     * @throws BetterStarscriptException when the value isn't compatible with the type
     */
    public Variable(@NotNull String _name, @NotNull VariableType _type, @NotNull Value _value) throws BetterStarscriptException {
        setName(_name);
        setType(_type.toString());
        if (!value(_value)) throw new BetterStarscriptException();
    }

    /**
     * Sets the value of the Variable object
     * @param _value the value to set as a <code>Value</code> object
     * @return <code>true</code> if it was successful, <code>false</code> if the value isn't compatible with the type
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean value(@NotNull Value _value) {
        if (type().notIn(_value)) return false;
        setValue(_value.toString());
        return true;
    }

    /**
     * Returns the type of the variable
     * @return the type bruh
     */
    public @NotNull VariableType type() { return VariableType.valueOf(getType()); }
    /**
     * Returns the value of the variable
     * @return the value as <code>Value</code>. Can be only String, Bool or Number. If it's null, something went hella wrong... :/
     */
    public @NotNull Value value() {
        return switch (type) {
            case VariableType.STRING_NAME -> Value.string(value);
            case VariableType.BOOL_NAME -> Value.bool(Boolean.parseBoolean(value));
            case VariableType.NUMBER_NAME -> Value.number(Double.parseDouble(value));
            default -> Value.null_();
        };
    }

    /**
     * Returns the name of the variable
     * @return the name of the variable
     */
    public @NotNull String getName() {
        return name;
    }

    /**
     * Used for the database
     * @deprecated please don't use, thx
     * @param _name the name
     */
    @Deprecated
    public void setName(@NotNull String _name) {
        name = _name;
    }
    /**
     * Used for the database
     * @deprecated please don't use, thx
     * @return the type AS A FRICKING STRING BROO
     */
    @Deprecated
    public @NotNull String getType() {
        return type;
    }
    /**
     * Used for the database
     * @deprecated please don't use, thx
     * @param _type the type -- AS A STRING
     */
    @Deprecated
    public void setType(@NotNull String _type) {
        type = _type;
    }
    /**
     * Used for the database
     * @deprecated please don't use, thx
     * @return the value - how? - yeah you guessed it ---- AS A STRIIIIIIING
     */
    @SuppressWarnings("unused")
    @Deprecated
    public @NotNull String getValue() {
        return value;
    }
    /**
     * Used for the database
     * @deprecated please don't use, thx
     * @param _value don't look at this, move on and forget pls
     */
    @Deprecated
    public void setValue(@NotNull String _value) {
        value = _value;
    }

    /**
     * Returns a new <code>Var</code> object, that can be used to get info about the variable. Yeeey!
     * @return The Var object
     */
    public @NotNull Var get() { return new Var(); }

    /**
     * Checks if it equals to another object.
     * @param _obj the object. You can use either a Variable, or a String object
     * @return if it equals
     */
    @Contract(value="null -> false", pure=true)
    @Override
    public boolean equals(@Nullable Object _obj) {
        if (_obj instanceof Variable _v) return getName().equals(_v.getName());
        else if (_obj instanceof String _s) return getName().equals(_s);
        return false;
    }
}
