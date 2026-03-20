package br.com.gatekeeper.controle_acessos.repository;

import br.com.gatekeeper.controle_acessos.model.ResponsavelModulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponsavelModuloRepository extends JpaRepository<ResponsavelModulo, Integer> {
    
    // Descobre quem são (ou foram) os responsáveis por um módulo
    List<ResponsavelModulo> findByModuloId(Integer moduloId);
    
    // Descobre de quais módulos um utilizador específico é responsável
    List<ResponsavelModulo> findByUsuarioId(Integer usuarioId);
}