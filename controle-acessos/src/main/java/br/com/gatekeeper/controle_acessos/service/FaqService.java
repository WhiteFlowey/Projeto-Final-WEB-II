package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.request.FaqRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.FaqResponseDTO;
import br.com.gatekeeper.controle_acessos.mapper.FaqMapper;
import br.com.gatekeeper.controle_acessos.model.FAQ;
import br.com.gatekeeper.controle_acessos.model.Modulo;
import br.com.gatekeeper.controle_acessos.repository.FaqRepository;
import br.com.gatekeeper.controle_acessos.repository.ModuloRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FaqService {

    private final FaqRepository faqRepository;
    private final ModuloRepository moduloRepository;
    private final FaqMapper mapper;

    FaqService(FaqRepository faqRepository, FaqMapper mapper, ModuloRepository moduloRepository) {
        this.faqRepository = faqRepository;
        this.mapper = mapper;
        this.moduloRepository = moduloRepository;
    } // Injetando o mapper que criamos

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

    @Transactional
    public FaqResponseDTO atualizarFaq(Integer id, FaqRequestDTO dto) {
        // 1. Busca o FAQ pelo ID dele mesmo
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ não encontrado com o ID: " + id));

        // 2. Atualiza apenas a pergunta e a resposta (o módulo continua o mesmo)
        faq.setPergunta(dto.getPergunta());
        faq.setResposta(dto.getResposta());

        // 3. Salva e converte para DTO
        faq = faqRepository.save(faq);
        return mapper.toDTO(faq);
    }

    @Transactional
    public void deletarFaq(Integer id) {
        // 1. Verifica se o FAQ existe
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ não encontrado com o ID: " + id));

        // 2. Deleta do banco
        faqRepository.delete(faq);
    }
}