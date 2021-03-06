package data.properties.read;

import ann.geral.Topologia;
import data.ConjuntoDados;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import main.Ctrl;
import main.gui.ValoresDisplay;
import main.utils.ExceptionPlanejada;
import main.utils.Utilidade;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
    private static void configTrainData() throws Exception {
        String in, out;
        in = dataProperties.getProperty("in");
        out = dataProperties.getProperty("out");
        if (in == null || out == null || in.equals("") || out.equals(""))
            throw new Exception("Erro ao carregar os dados do dataset.\n" +
                    "Verifique se as colunas de entradas estão setadas na propriedade 'in'  e as colunas de saída na propriedade 'out'" +
                    "Ex:\n\tin=0,1\n\tout=2");

        ConjuntoDados.setColunasEntrada(Utilidade.getInt(Utilidade.getStringSeparadorEspaco(in)));
        ConjuntoDados.setColunasSaida(Utilidade.getInt(Utilidade.getStringSeparadorEspaco(out)));

        Topologia.configuraTolopogia();
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
                ((SimpleStringProperty) ValoresDisplay.obsDadosTesteCaminho).set("");
                ((SimpleStringProperty) ValoresDisplay.obsDadosTreinoCaminho).set("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
