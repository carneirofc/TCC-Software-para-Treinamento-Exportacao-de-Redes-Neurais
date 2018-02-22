package main.gui.grafico;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import main.Ctrl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GraficoLinha implements Grafico {

    //private final Object token = new Object();

    public final XYChart.Series sTeste = new LineChart.Series<Number, Number>();
    public final XYChart.Series sTreino = new LineChart.Series<Number, Number>();


    private final int maxPontos = 120;

    private final NumberAxis eixoEpoca = new NumberAxis();
    private final NumberAxis eixoErro = new NumberAxis();

    private final LineChart<Number, Number> lineChart = new LineChart<>(eixoEpoca, eixoErro);


    public GraficoLinha(@NotNull String title, @NotNull String sTesteNome, @NotNull String sTreinoNome) {

        sTeste.setName(sTesteNome);
        sTreino.setName(sTreinoNome);

        SERIES_LIST.add(sTeste);
        SERIES_LIST.add(sTreino);

        eixoEpoca.setForceZeroInRange(false);
        eixoErro.setForceZeroInRange(false);

        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);
        lineChart.getData().addAll(sTeste, sTreino);
        lineChart.setTitle(title);
        lineChart.autosize();
    }

    public LineChart<Number, Number> getLineChart() {
        return lineChart;
    }


    @Override
    public void addPonto(@NotNull List<? extends Ponto> pontoList, @NotNull String nomeSerie) {

    }

    @Override
    public void addPonto(@NotNull Ponto ponto, @NotNull String nomeSerie) {

    }

    @Override
    public synchronized void addPonto(@NotNull List<? extends Ponto> pontoList, XYChart.@NotNull Series series) {
        if (Ctrl.isGuiMostrarGrafico()) {
            if (series.getData().size() == maxPontos)
                limparSerie(series);
            List<XYChart.Data<Double, Double>> data = new ArrayList<>();
            for (Ponto p : pontoList) {
                data.add(new XYChart.Data<>(p.getX(), p.getY()));
            }
            series.getData().setAll(data);
        }
    }

    @Override
    public synchronized void addPonto(@NotNull Ponto ponto, @NotNull XYChart.Series series) {
        if (Ctrl.isGuiMostrarGrafico()) {
            if (series.getData().size() >= maxPontos)
                limparSerie(series);

            series.getData().add(new XYChart.Data<>(ponto.getX(), ponto.getY()));
        }
    }

    @Override
    public synchronized void limparSeries() {
        for (XYChart.Series s : SERIES_LIST) {
            s.getData().clear();
        }
    }

    @Override
    public synchronized void limparSerie(@NotNull String nomeSerie) {
        for (XYChart.Series s : SERIES_LIST) {
            if (s.getName().equals(nomeSerie))
                s.getData().clear();
        }
    }

    @Override
    public synchronized void limparSerie(XYChart.@NotNull Series series) {
        for (XYChart.Series s : SERIES_LIST) {
            if (s.equals(series))
                s.getData().clear();
        }
    }
}
