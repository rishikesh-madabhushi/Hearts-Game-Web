package com.llwantedll.webhearts.models.dtolayer.converter;

public interface DTOConverter<E, D> {
    E forward(D dto);

    D backward(E entity);
}
