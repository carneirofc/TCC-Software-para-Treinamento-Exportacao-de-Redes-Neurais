package main.config;

import ann.detalhes.Rna;
import ann.funcao_ativacao.FuncaoTipo;
import ann.geral.FuncaoDecaimento;

import java.util.Arrays;

// TODO: Implementar main.config Geral no BD.
public class ConfigGeralDTO {


    private FuncaoDecaimento funcaoDecaimento;

    public void setFuncaoDecaimento(FuncaoDecaimento funcaoDecaimento) {
        this.funcaoDecaimento = funcaoDecaimento;
    }

    public ConfigGeralDTO(int[] topologiaOculta, int epocaMaxima, double erroAlvo, double eta, double momentum, double termoLinear, double faixaPesos, FuncaoTipo funcaoAtivacao, FuncaoTipo funcaoAtivacaoSaida, Rna rnaAtual, double normMin, double normMax, double[] inColumnMin, double[] inColumnMax, double[] outColumnMin, double[] outColumnMax, double taxaDecaimentoPasso, double taxaDecaimentoGanho, FuncaoDecaimento funcaoDecaimento) {
        this.topologiaOculta = topologiaOculta;
        this.epocaMaxima = epocaMaxima;
        this.erroAlvo = erroAlvo;
        this.taxaAprendizado = eta;
        this.momentum = momentum;
        this.termoLinear = termoLinear;
        this.faixaPesos = faixaPesos;
        this.funcaoAtivacao = funcaoAtivacao;
        this.funcaoAtivacaoSaida = funcaoAtivacaoSaida;
        this.rnaAtual = rnaAtual;
        this.normMin = normMin;
        this.normMax = normMax;
        this.inColumnMin = inColumnMin;
        this.inColumnMax = inColumnMax;
        this.outColumnMin = outColumnMin;
        this.outColumnMax = outColumnMax;
        this.taxaDecaimentoPasso = taxaDecaimentoPasso;
        this.taxaDecaimentoGanho = taxaDecaimentoGanho;
        this.funcaoDecaimento = funcaoDecaimento;
    }

    /**
     * Topologia das camadas internas da RNA.
     * Valores represetam a quantidade de neurônios.
     * Colunas representam camadas.
     */
    private int[] topologiaOculta;
    /**
     * Valor máximo de época
     */
    private int epocaMaxima;
    /**
     * Erro desejado. Critério de parada da RNA.
     */
    private double erroAlvo;
    /**
     * Taxa de aprendizado. ETA.
     */
    private double taxaAprendizado;
    /**
     * Valor do momentum.
     */
    private double momentum;
    /**
     * Valor do termo linear a ser utilizado na função de ativação da RNA. Tal valor pode ser 0.
     */
    private double termoLinear;
    /**
     * Valor da faixa inicial dosLogTempo pesos [-FAIXA_PESOS,+FAIXA_PESOS].
     */
    private double faixaPesos;

    private FuncaoTipo funcaoAtivacao;

    private FuncaoTipo funcaoAtivacaoSaida;

    private double taxaDecaimentoPasso;
    private double taxaDecaimentoGanho;

    /**
     * Rede neural completa.
     */
    private Rna rnaAtual;

    private double normMin;
    private double normMax;

    private double[] inColumnMin;
    private double[] inColumnMax;

    private double[] outColumnMin;
    private double[] outColumnMax;

    public double[] getInColumnMin() {
        return inColumnMin;
    }

    public void setInColumnMin(double[] inColumnMin) {
        this.inColumnMin = inColumnMin;
    }

    public double[] getInColumnMax() {
        return inColumnMax;
    }

    public void setInColumnMax(double[] inColumnMax) {
        this.inColumnMax = inColumnMax;
    }

    public double[] getOutColumnMin() {
        return outColumnMin;
    }

    public void setOutColumnMin(double[] outColumnMin) {
        this.outColumnMin = outColumnMin;
    }

    public double[] getOutColumnMax() {
        return outColumnMax;
    }

    public void setOutColumnMax(double[] outColumnMax) {
        this.outColumnMax = outColumnMax;
    }

    public int[] getTopologiaOculta() {
        return topologiaOculta;
    }

    public void setTopologiaOculta(int[] topologiaOculta) {
        this.topologiaOculta = topologiaOculta;
    }

    public int getEpocaMaxima() {
        return epocaMaxima;
    }

    public void setEpocaMaxima(int epocaMaxima) {
        this.epocaMaxima = epocaMaxima;
    }

    public double getErroAlvo() {
        return erroAlvo;
    }

    public void setErroAlvo(double erroAlvo) {
        this.erroAlvo = erroAlvo;
    }

    public double getTaxaAprendizado() {
        return taxaAprendizado;
    }

    public void setTaxaAprendizado(double taxaAprendizado) {
        this.taxaAprendizado = taxaAprendizado;
    }

    public double getMomentum() {
        return momentum;
    }

    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    public double getFaixaPesos() {
        return faixaPesos;
    }

    public void setFaixaPesos(double faixaPesos) {
        this.faixaPesos = faixaPesos;
    }

    public FuncaoTipo getFuncaoAtivacaoInterno() {
        return funcaoAtivacao;
    }

    public void setFuncaoAtivacao(FuncaoTipo funcaoAtivacao) {
        this.funcaoAtivacao = funcaoAtivacao;
    }


    public double getTermoLinear() {
        return termoLinear;
    }

    public void setTermoLinear(double termoLinear) {
        this.termoLinear = termoLinear;
    }

    public FuncaoTipo getFuncaoAtivacaoSaida() {
        return funcaoAtivacaoSaida;
    }

    public void setFuncaoAtivacaoSaida(FuncaoTipo funcaoAtivacaoSaida) {
        this.funcaoAtivacaoSaida = funcaoAtivacaoSaida;
    }

    public Rna getRnaAtual() {
        return rnaAtual;
    }

    public void setRnaAtual(Rna rnaAtual) {
        this.rnaAtual = rnaAtual;
    }

    public double getNormMin() {
        return normMin;
    }

    public void setNormMin(double normMin) {
        this.normMin = normMin;
    }

    public double getNormMax() {
        return normMax;
    }

    public void setNormMax(double normMax) {
        this.normMax = normMax;
    }

    @Override
    public String toString() {
        return "ConfigGeralDTO{" +
                "funcaoDecaimento=" + funcaoDecaimento +
                ", topologiaOculta=" + Arrays.toString(topologiaOculta) +
                ", epocaMaxima=" + epocaMaxima +
                ", erroAlvo=" + erroAlvo +
                ", taxaAprendizado=" + taxaAprendizado +
                ", momentum=" + momentum +
                ", termoLinear=" + termoLinear +
                ", faixaPesos=" + faixaPesos +
                ", funcaoAtivacao=" + funcaoAtivacao +
                ", funcaoAtivacaoSaida=" + funcaoAtivacaoSaida +
                ", taxaDecaimentoPasso=" + taxaDecaimentoPasso +
                ", taxaDecaimentoGanho=" + taxaDecaimentoGanho +
                ", rnaAtual=" + rnaAtual +
                ", normMin=" + normMin +
                ", normMax=" + normMax +
                ", inColumnMin=" + Arrays.toString(inColumnMin) +
                ", inColumnMax=" + Arrays.toString(inColumnMax) +
                ", outColumnMin=" + Arrays.toString(outColumnMin) +
                ", outColumnMax=" + Arrays.toString(outColumnMax) +
                '}';
    }

    public void setTaxaDecaimentoPasso(double taxaDecaimentoPasso) {
        this.taxaDecaimentoPasso = taxaDecaimentoPasso;
    }

    public void setTaxaDecaimentoGanho(double taxaDecaimentoGanho) {
        this.taxaDecaimentoGanho = taxaDecaimentoGanho;
    }

    public double getTaxaDecaimentoPasso() {
        return taxaDecaimentoPasso;
    }

    public double getTaxaDecaimentoGanho() {
        return taxaDecaimentoGanho;
    }

    public FuncaoDecaimento getFuncaoDecaimento() {
        return funcaoDecaimento;
    }
}

