package br.edu.ifrn.scatalapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	
    public static void main(String[] args) {
//        int porta = 9090;
//        if (args.length > 0) {
//			porta = Integer.parseInt(args[0]);
//		}
    	
    	SpringApplication.run(Application.class, args);
    }
}
