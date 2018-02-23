package main.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Main;
import main.Recursos;
import main.utils.Utilidade;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class Janela {
    /**
     * //  * @param parent    {@link StackPane} que o diálogo será mostrado. Obrigatório.
     *
     * @param cabecalho conteudo do cabeçalho mostrado
     * @param conteudo  conteudo da mensagem a ser apresentada
     */
    public static Optional dialogo(@NotNull final StackPane stackPane, @NotNull String cabecalho, @NotNull String conteudo, Function ok, Function cancelar) {
        //   Objects.requireNonNull(parent, "O StackPane pai não foi inserido.");
        Objects.requireNonNull(cabecalho, "Type não pode ser null.");
        Objects.requireNonNull(conteudo, "Type não pode ser null.");

        final JFXDialog jfxDialog = new JFXDialog();
        final List<JFXButton> buttons = new ArrayList<>();

        JFXButton btnOK = new JFXButton("Ok");
        btnOK.setPrefWidth(150);
        btnOK.setOnAction(event -> {
            jfxDialog.close();
            if (ok != null) {
                ok.apply(null);
            }
        });
        buttons.add(btnOK);
        if (cancelar != null) {
            JFXButton btnCancelar = new JFXButton("Cancelar");
            btnCancelar.setPrefWidth(150);
            btnCancelar.styleProperty().bind(
                    Bindings.when(btnCancelar.hoverProperty())
                            .then("-fx-background-color: #FC9D9A")
                            .otherwise("-fx-background-color: #FE4365"));
            btnCancelar.setOnAction(event -> {
                jfxDialog.close();
                cancelar.apply(null);
            });
            buttons.add(btnCancelar);
        }
        Text textCabecalho = new Text(cabecalho);
        textCabecalho.setFont(new Font(16));

        Text textConteudo = new Text(conteudo);
        textConteudo.setFont(new Font(14));

        JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(textCabecalho);
        jfxDialogLayout.setBody(textConteudo);
        jfxDialogLayout.setActions(buttons);

        jfxDialog.setDialogContainer(stackPane);
        jfxDialog.setContent(jfxDialogLayout);
        jfxDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);

        jfxDialog.show();
        return null;
    }

    public static void dialogo(@NotNull final StackPane stackPane, @NotNull String cabecalho, @NotNull String conteudo) {
        dialogo(stackPane, cabecalho, conteudo, null, null);
    }

    public static void sobre() {
        final JFXDialog jfxDialog = new JFXDialog();
        final JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();

        final JFXButton jfxButtonOk = new JFXButton("Ok");
        jfxButtonOk.setOnAction(event -> jfxDialog.close());

        final Text heading = new Text("Sobre");
        heading.setFont(Font.font(16));
        final Text texto = new Text(Recursos.TEXTO_SOBRE);
        texto.setFont(Font.font(14));

        jfxDialogLayout.setBody(texto);
        jfxDialogLayout.setHeading(heading);
        jfxDialogLayout.setActions(jfxButtonOk);

        jfxDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        jfxDialog.setContent(jfxDialogLayout);
        jfxDialog.setDialogContainer(Main.getMain().getStagePrincipalController().stackRoot);
        jfxDialog.show();
    }

    /**
     * @param titulo    Titulo do painel
     * @param cabecalho conteudo do cabeçalho mostrado, null retira o cabeçalho
     * @param conteudo  conteudo da mensagem a ser apresentada
     * @param type      o tipo de diálogo quye será exibido
     * @param escolhas  escolhas para a combo box
     */
    /*public static Optional choiceDialog(String titulo, String cabecalho, String conteudo, Alert.AlertType type, List escolhas) {
        if (type == null)
            throw new NullPointerException("Type não pode ser null.");
        if (escolhas == null)
            throw new NullPointerException("Escolhas não pode ser null.");
        if (escolhas.isEmpty())
            throw new NullPointerException("Escolhas não podem estar vazias.");

        ChoiceDialog dialog = new ChoiceDialog<>(escolhas.get(0), escolhas);

        Utilidade.setIcon((Stage) dialog.getDialogPane().getScene().getWindow());

        dialog.setTitle(titulo);
        dialog.setContentText(conteudo);
        dialog.setHeaderText(cabecalho);
        return dialog.showAndWait();
    }
*/
    /**
     * @param titulo    Titulo do painel
     * @param cabecalho conteudo do cabeçalho mostrado, null retira o cabeçalho
     * @param conteudo  conteudo da mensagem a ser apresentada
     */
   /* public static Optional textInputDialog(String titulo, String cabecalho, String conteudo) {
        TextInputDialog dialog = new TextInputDialog();
        Utilidade.setIcon((Stage) dialog.getDialogPane().getScene().getWindow());
        dialog.setTitle(titulo);
        dialog.setContentText(conteudo);
        dialog.setHeaderText(cabecalho);

        return dialog.showAndWait();
    }

*/

    /**
     * Abre uma caixa de diálogo para a exception lançada.
     *
     * @param e a Exception
     */
    public static Optional exceptionDialog(Exception e) {
        if (e == null)
            return Optional.empty();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Uma Exception foi lançada.");
        String header = "Ops ... ";
        if (e.getMessage() != null)
            header.concat(e.getMessage());
        alert.setHeaderText(header);
        alert.setContentText(e.getMessage());

        Utilidade.setIcon((Stage) alert.getDialogPane().getScene().getWindow());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("O stack trace foi:");

        JFXTextArea textArea = new JFXTextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        return alert.showAndWait();
    }

    /**
     * @param filters     lista com as extensões suportadas     ex: List ..; new FileChooser.ExtensionFilter("PNG", "*.png")
     * @param ownerWindow janela pai. Pode ser null
     * @param titulo      titulo do FileChooser
     */
    @Nullable
    private static File abrirArquivoGenerico(String titulo, Stage ownerWindow, Object filters) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(titulo);
            Utilidade.addFilters(fileChooser, filters);
            File file = fileChooser.showOpenDialog(ownerWindow);
            Utilidade.setIcon(ownerWindow);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param filters     lista com as extensões suportadas     ex: List ..; new FileChooser.ExtensionFilter("PNG", "*.png")
     * @param ownerWindow janela pai. Pode ser null
     * @param titulo      titulo do FileChooser
     */
    @Nullable
    public static List<File> abrirMultiplosArquivosGenerico(String titulo, Stage ownerWindow, Object filters) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(titulo);
            Utilidade.addFilters(fileChooser, filters);
            Utilidade.setIcon(ownerWindow);
            return fileChooser.showOpenMultipleDialog(ownerWindow);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param filter      extensão suportada     ex: List ..; new FileChooser.ExtensionFilter("PNG", "*.png") ou null
     * @param ownerWindow janela pai. Pode ser null
     * @param titulo      titulo do FileChooser
     */
    @Nullable
    public static File abrirArquivo(String titulo, Stage ownerWindow, FileChooser.ExtensionFilter filter) {
        return abrirArquivoGenerico(titulo, ownerWindow, filter);
    }

    /**
     * @param filters     lista com as extensões suportadas     ex: List ..; new FileChooser.ExtensionFilter("PNG", "*.png") ou null
     * @param ownerWindow janela pai. Pode ser null
     * @param titulo      titulo do FileChooser
     *//*
    @Nullable
    public static File abrirArquivo(String titulo, Stage ownerWindow, List<FileChooser.ExtensionFilter> filters) {
        return abrirArquivoGenerico(titulo, ownerWindow, filters);
    }
*/
    @Nullable
    private static File salvarArquivoGenerico(String titulo, Stage ownerWindow, Object filters) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(titulo);
            Utilidade.addFilters(fileChooser, filters);
            Utilidade.setIcon(ownerWindow);
            File file = fileChooser.showSaveDialog(ownerWindow);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param filter      extensão    ex:   new FileChooser.ExtensionFilter("PNG", "*.png")
     * @param ownerWindow janela pai. Pode ser null
     * @param titulo      titulo do FileChooser
     */
    @Nullable
    public static File salvarArquivo(String titulo, Stage ownerWindow, FileChooser.ExtensionFilter filter) {
        return salvarArquivoGenerico(titulo, ownerWindow, filter);
    }


    /**
     * @param filterList  lista com as extensões suportadas     ex: List ..; new FileChooser.ExtensionFilter("PNG", "*.png")
     * @param ownerWindow janela pai. Pode ser null
     * @param titulo      titulo do FileChooser
     *//*
    @Nullable
    public static File salvarArquivo(String titulo, Stage ownerWindow, List<FileChooser.ExtensionFilter> filterList) {
        return salvarArquivoGenerico(titulo, ownerWindow, filterList);
    }*/

    /**
     * @param ownerWindow janela pai. Pode ser null
     * @param titulo      titulo do FileChooser
     * @return retorna um {@link File} caso o arquivo escolhido for um diretório. Null para as demais possibilidades.
     *//*
    @Nullable
    public static File abrirDiretorio(String titulo, Stage ownerWindow) {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle(titulo);
            Utilidade.setIcon(ownerWindow);
            File file = directoryChooser.showDialog(ownerWindow);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/
}
