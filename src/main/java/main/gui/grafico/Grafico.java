package main.gui.grafico;

import javafx.scene.chart.XYChart;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface Grafico {
    List<XYChart.Series> SERIES_LIST = new ArrayList<>();

    void addPonto(@NotNull List<? extends Ponto> pontoList, @NotNull String nomeSerie);

    void addPonto(@NotNull Ponto ponto, @NotNull String nomeSerie);

    void addPonto(@NotNull List<? extends Ponto> pontoList, @NotNull XYChart.Series series);

    void addPonto(@NotNull Ponto ponto, @NotNull XYChart.Series series);

    void limparSeries();

    void limparSerie(@NotNull String nomeSerie);

    void limparSerie(@NotNull XYChart.Series series);
}
