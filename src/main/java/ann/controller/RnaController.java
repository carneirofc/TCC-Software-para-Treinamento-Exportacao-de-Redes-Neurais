package ann.controller;

import ann.detalhes.Rna;
import ann.geral.ConfiguracoesRna;
import data.ConjuntoDados;
import data.Operacoes;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import main.Ctrl;
import main.Recursos;
import main.config.ConfigGeral;
import main.gui.Janela;
import main.gui.ValoresDisplay;
import main.gui.controller.Principal;
import main.gui.grafico.Ponto;
import main.utils.ExceptionPlanejada;
import main.utils.Utilidade;
import org.jetbrains.annotations.Contract;

import java.io.*;

/**
 * Preparação e inicialização da RNA
 *
 * @author Claudio
 */
public class RnaController extends Task<Void> {


    // Geração de Log
    private static File logTempoCamada = null;
    private static File logErroEpoca = null;

    public static DataOutputStream dosLogTempo = null;
    public static DataOutputStream dosLogErro = null;

    private static long tIniTreino;
    private static long tTreino;


    // Controle de erro, critério de parada e informação para o usuário (UI)
    private static double erroEpocaTreino = 0;
    private static double erroEpocaTeste = 0;

    private static Rna rna = null;

    @Contract(pure = true)
    public static File getLogTempoCamada() {
        return logTempoCamada;
    }

    @Contract(pure = true)
    public static File getLogErroEpoca() {
        return logErroEpoca;
    }

    public static void setRna(Rna rna) {
        RnaController.rna = rna;
    }

    /**
     * Treina a rede até os parâmetros especificados.
     */
    private void treinar() throws Exception {

        // Usado para avaliar o tempo total de treinamneto
        tIniTreino = System.nanoTime();
        long tDeltaMin = 0;

        // Tempo de atualização da interface gráfica
        long tAttNano = Operacoes.normalParaNano(Ctrl.getGuiTempoAtualizacaoSegundos());

        // Loop principal do processo de treinamento
        do {
            rna.resetErroEpoca();
            // Função para treino propriamente dito, FFe BP
            for (int linhaAtual = 0; linhaAtual < ConjuntoDados.dadosTreinamento.getDadosEntradaNorm().length; linhaAtual++) {
                if (Ctrl.isLogTempoTreinoCamada()) { // Treinamento gerando log
                    rna.feedForwardLog(ConjuntoDados.dadosTreinamento.getDadosEntradaNorm()[linhaAtual], true);
                    rna.calcErro(ConjuntoDados.dadosTreinamento.getDadosSaidaNorm()[linhaAtual]);
                    rna.backPropagationLog(ConjuntoDados.dadosTreinamento.getDadosSaidaNorm()[linhaAtual]);
                } else { // Treinamento normal
                    rna.feedForward(ConjuntoDados.dadosTreinamento.getDadosEntradaNorm()[linhaAtual], true);
                    rna.calcErro(ConjuntoDados.dadosTreinamento.getDadosSaidaNorm()[linhaAtual]);
                    rna.backPropagation(ConjuntoDados.dadosTreinamento.getDadosSaidaNorm()[linhaAtual]);
                    // rna.backPropagationLogMultithread(DadosRna.getMatrixTreinamentoSaida()[linhaAtual]); // todo: TALVEZ TENTAR FAZER ISSO ACONTECER !@?#
                }
            }
            erroEpocaTreino = rna.getErroEpoca();

            // Critério de parada da RNA
            if (Ctrl.isDadosTesteCarregados() && Ctrl.isUsarDadosTesteTreino()) {
                // Passa o conjunto de dados de teste pela rede no momento de treinamento, não é necessário no processo de treinamento. Escolha do usuário
                rna.resetErroEpoca();
                for (int linhaAtual = 0; linhaAtual < ConjuntoDados.dadosTeste.getDadosEntradaNorm().length; linhaAtual++) {
                    rna.feedForward(ConjuntoDados.dadosTeste.getDadosEntradaNorm()[linhaAtual], false);
                    rna.calcErro(ConjuntoDados.dadosTeste.getDadosSaidaNorm()[linhaAtual]);
                }
                erroEpocaTeste = rna.getErroEpoca();
                // Critério de parada
                if ((rna.getEpocaAtual() > ConfiguracoesRna.getMAX_EPOCH()) || (erroEpocaTeste < ConfiguracoesRna.getTARGET_ERROR())) {
                    Ctrl.setRnaEmExecucao(false);
                }
            } else {// Sem usar os dados de teste
                // Critério de parada
                if ((rna.getEpocaAtual() > ConfiguracoesRna.getMAX_EPOCH()) || (erroEpocaTreino < ConfiguracoesRna.getTARGET_ERROR())) {
                    Ctrl.setRnaEmExecucao(false);
                }
            }

            rna.addEpoch();

            // Escreve no log caso necessário ....
            if (Ctrl.isLogErroEpoca()) {
                try {
                    dosLogErro.write((rna.getEpocaAtual() + "," + erroEpocaTreino + "\n").getBytes(Recursos.CHARSET_PADRAO));
                } catch (IOException e) {
                }
            }


            // Atualiza a interface gráfica ...
            if (((System.nanoTime() - tDeltaMin)) > tAttNano) {
                tDeltaMin = System.nanoTime();
                updateValue(null);
            }

        } while (Ctrl.isRnaEmExecucao());
        tTreino = System.nanoTime() - tIniTreino;
    }

    /**
     * Prepara para a rede para os treinamentos. Verifica se está tudo de
     * acordo.
     */
    private void treinoSetup() throws Exception {

        if (!Ctrl.isDadosTreinoCarregados()) {
            throw new ExceptionPlanejada("Dados para treino não estão carregados.");
        }
        if (!Ctrl.isPropertiesConfigCarregadas()) {
            throw new ExceptionPlanejada("As configurações básicas da RNA não foram carregadas.");
        }

        ConfiguracoesRna.configTolopogia();
        Ctrl.setRnaEmExecucao(true);

        //TODO: Aqui estava atribuindo os valores do usuário para a classe que gerencia tais informações de verdade. ... Implementar isso de uma maneira mais refinada chamanado uma função no momento de salvar estes dados...
        if (ConfigGeral.getConfigGeralAtual().getRnaAtual() == null) {
            ConfigGeral.getConfigGeralAtual().setRnaAtual(new Rna(ConfiguracoesRna.getTolopogiaArray()));
            Operacoes.findMinMaxVals(
                    ConjuntoDados.dadosTreinamento.getDadosSaida(),
                    ConjuntoDados.dadosTreinamento.getDadosEntrada(),
                    ConjuntoDados.dadosTreinamento.getNeuroniosEntradaQtd(),
                    ConjuntoDados.dadosTreinamento.getNeuroniosSaidaQtd());
            ConjuntoDados.setNormMinMax(ConfigGeral.getConfigGeralAtual().getNormMin(), ConfigGeral.getConfigGeralAtual().getNormMax());
        }
        // TODO: Normalizar os dados sempre não é uma boa mas por enquanto ... (Tenho que arrumar um jeito de saber se eles já foram normalizados )
        ConjuntoDados.dadosTreinamento.normalizaDados();
        if (Ctrl.isDadosTesteCarregados()) {
            ConjuntoDados.dadosTeste.normalizaDados();
        }
        rna = ConfigGeral.getConfigGeralAtual().getRnaAtual();
        // rna.configuraParametros();
        //
        System.out.println(ConfigGeral.getConfigGeralAtual().toString());
        System.out.println();
        System.out.println(rna.toString());
        System.out.println();
        System.out.println();
        //
        /**
         * Verificando os dados antes de iniciar o treinamento.
         */
        double[] entradas;
        double[] saidasDesejadas;
        if (ConjuntoDados.dadosTreinamento.getDadosEntradaNorm().length != ConjuntoDados.dadosTreinamento.getDadosSaidaNorm().length)
            throw new ExceptionPlanejada("Número de itensa da matrix de entrada difere do número de linhas da amtrix de saídas no conjunto de treinamento.");

        for (int linhaAtual = 0; linhaAtual < ConjuntoDados.dadosTreinamento.getDadosEntradaNorm().length; linhaAtual++) {
            entradas = ConjuntoDados.dadosTreinamento.getDadosEntradaNorm()[linhaAtual];
            saidasDesejadas = ConjuntoDados.dadosTreinamento.getDadosSaidaNorm()[linhaAtual];
            if (entradas == null || saidasDesejadas == null) {
                throw new ExceptionPlanejada("Erro com os dados carregados, uma das linhas está vazia...");
            }
            if (entradas.length != (ConfiguracoesRna.getTolopogiaArray()[0])) {
                throw new ExceptionPlanejada("Erro com os dados carregados, o número de dados de entrada difere do número esperado.");
            }
            if (saidasDesejadas.length != (ConfiguracoesRna.getTolopogiaArray()[ConfiguracoesRna.getTolopogiaArray().length - 1])) {
                throw new ExceptionPlanejada("Erro com os dados carregados, o número de dados de saída difere do número esperado.");
            }
            if ((rna.getUltimaCamada().getSizeListNeuronios() - 1) != saidasDesejadas.length) {
                throw new ExceptionPlanejada("Erro com os dados carregados, o número de neurônios de saída na RNA não é compatível com a quantidade de saídas do conjunto de treinamento.");
            }
            if ((rna.getPrimeiraCamada().getSizeListNeuronios() - 1) != entradas.length) {
                throw new ExceptionPlanejada("Erro com os dados carregados, o número de neurônios de entrada na RNA não é compatível com a quantidade de entradas do conjunto de treinamento.");
            }
        }
    }


    private static void logSetup() throws IOException {
        dosLogTempo = null;
        dosLogErro = null;

        if (Ctrl.isLogTempoTreinoCamada()) {
            logTempoCamada = File.createTempFile("rna-log-tempo", "data");
            logTempoCamada.deleteOnExit();
            dosLogTempo = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(logTempoCamada)));
            for (int i = 0; i < ConfiguracoesRna.getTolopogiaArray().length; i++) {
                dosLogTempo.write(("ff-c" + i + "nanos").getBytes(Recursos.CHARSET_PADRAO));
                dosLogTempo.write(",".getBytes(Recursos.CHARSET_PADRAO));
            }
            for (int i = ConfiguracoesRna.getTolopogiaArray().length - 1; i > 0; i--) {
                dosLogTempo.write(("bp-c" + i + "nanos").getBytes(Recursos.CHARSET_PADRAO));
                if (i != 1)
                    dosLogTempo.write(",".getBytes(Recursos.CHARSET_PADRAO));
            }
            dosLogTempo.write("\n".getBytes(Recursos.CHARSET_PADRAO));
        }
        if (Ctrl.isLogErroEpoca()) {
            logErroEpoca = File.createTempFile("rna-log-erro", "data");
            logErroEpoca.deleteOnExit();
            dosLogErro = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(logErroEpoca)));
            dosLogErro.write("epoca,erro-medio-norm\n".getBytes(Recursos.CHARSET_PADRAO));
        }
        // TODO: Terminar de implementar o LOG
    }

    /**
     * Testa o conjunto de dados escolhido....
     */
    public static synchronized void netFeedForwardTestData() {
        new Thread(new FFDadosTeste()).start();
    }

    /**
     * Faz um feedforward na rede. Usado para testes.
     */
    public static synchronized void netFeedForwardManual(String valorEntrada) throws Exception {
        new Thread(new FFManual(rna, valorEntrada)).start();
    }

    @Contract(pure = true)
    public static Rna getRna() {
        return rna;
    }

    @Override
    protected Void call() throws Exception {
        treinoSetup();
        logSetup();
        treinar();
        // TODO: atualizar a interface
        return null;
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        Ctrl.setRnaEmExecucao(false);
        Ctrl.setRnaTreinada(true);

        // Atualiza a interface gráfica ...
        if (Ctrl.isUsarDadosTesteTreino()) {
            Principal.getGraficoLinha().addPonto(new Ponto(rna.getEpocaAtual(), erroEpocaTeste), Principal.getGraficoLinha().sTeste);
            ((SimpleDoubleProperty) ValoresDisplay.obsTreinoErroTeste).set(erroEpocaTeste);
        }
        Principal.getGraficoLinha().addPonto(new Ponto(rna.getEpocaAtual(), erroEpocaTreino), Principal.getGraficoLinha().sTreino);
        ((SimpleDoubleProperty) ValoresDisplay.obsTreinoErro).set(erroEpocaTreino);
        ((SimpleDoubleProperty) ValoresDisplay.obsTreinoTempoDeTreinamento).set(Operacoes.nanoParaNormal(tTreino));
        ((SimpleLongProperty) ValoresDisplay.obsEpocaAtual).set(rna.getEpocaAtual());
        Utilidade.notification("Treinamento Concluído");
        //Janela.dialogo(,"Treinamento Concluído","Processo de treinamento concluído com sucesso.", Alert.AlertType.INFORMATION);

    }

    @Override
    protected void updateValue(Void value) {
        Platform.runLater(() -> {
            ((SimpleDoubleProperty) ValoresDisplay.obsTreinoErro).set(erroEpocaTreino);
            ((SimpleLongProperty) ValoresDisplay.obsEpocaAtual).set(rna.getEpocaAtual());

            Principal.getGraficoLinha().addPonto(new Ponto(rna.getEpocaAtual(), erroEpocaTreino), Principal.getGraficoLinha().sTreino);

            if (Ctrl.isDadosTesteCarregados() && Ctrl.isUsarDadosTesteTreino()) {
                ((SimpleDoubleProperty) ValoresDisplay.obsTreinoErroTeste).set(erroEpocaTeste);
                Principal.getGraficoLinha().addPonto(new Ponto(rna.getEpocaAtual(), erroEpocaTeste), Principal.getGraficoLinha().sTeste);
            }
        });
    }

    @Override
    protected void failed() {
        super.failed();
        if (getException() != null) {
            getException().printStackTrace();
            Janela.exceptionDialog(new Exception(getException()));
        }
        Ctrl.setRnaEmExecucao(false);
        Ctrl.setRnaTreinada(false);
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        Ctrl.setRnaEmExecucao(false);
        Ctrl.setRnaTreinada(false);
    }
}
