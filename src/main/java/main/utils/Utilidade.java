package main.utils;

import ann.controller.RnaController;
import com.google.gson.Gson;
import data.txt.read.ManipuladorTxt;
import javafx.application.Platform;
import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Main;
import main.Recursos;
import main.gui.Janela;
import org.controlsfx.control.Notifications;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Classe de utilizades
 */
public class Utilidade {
    public static String getJson(Object o) {
        Gson gson = new Gson();
        return gson.toJson(o);
    }

    /**
     * Retorna uma a representação exadecimal de uma hash MD5 da {@link String }informada
     *
     * @param palavra String as ser transformada em hash.
     * @return Hash MD5 como String
     */
    public static String getStringMD5(String palavra) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        byte[] bytesOfMessage = palavra.getBytes("UTF-8");

        MessageDigest md = MessageDigest.getInstance("MD5");

        byte[] thedigest = md.digest(bytesOfMessage);

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < thedigest.length; i++) {
            if ((0xff & thedigest[i]) < 0x10) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(0xff & thedigest[i]));
        }
        String md5 = sb.toString();

        System.out.println(md5);
        return md5;
    }

    /**
     * Encripta um texto utilizando o algoritmo AES e retorna a representação em byte[]
     *
     * @param key  Chave da criptografia
     * @param text texto a ser criptografado
     * @return texto criptografado em byte[]
     */
    public static byte[] encrypt(String text, String key) throws Exception {
        Key aesKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        return encrypted;
    }

    /**
     * Resolve determinado texto criptografado e retorna seu conteúdo como String
     *
     * @param bb  conteúdo criptografado
     * @param key chave da criptografica
     * @return Conteúdo descriptografado
     */
    public static String decrypt(byte[] bb, String key) throws Exception {
        Key aesKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        String decrypted = new String(cipher.doFinal(bb), "UTF-8");
        return decrypted;
    }

    public static void setIcon(@NotNull Stage stage) {
        stage.getIcons().add(new Image(Recursos.ICONE_JAVAFX));
    }

    public static void addFilters(@NotNull FileChooser fileChooser, @Nullable Object filters) {
        if (filters != null) {
            if (filters instanceof List)
                fileChooser.getExtensionFilters().addAll((List) filters);
            if (filters instanceof FileChooser.ExtensionFilter) {
                fileChooser.getExtensionFilters().add((FileChooser.ExtensionFilter) filters);
            }
        }
    }

    public static String getStringSeparadorEspaco(@NotNull String s) {
        return s.trim()
                .replaceAll(",|;|\\t", " ")
                .replaceAll("\\s{2,}", " ");
    }

    /**
     * Retorna uma list contendo os {@link Integer} separados por algo...
     */
    public static List<Integer> getInt(String line) {
        String[] aux2;
        List<Integer> auxI = new ArrayList<>();
        if (line != null) {
            aux2 = getStringSeparadorEspaco(line).split(" ");
            for (String anAux2 : aux2) {
                auxI.add(Integer.parseInt(anAux2));
            }
        }
        return auxI;
    }

    /**
     * Retorna o erro normalizado aka  de 1 a 0
     */
    public static double calErroNormalizado(double[] targetVals) {
        double sumTot = 0;
        for (int i = 0; i < targetVals.length; i++) {
            sumTot += Math.abs(targetVals[i]);
        }
        sumTot /= targetVals.length;
        return (RnaController.getRna().calcIteErro(targetVals) / sumTot);
    }

    @NotNull
    public static Tooltip getTooltip(@NotNull ObservableStringValue stringValue) {
        Tooltip tooltip = new Tooltip();
        tooltip.textProperty().bind(stringValue);
        return tooltip;
    }


    /**
     * Retorna uma list com os arquivos que são de texto
     */
    public static List<File> tratamentoArquivoTexto(@NotNull List<File> fs) {
        List<File> files = new ArrayList<>();
        for (File f : fs) {
            if (f.getName().endsWith(".txt") || f.getName().endsWith(".csv") || f.getName().endsWith(".out")) {
                files.add(f);
            }
        }
        return files;
    }

    public static void carregarDadosTeste(ActionEvent event) {
        try {
            List<File> file = Janela.abrirMultiplosArquivosGenerico("Arquivo de Texto Contendo o Conjunto de Teste", Main.getMain().getStagePrincipal(), Arrays.asList(Recursos.EXTENSION_FILTER_DADOS, Recursos.EXTENSION_FILTER_TXT, Recursos.EXTENSION_FILTER_CSV, Recursos.EXTENSION_FILTER_OUT));
            if (file == null) {
                return;
            }
            ManipuladorTxt.carregarDadosTeste(file);
            Platform.runLater(() -> Notifications.create()
                    .darkStyle()
                    .position(Pos.TOP_RIGHT)
                    .hideAfter(Duration.seconds(4))
                    .title("Operação concluída !")
                    .text("Os dados de TESTE foram carregados.")
                    .showInformation());
        } catch (ExceptionPlanejada exceptionPlanejada) {
            Utilidade.avisoExceptionPlanejada(exceptionPlanejada);

        } catch (Exception e) {
            e.printStackTrace();
            Janela.exceptionDialog(e);
        }
    }

    public static void dragDropDadosTreino(DragEvent dragEvent) {
        try {
            ManipuladorTxt.carregarDadosTreino(Utilidade.tratamentoArquivoTexto(dragEvent.getDragboard().getFiles()));
            Platform.runLater(() -> Notifications.create()
                    .darkStyle()
                    .position(Pos.TOP_RIGHT)
                    .hideAfter(Duration.seconds(4))
                    .title("Operação concluída !")
                    .text("Os dados de TREINO foram carregados.")
                    .showInformation());
        } catch (ExceptionPlanejada exceptionPlanejada) {
            Utilidade.avisoExceptionPlanejada(exceptionPlanejada);

        } catch (Exception e) {
            e.printStackTrace();
            Janela.exceptionDialog(e);
        }
    }

    public static void dragDropDadosTeste(DragEvent dragEvent) {
        try {
            ManipuladorTxt.carregarDadosTeste(Utilidade.tratamentoArquivoTexto(dragEvent.getDragboard().getFiles()));
            notification("Operação concluída !","Dados de Teste Carregados.");
            //   Janela.dialogo("Operação concluída !", "Os dados de TESTE foram carregados", "Dados carregados com sucesso!", Alert.AlertType.INFORMATION);

        } catch (ExceptionPlanejada exceptionPlanejada) {
            Utilidade.avisoExceptionPlanejada(exceptionPlanejada);
        } catch (Exception e) {
            e.printStackTrace();
           // Janela.exceptionDialog(e);
        }
    }


    /**
     * Carrega o conjunto de dados para treinamento
     */
    public static void carregarDadosTreino() {
        try {
            List<File> file = Janela.abrirMultiplosArquivosGenerico("Arquivo de Texto Contendo o Conjunto de Treinamento", Main.getMain().getStagePrincipal(), Arrays.asList(Recursos.EXTENSION_FILTER_DADOS, Recursos.EXTENSION_FILTER_CSV, Recursos.EXTENSION_FILTER_TXT, Recursos.EXTENSION_FILTER_OUT));
            if (file == null) {
                return;
            }
            ManipuladorTxt.carregarDadosTreino(file);
            Platform.runLater(() ->
                    Notifications.create()
                            .darkStyle()
                            .position(Pos.TOP_RIGHT)
                            .hideAfter(Duration.seconds(4))
                            .title("Operação concluída !")
                            .text("Os dados de TREINO foram carregados")
                            .showInformation());
        } catch (ExceptionPlanejada exceptionPlanejada) {
            Utilidade.avisoExceptionPlanejada(exceptionPlanejada);

        } catch (Exception e) {
            e.printStackTrace();
            Janela.exceptionDialog(e);
        }
    }

    /**
     * Método para habilitar o drag over ...
     */
    public static void handleDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
    }

    public static void notification(String texto) {
        notification(texto, texto);

    }

    public static void notification(String texto, String titulo) {
        Platform.runLater(() -> Notifications.create()
                .darkStyle()
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(4))
                .title(titulo)
                .text(texto)
                .showInformation());
    }

    public static void aindaNaoImplementado() {
        Platform.runLater(() -> Notifications.create()
                .darkStyle()
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(4))
                .title("Ainda não foi implementado ...")
                .showInformation());
    }

    public static void avisoExceptionPlanejada(ExceptionPlanejada exceptionPlanejada) {
        Platform.runLater(() -> Notifications.create()
                .darkStyle()
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(4))
                .title(exceptionPlanejada.getTitle())
                .text(exceptionPlanejada.getMessage())
                .showError());
    }
}
