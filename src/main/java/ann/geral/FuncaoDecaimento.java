package ann.geral;

public enum FuncaoDecaimento {
    NENHUM("Sem Decaimento",
            (taxa, taxaOriginal, epocaAtual, passo, ganho) -> taxaOriginal),
    DEGRAU("Percentual por n épocas: taxa *= ganho",
            (taxa, taxaOriginal, epocaAtual, passo, ganho) -> {
                if (passo == 0) {
                    return taxa;
                }
                if (epocaAtual % passo == 0) {
                    taxa *= ganho;
                }
                return taxa;
            }),

    METADE("Metade por n épocas: taxa /= 2",
            (taxa, taxaOriginal, epocaAtual, passo, ganho) -> {
                if (passo == 0) {
                    return taxa;
                }
                if (epocaAtual % passo == 0) {
                    taxa /= 2;
                }
                return taxa;
            }),
    GANHO_PROPORCIONAL("Porporcional: taxa/(1 + ganho * taxaOriginal)",
            (taxa, taxaOriginal, epocaAtual, passo, ganho) -> {
                if (passo == 0 || (1 + ganho * taxaOriginal) == 0) {
                    return taxa;
                }
                if (epocaAtual % passo == 0) {
                    taxa = taxa / (1 + ganho * taxaOriginal);
                }
                return taxa;
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
