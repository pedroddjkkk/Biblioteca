package com.teste.elotech.mapper;

import com.teste.elotech.dto.CreateBookDTO;
import com.teste.elotech.dto.UpdateBookDTO;
import com.teste.elotech.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book toEntity(CreateBookDTO createBookDTO);
    Book toEntity(UpdateBookDTO updateBookDTO);
}