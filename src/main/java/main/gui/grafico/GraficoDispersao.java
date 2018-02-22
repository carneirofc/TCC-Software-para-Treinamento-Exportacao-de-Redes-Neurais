package main.gui.grafico;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import main.Ctrl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GraficoDispersao implements Grafico {

    public final XYChart.Series sTeste = new ScatterChart.Series<Number, Number>();

    private final NumberAxis eixoEpoca = new NumberAxis();
    private final NumberAxis eixoErro = new NumberAxis();

    private final ScatterChart<Number, Number> scatterChart = new ScatterChart<>(eixoEpoca, eixoErro);

    public GraficoDispersao(String title, String sTesteNome) {

        sTeste.setName(sTesteNome);

        SERIES_LIST.add(sTeste);

        eixoEpoca.setForceZeroInRange(false);
        eixoErro.setForceZeroInRange(true);

        scatterChart.getData().add(sTeste);
        scatterChart.setAnimated(false);
        scatterChart.setTitle(title);
        scatterChart.autosize();
    }

    public ScatterChart<Number, Number> getScatterChart() {
        return scatterChart;
    }

    @Override
    public void addPonto(@NotNull List<? extends Ponto> pontoList, @NotNull String nomeSerie) {

    }

    @Override
    public void addPonto(@NotNull Ponto ponto, @NotNull String nomeSerie) {

    }

    @Override
    public synchronized void addPonto(@NotNull List<? extends Ponto> pontoList, @NotNull XYChart.Series series) {
        if (Ctrl.isGuiMostrarGrafico()) {
            List<XYChart.Data<Double, Double>> data = new ArrayList<>();
            for (Ponto p : pontoList) {
                data.add(new ScatterChart.Data<>(p.getX(), p.getY()));
            }
            series.getData().setAll(data);
        }
    }

    public synchronized List<Ponto> getPontos(double min, double max) {
        List<Ponto> pontos = new ArrayList<>();
        try {
            for (XYChart.Data data : scatterChart.getData().get(0).getData()) {
                if ((double) data.getYValue() >= min && (double) data.getYValue() <= max) {
                    pontos.add(new Ponto((double) data.getXValue(), (double) data.getYValue()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pontos;
    }

    @Override
    public synchronized void addPonto(@NotNull Ponto ponto, XYChart.@NotNull Series series) {
        if (Ctrl.isGuiMostrarGrafico()) {
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

    }

    @Override
    public synchronized void limparSerie(XYChart.@NotNull Series series) {
        for (XYChart.Series s : SERIES_LIST) {
            if (s.equals(series))
                s.getData().clear();
        }
    }
}
