package org.example.entities.UsuarioModel;

import org.example.entities.ServicoModel.Denuncia;
import org.example.entities._BaseEntity;

import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;

public class Denunciante extends _BaseEntity{
    private String nomeCompleto;
    private String email;
    private String telefone;
    private Denuncia denuncia;


    public Denunciante() {}


    public Denunciante(String nomeCompleto, String email, String telefone) {
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.telefone = telefone;
    }

    public Denunciante(int id, String nomeCompleto, String email, String telefone) {
        super(id);
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.telefone = telefone;
    }


    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", Denunciante.class.getSimpleName() + "[", "]")
                .add("nomeCompleto='" + nomeCompleto + "'")
                .add("email='" + email + "'")
                .add("telefone='" + telefone + "'")
                .add("denuncia=" + denuncia)
                .add("id=" + getId())
                .toString();
    }

    public Map<Boolean, ArrayList<String>> validate() {
        var errors = new ArrayList<String>();
        if (telefone == null || telefone.isBlank() || !telefone.replaceAll("[.-]", "").matches("\\d{11}"))
            errors.add("O telefone deve ser composto por 11 números, incluindo o ddd");

        if (nomeCompleto == null || nomeCompleto.isBlank())
            errors.add("Nome não pode ser vazio");

        if (email == null || email.isBlank())
            errors.add("Email não pode ser vazio");

        return !errors.isEmpty() ?
                Map.of(false, errors) :
                Map.of(true, errors);
    }
}
