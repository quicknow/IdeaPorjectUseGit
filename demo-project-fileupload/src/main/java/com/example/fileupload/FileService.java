package com.example.fileupload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    // 保存文件
    public String saveFile(MultipartFile file) throws IOException {
        // 创建目录，如果不存在的话
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        // 获取文件名
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        // 保存文件
        file.transferTo(filePath);

        return fileName;
    }

    // 加载单个文件
    public Path loadFile(String fileName) {
        return Paths.get(uploadDir).resolve(fileName).normalize();
    }

    // 获取所有文件的列表
    public List<String> loadAllFiles() {
        try {
            // 获取目录中的所有文件名
            return Files.list(Paths.get(uploadDir))
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();  // 返回空的ArrayList以兼容Java 8及以下版本  // 如果读取目录失败，返回空列表
        }
    }
}
