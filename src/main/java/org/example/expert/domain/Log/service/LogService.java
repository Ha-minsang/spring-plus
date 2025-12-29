package org.example.expert.domain.Log.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.Log.entity.Log;
import org.example.expert.domain.Log.repository.LogRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;


    public void saveLog(Long userId, Long todoId) {
        Log log = new Log(userId, todoId);
        logRepository.save(log);
    }
}
