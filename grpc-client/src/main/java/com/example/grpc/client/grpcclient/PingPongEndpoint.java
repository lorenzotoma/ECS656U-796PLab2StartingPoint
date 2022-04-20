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
	private String m1;
	private String m2;

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
	@ResponseBody
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

		m1 = new String(file.getBytes());
		m2 = new String(file2.getBytes());

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

		int size = row1.length;
		System.out.println("lengths");
		System.out.println(row1.length);
		System.out.println(row2.length);
		first_matrix = new int[size][size];
		second_matrix = new int[size][size];
		int i = 0;
		int r = 0;
		int c = 0;

		while (r < row1.length){
			String [] columns1 = row1[r].trim().split(" ");
			String [] columns2 = row2[r].trim().split(" ");
			System.out.println("Printing columns1");
			printArray(columns1);
			System.out.println("Printing 2");
			printArray(columns2);
			
			while (c < columns2.length){
				try{
					first_matrix [r][c] = Integer.parseInt(columns1[i]);
					second_matrix [r][c] = Integer.parseInt(columns2[i]);
				}
				catch(Exception e){
					System.out.println("Problem is when r="+r+" c="+c+" i="+i);
				}
				i++;
				c++;
			}
			c=0;
			i=0;
			r++;
		}
		return ("redirect:/showUpload");	
	}

	@GetMapping("/showUpload")
	@ResponseBody
	public String show() {
		String message = "Uploaded<br><br>"+m1.replaceAll("\n","<br>")+"<br><br>"+m2.replaceAll("\n","<br>");
		return message;
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

	private void printArray(String[] a){
		for(String e : a){
			System.out.println(e);
		}
	}
	
}

	