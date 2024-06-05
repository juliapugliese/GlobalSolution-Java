package org.example.entities.ServicoModel;

import org.example.entities._BaseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;

public class Feedback extends _BaseEntity {
    private String status;
    private String retorno;
    private LocalDate data;
    public Feedback() {
    }

    public Feedback(String status, String retorno, LocalDate data) {
        this.status = status;
        this.retorno = retorno;
        this.data = data;
    }

    public Feedback(int id, String status, String retorno, LocalDate data) {
        super(id);
        this.status = status;
        this.retorno = retorno;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRetorno() {
        return retorno;
    }

    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Feedback.class.getSimpleName() + "[", "]")
                .add("status='" + status + "'")
                .add("retorno='" + retorno + "'")
                .add("data=" + data)
                .add("id=" + getId())
                .toString();
    }

    public Map<Boolean, ArrayList<String>> validate() {
        var errors = new ArrayList<String>();
        if (status == null || status.isBlank())
            errors.add("Status não pode estar vazio");
        if (data == null)
            errors.add("O feedback tem que ter uma data");

        return !errors.isEmpty() ?
                Map.of(false, errors) :
                Map.of(true, errors);
    }

}

