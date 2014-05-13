package org.digijava.module.aim.util;
/*
Created By: Vazha Ezugbaia
*/
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

    public static long calcExp(String exp) {
        String expr = exp.toLowerCase();
        expr = expr.replace(" ", "");
        expr = expr.replace(",",".");
        return (long)calcExp_Ex(expr);
    }

    private static double calcExp_Ex(String exp) {
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
                return Math.pow(calcExp_Ex(lexp), (int) calcExp_Ex(rexp));
            } else if (op.equals("*")) {
                return calcExp_Ex(lexp) * calcExp_Ex(rexp);
            } else if (op.equals("/")) {
                return calcExp_Ex(lexp) / calcExp_Ex(rexp);
            } else if (op.equals("\\")) {
                return Math.round(calcExp_Ex(lexp) / calcExp_Ex(rexp));
            } else if (op.equals("%")) {
                return calcExp_Ex(lexp) / 100 * calcExp_Ex(rexp);
            } else if (op.equals("#")) {
                return calcExp_Ex(lexp) % calcExp_Ex(rexp);
            } else if (op.equals("+")) {
                return calcExp_Ex(lexp) + calcExp_Ex(rexp);
            } else if (op.equals("-")) {
                return calcExp_Ex(lexp) - calcExp_Ex(rexp);
            } else if (op.equals("@")) {
                return (int) calcExp_Ex(lexp) ^ (int) calcExp_Ex(rexp);
            }
        }

        if (exp.startsWith("(") && exp.endsWith(")")) {
            return calcExp_Ex(exp.substring(1, exp.length() - 1));
        }

        if (exp.startsWith("-")) {
            return -calcExp_Ex(exp.substring(1));
        }

        if (exp.startsWith("+")) {
            return calcExp_Ex(exp.substring(1));
        }

        if (exp.length() > 4 && exp.endsWith(")")) {
            int pos = exp.indexOf("(");

            if (pos > 0 && pos < 4) {
                String lexp = exp.substring(0, pos);
                String rexp = exp.substring(pos + 1, exp.length() - pos - 2);

                if (lexp.equals("abs")) {
                    return Math.abs(calcExp_Ex(rexp));
                } else if (lexp.equals("cos")) {
                    return Math.cos(calcExp_Ex(rexp));
                } else if (lexp.equals("exp")) {
                    return Math.exp(calcExp_Ex(rexp));
                } else if (lexp.equals("flr")) {
                    return Math.floor(calcExp_Ex(rexp));
                } else if (lexp.equals("log")) {
                    return Math.log(calcExp_Ex(rexp));
                } else if (lexp.equals("rnd")) {
                    return Math.round(calcExp_Ex(rexp));
                } else if (lexp.equals("sin")) {
                    return Math.sin(calcExp_Ex(rexp));
                } else if (lexp.equals("sqr")) {
                    return Math.sqrt(calcExp_Ex(rexp));
                } else if (lexp.equals("tan")) {
                    return Math.tan(calcExp_Ex(rexp));
                } else if (lexp.equals("fct")) {
                    return fact(calcExp_Ex(rexp));
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

    public static boolean isLong(String str) {
        try {
            Long.valueOf(str);
            return true;
        } catch (Exception ex) {
            return false;
        }
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
