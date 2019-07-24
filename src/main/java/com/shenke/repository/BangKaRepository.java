package com.shenke.repository;

import com.shenke.Entity.BangKa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BangKaRepository extends JpaRepository<BangKa, Long>, JpaSpecificationExecutor<BangKa> {

}
