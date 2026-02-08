package com.projeto.erp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentosAnexosService {

    private final MinioService minioService;

    public DocumentosAnexosService(MinioService minioService) {
        this.minioService = minioService;
    }

    public List<String> uploadArquivos(List<MultipartFile> arquivos) {
        return arquivos.stream()
                .map(file -> {
                    String objectName = gerarObjectName(file);
                    return minioService.uploadFile(file, objectName);
                })
                .toList();
    }

    private String gerarObjectName(MultipartFile file) {
        String original = file.getOriginalFilename();
        String extensao = original != null && original.contains(".")
                ? original.substring(original.lastIndexOf("."))
                : "";

        return "onboarding/"
                + UUID.randomUUID()
                + extensao;
    }
}
