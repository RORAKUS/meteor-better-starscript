package codes.rorak.meteorbetterstarscript.variable;

import codes.rorak.meteorbetterstarscript.BetterStarscript;
import codes.rorak.meteorbetterstarscript.BetterStarscriptException;
import codes.rorak.meteorbetterstarscript.StringWrapper;
import io.jsondb.JsonDBTemplate;
import meteordevelopment.starscript.value.Value;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

/**
 * The main class used to manipulate with variables
 * For start, acquire the instance using the <code>get()</code> method
 */
public final class Variables {
    private static final Variables INSTANCE = new Variables();
    private static final Class<Variable> V = Variable.class;

    /**
     * Returns an instance of this object
     * @return well, the instance maybe?
     */
    @Contract(pure=true)
    public static @NotNull Variables get() { return INSTANCE; }

    private final JsonDBTemplate db;

    private Variables() {
        db = new JsonDBTemplate(BetterStarscript.FOLDER.getAbsolutePath(), getClass().getPackageName());
        db.getCollection(Variable.class);
    }

    /**
     * Returns the value of a variable
     * @param _name The name of the variable
     * @return The value of that variable, or null if the variable does not exist
     */
    public @Nullable Value get(@NotNull String _name) {
        return valueOrNull(getv(_name), Variable::value);
    }

    /**
     * Sets the value of a variable. The variable must exist to do this
     * @param _name the name of the variable
     * @param _value new value for that variable
     * @return true if everything is ok, false if the value has wrong type and null if the variable does not exist. Note that it returns a <code>Boolean</code> object
     */
    public @Nullable Boolean set(@NotNull String _name, @NotNull Value _value) {
        if (!exists(_name)) return null;
        Variable $var = getv(_name);
        assert $var != null;
        if (!$var.value(_value)) return false;
        db.save($var, V);
        return true;
    }

    /**
     * Sets the value of a variable without checking anything.
     * @param _name the name of the variable
     * @param _value new value for that variable
     * @throws RuntimeException when the value has wrong type or when the variable doesn't exist
     */
    public void noCheckSet(@NotNull String _name, @NotNull Value _value) {
        try {
            if (!Boolean.TRUE.equals(set(_name, _value))) throw new BetterStarscriptException();
        }
        catch (BetterStarscriptException _ex) {
            throw new RuntimeException();
        }
    }

    /**
     * Checks if variable with name exists
     * @param _name the name to check
     * @return if it exists or not
     */
    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean exists(@NotNull String _name) {
        return col().contains(new StringWrapper(_name));
    }

    /**
     * Creates a new variable
     * @param _name The name of the new variable
     * @param _type The type of the new variable
     * @param _value The value of the new variable
     * @return true if everything was ok, false if the variable already exists
     * @throws BetterStarscriptException when the value doesn't match with the type
     */
    public boolean create(@NotNull String _name, @NotNull VariableType _type, @NotNull Value _value) throws BetterStarscriptException {
        if (exists(_name)) return false;
        Variable $var = new Variable(_name, _type, _value);
        db.insert($var);
        return true;
    }

    /**
     * Creates a new variable without checking anything
     * @param _name The name of the new variable
     * @param _type The type of the new variable
     * @param _value The value of the new variable
     * @return A <code>Var</code> object of the new variable that was just created
     * @throws RuntimeException when the variable already exists or when the type doesn't match the value
     */
    public @NotNull Variable.Var noCheckCreate(@NotNull String _name, @NotNull VariableType _type, @NotNull Value _value) {
        try {
            if (!create(_name, _type, _value)) throw new BetterStarscriptException();
            return Objects.requireNonNull(getVar(_name));
        }
        catch (BetterStarscriptException _ex) {
            throw new RuntimeException();
        }
    }

    /**
     * Removes a variable
     * @param _name The name of that variable
     * @return true if everything was ok, false if the variable doesn't exist
     */
    public boolean remove(@NotNull String _name) {
        if (!exists(_name)) return false;
        Variable $var = getv(_name);
        db.remove($var, V);
        return true;
    }

    /**
     * Removes a variable without checking anything
     * @param _name The name of that variable
     * @throws RuntimeException when the variable does not exist
     */
    public void noCheckRemove(@NotNull String _name) {
        try {
            if (!remove(_name)) throw new BetterStarscriptException();
        }
        catch (BetterStarscriptException _ex) {
            throw new RuntimeException();
        }
    }

    /**
     * Gets the container object of a variable
     * @param _name The name of the variable to get the object of
     * @return the container object or null if the variable does not exist
     */
    public @Nullable Variable.Var getVar(@NotNull String _name) {
        return valueOrNull(getv(_name), Variable::get);
    }
    /**
     * Gets the container object of a variable without checks
     * @param _name The name of the variable to get the object of
     * @return the container object. cannot return null :)
     * @throws RuntimeException when the variable does not exist
     */
    public @NotNull Variable.Var noCheckGetVar(@NotNull String _name) {
        try {
            Variable.Var $val;
            if (($val = getVar(_name))==null) throw new BetterStarscriptException();
            return $val;
        }
        catch (BetterStarscriptException _ex) {
            throw new RuntimeException();
        }
    }

    /**
     * Returns a list of all variables
     * @return a list of all variables as containers
     */
    public @NotNull List<Variable.Var> getAll() {
        return col().stream().map(Variable::get).toList();
    }

    private @NotNull List<Variable> col() { return db.getCollection(V); }
    @SuppressWarnings({"SuspiciousMethodCalls", "SpellCheckingInspection"})
    private @Nullable Variable getv(@NotNull String _name) {
        if (!exists(_name)) return null;
        return col().get(col().indexOf(new StringWrapper(_name)));
    }

    @Contract("null, _ -> null")
    private static <T, R> @Nullable R valueOrNull(@Nullable T _object, @NotNull Function<T, R> _convertor) {
        return _object==null ? null : _convertor.apply(_object);
    }
}
