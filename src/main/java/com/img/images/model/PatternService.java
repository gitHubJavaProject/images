package com.img.images.model;

import com.img.images.mapper.PatternMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatternService {

    @Autowired
    private PatternMapper patternMapper;

    public List<Pattern> findAll() {
        return patternMapper.findAll();
    }
}
