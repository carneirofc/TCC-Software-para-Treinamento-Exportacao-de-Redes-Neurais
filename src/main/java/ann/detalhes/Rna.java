package ann.detalhes;

import ann.controller.RnaController;
import ann.geral.Topologia;
import ann.geral.DecaimentoTaxaAprendizado;
import ann.funcao_ativacao.FuncaoTipo;
import data.ConjuntoDados;
import main.Ctrl;
import main.Recursos;
import main.config.ConfigGeral;
import main.utils.ExceptionPlanejada;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Visão "macro" da rede
 *
 * @author Claudio
 */
public class Rna {


    /**
     * Faixa na qual os pesos das ligações será buscada. Este valor representa a
     * "meia largura" ou seja, para um valor de 0.5, serão sorteados pesos entre
     * -0.5 e 0.5.
     */
    private final double faixaInicialPesos;
    private final boolean dropout;
    private final double termoLinear;
    private final FuncaoTipo funcaoAtivacaoInterna;
    private final FuncaoTipo getFuncaoAtivacaoSaida;
    private final DecaimentoTaxaAprendizado decaimentoTaxaAprendizado;
    private final int[] topologia;
    private final List<Camada> camadas = new ArrayList<>();

    private long epocaMaxima;
    private double erroAlvo;
    private double taxaAprendizado;
    private double taxaAprendizadoInicial;
    private double momentum;

    private Double erroIteracao = 0.0;
    private double erroEpoca = 0;
    private long epocaAtual = 0;


    /**
     * Contrutor da rede. Informa ao sistema que a rede não foi treinada, cria
     * as camadas e os neurônios.
     *
     * @param topologia vetor contendo a topologia da rna. Nº  de colunas representam as camadas e seus valores a quantidade de neurônios.
     */
    public Rna(final int[] topologia) {
        // @// TODO: 2/22/2018 arrumar o decaimento da taxa de aprendizagem
        this(ConfigGeral.getConfigGeralAtual().getFaixaPesos(),
                false,
                ConfigGeral.getConfigGeralAtual().getTermoLinear(),
                ConfigGeral.getConfigGeralAtual().getFuncaoAtivacaoInterno(),
                ConfigGeral.getConfigGeralAtual().getFuncaoAtivacaoSaida(),
                Topologia.getTolopogiaArray(),
                (taxaAprendizado, param1, param2, param3) -> ((param1 % param2 == 0) ? (taxaAprendizado / 2) : (taxaAprendizado)));
        Ctrl.setRnaTreinada(false);
        inicializaLayers(topologia);
        configuraParametros();
    }

    private void atualizaEta() {
        taxaAprendizado = decaimentoTaxaAprendizado.calc(taxaAprendizado, epocaAtual, 1000, 0);
    }

    private Rna(double faixaInicialPesos, boolean dropout, double termoLinear, FuncaoTipo funcaoAtivacaoInterna, FuncaoTipo getFuncaoAtivacaoSaida, int[] topologia, DecaimentoTaxaAprendizado decaimentoTaxaAprendizado) {
        this.faixaInicialPesos = faixaInicialPesos;
        this.dropout = dropout;
        this.termoLinear = termoLinear;
        this.funcaoAtivacaoInterna = funcaoAtivacaoInterna;
        this.getFuncaoAtivacaoSaida = getFuncaoAtivacaoSaida;
        this.topologia = topologia;
        this.decaimentoTaxaAprendizado = decaimentoTaxaAprendizado;
    }

    public void configuraParametros() {
        this.epocaMaxima = ConfigGeral.getConfigGeralAtual().getEpocaMaxima();
        this.erroAlvo =  ConfigGeral.getConfigGeralAtual().getErroAlvo();
        this.taxaAprendizadoInicial =  ConfigGeral.getConfigGeralAtual().getTaxaAprendizado();
        this.taxaAprendizado = taxaAprendizadoInicial;
        this.momentum =  ConfigGeral.getConfigGeralAtual().getMomentum();
    }

    /**
     * Cria as as camadas e seus devidos neurônios de conforme descrito nas
     * configurações de topologia da rede.
     */
    private void inicializaLayers(final int[] topology) {

        /**
         * Quantos neurônios existem no layer seguinte.
         */
        int numNeuroniosProxLayer;
        for (int layerNum = 0; layerNum < topology.length; layerNum++) {
            Camada camadaRna = new Camada();

            // Quantos neurônios existem na próxima camada?
            if ((layerNum == (topology.length - 1))) { // Se estou no último layer
                numNeuroniosProxLayer = 0;
            } else {
                numNeuroniosProxLayer = topology[layerNum + 1];
            }

            /* 
                Adiciona os neuronios informados mais o bias.
             */
            for (int neuronNum = 0; neuronNum <= topology[layerNum]; neuronNum++) {
                camadaRna.addNeuron(new Neuronio(numNeuroniosProxLayer, neuronNum, this));
            }
            // Força o valor do bias em -1 // Assim funciona melhor 
            camadaRna.getBias().setValorSaida(-1.0);
            camadas.add(camadaRna);
        }
    }

    /**
     * Calcula o  erro médio da iteração !
     * somatório targetVals[i] - valoeCalculado[i]
     */
    public double calcIteErro(double[] targetVals) {
        Camada outputLayer = getUltimaCamada();
        erroIteracao = 0.0;
        for (int neuronNum = 0; neuronNum < (ConjuntoDados.dadosTreinamento.getNeuroniosSaidaQtd()); neuronNum++) {
            erroIteracao += Math.abs(targetVals[neuronNum] - outputLayer.getNeuron(neuronNum).getValorSaida());
        }
        erroIteracao /= ConjuntoDados.dadosTreinamento.getNeuroniosSaidaQtd();
        //  erroIteracao = Math.sqrt(erroIteracao / DadosRna.getNeuroniosSaidaQtd());
        return erroIteracao;
    }

    /**
     * Acumula o erro de todas as iterações dentro da época
     */
    public void calcErro(double[] targetVals) {
        /**
         * Cálculo do RMS das saídas .
         * Desempenho da rede
         *
         */
        calcIteErro(targetVals);

        erroEpoca += erroIteracao;
        erroEpoca /= 2;
    }

    /**
     * Realiza os cálculos de backpropagation (treinamento ....)
     */
    public void backPropagation(double[] targetVals) {

        Camada outputLayer = camadas.get(camadas.size() - 1);
        Camada prevOutLayer = camadas.get(camadas.size() - 2);

        /*
              Cálculo dosLogTempo componentes do vetor gradiente e atualização dosLogTempo pesos de entrada
               nos neurônios da camada de saída.  (ref[i] - saídaNeuron[i])*(DxFt(entradaNeuron[i]))                           
         */

        for (int neuronNum = 0; neuronNum < (outputLayer.getSizeListNeuronios() - 1); neuronNum++) {
            outputLayer.getNeuron(neuronNum).calculaGradienteSaida(targetVals[neuronNum]);
            outputLayer.getNeuron(neuronNum).atualizaPesos(prevOutLayer);
        }

        /*
               Cálculo dosLogTempo gradientes parciais das camadas escondidas (gradiente local)
            e atualização dosLogTempo pesos de entrada.
         */
        for (int camadaEscondida = camadas.size() - 2; camadaEscondida > 0; --camadaEscondida) {
            Camada camada = camadas.get(camadaEscondida);
            Camada camadaSeguinte = camadas.get(camadaEscondida + 1);
            Camada camadaAnterior = camadas.get(camadaEscondida - 1);
            calcCamadaEscondida(camada, camadaSeguinte, camadaAnterior);
        }
    }

    private void calcCamadaEscondida(Camada camada, Camada camadaSeguinte, Camada camadaAnterior) {
        for (int neuronio = 0; neuronio < camada.getSizeListNeuronios(); neuronio++) {
            Neuronio neuronioRna = camada.getNeuron(neuronio);
            neuronioRna.calculaGradiente(camadaSeguinte);
            if (neuronio != (camada.getSizeListNeuronios() - 1)) { // O último neurônio é o bias. Não possui conexão de entrada.
                neuronioRna.atualizaPesos(camadaAnterior);
            }
        }
    }

    /**
     * Realiza os cálculos de backpropagation (treinamento ....)
     */
    public void backPropagationLog(double[] targetVals) throws Exception {
        long antes, depois;
        long iAlg = 0;
        long fAlg;
        antes = System.nanoTime();
        iAlg = antes;
        Camada outputLayer = camadas.get(camadas.size() - 1);
        Camada prevOutLayer = camadas.get(camadas.size() - 2);

        /*
              Cálculo dosLogTempo componentes do vetor gradiente e atualização dosLogTempo pesos de entrada
               nos neurônios da camada de saída.  (ref[i] - saídaNeuron[i])*(DxFt(entradaNeuron[i]))
         */
        for (int neuronNum = 0; neuronNum < (outputLayer.getSizeListNeuronios() - 1); neuronNum++) {
            outputLayer.getNeuron(neuronNum).calculaGradienteSaida(targetVals[neuronNum]);
            outputLayer.getNeuron(neuronNum).atualizaPesos(prevOutLayer);
        }
        depois = System.nanoTime();
        RnaController.dosLogTempo.write(Long.toString(depois - antes).getBytes(Recursos.CHARSET_PADRAO));
        RnaController.dosLogTempo.write(",".getBytes(Recursos.CHARSET_PADRAO));

        /*
               Cálculo dosLogTempo gradientes parciais das camadas escondidas (gradiente local)
            e atualização dosLogTempo pesos de entrada.
         */
        for (int camadaEscondida = camadas.size() - 2; camadaEscondida > 0; --camadaEscondida) {
            antes = System.nanoTime();
            Camada camada = camadas.get(camadaEscondida);
            Camada camadaSeguinte = camadas.get(camadaEscondida + 1);
            Camada camadaAnterior = camadas.get(camadaEscondida - 1);

            calcCamadaEscondida(camada, camadaSeguinte, camadaAnterior);
            depois = System.nanoTime();
            RnaController.dosLogTempo.write(Long.toString(depois - antes).getBytes(Recursos.CHARSET_PADRAO));
            if (camadaEscondida != 1) {
                RnaController.dosLogTempo.write(",".getBytes(Recursos.CHARSET_PADRAO));
            }
        }
        fAlg = System.nanoTime();
        RnaController.dosLogTempo.write(",".getBytes(Recursos.CHARSET_PADRAO));
        RnaController.dosLogTempo.write(Long.toString(fAlg - iAlg).getBytes(Recursos.CHARSET_PADRAO));
        RnaController.dosLogTempo.write("\n".getBytes(Recursos.CHARSET_PADRAO));
    }


    /**
     * FeedForward
     *
     * @param treinamento indica se está na fase de treinamento ou não
     */
    public void feedForwardLog(double[] valoresEntrada, boolean treinamento) throws Exception {
        long antes, depois;
        antes = System.nanoTime();
        // Atribui a entrada. Atribui os valores de entrada (inputVals[]) para os neurônios de entrada(camada entrada).
        for (int i = 0; i < valoresEntrada.length; i++) {
            getPrimeiraCamada().getListNeuronios().get(i).setValorSaida(valoresEntrada[i]);
        }
        depois = System.nanoTime();
        RnaController.dosLogTempo.write(Long.toString(depois - antes).getBytes(Recursos.CHARSET_PADRAO));
        RnaController.dosLogTempo.write(",".getBytes(Recursos.CHARSET_PADRAO));

        // Forward Propagation
        /**
         * Faz o somatório dosLogTempo valores e depois joga para a função de ativação.
         *
         * Como a primeira cama de neurônios é usada apenas para "guardar" os
         * valores de entrada, ela não entrada no loop para ser realizado o feed
         * forward uma vez os valores de saída dosLogTempo neurônios já estão
         * atribuídos.
         *
         */
        boolean ultimaCamada;
        for (int numCamada = 1; numCamada < camadas.size(); numCamada++) {
            ultimaCamada = (numCamada == (camadas.size() - 1));// Verifica se está na última camada
            antes = System.nanoTime();
            Camada camadaAnterior = camadas.get(numCamada - 1);
            Camada camadaAtual = camadas.get(numCamada);
            /*
                 Alimenta as entradas dosLogTempo neurônios da camada de entradas exceto o neurônio de bias.
             */
            for (int neuronNum = 0; neuronNum < (camadaAtual.getSizeListNeuronios() - 1); neuronNum++) {
                if (dropout) {
                    camadaAtual.getNeuron(neuronNum).feedForwardDropout(camadaAnterior, ultimaCamada, treinamento);
                } else {
                    camadaAtual.getNeuron(neuronNum).feedForward(camadaAnterior, ultimaCamada);
                }
            }
            depois = System.nanoTime();
            RnaController.dosLogTempo.write(Long.toString(depois - antes).getBytes(Recursos.CHARSET_PADRAO));
            RnaController.dosLogTempo.write(",".getBytes(Recursos.CHARSET_PADRAO));
        }
    }

    /**
     * FeedForward
     *
     * @param treinamento indica se está na fase de treinamento ou não
     */
    public void feedForward(double[] valoresEntrada, boolean treinamento) throws Exception {
        //  long antes, depois;
        //  antes = System.nanoTime();
        // Atribui a entrada. Atribui os valores de entrada (inputVals[]) para os neurônios de entrada(camada entrada).
        for (int i = 0; i < valoresEntrada.length; i++) {
            getPrimeiraCamada().getListNeuronios().get(i).setValorSaida(valoresEntrada[i]);
        }
        // depois = System.nanoTime();
        //RnaController.dosLogTempo.write(Long.toString(depois - antes).getBytes(Recursos.CHARSET_PADRAO));
        //RnaController.dosLogTempo.write(",".getBytes(Recursos.CHARSET_PADRAO));

        // Forward Propagation
        /**
         * Faz o somatório dosLogTempo valores e depois joga para a função de ativação.
         *
         * Como a primeira cama de neurônios é usada apenas para "guardar" os
         * valores de entrada, ela não entrada no loop para ser realizado o feed
         * forward uma vez os valores de saída dosLogTempo neurônios já estão
         * atribuídos.
         *
         */
        boolean ultimaCamada;
        for (int numCamada = 1; numCamada < camadas.size(); numCamada++) {
            ultimaCamada = (numCamada == (camadas.size() - 1));// Verifica se está na última camada
            //   antes = System.nanoTime();
            Camada camadaAnterior = camadas.get(numCamada - 1);
            Camada camadaAtual = camadas.get(numCamada);
            /*
                 Alimenta as entradas dosLogTempo neurônios da camada de entradas exceto o neurônio de bias.
             */
            for (int neuronNum = 0; neuronNum < (camadaAtual.getSizeListNeuronios() - 1); neuronNum++) {
                if (dropout) {
                    camadaAtual.getNeuron(neuronNum).feedForwardDropout(camadaAnterior, ultimaCamada, treinamento);
                } else {
                    camadaAtual.getNeuron(neuronNum).feedForward(camadaAnterior, ultimaCamada);
                }
            }
            //     depois = System.nanoTime();
            //      RnaController.dosLogTempo.write(Long.toString(depois - antes).getBytes(Recursos.CHARSET_PADRAO));
            //      if (numCamada != camadas.size() - 1)
            //          RnaController.dosLogTempo.write(",".getBytes(Recursos.CHARSET_PADRAO));
        }
        //      RnaController.dosLogTempo.write('\n');
    }

    /**
     * Pega o resultado da iteração
     */
    public void getResultsIte(double[] resultVals) throws Exception {
        Camada outputLayer = camadas.get(camadas.size() - 1);
        if (resultVals.length != (outputLayer.getSizeListNeuronios() - 1)) {
            throw new ExceptionPlanejada("O número de neurônios de saída e a quantidade de saídas esperardas não sao iguais.");
        }
        int outputNeuronsNum = (outputLayer.getSizeListNeuronios() - 1); // Não leva em consideração o neurônio de bias pois estou na última camada, e ele foi criado apenas por conveniência no código 
        for (int neuronNum = 0; neuronNum < outputNeuronsNum; neuronNum++) {
            resultVals[neuronNum] = outputLayer.getNeuron(neuronNum).getValorSaida();
        }
    }

    public synchronized long getEpocaAtual() {
        return epocaAtual;
    }

    public synchronized void addEpoch() {
        this.epocaAtual++;
        atualizaEta();
    }

    public double getErroEpoca() {
        return erroEpoca;
    }

    public void resetErroEpoca() {
        this.erroEpoca = 0;
    }

    public List<Camada> getCamadas() {
        return camadas;
    }

    public Camada getUltimaCamada() {
        return camadas.get(camadas.size() - 1);
    }

    public Camada getPrimeiraCamada() {
        return camadas.get(0);
    }

    @Override
    public String toString() {
        return "Rna{" +
                "faixaInicialPesos=" + faixaInicialPesos +
                ", dropout=" + dropout +
                ", termoLinear=" + termoLinear +
                ", funcaoAtivacaoInterna=" + funcaoAtivacaoInterna +
                ", getFuncaoAtivacaoSaida=" + getFuncaoAtivacaoSaida +
                ", decaimentoTaxaAprendizado=" + decaimentoTaxaAprendizado +
                ", topologia=" + Arrays.toString(topologia) +
                ", camadas=" + camadas +
                ", epocaMaxima=" + epocaMaxima +
                ", erroAlvo=" + erroAlvo +
                ", taxaAprendizado=" + taxaAprendizado +
                ", taxaAprendizadoInicial=" + taxaAprendizadoInicial +
                ", momentum=" + momentum +
                ", erroIteracao=" + erroIteracao +
                ", erroEpoca=" + erroEpoca +
                ", epocaAtual=" + epocaAtual +
                '}';
    }

    public DecaimentoTaxaAprendizado getDecaimentoTaxaAprendizado() {
        return decaimentoTaxaAprendizado;
    }

    public double getTaxaAprendizadoInicial() {
        return taxaAprendizadoInicial;
    }

    public double getFaixaInicialPesos() {
        return faixaInicialPesos;
    }

    public boolean isDropout() {
        return dropout;
    }

    public double getTermoLinear() {
        return termoLinear;
    }

    public FuncaoTipo getFuncaoAtivacaoInterna() {
        return funcaoAtivacaoInterna;
    }

    public FuncaoTipo getGetFuncaoAtivacaoSaida() {
        return getFuncaoAtivacaoSaida;
    }

    public int[] getTopologia() {
        return topologia;
    }

    public long getEpocaMaxima() {
        return epocaMaxima;
    }

    public double getErroAlvo() {
        return erroAlvo;
    }

    public double getTaxaAprendizado() {
        return taxaAprendizado;
    }

    public double getMomentum() {
        return momentum;
    }

    public Double getErroIteracao() {
        return erroIteracao;
    }
}
