package com.ADNService.SWP391.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.ADNService.SWP391.entity.TestSample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestSampleRepository extends JpaRepository<TestSample, Long> {
}
