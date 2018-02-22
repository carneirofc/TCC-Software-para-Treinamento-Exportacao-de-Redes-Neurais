package ann.detalhes;

import java.util.ArrayList;
import java.util.List;

/**
 * Camada "n" da rede neural
 *
 * @author Claudio
 */
public class CamadaRna {

    private List<NeuronioRna> listNeuronios = new ArrayList<>();

    /**
     * Adiciona um neurônio a camada.
     */
    public void addNeuron(NeuronioRna neuron) {
        listNeuronios.add(neuron);
    }

    public NeuronioRna getNeuron(int pos) {
        return listNeuronios.get(pos);
    }

    /**
     * Retorna o último neurônio criado na camada. Este deve ser o neurônio
     * BIAS.
     */
    public NeuronioRna getBias() {
        return listNeuronios.get(listNeuronios.size() - 1);
    }

    public List<NeuronioRna> getListNeuronios() {
        return listNeuronios;
    }

    /**
     * @return listNeuronios.size();
     */
    public int getSizeListNeuronios() {
        return listNeuronios.size();
    }

}
