package ann.geral;

import data.ConjuntoDados;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import main.gui.ValoresDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Claudio
 * @// TODO: 2/20/2018 Remover esta classe, devendo existir somente {@link main.config.ConfigGeral}
 */
public class Topologia {

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
            Topologia.topologiaEscondida.set(novaTopologiaEscondida);
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
            Topologia.setHiddenTopology("5;3");
            tolopogiaArray = topology;
        }
    }

    public static int[] getTolopogiaArray() {
        return tolopogiaArray;
    }
}
