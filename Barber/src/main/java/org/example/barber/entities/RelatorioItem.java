package org.example.barber.entities;

public class RelatorioItem {
    private final String periodo;
    private final int atendimentos;
    private final double faturamento;
    private final double comissao;

    public RelatorioItem(String periodo, int atendimentos, double faturamento, double comissao) {
        this.periodo = periodo;
        this.atendimentos = atendimentos;
        this.faturamento = faturamento;
        this.comissao = comissao;
    }

    public String getPeriodo() { return periodo; }
    public int getAtendimentos() { return atendimentos; }
    public double getFaturamento() { return faturamento; }
    public double getComissao() { return comissao; }
}
