package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.request.FaqRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.FaqResponseDTO;
import br.com.gatekeeper.controle_acessos.mapper.FaqMapper;
import br.com.gatekeeper.controle_acessos.model.FAQ;
import br.com.gatekeeper.controle_acessos.model.Modulo;
import br.com.gatekeeper.controle_acessos.repository.FaqRepository;
import br.com.gatekeeper.controle_acessos.repository.ModuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FaqService {

    @Autowired private FaqRepository faqRepository;
    @Autowired private ModuloRepository moduloRepository;
    @Autowired private FaqMapper mapper; // Injetando o mapper que criamos

    @Transactional
    public FaqResponseDTO criarFaq(FaqRequestDTO dto, Integer moduloId) {
        // 1. Regra de Negócio: O FAQ precisa de um módulo válido
        Modulo modulo = moduloRepository.findById(moduloId)
                .orElseThrow(() -> new RuntimeException("Módulo não encontrado"));
        
        // 2. Uso do Mapper para converter o Request em Entidade
        FAQ faq = mapper.toEntity(dto);
        faq.setModulo(modulo);
        
        // 3. Salva no banco
        faq = faqRepository.save(faq);
        
        // 4. Retorna o ResponseDTO (blindando a entidade)
        return mapper.toDTO(faq);
    }

    public List<FaqResponseDTO> listarPorModulo(Integer moduloId) {
        // Listagem já convertendo para DTO
        return faqRepository.findByModuloId(moduloId).stream()
                .map(mapper::toDTO)
                .toList();
    }
}