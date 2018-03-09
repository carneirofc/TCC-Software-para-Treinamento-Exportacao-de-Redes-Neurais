package ann.geral;

@FunctionalInterface
public interface DecaimentoTaxaAprendizado {
    double calc(double taxa, double taxaOriginal, double taxaMinima, double epocaAtual, double passo, double ganho);
}
