package ann.worker;

import ann.detalhes.Rna;
import ann.detalhes.CamadaRna;
import ann.detalhes.NeuronioRna;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @// TODO: 2/20/2018 remover esta classe, não é usada para nada.
 *@deprecated deve ser excluído ....
 *
 * DESISTO DE FAZER ISSO .... IMPOSSÍVEL !!!!!!!!!!! !!!
 * !@#!%!@##%¨& $%¨@#$!@#
 * <p>
 * Classe Worker, utilizada para implementações de multithreading
 */
public class Worker implements Runnable {

    private static ExecutorService executor;
    public static volatile AtomicInteger atomicInteger = new AtomicInteger(2);

    // Threads. Crida originalmente para o trabalho de modelagem para avaliar o desempenho com muiltithreading em determinadas camadas da rede ...
//    private static Worker wImpar;
//    private static Thread tImpar;
//    private static Worker wPar;
//    private static Thread tPar;

    /**
     * LIBEROU_RNA se refere a rna e não a Worker thread. Aqui, uma lógica inversa se aplica.
     */
    //   public static volatile AtomicBoolean LIBEROU_RNA = new AtomicBoolean(false);
//    public static volatile AtomicBoolean LIBEROU_WORKER = new AtomicBoolean(false);

    //public static volatile AtomicBoolean FINALIZA = new AtomicBoolean(false);

    private static Object lockRna;
    private CamadaRna camada = null;
    private CamadaRna camadaSeguinte = null;
    private CamadaRna camadaAnterior = null;
    private int posIni;

    public static void addWorkerToPool(CamadaRna camada, CamadaRna camadaSeguinte, CamadaRna camadaAnterior) {
        atomicInteger.set(2);
        executor.submit(new Worker(camada, camadaSeguinte, camadaAnterior, true));
        executor.submit(new Worker(camada, camadaSeguinte, camadaAnterior, false));
    }
    // private static volatile AtomicBoolean IMPAR_TERMINOU = new AtomicBoolean(false);
    //private static volatile AtomicBoolean PAR_TERMINOU = new AtomicBoolean(false);

    //private boolean par;

    // private Object lock = new Object();

//    public Worker(Object lockRna, boolean par) {
//        this.lockRna = lockRna;
//        posIni = (par ? 0 : 1);
//        //    this.par = par;
//    }

    public Worker(CamadaRna camada, CamadaRna camadaSeguinte, CamadaRna camadaAnterior, boolean par) {
        this.camada = camada;
        this.camadaSeguinte = camadaSeguinte;
        this.camadaAnterior = camadaAnterior;
        // this.lock = lock;
        posIni = (par ? 0 : 1);
    }

    /**
     * Configurações das threads ....
     * <p>
     * TODO: Conferir se vai funcionar direito
     */
    public static void threadSetup(@NotNull Rna rna) {
        // Iniciando as Worker Threads ...
        finalizaAtual();
        lockRna = rna.getLock();
        executor = Executors.newFixedThreadPool(2);
//        wPar = new Worker(rna.getLock(), true);
//        wImpar = new Worker(rna.getLock(), false);
//
//        if (tPar != null) {
//            if (tPar.isAlive())
//                tPar.interrupt();
//        }
//        if (tImpar != null) {
//            if (tImpar.isAlive())
//                tImpar.interrupt();
//        }
//        //FINALIZA.set(false);
//        tPar = new Thread(wPar);
//        tPar.start();
//        tImpar = new Thread(wImpar);
//        tImpar.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

//    public static void setCamadas(CamadaRna camada, CamadaRna camadaSeguinte, CamadaRna camadaAnterior) {
//        synchronized (lockRna) {
//
//            atomicInteger.set(2);
//
//            Worker.camada = camada;
//            Worker.camadaAnterior = camadaAnterior;
//            Worker.camadaSeguinte = camadaSeguinte;
//
//            //     PAR_TERMINOU.set(false);
//            //   IMPAR_TERMINOU.set(false);
//
//            //   LIBEROU_RNA.set(false);
//            //  LIBEROU_WORKER.set(true);
//
//
//        }
//     /*   synchronized (wImpar.getLock()) {
//            if (tImpar.getState().equals(Thread.State.WAITING))
//                wImpar.getLock().notify();
//        }
//        synchronized (wPar.getLock()) {
//            if (tPar.getState().equals(Thread.State.WAITING))
//                wPar.getLock().notify();
//        }*/
//    }

//    public Object getLock() {
//        return lock;
//    }
/*

    public static synchronized boolean terminouCamada() {
        return (PAR_TERMINOU.getPorCod() && IMPAR_TERMINOU.getPorCod());
    }*/

    public static void finalizaAtual() {
        try {
            if (executor != null) {
                executor.shutdownNow();
                executor = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*FINALIZA.set(true);
        if (tImpar != null && tPar != null && wPar != null && wImpar != null) {
            synchronized (wImpar.getLock()) {
                if (tImpar.getState().equals(Thread.State.WAITING))
                    wImpar.getLock().notify();
            }
            synchronized (wPar.getLock()) {
                if (tPar.getState().equals(Thread.State.WAITING))
                    wPar.getLock().notify();
            }
        }*/
    }

    @Override
    public void run() {

        // Enquanto está em execução vai rodar ...
        //   while (Ctrl.isRnaEmExecucao()) {

        // Sleep até que tenha alguma coisa para ser processada ...
          /*  synchronized (lock) {
                try {
                    if (!LIBEROU_WORKER.getPorCod())
                        lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
        //   if (!FINALIZA.getPorCod()) {
        if (camada != null && camadaSeguinte != null && camadaAnterior != null) {
            /// if (par) { // Processa os neurônios pares
            if (camada.getSizeListNeuronios() > posIni)
                for (int neuronio = 0; neuronio < camada.getSizeListNeuronios(); neuronio += 2) {
                    NeuronioRna neuronioRna = camada.getNeuron(neuronio);
                    neuronioRna.calculaGradiente(camadaSeguinte);
                    if (neuronio != (camada.getSizeListNeuronios() - 1)) { // O último neurônio é o bias. Não possui conexão de entrada.
                        neuronioRna.atualizaPesos(camadaAnterior);
                    }
                }
            //    System.out.println("P");
            //   PAR_TERMINOU.set(true);
            // }
            //else if (camada.getSizeListNeuronios() >= 1) { // Processa os neurônios ímpares
//                for (int neuronio = 1; neuronio < camada.getSizeListNeuronios(); neuronio += 2) {
//                    NeuronioRna neuronioRna = camada.getNeuron(neuronio);
//                    neuronioRna.calculaGradiente(camadaSeguinte);
//                    if (neuronio != (camada.getSizeListNeuronios() - 1)) { // O último neurônio é o bias. Não possui conexão de entrada.
//                        neuronioRna.atualizaPesos(camadaAnterior);
//                    }
//                }
//                //    System.out.println("I");
//                IMPAR_TERMINOU.set(true);
//            }

            // Finalizou o processamento ... Deve notificar a RNA para mudar de camada
            if (atomicInteger.decrementAndGet() == 0) {
                //   synchronized (token) {
                //       token.notify();
                //   }

                synchronized (lockRna) { // TODO: deveria verificar o estado da thread ....?!
                    // if (terminouCamada()) {
                    //    LIBEROU_RNA.set(true);
                    //   LIBEROU_WORKER.set(false);
                    //  if (Tarefas.getAnnThread().getState().equals(Thread.State.WAITING)) {
                    lockRna.notify();
                    //  }
                }
            }
        }
        //  }
        //   }
    }
}


