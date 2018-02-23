package main;

import ann.controller.RnaController;
import main.gui.controller.Principal;
import main.utils.ExceptionPlanejada;

/**
 * Classe que irá gerenciar as tarefas relacionadas a atividades intensas como a execução/treinamento da RNA.
 */
public class Tarefas {

     private static final RnaController RNA_CONTROLLER = new RnaController();
    private static Thread annThread = new Thread(RNA_CONTROLLER);

   public static Thread getAnnThread() {
        return annThread;
    }

    public static void setAnnThread(Thread annThread) {
        Tarefas.annThread = annThread;
    }

    static {
      //  annThread.setPriority(Thread.MAX_PRIORITY);
    }

    public static void iniciarTreino() throws ExceptionPlanejada {
        if (!Ctrl.isPropertiesDadosCarregados()) {
            throw new ExceptionPlanejada("Arquivo de properties ainda não foi carregado.");
        }
        if (!Ctrl.isDadosTreinoCarregados()) {
            throw new ExceptionPlanejada("Txt contendo os dados de treinamento não foi carregado.");
        }
        if (Ctrl.isRnaEmExecucao()) {
            throw new ExceptionPlanejada("A RNA já está em execução.");
        }
        new Thread(new RnaController()).start();
      /*  if (annThread.isAlive()) {
            annThread.interrupt();// TODO: Conferir se posso fazer isso...
            annThread.start();
        } else {
            Principal.getGraficoLinha().limparSeries();
            annThread = new Thread(RNA_CONTROLLER);
            annThread.setPriority(Thread.MAX_PRIORITY);
            annThread.start();
        }*/
    }

    /**
     * Teste com o conjunto completo
     */
    public static void iniciarTeste() throws Exception {

        if (!Ctrl.isRnaEmExecucao() && Ctrl.isRnaTreinada() && Ctrl.isPropertiesDadosCarregados() && Ctrl.isDadosTesteCarregados()) {
            RnaController.netFeedForwardTestData();
        } else {
            throw new ExceptionPlanejada("Verifique se a rede está treinada e não está em execução. Os dados de treino e o arquivo de contendo as propriedades dosLogTempo dados devem estar carregados.");
        }
    }
}
