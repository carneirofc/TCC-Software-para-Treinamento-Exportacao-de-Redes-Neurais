package data.txt.write;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import javafx.stage.FileChooser;
import main.Ctrl;
import main.utils.ExceptionPlanejada;

/**
 * @author Claudio
 */
public class TxtWriter {

    private FileWriter fileWriter;
    private PrintWriter printWriter;


    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public void finaliza() {
        if (fileWriter != null) {
            try {
                printWriter.close();
                fileWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(TxtWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Instancia o filewriter
     *
     * @param extensao extensao do arquivo a ser salvo.
     * @param file     arquivo a ser salvo
     */
    public boolean criaWriter(File file, FileChooser.ExtensionFilter extensao) throws Exception {
        if (file == null)
            throw new ExceptionPlanejada("Arquivo não existe.");
        fileWriter = new FileWriter(file.getCanonicalPath());
        printWriter = new PrintWriter(fileWriter);
        return true;
    }

    /**
     * @param extensao Ex: ".c"
     */
    public boolean selectDir(String msg, String extensao) throws IOException {
        JOptionPane.showMessageDialog(null, msg, "Esolha o diretório.", JOptionPane.INFORMATION_MESSAGE);
        try {
            JFileChooser jf;
            if (Ctrl.getDirPadrao() != null) {
                jf = new JFileChooser(Ctrl.getDirPadrao());
            } else {
                jf = new JFileChooser();
            }
            jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int res = jf.showSaveDialog(null);
            if (JFileChooser.APPROVE_OPTION == res) {
                Ctrl.setDirPadrao(jf.getSelectedFile().getCanonicalPath());
                String dir = Ctrl.getDirPadrao();
                if (dir != null) {
                    String nome = JOptionPane.showInputDialog("Nome do arquivo");
                    fileWriter = new FileWriter(dir + "\\" + nome + extensao);
                    printWriter = new PrintWriter(fileWriter);
                    return true;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao tentar gravar o arquivo." + e.getLocalizedMessage());
        }
        return false;
    }

}
