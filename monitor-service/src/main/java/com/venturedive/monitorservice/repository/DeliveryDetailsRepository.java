package com.venturedive.monitorservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.venturedive.monitorservice.entity.DeliveryDetail;
import com.venturedive.monitorservice.entity.DeliveryStatus;

@Repository
public interface DeliveryDetailsRepository extends JpaRepository<DeliveryDetail, Integer>{

	List<DeliveryDetail> findAllByDeliveryStatusNot(DeliveryStatus deliveryStatus);
}