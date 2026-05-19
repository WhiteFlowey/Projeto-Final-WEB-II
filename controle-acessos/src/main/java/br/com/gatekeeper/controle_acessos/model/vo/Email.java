package br.com.gatekeeper.controle_acessos.model.vo;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable // Avisa ao JPA que os campos desta classe farão parte da tabela da Entidade que a usar. Usamos isso para não preciar criar uma nova tabela no banco de dados.

public class Email {

    @Column(name = "email") // Mantém o vínculo com a coluna 'email' que já existe no banco
    private String endereco;

    // O Hibernate exige um construtor vazio padrão para conseguir reconstruir o objeto ao buscar no banco
    protected Email() {}

    public Email(String endereco) {
        // Validação estrita: se for nulo ou não passar na Regex de e-mail, o sistema barra na hora
        if (endereco == null || !endereco.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            throw new IllegalArgumentException("Formato de e-mail inválido!");
        }
        this.endereco = endereco;
    }

    public String getEndereco() {
        return endereco;
    }

    // VOs são comparados pelos seus valores, e não por IDs. Por isso, equals e hashCode são obrigatórios!
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(endereco, email.endereco);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endereco);
    }
}