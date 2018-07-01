package ann.funcao_ativacao;

import ann.detalhes.Rna;
import data.Operacoes;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public enum FuncaoTipo implements Serializable {
    TANGENTE_HIPERBOLICA(
            "Tangente Hiperbólica",
            (double x, double termoLinear) -> (Operacoes.tanh(x) + termoLinear * x),
            (double x, double termoLinear) -> (Operacoes.tanhDerivative(x) + termoLinear),
            termoLinear -> "*outVal = tanh(x)" + ((termoLinear == null || termoLinear == 0) ? (";") : ("+" + termoLinear + " * x;"))
    ),
    SIGMOIDE(
            "Sigmóide",
            (double x, double termoLinear) -> (Operacoes.sigmoid(x) + termoLinear * x),
            (double x, double termoLinear) -> (Operacoes.sigmoidDerivative(x) + termoLinear),
            termoLinear -> "*outVal = 1 / (1 + pow(2.718282, -x))" + ((termoLinear == null || termoLinear == 0) ? (";") : ("+" + termoLinear + " * x;"))
    ),
    LINEAR(
            "Linear",
            (double x, double termoLinear) -> (x),
            (double x, double termoLinear) -> (1.0),
            termoLinear -> "*outVal =  x;"
    ),
    /**
     * 1.7159*tanh((2/3)*x)
     */
    TANGENTE_HIPERBOLICA_YANN_LECUN(
            "Tange Hiberbólica 1.7159*tanh((2/3)*x)",
            (double x, double termoLinear) -> (1.7159 * (Operacoes.tanh(3 / 2 * x)) + termoLinear * x),
            (double x, double termoLinear) -> (1.14393333 * (Math.pow(Operacoes.sech(0.66666 * x), 2)) + termoLinear * x),
            termoLinear -> "*outVal =  1.7159 * (tanh(3 / 2 * x))" + ((termoLinear == null || termoLinear == 0) ? (";") : ("+" + termoLinear + " * x;"))
    ),
    GAUSSIANA(
            "Gaussiana c = 0 e sigma = 1",
            (double x, double termoLinear) -> (Operacoes.gaussiana(x)),
            (double x, double termoLinear) -> (Operacoes.gaussianaDiff(x)),
            termoLinear -> "*outVal =  exp(-((x * x) / (2)));"
    );

    private final String nome;
    private final FuncaoAtivacao calculoFuncaoAtivacao;
    private final FuncaoAtivacao calculoFuncaoAtivacaoDerivada;
    private final FuncaoAtivacaoString stringFuncao;

    FuncaoTipo(String nome, FuncaoAtivacao calculoFuncaoAtivacao, FuncaoAtivacao calculoFuncaoAtivacaoDerivada, FuncaoAtivacaoString stringFuncao) {
        this.stringFuncao = stringFuncao;
        this.nome = nome;
        this.calculoFuncaoAtivacao = calculoFuncaoAtivacao;
        this.calculoFuncaoAtivacaoDerivada = calculoFuncaoAtivacaoDerivada;
    }

    public Double calc(double x, double termoLinear) {
        return calculoFuncaoAtivacao.calc(x, termoLinear);
    }

    public Double calcDerivada(double x, double termoLinear) {
        return calculoFuncaoAtivacaoDerivada.calc(x, termoLinear);
    }

    @NotNull
    public String getNome() {
        return nome;
    }

    @Contract(pure = true)
    @NotNull
    public String getStringFuncao(Rna rna) {
        return getStringFuncao(rna.getTermoLinear());
    }

    public String getStringFuncao(double termoLinear) {
        return stringFuncao.getString(termoLinear);
    }
}
