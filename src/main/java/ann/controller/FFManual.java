package ann.controller;

import ann.detalhes.Rna;
import data.ConjuntoDados;
import data.Operacoes;

import main.gui.Janela;
import main.gui.ValoresDisplay;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import main.Ctrl;
import main.utils.Converter;
import main.utils.ExceptionPlanejada;

/**
 * Realiza as operações de feed forward na rede.
 * <p>
 * TODO: Acho que fiz coisa desnecessária aqui. Considerar voltar e revisar essa classe
 *
 * @author Claudio
 */
public class FFManual implements Runnable {

    private Rna net;
    private String inval;

    public FFManual(Rna net, String inval) {
        this.net = net;
        this.inval = inval;
    }

    private String feedForward() throws Exception {

        double[] inDoubleVals = Converter.stringToDoubleVector(inval);
        if (inDoubleVals == null)
            throw new ExceptionPlanejada("Valores de entrada vazios.");
        if (net == null)
            throw new ExceptionPlanejada("Rede não foi inicializada.");
        if (Ctrl.isRnaEmExecucao() || !Ctrl.isRnaTreinada())
            throw new ExceptionPlanejada("A rede ainda não terminou o treinamento.");
        double[] res = new double[ConjuntoDados.dadosTreinamento.getDadosSaida()[0].length];
        if (inDoubleVals.length != ConjuntoDados.dadosTreinamento.getDadosEntrada()[0].length)
            throw new ExceptionPlanejada(" inDoubleVals.length != DadosRna.dadosTreinamento.getDadosEntrada()[0].length.");

        Operacoes.setNormMinMax(ConjuntoDados.getNormMin(), ConjuntoDados.getNormMax());
        Operacoes.normalizeVector(inDoubleVals, ConjuntoDados.getMinEntrada(), ConjuntoDados.getMaxEntrada());

        net.feedForward(inDoubleVals, false);
        net.getResultsIte(res);

        Operacoes.deNormalizeVector(res, ConjuntoDados.getMinSaida(), ConjuntoDados.getMaxSaida());
        return Converter.doubleVectorToString(res);
    }

    @Override
    public void run() {
        try {
            String res = feedForward();
            Platform.runLater(() -> ((SimpleStringProperty) ValoresDisplay.obsFeedforwardResultado).set(res.replaceAll(";", "\n")));
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> Janela.exceptionDialog(e));
        }
    }
}
