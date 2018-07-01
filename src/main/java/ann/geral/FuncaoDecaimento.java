package ann.geral;

import java.io.Serializable;

public enum FuncaoDecaimento implements Serializable {
    NENHUM("Sem Decaimento",
            (taxa, taxaOriginal, taxaMinima, epocaAtual, passo, ganho) -> taxaOriginal),
    DEGRAU("Percentual por n épocas: taxa *= ganho",
            (taxa, taxaOriginal, taxaMinima, epocaAtual, passo, ganho) -> {
                if (passo == 0) {
                    return taxa;
                }
                if (epocaAtual % passo == 0) {
                    taxa *= ganho;
                }
                return (taxa > taxaMinima) ? taxa : taxaMinima;
            }),

    METADE("Metade por n épocas: taxa /= 2",
            (taxa, taxaOriginal, taxaMinima, epocaAtual, passo, ganho) -> {
                if (passo == 0) {
                    return taxa;
                }
                if (epocaAtual % passo == 0) {
                    taxa /= 2;
                }
                return (taxa > taxaMinima) ? taxa : taxaMinima;
            }),

    GANHO_PROPORCIONAL("Porporcional: taxa/(1 + ganho * taxaOriginal)",
            (taxa, taxaOriginal, taxaMinima, epocaAtual, passo, ganho) -> {
                if (passo == 0 || (1 + ganho * taxaOriginal) == 0) {
                    return taxa;
                }
                if (epocaAtual % passo == 0) {
                    taxa = taxa / (1 + ganho * taxaOriginal);
                }
                return (taxa > taxaMinima) ? taxa : taxaMinima;
            });

    public final String nome;
    public final DecaimentoTaxaAprendizado novoEta;

    FuncaoDecaimento(String nome, DecaimentoTaxaAprendizado novoEta) {
        this.nome = nome;
        this.novoEta = novoEta;
    }

    public String getNome() {
        return nome;
    }

    public DecaimentoTaxaAprendizado getNovoEta() {
        return novoEta;
    }
}
