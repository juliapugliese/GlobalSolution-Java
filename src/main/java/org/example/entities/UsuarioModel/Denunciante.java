package org.example.entities.UsuarioModel;

import org.example.entities.ServicoModel.Denuncia;
import org.example.entities._BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class Denunciante extends _BaseEntity{
    private String nome;
    private String email;
    private String telefone;
    private List<Denuncia> denuncias = new ArrayList<>();


    public Denunciante() {}

    public Denunciante(String nome, String email, String telefone, List<Denuncia> denuncias) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.denuncias = denuncias;
    }

    public Denunciante(int id, String nome, String email, String telefone, List<Denuncia> denuncias) {
        super(id);
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.denuncias = denuncias;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public List<Denuncia> getDenuncias() {
        return denuncias;
    }

    public void setDenuncias(List<Denuncia> denuncias) {
        this.denuncias = denuncias;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Denunciante.class.getSimpleName() + "[", "]")
                .add("nomeCompleto='" + nome + "'")
                .add("email='" + email + "'")
                .add("telefone='" + telefone + "'")
                .add("denuncias=" + denuncias)
                .add("id=" + getId())
                .toString();
    }

    public Map<Boolean, ArrayList<String>> validate() {
        var errors = new ArrayList<String>();
        if (telefone == null || telefone.isBlank() || !telefone.replaceAll("[.-]", "").matches("\\d{11}"))
            errors.add("O telefone deve ser composto por 11 números, incluindo o ddd");

        if (nome == null || nome.isBlank())
            errors.add("Nome não pode ser vazio");

        if (email == null || email.isBlank())
            errors.add("Email não pode ser vazio");

        return !errors.isEmpty() ?
                Map.of(false, errors) :
                Map.of(true, errors);
    }
}
