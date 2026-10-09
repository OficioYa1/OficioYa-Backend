package com.oficioya.service.impl;

import com.oficioya.exception.EstadoInvalidoException;
import com.oficioya.exception.UsuarioNoEncontradoException;
import com.oficioya.persistence.entity.ReferidoEntity;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.ReferidoRepository;
import com.oficioya.repository.UsuarioRepository;
import com.oficioya.service.IReferidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReferidoServiceImpl implements IReferidoService {

    private final ReferidoRepository referidoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public void registrarReferido(Long referenteId, String correoReferido) {
        log.info("Usuario {} está refiriendo a {}", referenteId, correoReferido);
        
        UsuarioEntity referente = usuarioRepository.findById(referenteId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Referente no encontrado"));

        if (usuarioRepository.findByCorreo(correoReferido).isPresent()) {
            throw new EstadoInvalidoException("El correo referido ya pertenece a una cuenta existente");
        }

        if (referidoRepository.findByCorreoReferido(correoReferido).isPresent()) {
             throw new EstadoInvalidoException("Este correo ya ha sido invitado previamente");
        }

        ReferidoEntity referido = ReferidoEntity.builder()
                .referente(referente)
                .correoReferido(correoReferido)
                .cuentaCreada(false)
                .fechaInvitacion(LocalDateTime.now())
                .build();
                
        referidoRepository.save(referido);
        
        // Aquí se enviaría un correo de invitación real
        log.info("Simulando envío de correo de invitación a {}", correoReferido);
    }

    @Override
    @Transactional
    public void procesarRegistroReferido(String correoNuevoUsuario) {
        referidoRepository.findByCorreoReferido(correoNuevoUsuario).ifPresent(referido -> {
            referido.setCuentaCreada(true);
            referidoRepository.save(referido);
            
            UsuarioEntity referente = referido.getReferente();
            long totalReferidos = referidoRepository.countByReferenteIdAndCuentaCreadaTrue(referente.getId());
            
            if (totalReferidos >= 5 && !referente.isEmbajador()) {
                referente.setEmbajador(true);
                usuarioRepository.save(referente);
                log.info("Usuario {} ahora es Embajador OficioYa (Superó 5 referidos)", referente.getId());
            }
        });
    }
}
