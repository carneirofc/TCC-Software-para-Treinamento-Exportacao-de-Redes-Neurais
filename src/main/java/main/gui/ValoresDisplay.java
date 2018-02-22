package main.gui;

import ann.geral.FuncaoTipo;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Ctrl;
import main.config.ConfigGeral;

import java.text.DecimalFormat;

public class ValoresDisplay {

    public static final DecimalFormat NUMBER_FORMAT_NOTACAO_CIENTIFICA = new DecimalFormat();

    static {
        NUMBER_FORMAT_NOTACAO_CIENTIFICA.applyPattern("0.0000#E0");
    }

    // Provenientes de ConfigGeral
    public static ObservableDoubleValue obsMomentum = new SimpleDoubleProperty();
    public static ObservableDoubleValue obsTaxaAprendizado = new SimpleDoubleProperty();
    public static ObservableDoubleValue obsTermoLinear = new SimpleDoubleProperty();
    public static ObservableDoubleValue obsFaixaPesoInicial = new SimpleDoubleProperty();
    public static ObservableDoubleValue obsErroDesejado = new SimpleDoubleProperty();
    public static ObservableLongValue obsEpocaMaxima = new SimpleLongProperty();
    public static ObservableStringValue obsTopologiaEscondida = new SimpleStringProperty();
    public static ObservableStringValue obsTopologia = new SimpleStringProperty();
    public static ObservableStringValue obsFuncaoAtivacaoInternaAtual = new SimpleStringProperty();
    public static ObservableStringValue obsFuncaoAtivacaoSaidaAtual = new SimpleStringProperty();
    // -----------------------------------------------------------------------------------

    // Provenientes do Estado Atual da RNA
    public static ObservableLongValue obsEpocaAtual = new SimpleLongProperty();
    public static ObservableBooleanValue obsRnaEmExecucao = Ctrl.rnaEmExecucaoProperty();
    public static ObservableStringValue obsFeedforwardResultado = new SimpleStringProperty();

    public static ObservableDoubleValue obsTreinoTempoDeTreinamento = new SimpleDoubleProperty();
    public static ObservableDoubleValue obsTreinoErro = new SimpleDoubleProperty();
    public static ObservableDoubleValue obsTreinoErroTeste = new SimpleDoubleProperty();

    public static ObservableDoubleValue obsTesteErroMedio = new SimpleDoubleProperty();
    public static ObservableDoubleValue obsTesteErroMaximo = new SimpleDoubleProperty();
    public static ObservableDoubleValue obsTesteErroMinimo = new SimpleDoubleProperty();
    public static ObservableDoubleValue obsTesteTempoDeTeste = new SimpleDoubleProperty();
    public static ObservableIntegerValue obsTesteNumeroAmostras = new SimpleIntegerProperty();

    // -----------------------------------------------------------------------------------

    // Provenientes dosLogTempo Arquivos e Configuraçoes
    public static ObservableStringValue obsDadosTreinoCaminho = new SimpleStringProperty("");
    public static ObservableStringValue obsDadosTesteCaminho = new SimpleStringProperty("");
    public static ObservableStringValue obsPropertiesCaminho = new SimpleStringProperty("");
    public static ObservableStringValue obsPropertiesConfigCaminho = new SimpleStringProperty("");
    public static ObservableBooleanValue obsGerarLog = Ctrl.gerarAquivoLogProperty();
    public static ObservableBooleanValue obsDadoTesteDisable = new SimpleBooleanProperty(true);
    public static ObservableBooleanValue obsMostrarTexto = Ctrl.guiMostrarTextoProperty();
    public static ObservableBooleanValue obsMostrarGrafico = Ctrl.guiMostrarGraficoProperty();
    public static ObservableList<String> obsFuncaoAtivacaoPossiveis = FXCollections.observableArrayList();
    public static ObservableList<String> obsFuncaoDecaimentoPosssiveis = FXCollections.observableArrayList();

    public static void atualizaFuncaoAtivacao(String s, boolean interna) {
        if (s == null) {
            return;
        }
        if (obsFuncaoAtivacaoPossiveis.isEmpty()) {
            for (FuncaoTipo funcaoTipo : FuncaoTipo.values()) {
                obsFuncaoAtivacaoPossiveis.add(funcaoTipo.getNomeFuncao());
            }
        }

        String[] res = s.split(":");
        if (interna) {
            ConfigGeral.getConfigGeralAtual().setFuncaoAtivacao(FuncaoTipo.getPorCod(Integer.parseInt((res[0]).trim())));
            ((SimpleStringProperty) ValoresDisplay.obsFuncaoAtivacaoInternaAtual).set((res[1]).trim());
        } else {
            ConfigGeral.getConfigGeralAtual().setFuncaoAtivacaoSaida(FuncaoTipo.getPorCod(Integer.parseInt((res[0]).trim())));
            ;
            ((SimpleStringProperty) ValoresDisplay.obsFuncaoAtivacaoSaidaAtual).set((res[1]).trim());
        }
    }

}

