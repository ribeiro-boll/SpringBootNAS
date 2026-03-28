package com.bolota.springnas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class SpringNasApplication {
    public static String getLocalIpFast() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();  // ex: 192.168.0.50
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }
    public static void main(String[] args) {
        String ip = getLocalIpFast();

        System.out.println("Mini NAS disponível em: http://" + ip + ":" + 9000);
        SpringApplication.run(SpringNasApplication.class, args);
    }

}
