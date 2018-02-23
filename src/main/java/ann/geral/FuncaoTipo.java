package ann.geral;

import ann.detalhes.Rna;
import data.Operacoes;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.DoubleFunction;

public enum FuncaoTipo {
    TANGENTE_HIPERBOLICA(
            "Tangente Hiperbólica",
            (double x) -> (Operacoes.tanh(x) + ConfiguracoesRna.getLINEAR_TERM() * x),
            (double x) -> (Operacoes.tanhDerivative(x) + ConfiguracoesRna.getLINEAR_TERM()),
            termoLinear -> "*outVal = tanh(x)" + ((termoLinear == null || termoLinear == 0) ? (";") : ("+" + termoLinear + " * x;"))
    ),
    SIGMOIDE(
            "Sigmóide",
            (double x) -> (Operacoes.sigmoid(x) + ConfiguracoesRna.getLINEAR_TERM() * x),
            (double x) -> (Operacoes.sigmoidDerivative(x) + ConfiguracoesRna.getLINEAR_TERM()),
            termoLinear -> "*outVal = 1 / (1 + pow(2.718282, -x))" + ((termoLinear == null || termoLinear == 0) ? (";") : ("+" + termoLinear + " * x;"))
    ),
    LINEAR(
            "Linear",
            (double x) -> (x),
            (double x) -> (1.0),
            termoLinear -> "*outVal =  x;"
    ),
    /**
     * 1.7159*tanh((2/3)*x)
     */
    TANGENTE_HIPERBOLICA_YANN_LECUN(
            "Tange Hiberbólica 1.7159*tanh((2/3)*x)",
            (double x) -> (1.7159 * (Operacoes.tanh(3 / 2 * x)) + ConfiguracoesRna.getLINEAR_TERM() * x),
            (double x) -> (1.14393333 * (Math.pow(Operacoes.sech(0.66666 * x), 2)) + ConfiguracoesRna.getLINEAR_TERM() * x),
            termoLinear -> "*outVal =  1.7159 * (tanh(3 / 2 * x))" + ((termoLinear == null || termoLinear == 0) ? (";") : ("+" + termoLinear + " * x;"))
    ),
    GAUSSIANA(
            "Gaussiana c = 0 e sigma = 1",
            (double x) -> (Operacoes.gaussiana(x)),
            (double x) -> (Operacoes.gaussianaDiff(x)),
            termoLinear -> "*outVal =  exp(-((x * x) / (2)));"
    );

    private final String nome;
    private final DoubleFunction<Double> calculoFuncaoAtivacao;
    private final DoubleFunction<Double> calculoFuncaoAtivacaoDerivada;
    private final FuncaoAtivacaoString stringFuncao;

    FuncaoTipo(String nome, DoubleFunction<Double> calculoFuncaoAtivacao, DoubleFunction<Double> calculoFuncaoAtivacaoDerivada, FuncaoAtivacaoString stringFuncao) {
        this.stringFuncao = stringFuncao;
        this.nome = nome;
        this.calculoFuncaoAtivacao = calculoFuncaoAtivacao;
        this.calculoFuncaoAtivacaoDerivada = calculoFuncaoAtivacaoDerivada;
    }

    public Double calc(double x) {
        return calculoFuncaoAtivacao.apply(x);
    }

    public Double calcDerivada(double x) {
        return calculoFuncaoAtivacaoDerivada.apply(x);
    }

    @Nullable
    public static FuncaoTipo getPorCod(int i) {
        for (FuncaoTipo funcaoTipo : values()) {
            if (funcaoTipo.ordinal() == i)
                return funcaoTipo;
        }
        return null;
    }

    @NotNull
    public String getNomeFuncao() {
        return Integer.toString(ordinal()) + " : " + nome;
    }

    @Contract(pure = true)
    @NotNull
    public String getStringFuncao(Rna rna) {
        return stringFuncao.getString(rna.getTermoLinear());
    }
}
