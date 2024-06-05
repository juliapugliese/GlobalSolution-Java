package org.example.resources;

import jakarta.ws.rs.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class DenunciasResource {

    @Path("denuncia")

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        // Aqui você pode adicionar lógica para salvar o arquivo no sistema de arquivos
        // Por exemplo, salvando no diretório raiz do projeto
        String filePath = System.getProperty("user.dir") + "/uploads/" + multipartFile.getOriginalFilename();

        try {
            multipartFile.transferTo(new File(filePath));
            return ResponseEntity.ok("Arquivo " + multipartFile.getOriginalFilename() + " foi carregado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao carregar o arquivo.");
        }
    }
}
