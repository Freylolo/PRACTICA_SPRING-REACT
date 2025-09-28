package ec.sasf.ms_comp_prueba_freya_lopez.web.Config;

import ec.sasf.ms_comp_prueba_freya_lopez.service.ExternalUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ExternalUserService externalUserService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Iniciando carga de usuarios externos...");
        externalUserService.cargarUsuariosExternos();
        System.out.println("Carga de usuarios externos finalizada.");
    }
}
