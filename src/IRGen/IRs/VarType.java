package IRGen.IRs;

public enum VarType {
    NUMBER,
    GLOBVAR,
    VOIDRET,

    SP,
    REG,
    /**
     * // tmp array stored in stack, so base get from $sp
     * */
    ARRAY,
}
