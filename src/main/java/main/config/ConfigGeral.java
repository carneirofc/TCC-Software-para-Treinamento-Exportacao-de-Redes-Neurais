package main.config;


import ann.controller.RnaController;
import com.google.gson.Gson;
import data.ConjuntoDados;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import main.gui.ValoresDisplay;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import main.Ctrl;
import main.utils.ExceptionPlanejada;
import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * Classe que organiza as paradas da configuração ...
 */
public class ConfigGeral {

    private static ConfigGeralDTO configGeralAtual = null;

    /**
     * Carrega as configurações contidas no json e define como as configurações atuais.
     */
    public static void setConfigGeralAtual(@NotNull Reader reader) {
        Gson gson = new Gson();
        ConfigGeral.configGeralAtual = gson.fromJson(reader, ConfigGeralDTO.class);
        setConfigGeralAtual();
    }

    public static void setConfigGeralAtual(@NotNull ConfigGeralDTO configGeralAtual) {
        ConfigGeral.configGeralAtual = configGeralAtual;
        setConfigGeralAtual();
    }

    public static void setConfigGeralAtual() {
        atualizaInterface();
        if (configGeralAtual.getRnaAtual() != null) {
            Ctrl.setRnaTreinada(true);

            ConjuntoDados.setMaxEntrada(configGeralAtual.getInColumnMax());
            ConjuntoDados.setMinEntrada(configGeralAtual.getInColumnMin());

            ConjuntoDados.setMaxSaida(configGeralAtual.getOutColumnMax());
            ConjuntoDados.setMinSaida(configGeralAtual.getOutColumnMin());

            ConjuntoDados.setNormMinMax(configGeralAtual.getNormMin(), configGeralAtual.getNormMax());
        }
        RnaController.setRna(configGeralAtual.getRnaAtual());

        System.out.println(configGeralAtual.toString());
        System.out.println();
        Ctrl.setPropertiesConfigCarregadas(true);
    }

    public static ConfigGeralDTO getConfigGeralAtual() {
        return configGeralAtual;
    }

    public static void atualizaInterface() {
        if (configGeralAtual != null) {
            try {
                Platform.runLater(() -> {
                    ((SimpleDoubleProperty) ValoresDisplay.obsFaixaPesoInicial).set(configGeralAtual.getFaixaPesos());
                    ((SimpleLongProperty) ValoresDisplay.obsEpocaMaxima).set(configGeralAtual.getEpocaMaxima());
                    ((SimpleDoubleProperty) ValoresDisplay.obsTaxaAprendizado).set(configGeralAtual.getTaxaAprendizado());
                    ((SimpleDoubleProperty) ValoresDisplay.obsErroDesejado).set(configGeralAtual.getErroAlvo());
                    ((SimpleDoubleProperty) ValoresDisplay.obsMomentum).set((configGeralAtual.getMomentum()));
                    ((SimpleDoubleProperty) ValoresDisplay.obsTermoLinear).set(configGeralAtual.getTermoLinear());
                    ValoresDisplay.atualizaFuncaoAtivacao(configGeralAtual.getFuncaoAtivacaoInterno(), true);
                    ValoresDisplay.atualizaFuncaoAtivacao(configGeralAtual.getFuncaoAtivacaoSaida(), false);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Erro ao atualizar a interface com as configurações gerais da RNA. configGeralAtual=null");
        }
    }

    /**
     * Carrega um arquivo contendo as configurações dosLogTempo parÂmetros da RNA
     */
    public static void loadConfigGeral(File file) throws Exception {
        if (file == null)
            throw new ExceptionPlanejada("Não existe arquivo carregado.");
        if (!file.isFile())
            throw new ExceptionPlanejada("Arquivo não corresponde ao esperado.");
        if (Ctrl.isRnaEmExecucao()) {
            throw new ExceptionPlanejada("A RNA já está em execução.");
        }
        try (InputStream inputStream = new FileInputStream(file); Reader reader = new InputStreamReader(inputStream)) {
            ConfigGeral.setConfigGeralAtual(reader);
            // TODO: Mostrar o dirtório de onde esse arquivo saiu ...
            Platform.runLater(() -> {
                try {
                    ((SimpleStringProperty) ValoresDisplay.obsPropertiesConfigCaminho).set(file.getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

}

