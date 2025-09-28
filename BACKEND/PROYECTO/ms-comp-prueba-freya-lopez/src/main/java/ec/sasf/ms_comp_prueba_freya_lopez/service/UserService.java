package ec.sasf.ms_comp_prueba_freya_lopez.service;

import ec.sasf.ms_comp_prueba_freya_lopez.exception.UserNotFoundException;
import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.UserEntity;
import ec.sasf.ms_comp_prueba_freya_lopez.persistence.repository.UserRepository;
import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.UserRequestDTO;
import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.UserResponseDTO;
import ec.sasf.ms_comp_prueba_freya_lopez.web.Mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserResponseDTO crearUsuario(UserRequestDTO dto) {
        UserEntity user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("USER");
        user.setAccountNonLocked(true); // Usuario desbloqueado por defecto
        UserEntity saved = userRepository.save(user);
        return userMapper.toResponseDTO(saved);
    }

    public Page<UserResponseDTO> listarUsuariosPaginados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> usersPage = userRepository.findAll(pageable);
        return usersPage.map(userMapper::toResponseDTO); // convierte UserEntity a DTO
    }

    public UserResponseDTO buscarPorId(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO actualizarUsuario(Long id, UserRequestDTO dto) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        user.setName(dto.getName());
        user.setAvatar(dto.getAvatar());
        user.setEstadoCivil(dto.getEstadoCivil());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        UserEntity updated = userRepository.save(user);
        return userMapper.toResponseDTO(updated);
    }

    public void eliminarUsuario(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuario no encontrado");
        }
        userRepository.deleteById(id);
    }

    public UserEntity buscarPorEmailEntity(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
    }

    public void guardarUsuario(UserEntity user) {
        userRepository.save(user);
    }

    public UserEntity buscarPorIdEntity(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
    }
    public void increaseFailedAttempts(UserEntity user) {
        int newAttempts = user.getFailedAttempts() + 1;
        user.setFailedAttempts(newAttempts);
        if (newAttempts >= 5) {
            user.setAccountNonLocked(false); // bloquea el usuario
        }
        userRepository.save(user);
    }

    public void resetFailedAttempts(UserEntity user) {
        user.setFailedAttempts(0);
        userRepository.save(user);
    }

    // Bloquear usuario
    public void bloquearUsuario(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        user.setAccountNonLocked(false);
        userRepository.save(user);
    }

    // Desbloquear usuario
    public void desbloquearUsuario(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        user.setAccountNonLocked(true);
        user.setFailedAttempts(0); // Resetear intentos fallidos
        userRepository.save(user);
    }


}
