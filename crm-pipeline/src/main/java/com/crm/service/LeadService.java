package com.crm.service;

import com.crm.model.Lead;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.collections.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class LeadService {

    private static LeadService instance;

    public static final String[] ESTAGIOS = {
        "New Lead", "Contacted", "Proposal Sent", "Closed", "Lost"
    };

    public static final String[] CORES = {
        "#7C6AF7", "#4DA8FF", "#FFB347", "#52D9A0", "#FF6B6B"
    };

    private final Map<String, ObservableList<Lead>> pipeline = new LinkedHashMap<>();
    private static final String SAVE_FILE = "crm_data.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private LeadService() {
        for (String e : ESTAGIOS) {
            pipeline.put(e, FXCollections.observableArrayList());
        }
        carregarDados();
    }

    public static LeadService getInstance() {
        if (instance == null) instance = new LeadService();
        return instance;
    }

    public ObservableList<Lead> getLeads(String estagio) {
        return pipeline.getOrDefault(estagio, FXCollections.observableArrayList());
    }

    public Map<String, ObservableList<Lead>> getPipeline() {
        return pipeline;
    }

    public void adicionarLead(Lead lead) {
        pipeline.get(lead.getEstagio()).add(lead);
        salvarDados();
    }

    public void moverLead(Lead lead, String novoEstagio) {
        pipeline.get(lead.getEstagio()).remove(lead);
        lead.setEstagio(novoEstagio);
        pipeline.get(novoEstagio).add(lead);
        salvarDados();
    }

    public void removerLead(Lead lead) {
        pipeline.get(lead.getEstagio()).remove(lead);
        salvarDados();
    }

    public void atualizarLead(Lead lead) {
        salvarDados();
    }

    public int getTotalLeads() {
        return pipeline.values().stream().mapToInt(List::size).sum();
    }

    public double getValorTotal() {
        return pipeline.values().stream()
            .flatMap(Collection::stream)
            .mapToDouble(Lead::getValor).sum();
    }

    public double getValorFechado() {
        return pipeline.get("Closed").stream()
            .mapToDouble(Lead::getValor).sum();
    }

    public double getTaxaConversao() {
        int total = getTotalLeads();
        if (total == 0) return 0;
        return (pipeline.get("Closed").size() * 100.0) / total;
    }

    private void salvarDados() {
        try {
            Map<String, List<Lead>> data = new LinkedHashMap<>();
            for (String e : ESTAGIOS) {
                data.put(e, new ArrayList<>(pipeline.get(e)));
            }
            try (Writer w = new FileWriter(SAVE_FILE)) {
                gson.toJson(data, w);
            }
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    private void carregarDados() {
        File f = new File(SAVE_FILE);
        if (!f.exists()) {
            carregarDadosExemplo();
            return;
        }
        try (Reader r = new FileReader(f)) {
            Type type = new TypeToken<Map<String, List<Lead>>>() {}.getType();
            Map<String, List<Lead>> data = gson.fromJson(r, type);
            if (data != null) {
                for (String e : ESTAGIOS) {
                    List<Lead> lista = data.get(e);
                    if (lista != null) pipeline.get(e).addAll(lista);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
            carregarDadosExemplo();
        }
    }

    private void carregarDadosExemplo() {
        pipeline.get("New Lead").addAll(Arrays.asList(
            new Lead("Maria Silva", "maria@techcorp.com", "+1 555-0101",
                     "TechCorp Inc.", 15000, "HIGH", "New Lead", "Interested in enterprise plan"),
            new Lead("John Santos", "john@startup.io", "+1 555-0102",
                     "Startup IO", 8000, "MEDIUM", "New Lead", "")
        ));
        pipeline.get("Contacted").addAll(Arrays.asList(
            new Lead("Anna Costa", "anna@logistics.com", "+1 555-0103",
                     "Logistics Brazil", 25000, "HIGH", "Contacted", "Meeting scheduled for Friday"),
            new Lead("Peter Oliveira", "peter@retail.com", "+1 555-0104",
                     "Retail Plus", 12000, "LOW", "Contacted", "")
        ));
        pipeline.get("Proposal Sent").add(
            new Lead("Carlos Lima", "carlos@fintech.com", "+1 555-0105",
                     "FinTech Brazil", 50000, "HIGH", "Proposal Sent", "Awaiting board approval")
        );
        pipeline.get("Closed").add(
            new Lead("Fernanda Rocha", "fernanda@edu.com", "+1 555-0106",
                     "EduTech", 18000, "MEDIUM", "Closed", "Contract signed 03/2026")
        );
    }
}