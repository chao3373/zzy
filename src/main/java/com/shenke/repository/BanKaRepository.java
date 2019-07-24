package com.shenke.repository;

import com.shenke.Entity.BanKa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BanKaRepository extends JpaRepository<BanKa, Long>, JpaSpecificationExecutor<BanKa> {

}
