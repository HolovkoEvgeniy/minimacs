package com.lumiring.minimacs.services.test;

import com.lumiring.minimacs.domain.constant.Code;
import com.lumiring.minimacs.domain.response.Response;
import com.lumiring.minimacs.domain.response.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestServices {
    public ResponseEntity<Response> test(){
        throw CommonException.builder().code(Code.TEST).techMessage("techMessage").httpStatus(HttpStatus.BAD_REQUEST).build();
    }


}
