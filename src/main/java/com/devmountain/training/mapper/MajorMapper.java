package com.devmountain.training.mapper;

import com.devmountain.training.entity.Major;
import com.devmountain.training.model.MajorDto;
import org.mapstruct.Mapper;

@Mapper
public interface MajorMapper {
    MajorDto majorToMajorDto(Major major);

    Major majorDtoToMajor(MajorDto majorDto);
}
