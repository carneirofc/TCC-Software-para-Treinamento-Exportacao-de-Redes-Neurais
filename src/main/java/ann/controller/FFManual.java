package ann.controller;

import ann.detalhes.Rna;
import data.ConjuntoDados;
import data.Operacoes;

import javafx.concurrent.Task;
import main.gui.Janela;
import main.gui.ValoresDisplay;
import javafx.beans.property.SimpleStringProperty;
import main.Ctrl;
import main.utils.Converter;
import main.utils.ExceptionPlanejada;

import java.util.concurrent.ExecutionException;

/**
 * Realiza as operações de feed forward na rede.
 * <p>
 * TODO: Acho que fiz coisa desnecessária aqui. Considerar voltar e revisar essa classe
 *
 * @author Claudio
 */
public class FFManual extends Task<String> {

    private final Rna rna;
    private final String inval;

    FFManual(Rna rna, String inval) {
        this.rna = rna;
        this.inval = inval;
    }

    @Override
    protected String call() throws Exception {
        if (Ctrl.isRnaEmExecucao())
            throw new ExceptionPlanejada("A rede já está em execução.");
        if (!Ctrl.isRnaTreinada())
            throw new ExceptionPlanejada("A rede não foi treinada.");
        if (rna == null)
            throw new ExceptionPlanejada("A rede não foi incializada.");
        double[] inDoubleVals = Converter.stringToDoubleVector(inval);
        if (inDoubleVals == null)
            throw new ExceptionPlanejada("Valores de entrada vazios.");
        if (Ctrl.isRnaEmExecucao() || !Ctrl.isRnaTreinada())
            throw new ExceptionPlanejada("A rede ainda não terminou o treinamento.");
        double[] res = new double[ConjuntoDados.dadosTreinamento.getDadosSaida()[0].length];
        if (inDoubleVals.length != ConjuntoDados.dadosTreinamento.getDadosEntrada()[0].length)
            throw new ExceptionPlanejada(" inDoubleVals.length != DadosRna.dadosTreinamento.getDadosEntrada()[0].length.");

        Ctrl.setRnaEmExecucao(true);
        Operacoes.setNormMinMax(ConjuntoDados.getNormMin(), ConjuntoDados.getNormMax());
        Operacoes.normalizeVector(inDoubleVals, ConjuntoDados.getMinEntrada(), ConjuntoDados.getMaxEntrada());

        rna.feedForward(inDoubleVals, false);
        rna.getResultsIte(res);

        Operacoes.deNormalizeVector(res, ConjuntoDados.getMinSaida(), ConjuntoDados.getMaxSaida());
        return Converter.doubleVectorToString(res);
    }

    @Override
    protected void failed() {
        super.failed();
        Ctrl.setRnaEmExecucao(false);
        Janela.exceptionDialog(new Exception(getException()));
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        Ctrl.setRnaEmExecucao(false);
        try {
            ((SimpleStringProperty) ValoresDisplay.obsFeedforwardResultado).set(get().replaceAll(";", "\n"));
        } catch (InterruptedException | ExecutionException e) {
            Janela.exceptionDialog(new Exception(e));
        }
    }
}
