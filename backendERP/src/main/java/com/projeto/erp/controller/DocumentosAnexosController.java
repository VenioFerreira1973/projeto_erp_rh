package com.projeto.erp.controller;

import com.projeto.erp.service.DocumentosAnexosService;
import com.projeto.erp.service.MinioService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documentos")
public class DocumentosAnexosController {

    private final MinioService minioService;
    private final DocumentosAnexosService documentosAnexosService;

    public DocumentosAnexosController(MinioService minioService, DocumentosAnexosService documentosAnexosService) {
        this.minioService = minioService;
        this.documentosAnexosService = documentosAnexosService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        String objectName = file.getOriginalFilename();
        minioService.uploadFile(file, objectName);
        return ResponseEntity.ok("Arquivo enviado: " + objectName);
    }

    @GetMapping("/download/{name}")
    public ResponseEntity<?> download(@PathVariable("name") String name) {
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + name + "\"")
                .body(minioService.downloadFile(name));
    }

    @PreAuthorize("hasAuthority('ONBOARDING_WRITE')")
    @PostMapping(
            value = "/anexos/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<List<String>> uploadDocumentos(
            @RequestParam("arquivos") List<MultipartFile> arquivos) {

        List<String> urls = documentosAnexosService.uploadArquivos(arquivos);

        return ResponseEntity.ok(urls);
    }

}
