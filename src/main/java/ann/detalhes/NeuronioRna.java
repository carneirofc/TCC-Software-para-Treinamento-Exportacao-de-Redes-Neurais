package ann.detalhes;

import ann.geral.FuncaoAtivacao;
import ann.geral.ConfiguracoesRna;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Um único neurônio, realiza as operações matemáticas. Sempre o primeiro
 * neurônio em uma camada é o BIAS.
 *
 * @author Claudio
 */
public class NeuronioRna {
    /**
     * Usado no processo de dropout
     */
    private static final Random random = new Random();
    /**
     * O identificador do neurônio em sua camada.
     */
    private final int meuIndex;
    /**
     * Valor de saída do neurônio. Valor final.
     */
    private double valorSaida;
    /**
     * Valor de entrada do neurônio. É o valor antes de ser aplicada a função de ativação, ou seja,
     * é o somatório das saídas dos neurônios das camadas anteriores vezes seus respectivos pesos relativos a este neurônio
     */
    private double valorEntradaTotal;
    private double gradient;

    private boolean dropped = false;

    private List<ConexaoPesosRna> outputConnections = new ArrayList<>();

    /**
     * Inicializa um neurônio com suas conexões
     */
    public NeuronioRna(int qtdSaidas, int meuIndex) {
        this.meuIndex = meuIndex;
        for (int connectionNum = 0; connectionNum < qtdSaidas; connectionNum++) {
            outputConnections.add(new ConexaoPesosRna());
        }
    }

    public ConexaoPesosRna getConnection(int connectionNum) {
        return outputConnections.get(connectionNum);
    }

    /**
     * FeedForward. Recebe as informações dos neurônios da camada anterior,
     * calcula o somatório das entradas*pesos e depois aplica a função de
     * ativação. A quantidade de iterações é proporcional ao número de neurônios
     * na camada anterior.
     *
     * @param ultimaCamada se é a camada de saída (para dar liberdade de usar
     *                     funções de ativação diferentes)
     */
    public void feedForward(CamadaRna camadaAnterior, boolean ultimaCamada) {
        calValEntradaFeedForward(camadaAnterior);
        if (ultimaCamada) {
            valorSaida = FuncaoAtivacao.funcAtivacaoSaida(valorEntradaTotal);
        } else {
            valorSaida = FuncaoAtivacao.funcAtivacao(valorEntradaTotal);
        }
    }

    private void calValEntradaFeedForward(CamadaRna camadaAnterior) {
        valorEntradaTotal = 0.0;
        for (int i = 0; i < camadaAnterior.getSizeListNeuronios(); i++) {
            valorEntradaTotal += camadaAnterior.getNeuron(i).getValorSaida()
                    * camadaAnterior.getNeuron(i).getOutputConnections().get(meuIndex).getPeso();
        }
    }

    /**
     * Caso a rede esteja configurarda para treino usando a técnica de dropout
     */
    public void feedForwardDropout(CamadaRna camadaAnterior, boolean ultimaCamada, boolean emTreinamento) {
        if (emTreinamento) {
            if (ultimaCamada) {
                calValEntradaFeedForward(camadaAnterior);
                valorSaida = FuncaoAtivacao.funcAtivacaoSaida(valorEntradaTotal);
            } else {
                if (random.nextBoolean()) {
                    calValEntradaFeedForward(camadaAnterior);
                    valorSaida = FuncaoAtivacao.funcAtivacao(valorEntradaTotal);
                    dropped = false;
                } else {
                    dropped = true;
                    valorSaida = 0;
                }
            }
        } else {
            calValEntradaFeedForward(camadaAnterior);
            if (ultimaCamada) {
                valorSaida = (FuncaoAtivacao.funcAtivacaoSaida(valorEntradaTotal) / 2); // Ao usar deve-se utilizar a metade do valor. Técnica de dropout
            } else {
                valorSaida = (FuncaoAtivacao.funcAtivacao(valorEntradaTotal) / 2);
            }
        }
    }

    /**
     * Cálculo do gradiente para neurônios pertencentes à camada de saída.
     * gradiente parcial = erro local * derivada da entrada deste neurônio Sendo
     * que o erro local é dado por: (resultado esperado deste neurônio -
     * resultado deste neurônio)
     *
     * @param valorAlvo valor que deveria ser o "correto", utilizado para o cálculo do erro.
     */
    public void calculaGradienteSaida(double valorAlvo) {
        gradient = (valorAlvo - valorSaida) * FuncaoAtivacao.funcAtivacaoDerivadaSaida(valorEntradaTotal);
    }

    /**
     * Cálculo do gradiente local de um neurônio pertencente a uma camada
     * escondida.
     * <p>
     * Para o cálculo do gradiente de uma camada escondida, é feito com:
     * <p>
     * Gradiente = (Somatório dosLogTempo gradientes dosLogTempo neurônios da próxima camada * o
     * respectivo peso da conexão com este neurônio.) * derivada da função de
     * ativação com o valor que este neurônio recebeu de entrada (valor antes de
     * ser aplicada a Tf)
     */
    public void calculaGradiente(CamadaRna nextLayer) {
        gradient = 0;
        if (!dropped) {
            for (int neuronio = 0; neuronio < (nextLayer.getSizeListNeuronios() - 1); neuronio++) {
                gradient += (outputConnections.get(neuronio).getPeso()
                        * nextLayer.getNeuron(neuronio).getGradient()
                        * FuncaoAtivacao.funcAtivacaoDerivada(valorEntradaTotal));
            }
        }
    }

    /**
     * Atualiza os pesos que conectam a esta camada em questão.
     *
     * @param prevLayer camada anterior a camada em que o neurônio referido
     *                  está. Os pesos a serem atualizados estão contidos nela (pesos das
     *                  conexões de grasaída dos neurônios)
     */
    public void atualizaPesos(CamadaRna prevLayer) {
        for (int neuronNum = 0; neuronNum < prevLayer.getSizeListNeuronios(); neuronNum++) {
            NeuronioRna prevNuron = prevLayer.getNeuron(neuronNum);
            double oldDetaWeight = prevNuron.getConnection(meuIndex).getDeltaPeso();
            /*
             * O novo peso é composto do peso anterior + saída do neurônio da cvamada anterior*taxa de apendizado* gradiente do neurônio em questão
            (o que os pesos apontam) + momentum* delta da última variação dos pesos
             */
            //  double newDeltaWeight = eta * prevNuron.getValorSaida() * gradient + momentum * oldDetaWeight;
            double newDeltaWeight = ConfiguracoesRna.getEta() * prevNuron.getValorSaida() * gradient + ConfiguracoesRna.getMomentum() * oldDetaWeight;
            prevNuron.getConnection(meuIndex).setDeltaPeso(newDeltaWeight);
            prevNuron.getConnection(meuIndex).updateWeight(newDeltaWeight);
        }
    }

    public double getValorSaida() {
        return valorSaida;
    }

    public void setValorSaida(double valorSaida) {
        this.valorSaida = valorSaida;
    }

    public List<ConexaoPesosRna> getOutputConnections() {
        return outputConnections;
    }

    public double getGradient() {
        return gradient;
    }
}
