package org.example.services;


import org.example.entities.UsuarioModel.Denunciante;
import org.example.repositories.DenunciantesRepository;

public class DenuncianteService {

    private final DenunciantesRepository denunciantesRepository;

    public DenuncianteService(){
        denunciantesRepository = new DenunciantesRepository();
    }

    public void create(Denunciante denunciante){
        var validation = denunciante.validate();

        if(validation.containsKey(false))
            throw new IllegalArgumentException(validation.get(false).toString());
        else
            denunciantesRepository.create(denunciante);
    }

    public void update(int id, Denunciante denunciante){
        var validation = denunciante.validate();

        if(validation.containsKey(false))
            throw new IllegalArgumentException(validation.get(false).toString());
        else
            denunciantesRepository.update(id, denunciante);
    }

    public void delete(int id){
        denunciantesRepository.delete(id);
    }

}
