package main.gui;

import ann.funcao_ativacao.FuncaoTipo;
import ann.geral.DecaimentoTaxaAprendizado;
import ann.geral.FuncaoDecaimento;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Ctrl;
import main.config.ConfigGeral;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;

public class ValoresDisplay {

    public static final DecimalFormat NUMBER_FORMAT_NOTACAO_CIENTIFICA = new DecimalFormat();

    static {
        NUMBER_FORMAT_NOTACAO_CIENTIFICA.applyPattern("0.0000#E0");
    }

    // Provenientes de ConfigGeral
    public static ObservableDoubleValue obsMomentum = new SimpleDoubleProperty();
    public static ObservableDoubleValue obsTaxaAprendizado = new SimpleDoubleProperty();
    public static ObservableDoubleValue obsTaxaAprendizadoAtual = new SimpleDoubleProperty();
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
    public static ObservableList<FuncaoTipo> obsFuncaoAtivacaoPossiveis = FXCollections.observableArrayList();
    public static ObservableList<FuncaoDecaimento> obsFuncaoDecaimentoPosssiveis = FXCollections.observableArrayList();

    public static void inivializaLists() {
        if (obsFuncaoAtivacaoPossiveis.isEmpty()) {
            obsFuncaoAtivacaoPossiveis.addAll(Arrays.asList(FuncaoTipo.values()));
        }
        if (obsFuncaoDecaimentoPosssiveis.isEmpty()) {
            obsFuncaoDecaimentoPosssiveis.addAll(Arrays.asList(FuncaoDecaimento.values()));
        }
    }

    public static void atualizaFuncaoAtivacao(FuncaoTipo funcaoTipo, boolean interna) {
        if (funcaoTipo == null) {
            return;
        }
        if (interna) {
            ConfigGeral.getConfigGeralAtual().setFuncaoAtivacao(funcaoTipo);
            ((SimpleStringProperty) ValoresDisplay.obsFuncaoAtivacaoInternaAtual).set(funcaoTipo.getStringFuncao(ConfigGeral.getConfigGeralAtual().getTermoLinear()));
        } else {
            ConfigGeral.getConfigGeralAtual().setFuncaoAtivacaoSaida(funcaoTipo);
            ((SimpleStringProperty) ValoresDisplay.obsFuncaoAtivacaoSaidaAtual).set(funcaoTipo.getStringFuncao(ConfigGeral.getConfigGeralAtual().getTermoLinear()));
        }
    }

}

