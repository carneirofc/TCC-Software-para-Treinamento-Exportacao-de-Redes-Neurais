/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormatSymbols;
import java.util.regex.Pattern;

/**
 * @author Claudio
 */
public class Converter {

    /**
     * USADO para diferenciar qual será o formatter da string
     */
    private static final byte INPUT_VECTOR = 01;

    @Contract(pure = true)
    public static String doubleVectorToString(double[] d) {
        String aux = "";
        if (d == null)
            return aux;
        for (int i = 0; i < d.length; i++) {
            aux += d[i];
            if (i != d.length - 1) {
                aux += ";";
            }
        }
        return aux;
    }

    @Contract(pure = true)
    public static String intVectorToString(int[] d) {
        String aux = "";
        if (d == null)
            return aux;
        for (int i = 0; i < d.length; i++) {
            aux += d[i];
            if (i != d.length - 1) {
                aux += ";";
            }
        }
        return aux;
    }

    @NotNull
    public static int[] stringToIntVector(String string) {
        string = string.trim().replaceAll(",|-", ";");
        if (string == null)
            return new int[]{};
        if (string.isEmpty())
            return new int[]{};
        string = string.replaceAll("[^0-9;]", "");
        String s[] = string.split(";");
        int is[] = new int[s.length];
        for (int i = 0; i < s.length; i++) {
            if (!s[i].equals(""))
                is[i] = Integer.parseInt(s[i]);
        }
        return is;
    }

    @Nullable
    public static double[] stringToDoubleVector(String string) throws ExceptionPlanejada {
        try {
            string = string.trim();
            if (string == null)
                return new double[]{};
            if (string.isEmpty())
                return new double[]{};
            char[] chars = string.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if ((chars[i] == '-')) {
                    if ((i > 0) && i < chars.length - 1) {
                        switch (chars[i - 1]) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                            case 'E':
                            case ';':
                                break;
                            default:
                                chars[i - 1] = ' ';
                        }
                        switch (chars[i + 1]) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                            case 'E':
                            case ';':
                                break;
                            default:
                                chars[i + 1] = '-';
                        }
                    } else if (i == (chars.length - 1)) {
                        chars[i] = ' ';
                    }
                }

            }
            //    System.out.println("string=" + string);
            string = new String(chars);
            //    System.out.println("stringChars=" + string);
            string = string.replaceAll("\\s{2,}", " ");
            string = string.trim().replaceAll("[\\s]", ";");
            string = string.trim().replaceAll(";{2,}", ";");
            string = string.trim().replaceAll("-{2,}", "-");
            string = string.replaceAll("[^-0-9E;" + DecimalFormatSymbols.getInstance().getDecimalSeparator() + "]", "");
            String s[] = string.split(";");
            //   System.out.println("stringReplace=" + string);
            double is[] = new double[s.length];
            for (int i = 0; i < s.length; i++) {
                //   System.out.println("s[i]=" + s[i]);
                if (!s[i].equals("") && !s[i].isEmpty())
                    is[i] = Double.parseDouble(s[i]);
            }
            //   System.out.println("------------------");
            return is;
        } catch (NumberFormatException e) {
            throw new ExceptionPlanejada("Erro com a formatação da String.");
        }
    }


}
