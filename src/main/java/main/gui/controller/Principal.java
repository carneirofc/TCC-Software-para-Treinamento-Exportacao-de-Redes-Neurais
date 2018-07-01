package main.gui.controller;

import ann.controller.RnaController;
import ann.funcao_ativacao.FuncaoTipo;
import ann.geral.FuncaoDecaimento;
import ann.geral.Topologia;
import com.jfoenix.controls.*;
import data.properties.read.ManipuladorProperties;
import data.txt.write.Ccode;
import data.txt.write.ExportaTexto;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.util.StringConverter;
import main.Ctrl;
import main.Main;
import main.Recursos;
import main.Tarefas;
import main.config.ConfigGeral;
import main.config.ConfigGeralDTO;
import main.gui.Janela;
import main.gui.ValoresDisplay;
import main.gui.grafico.GraficoLinha;
import main.utils.Converter;
import main.utils.ExceptionPlanejada;
import main.utils.Utilidade;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.ResourceBundle;

public class Principal implements Initializable {

    @FXML
    private MenuItem miSobre;
    @FXML
    private MenuItem miFaq;
    public StackPane stackRoot;
    @FXML
    private Label lblTaxaAprendizadoAtual;
    @FXML
    private MenuItem miTreinar;
    @FXML
    private MenuItem miTestar;
    @FXML
    private MenuItem miPropTreino;
    @FXML
    private JFXSlider tSlider;
    @FXML
    private MenuItem miDadosTreino;
    @FXML
    private MenuItem miDadosTeste;
    @FXML
    private MenuItem miFuncC;
    @FXML
    private MenuItem miLogTempoCamada;
    @FXML
    private MenuItem miLogErroEpoca;
    @FXML
    private MenuItem miConfParamRNA;
    @FXML
    private JFXButton btnPlay;
    @FXML
    private JFXButton btnLimpaGraph;
    @FXML
    private JFXButton btnLimpaText;
    @FXML
    private JFXButton btnMostraText;
    @FXML
    private JFXButton btnMostraGraph;
    @FXML
    private JFXButton btnNovaRede;
    @FXML
    private JFXButton btnTestar;
    @FXML
    private Tab tabTreino;
    @FXML
    private JFXTextField tfFeedforward;
    @FXML
    private Label lblEpocaAtual;
    @FXML
    private Label lblErroTreino;
    @FXML
    private Label lblErroTeste;
    @FXML
    private Label lblTempoTreino;
    @FXML
    private JFXTextArea taResultado;
    @FXML
    private Label lblTopologia;
    @FXML
    private Label lblEpocaMax;
    @FXML
    private Label lblErroDesejado;
    @FXML
    private Label lblTermoLinear;
    @FXML
    private Label lblTaxaAprendizado;
    @FXML
    private Label lblMomentum;
    @FXML
    private Label lblFuncaoAtiv;
    @FXML
    private JFXCheckBox cbGerarLogEstado;
    @FXML
    private JFXCheckBox cbDadoTesteParada;
    @FXML
    private Label lblDadosTreino;
    @FXML
    private Label lblDadosTeste;
    @FXML
    private Label lblProperties;
    @FXML
    private Label lblFaixaPeso;
    @FXML
    private Label lblConfig;

    private static final GraficoLinha GRAFICO_LINHA = new GraficoLinha("Desempenho de Treinamento", "Erro RMS Dados Treinamento", "Erro RMS Dados Teste");

    public static GraficoLinha getGraficoLinha() {
        return GRAFICO_LINHA;
    }


    public void logTempoCamada() {
        try {
            File file = Janela.salvarArquivo("Exportar os dados de treino", Main.getMain().getStagePrincipal(), Recursos.EXTENSION_FILTER_TXT);
            //File file = Janela.salvarArquivo("Exportar os dados de treino", SessionUtil.getMain().getStagePrincipal(), Recursos.EXTENSION_FILTER_TXT);
            if (file == null) {
                return;
            }
            if (RnaController.getLogTempoCamada() == null) {
                throw new ExceptionPlanejada("Não existe log para a exportação.");
            }
            Files.copy(new FileInputStream(RnaController.getLogTempoCamada()), file.toPath());
            Utilidade.notification("Exportação concluída.");
        } catch (ExceptionPlanejada exceptionPlanejada) {
            Utilidade.avisoExceptionPlanejada(exceptionPlanejada);
        } catch (Exception e) {
            Janela.exceptionDialog(e);
        }
    }

    public void logErroTreino() {
        try {
            File file = Janela.salvarArquivo("Exportar log erro de treinamento.", Main.getMain().getStagePrincipal(), Recursos.EXTENSION_FILTER_TXT);
            if (file == null) {
                return;
            }
            if (RnaController.getLogErroEpoca() == null) {
                throw new ExceptionPlanejada("Não existe log para a exportação.");
            }
            Files.copy(new FileInputStream(RnaController.getLogErroEpoca()), file.toPath());
            Utilidade.notification("Exportação concluída.");
        } catch (ExceptionPlanejada exceptionPlanejada) {
            Utilidade.avisoExceptionPlanejada(exceptionPlanejada);
        } catch (Exception e) {
            Janela.exceptionDialog(e);
        }
    }

    /**
     * Janela de configurações do sistema
     */
    public void janelaOpcoes(ActionEvent actionEvent) {
        Main.getMain().adicionarPopUp(Recursos.FXML_OPCOES, "Configurações");
    }

    /**
     * Inicia o treinamento da RNA
     */
    public void iniciarTreino(ActionEvent actionEvent) {
        if (!Ctrl.isRnaEmExecucao()) {
            try {
                limparGrafico(null);
                limparTexto(null);
                Tarefas.iniciarTreino();
                // TODO: Atualizar a interface gráfica ...
            } catch (ExceptionPlanejada exceptionPlanejada) {
                Utilidade.avisoExceptionPlanejada(exceptionPlanejada);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.printf("Erro ao iniciar o processo de treino.");
                Janela.exceptionDialog(e);
            }
        } else {
            Janela.dialogo(stackRoot, "Pausar Treinamento", "Deseja Realmente pausar o Treinamento?",
                    o -> {
                        Ctrl.setRnaEmExecucao(false);
                        return null;
                    }, o -> null);
            /*if (!optional.isPresent())
                return;
            if (optional.get() == ButtonType.OK)
                Ctrl.setRnaEmExecucao(false);*/
        }
    }

    /**
     * Cria uma nova rede neural.....
     */
    @FXML
    public void novaRede(ActionEvent event) {
        if (!Ctrl.isRnaEmExecucao()) {
            /*   Optional option =*/
            Janela.dialogo(
                    stackRoot,
                    "Criar uma nova rede neural?",
                    "Deseja realmente apagar as informações relativas à rede neural atual e criar uma nova?.",

                    (o) -> {
                        limparTexto(null);
                        limparGrafico(null);
                        ConfigGeral.getConfigGeralAtual().setRnaAtual(null);
                        return null;
                    },
                    o -> null);
            /*if (!option.isPresent())
                return;
            if (option.get() == ButtonType.OK) {
            }*/
        } else {
            Utilidade.avisoExceptionPlanejada(new ExceptionPlanejada("Um processo de treinamento ainda está em execução."));
        }
    }

    /**
     * Testa   o conjunto de dados de teste carregado
     */
    public void iniciarTeste(ActionEvent actionEvent) {
        Main.getMain().adicionarPopUp(Recursos.FXML_TESTE, "Painel de Testes", true, false, Recursos.ICONE_JAVAFX);
    }


    /**
     * Exporta a RNA já treinada em uma arquivo .c
     */
    public void exportarFuncaoC(ActionEvent actionEvent) {
        try {
            File file = Janela.salvarArquivo("Exportar RNA como uma Função em C", Main.getMain().getStagePrincipal(), Recursos.EXTENSION_FILTER_C);
            if (file == null) {
                return;
            }

            new Ccode().salvarPesosC(file);
            Janela.dialogo(stackRoot, "Operação concluída !", "A exportação em .c fui concluída");
        } catch (ExceptionPlanejada exceptionPlanejada) {
            Utilidade.avisoExceptionPlanejada(exceptionPlanejada);
        } catch (Exception e) {
            Janela.exceptionDialog(e);
        }
    }

    /**
     * Método para habilitar o drag over ...
     */
    public void handleDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
    }


    public void dragDropDadosTeste(DragEvent dragEvent) {
        Utilidade.dragDropDadosTeste(dragEvent);
    }


    /**
     * Carrega o arquivo de propriedades
     */
    public void carregarPropertiesTreino(ActionEvent actionEvent) {
        try {
            File file = Janela.abrirArquivo("Arquivo de Propriedades do Conjunto de Treinamento", Main.getMain().getStagePrincipal(), Recursos.EXTENSION_FILTER_PROPERTIES);
            if (file == null)
                return;
            ManipuladorProperties.loadTrainProperties(file);
        } catch (ExceptionPlanejada exceptionPlanejada) {
            Utilidade.avisoExceptionPlanejada(exceptionPlanejada);

        } catch (Exception e) {
            e.printStackTrace();
            Janela.exceptionDialog(e);
        }
    }

    public void dragDropPropDados(DragEvent dragEvent) {
        List<File> files = dragEvent.getDragboard().getFiles();
        if (files.size() > 1) {
            Notifications.create()
                    .darkStyle()
                    .position(Pos.TOP_RIGHT)
                    .hideAfter(Duration.seconds(4))
                    .title("Erro !")
                    .text("Apenas 1 arquivo por vez ....")
                    .showError();
            return;
        }
        File file = files.get(0);
        if (file.getName().endsWith(".properties") || file.getName().endsWith(".txt")) {
            try {
                ManipuladorProperties.loadTrainProperties(file);
            } catch (ExceptionPlanejada exceptionPlanejada) {
                Utilidade.avisoExceptionPlanejada(exceptionPlanejada);
            } catch (Exception e) {
                e.printStackTrace();
                Janela.exceptionDialog(e);
            }
        }
    }

    /**
     * Carrega o conjunto de dados para treinamento
     */
    public void carregarDadosTreino(ActionEvent actionEvent) {
        Utilidade.carregarDadosTreino();
    }

    public void dragDropDadosTreino(DragEvent dragEvent) {
        Utilidade.dragDropDadosTreino(dragEvent);
    }


    /**
     * Carrega o conjunto de dados para teste
     */
    public void carregarDadosTeste(ActionEvent actionEvent) {
        Utilidade.carregarDadosTeste(actionEvent);
    }

    /**
     * Carrega o .json contendo os arquivos de configuração e estado da rede *
     */
    public void carregarConfig(ActionEvent actionEvent) {
        try {
            File file = Janela.abrirArquivo("Arquivo de Propriedades de Configurações da RNA", Main.getMain().getStagePrincipal(), Recursos.EXTENSION_FILTER_BIN);
            if (file == null)
                return;
            //ConfigGeral.loadConfigGeral(file);
            ConfigGeral.deserializeConfigGerialDTO(file.getAbsolutePath());
            Notifications.create()
                    .darkStyle()
                    .position(Pos.TOP_RIGHT)
                    .hideAfter(Duration.seconds(4))
                    .title("Operação concluída !")
                    .text("Arquivos de configuração carregados.")
                    .showInformation();
       // } catch (ExceptionPlanejada exceptionPlanejada) {
        //    Utilidade.avisoExceptionPlanejada(exceptionPlanejada);
        } catch (Exception e) {
            e.printStackTrace();
            Janela.exceptionDialog(e);
        }
    }

    public void dragDropConfig(DragEvent dragEvent) {
        try {
            List<File> files = dragEvent.getDragboard().getFiles();
            if (files.size() > 1) {
                Notifications.create()
                        .darkStyle()
                        .position(Pos.TOP_RIGHT)
                        .hideAfter(Duration.seconds(4))
                        .title("Erro !")
                        .text("Apenas 1 arquivo por vez ....")
                        .showError();
                return;
            }
            File file = files.get(0);
            if (file.getName().endsWith(".json")) {
                ConfigGeral.loadConfigGeral(file);
            }
        } catch (ExceptionPlanejada exceptionPlanejada) {
            Utilidade.avisoExceptionPlanejada(exceptionPlanejada);

        } catch (Exception e) {
            e.printStackTrace();
            Janela.exceptionDialog(e);
        }
    }

    public void exportarConfig(ActionEvent actionEvent) {
        File file = Janela.salvarArquivo("Exportar as Configurações Atuais do Sistema", Main.getMain().getStagePrincipal(), Recursos.EXTENSION_FILTER_BIN);
        if (file == null) {
            return;
        }
        new ExportaTexto().salvarConfigDadosJson(file);
    }


    public void limparGrafico(ActionEvent actionEvent) {
        GRAFICO_LINHA.limparSerie(GRAFICO_LINHA.sTreino);
        GRAFICO_LINHA.limparSerie(GRAFICO_LINHA.sTeste);
    }

    public void limparTexto(ActionEvent actionEvent) {
        ((SimpleDoubleProperty) ValoresDisplay.obsTreinoTempoDeTreinamento).set(0);
        ((SimpleLongProperty) ValoresDisplay.obsEpocaAtual).set(0);
        ((SimpleDoubleProperty) ValoresDisplay.obsTreinoErroTeste).set(0);
        ((SimpleDoubleProperty) ValoresDisplay.obsTreinoErro).set(0);
    }

    public void mostrarTexto(ActionEvent actionEvent) {
        Ctrl.setGuiMostrarTexto(!Ctrl.isGuiMostrarTexto());
    }

    public void mostrarGrafico(ActionEvent actionEvent) {
        Ctrl.setGuiMostrarGrafico(!Ctrl.isGuiMostrarGrafico());
    }

    public void feedforwad(ActionEvent actionEvent) {
        try {
            String s = tfFeedforward.getText();
            if (s == null)
                return;
            RnaController.netFeedForwardManual(Utilidade.getStringSeparadorEspaco(s));
        } catch (ExceptionPlanejada exceptionPlanejada) {
            Utilidade.avisoExceptionPlanejada(exceptionPlanejada);

        } catch (Exception e) {
            e.printStackTrace();
            Janela.exceptionDialog(e);
        }
    }

    @FXML
    void sair(ActionEvent event) {
        Main.getMain().getStagePrincipal().fireEvent(new WindowEvent(Main.getMain().getStagePrincipal(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configuração inicial da RNA
        ConfigGeralDTO configGeralDTO = new ConfigGeralDTO(
                new int[]{5, 3},
                50000,
                0.0001,
                0.01,
                0.01,
                0.00001,
                0.7,
                FuncaoTipo.TANGENTE_HIPERBOLICA,
                FuncaoTipo.LINEAR,
                null,
                -1.0,
                1.0,
                new double[]{0},
                new double[]{0},
                new double[]{0},
                new double[]{0},
                0,
                0,
                FuncaoDecaimento.NENHUM
        );
        ConfigGeral.setConfigGeralAtual(
                configGeralDTO
        );
        Topologia.configuraTolopogia();

        tabTreino.setContent(GRAFICO_LINHA.getLineChart());

        lblEpocaAtual.textProperty().bind(Bindings.format("%d", ValoresDisplay.obsEpocaAtual));
        lblTempoTreino.textProperty().bind(Bindings.format("%f s", ValoresDisplay.obsTreinoTempoDeTreinamento));
        lblErroTreino.textProperty().bind(Bindings.format("%e", ValoresDisplay.obsTreinoErro));
        lblErroTeste.textProperty().bind(Bindings.format("%e", ValoresDisplay.obsTreinoErroTeste));
        lblEpocaMax.textProperty().bind(Bindings.format("%d", ValoresDisplay.obsEpocaMaxima));
        lblErroDesejado.textProperty().bind(Bindings.format("%e", ValoresDisplay.obsErroDesejado));
        lblMomentum.textProperty().bind(Bindings.format("%e", ValoresDisplay.obsMomentum));
        lblTermoLinear.textProperty().bind(Bindings.format("%e", ValoresDisplay.obsTermoLinear));
        lblTaxaAprendizado.textProperty().bind(Bindings.format("%e", ValoresDisplay.obsTaxaAprendizado));
        lblTaxaAprendizadoAtual.textProperty().bind(Bindings.format("%e", ValoresDisplay.obsTaxaAprendizadoAtual));
        lblFaixaPeso.textProperty().bind(Bindings.format("%f", ValoresDisplay.obsFaixaPesoInicial));

        lblTopologia.textProperty().bind(ValoresDisplay.obsTopologia);
        lblFuncaoAtiv.textProperty().bind(ValoresDisplay.obsFuncaoAtivacaoInternaAtual);
        lblDadosTeste.textProperty().bind(ValoresDisplay.obsDadosTesteCaminho);
        lblDadosTreino.textProperty().bind(ValoresDisplay.obsDadosTreinoCaminho);
        lblProperties.textProperty().bind(ValoresDisplay.obsPropertiesCaminho);
        lblConfig.textProperty().bind(ValoresDisplay.obsPropertiesConfigCaminho);
        btnNovaRede.disableProperty().bind(ValoresDisplay.obsRnaEmExecucao);
        //    cbGerarLogEstado.selectedProperty().bind(ValoresDisplay.obsGerarLog);
        cbGerarLogEstado.selectedProperty().bind(Bindings.or(Ctrl.logErroEpocaProperty(), Ctrl.logTempoTreinoCamadaProperty()));
        cbDadoTesteParada.selectedProperty().bindBidirectional(Ctrl.usarDadosTesteTreinoProperty());
        cbDadoTesteParada.disableProperty().bind(ValoresDisplay.obsDadoTesteDisable);
        btnTestar.disableProperty().bind(ValoresDisplay.obsRnaEmExecucao);
        tSlider.disableProperty().bind(ValoresDisplay.obsRnaEmExecucao);

        taResultado.textProperty().bind(ValoresDisplay.obsFeedforwardResultado);
        taResultado.setEditable(false);

        ValoresDisplay.obsMostrarGrafico.addListener((observable, oldValue, newValue) -> {
            if (observable.getValue().booleanValue()) {
                ((ImageView) btnMostraGraph.getGraphic()).setImage(Recursos.GRAFICO_ON);
            } else {
                ((ImageView) btnMostraGraph.getGraphic()).setImage(Recursos.GRAFICO_OFF);
            }
        });
        ValoresDisplay.obsMostrarTexto.addListener((observable, oldValue, newValue) -> {
            if (observable.getValue().booleanValue()) {
                ((ImageView) btnMostraText.getGraphic()).setImage(Recursos.TEXTO_ON);
            } else {
                ((ImageView) btnMostraText.getGraphic()).setImage(Recursos.TEXTO_OFF);
            }
        });
        ValoresDisplay.obsRnaEmExecucao.addListener((observable, oldValue, newValue) -> {
            if (observable.getValue().booleanValue()) {
                ((ImageView) btnPlay.getGraphic()).setImage(Recursos.STOP);
            } else {
                ((ImageView) btnPlay.getGraphic()).setImage(Recursos.PLAY);
            }
        });

        btnLimpaGraph.setTooltip(new Tooltip("Limpa o gráfico."));
        btnLimpaText.setTooltip(new Tooltip("Limpa os textos na tela."));
        btnMostraGraph.setTooltip(new Tooltip("Habilita a atualização do gráfico. Desabilitar aumenta a performance."));
        btnMostraText.setTooltip(new Tooltip("Habilita a atualização de textos. Desabilitar aumenta a performance."));
        btnPlay.setTooltip(new Tooltip("Inicia/Pausa o treinamento."));
        cbDadoTesteParada.setTooltip(new Tooltip("Habilita o uso dosLogTempo dados de teste no processo de treinamento."));

        lblDadosTreino.setTooltip(Utilidade.getTooltip(ValoresDisplay.obsDadosTreinoCaminho));
        lblDadosTeste.setTooltip(Utilidade.getTooltip(ValoresDisplay.obsDadosTesteCaminho));
        lblProperties.setTooltip(Utilidade.getTooltip(ValoresDisplay.obsPropertiesCaminho));
        lblConfig.setTooltip(Utilidade.getTooltip(ValoresDisplay.obsPropertiesConfigCaminho));
        lblFuncaoAtiv.setTooltip(Utilidade.getTooltip(ValoresDisplay.obsFuncaoAtivacaoInternaAtual));
        tSlider.valueProperty().bindBidirectional(Ctrl.guiTempoAtualizacaoSegundosProperty());
        Tooltip tooltip = new Tooltip();
        tooltip.setShowDelay(Duration.seconds(1));
        tooltip.textProperty().bind(Bindings.format("%.4f s", tSlider.valueProperty()));
        tSlider.setTooltip(tooltip);

        miSobre.setOnAction(event -> Janela.sobre());
        miFaq.setOnAction(event -> Utilidade.aindaNaoImplementado());

        miLogErroEpoca.setOnAction(event -> Utilidade.aindaNaoImplementado());
        miLogTempoCamada.setOnAction(event -> logTempoCamada());
        miLogErroEpoca.setOnAction(event -> logErroTreino());

        tfFeedforward.setTextFormatter(new TextFormatter<>(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                if (object == null)
                    return "";
                try {
                    return Converter.doubleVectorToString(Converter.stringToDoubleVector(object));
                } catch (ExceptionPlanejada exceptionPlanejada) {
                    Utilidade.avisoExceptionPlanejada(exceptionPlanejada);
                    return "";
                }
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        }));
    }
}

