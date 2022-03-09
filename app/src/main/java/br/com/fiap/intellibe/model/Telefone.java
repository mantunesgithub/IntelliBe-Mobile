package br.com.fiap.intellibe.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity
public class Telefone {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String numero;
    private TipoTelefone tipo;
    @ForeignKey(entity = Cliente.class,
            parentColumns = "cnpjOuCpf",
            childColumns = "clienteCnpjOuCpf",
            onUpdate = CASCADE,
            onDelete = CASCADE)
    private Long clienteCnpjOuCpf;


    public Telefone(String numero, TipoTelefone tipo) {
        this.numero = numero;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public TipoTelefone getTipo() {
        return tipo;
    }

    public void setTipo(TipoTelefone tipo) {
        this.tipo = tipo;
    }

    public Long getClienteCnpjOuCpf() {
        return clienteCnpjOuCpf;
    }

    public void setClienteCnpjOuCpf(Long clienteCnpjOuCpf) {
        this.clienteCnpjOuCpf = clienteCnpjOuCpf;
    }
}
