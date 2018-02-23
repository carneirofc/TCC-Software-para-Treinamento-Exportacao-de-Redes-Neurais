package data.properties.read;

import ann.geral.Topologia;
import data.ConjuntoDados;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import main.gui.ValoresDisplay;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import main.Ctrl;
import main.utils.ExceptionPlanejada;
import main.utils.Utilidade;

/**
 * Classe responsável pela leitura de arquivos .properties bem como a associação
 * dos valores às devidas classes.
 *
 * @author Claudio
 */

public class ManipuladorProperties {

    private static Properties dataProperties = new Properties();
    private static InputStream inputStream;


    /**
     * Carrega os dados do arquivo para o programa.
     */
    private static void configTrainData() {
        ConjuntoDados.setColunasEntrada(Utilidade.getInt(Utilidade.getStringSeparadorEspaco(dataProperties.getProperty("colunasEntrada"))));
        ConjuntoDados.setColunasSaida(Utilidade.getInt(Utilidade.getStringSeparadorEspaco(dataProperties.getProperty("colunasSaida"))));

        //DadosRna.setColunasEntrada();
        //DadosRna.setColunasSaida(Utilidade.getInt(Utilidade.getStringSeparadorEspaco(dataProperties.getProperty("colunasSaida"))));

      //  DadosRna.setColunasEntradaQtd(DadosRna.getColunasEntrada().size());
       // DadosRna.setColunasSaidaQtd(DadosRna.getColunasSaida().size());
        Topologia.setHiddenTopology();
    }

    /**
     * Sempre que um novo arquivo de properties para treino for carregado deve
     * ser carregado também um novo conjunto de dados para treino e teste.
     */
    public static void loadTrainProperties(File file) throws Exception {
        if (file == null)
            throw new ExceptionPlanejada("Não existe arquivo carregado.");
        if (!file.isFile())
            throw new ExceptionPlanejada("Arquivo não corresponde ao esperado.");
        if (Ctrl.isRnaEmExecucao()) {
            throw new ExceptionPlanejada("A RNA já está em execução.");
        }
        inputStream = new FileInputStream(file);
        dataProperties.load(inputStream);
        inputStream.close();

        configTrainData();

        Ctrl.setDadosTreinoCarregados(false);
        Ctrl.setDadosTesteCarregados(false);
        Ctrl.setPropertiesDadosCarregados(true);

        Platform.runLater(() -> {
            try {
                ((SimpleStringProperty) ValoresDisplay.obsPropertiesCaminho).set(file.getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
