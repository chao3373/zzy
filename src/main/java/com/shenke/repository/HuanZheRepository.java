package com.shenke.repository;

import com.shenke.Entity.HuanZhe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HuanZheRepository extends JpaRepository<HuanZhe, Long>, JpaSpecificationExecutor<HuanZhe> {

}
