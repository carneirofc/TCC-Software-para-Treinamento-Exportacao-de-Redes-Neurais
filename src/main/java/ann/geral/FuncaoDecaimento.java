package ann.geral;

import data.Operacoes;

import java.util.function.Function;

public enum FuncaoDecaimento {
    EXPONENCIAL(
            "Exponencial",
            (Double x) -> (Operacoes.gaussiana(x))
    );

    public final String nome;
    public final Function novoEta;

    FuncaoDecaimento(String nome, Function<Double, Double> novoEta) {
        this.nome = nome;
        this.novoEta = novoEta;

    }
}
