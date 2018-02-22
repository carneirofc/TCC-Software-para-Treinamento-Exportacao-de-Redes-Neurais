/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import main.utils.ExceptionPlanejada;
import main.utils.Utilidade;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;


/**
 * @author claud
 */
public class Recursos {

    public static final String HIBERNATE_CFG = "hibernate/hibernate.cfg.xml";
    public static final String CHARSET_PADRAO = "UTF-8";

    public static final String FXML_PRINCIPAL = "fxml/Principal.fxml";
    public static final String FXML_OPCOES = "fxml/Opcoes.fxml";
    public static final String FXML_TESTE = "fxml/Teste.fxml";

    public static final String TITULO_APLICATIVO = "Sistema de Treinamento de RNAs";

    //    public static final String ICONE_JAVAFX = "img/cefet.png";
    public static final String ICONE_JAVAFX = "img/zerg.png";
    //   public static final String ICONE_JAVAFX = "img/zergicon.png";
    // public static final String STYLE_SHEET = "css/Estilo.css";
    // public static final String STYLE_SHEET = "css/Estilo-3.css";
    public static final String STYLE_SHEET = "css/Dark-Theme.css";

    //public static final Image LIMPAR_TEXTO = new Image("img/limpar-texto.png");
    //public static final Image LIMPAR_GRAFICO = new Image("img/limpar-grafico.png");
    public static final Image PLAY = new Image("img/play.png");
    //public static final Image PAUSE = new Image("img/pause.png");
    public static final Image TEXTO_ON = new Image("img/texto-on.png");
    public static final Image TEXTO_OFF = new Image("img/texto-off.png");
    public static final Image STOP = new Image("img/stop.png");
    public static final Image GRAFICO_ON = new Image("img/grafico-on.png");
    public static final Image GRAFICO_OFF = new Image("img/grafico-off.png");
    public static final String TEXTO_SOBRE = "\tAplicativo para treinamento, validação e exportação de redes neurais artificiais MLP.\n" +
            "Visa possiblitar a rápida implementação de RNAs, abstraindo etapas relativamente trabalhosas do processo de treinamento,\n" +
            "validação e programação propriamente dita. O aplicativo é especialmente útil em processos de prototipagem, simulações em\n" +
            "softwares de terceiros e implementações em microcontroladores.\n" +
            "\nO autor não se responsabiliza por possíveis danos, diretos ou indiretos, devido ao uso do aplicativo." +
            "\n\nautor  : Cláudio Ferreira Carrneiro" +
            "\ncontato: claudiofcarneiro@hotmail.com";
    public static Reader CONFIG_GERAL_DEFAULT;

    static {
        try {
            CONFIG_GERAL_DEFAULT = new InputStreamReader(ClassLoader.getSystemResourceAsStream("main/config/config-default.json"), CHARSET_PADRAO);
        } catch (Exception e) {
            e.printStackTrace();
            CONFIG_GERAL_DEFAULT = null;
        }
    }

    public static final FileChooser.ExtensionFilter EXTENSION_FILTER_OUT = new FileChooser.ExtensionFilter("Arquivo .out", "*.out");
    public static final FileChooser.ExtensionFilter EXTENSION_FILTER_C = new FileChooser.ExtensionFilter("Código em C (*.c)", "*.c");
    public static final FileChooser.ExtensionFilter EXTENSION_FILTER_TXT = new FileChooser.ExtensionFilter("Arquivo de Texto (*.txt)", "*.txt");
    public static final FileChooser.ExtensionFilter EXTENSION_FILTER_CSV = new FileChooser.ExtensionFilter("Arquivo .csv", "*.csv");
    public static final FileChooser.ExtensionFilter EXTENSION_FILTER_PROPERTIES = new FileChooser.ExtensionFilter("Arquivo de Propriedades (*.properties)", "*.properties", "*.txt");
    public static final FileChooser.ExtensionFilter EXTENSION_FILTER_JSON = new FileChooser.ExtensionFilter("Arquivo Json", "*.json");
    public static final FileChooser.ExtensionFilter EXTENSION_FILTER_DADOS = new FileChooser.ExtensionFilter("Arquivo de Dados", "*.out", "*.txt", "*.csv");
}
