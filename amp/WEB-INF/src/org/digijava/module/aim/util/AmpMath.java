package org.digijava.module.aim.util;

public class AmpMath {
    static byte PREC_PLUS = 3;
    static byte PREC_MIN = 4;
    static byte PREC_MUL = 5;
    static byte PREC_INT_DIV = 6;
    static byte PREC_DIV = 7;
    static byte PREC_XOR = 8;
    static byte PREC_MOD = 9;
    static byte PREC_PER = 10;
    static byte PREC_POWER = 11;
    static byte PREC_UNARY = 12; // Not actually used.
    static byte PREC_NONE = 13;

    public static double CalcExp(String exp) {
        String expr = exp.toLowerCase();
        expr = expr.replace(" ", "");
        expr = expr.replace(",",".");
        return CalcExp_Ex(expr);
    }

    private static double CalcExp_Ex(String exp) {
        if (isEmpty(exp)) {
            return 0;
        }

        if (isNumeric(exp)) {
            return Double.valueOf(exp);
        } else if (exp.equals("p")) {
            return Math.PI;
        } else if (exp.equals("e")) {
            return Math.E;
        }

        boolean is_unary = true;
        boolean next_unary;
        int parens = 0;
        int best_pos = 0;

        byte best_prec = PREC_NONE;

        for (int i = 0;i<exp.length();i++) {
            String ch = exp.substring(i, i + 1);

            next_unary = false;

            if (ch.equals("(")) {
                parens++;
                next_unary = true;
            } else if (ch.equals(")")) {
                parens--;
                next_unary = false;
            } else if (parens == 0) {
                if (ch.equals("^") ||
                    ch.equals("*") ||
                    ch.equals("/") ||
                    ch.equals("\\") ||
                    ch.equals("%") ||
                    ch.equals("#") ||
                    ch.equals("@") ||
                    ch.equals("+") ||
                    ch.equals("-")) {

                    next_unary = true;
                    if (ch.equals("^") && best_prec >= PREC_POWER) {
                        best_prec = PREC_POWER;
                        best_pos = i;

                    } else if (ch.equals("*") && best_prec >= PREC_MUL) {
                        best_prec = PREC_MUL;
                        best_pos = i;

                    } else if (ch.equals("/") && best_prec >= PREC_DIV) {
                        best_prec = PREC_DIV;
                        best_pos = i;

                    } else if (ch.equals("\\") && best_prec >= PREC_INT_DIV) {
                        best_prec = PREC_INT_DIV;
                        best_pos = i;

                    } else if (ch.equals("%") && best_prec >= PREC_PER) {
                        best_prec = PREC_PER;
                        best_pos = i;

                    } else if (ch.equals("#") && best_prec >= PREC_MOD) {
                        best_prec = PREC_MOD;
                        best_pos = i;

                    } else if (ch.equals("@") && best_prec >= PREC_XOR) {
                        best_prec = PREC_XOR;
                        best_pos = i;

                    } else if (ch.equals("+") && !is_unary && best_prec >= PREC_PLUS) {
                        best_prec = PREC_PLUS;
                        best_pos = i;

                    } else if (ch.equals("-") && !is_unary && best_prec >= PREC_MIN) {
                        best_prec = PREC_MIN;
                        best_pos = i;

                    }
                }
            }
            is_unary = next_unary;
        }

        if (parens > 0) {
            throw new RuntimeException("Missing ) in '" + exp + "'");
        } else if (parens < 0) {
            throw new RuntimeException("Too many )s in '" + exp + "'");
        }

        if (best_prec < PREC_NONE) {
            String lexp = exp.substring(0, best_pos);
            String rexp = exp.substring(best_pos + 1);
            String op = exp.substring(best_pos, best_pos + 1);
            if (op.equals("^")) {
                return Math.pow(CalcExp_Ex(lexp), (int) CalcExp_Ex(rexp));
            } else if (op.equals("*")) {
                return CalcExp_Ex(lexp) * CalcExp_Ex(rexp);
            } else if (op.equals("/")) {
                return CalcExp_Ex(lexp) / CalcExp_Ex(rexp);
            } else if (op.equals("\\")) {
                return Math.round(CalcExp_Ex(lexp) / CalcExp_Ex(rexp));
            } else if (op.equals("%")) {
                return CalcExp_Ex(lexp) / 100 * CalcExp_Ex(rexp);
            } else if (op.equals("#")) {
                return CalcExp_Ex(lexp) % CalcExp_Ex(rexp);
            } else if (op.equals("+")) {
                return CalcExp_Ex(lexp) + CalcExp_Ex(rexp);
            } else if (op.equals("-")) {
                return CalcExp_Ex(lexp) - CalcExp_Ex(rexp);
            } else if (op.equals("@")) {
                return (int) CalcExp_Ex(lexp) ^ (int) CalcExp_Ex(rexp);
            }
        }

        if (exp.startsWith("(") && exp.endsWith(")")) {
            return CalcExp_Ex(exp.substring(1, exp.length() - 1));
        }

        if (exp.startsWith("-")) {
            return -CalcExp_Ex(exp.substring(1));
        }

        if (exp.startsWith("+")) {
            return CalcExp_Ex(exp.substring(1));
        }

        if (exp.length() > 4 && exp.endsWith(")")) {
            int pos = exp.indexOf("(");

            if (pos > 0 && pos < 4) {
                String lexp = exp.substring(0, pos);
                String rexp = exp.substring(pos + 1, exp.length() - pos - 2);

                if (lexp.equals("abs")) {
                    return Math.abs(CalcExp_Ex(rexp));
                } else if (lexp.equals("cos")) {
                    return Math.cos(CalcExp_Ex(rexp));
                } else if (lexp.equals("exp")) {
                    return Math.exp(CalcExp_Ex(rexp));
                } else if (lexp.equals("flr")) {
                    return Math.floor(CalcExp_Ex(rexp));
                } else if (lexp.equals("log")) {
                    return Math.log(CalcExp_Ex(rexp));
                } else if (lexp.equals("rnd")) {
                    return Math.round(CalcExp_Ex(rexp));
                } else if (lexp.equals("sin")) {
                    return Math.sin(CalcExp_Ex(rexp));
                } else if (lexp.equals("sqr")) {
                    return Math.sqrt(CalcExp_Ex(rexp));
                } else if (lexp.equals("tan")) {
                    return Math.tan(CalcExp_Ex(rexp));
                } else if (lexp.equals("fct")) {
                    return fact(CalcExp_Ex(rexp));
                } else {
                    throw new RuntimeException("Invalid function " + exp);
                }
            }
        }
        return Double.valueOf(exp);
    }

    public static double fact(double value) {
        if (value <= 1) {
            return 0;
        }

        double result = 1;

        for (double i = 0; i < value; i++) {
            result *= i;
        }

        return result;
    }

    public static boolean isNumeric(Object o) {
        try {
            Double.valueOf(String.valueOf(o));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isEmpty(Object o) {
        if(o==null || o.equals("")) return true;
        return false;
    }
}
