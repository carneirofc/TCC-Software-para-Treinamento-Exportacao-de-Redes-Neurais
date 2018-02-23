/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import ann.funcao_ativacao.FuncaoTipo;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import main.config.ConfigGeral;
import main.gui.ValoresDisplay;

/**
 * Armazena os estados do sistema etc... Aqui ficam as variáveis de controle,
 * constantes utilizadas dentre outrasd configurações. Informações especificas a
 * respeito da rede neurão ficam na classe Configurações RNA
 *
 * @author Claudio
 */
public class Ctrl {

    private static boolean propertiesDadosCarregados = false;
    private static boolean propertiesConfigCarregadas = false;
    private static boolean dadosTreinoCarregados = false;
    private static boolean dadosTesteCarregados = false;


    private static final SimpleBooleanProperty guiMostrarTexto = new SimpleBooleanProperty(true);
    private static final SimpleBooleanProperty guiMostrarGrafico = new SimpleBooleanProperty(true);

    private static final SimpleBooleanProperty rnaDropout = new SimpleBooleanProperty(false);
    private static final SimpleBooleanProperty rnaTreinada = new SimpleBooleanProperty(false);
    private static final SimpleBooleanProperty rnaEmExecucao = new SimpleBooleanProperty(false);

    private static final SimpleBooleanProperty usarDadosTesteTreino = new SimpleBooleanProperty(false);
    private static final SimpleBooleanProperty logTempoTreinoCamada = new SimpleBooleanProperty(false);
    private static final SimpleBooleanProperty logErroEpoca = new SimpleBooleanProperty(false);
    /**
     * Determina se são mostradas as informações no console. Prejudica muito a
     * performance, deve ser usada apenas para debug .
     */
    private static boolean sysLogInputTxt = false;

    private static String dirPadrao = null;

    /**
     * Tempo mínimo para atualização da tela em ms
     */
    private static SimpleDoubleProperty guiTempoAtualizacaoSegundos = new SimpleDoubleProperty(0.5);
    private static Object decaimentoTaxaAprendizado;
    //private static double guiTempoAtualizacaoSegundos = 0.5;


    public static boolean isLogTempoTreinoCamada() {
        return logTempoTreinoCamada.get();
    }

    public static SimpleBooleanProperty logTempoTreinoCamadaProperty() {
        return logTempoTreinoCamada;
    }

    public static boolean isLogErroEpoca() {
        return logErroEpoca.get();
    }

    public static SimpleBooleanProperty logErroEpocaProperty() {
        return logErroEpoca;
    }

    public static void setLogErroEpoca(boolean logErroEpoca) {
        Ctrl.logErroEpoca.set(logErroEpoca);
    }

    public static boolean isGuiMostrarTexto() {
        return guiMostrarTexto.get();
    }

    public static SimpleBooleanProperty guiMostrarTextoProperty() {
        return guiMostrarTexto;
    }

    public static void setGuiMostrarTexto(boolean guiMostrarTexto) {
        Ctrl.guiMostrarTexto.set(guiMostrarTexto);
    }

    public static boolean isGuiMostrarGrafico() {
        return guiMostrarGrafico.get();
    }

    public static SimpleBooleanProperty guiMostrarGraficoProperty() {
        return guiMostrarGrafico;
    }

    public static void setGuiMostrarGrafico(boolean guiMostrarGrafico) {
        Ctrl.guiMostrarGrafico.set(guiMostrarGrafico);
    }

    public static boolean isPropertiesDadosCarregados() {
        return propertiesDadosCarregados;
    }

    public static void setPropertiesDadosCarregados(boolean propertiesDadosCarregados) {
        Ctrl.propertiesDadosCarregados = propertiesDadosCarregados;
    }

    public static boolean isPropertiesConfigCarregadas() {
        return propertiesConfigCarregadas;
    }

    public static void setPropertiesConfigCarregadas(boolean propertiesConfigCarregadas) {
        Ctrl.propertiesConfigCarregadas = propertiesConfigCarregadas;
    }

    public static boolean isDadosTreinoCarregados() {
        return dadosTreinoCarregados;
    }

    public static void setDadosTreinoCarregados(boolean dadosTreinoCarregados) {
        Ctrl.dadosTreinoCarregados = dadosTreinoCarregados;
    }

    public static boolean isDadosTesteCarregados() {
        return dadosTesteCarregados;
    }

    public static void setDadosTesteCarregados(boolean dadosTesteCarregados) {
        Ctrl.dadosTesteCarregados = dadosTesteCarregados;
        Platform.runLater(() -> ((SimpleBooleanProperty) ValoresDisplay.obsDadoTesteDisable).set(!dadosTesteCarregados));
    }

    public static boolean isRnaEmExecucao() {
        return rnaEmExecucao.get();
    }

    public static void setRnaEmExecucao(boolean rnaEmExecucao) {
        Ctrl.rnaEmExecucao.set(rnaEmExecucao);
    }

    public static SimpleBooleanProperty rnaEmExecucaoProperty() {
        return rnaEmExecucao;
    }

    public static boolean isRnaTreinada() {
        return rnaTreinada.get();
    }

    public static void setRnaTreinada(boolean rnaTreinada) {
        Ctrl.rnaTreinada.set(rnaTreinada);
    }

    public static SimpleBooleanProperty rnaTreinadaProperty() {
        return rnaTreinada;
    }

    public static boolean isUsarDadosTesteTreino() {
        return usarDadosTesteTreino.get();
    }

    public static SimpleBooleanProperty usarDadosTesteTreinoProperty() {
        return usarDadosTesteTreino;
    }

    public static void setUsarDadosTesteTreino(boolean usarDadosTesteTreino) {
        Ctrl.usarDadosTesteTreino.set(usarDadosTesteTreino);
    }

    public static boolean getLogTempoTreinoCamada() {
        return logTempoTreinoCamada.get();
    }

    public static SimpleBooleanProperty gerarAquivoLogProperty() {
        return logTempoTreinoCamada;
    }

    public static void setLogTempoTreinoCamada(boolean logTempoTreinoCamada) {
        Ctrl.logTempoTreinoCamada.set(logTempoTreinoCamada);
    }

    public static boolean isSysLogInputTxt() {
        return sysLogInputTxt;
    }

    public static void setSysLogInputTxt(boolean sysLogInputTxt) {
        Ctrl.sysLogInputTxt = sysLogInputTxt;
    }

    public static String getDirPadrao() {
        return dirPadrao;
    }

    public static void setDirPadrao(String dirPadrao) {
        Ctrl.dirPadrao = dirPadrao;
    }
//
//    public static FuncaoTipo getFuncaoAtivacaoInterno() {
//        return ConfigGeral.getConfigGeralAtual().getFuncaoAtivacaoInterno();
//    }
//
//    public static void setFuncaoAtivacaoInterno(FuncaoTipo funcaoAtivacaoInterno) {
//        ConfigGeral.getConfigGeralAtual().setFuncaoAtivacao(funcaoAtivacaoInterno);
//    }
//
//    public static FuncaoTipo getFuncaoAtivacaoSaida() {
//        return ConfigGeral.getConfigGeralAtual().getFuncaoAtivacaoSaida();
//    }
//
//    public static void setFuncaoAtivacaoSaida(FuncaoTipo funcaoAtivacaoSaida) {
//        ConfigGeral.getConfigGeralAtual().setFuncaoAtivacaoSaida(funcaoAtivacaoSaida);
//    }

    public static double getGuiTempoAtualizacaoSegundos() {
        return guiTempoAtualizacaoSegundos.get();
    }

    public static SimpleDoubleProperty guiTempoAtualizacaoSegundosProperty() {
        return guiTempoAtualizacaoSegundos;
    }

    public static void setGuiTempoAtualizacaoSegundos(double guiTempoAtualizacaoSegundos) {
        Ctrl.guiTempoAtualizacaoSegundos.set(guiTempoAtualizacaoSegundos);
    }

    public static boolean isRnaDropout() {
        return rnaDropout.get();
    }

    public static SimpleBooleanProperty rnaDropoutProperty() {
        return rnaDropout;
    }

    public static void setRnaDropout(boolean rnaDropout) {
        Ctrl.rnaDropout.set(rnaDropout);
    }

}
