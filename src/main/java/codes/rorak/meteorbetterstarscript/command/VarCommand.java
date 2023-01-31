package codes.rorak.meteorbetterstarscript.command;

import codes.rorak.meteorbetterstarscript.BetterStarscriptException;
import codes.rorak.meteorbetterstarscript.variable.Variable;
import codes.rorak.meteorbetterstarscript.variable.VariableType;
import codes.rorak.meteorbetterstarscript.variable.Variables;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import meteordevelopment.meteorclient.systems.commands.Command;
import meteordevelopment.starscript.value.Value;
import net.minecraft.command.CommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import static codes.rorak.meteorbetterstarscript.command.VarCommand.LogType.*;
import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

/**
 * Command for manipulating with variables.
 * <br>
 * <strong>Name:</strong> var
 * <br>
 * <strong>Description:</strong> Manipulation with variables
 * <br>
 * <strong>Aliases:</strong> variable
 */
@SuppressWarnings("SameReturnValue")
public final class VarCommand extends Command {

    /**
     * Creates a new command
     */
    public VarCommand() {
        super("var", "Manipulation with variables", "variable");
    }

    @Override
    public void build(@NotNull LiteralArgumentBuilder<CommandSource> _b) {
        _b.then(
            literal("create").then(
            argument("name", NewVariableNameArgumentType.create()).then(
            argument("type", VariableTypeArgumentType.create()).then(
            argument("value", VariableValueArgumentType.create())
            .executes(this::create))))
        );
        _b.then(
            literal("remove").then(
            argument("name", VariableArgumentType.create())
            .executes(this::remove))
        );
        _b.then(
            literal("get").then(
            argument("name", VariableArgumentType.create())
            .executes(this::get))
        );
        _b.then(
            literal("set").then(
            argument("name", VariableArgumentType.create()).then(
            argument("value", VariableValueArgumentType.create())
            .executes(this::set)))
        );
        _b.then(
            literal("list")
            .executes(this::list)
        );
    }

    private int create(CommandContext<?> _c) throws CommandSyntaxException {
        String $name = NewVariableNameArgumentType.get(_c, "name");
        VariableType $type = VariableTypeArgumentType.get(_c, "type");
        Value $value;
        try {
            $value = VariableValueArgumentType.getWithType(_c, "value", "type");
        } catch (BetterStarscriptException _ex) {
            return SINGLE_SUCCESS;
        }

        Variable.Var $var = Variables.get().noCheckCreate($name, $type, $value);

        log(CREATE, $var);
        return SINGLE_SUCCESS;
    }
    private int remove(CommandContext<?> _c) {
        Variable.Var $var = VariableArgumentType.get(_c, "name");

        Variables.get().noCheckRemove($var.getName());

        log(REMOVE, $var);
        return SINGLE_SUCCESS;
    }
    private int get(CommandContext<?> _c) {
        Variable.Var $var = VariableArgumentType.get(_c, "name");
        log(GET, $var);
        return SINGLE_SUCCESS;
    }
    private int set(CommandContext<?> _c) throws CommandSyntaxException {
        Variable.Var $var = VariableArgumentType.get(_c, "name");
        Value $value;
        try {
            $value = VariableValueArgumentType.getWithVar(_c, "value", "name");
        } catch (BetterStarscriptException _ex) {
            return SINGLE_SUCCESS;
        }

        Variables.get().noCheckSet($var.getName(), $value);

        log(SET, Variables.get().noCheckGetVar($var.getName()));
        return SINGLE_SUCCESS;
    }
    private int list(@SuppressWarnings("unused") CommandContext<?> _c) {
        if (Variables.get().getAll().size()==0) {
            log(LIST_NONE, null);
            return SINGLE_SUCCESS;
        }
        log(LIST_BORDER_UP, null);
        for (Variable.Var $var : Variables.get().getAll()) {
            log(GET, $var);
        }
        log(LIST_BORDER_DOWN, null);
        return SINGLE_SUCCESS;
    }



    private static final Style TYPE = Style.EMPTY.withFormatting(Formatting.RED).withBold(true);
    private static final Style NAME = Style.EMPTY.withFormatting(Formatting.YELLOW);
    /**
     * The style of number entity in chat
     */
    public static final Style NUMBER = Style.EMPTY.withColor(6854587);
    /**
     * The style of string entity in chat
     */
    public static final Style STRING = Style.EMPTY.withColor(1558179);
    /**
     * The style of bool entity in chat
     */
    public static final Style BOOL = Style.EMPTY.withFormatting(Formatting.GOLD);
    private static final Style OPERATOR = Style.EMPTY.withColor(15132410);
    private static final Style EQ_STYLE = Style.EMPTY.withFormatting(Formatting.LIGHT_PURPLE);
    private static final Style HEADER_STYLE = EQ_STYLE.withBold(true);
    private final MutableText HEADER =
        Text.literal("==========").setStyle(EQ_STYLE).append(
            Text.literal(" VARIABLES ").setStyle(HEADER_STYLE)).append(
            Text.literal("==========").setStyle(EQ_STYLE));
    private final MutableText FOOTER = Text.literal("===============================").setStyle(EQ_STYLE);
    private void log(@NotNull LogType _type, Variable.Var _v) {
        MutableText $TYPE = Text.literal("");
        MutableText $NAME = Text.literal("");
        MutableText $VALUE = Text.literal("");
        if (_v != null) {
            $TYPE = Text.literal(_v.getType().toString()).setStyle(TYPE);
            $NAME = Text.literal(_v.getName()).setStyle(NAME);
            $VALUE = Text.literal(valueToString(_v.getValue())).setStyle(_v.getType().style());
        }

        switch (_type) {
            case GET -> info(Text.literal("").append($TYPE).append(" ").append($NAME).append(Text.literal(" = ").setStyle(OPERATOR)).append($VALUE).append(";").setStyle(OPERATOR));
            case SET -> info(Text.literal("Set value of ").append($NAME).append(" to ").append($VALUE));
            case CREATE -> info(Text.literal("Created variable ").append($NAME).append(" with type ").append($TYPE).append(" with value ").append($VALUE));
            case REMOVE -> info(Text.literal("Removed variable ").append($NAME));
            case LIST_NONE -> info("There are no variables");
            case LIST_BORDER_UP -> info(HEADER);
            case LIST_BORDER_DOWN -> info(FOOTER);
        }
    }

    private static String valueToString(@NotNull Value _v) {
        if (_v.isString()) return "\"" + _v + "\"";
        return _v.toString();
    }
    public enum LogType {
        CREATE, REMOVE, GET, SET, LIST_NONE, LIST_BORDER_UP, LIST_BORDER_DOWN
    }
}
