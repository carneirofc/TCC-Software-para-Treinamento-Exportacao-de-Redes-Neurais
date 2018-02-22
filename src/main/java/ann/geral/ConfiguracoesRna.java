package ann.geral;

import data.ConjuntoDados;

import java.util.ArrayList;
import java.util.List;

import data.txt.read.ManipuladorTxt;
import main.gui.ValoresDisplay;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import main.Ctrl;
import org.jetbrains.annotations.NotNull;

/**
 * @// TODO: 2/20/2018 Remover esta classe, devendo existir somente {@link main.config.ConfigGeral}
 *
 * Classe que vai cuidar de alguns detalhes dosLogTempo dados de teste relativos &agrave; esta rede em particular, cuida de tratar a topologia etc...
 * Com uma forte revis&atilde;o no projeto do programa, essa classe pode vir a desaparecer.
 *
 * @author Claudio
 */
public class ConfiguracoesRna {

    /**
     * Faixa na qual os pesos das ligações será buscada. Este valor representa a
     * "meia largura" ou seja, para um valor de 0.5, serão sorteados pesos entre
     * -0.5 e 0.5.
     */
    private static double faixaInicialPesos = 0.7;
    private static int epocaMaxima;
    private static double erroAlvo;
    /**
     * Termo adicionado na função de ativação do neurônio [Yann Lecun].
     */
    private static double termoLinear;

    /**
     * [0 ... 1] Overall learning rate - Taxa de aprendizado
     */
    private static double eta;

    /**
     * [0 .. n] Momentum (multiplier of the last weight change)
     */
    private static double momentum;

    /**
     * Topologia da ANN, quantidade de camadas (length) e número de neurônios
     * por camadas(val).
     * <p>
     * Não leva em consideração os neurônios de BIAS
     */
    private static int[] tolopogiaArray;

    private static SimpleStringProperty topologiaEscondida = (SimpleStringProperty) ValoresDisplay.obsTopologiaEscondida;// = new SimpleStringProperty();

    // TODO: Essa maneira de atualizar a topologiua não ideal, mudar para fica rmais fácil de trabalhar ....
    public static void setHiddenTopology(String novaTopologiaEscondida) {
        Platform.runLater(() -> {
            ConfiguracoesRna.topologiaEscondida.set(novaTopologiaEscondida);
            ((SimpleStringProperty) ValoresDisplay.obsTopologia).set(Integer.toString(ConjuntoDados.dadosTreinamento.getNeuroniosEntradaQtd()).concat(";").concat(novaTopologiaEscondida).concat(";").concat(Integer.toString(ConjuntoDados.dadosTreinamento.getNeuroniosSaidaQtd())));
        });
    }

    /**
     * TODO: isso é uma gambiarra pq estou fazendo a migração para o uso do json
     */
    public static void setHiddenTopology(@NotNull int[] novaTopologiaEscondida) {
        String s = "";
        for (int i = 0; i < novaTopologiaEscondida.length; i++) {
            s += Integer.toString(novaTopologiaEscondida[i]);
            if (i != novaTopologiaEscondida.length - 1)
                s += ";";
        }
        setHiddenTopology(s);
    }

    /**
     * Apenas atualiza a inteface gráfica com os valores das camadas de entrada e saída.s
     * Provisório
     * TODO: implementar de uma forma mais clara ...
     */
    public static void setHiddenTopology() {
        Platform.runLater(() -> {
            ((SimpleStringProperty) ValoresDisplay.obsTopologia).set(Integer.toString(ConjuntoDados.dadosTreinamento.getNeuroniosEntradaQtd()).concat(";").concat(topologiaEscondida.get()).concat(";").concat(Integer.toString(ConjuntoDados.dadosTreinamento.getNeuroniosSaidaQtd())));
        });
      //  Platform.
    }

    public static double getFaixaInicialPesos() {
        return faixaInicialPesos;
    }

    public static void setFaixaInicialPesos(double faixaInicialPesos) {
        ConfiguracoesRna.faixaInicialPesos = faixaInicialPesos;
    }

    public static int getMAX_EPOCH() {
        return epocaMaxima;
    }

    public static void setMAX_EPOCH(int MAX_EPOCH) {
        ConfiguracoesRna.epocaMaxima = MAX_EPOCH;
    }

    /**
     * Configura a topologia da RNA baseado nas informações configuradas.
     */
    public static void configTolopogia() {
        String[] aux = topologiaEscondida.get().trim().split(";");
        try {
            List<Integer> integers = new ArrayList<>();
            integers.add(ConjuntoDados.dadosTreinamento.getNeuroniosEntradaQtd());

            for (int i = 0; i < aux.length; i++) {
                integers.add(Math.abs(Integer.parseInt(aux[i])));
            }
            integers.add(ConjuntoDados.dadosTreinamento.getNeuroniosSaidaQtd());

            int[] topology = new int[integers.size()];
            for (int i = 0; i < topology.length; i++) {
                topology[i] = integers.get(i);
            }
            tolopogiaArray = topology;
        } catch (Exception e) {
            System.out.printf("Erro ao configurar a topologia, configurações padrão serão utilizadas. 5;3");
            e.printStackTrace();
            int[] topology = {ConjuntoDados.dadosTreinamento.getNeuroniosEntradaQtd(), 5, 3, ConjuntoDados.dadosTreinamento.getNeuroniosSaidaQtd()};
            ConfiguracoesRna.setHiddenTopology("5;3");
            tolopogiaArray = topology;
        }
    }

    public static double getTARGET_ERROR() {
        return erroAlvo;
    }

    public static void setTARGET_ERROR(double TARGET_ERROR) {
        ConfiguracoesRna.erroAlvo = TARGET_ERROR;
    }

    public static double getEta() {
        return eta;
    }

    public static void setEta(double ETA) {
        ConfiguracoesRna.eta = ETA;
    }

    public static double getMomentum() {
        return momentum;
    }

    public static void setMomentum(double momentum) {
        ConfiguracoesRna.momentum = momentum;
    }

    public static int[] getTolopogiaArray() {
        return tolopogiaArray;
    }

    public static double getLINEAR_TERM() {
        return termoLinear;
    }

    public static void setLINEAR_TERM(double LINEAR_TERM) {
        ConfiguracoesRna.termoLinear = LINEAR_TERM;
    }

    /**
     * Faz os ajustes para a rede funcionar de acordo com os dados de teste. Verifica se está tudo ok.
     */
    public static void setupDadosTeste() throws Exception {

        double[][] testInMatrix = ManipuladorTxt.getValorEntradaTeste();
        double[][] testOutMatrix = ManipuladorTxt.getValorSaidaTeste();

        ConjuntoDados.dadosTeste.setDadosEntrada(testInMatrix);
        ConjuntoDados.dadosTeste.setDadosSaida(testOutMatrix);

        Ctrl.setDadosTesteCarregados(true);
    }

    /**
     * Faz os ajustes para a rede funcionar de acordo com os dados de
     * treinamento. Verifica se está tudo ok.
     */
    public static void setupDadosTreino() throws Exception {

        ConjuntoDados.dadosTreinamento.setDadosEntrada(ManipuladorTxt.getValorEntradaTreino());
        ConjuntoDados.dadosTreinamento.setDadosSaida(ManipuladorTxt.getValorSaidaTreino());

        Ctrl.setDadosTreinoCarregados(true);
    }
}
