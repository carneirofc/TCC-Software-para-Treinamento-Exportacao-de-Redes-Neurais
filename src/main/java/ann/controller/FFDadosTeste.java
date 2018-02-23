package ann.controller;

import data.ConjuntoDados;

import data.Operacoes;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import main.gui.Janela;
import main.gui.ValoresDisplay;
import main.gui.controller.Teste;
import main.gui.grafico.Ponto;
import javafx.application.Platform;
import main.Ctrl;
import main.utils.ExceptionPlanejada;
import main.utils.Utilidade;

import java.util.ArrayList;
import java.util.List;

/**
 * Usada para fazer o feed forward nos dados de Teste por meio de uma thread.
 *
 * @author Claudio
 */
public class FFDadosTeste extends Task<Void> {

    private double erroEpocaTeste = 0, erroMin = 0, erroMax = 0;
    private long tf = 0, t = 0;
    private final List<Ponto> pontos = new ArrayList<>();

    @Override
    protected Void call() throws Exception {

        if (RnaController.getRna() == null) {
            throw new ExceptionPlanejada("A rede não foi incializada.");
        }
        if (Ctrl.isRnaEmExecucao()) {
            throw new ExceptionPlanejada("A rede já está em execução.");
        }
        if (!Ctrl.isRnaTreinada()) {
            throw new ExceptionPlanejada("A rede não foi treinada.");
        }
        if (!Ctrl.isDadosTesteCarregados()) {
            throw new ExceptionPlanejada("Os dados de teste não foram carregados.");
        }

        double[] inputVals;
        double[] targetVals;

        Ctrl.setRnaEmExecucao(true);
        t = System.nanoTime();

        ConjuntoDados.dadosTeste.normalizaDados(); // Devo normalizar aqui para dar suporte a novos conjuntos de dados

        for (int testLine = 0; testLine < ConjuntoDados.dadosTeste.getDadosEntradaNorm().length; testLine++) {

            inputVals = ConjuntoDados.dadosTeste.getDadosEntradaNorm()[testLine];
            targetVals = ConjuntoDados.dadosTeste.getDadosSaidaNorm()[testLine];

            if (inputVals == null || targetVals == null) {
                throw new ExceptionPlanejada("inputVals == null || targetVals == null || resultVals == null");
            }

            RnaController.getRna().feedForward(inputVals, false);
            pontos.add(new Ponto(testLine, Utilidade.calErroNormalizado(targetVals)));
            // TODO: Atualizar a itnerface gráfica.
        }
        tf = System.nanoTime();

        if (!pontos.isEmpty()) {
            erroMin = pontos.get(0).getY() * 100;
            erroMax = pontos.get(0).getY() * 100;

            for (Ponto ponto : pontos) {
                ponto.setY(ponto.getY() * 100);// Agora estou passando para porcentagem para mostrar no gráfico ....
                erroEpocaTeste += ponto.getY();
                if (ponto.getY() > erroMax)
                    erroMax = ponto.getY();
                if (ponto.getY() < erroMin)
                    erroMin = ponto.getY();
            }
            erroEpocaTeste /= pontos.size();
        }
        Ctrl.setRnaEmExecucao(false);

        return null;
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        Ctrl.setRnaEmExecucao(false);
    }

    @Override
    protected void failed() {
        super.failed();
        if (getException() != null) {
            Janela.exceptionDialog(new Exception(getException()));
            getException().printStackTrace();
        }
        Ctrl.setRnaEmExecucao(false);
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        ((SimpleDoubleProperty) ValoresDisplay.obsTesteTempoDeTeste).set(Operacoes.nanoParaNormal(tf - t));
        ((SimpleDoubleProperty) ValoresDisplay.obsTesteErroMedio).set(erroEpocaTeste);
        ((SimpleDoubleProperty) ValoresDisplay.obsTesteErroMinimo).set(erroMin);
        ((SimpleDoubleProperty) ValoresDisplay.obsTesteErroMaximo).set(erroMax);
        ((SimpleIntegerProperty) ValoresDisplay.obsTesteNumeroAmostras).set(pontos.size());
        Teste.GRAFICO_DISPERSAO.addPonto(pontos, Teste.GRAFICO_DISPERSAO.sTeste);
        Ctrl.setRnaEmExecucao(false);
    }
}
