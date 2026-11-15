package com.sigg.fazendadigital.domain.service;

public class OrdenhaDuplicadaException extends RuntimeException {
    public OrdenhaDuplicadaException() {
        super("Este animal já possui uma ordenha cadastrada para esta data e turno.");
    }
}
