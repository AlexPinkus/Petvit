package com.alexpinkus.petvit.db;

/**
 * Created by Rodrigo on 19/04/17.
 */

public class Raza {

    private int id;
    private int tipo_raza;
    private String nombre_raza;
    private String peso_adulto;
    private String altura_adulto;
    private String descripcion;
    private String esperanza;
    private String origen;
    private String historia;
    private String foto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipo_raza() {
        return tipo_raza;
    }

    public void setTipo_raza(int tipo_raza) {
        this.tipo_raza = tipo_raza;
    }

    public String getNombre_raza() {
        return nombre_raza;
    }

    public void setNombre_raza(String nombre_raza) {
        this.nombre_raza = nombre_raza;
    }

    public String getPeso_adulto() {
        return peso_adulto;
    }

    public void setPeso_adulto(String peso_adulto) {
        this.peso_adulto = peso_adulto;
    }

    public String getAltura_adulto() {
        return altura_adulto;
    }

    public void setAltura_adulto(String altura_adulto) {
        this.altura_adulto = altura_adulto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEsperanza() {
        return esperanza;
    }

    public void setEsperanza(String esperanza) {
        this.esperanza = esperanza;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getHistoria() {
        return historia;
    }

    public void setHistoria(String historia) {
        this.historia = historia;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Raza(int id, int tipo_raza, String nombre_raza, String peso_adulto, String altura_adulto, String descripcion, String esperanza, String origen, String historia, String foto) {
        this.id = id;
        this.tipo_raza = tipo_raza;
        this.nombre_raza = nombre_raza;
        this.peso_adulto = peso_adulto;
        this.altura_adulto = altura_adulto;
        this.descripcion = descripcion;
        this.esperanza = esperanza;
        this.origen = origen;
        this.historia = historia;
        this.foto = foto;
    }
}
