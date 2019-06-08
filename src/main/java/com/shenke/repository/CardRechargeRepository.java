package com.shenke.repository;

import com.shenke.Entity.CardRecharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CardRechargeRepository extends JpaRepository<CardRecharge, Long>, JpaSpecificationExecutor<CardRecharge> {

}
