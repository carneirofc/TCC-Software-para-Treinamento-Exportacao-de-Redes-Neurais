package ann.detalhes;

import java.io.Serializable;
import java.util.Random;

/**
 * "Conexão de um neurônio com outro"
 *
 * @author Claudio
 */
public class Pesos implements Serializable {

    private double peso;
    private double deltaPeso;

    public Pesos(double faixaInicial) {
        peso = (new Random().nextGaussian()) * faixaInicial;
        deltaPeso = 0;
    }

    public double getPeso() {
        return peso;
    }

    public void updateWeight(double deltaWeight) {
        this.peso += deltaWeight;
    }

    public double getDeltaPeso() {
        return deltaPeso;
    }

    public void setDeltaPeso(double deltaPeso) {
        this.deltaPeso = deltaPeso;
    }

}
