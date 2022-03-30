package br.com.fiap.intellibe.model;

import android.util.Log;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import br.com.fiap.intellibe.util.UtilCnpjCpf;

@Entity
public class Cliente implements Serializable {
    @PrimaryKey
    private Long cnpjOuCpf;
    private String tipoCliente;
    private String nomeCliente;
    private String descricaoEmail;
    private String descricaoEndereco;
    private String complementoEndereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String pais;
    private String cep;
    private String telefoneFixo;
    private String telefoneCelular;
    private String telefoneComercial;
    private String caminhoFoto;

    public Cliente() {
    }

    public Cliente(Long cnpjOuCpf, String tipoCliente, String nomeCliente, String descricaoEmail, String descricaoEndereco,
                   String complementoEndereco, String bairro, String cidade, String estado, String pais,
                   String cep, String telefoneFixo, String telefoneCelular,
                   String telefoneComercial, String caminhoFoto) {
        this.cnpjOuCpf = cnpjOuCpf;
        this.tipoCliente = tipoCliente;
        this.nomeCliente = nomeCliente;
        this.descricaoEmail = descricaoEmail;
        this.descricaoEndereco = descricaoEndereco;
        this.complementoEndereco = complementoEndereco;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.pais = pais;
        this.cep = cep;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.telefoneComercial = telefoneComercial;
        this.caminhoFoto = caminhoFoto;
    }
    public Long getCnpjOuCpf() {
        return cnpjOuCpf;
    }

    public void setCnpjOuCpf(Long cnpjOuCpf) {
        this.cnpjOuCpf = cnpjOuCpf;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
//        Log.d("TipoCliente", "===========setTipoCliente: " + tipoCliente);
        this.tipoCliente = tipoCliente.toUpperCase();
//        if  (!(tipoCliente.equals("PJ"))  &&  !(tipoCliente.equals("PF"))  ) {
//            throw new DominioException("Tipo de Cliente deve ser 'PJ' ou  'PF'");
//        }
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getDescricaoEmail() {
        return descricaoEmail;
    }

    public void setDescricaoEmail(String descricaoEmail) {
        this.descricaoEmail = descricaoEmail;
    }

    public String getDescricaoEndereco() {
        return descricaoEndereco;
    }

    public void setDescricaoEndereco(String descricaoEndereco) {
        this.descricaoEndereco = descricaoEndereco;
    }

    public String getComplementoEndereco() {
        return complementoEndereco;
    }

    public void setComplementoEndereco(String complementoEndereco) {
        this.complementoEndereco = complementoEndereco;
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

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

//    public Integer checkTipoPessoa(String tipoPessoa)  {
//        tipoPessoa = tipoPessoa.toUpperCase();
//        if  (  !(tipoPessoa.equals("PJ"))  &&  !(tipoPessoa.equals("PF"))  ) {
//            throw new DominioException("Tipo de pessoa deve ser 'PJ' ou  'PF'");
//        }
//        return (tipoPessoa.equals("PF")  ? 1 : 2);
//    }

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
                ", tipoCliente='" + tipoCliente + '\'' +
                ", nomeCliente='" + nomeCliente + '\'' +
                ", descricaoEmail='" + descricaoEmail + '\'' +
                ", descricaoEndereco='" + descricaoEndereco + '\'' +
                ", complementoEndereco='" + complementoEndereco + '\'' +
                ", bairro='" + bairro + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                ", pais='" + pais + '\'' +
                ", cep='" + cep + '\'' +
                ", telefoneFixo='" + telefoneFixo + '\'' +
                ", telefoneCelular='" + telefoneCelular + '\'' +
                ", telefoneComercial='" + telefoneComercial + '\'' +
                ", caminhoFoto='" + caminhoFoto + '\'' +
                '}';
    }
}
