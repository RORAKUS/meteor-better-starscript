package codes.rorak.meteorbetterstarscript;

import codes.rorak.meteorbetterstarscript.variable.Variable;
import org.jetbrains.annotations.Contract;

/**
 * Thanks to the stupidly implemented method <code>ArrayList::indexOf</code> I was forced to make this.
 * Just read the code, and then you can use it too.
 */
public final class StringWrapper {
    private String string;

    public StringWrapper(String _string) {
        set(_string);
    }
    public void set(String _string) {
        string = _string;
    }
    public String get() {
        return string;
    }

    @Contract(value="null -> false", pure=true)
    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Override
    public boolean equals(Object _obj) {
        if (_obj instanceof StringWrapper _w) return _w.get().equals(get());
        else if (_obj instanceof String _s) return _s.equals(get());
        else if (_obj instanceof Variable _v) return _v.equals(get());
        return false;
    }
}
