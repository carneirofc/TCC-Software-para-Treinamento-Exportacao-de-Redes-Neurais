package data.txt.read;

import data.ConjuntoDados;
import data.Operacoes;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.gui.ValoresDisplay;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import main.Ctrl;
import main.utils.ExceptionPlanejada;
import org.jetbrains.annotations.NotNull;

/**
 * Responsável por carregar os arquivos .txt
 *
 * @author Claudio
 */
public class ManipuladorTxt {

    private static double[][] valorEntradaTreino;
    private static double[][] valorSaidaTreino;

    private static double[][] valorEntradaTeste;
    private static double[][] valorSaidaTeste;


    public static List<double[]> carregarDados(@NotNull List<File> files) throws Exception {
        String line;
        List<double[]> doubles = new ArrayList<>();

        if (Ctrl.isRnaEmExecucao())
            throw new ExceptionPlanejada("A RNA já está em execução.");
        if (!Ctrl.isPropertiesDadosCarregados())
            throw new ExceptionPlanejada("Antes de carregar os dados, o arquivo contendo a descrição das colunas de entrada/saída deve estar carregado.\n" +
                    "O arquivo de propriedades dosLogTempo dados não foi carregado.");

        for (File file : files) {
            if (file == null)
                throw new ExceptionPlanejada("O arquivo não foi carregado.");
            if (!file.isFile())
                throw new ExceptionPlanejada("O arquivo não é compatível.");
            BufferedReader br = new BufferedReader(new FileReader(file));

            while ((line = br.readLine()) != null) {
                // TODO: fazer um regex para substituir tudo de uma vex só
                String aux =
                        line.trim()
                                .replaceAll(",", " ")
                                .replaceAll(";", " ")
                                .replaceAll("\t", " ")
                                .replaceAll("\\s{2,}", " ");
                if (!aux.isEmpty()) {
                    String[] splited = aux.trim().split(" ");
                    double[] lineDouble = new double[splited.length];
                    for (int i = 0; i < splited.length; i++) {
                        lineDouble[i] = Double.parseDouble(splited[i]);
                    }
                    doubles.add(lineDouble);
                }
            }
        }
        return doubles;
    }

    public static void carregarDadosTeste(File file) throws Exception {
        carregarDadosTeste(Arrays.asList(file));
    }

    public static void carregarDadosTeste(@NotNull List<File> files) throws Exception {
        List<double[]> doubles = carregarDados(files);
        StringBuilder filesPath = new StringBuilder();
        for (File f : files) {
            filesPath.append(f.getCanonicalPath()).append("\n");
        }
        valorEntradaTeste = new double[doubles.size()][ConjuntoDados.dadosTeste.getNeuroniosEntradaQtd()];
        valorSaidaTeste = new double[doubles.size()][ConjuntoDados.dadosTeste.getNeuroniosSaidaQtd()];

        if (ConjuntoDados.dadosTeste.getNeuroniosEntradaQtd() > doubles.get(0).length || ConjuntoDados.dadosTeste.getNeuroniosSaidaQtd() > doubles.get(0).length) {
            throw new ExceptionPlanejada("Erro ao carregar informações de teste. Número para entrada ou saída é maior que numero de amostras ");
        }

        List<Integer> inputsColumnsNum = ConjuntoDados.dadosTeste.getColunasEntrada();
        List<Integer> outputsColumnsNum = ConjuntoDados.dadosTeste.getColunasSaida();

        // Associando os valores do banco de dados aos vetores específicos para dados de entrada e saída
        associaValores(doubles, inputsColumnsNum, outputsColumnsNum, valorEntradaTeste, valorSaidaTeste);
        // ------------------

        if (Ctrl.isSysLogInputTxt()) {
            showTxtVals(doubles, false);
        }
        Operacoes.embaralhaDados(valorSaidaTeste, valorEntradaTeste);

        double[][] testInMatrix = ManipuladorTxt.getValorEntradaTeste();
        double[][] testOutMatrix = ManipuladorTxt.getValorSaidaTeste();

        ConjuntoDados.dadosTeste.setDadosEntrada(testInMatrix);
        ConjuntoDados.dadosTeste.setDadosSaida(testOutMatrix);

        Ctrl.setDadosTesteCarregados(true);

        final String finalFilesPath = filesPath.toString();
        Platform.runLater(() -> ((SimpleStringProperty) ValoresDisplay.obsDadosTesteCaminho).set(finalFilesPath));
    }

    private static void associaValores(List<double[]> doubles, List<Integer> inputsColumnsNum, List<Integer> outputsColumnsNum, double[][] valorEntradaTeste, double[][] valorSaidaTeste) {
        for (int i = 0; i < doubles.size(); i++) {
            int jI = 0;
            int jO = 0;
            for (int j = 0; j < doubles.get(i).length; j++) {
                if (inputsColumnsNum.contains(j)) {
                    valorEntradaTeste[i][jI] = doubles.get(i)[j];
                    jI++;
                }
                if (outputsColumnsNum.contains(j)) {
                    valorSaidaTeste[i][jO] = doubles.get(i)[j];
                    jO++;
                }
            }
        }
    }

    /**
     * Carrega os dados de treinamento.
     *
     * @param file arquivo de .txt contendo os dados de treinamento
     */
    public static void carregarDadosTreino(@NotNull File file) throws Exception {
        carregarDadosTreino(Arrays.asList(file));
    }

    public static void carregarDadosTreino(@NotNull List<File> files) throws Exception {
        List<double[]> doubles = carregarDados(files);
        StringBuilder filesPath = new StringBuilder();
        for (File f : files) {
            filesPath.append(f.getCanonicalPath()).append("\n");
        }

        valorEntradaTreino = new double[doubles.size()][ConjuntoDados.dadosTreinamento.getNeuroniosEntradaQtd()];
        valorSaidaTreino = new double[doubles.size()][ConjuntoDados.dadosTreinamento.getNeuroniosSaidaQtd()];

        if (ConjuntoDados.dadosTreinamento.getNeuroniosEntradaQtd() > doubles.get(0).length || ConjuntoDados.dadosTreinamento.getNeuroniosSaidaQtd() > doubles.get(0).length) {
            throw new ExceptionPlanejada("Erro ao carregar informações de treino. Número para entrada ou saída é maior que numero de amostras .");
        }

        List<Integer> colunasDeEntrada = ConjuntoDados.dadosTreinamento.getColunasEntrada();
        List<Integer> colunasDeSaida = ConjuntoDados.dadosTreinamento.getColunasSaida();

        // Associando os valores do banco de dados aos vetores específicos para dados de entrada e saída
        associaValores(doubles, colunasDeEntrada, colunasDeSaida, valorEntradaTreino, valorSaidaTreino);
        // --------------------------------------

        Operacoes.embaralhaDados(valorSaidaTreino, valorEntradaTreino);
        //TODO: Criar ferramentas para o tratamento de dados mais claras
        // TODO: Dar ao usuário o poder de escolher quando normalizar, quando buscar novos valores mínimos e máximos para normalização etc....

        // Debug
        if (Ctrl.isSysLogInputTxt()) {
            showTxtVals(doubles, true);
        }

        ConjuntoDados.dadosTreinamento.setDadosEntrada(ManipuladorTxt.getValorEntradaTreino());
        ConjuntoDados.dadosTreinamento.setDadosSaida(ManipuladorTxt.getValorSaidaTreino());

        Ctrl.setDadosTreinoCarregados(true);

        final String finalFilesPath = filesPath.toString();
        Platform.runLater(() -> ((SimpleStringProperty) ValoresDisplay.obsDadosTreinoCaminho).set(finalFilesPath));
    }

    private static void showTxtVals(List<double[]> doubles, boolean train) {
        if (train) {
            if (valorSaidaTreino.length == valorEntradaTreino.length) {
                for (int i = 0; i < valorSaidaTreino.length; i++) {

                    System.out.print("| Entrada ");
                    for (int j = 0; j < valorEntradaTreino[i].length; j++) {
                        System.out.print(valorEntradaTreino[i][j] + " ");
                    }
                    System.out.print("| Saída ");

                    for (int j = 0; j < valorSaidaTreino[i].length; j++) {
                        System.out.print(valorSaidaTreino[i][j] + " ");
                    }
                    System.out.println();
                }
            }
        }
        System.out.println(" --- Tudo ");
        for (double[] aDouble : doubles) {
            for (double anADouble : aDouble) {
                System.out.print(anADouble + " ");
            }
            System.out.println();
        }
    }

    private static double[][] getValorEntradaTreino() {
        return valorEntradaTreino;
    }


    private static double[][] getValorSaidaTreino() {
        return valorSaidaTreino;
    }


    private static double[][] getValorEntradaTeste() {
        return valorEntradaTeste;
    }


    private static double[][] getValorSaidaTeste() {
        return valorSaidaTeste;
    }

}
