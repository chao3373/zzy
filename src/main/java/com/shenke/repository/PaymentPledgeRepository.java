package com.shenke.repository;

import com.shenke.Entity.PaymentPledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PaymentPledgeRepository extends JpaRepository<PaymentPledge, Long>, JpaSpecificationExecutor<PaymentPledge> {

}
