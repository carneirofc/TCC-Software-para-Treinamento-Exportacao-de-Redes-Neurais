package data;

import main.utils.ExceptionPlanejada;

import java.util.List;

/**
 * Organiza e armazena os dados de teste e treinamento utilizados na RNA
 */
public class ConjuntoDados {
    /**
     * Quais são as colunas do conjunto de dadosque representam informações de saída na RNA
     */
    public static final ConjuntoDados dadosTreinamento = new ConjuntoDados();
    public static final ConjuntoDados dadosTeste = new ConjuntoDados();
    /**
     * Valor minimo para normalização
     */
    private static double normMin;
    /**
     * Valor máximo para normalizaçao
     */
    private static double normMax;

    private double dadosEntrada[][];
    private double dadosSaida[][];

    private double dadosEntradaNorm[][];
    private double dadosSaidaNorm[][];

    private static double minEntrada[];
    private static double maxEntrada[];

    private static double minSaida[];
    private static double maxSaida[];
    /**
     * Quais são as colunas do conjunto de dados que representa informações para entrada na RNA
     */
    private static List<Integer> colunasEntrada;
    /**
     * Quais são as colunas do conjunto de dadosque representam informações de saída na RNA
     */
    private static List<Integer> colunasSaida;

    public int getNeuroniosEntradaQtd() {
        return ((colunasEntrada == null) ? 0 : colunasEntrada.size());
    }

    public int getNeuroniosSaidaQtd() {
        return ((colunasSaida == null) ? 0 : colunasSaida.size());
    }

    public static double[] getMinEntrada() {
        return minEntrada;
    }

    public static void setMinEntrada(double[] minEntrada) {
        ConjuntoDados.minEntrada = minEntrada;
    }

    public static double[] getMaxEntrada() {
        return maxEntrada;
    }

    public static void setMaxEntrada(double[] maxEntrada) {
        ConjuntoDados.maxEntrada = maxEntrada;
    }

    public static double[] getMinSaida() {
        return minSaida;
    }

    public static void setMinSaida(double[] minSaida) {
        ConjuntoDados.minSaida = minSaida;
    }

    public static double[] getMaxSaida() {
        return maxSaida;
    }

    public static void setMaxSaida(double[] maxSaida) {
        ConjuntoDados.maxSaida = maxSaida;
    }

    public double[][] getDadosSaidaNorm() {
        return dadosSaidaNorm;
    }

    public double[][] getDadosSaida() {
        return dadosSaida;
    }

    public void setDadosSaida(double[][] dadosSaida) {
        this.dadosSaida = dadosSaida;
    }

    public static List<Integer> getColunasSaida() {
        return colunasSaida;
    }

    public static void setColunasSaida(List<Integer> colunasSaida) {
        ConjuntoDados.colunasSaida = colunasSaida;
    }

    public static List<Integer> getColunasEntrada() {
        return colunasEntrada;
    }

    public static void setColunasEntrada(List<Integer> colunasEntrada) {
        ConjuntoDados.colunasEntrada = colunasEntrada;
    }

    public static double getNormMin() {
        return normMin;
    }

    public static double getNormMax() {
        return normMax;
    }

    public static void setNormMinMax(double normMin, double normMax) {
        ConjuntoDados.normMax = normMax;
        ConjuntoDados.normMin = normMin;
    }

    public double[][] getDadosEntrada() {
        return dadosEntrada;
    }

    public void setDadosEntrada(double[][] dadosEntrada) {
        this.dadosEntrada = dadosEntrada;
    }

    public double[][] getDadosEntradaNorm() {
        return dadosEntradaNorm;
    }


    public void verificaIntegridadeDados() throws ExceptionPlanejada {
        if (maxEntrada.length != dadosEntrada[0].length || minEntrada.length != dadosEntrada[0].length) {
            throw new ExceptionPlanejada("Erro com a relaçao dosLogTempo dadosEntrada de teste com os dadosEntrada de configuração. Tamanho de matriz");
        }
        if (dadosEntrada.length != dadosSaida.length) {
            throw new ExceptionPlanejada("Erro. Dados na entrada diferente da quantidade na saida.");
        }
    }

    public void normalizaDados() throws ExceptionPlanejada {

        if (ConjuntoDados.normMin >= ConjuntoDados.normMax) {
            throw new ExceptionPlanejada("Erro nos valores de mínimo e máximo para normalização. Valor Mínimo=" + normMin + " ValorMáximo=" + normMax + ".");
        }
        Operacoes.setNormMinMax(ConjuntoDados.normMin, ConjuntoDados.normMax);

        dadosEntradaNorm = new double[dadosEntrada.length][dadosEntrada[0].length];
        dadosSaidaNorm = new double[dadosSaida.length][dadosSaida[0].length];

        Operacoes.normalizeMatrix(dadosEntrada, dadosEntradaNorm, minEntrada, maxEntrada);
        Operacoes.normalizeMatrix(dadosSaida, dadosSaidaNorm, minSaida, maxSaida);
//
//        System.out.println("========== Dados Entrada ===========================");
//        for (int i = 0; i < dadosEntrada.length; i++) {
//            for (int j = 0; j < dadosEntrada[i].length; j++) {
//                System.out.print(dadosEntrada[i][j] + " ");
//            }
//            System.out.println();
//        }
//        System.out.println("-------------------------------------------");
//        for (int i = 0; i < dadosEntradaNorm.length; i++) {
//            for (int j = 0; j < dadosEntradaNorm[i].length; j++) {
//                System.out.print(dadosEntradaNorm[i][j] + " ");
//            }
//            System.out.println();
//        }
//
//
//        System.out.println();
//        System.out.println();
//        System.out.println("========== Dados Saida ===========================");
//        for (int i = 0; i < dadosSaida.length; i++) {
//            for (int j = 0; j < dadosSaida[i].length; j++) {
//                System.out.print(dadosSaida[i][j] + " ");
//            }
//            System.out.println();
//        }
//        System.out.println("-------------------------------------------");
//        for (int i = 0; i < dadosSaidaNorm.length; i++) {
//            for (int j = 0; j < dadosSaidaNorm[i].length; j++) {
//                System.out.print(dadosSaidaNorm[i][j] + " ");
//            }
//            System.out.println();
//        }

        System.out.println("Dados Normalizados Min=" + normMin + "Max=" + normMax + " Entrada/Saida" + this.getNeuroniosEntradaQtd() + " " + this.getNeuroniosSaidaQtd());
    }
}
