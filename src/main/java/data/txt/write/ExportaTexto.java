package data.txt.write;

import com.google.gson.Gson;
import data.ConjuntoDados;
import main.Ctrl;
import main.Main;
import main.Recursos;
import main.config.ConfigGeral;
import main.gui.controller.Teste;
import main.gui.grafico.Ponto;
import main.utils.ExceptionPlanejada;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExportaTexto extends TxtWriter {
    public void salvarConfigDadosJson(File file) throws Exception {
        criaWriter(file, Recursos.EXTENSION_FILTER_JSON);
        Gson gson = new Gson();
        getPrintWriter().print(gson.toJson(ConfigGeral.getConfigGeralAtual()));
        finaliza();
    }

    public void salvarDados(File file, String s) throws Exception {
        criaWriter(file, Recursos.EXTENSION_FILTER_TXT);
        getPrintWriter().print(s);
        finaliza();
    }

    public void geraDadosTeste(File file, double min, double max) throws Exception {
        if (Ctrl.isDadosTreinoCarregados()) {
         /*   double dadosEntrada[][] = ConjuntoDados.dadosTreinamento.getDadosEntrada();
            double dadosSaida[][] = ConjuntoDados.dadosTreinamento.getDadosSaida();
            if (dadosEntrada[0].length != dadosSaida[0].length) {
                throw new ExceptionPlanejada("Tamanho do conjunto de entrada e saída diverge. Verificar os dados de treinamento.");
            }
*/
            StringBuilder stringBuilder = new StringBuilder();

            List<Ponto> pontos = Teste.GRAFICO_DISPERSAO.getPontos(min, max);

            Integer tam = Collections.max(ConjuntoDados.getColunasEntrada());
            if (tam < Collections.max(ConjuntoDados.getColunasSaida())) {
                tam = Collections.max(ConjuntoDados.getColunasSaida());
            }
            tam++;

            List<Double[]> doubles = new ArrayList<>();

            for (Ponto ponto : pontos) {
                Double d[] = new Double[tam];
                for(int i=0; i < tam; i++){
                    d[i] = 0.0;
                }
                double dI[] = ConjuntoDados.dadosTeste.getDadosEntrada()[(int) ponto.getX()];
                double dO[] = ConjuntoDados.dadosTeste.getDadosSaida()[(int) ponto.getX()];

                for (int i = 0; i < ConjuntoDados.getColunasEntrada().size(); i++) {
                    System.out.println(ConjuntoDados.getColunasEntrada().get(i)+"="+dI[i]);
                    d[ConjuntoDados.getColunasEntrada().get(i)] = dI[i];
                    //d[ConjuntoDados.getColunasEntrada().get(i)] = dI[i];
                }
                for (int i = 0; i < ConjuntoDados.getColunasSaida().size(); i++) {
                    System.out.println(ConjuntoDados.getColunasSaida().get(i)+"="+dO[i]);
                    d[ConjuntoDados.getColunasSaida().get(i)] = dO[i];
                    //d[ConjuntoDados.getColunasEntrada().get(i)] = dI[i];
                }/*
                for (int i = 0; i < dO.length; i++) {
                    d[ConjuntoDados.getColunasSaida().get(i)] = dO[i];
                }*/
                doubles.add(d);
                System.out.println("========");
            }
            for (Double d[] : doubles) {
                for (double s : d) {
                    stringBuilder.append(s);
                    stringBuilder.append(' ');
                }
                stringBuilder.append('\n');
            }

            criaWriter(file, Recursos.EXTENSION_FILTER_TXT);
            getPrintWriter().printf(stringBuilder.toString());
            finaliza();
        } else {
            throw new ExceptionPlanejada("Dados de Treino não foram carregados.");
        }
    }
}
