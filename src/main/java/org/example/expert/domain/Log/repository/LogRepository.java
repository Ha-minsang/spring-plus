package org.example.expert.domain.Log.repository;

import org.example.expert.domain.Log.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long>
{
}
