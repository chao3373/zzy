package com.shenke.repository;

import com.shenke.Entity.Charges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChargesRepository extends JpaRepository<Charges, Long>, JpaSpecificationExecutor<Charges> {

}
