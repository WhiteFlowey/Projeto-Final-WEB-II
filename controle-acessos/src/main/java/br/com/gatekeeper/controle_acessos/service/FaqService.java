package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.model.FAQ;
import br.com.gatekeeper.controle_acessos.model.Modulo;
import br.com.gatekeeper.controle_acessos.repository.FaqRepository;
import br.com.gatekeeper.controle_acessos.repository.ModuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FaqService {

    @Autowired private FaqRepository faqRepository;
    @Autowired private ModuloRepository moduloRepository;

    public FAQ criarFaq(FAQ faq, Integer moduloId) {
        Modulo modulo = moduloRepository.findById(moduloId)
                .orElseThrow(() -> new RuntimeException("Módulo não encontrado"));
        faq.setModulo(modulo);
        return faqRepository.save(faq);
    }

    public List<FAQ> listarPorModulo(Integer moduloId) {
        return faqRepository.findByModuloId(moduloId);
    }
}