package com.example.grpc.client.grpcclient;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ResponseBody;


import java.io.IOException;

@Controller
public class PingPongEndpoint {    

	GRPCClientService grpcClientService;    
	@Autowired
    	public PingPongEndpoint(GRPCClientService grpcClientService) {
        	this.grpcClientService = grpcClientService;
    	}    
	@GetMapping("/ping")
    	public String ping() {
        	return grpcClientService.ping();
    	}
    @GetMapping("/add")
		public String add() {
			return grpcClientService.add();
		}
	@GetMapping("/")
    	public String home() {
        	return "uploadForm.html";
    	}
	
	@PostMapping("/")
    public String fileUpload(@RequestParam("file") MultipartFile file,@RequestParam("file2") MultipartFile file2, RedirectAttributes redirectAttributes) throws IOException{
    	//Make sure a file has been uploaded
        redirectAttributes.addFlashAttribute("message","Upload successful!");
        return "redirect:/";
    }
	// @GetMapping("/files/{filename:.+}")
	// @ResponseBody
	// public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

	// 	Resource file = storageService.loadAsResource(filename);
	// 	return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
	// 			"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	// }

	// @ExceptionHandler(StorageFileNotFoundException.class)
	// public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
	// 	return ResponseEntity.notFound().build();
	// }
}

	