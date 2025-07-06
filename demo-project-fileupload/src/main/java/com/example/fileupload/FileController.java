package com.example.fileupload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    // 显示上传页面
    @GetMapping("/")
    public String index(Model model) {
        return "index";  // 只显示上传表单，没有文件列表
    }

    // 处理文件上传
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        try {
            // 保存文件并返回文件名
            String fileName = fileService.saveFile(file);
            model.addAttribute("message", "文件上传成功: " + fileName);
        } catch (IOException e) {
            model.addAttribute("message", "文件上传失败: " + e.getMessage());
        }
        return "index";  // 返回上传页面，显示上传成功的消息
    }

    // 显示文件下载页面
    @GetMapping("/files")
    public String showFileList(Model model) {
        List<String> fileNames = fileService.loadAllFiles();
        model.addAttribute("fileNames", fileNames);
        return "files";  // 返回显示文件列表的页面
    }

    // 处理文件下载
    @GetMapping("/download/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {
        Path file = fileService.loadFile(filename);
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists()) {
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
