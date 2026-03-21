package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // O parâmetro 'username' aqui, no nosso caso, será o e-mail digitado no login
        UserDetails usuario = repository.findByEmail(username);
        
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }
        
        return usuario;
    }
}