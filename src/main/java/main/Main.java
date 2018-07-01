package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.gui.Janela;
import main.gui.ValoresDisplay;
import main.gui.controller.Opcoes;
import main.gui.controller.Principal;
import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.util.Locale;

public class Main extends Application {

    private static Main main = null;

    private Scene scene = null;
    private Stage stagePrincipal = null;
    private Principal stagePrincipalController = null;

    @Contract(pure = true)
    public static Main getMain() {
        return main;
    }

    public Principal getStagePrincipalController() {
        return stagePrincipalController;
    }

    public Main() {
        Main.main = this;
    }


    /**
     * @return Retorna a instancia atual do SessionUtil
     * @throws NullPointerException caso main for null
     *//*
    private static SessionUtil getInstance() {
        if (main == null) {
            throw new NullPointerException(SessionUtil.class.getName().concat(" não foi inicializado."));
        }
        return main;
    }*/
    public Stage getStagePrincipal() {
        return stagePrincipal;
    }

    /**
     * @param localFxml local do arquivo .fxml
     *//*
    public void mudarScene(String localFxml) {
        try {
            Parent root = FXMLLoader.load(ClassLoader.getSystemClassLoader().getResource(localFxml));
            if (scene == null) {
                scene = new Scene(root);
            } else {
                scene.setRoot(root);
            }
            scene.getStylesheets().add(Recursos.STYLE_SHEET);
        } catch (IOException e) {
            System.err.println("Erro ao carregar a Scene. ".concat(e.getMessage()));
        }
    }*/

    /**
     * Cria um popup
     */
    public void adicionarPopUp(String localFxml, String tituloJanela) {
        adicionarPopUp(localFxml, tituloJanela, true, true, Recursos.ICONE_JAVAFX);
    }

    public static void main(String[] args) {
        //   FuncaoTipo ad = FuncaoTipo.TANGENTE_HIPERBOLICA;
        Locale.setDefault(Locale.UK);
        launch(args);
    }

    /**
     * Cria um popup
     *
     * @param iconeJanela  local do incone usado na janela
     * @param isModal      é modal?
     * @param isResizable  é resizable?
     * @param tituloJanela titulo da janela
     * @param localFxml    local do arquivo .fxml
     */
    public void adicionarPopUp(String localFxml, String tituloJanela, boolean isModal, boolean isResizable, String iconeJanela) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource(localFxml));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Recursos.STYLE_SHEET);
            Stage stage = new Stage();
            stage.setScene(scene);
            if (isModal)
                stage.initModality(Modality.APPLICATION_MODAL);
            else
                stage.initModality(Modality.NONE);
            stage.setResizable(isResizable);
            stage.setTitle(tituloJanela);
            stage.getIcons().add(new Image(iconeJanela));
            if (fxmlLoader.getController() instanceof Opcoes)
                ((Opcoes) fxmlLoader.getController()).setStage(stage);
            stage.sizeToScene();
            stage.show();
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar a Scene".concat(e.getMessage()));
        }
    }

    @Override
    public void start(Stage primaryStage) {
        //    primaryStage.initStyle(StageStyle.TRANSPARENT);
        stagePrincipal = primaryStage;
        ValoresDisplay.inivializaLists();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource(Recursos.FXML_PRINCIPAL));
            Parent root = fxmlLoader.load();
            stagePrincipalController = fxmlLoader.getController();
            scene = new Scene(root);
            stagePrincipal.setScene(scene);
            stagePrincipal.getIcons().add(new Image(Recursos.ICONE_JAVAFX));
            stagePrincipal.setTitle(Recursos.TITULO_APLICATIVO);
            stagePrincipal.setOnCloseRequest(event -> {
                event.consume();
                Janela.dialogo(stagePrincipalController.stackRoot, "Fechar o Programa", "Deseja realmente fechar o programa?", (o) -> {
                    System.exit(0);
                    return null;
                }, o -> null);
            });
            scene.getStylesheets().add(Recursos.STYLE_SHEET);
            stagePrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

