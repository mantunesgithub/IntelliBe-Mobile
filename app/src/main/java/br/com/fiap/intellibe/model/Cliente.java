package br.com.fiap.intellibe.model;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import br.com.fiap.intellibe.util.UtilCnpjCpf;

@Entity
public class Cliente implements Serializable {
    @PrimaryKey
    private Long cnpjOuCpf;
    private Integer tipoPessoa;
    private String nome;
    private String site;
    private String endereco;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String pais;
    private String cep;
    private String telefoneFixo;
    private String telefoneCelular;
    private String telefoneComercial;

    public Cliente() {
    }

    @Ignore
    public Cliente(Long cnpjOuCpf, Integer tipoPessoa, String nome, String site, String endereco,
                   String complemento, String bairro, String cidade, String estado, String pais,
                   String cep, String telefoneCelular, String telefoneFixo, String telefoneComercial){

        this.cnpjOuCpf = cnpjOuCpf;
        this.tipoPessoa = tipoPessoa;
        this.nome = nome;
        this.site = site;
        this.endereco = endereco;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.pais = pais;
        this.cep = cep;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.telefoneComercial = telefoneComercial;
    }

    public Long getCnpjOuCpf() {
        return cnpjOuCpf;
    }

    public void setCnpjOuCpf(Long cnpjOuCpf) {
        this.cnpjOuCpf = cnpjOuCpf;
    }

    public Integer getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(Integer tipoPessoa)  {
        this.tipoPessoa = tipoPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getTelefoneFixo() {
        return telefoneFixo;
    }
    public void setTelefoneFixo(String telefoneFixo) {
        this.telefoneFixo = telefoneFixo;
    }

    public String getTelefoneCelular() {
        return telefoneCelular;
    }

    public void setTelefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }

    public String getTelefoneComercial() {
        return telefoneComercial;
    }

    public void setTelefoneComercial(String telefoneComercial) {
        this.telefoneComercial = telefoneComercial;
    }

    public Integer checkTipoPessoa(String tipoPessoa)  {
        if  (  !(tipoPessoa.equals("PJ"))  &&  !(tipoPessoa.equals("PF"))  ) {
            throw new DominioException("Tipo de pessoa deve ser 'PJ' ou  'PF'");
        }
        return (tipoPessoa.equals("PF")  ? 1 : 2);
    }

     //  Verifica se cnpj ou cpf válido e Formata como String
     public Long checkCnpjCpf(String cnpjOuCpf) {
        try {
            String cnpjOuCpfformatado = UtilCnpjCpf.formatCPForCPNJ(cnpjOuCpf, true);

            //  converte cnpj ou cpf para  Long
            return UtilCnpjCpf.formatCPForCPNJ(cnpjOuCpfformatado);

        } catch (IllegalArgumentException e) {
            throw new DominioException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "cnpjOuCpf=" + cnpjOuCpf +
                ", tipoPessoa=" + tipoPessoa +
                ", nome='" + nome + '\'' +
                ", email='" + site + '\'' +
                ", endereco='" + endereco + '\'' +
                ", complemento='" + complemento + '\'' +
                ", bairro='" + bairro + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                ", pais='" + pais + '\'' +
                ", cep='" + cep + '\'' +
                ", telefoneFixo='" + telefoneFixo + '\'' +
                ", telefoneCelular='" + telefoneCelular + '\'' +
                ", telefoneComercial='" + telefoneComercial + '\'' +
                '}';
    }
}
