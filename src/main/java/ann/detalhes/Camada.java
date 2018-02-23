package ann.detalhes;

import java.util.ArrayList;
import java.util.List;

/**
 * Camada "n" da rede neural
 *
 * @author Claudio
 */
public class Camada {

    private List<Neuronio> listNeuronios = new ArrayList<>();

    /**
     * Adiciona um neurônio a camada.
     */
    public void addNeuron(Neuronio neuron) {
        listNeuronios.add(neuron);
    }

    public Neuronio getNeuron(int pos) {
        return listNeuronios.get(pos);
    }

    /**
     * Retorna o último neurônio criado na camada. Este deve ser o neurônio
     * BIAS.
     */
    public Neuronio getBias() {
        return listNeuronios.get(listNeuronios.size() - 1);
    }

    public List<Neuronio> getListNeuronios() {
        return listNeuronios;
    }

    /**
     * @return listNeuronios.size();
     */
    public int getSizeListNeuronios() {
        return listNeuronios.size();
    }

}
