package data;


import main.config.ConfigGeral;
import org.jetbrains.annotations.Contract;

import java.util.Random;

/**
 * @author Claudio
 */
public class Operacoes {

    private static double normMin;
    private static double normMax;

    private static final Random RANDOM = new Random();

    public static void setNormMinMax(double normMin, double normMax) {
        Operacoes.normMin = normMin;
        Operacoes.normMax = normMax;
    }

    public static double getNormMin() {
        return normMin;
    }

    public static void setNormMin(double normMin) {
        Operacoes.normMin = normMin;
    }

    public static double getNormMax() {
        return normMax;
    }

    public static void setNormMax(double normMax) {
        Operacoes.normMax = normMax;
    }

    /**
     * Percorre os dados de entrada para encontrar o maiores e os menores
     * valores.
     */
    public static void findMinMaxVals(
            double[][] trainOutputVals,
            double[][] trainInputVals,
            int inputColumnsQuantity,
            int outputColumnsQuantity ) {

        double[] minIn = new double[inputColumnsQuantity];
        double[] maxIn = new double[inputColumnsQuantity];

        double[] minOut = new double[outputColumnsQuantity];
        double[] maxOut = new double[outputColumnsQuantity];

        if (trainInputVals.length == trainOutputVals.length) {
            for (int i = 0; i < trainInputVals.length; i++) {

                for (int j = 0; j < trainInputVals[i].length; j++) {
                    if (i == 0) {
                        minIn[j] = trainInputVals[0][j];
                        maxIn[j] = trainInputVals[0][j];
                    } else {
                        if (trainInputVals[i][j] < minIn[j]) {
                            minIn[j] = trainInputVals[i][j];
                        }
                        if (trainInputVals[i][j] > maxIn[j]) {
                            maxIn[j] = trainInputVals[i][j];
                        }
                    }
                }

                for (int j = 0; j < trainOutputVals[i].length; j++) {
                    if (i == 0) {
                        minOut[j] = trainOutputVals[0][j];
                        maxOut[j] = trainOutputVals[0][j];
                    } else {
                        if (trainOutputVals[i][j] < minOut[j]) {
                            minOut[j] = trainOutputVals[i][j];
                        }
                        if (trainOutputVals[i][j] > maxOut[j]) {
                            maxOut[j] = trainOutputVals[i][j];
                        }
                    }
                }
            }

            ConfigGeral.getConfigGeralAtual().setInColumnMax(maxIn);
            ConfigGeral.getConfigGeralAtual().setInColumnMin(minIn);

            ConfigGeral.getConfigGeralAtual().setOutColumnMax(maxOut);
            ConfigGeral.getConfigGeralAtual().setOutColumnMin(minOut);

            ConjuntoDados.setMaxEntrada(maxIn);
            ConjuntoDados.setMinEntrada(minIn);

            ConjuntoDados.setMaxSaida(maxOut);
            ConjuntoDados.setMinSaida(minOut);
        }
    }

    public static double sech(double x) {
        double eEx = Math.pow(Math.E, x);
        double eEmx = Math.pow(Math.E, -x);
        return 2 / (eEx + eEmx);
    }

    public static double tanh(double x) {
        double eEx = Math.pow(Math.E, x);
        double eEmx = Math.pow(Math.E, -x);
        double res;
        if (x == Double.POSITIVE_INFINITY) {
            res = 1;
        } else if (x == Double.NEGATIVE_INFINITY) {
            res = -1;
        } else {
            res = (eEx - eEmx) / (eEx + eEmx);
        }
        return res;
    }

    public static double tanhDerivative(double x) {
        double tanh_x = tanh(x);
        double res = (1 - (tanh_x * tanh_x));
        return res;
    }

    public static double sigmoid(double x) {
        return 1 / (1 + Math.pow(Math.E, -x));
    }

    public static double sigmoidDerivative(double x) {
        double e_m_x = Math.pow(Math.E, -x);
        return e_m_x / ((1 + e_m_x) * (1 + e_m_x));
    }

    /**
     * Normaliza os dados (linear) para uma faixa determinada.
     */
    @Contract(pure = true)
    public static double normalizeValue(double min, double max, double x) {
        double a = (normMax - normMin) / (max - min);
        double b = (-normMax * min + normMin * max) / (max - min);
        return (a * x + b);
    }

    /**
     * Caminho inverso da normalização
     */
    @Contract(pure = true)
    public static double deNormalizeValue(double min, double max, double x) {
        double a = (normMax - normMin) / (max - min);
        double b = (-normMax * min + normMin * max) / (max - min);
        return (x - b) / a;
    }

    /**
     * Normaliza todos os dados da matriz
     */
    public static void normalizeMatrix(double[][] matrix,double[][] matrizNormalizada, double[] min, double[] max) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                double aux = normalizeValue(min[j], max[j], matrix[i][j]);
                matrizNormalizada[i][j] = aux;
            }
        }
    }

    public static void deNormalizeVector(double[] vector, double[] min, double[] max) {
        if (vector.length == min.length && vector.length == max.length) {
            for (int i = 0; i < vector.length; i++) {
                double aux = deNormalizeValue(min[i], max[i], vector[i]);
                vector[i] = aux;
            }
        }
    }

    public static void normalizeVector(double[] vector, double[] min, double[] max) {
        if (vector.length == min.length && vector.length == max.length) {
            for (int i = 0; i < vector.length; i++) {
                double aux = normalizeValue(min[i], max[i], vector[i]);
                vector[i] = aux;
            }
        }
    }

    /**
     * Embaralha usando o método de Fisher–Yates
     */
    public static double[] embaralhaDados(double[] vector) {
        for (int i = vector.length - 1; i > 0; i--) {
            int rand = RANDOM.nextInt(i + 1);
            double aux1 = vector[i];
            vector[i] = vector[rand];
            vector[rand] = aux1;
        }
        return vector;
    }

    /**
     * Embaralha usando o método de Fisher–Yates.
     * Por conveniência esse método embaralha 2 matrizes por vez
     */
    public static boolean embaralhaDados(double[][] matrix1, double[][] matrix2) {
        if (matrix1.length == matrix2.length) {
            /*
               Aqui já vou encontrar os valores min/max das entradas.
             */
            for (int i = matrix1.length - 1; i > 0; i--) {
                int rand = RANDOM.nextInt(i + 1);

                double[] aux1 = matrix1[i];
                matrix1[i] = matrix1[rand];
                matrix1[rand] = aux1;

                double[] aux2 = matrix2[i];
                matrix2[i] = matrix2[rand];
                matrix2[rand] = aux2;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retorna o valor de um numero
     */
    @Contract(pure = true)
    public static double nanoParaNormal(long nanos) {
        return (nanos / 1000000000.0);
    }

    public static long normalParaNano(double segundo) {
        return (long) (segundo * 1000000000.0);
    }

    public static double gaussiana(double x, double c, double sigma) {
        return Math.exp(-(Math.pow(x - c, 2) / (2 * sigma)));
    }

    public static double gaussiana(double x) {
        // return Math.exp(-(Math.pow(x-c,2)/(2*sigma)));
        return Math.exp(-(x * x / (2))); // para c = 0 e sigma = 1;
    }

    public static double gaussianaDiff(double x, double c, double sigma) {
        return -((x - c) * Math.exp(-(Math.pow(x - c, 2) / (2 * sigma)))) / sigma;
    }

    @Contract(pure = true)
    public static double gaussianaDiff(double x) {
        return -((x) * Math.exp(-(x * x) / (2)));
    }
}
