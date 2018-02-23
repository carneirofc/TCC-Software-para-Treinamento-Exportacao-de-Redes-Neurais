package main.gui.controller;

import ann.controller.RnaController;
import ann.funcao_ativacao.FuncaoTipo;
import ann.funcao_ativacao.FuncaoTipoStringConverter;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import main.config.ConfigGeral;
import main.gui.Janela;
import main.gui.ValoresDisplay;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.FormatStringConverter;
import main.Ctrl;
import main.utils.Converter;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class Opcoes implements Initializable {

    @FXML
    private JFXTextField tfTaxaDecaimento;
    @FXML
    private JFXComboBox cbFuncaoDecaimento;
    @FXML
    private Label lblFuncaoDecaimento;
    @FXML
    private JFXCheckBox cbDroput;
    @FXML
    private JFXComboBox<FuncaoTipo> cbFuncAtivacaoSaida;
    @FXML
    private JFXComboBox<FuncaoTipo> cbFuncaoAtivacao;
    @FXML
    private Label lblFuncaAtivSaida;
    @FXML
    private CustomTextField normMin;
    @FXML
    private CustomTextField normMax;
    @FXML
    private StackPane stackRoot;
    @FXML
    private JFXCheckBox cbLogErroEpoca;
    @FXML
    private TextField tfTopologia;
    @FXML
    private TextField tfEpocaMaxima;
    @FXML
    private TextField tfErroDesejado;
    @FXML
    private TextField tfTaxaAprendizado;
    @FXML
    private TextField tfMomentum;
    @FXML
    private TextField tfTermoLinear;
    @FXML
    private TextField tfPesoInicial;
    @FXML
    private JFXCheckBox cbLogTempoCamada;
    @FXML
    private Label lblTopologia;
    @FXML
    private Label lblEpocaMax;
    @FXML
    private Label lblErroDesejado;
    @FXML
    private Label lblTaxaAprendizado;
    @FXML
    private Label lblMomentum;
    @FXML
    private Label lblTermoLinear;
    @FXML
    private Label lblPesoInicial;
    @FXML
    private Label lblFuncaoAtiv;
    @FXML
    private JFXButton btnSalvar;
    @FXML
    private JFXButton btnCancelar;
    @FXML
    private Label lblTaxaAttGui;
    @FXML
    private Stage stage;
    private ValidationSupport validationSupport = new ValidationSupport();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void cancelar(ActionEvent event) {
        stage.close();
    }

    @FXML
    void salvar(ActionEvent event) throws Exception {
        if (!Ctrl.isRnaEmExecucao()) {

            Ctrl.setRnaDropout(cbDroput.isSelected());
            Ctrl.setLogTempoTreinoCamada(cbLogTempoCamada.isSelected());
            Ctrl.setLogErroEpoca(cbLogErroEpoca.isSelected());

            if (validationSupport.getValidationResult().getErrors().isEmpty()) {
                System.out.println("Sem erro");
                ConfigGeral.getConfigGeralAtual().setNormMin(Double.parseDouble(normMin.getText()));
                ConfigGeral.getConfigGeralAtual().setNormMax(Double.parseDouble(normMax.getText()));
                System.out.println("  " + ConfigGeral.getConfigGeralAtual().getNormMin() + "  " + ConfigGeral.getConfigGeralAtual().getNormMax());
            } else {
                System.out.println("Com erro");
            }

            ValoresDisplay.atualizaFuncaoAtivacao(cbFuncaoAtivacao.getValue(), true);
            ValoresDisplay.atualizaFuncaoAtivacao(cbFuncAtivacaoSaida.getValue(), false);

            if (!tfEpocaMaxima.getText().isEmpty())
                ConfigGeral.getConfigGeralAtual().setEpocaMaxima(NumberFormat.getIntegerInstance().parse(tfEpocaMaxima.getText()).intValue());
            if (!tfErroDesejado.getText().isEmpty()) {
                ConfigGeral.getConfigGeralAtual().setErroAlvo(ValoresDisplay.NUMBER_FORMAT_NOTACAO_CIENTIFICA.parse(tfErroDesejado.getText()).doubleValue());
            }
            if (!tfTaxaAprendizado.getText().isEmpty()) {
                ConfigGeral.getConfigGeralAtual().setTaxaAprendizado(ValoresDisplay.NUMBER_FORMAT_NOTACAO_CIENTIFICA.parse(tfTaxaAprendizado.getText()).doubleValue());
            }
            if (!tfMomentum.getText().isEmpty()) {
                ConfigGeral.getConfigGeralAtual().setMomentum(ValoresDisplay.NUMBER_FORMAT_NOTACAO_CIENTIFICA.parse(tfMomentum.getText()).doubleValue());
            }
            if (!tfTermoLinear.getText().isEmpty()) {
                ConfigGeral.getConfigGeralAtual().setTermoLinear(ValoresDisplay.NUMBER_FORMAT_NOTACAO_CIENTIFICA.parse(tfTermoLinear.getText()).doubleValue());
            }
            if (!tfPesoInicial.getText().isEmpty()) {
                ConfigGeral.getConfigGeralAtual().setFaixaPesos(ValoresDisplay.NUMBER_FORMAT_NOTACAO_CIENTIFICA.parse(tfPesoInicial.getText()).doubleValue());
            }
            //TODO: POG!
            if (!tfTopologia.getText().isEmpty()) {
                String hiddenTopology = tfTopologia.getText();
                hiddenTopology = hiddenTopology.trim();
                if (!hiddenTopology.isEmpty()) {
                    hiddenTopology = hiddenTopology.replaceAll(Pattern.quote(".01234567890E-4;"), "");
                    String s[] = hiddenTopology.split(";");
                    int is[] = new int[s.length];
                    for (int i = 0; i < s.length; i++) {
                        is[i] = Integer.parseInt(s[i]);
                    }
                    ConfigGeral.getConfigGeralAtual().setTopologiaOculta(is);
                }
            }
            ConfigGeral.setConfigGeralAtual();
            stage.close();
            //Janela.dialogo(stackRoot,"Aviso !", "Para determindadas mudanças surtirem efeito uma nova RNA deverá ser criada.", "Determinados tipos de mudanças, como alterações na topologia, exigem que uma nova RNA seja criada. Alterações na função de ativação em uma rede já treinada podem acarretar resultados inesperados.", Alert.AlertType.INFORMATION);
            Janela.dialogo(stackRoot, "Aviso !", "Para determindadas mudanças surtirem efeito uma nova RNA deverá ser criada." +
                    "\nDeterminados tipos de mudanças, como alterações na topologia, exigem que uma nova RNA seja criada. Alterações na função de ativação em uma rede já treinada podem acarretar resultados inesperados.");
            //todo: arrumar ... improviso ....
            if (RnaController.getRna() != null) {
                RnaController.getRna().configuraParametros();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Callback<ListView<FuncaoTipo>, ListCell<FuncaoTipo>> cellFactoryFuncaoTipo = new Callback<>() {
            @Override
            public ListCell<FuncaoTipo> call(ListView<FuncaoTipo> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(FuncaoTipo item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setText(null);
                        } else {
                            setText(item.getStringFuncao(ConfigGeral.getConfigGeralAtual().getTermoLinear()));
                        }
                    }
                };
            }
        };
        //cbFuncaoAtivacao.setCellFactory(cellFactoryFuncaoTipo);
        //cbFuncAtivacaoSaida.setCellFactory(cellFactoryFuncaoTipo);
        cbFuncaoAtivacao.setConverter(new FuncaoTipoStringConverter());
        cbFuncAtivacaoSaida.setConverter(new FuncaoTipoStringConverter());

        cbFuncaoAtivacao.getSelectionModel().selectFirst();
        cbFuncaoAtivacao.setItems(ValoresDisplay.obsFuncaoAtivacaoPossiveis);

        cbFuncAtivacaoSaida.getSelectionModel().selectFirst();
        cbFuncAtivacaoSaida.setItems(ValoresDisplay.obsFuncaoAtivacaoPossiveis);
        //  cbFuncaoDecaimento.setItems();

        cbDroput.setSelected(Ctrl.rnaDropoutProperty().get());
        cbLogTempoCamada.setSelected(Ctrl.logTempoTreinoCamadaProperty().get());
        cbLogErroEpoca.setSelected(Ctrl.logErroEpocaProperty().get());

        lblFuncaoAtiv.textProperty().bind(ValoresDisplay.obsFuncaoAtivacaoInternaAtual);
        lblFuncaAtivSaida.textProperty().bind(ValoresDisplay.obsFuncaoAtivacaoSaidaAtual);
        lblErroDesejado.textProperty().bind(Bindings.format("%e", ValoresDisplay.obsErroDesejado));
        lblMomentum.textProperty().bind(Bindings.format("%e", ValoresDisplay.obsMomentum));
        lblPesoInicial.textProperty().bind(Bindings.format("%f", ValoresDisplay.obsFaixaPesoInicial));
        lblTaxaAprendizado.textProperty().bind(Bindings.format("%e", ValoresDisplay.obsTaxaAprendizado));
        lblTopologia.textProperty().bind(ValoresDisplay.obsTopologiaEscondida);
        lblEpocaMax.textProperty().bind(Bindings.format("%d", ValoresDisplay.obsEpocaMaxima));
        lblTermoLinear.textProperty().bind(Bindings.format("%e", ValoresDisplay.obsTermoLinear));

        tfEpocaMaxima.setTextFormatter(new TextFormatter<>(new FormatStringConverter<>(NumberFormat.getIntegerInstance())));
        tfErroDesejado.setTextFormatter(new TextFormatter<>(new FormatStringConverter<>(ValoresDisplay.NUMBER_FORMAT_NOTACAO_CIENTIFICA)));
        tfTermoLinear.setTextFormatter(new TextFormatter<>(new FormatStringConverter<>(ValoresDisplay.NUMBER_FORMAT_NOTACAO_CIENTIFICA)));
        tfTaxaAprendizado.setTextFormatter(new TextFormatter<>(new FormatStringConverter<>(ValoresDisplay.NUMBER_FORMAT_NOTACAO_CIENTIFICA)));
        tfMomentum.setTextFormatter(new TextFormatter<>(new FormatStringConverter<>(ValoresDisplay.NUMBER_FORMAT_NOTACAO_CIENTIFICA)));
        tfPesoInicial.setTextFormatter(new TextFormatter<>(new FormatStringConverter<>(NumberFormat.getInstance())));
        tfTopologia.setTextFormatter(new TextFormatter<>(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                if (object == null)
                    return null;
                return Converter.intVectorToString(Converter.stringToIntVector(object));
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        }));

        normMin.setTooltip(new Tooltip("Mínimo Atual: " + ConfigGeral.getConfigGeralAtual().getNormMin()));
        normMax.setTooltip(new Tooltip("Máximo Atual: " + ConfigGeral.getConfigGeralAtual().getNormMax()));

        normMax.setTextFormatter(new TextFormatter<>(new FormatStringConverter<>(DecimalFormat.getInstance())));
        normMin.setTextFormatter(new TextFormatter<>(new FormatStringConverter<>(DecimalFormat.getInstance())));

        Validator<String> stringValidator = (control, s) -> {
            boolean condition = true;
            try {
                double min, max;
                min = Double.parseDouble(normMin.getText());
                max = Double.parseDouble(normMax.getText());
                if (min < max)
                    condition = false;
            } catch (Exception e) {
                // e.printStackTrace();
            }
            return ValidationResult.fromMessageIf(control, "Faixa de valores não é adequada.", Severity.ERROR, condition);
        };
        validationSupport.registerValidator(normMin, true, stringValidator);
        validationSupport.registerValidator(normMax, true, stringValidator);

    }
}
