package org.example.services;


import org.example.entities.ServicoModel.Denuncia;
import org.example.entities.UsuarioModel.Denunciante;
import org.example.repositories.DenunciantesRepository;
import org.example.repositories.DenunciasRepository;

public class DenunciaService {

    private final DenunciasRepository denunciasRepository;

    public DenunciaService(){
        denunciasRepository = new DenunciasRepository();
    }
    public void update(int id, Denuncia denuncia){
        var validation = denuncia.validate();

        if(validation.containsKey(false))
            throw new IllegalArgumentException(validation.get(false).toString());
        else
            denunciasRepository.update(id, denuncia);
    }

    public void delete(int id){
        denunciasRepository.delete(id);
    }

}
