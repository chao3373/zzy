package com.shenke.repository;

import com.shenke.Entity.PaymentMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PaymentMessageRepository extends JpaRepository<PaymentMessage, Long>, JpaSpecificationExecutor<PaymentMessage> {

}
