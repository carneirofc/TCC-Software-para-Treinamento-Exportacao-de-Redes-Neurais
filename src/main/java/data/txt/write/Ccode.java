package data.txt.write;

import ann.controller.RnaController;
import ann.detalhes.Camada;
import ann.detalhes.Rna;
import data.ConjuntoDados;
import main.Ctrl;
import main.Recursos;
import main.utils.ExceptionPlanejada;
import main.utils.Utilidade;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Classe responsável por exportar a rede em C
 *
 * @author Claudio
 */
public class Ccode extends TxtWriter {
    private Rna rna;

    /**
     * Cabeçalho
     */
    private void printCabecalho(PrintWriter gravarArq) {
        gravarArq.println("/**\n"
                + "\tArquivo gerado automaticamente pelo software de treinamento. Por Cláudio Carneiro\n"
                + "\tInfo : " + System.getProperty("user.name") + "\n"
                + "\tData: " + new Date() + "\n\n"
                + "\tTopologia: " + Utilidade.getJson(rna.getTopologia()) + "\n"
                + "\tFuncao de ativacao nas camadas escondidas:" + rna.getFuncaoAtivacaoInterna().getStringFuncao(rna) + "\n"
                + "\tFuncao de ativacao nas saidas: " + rna.getGetFuncaoAtivacaoSaida().getStringFuncao(rna) +"\n"
                + "\tEta Inicial: " + rna.getTaxaAprendizadoInicial() + "\n"
                + "\tEta Atual: " + rna.getTaxaAprendizado() + "\n"
                + "\tEta Funcao de Decaimento: " + rna.getDecaimentoTaxaAprendizado().getNome() + "\n"
                + "\tEta Funcao de Decaimento ganho: " + rna.getTaxaDecaimentoGanho() + "\n"
                + "\tEta Funcao de Decaimento passo: " + rna.getTaxaDecaimentoPasso() + "\n"
                + "\tEta Funcao de Decaimento: " + rna.getDecaimentoTaxaAprendizado().getNome() + "\n"
                + "\tMomentum:" + rna.getMomentum() + "\n"
                + "\tEpoca maxima de treinamento: " + rna.getEpocaMaxima() + "\n"
                + "\tEpoca atual: " + rna.getEpocaAtual() + "\n"
                + "\tErro alvo durante treinamento: " + rna.getErroAlvo() + "\n"
                + "\tErro médio: " + rna.getErroEpoca() + "\n"
                + "\n\n"
                + "\tO nome das variaveis possui informacoes a respeito de sua localizacao na ANN. ex:\n"
                + "\tw010 -> o primeiro 0 indica a camada que o neuronio possuidor desse peso esta,\n"
                + "\to segundo numero,'1', indica o número do neurônio da camada (o ultimo neuronio e sempre um bias)\n"
                + "\to ultimo numero ,'0', indica qual a posicao do neuronio alvo localizado na camada seguinte. \n"
                + "\tAs variáveis de entrada estão na mesma ordem em que são encontradas no arquivo de treinamento.\n"
                + "\n" +
                "       Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:\n" +
                "   \n" +
                "       1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.\n" +
                "       2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer\n" +
                "           in the documentation and/or other materials provided with the distribution.\n" +
                "       3. Neither the name of the Institute nor the names of its contributors may be used to endorse or promote products derived from\n" +
                "           this software without specific prior written permission.\n" +
                " \n\n" +
                " THIS SOFTWARE IS PROVIDED BY THE INSTITUTE AND CONTRIBUTORS ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, \n" +
                " INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.\n" +
                " IN NO EVENT SHALL THE INSTITUTE OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, \n" +
                " OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,\n" +
                " DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, \n" +
                " STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,\n" +
                " EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE."
        );
        gravarArq.print("   Colunas de ENTRADA referente aos dados de treino :\n");
        for (Integer i : ConjuntoDados.dadosTreinamento.getColunasEntrada()) {
            gravarArq.print("   " + i);
        }
        gravarArq.println();
        gravarArq.print("   Colunas de SAIDA referente aos dados de treino :\n");
        for (Integer i : ConjuntoDados.dadosTreinamento.getColunasSaida()) {
            gravarArq.print("   " + i);
        }
        gravarArq.println("\n*/");

        gravarArq.println("#include <math.h>");
        gravarArq.println("#define NORM_MIN " + ConjuntoDados.getNormMin());
        gravarArq.println("#define NORM_MAX " + ConjuntoDados.getNormMax());

    }

    private void declaraPesos(PrintWriter gravarArq, List<Camada> layers) {
        for (int layerNum = 0; layerNum < layers.size() - 1; layerNum++) {
            gravarArq.println("\n// -------- Layer " + layerNum + " --------");

            gravarArq.printf("double ");
            //   gravarArq.printf("w" + layerNum + "_" + neuronNum + "[]={");
            int nNeuroniosCamada = layers.get(layerNum).getSizeListNeuronios();
            int nConexoesNeuronios = layers.get(layerNum).getNeuron(0).getOutputConnections().size();

            gravarArq.printf("w" + layerNum + "[" + nNeuroniosCamada + "][" + nConexoesNeuronios + "]={");
            for (int neuronNum = 0; neuronNum < nNeuroniosCamada; neuronNum++) {
                /*if (neuronNum == layers.getPorCod(layerNum).getListNeuronios().size() - 1) {
                    gravarArq.println("// Bias");
                } else {
                    gravarArq.println("// Neuron " + neuronNum);
                }*/
                gravarArq.printf("{");
                for (int conNeu = 0; conNeu < layers.get(layerNum).getNeuron(neuronNum).getOutputConnections().size(); conNeu++) {
                    gravarArq.printf(
                            String.valueOf(layers.get(layerNum).getNeuron(neuronNum).getOutputConnections().get(conNeu).getPeso())
                    );
                    if (conNeu != layers.get(layerNum).getNeuron(neuronNum).getOutputConnections().size() - 1) {
                        gravarArq.printf(",");
                    }
                }
                gravarArq.printf("}");

                if (neuronNum != nNeuroniosCamada - 1) {
                    gravarArq.printf(",");
                }

                if (neuronNum == nNeuroniosCamada - 1) {
                    gravarArq.println("// Bias");
                } else {
                    gravarArq.println("// Neuronio " + neuronNum);
                }
            }
            gravarArq.printf("};");
        }
    }

    /**
     * Declaração de variáveis globais
     */
    private void printDeclaracao(PrintWriter gravarArq, List<Camada> layers) {
        declaraPesos(gravarArq, layers);


        gravarArq.printf("double inMax[] = {");
        for (int i = 0; i < ConjuntoDados.getMaxEntrada().length; i++) {
            gravarArq.printf(" " + ConjuntoDados.getMaxEntrada()[i]);
            if (i != ConjuntoDados.getMaxEntrada().length - 1) {
                gravarArq.printf(",");
            }
        }
        gravarArq.println("};");

        gravarArq.printf("double inMin[] = {");
        for (int i = 0; i < ConjuntoDados.getMinEntrada().length; i++) {
            gravarArq.printf(" " + ConjuntoDados.getMinEntrada()[i]);
            if (i != ConjuntoDados.getMinEntrada().length - 1) {
                gravarArq.printf(",");
            }
        }
        gravarArq.println("};");

        gravarArq.printf("double outMaxD[] = {");
        for (int i = 0; i < ConjuntoDados.getMaxSaida().length; i++) {
            gravarArq.printf(" " + ConjuntoDados.getMaxSaida()[i]);
            if (i != ConjuntoDados.getMaxSaida().length - 1) {
                gravarArq.printf(",");
            }
        }
        gravarArq.println("};");

        gravarArq.printf("double outMinD[] = {");
        for (int i = 0; i < ConjuntoDados.getMinSaida().length; i++) {
            gravarArq.printf(" " + ConjuntoDados.getMinSaida()[i]);
            if (i != ConjuntoDados.getMinSaida().length - 1) {
                gravarArq.printf(",");
            }
        }
        gravarArq.println("};");

        gravarArq.println("// --------------------------------------------------------------------------------------");
    }

    private void printNormalizacao(PrintWriter printWriter) {
        printWriter.println("void normaliza(double *in, int i, double *outN){\n"
                + "\tdouble a = (NORM_MAX - NORM_MIN) / (inMax[i] - inMin[i]);\n"
                + "\tdouble b = ((-(NORM_MAX)) * inMin[i] + (NORM_MIN) * inMax[i]) / (inMax[i] - inMin[i]);\n"
                + "\t*outN = (a * *in + b);\n"
                + "}");
    }

    private void printDesnormalizacao(PrintWriter printWriter) {
        printWriter.println("void desnormaliza(double x, int i, double *resNorm){\n"
                + "\tdouble a = (NORM_MAX - (NORM_MIN)) / (outMaxD[i] - outMinD[i]);\n"
                + "\tdouble b = (-(NORM_MAX) * outMinD[i] + (NORM_MIN) * outMaxD[i]) / (outMaxD[i] - outMinD[i]);\n"
                + "\t*resNorm = (x - b) / a;\n"
                + "}");
    }

    private void printRedeNeuralFeedForward(PrintWriter gravarArq, List<Camada> layers) {

        gravarArq.printf("void reden_(");
        for (int i = 0; i < ConjuntoDados.dadosTreinamento.getNeuroniosEntradaQtd(); i++) {
            gravarArq.printf("double *inExt" + i + ",");
        }
        for (int i = 0; i < ConjuntoDados.dadosTreinamento.getNeuroniosSaidaQtd(); i++) {
            gravarArq.printf("double *out" + i);

            if (i != (ConjuntoDados.dadosTreinamento.getNeuroniosSaidaQtd() - 1)) {
                gravarArq.printf(",");
            }
        }
        gravarArq.println("){");

        for (int layer = 1; layer < layers.size() - 1; layer++) {
            gravarArq.printf("      double   in" + layer + "[]={");
            for (int neuronNum = 0; neuronNum < layers.get(layer).getListNeuronios().size() - 1; neuronNum++) {
                gravarArq.printf("0");
                if (neuronNum != layers.get(layer).getListNeuronios().size() - 2) {
                    gravarArq.printf(",");
                }
            }
            gravarArq.println("};");
        }


        // Saídas dos neurônios ... 
        gravarArq.printf("double ");
        for (int i = 0; i < ConjuntoDados.dadosTreinamento.getNeuroniosSaidaQtd(); i++) {
            gravarArq.printf(" out" + i + "D" + " = 0");
            if (i != ConjuntoDados.dadosTreinamento.getNeuroniosSaidaQtd() - 1) {
                gravarArq.printf(",");
            } else {
                gravarArq.println(";");
            }
        }

        // Variáveis p/ as entradas normalizadas
        gravarArq.printf("double ");
        for (int i = 0; i < ConjuntoDados.dadosTreinamento.getNeuroniosEntradaQtd(); i++) {
            gravarArq.printf(" in" + i + "N" + " = 0");
            if (i != ConjuntoDados.dadosTreinamento.getNeuroniosEntradaQtd() - 1) {
                gravarArq.printf(",");
            } else {
                gravarArq.println(";");
            }
        }
        //Normalização
        for (int i = 0; i < ConjuntoDados.dadosTreinamento.getNeuroniosEntradaQtd(); i++) {
            gravarArq.printf("normaliza(inExt" + i + "," + i + "," + "&in" + i + "N);\n");
        }

        // Fim norm entrada 
        // Primeira Camada ...
        for (int neuronNum = 0; neuronNum < layers.get(1).getListNeuronios().size() - 1; neuronNum++) {
            gravarArq.printf("actvFunc((");
            for (int i = 0; i < ConjuntoDados.dadosTreinamento.getNeuroniosEntradaQtd(); i++) {
                gravarArq.printf(" in" + i + "N" + " * w0[" + i + "][" + neuronNum + "] + ");
            }
            gravarArq.printf(
                    layers.get(1).getBias().getValorSaida() + "*w0[" + ConjuntoDados.dadosTreinamento.getNeuroniosEntradaQtd() + "][" + neuronNum + "]");
            gravarArq.printf("), &in1[" + neuronNum + "]");
            gravarArq.println(");");
        }

        // Demais camadas escondidas ....
        for (int layer = 2; layer < layers.size() - 1; layer++) {

            for (int neuronNum = 0; neuronNum < layers.get(layer).getListNeuronios().size() - 1; neuronNum++) {
                int layerAntes = layer - 1;
                gravarArq.printf("actvFunc((");
                for (int i = 0; i < layers.get(layerAntes).getSizeListNeuronios() - 1; i++) {
                    gravarArq.printf(" in" + layerAntes + "[" + i + "] *w" + layerAntes + "[" + i + "][" + neuronNum + "] + ");
                }
                int n = layers.get(layerAntes).getListNeuronios().size() - 1;
                gravarArq.printf(
                        layers.get(layerAntes).getBias().getValorSaida() + "*w" + layerAntes + "[" + n + "][" + neuronNum + "]");
                gravarArq.printf("), &in" + layer + "[" + neuronNum + "]");
                gravarArq.println(");");
            }
        }

        // Última camada ...
        Camada penultimaCamada = layers.get(layers.size() - 2);
        int penultimaCamadaNum = (layers.size() - 2);
        Camada ultimaCamada = layers.get(layers.size() - 1);

        // Função de Ativação da Saída
        for (int neuronNum = 0; neuronNum < (ultimaCamada.getListNeuronios().size() - 1); neuronNum++) {
            gravarArq.printf("actvFuncSaida( (");
            for (int i = 0; i < penultimaCamada.getSizeListNeuronios() - 1; i++) {
                gravarArq.printf(" in" + penultimaCamadaNum + "[" + i + "] *w" + penultimaCamadaNum + "[" + i + "][" + neuronNum + "] + ");
            }
            int n = penultimaCamada.getListNeuronios().size() - 1;
            gravarArq.printf(
                    penultimaCamada.getBias().getValorSaida() + "*w" + penultimaCamadaNum + "[" + n + "][" + neuronNum + "]");
            gravarArq.println("),  &out" + neuronNum + "D);");
        }

        // Desnormalizando as Saídas da RNA e Enviando de Volta
        for (int neuronNum = 0; neuronNum < (ultimaCamada.getListNeuronios().size() - 1); neuronNum++) {
            gravarArq.printf("desnormaliza(  ");
            gravarArq.println("out" + neuronNum + "D , " + neuronNum + ", &out" + neuronNum + "D);");
        }
        for (int i = 0; i < ConjuntoDados.dadosTreinamento.getNeuroniosSaidaQtd(); i++) {
            gravarArq.printf(" *out" + i + " =  out" + i + "D;");
        }
        gravarArq.printf("\n}");
    }

    private void printFuncAtivacaoInterna(PrintWriter gravarArq) {
        gravarArq.println("void actvFunc(double x, double *outVal){" + rna.getFuncaoAtivacaoInterna().getStringFuncao(rna) + "}\n");
    }

    private void printFuncAtivacaoSaida(PrintWriter gravarArq) {
        gravarArq.println("void actvFuncSaida(double x, double *outVal){" + rna.getGetFuncaoAtivacaoSaida().getStringFuncao(rna) + "}\n");
    }


    /**
     * Exporta a rede neural em uma função em c
     *
     * @param file arquivo a ser criado
     */
    public void salvarPesosC(File file) throws Exception {
        if (!Ctrl.isRnaEmExecucao() && Ctrl.isRnaTreinada() && Ctrl.isDadosTreinoCarregados() && Ctrl.isPropertiesDadosCarregados()) {
            rna = RnaController.getRna();
            Objects.requireNonNull(rna, "A RNA não foi inicializada.");

            criaWriter(file, Recursos.EXTENSION_FILTER_C);

            List<Camada> layers = rna.getCamadas();

            printCabecalho(getPrintWriter());
            printDeclaracao(getPrintWriter(), layers);

            printNormalizacao(getPrintWriter());
            printDesnormalizacao(getPrintWriter());
            printFuncAtivacaoInterna(getPrintWriter());
            printFuncAtivacaoSaida(getPrintWriter());

            printRedeNeuralFeedForward(getPrintWriter(), layers);

            finaliza();

        } else {
            throw new ExceptionPlanejada("Para isso a rede já deve estar treinada e com todos os dados devidamente carregados (Dados de treinamento e Arquivo de Propriedades).");
        }
    }
}
