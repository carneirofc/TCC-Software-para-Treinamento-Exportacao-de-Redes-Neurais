package ann.detalhes;

import ann.controller.RnaController;
import ann.worker.Worker;
import data.ConjuntoDados;
import main.Ctrl;
import main.Recursos;
import main.utils.ExceptionPlanejada;

import java.util.ArrayList;
import java.util.List;

/**
 * Visão "macro" da rede
 *
 * @author Claudio
 */
public class Rna {

    /**
     * Lock utilizado parea multithreading
     */
    private Object lock = new Object();
    private Double rms = 0.0;
    /**
     * Camadas da rede neural.
     */
    private final List<CamadaRna> camadas = new ArrayList<>();
    /**
     * Erro
     */
    private double erroEpoca = 0;

    /**
     * Época atual da rede (treinamento)
     */
    private long epochAtual = 0;

    /**
     * Contrutor da rede. Informa ao sistema que a rede não foi treinada, cria
     * as camadas e os neurônios.
     *
     * @param dopout   diz se a rna será treinada utilizando a técnica de dropout ou não ...
     * @param topology vetor contendo a topologia da rna. Nº  de colunas representam as camadas e seus valores a quantidade de neurônios.
     */
    public Rna(final int[] topology, boolean dopout) {
        this.dropout = dopout;
        Ctrl.setRnaTreinada(false);
        inicializaLayers(topology);
    }

    public Object getLock() {
        return lock;
    }

    /**
     * Indica se a rede foi treinada utilizando a técnica de dropout
     */
    private boolean dropout;

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
            CamadaRna camadaRna = new CamadaRna();

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
                camadaRna.addNeuron(new NeuronioRna(numNeuroniosProxLayer, neuronNum));
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
        CamadaRna outputLayer = getUltimaCamada();
        rms = 0.0;
        for (int neuronNum = 0; neuronNum < (ConjuntoDados.dadosTreinamento.getNeuroniosSaidaQtd()); neuronNum++) {
            rms += Math.abs(targetVals[neuronNum] - outputLayer.getNeuron(neuronNum).getValorSaida());
        }
        rms /= ConjuntoDados.dadosTreinamento.getNeuroniosSaidaQtd();
        //  rms = Math.sqrt(rms / DadosRna.getNeuroniosSaidaQtd());
        return rms;
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

        erroEpoca += rms;
        erroEpoca /= 2;
    }

    /**
     * Realiza os cálculos de backpropagation (treinamento ....)
     */
    public void backPropagation(double[] targetVals) throws Exception {

        CamadaRna outputLayer = camadas.get(camadas.size() - 1);
        CamadaRna prevOutLayer = camadas.get(camadas.size() - 2);

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
            CamadaRna camada = camadas.get(camadaEscondida);
            CamadaRna camadaSeguinte = camadas.get(camadaEscondida + 1);
            CamadaRna camadaAnterior = camadas.get(camadaEscondida - 1);

            for (int neuronio = 0; neuronio < camada.getSizeListNeuronios(); neuronio++) {
                NeuronioRna neuronioRna = camada.getNeuron(neuronio);
                neuronioRna.calculaGradiente(camadaSeguinte);
                if (neuronio != (camada.getSizeListNeuronios() - 1)) { // O último neurônio é o bias. Não possui conexão de entrada.
                    neuronioRna.atualizaPesos(camadaAnterior);
                }
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
        CamadaRna outputLayer = camadas.get(camadas.size() - 1);
        CamadaRna prevOutLayer = camadas.get(camadas.size() - 2);

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
            CamadaRna camada = camadas.get(camadaEscondida);
            CamadaRna camadaSeguinte = camadas.get(camadaEscondida + 1);
            CamadaRna camadaAnterior = camadas.get(camadaEscondida - 1);

            for (int neuronio = 0; neuronio < camada.getSizeListNeuronios(); neuronio++) {
                NeuronioRna neuronioRna = camada.getNeuron(neuronio);
                neuronioRna.calculaGradiente(camadaSeguinte);
                if (neuronio != (camada.getSizeListNeuronios() - 1)) { // O último neurônio é o bias. Não possui conexão de entrada.
                    neuronioRna.atualizaPesos(camadaAnterior);
                }
            }
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
     * Realiza os cálculos de backpropagation (treinamento ....)
     */
    public void backPropagationLogMultithread(double[] targetVals) throws Exception {
        //  long antes, depois;
        //  antes = System.nanoTime();
        CamadaRna outputLayer = camadas.get(camadas.size() - 1);
        CamadaRna prevOutLayer = camadas.get(camadas.size() - 2);

        /*
              Cálculo dosLogTempo componentes do vetor gradiente e atualização dosLogTempo pesos de entrada
               nos neurônios da camada de saída.  (ref[i] - saídaNeuron[i])*(DxFt(entradaNeuron[i]))
         */
        for (int neuronNum = 0; neuronNum < (outputLayer.getSizeListNeuronios() - 1); neuronNum++) {
            outputLayer.getNeuron(neuronNum).calculaGradienteSaida(targetVals[neuronNum]);
            outputLayer.getNeuron(neuronNum).atualizaPesos(prevOutLayer);
        }
        // depois = System.nanoTime();
        //  RnaController.dosLogTempo.write(Long.toString(depois - antes).getBytes(Recursos.CHARSET_PADRAO));
        //  RnaController.dosLogTempo.write(",".getBytes(Recursos.CHARSET_PADRAO));

        /*
               Cálculo dosLogTempo gradientes parciais das camadas escondidas (gradiente local)
            e atualização dosLogTempo pesos de entrada.
         */
        for (int camadaEscondida = camadas.size() - 2; camadaEscondida > 0; --camadaEscondida) {
            //      antes = System.nanoTime();
            CamadaRna camada = camadas.get(camadaEscondida);
            CamadaRna camadaSeguinte = camadas.get(camadaEscondida + 1);
            CamadaRna camadaAnterior = camadas.get(camadaEscondida - 1);

            Worker.addWorkerToPool(camada, camadaSeguinte, camadaAnterior);
            // Worker.setCamadas(camada, camadaSeguinte, camadaAnterior);

            // Esperando ser notificada pelas Worker Threads ...
            synchronized (lock) {
                if (Worker.atomicInteger.get() != 0) {
                    lock.wait();
                }
            }
            /*
            for (int neuronio = 0; neuronio < camada.getSizeListNeuronios(); neuronio++) {
                NeuronioRna neuronioRna = camada.getNeuron(neuronio);
                neuronioRna.calculaGradiente(camadaSeguinte);
                if (neuronio != (camada.getSizeListNeuronios() - 1)) { // O último neurônio é o bias. Não possui conexão de entrada.
                    neuronioRna.atualizaPesos(camadaAnterior);
                }
            }
*/

            // depois = System.nanoTime();
            //  RnaController.dosLogTempo.write(Long.toString(depois - antes).getBytes(Recursos.CHARSET_PADRAO));
            //   if (camadaEscondida != 1) {
            //        RnaController.dosLogTempo.write(",".getBytes(Recursos.CHARSET_PADRAO));
            //     }
        }
        //   RnaController.dosLogTempo.write("\n".getBytes(Recursos.CHARSET_PADRAO));
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
            CamadaRna camadaAnterior = camadas.get(numCamada - 1);
            CamadaRna camadaAtual = camadas.get(numCamada);
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
            CamadaRna camadaAnterior = camadas.get(numCamada - 1);
            CamadaRna camadaAtual = camadas.get(numCamada);
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
        CamadaRna outputLayer = camadas.get(camadas.size() - 1);
        if (resultVals.length != (outputLayer.getSizeListNeuronios() - 1)) {
            throw new ExceptionPlanejada("O número de neurônios de saída e a quantidade de saídas esperardas não sao iguais.");
        }
        int outputNeuronsNum = (outputLayer.getSizeListNeuronios() - 1); // Não leva em consideração o neurônio de bias pois estou na última camada, e ele foi criado apenas por conveniência no código 
        for (int neuronNum = 0; neuronNum < outputNeuronsNum; neuronNum++) {
            resultVals[neuronNum] = outputLayer.getNeuron(neuronNum).getValorSaida();
        }
    }

    public synchronized long getEpochAtual() {
        return epochAtual;
    }

    public synchronized void addEpoch() {
        this.epochAtual++;
    }

    public double getErroEpoca() {
        return erroEpoca;
    }

    public void resetErroEpoca() {
        this.erroEpoca = 0;
    }

    public List<CamadaRna> getCamadas() {
        return camadas;
    }

    public CamadaRna getUltimaCamada() {
        return camadas.get(camadas.size() - 1);
    }

    public CamadaRna getPrimeiraCamada() {
        return camadas.get(0);
    }

    @Override
    public String toString() {
        return "Rna{" +
                "lock=" + lock +
                ", rms=" + rms +
                ", camadas=" + camadas +
                ", erroEpoca=" + erroEpoca +
                ", epochAtual=" + epochAtual +
                ", dropout=" + dropout +
                '}';
    }
}
