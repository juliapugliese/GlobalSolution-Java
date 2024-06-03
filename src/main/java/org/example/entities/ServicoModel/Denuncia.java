package org.example.entities.ServicoModel;

import org.example.entities._BaseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class Denuncia extends _BaseEntity {
    private String descricao;
    private LocalDate data;
    private String localizacao;
    private String impactoPercebido;
    private String tipoIncidente;
    private String origemResiduo;
    private String frequenciaProblema;
    private String comentarios;
    private Feedback feedback;

    public Denuncia() {
    }

    public Denuncia(String descricao, LocalDate data, String localizacao, String impactoPercebido, String tipoIncidente, String origemResiduo, String frequenciaProblema, String comentarios, Feedback feedback) {
        this.descricao = descricao;
        this.data = data;
        this.localizacao = localizacao;
        this.impactoPercebido = impactoPercebido;
        this.tipoIncidente = tipoIncidente;
        this.origemResiduo = origemResiduo;
        this.frequenciaProblema = frequenciaProblema;
        this.comentarios = comentarios;
        this.feedback = feedback;
    }

    public Denuncia(int id, String descricao, LocalDate data, String localizacao, String impactoPercebido, String tipoIncidente, String origemResiduo, String frequenciaProblema, String comentarios, Feedback feedback) {
        super(id);
        this.descricao = descricao;
        this.data = data;
        this.localizacao = localizacao;
        this.impactoPercebido = impactoPercebido;
        this.tipoIncidente = tipoIncidente;
        this.origemResiduo = origemResiduo;
        this.frequenciaProblema = frequenciaProblema;
        this.comentarios = comentarios;
        this.feedback = feedback;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getImpactoPercebido() {
        return impactoPercebido;
    }

    public void setImpactoPercebido(String impactoPercebido) {
        this.impactoPercebido = impactoPercebido;
    }

    public String getTipoIncidente() {
        return tipoIncidente;
    }

    public void setTipoIncidente(String tipoIncidente) {
        this.tipoIncidente = tipoIncidente;
    }

    public String getOrigemResiduo() {
        return origemResiduo;
    }

    public void setOrigemResiduo(String origemResiduo) {
        this.origemResiduo = origemResiduo;
    }

    public String getFrequenciaProblema() {
        return frequenciaProblema;
    }

    public void setFrequenciaProblema(String frequenciaProblema) {
        this.frequenciaProblema = frequenciaProblema;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }



    public Map<Boolean, ArrayList<String>> validate() {
        var errors = new ArrayList<String>();

        if (descricao == null || descricao.isBlank())
            errors.add("A denúncia deve apresentar uma descrição");

        if (data == null)
            errors.add("A denúncia tem que ter uma data");

        if (impactoPercebido == null || impactoPercebido.isBlank())
            errors.add("O campo impacto percebido deve ser preenchido");

        if (tipoIncidente == null || tipoIncidente.isBlank())
            errors.add("A denúncia deve apresentar um tipo de incidente, ex: derramamento de petróleo");

        if (localizacao == null || localizacao.isBlank())
            errors.add("A localização do incidente deve ser inserida");




        return !errors.isEmpty() ?
                Map.of(false, errors) :
                Map.of(true, errors);
    }
}
