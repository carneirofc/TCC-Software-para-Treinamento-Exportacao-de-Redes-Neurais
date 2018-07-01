package data.txt.write;

import com.google.gson.Gson;
import data.ConjuntoDados;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.util.Duration;
import main.Ctrl;
import main.Recursos;
import main.config.ConfigGeral;
import main.gui.Janela;
import main.gui.controller.Teste;
import main.gui.grafico.Ponto;
import main.utils.ExceptionPlanejada;
import main.utils.Utilidade;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExportaTexto extends TxtWriter {
    private static class ConfigRunnable implements Runnable {

        private static final AtomicBoolean IS_SALVANDO = new AtomicBoolean(false);
        private File file;
        private ExportaTexto exportaTexto;

        ConfigRunnable(ExportaTexto exportaTexto, File file) {
            this.exportaTexto = exportaTexto;
            this.file = file;
        }

        @Override
        public void run() {

            if (IS_SALVANDO.compareAndSet(false, true)) {
                try {
                    ConfigGeral.serializeConfigGeralDTO(this.file.getAbsolutePath());
                    /*
                    // Forma antiga ... Salvando JSON ! Estava dando problemas com arquivos largos...
                        this.exportaTexto.criaWriter(file, Recursos.EXTENSION_FILTER_JSON);
                        Gson gson = new Gson();
                        this.exportaTexto.getPrintWriter().print(gson.toJson(ConfigGeral.getConfigGeralAtual()));
                        this.exportaTexto.finaliza();
                    */
                    Platform.runLater(() -> Notifications.create()
                            .darkStyle()
                            .position(Pos.TOP_RIGHT)
                            .hideAfter(Duration.seconds(4))
                            .title("Operação concluída !")
                            .text("\"A exportação das configurações fui concluída.")
                            .showInformation());

                } catch (ExceptionPlanejada e) {
                    Utilidade.avisoExceptionPlanejada(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> Janela.exceptionDialog(e));
                } finally {
                    IS_SALVANDO.set(false);
                }
            } else {
                System.out.println("Operação já está em execuição ...");
            }
        }
    }

    private static class DadosTesteRunnable implements Runnable {

        private static final AtomicBoolean IS_SALVANDO = new AtomicBoolean(false);
        private final ExportaTexto exportaTexto;
        private final File file;
        private double min, max;

        DadosTesteRunnable(final ExportaTexto exportaTexto, final File file, double min, double max) {
            this.exportaTexto = exportaTexto;
            this.file = file;
            this.min = min;
            this.max = max;
        }

        @Override
        public void run() {
            if (IS_SALVANDO.compareAndSet(false, true)) {
                try {
                    if (Ctrl.isDadosTreinoCarregados()) {

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
                            for (int i = 0; i < tam; i++) {
                                d[i] = 0.0;
                            }
                            double dI[] = ConjuntoDados.dadosTeste.getDadosEntrada()[(int) ponto.getX()];
                            double dO[] = ConjuntoDados.dadosTeste.getDadosSaida()[(int) ponto.getX()];

                            for (int i = 0; i < ConjuntoDados.getColunasEntrada().size(); i++) {
                                System.out.println(ConjuntoDados.getColunasEntrada().get(i) + "=" + dI[i]);
                                d[ConjuntoDados.getColunasEntrada().get(i)] = dI[i];
                            }
                            for (int i = 0; i < ConjuntoDados.getColunasSaida().size(); i++) {
                                System.out.println(ConjuntoDados.getColunasSaida().get(i) + "=" + dO[i]);
                                d[ConjuntoDados.getColunasSaida().get(i)] = dO[i];
                            }
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

                        this.exportaTexto.criaWriter(file, Recursos.EXTENSION_FILTER_TXT);
                        this.exportaTexto.getPrintWriter().printf(stringBuilder.toString());
                        this.exportaTexto.finaliza();
                    } else {
                        throw new ExceptionPlanejada("Dados de Treino não foram carregados.");
                    }
                } catch (ExceptionPlanejada e) {
                    Utilidade.avisoExceptionPlanejada(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> Janela.exceptionDialog(e));
                } finally {
                    IS_SALVANDO.set(false);
                }
            }
        }
    }

    public void salvarConfigDadosJson(File file) {
        new Thread(new ConfigRunnable(this, file)).start();
    }

    public void geraDadosTeste(File file, double min, double max) {
        new Thread(new DadosTesteRunnable(this, file, min, max)).start();
    }
}
