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

	private int[][] first_matrix;
		
	private int[][] second_matrix;

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
	public String handleFileUpload(@RequestParam("file") MultipartFile file,@RequestParam("file2") MultipartFile file2, RedirectAttributes redirectAttributes) throws IOException{
		// if (file.getBytes().length!=0 && file2.getBytes().length!=0){
		// 	redirectAttributes.addFlashAttribute("message", "Both files have been uploaded and are not empty");
		// 	return "redirect:/";
		// }
		if (file.getBytes().length==0 || file2.getBytes().length==0) {
			redirectAttributes.addFlashAttribute("message", "One or both files that you have provided are empty! Please check you have uploaded the correct files");
			return "redirect:/";

		}

		String m1 = new String(file.getBytes());
		String m2 = new String(file2.getBytes());

		if (m1.length()!= 0 && m2.length()!=0){
			System.out.println(m1);
			System.out.println(m2);

		}

		String[] row1 = m1.split("\n");
		String[] row2 = m2.split("\n");
		System.out.println(row1.length);
		System.out.println(row2.length);

		if (!CheckIfPowerOf2(row1.length) || !CheckIfPowerOf2(row2.length)){
			redirectAttributes.addFlashAttribute("message", "The matrices provided are not in the power of 2");
			return "redirect:/";

		}

		if (row1.length != row2.length){
			redirectAttributes.addFlashAttribute("message", "The matrices should be of same size");
			
			return "redirect:/";
		}
		for (String rr : row1){
			String [] cc = rr.split(" ");
			if(cc.length != row1.length){
				redirectAttributes.addFlashAttribute("message", "The matrices should be square");
				return "redirect:/";
			}
		}
		for (String rr : row2){
			String [] cc = rr.split(" ");
			if(cc.length != row2.length){
				redirectAttributes.addFlashAttribute("message", "The matrices should be square");
				return "redirect:/";
			}
		}

		int dummy_columns1 = row1[0].split(" ").length;
		int dummy_columns2 = row2[0].split(" ").length;
		if (dummy_columns1 != dummy_columns2){
			redirectAttributes.addFlashAttribute("message", "The matrices should be of same size");
			return "redirect:/";
		}

		first_matrix = new int[row1.length][dummy_columns1];
		second_matrix = new int[row2.length][dummy_columns2];
		int i = 0;
		int r = 0;
		int c = 0;

		while (r < row1.length){
			String [] columns1 = row1[r].replaceAll("[^\s0-9]","").split(" ");
			String [] columns2 = row2[r].replaceAll("[^\s0-9]","").split(" ");;
			
			while (c < columns2.length){
				first_matrix [r][c] = Integer.parseInt(columns1[i]);
				second_matrix [r][c] = Integer.parseInt(columns2[i]);
				i++;
				c++;
			}
			c=0;
			r++;
		}
		String m = "Great success"+"<br>"+"Matrix 1"+"<br>"+m1.replaceAll("\n","<br>")+"<br>"+"Matrix 2"+"<br>"+m2.replaceAll("\n","<br>");
		redirectAttributes.addFlashAttribute("message", m);
		return ("redirect:/");	
	}
	// code taken from https://www.geeksforgeeks.org/program-to-find-whether-a-given-number-is-power-of-2/
	public static boolean CheckIfPowerOf2 (int n){
		if (n == 1)
			return true;
		else if (n % 2 != 0 ||
				n ==0)
			return false;
		
		// recursive function call
		return CheckIfPowerOf2(n / 2);
	}
	
}

	