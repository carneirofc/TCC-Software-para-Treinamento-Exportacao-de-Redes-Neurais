package ann.geral;

import data.ConjuntoDados;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.StringConverter;
import main.config.ConfigGeral;
import main.gui.ValoresDisplay;
import main.utils.Converter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public static int[] getTolopogiaArray() {
        return tolopogiaArray;
    }

    private static void atulizaInterface() {
        Platform.runLater(() -> {
            ((SimpleStringProperty) ValoresDisplay.obsTopologia).set(Converter.intVectorToString(tolopogiaArray));
            ((SimpleStringProperty) ValoresDisplay.obsTopologiaEscondida).set(Converter.intVectorToString(ConfigGeral.getConfigGeralAtual().getTopologiaOculta()));
        });
    }

    /**
     * Configura a topologia da RNA baseado nas informações configuradas.
     */
    public static void configuraTolopogia() {
        int[] aux = ConfigGeral.getConfigGeralAtual().getTopologiaOculta();
        List<Integer> integers = new ArrayList<>();
        integers.add(ConjuntoDados.dadosTreinamento.getNeuroniosEntradaQtd());
        for (int anAux : aux) {
            integers.add(Math.abs(anAux));
        }
        integers.add(ConjuntoDados.dadosTreinamento.getNeuroniosSaidaQtd());

        int[] topology = new int[integers.size()];
        for (int i = 0; i < topology.length; i++) {
            topology[i] = integers.get(i);
        }
        tolopogiaArray = topology;
        atulizaInterface();
    }
}
