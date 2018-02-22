package main.gui.controller;

import com.jfoenix.controls.JFXButton;
import data.txt.write.ExportaTexto;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.FormatStringConverter;
import main.Ctrl;
import main.Main;
import main.Recursos;
import main.Tarefas;
import main.gui.Janela;
import main.gui.ValoresDisplay;
import main.gui.grafico.GraficoDispersao;
import main.utils.ExceptionPlanejada;
import main.utils.Utilidade;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.File;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class Teste implements Initializable {

    private static AtomicBoolean existe = new AtomicBoolean(false);
    public static final GraficoDispersao GRAFICO_DISPERSAO = new GraficoDispersao("Desempenho de Teste", "Erro Percentual");


    public JFXButton btnMostraGraph;
    public JFXButton btnLimpaGraph;
    public JFXButton btnTestar;
    public JFXButton btnOk;
    public JFXButton btnGerarConjTreino;
    public JFXButton btnCarregarDadosTeste;
    public CustomTextField erroInicial;
    public CustomTextField erroFinal;
    public Label erroMedio;
    public Label erroMaximo;
    public Label erroMinimo;
    public Label numeroAmostras;
    public Tab tabTeste;
    public Label lblDadosTreino;
    public Label tempoTeste;


    @FXML
    public void sair(javafx.event.ActionEvent event) {
        Stage stage = (Stage) btnOk.getScene().getWindow();
        stage.close();
    }

    public void mostrarGrafico(javafx.event.ActionEvent actionEvent) {
        Ctrl.setGuiMostrarGrafico(!Ctrl.isGuiMostrarGrafico());
    }

    public void limparGrafico(javafx.event.ActionEvent actionEvent) {
        GRAFICO_DISPERSAO.limparSerie(GRAFICO_DISPERSAO.sTeste);
    }


    public void testarRna() {
        try {
            limparGrafico(null);
            Tarefas.iniciarTeste();
        } catch (ExceptionPlanejada exceptionPlanejada) {
            Utilidade.avisoExceptionPlanejada(exceptionPlanejada);
        } catch (Exception e) {
            e.printStackTrace();
            Janela.exceptionDialog(e);
        }
    }


    @FXML
    public void gerarDadosTreino() {
        //TODO: Implementar
         try {
            File file = Janela.salvarArquivo("Arquivo com dados de treinamento.", Main.getMain().getStagePrincipal(), Recursos.EXTENSION_FILTER_DADOS);
            if (file == null) {
                return;
            }
            double  errIni =  NumberFormat.getInstance().parse(erroInicial.getText().trim()).doubleValue();// Double.parseDouble(erroInicial.getText().trim());
            double  errFim = NumberFormat.getInstance().parse(erroFinal.getText().trim()).doubleValue();

            new ExportaTexto().geraDadosTeste(file,errIni,errFim);
            Notifications.create()
                    .darkStyle()
                    .position(Pos.TOP_RIGHT)
                    .hideAfter(Duration.seconds(4))
                    .title("Operação concluída !")
                    .text("\"A exportação do novo conjunto de teste concluída.")
                    .showInformation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ValoresDisplay.obsMostrarGrafico.addListener((observable, oldValue, newValue) -> {
            if (observable.getValue().booleanValue()) {
                ((ImageView) btnMostraGraph.getGraphic()).setImage(Recursos.GRAFICO_ON);
            } else {
                ((ImageView) btnMostraGraph.getGraphic()).setImage(Recursos.GRAFICO_OFF);
            }
        });

        tabTeste.setContent(GRAFICO_DISPERSAO.getScatterChart());

        btnTestar.setOnAction(event -> testarRna());
        btnGerarConjTreino.setOnAction(event -> gerarDadosTreino());
        btnCarregarDadosTeste.setOnAction(event -> Utilidade.carregarDadosTeste());
        btnCarregarDadosTeste.setOnDragOver(event -> Utilidade.handleDragOver(event));
        btnCarregarDadosTeste.setOnDragDropped(event -> Utilidade.dragDropDadosTeste(event));

        erroMaximo.textProperty().bind(Bindings.format("%.5f %%", ValoresDisplay.obsTesteErroMaximo));
        erroMinimo.textProperty().bind(Bindings.format("%.5f %%", ValoresDisplay.obsTesteErroMinimo));
        erroMedio.textProperty().bind(Bindings.format("%.5f %%", ValoresDisplay.obsTesteErroMedio));
        tempoTeste.textProperty().bind(Bindings.format("%.5f s", ValoresDisplay.obsTesteTempoDeTeste));
        numeroAmostras.textProperty().bind(Bindings.format("%d un", ValoresDisplay.obsTesteNumeroAmostras));
        erroInicial.setTextFormatter(new TextFormatter<>(new FormatStringConverter<>(NumberFormat.getInstance())));
        erroFinal.setTextFormatter(new TextFormatter<>(new FormatStringConverter<>(NumberFormat.getInstance())));

        ValidationSupport validationSupport = new ValidationSupport();
        Validator<String> stringValidator = (control, s) -> {
            boolean condition = false;
            String sIni = erroInicial.getText();
            String sFim = erroFinal.getText();
            if (sIni != null && sFim != null) {
                try {
                    NumberFormat.getInstance().parse(s);
                } catch (ParseException e) {
                    condition = true;
                }
            } else {
                condition = true;
            }
           // return ValidationResult.fromMessageIf(control, "Não é um percentual válido. Ex: 12.5% ou 1,000.00%", Severity.ERROR, condition);
            return ValidationResult.fromMessageIf(control, "Não é um valor válido.", Severity.ERROR, condition);
        };
        Validator<String> maiorQueMenor = (control, s) -> {
            boolean condition = false;
            try {
                String sIni = erroInicial.getText();
                String sFim = erroFinal.getText();
                if (sIni != null && sFim != null) {
                    double valueInicial = NumberFormat.getInstance().parse(sIni).doubleValue();
                    double valueFinal = NumberFormat.getInstance().parse(sFim).doubleValue();
                    if (valueInicial > valueFinal) {
                        condition = true;
                    }
                }
            } catch (Exception e) {
                condition = true;
            }
            return ValidationResult.fromMessageIf(control, "O valor inicial deve ser igual ou menor que o valor final.", Severity.ERROR, condition);
        };

        validationSupport.registerValidator(erroInicial, true, stringValidator);
        validationSupport.registerValidator(erroInicial, true, maiorQueMenor);

        validationSupport.registerValidator(erroFinal, true, maiorQueMenor);
        validationSupport.registerValidator(erroFinal, true, stringValidator);
    }
}
