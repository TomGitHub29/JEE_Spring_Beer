package ch.hearc.jee2024.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//>docker run --name tom_beers-mysql -e MYSQL_ROOT_PASSWORD=beers -p 3306:3306 -d mysql:8.2.0-oracle

@SpringBootApplication
public class ProjectApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(ProjectApplication.class, args);

    }

}
