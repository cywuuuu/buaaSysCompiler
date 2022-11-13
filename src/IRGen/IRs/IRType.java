package IRGen.IRs;

public enum IRType {

    /**
     * assign:
     * move/li a, 10
     * */
    MOVE,
    /**
     * malloc arr size
     * */
    MALLOC, // arr = malloc

    /**
     * seq c, a, b
     * */
    EQ,

    /**
     * SNE, c, a, b
     * */
    NEQ,
    /**
     * sge c, b, a
     * */
    GEQ,

    /**
     * sgt c, a, b
     * */
    GT,

    /**
     * sle c, a, b
     * */
    LEQ,

    /**
     * slt c, a, b
     * */
    LT,

    /**
     * NOT (LOGIC) c, a : seq c, a, 0
     * */
    NOT,

    /**
     * fun_call:jal label
     * */
    CALL,

    /**
     * move c, v0
     * */
    GET_INT,

    /**
     * syscall
     * */
    READ_INT,

    /**
     * pint a
     * */
    PRINT_INT,

    /**
     * pstr a
     * */
    PRINT_STR,

    /**
     * getParam c, i, func -> stackpop or a0-3
     * */
    GET_PARAM,

    /**
     * setParam c, i, func
     * */
    SET_PARAM,

    /**
     * get res of fun_call: move c, v0
     * */
    GETRET,

    /**
     * j label
     * */
    JMP,

    /**
     * bnez
     * */
    JMP_TRUE,

    /**
     * beq
     * */
    JMP_FALSE,

    /**
     * jr $31
     * */
    JMP_RET,
    /**
     * LOAD c, arr, offset -> base+ offset *4
     * */
    LW,

    /**
     * STORE
     * */
    SW,

    /**
     * getPT -> calculate pointer
     * */
    GETADDR,

    /**
     * SUB c, b, a
     * */
    SUB,

    /**
     * ADD c, b, a
     * */
    ADD,

    /**
     * MUL c, b, a
     * */
    MUL,

    /**
     * div c, a, b, mflo
     * */
    DIV,


    /**
     * MOD c, a, b: div+mfhi
     * */
    MOD,

    /**
     * NEG c, a:negative
     * */
    NEG,


    PHI,

    /**
     * jr
     * */
    RET,

}


