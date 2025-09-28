package ec.sasf.ms_comp_prueba_freya_lopez.service;

import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.ErrorLogEntity;
import ec.sasf.ms_comp_prueba_freya_lopez.persistence.repository.ErrorLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ErrorLogService {
    private final ErrorLogRepository errorLogRepository;

    public void registrarError(String message, String stackTrace, Long userId) {
        ErrorLogEntity log = new ErrorLogEntity();
        log.setMessage(message);
        log.setStackTrace(stackTrace);
        errorLogRepository.save(log);
    }
}
