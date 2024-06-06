package org.example.entities.ServicoModel;

import org.example.entities._BaseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Denuncia extends _BaseEntity {
    private String descricao;
    private LocalDate data;
    private String localizacao;
    private String tipoIncidente;
    private String origemResiduo;
    private String recorrenciaProblema;
    private String comentariosAdicionais;

    private Feedback feedback;

    public Denuncia() {
    }

    public Denuncia(String descricao, LocalDate data, String localizacao, String tipoIncidente, String origemResiduo, String recorrenciaProblema, String comentariosAdicionais, Feedback feedback) {
        this.descricao = descricao;
        this.data = data;
        this.localizacao = localizacao;
        this.tipoIncidente = tipoIncidente;
        this.origemResiduo = origemResiduo;
        this.recorrenciaProblema = recorrenciaProblema;
        this.comentariosAdicionais = comentariosAdicionais;
        this.feedback = feedback;
    }

    public Denuncia(int id, String descricao, LocalDate data, String localizacao, String tipoIncidente, String origemResiduo, String recorrenciaProblema, String comentariosAdicionais, Feedback feedback) {
        super(id);
        this.descricao = descricao;
        this.data = data;
        this.localizacao = localizacao;
        this.tipoIncidente = tipoIncidente;
        this.origemResiduo = origemResiduo;
        this.recorrenciaProblema = recorrenciaProblema;
        this.comentariosAdicionais = comentariosAdicionais;
        this.feedback = feedback;
    }



    public Denuncia(String descricao, LocalDate data, String localizacao, String tipoIncidente, String origemResiduo, String recorrenciaProblema, String comentariosAdicionais) {
        this.descricao = descricao;
        this.data = data;
        this.localizacao = localizacao;
        this.tipoIncidente = tipoIncidente;
        this.origemResiduo = origemResiduo;
        this.recorrenciaProblema = recorrenciaProblema;
        this.comentariosAdicionais = comentariosAdicionais;
    }

    public Denuncia(int id, String descricao, LocalDate data, String localizacao, String tipoIncidente, String origemResiduo, String recorrenciaProblema, String comentariosAdicionais) {
        super(id);
        this.descricao = descricao;
        this.data = data;
        this.localizacao = localizacao;
        this.tipoIncidente = tipoIncidente;
        this.origemResiduo = origemResiduo;
        this.recorrenciaProblema = recorrenciaProblema;
        this.comentariosAdicionais = comentariosAdicionais;
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

    public String getRecorrenciaProblema() {
        return recorrenciaProblema;
    }

    public void setRecorrenciaProblema(String recorrenciaProblema) {
        this.recorrenciaProblema = recorrenciaProblema;
    }

    public String getComentariosAdicionais() {
        return comentariosAdicionais;
    }

    public void setComentariosAdicionais(String comentariosAdicionais) {
        this.comentariosAdicionais = comentariosAdicionais;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Denuncia.class.getSimpleName() + "[", "]")
                .add("descricao='" + descricao + "'")
                .add("data=" + data)
                .add("localizacao='" + localizacao + "'")
                .add("tipoIncidente='" + tipoIncidente + "'")
                .add("origemResiduo='" + origemResiduo + "'")
                .add("recorrenciaProblema='" + recorrenciaProblema + "'")
                .add("comentariosAdicionais='" + comentariosAdicionais + "'")
                .add("feedback=" + feedback)
                .add("id=" + getId())
                .toString();
    }

    public Map<Boolean, ArrayList<String>> validate() {
        var errors = new ArrayList<String>();

        if (descricao == null || descricao.isBlank())
            errors.add("A denúncia deve apresentar uma descrição");

        if (data == null)
            errors.add("A denúncia tem que ter uma data");

        if (tipoIncidente == null || tipoIncidente.isBlank())
            errors.add("A denúncia deve apresentar um tipo de incidente, ex: derramamento de petróleo");

        if (localizacao == null || localizacao.isBlank())
            errors.add("A localização do incidente deve ser inserida");




        return !errors.isEmpty() ?
                Map.of(false, errors) :
                Map.of(true, errors);
    }
}
