package org.example.repositories;

import org.example.entities.ServicoModel.Denuncia;

import java.util.List;
import java.util.Optional;

public class DenunciasRepository extends Starter implements _BaseRepository<Denuncia>, _Logger<String>{
    @Override
    public void create(Denuncia obj) {

    }

    @Override
    public List<Denuncia> readAll(String orderBy, String direction, int limit, int offset) {
        return null;
    }

    @Override
    public Optional<Denuncia> read(int id) {
        return Optional.empty();
    }

    @Override
    public void update(int id, Denuncia obj) {

    }

    @Override
    public void delete(int id) {

    }
}
